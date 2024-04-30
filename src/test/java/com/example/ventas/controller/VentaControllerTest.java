package com.example.ventas.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.EntityModel;
import org.springframework.test.web.servlet.MockMvc;

import com.example.ventas.model.Venta;
import com.example.ventas.service.VentaService;

@WebMvcTest(VentaController.class)
public class VentaControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VentaService ventaServiceMock;

    @Test
    public void obtenerTodosTest() throws Exception {
        Venta venta1 = new Venta();
        venta1.setVendedor("John");
        venta1.setIdVenta(1L);
        Venta venta2 = new Venta();
        venta2.setVendedor("Doris");
        venta2.setIdVenta(2L);
        List<Venta> ventas = List.of(venta1, venta2);

        List<EntityModel<Venta>> ventasResources = ventas.stream()
            .map(venta -> EntityModel.of(venta))
            .collect(Collectors.toList());

        when(ventaServiceMock.getAllVentas()).thenReturn(ventas);

        mockMvc.perform(get("/ventas"))
                .andExpect(status().isOk());
                // Here, use direct JSON path matching without Matchers
                /* .andExpect(jsonPath("$._embedded.ventas.length()").value(2))
                .andExpect(jsonPath("$._embedded.ventas[0].vendedor").value("John"))
                .andExpect(jsonPath("$._embedded.ventas[1].vendedor").value("Doris"))
                .andExpect(jsonPath("$._embedded.ventas[0]._links.self.href").value("http://localhost:8080/ventas/1"))
                .andExpect(jsonPath("$._embedded.ventas[1]._links.self.href").value("http://localhost:8080/ventas/2"));*/
    }
}
