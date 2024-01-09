package com.trainingfinance.apisystem.service;

import com.trainingfinance.apisystem.entity.LecturePriceEntity;
import com.trainingfinance.apisystem.entity.TargetsEntity;
import com.trainingfinance.apisystem.exception.NotFoundException;
import com.trainingfinance.apisystem.repository.LecturePriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class LecturePriceService {
    @Autowired
    LecturePriceRepository lecturePriceRepository;

    public List<LecturePriceEntity> getAllLecturePrice(){
        return StreamSupport
                .stream(lecturePriceRepository.findAll().spliterator(),false)
                .collect(Collectors.toList());
    }

    public LecturePriceEntity add(LecturePriceEntity lecturePrice) {
        return  lecturePriceRepository.save(lecturePrice);
    }

    public List<LecturePriceEntity> getByStatus(boolean status) {
        return StreamSupport
                .stream(lecturePriceRepository.findByStatus(status).spliterator(),false)
                .collect(Collectors.toList());
    }

    public LecturePriceEntity getItem(Long id) {
        return lecturePriceRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    public LecturePriceEntity getByName(String name) {
        return lecturePriceRepository.findByName(name);
    }

    public LecturePriceEntity deleteItem(Long id){
        LecturePriceEntity item = getItem(id);
        lecturePriceRepository.delete(item);
        return item;
    }

    @Transactional
    public void editItem(Long id){
        LecturePriceEntity itemToEdit = getItem(id);
        itemToEdit.setStatus(true);
        lecturePriceRepository.save(itemToEdit);
    }
}
