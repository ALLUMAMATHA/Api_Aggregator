package com.example.API_Aggregator.repository;

import com.example.API_Aggregator.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<OrderEntity, Integer> {

    List<OrderEntity> findByUserId(Integer userId);
}