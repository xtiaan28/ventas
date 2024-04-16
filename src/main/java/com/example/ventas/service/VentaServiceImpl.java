package com.example.ventas.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ventas.model.Venta;
import com.example.ventas.repository.VentaRepository;

@Service
public class VentaServiceImpl implements VentaService {
    @Autowired
    private VentaRepository ventaRepository;

    @Override
    public List<Venta> getAllVentas() {
        return ventaRepository.findAll();
    }

    @Override
    public Optional<Venta> getVentaById(Long id) {
        return ventaRepository.findById(id);
    }
    @Override
    public Venta createVenta(Venta venta) {

        return ventaRepository.save(venta);
    }

    @Override
    public Venta updateVenta(Long id, Venta venta) {
        if(ventaRepository.existsById(id)){
            venta.setIdVenta(id);
            return ventaRepository.save(venta);
        }else{
            return null;
        }
    }
    public void deleteVenta(Long id){
        ventaRepository.deleteById(id);
    }
}
