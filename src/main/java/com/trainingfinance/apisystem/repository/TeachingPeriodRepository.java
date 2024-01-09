package com.trainingfinance.apisystem.repository;
import com.trainingfinance.apisystem.entity.TeachingPeriodEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeachingPeriodRepository extends JpaRepository<TeachingPeriodEntity,Long> {
    @Query(value ="SELECT * FROM teaching_period WHERE lecturer_id = :lecturerId", nativeQuery = true)
    List<TeachingPeriodEntity> findAllByIdLecture(@Param("lecturerId")Long lecturerId);

    @Query(value ="SELECT * FROM teaching_period WHERE status = :status AND is_used = :is_used", nativeQuery = true)
    List<TeachingPeriodEntity> findAllByStatus(@Param("status")boolean status,
                                               @Param("is_used")boolean is_used);
    @Query(value ="SELECT * FROM teaching_period WHERE lecturer_id = :lecturerId AND teaching_period_id = (SELECT MAX(teaching_period_id) FROM teaching_period WHERE lecturer_id = :lecturerId)", nativeQuery = true)
    TeachingPeriodEntity findNewByIdLecture(@Param("lecturerId")Long lecturerId);

    @Modifying
    @Transactional
    @Query(value ="DELETE FROM teaching_period WHERE teaching_period_id = :teaching_period_id", nativeQuery = true)
    Integer deleteByEID(@Param("teaching_period_id")long teaching_period_id);

    @Query(value ="SELECT * FROM teaching_period WHERE status = true AND is_used = true AND semester_id = :semester_id", nativeQuery = true)
    List<TeachingPeriodEntity> findBySemesterId(@Param("semester_id")long semester_id);
}
