package com.example.ventas.service;

import java.util.List;
import java.util.Optional;

import com.example.ventas.model.Venta;

public interface VentaService {

    List<Venta> getAllVentas();
    Optional<Venta> getVentaById(Long id);

    Venta createVenta(Venta venta);
    Venta updateVenta(Long id, Venta venta);
    void deleteVenta(Long id);
    
}
