package com.trainingfinance.apisystem.controller;

import com.google.gson.JsonObject;
import com.trainingfinance.apisystem.config.Config;
import com.trainingfinance.apisystem.dto.TransactionStatusDto;
import com.trainingfinance.apisystem.entity.HistoryPaymentEntity;
import com.trainingfinance.apisystem.entity.InvoiceEntity;
import com.trainingfinance.apisystem.service.HistoryPaymentService;
import com.trainingfinance.apisystem.service.InvoiceService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import com.google.gson.Gson;
@RestController
@RequestMapping("/api/v1/public/student")
public class PaymentController {
    @Autowired
    InvoiceService invoiceService;
    @Autowired
    HistoryPaymentService historyPaymentService;
    @GetMapping("/pay")
    public String getPay(@RequestParam("invoiceId") Long invoiceId,
                         @RequestParam("amount") Long amount_res)
            throws UnsupportedEncodingException {
        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String orderType = "250000";
        long amount = amount_res*100;
        String bankCode = "NCB";
        String vnp_TxnRef = Config.getRandomNumber(8);
        String vnp_IpAddr = "127.0.0.1";
        String vnp_TmnCode = Config.vnp_TmnCode;

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_CurrCode", "VND");

        if (bankCode != null && !bankCode.isEmpty()) {
            vnp_Params.put("vnp_BankCode", bankCode);
        }
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + vnp_TxnRef);
        vnp_Params.put("vnp_OrderType", orderType);

