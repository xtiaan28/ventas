package com.example.ventas.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.ventas.model.Venta;
import com.example.ventas.repository.VentaRepository;

@ExtendWith(MockitoExtension.class)
public class VentaServiceTest {
    @InjectMocks
    private VentaServiceImpl ventaServicio;

    @Mock
    private VentaRepository ventaRepositoryMock;

    @Test
    public void guardarVentaTest() {

        Venta venta = new Venta();
        venta.setVendedor("Jose");

        when(ventaRepositoryMock.save(any())).thenReturn(venta);

        Venta resultado = ventaServicio.createVenta(venta);

        assertEquals("Jose", resultado.getVendedor());
    }
    public void editaVentaTest(Long idVenta){
        Venta venta = new Venta();
        venta.setVendedor("Pablo");
        when(ventaRepositoryMock.findById(idVenta));
        Venta resultado = ventaServicio.updateVenta(idVenta, venta);
        assertEquals("Pablo", resultado.getVendedor());
    }

}
