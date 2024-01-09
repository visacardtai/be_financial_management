package com.trainingfinance.apisystem.dto;

import com.trainingfinance.apisystem.entity.Lecturer;
import com.trainingfinance.apisystem.entity.TeachingPeriodEntity;
import lombok.Data;

import java.text.DecimalFormat;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class TeachingPeriodDto {
    private Long id;
    private String name;
    private Long lecturer_id;
    private SemesterDto semester;
    private LecturePriceDto lecturePrice;
    private String qualification;
    private String position;
    private TargetsDto targets;
    private List<TeachingDetailsDto> details;
    private Double total;
    private Double calculate;
    private Double total_price;
    private Double tien_troi;

    public static TeachingPeriodDto from(TeachingPeriodEntity teachingPeriodEntity){
        TeachingPeriodDto teachingPeriodDto = new TeachingPeriodDto();
        teachingPeriodDto.setId(teachingPeriodEntity.getId());
        teachingPeriodDto.setSemester(SemesterDto.from(teachingPeriodEntity.getSemester()));
        teachingPeriodDto.setLecturePrice(LecturePriceDto.from(teachingPeriodEntity.getLecture_price()));
        teachingPeriodDto.setTargets(TargetsDto.from(teachingPeriodEntity.getTargets()));
        teachingPeriodDto.setName(teachingPeriodEntity.getLecturer().getProfile().getFullname());
        teachingPeriodDto.setLecturer_id(teachingPeriodEntity.getLecturer().getLecturerId());
        teachingPeriodDto.setQualification(teachingPeriodEntity.getLecturer().getProfile().getQualification());
        teachingPeriodDto.setPosition(teachingPeriodEntity.getLecturer().getPosition().getName());
        teachingPeriodDto.setDetails(teachingPeriodEntity.getTeaching_details()
                .stream()
                .map(TeachingDetailsDto::from)
                .collect(Collectors.toList()));
        teachingPeriodDto.setTotal(sTotal(teachingPeriodDto.getDetails()));
        teachingPeriodDto.setCalculate(sCalculate(teachingPeriodDto.getDetails()));
        teachingPeriodDto.setTotal_price(totalPrice(teachingPeriodDto.getDetails(),teachingPeriodDto.getTargets(),teachingPeriodDto.getLecturePrice()));
        teachingPeriodDto.setTien_troi(tien(teachingPeriodDto.getDetails(),teachingPeriodDto.getTargets(),teachingPeriodDto.getLecturePrice()));
        return teachingPeriodDto;
    }
    public static Double sTotal(List<TeachingDetailsDto> teachingDetailsDtoList) {
        double sum= 0;
        for (TeachingDetailsDto teachingDetailsDto: teachingDetailsDtoList) {
            sum+= (teachingDetailsDto.getTheory_total()*teachingDetailsDto.getClassCoefficient().getCoefficient()) + (teachingDetailsDto.getPractical_total()*0.5);
        }
        DecimalFormat df = new DecimalFormat("#.##");
        double result = Double.parseDouble(df.format(sum));
        return result;
    }
    public static Double sCalculate(List<TeachingDetailsDto> teachingDetailsDtoList) {
        double sum= 0;
        for (TeachingDetailsDto teachingDetailsDto: teachingDetailsDtoList) {
            sum+= teachingDetailsDto.getTheory_total()*teachingDetailsDto.getClassCoefficient().getCoefficient() + teachingDetailsDto.getPractical_total()*0.5;
        }
        DecimalFormat df = new DecimalFormat("#.##");
        double result = Double.parseDouble(df.format(sum));
        return result;
    }

    public static Double totalPrice(List<TeachingDetailsDto> teachingDetailsDtoList, TargetsDto targets, LecturePriceDto lecturePrice) {
        double sum= 0;
        for (TeachingDetailsDto teachingDetailsDto: teachingDetailsDtoList) {
            sum+= (teachingDetailsDto.getTheory_total()*teachingDetailsDto.getClassCoefficient().getCoefficient()) + (teachingDetailsDto.getPractical_total()*0.5);
        }
        System.out.println(sum);
        double thieu;
        double troi;
        double binh_thuong;
        if (targets.getQuantity() <= sum) {
            thieu = 0;
            troi = sum - targets.getQuantity();
            binh_thuong = targets.getQuantity();
        } else {
            thieu = targets.getQuantity() - sum;
            troi = 0;
            binh_thuong = sum;
        }
        System.out.println(thieu);
        System.out.println(troi);
        System.out.println(binh_thuong);
        double spTotal = binh_thuong*lecturePrice.getBasic_price() + troi*lecturePrice.getBasic_price()*lecturePrice.getCoefficient() - thieu*lecturePrice.getBasic_price()*lecturePrice.getCoefficient();
        DecimalFormat df = new DecimalFormat("#.##");
        double result = Double.parseDouble(df.format(spTotal));
        return result;
    }

    public static Double tien(List<TeachingDetailsDto> teachingDetailsDtoList, TargetsDto targets, LecturePriceDto lecturePrice) {
        double sum= 0;
        for (TeachingDetailsDto teachingDetailsDto: teachingDetailsDtoList) {
            sum+= (teachingDetailsDto.getTheory_total()*teachingDetailsDto.getClassCoefficient().getCoefficient()) + (teachingDetailsDto.getPractical_total()*0.5);
        }
        double thieu;
        double troi;
        double binh_thuong;
        if (targets.getQuantity() <= sum) {
            thieu = 0;
            troi = sum - targets.getQuantity();
            binh_thuong = targets.getQuantity();
        } else {
            thieu = targets.getQuantity() - sum;
            troi = 0;
            binh_thuong = sum;
        }
        System.out.println(thieu);
        System.out.println(troi);
        System.out.println(binh_thuong);
        double spTotal = troi*lecturePrice.getBasic_price()*lecturePrice.getCoefficient() - thieu*lecturePrice.getBasic_price()*lecturePrice.getCoefficient();
        DecimalFormat df = new DecimalFormat("#.##");
        double result = Double.parseDouble(df.format(spTotal));
        return result;
    }
}
