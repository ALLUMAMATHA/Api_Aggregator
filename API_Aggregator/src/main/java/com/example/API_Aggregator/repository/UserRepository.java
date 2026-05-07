package com.example.API_Aggregator.repository;

import com.example.API_Aggregator.entity.OrderEntity;
import com.example.API_Aggregator.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    //List<UserEntity> findByUserId(Integer id);
}