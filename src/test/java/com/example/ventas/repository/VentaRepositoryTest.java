package com.example.ventas.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.example.ventas.model.Venta;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class VentaRepositoryTest {
    @Autowired
    private VentaRepository ventaRepository;

    @Test
    public void guardarVentaTest() {
        Venta venta = new Venta();
        venta.setVendedor("John");

        Venta resultado = ventaRepository.save(venta);

        assertNotNull(resultado.getIdVenta());
        assertEquals("John", resultado.getVendedor());
    }

    @Test
    public void editaVentaTest(Long idVenta){
        Venta venta = new Venta();
        venta.setVendedor("Pablo");

        Venta resultado = ventaRepository.save(venta);
        assertNotNull(resultado.getIdVenta());
        assertEquals("Pablo", resultado.getVendedor());
    }

}
