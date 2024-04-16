package com.example.ventas.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ventas.model.Venta;

public interface VentaRepository extends JpaRepository<Venta, Long>{
    
}
