package com.example.API_Aggregator.repository;

import com.example.API_Aggregator.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<PaymentEntity, Integer> {

    List<PaymentEntity> findByUserId(Integer userId);
}