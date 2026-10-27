package com.example.orderingsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.orderingsystem.entity.Menu;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {
}