package com.trainingfinance.apisystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.trainingfinance.apisystem.dto.TargetsDto;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;


@Data
@Entity
@Table(name = "targets")
public class TargetsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "targets_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "quantity")
    private Double quantity;

    @Column(name = "description")
    private String description;

    @Column(name = "status")
    private Boolean status;

    @JsonIgnore
    @OneToMany(mappedBy = "targets", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TeachingPeriodEntity> teaching_period;

    public static TargetsEntity from(TargetsDto targetsDto){
        TargetsEntity targets = new TargetsEntity();
        targets.setName(targetsDto.getName());
        targets.setQuantity(targetsDto.getQuantity());
        targets.setDescription(targetsDto.getDescription());
        targets.setStatus(targetsDto.getStatus());
        return targets;
    }
}
