package com.trainingfinance.apisystem.entity.id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentScoreID implements Serializable {
    private Long regisClass;
    private Long componentSubject;

}
