package com.trainingfinance.apisystem.service;

import com.trainingfinance.apisystem.entity.InvoiceEntity;
import com.trainingfinance.apisystem.entity.Student;
import com.trainingfinance.apisystem.entity.TargetsEntity;
import com.trainingfinance.apisystem.exception.NotFoundException;
import com.trainingfinance.apisystem.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class StudentService {
    @Autowired
    StudentRepository studentRepository;

    public Student getItem(Long id) {
        return studentRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    public List<Student> getAllStudent(){
        return StreamSupport
                .stream(studentRepository.findAll().spliterator(),false)
                .collect(Collectors.toList());
    }
//    @Transactional
//    public Student addItemToCart(Long cartId, Long itemId){
//        Cart cart = getCart(cartId);
//        Item item = itemService.getItem(itemId);
//        if(Objects.nonNull(item.getCart())){
//            throw new ItemIsAlreadyAssignedException(itemId,
//                    item.getCart().getId());
//        }
//        cart.addItem(item);
//        item.setCart(cart);
//        return cart;
//    }
}