        String locate = null;
        if (locate != null && !locate.isEmpty()) {
            vnp_Params.put("vnp_Locale", locate);
        } else {
            vnp_Params.put("vnp_Locale", "vn");
        }
        vnp_Params.put("vnp_ReturnUrl", Config.vnp_ReturnUrl+ "/" + invoiceId);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));

        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());

        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        List fieldNames = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                //Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = Config.hmacSHA512(Config.secretKey, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = Config.vnp_PayUrl + "?" + queryUrl;

        HistoryPaymentEntity historyPayment = historyPaymentService.getByIdInvoice(invoiceId);
        if(historyPayment == null){
            InvoiceEntity invoice = invoiceService.getInvoice(invoiceId);
            if (invoice == null) {
                // Xử lý khi không tìm thấy Invoice với invoiceId tương ứng
                return String.valueOf(new ResponseEntity<>(HttpStatus.NOT_FOUND));
            }
            HistoryPaymentEntity historyP = new HistoryPaymentEntity();
            historyP.setStatus(false);
            historyP.setPrice(Double.valueOf(amount_res));
            historyP.setVnp_TxnRef(vnp_TxnRef);
            historyP.setInvoice(invoice);
            historyPaymentService.addItem(historyP);
        } else {
            historyPayment.setVnp_TxnRef(vnp_TxnRef);
            historyPayment.setPrice(Double.valueOf(amount_res));
            historyPaymentService.editItem(historyPayment.getId(),historyPayment);
        }
        return paymentUrl;
    }

    @GetMapping("/vnpay_jsp/vnpay_return")
    public ResponseEntity<?> transaction(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
        var message = "";
        try
        {
            Map fields = new HashMap();
            for (Enumeration params = request.getParameterNames(); params.hasMoreElements();) {
                String fieldName = URLEncoder.encode((String) params.nextElement(), StandardCharsets.US_ASCII.toString());
                String fieldValue = URLEncoder.encode(request.getParameter(fieldName), StandardCharsets.US_ASCII.toString());
                if ((fieldValue != null) && (fieldValue.length() > 0)) {
                    fields.put(fieldName, fieldValue);
                }
            }

            // Get vnp_SecureHash and invoiceId, convert vnp_PayDate;
            String vnp_SecureHash = request.getParameter("vnp_SecureHash");
            String vnp_PayDate = request.getParameter("vnp_PayDate");
            Long invoiceId = Long.valueOf(request.getParameter("invoiceId"));
            String vnp_TxnRef = request.getParameter("vnp_TxnRef");
            Object amount = fields.get("vnp_Amount");
            Double vnp_Amount = Double.parseDouble(amount.toString())/100;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            Date dateObject = dateFormat.parse(vnp_PayDate);


            if (fields.containsKey("vnp_SecureHashType"))
            {
                fields.remove("vnp_SecureHashType");
            }
            if (fields.containsKey("vnp_SecureHash"))
            {
                fields.remove("vnp_SecureHash");
            }
            if (fields.containsKey("invoiceId"))
            {
                fields.remove("invoiceId");
            }

            // Check checksum
            String signValue = Config.hashAllFields(fields);

            if (signValue.equals(vnp_SecureHash))
            {
                boolean checkOrderId = false; // vnp_TxnRef exists in your database
                boolean checkAmount = false; // vnp_Amount is valid (Check vnp_Amount VNPAY returns compared to the amount of the code (vnp_TxnRef) in the Your database).
                boolean checkOrderStatus = false; // PaymnentStatus = 0 (pending)

                // get data from database
                HistoryPaymentEntity historyPayment = historyPaymentService.getByIdInvoice(invoiceId);
                InvoiceEntity invoice = invoiceService.getInvoice(invoiceId);
                if(historyPayment != null){
                    if (vnp_TxnRef.equals(historyPayment.getVnp_TxnRef())){
                        checkOrderId = true;
                    }
                    System.out.println(vnp_Amount);
                    System.out.println(historyPayment.getPrice());
                    if (vnp_Amount.equals(historyPayment.getPrice())){
                        checkAmount = true;
                    }
                    if (!historyPayment.getStatus()){
                        checkOrderStatus = true;
                    }
                }
                if(checkOrderId)
                {
                    if(checkAmount)
                    {
                        if (checkOrderStatus)
                        {
                            if ("00".equals(request.getParameter("vnp_ResponseCode")))
                            {
                                //Here Code update PaymnentStatus = 1 into your Database
                                historyPayment.setStatus(true);
                                historyPaymentService.editItem(historyPayment.getId(),historyPayment);
                                invoice.setDate_of_payment(dateObject);
                                invoice.setStatus(true);
                                invoiceService.editInvoice(invoiceId,invoice);
                            }
                            else
                            {
                                // Here Code update PaymnentStatus = 2 into your Database
                            }
                            System.out.println("{\"RspCode\":\"00\",\"Message\":\"Confirm Success\"}");
                            Map<String, Object> data = new HashMap<>();
                            data.put("id", invoice.getStudent().getStudentId());
                            data.put("name", invoice.getStudent().getProfile().getFullname());
                            data.put("price", vnp_Amount);
                            return new ResponseEntity<>(data,HttpStatus.OK);
                        }
                        else
                        {
                            System.out.println("{\"RspCode\":\"02\",\"Message\":\"Order already confirmed\"}");
                            message = "{\"RspCode\":\"02\",\"Message\":\"Order already confirmed\"}";
                            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
                        }
                    }
                    else
                    {
                        System.out.println("{\"RspCode\":\"04\",\"Message\":\"Invalid Amount\"}");
                        message = "{\"RspCode\":\"04\",\"Message\":\"Invalid Amount\"}";
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
                    }
                }
                else
                {
                    System.out.println("{\"RspCode\":\"01\",\"Message\":\"Order not Found\"}");
                    message = "{\"RspCode\":\"01\",\"Message\":\"Order not Found\"}";
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
                }
            }
            else
            {
                System.out.println("{\"RspCode\":\"97\",\"Message\":\"Invalid Checksum\"}");
                message = "\"{\\\"RspCode\\\":\\\"97\\\",\\\"Message\\\":\\\"Invalid Checksum\\\"}\"";
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
            }
        }
        catch(Exception e)
        {
            System.out.println("{\"RspCode\":\"99\",\"Message\":\"Unknow error\"}");
            message = "\"{\\\"RspCode\\\":\\\"99\\\",\\\"Message\\\":\\\"Unknow error\\\"}\"";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
        }
    }
}

