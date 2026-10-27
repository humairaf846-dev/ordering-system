package com.example.orderingsystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.orderingsystem.entity.Pesanan;

@Repository
public interface PesananRepository extends JpaRepository<Pesanan, Long> {
    List<Pesanan> findByPelangganId(Long pelangganId);
}