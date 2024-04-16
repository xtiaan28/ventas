package com.example.ventas.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

import java.time.ZoneId;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.ventas.model.DetalleVenta;
import com.example.ventas.model.Venta;
import com.example.ventas.service.VentaService;

@RestController
@RequestMapping("/ventas")
public class VentaController {
    private static final Logger log = LoggerFactory.getLogger(VentaController.class);
    DecimalFormat formato = new DecimalFormat("$#,###,###");
    @Autowired
    private VentaService ventaService;

    @GetMapping
    public List<Venta> getVentas(){
        log.info("GET/ventas");
        log.info("Retornando todos las ventas");
        return ventaService.getAllVentas();
    }

    @GetMapping("/{id}")
    public ResponseEntity <Object> getVentasById(@PathVariable Long id) {
        Optional<Venta> venta = ventaService.getVentaById(id);
        if(venta.isEmpty()){
            log.error("No se encontro venta con ID {}", id);
            
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("No se encontro venta con ID " + id));
            //return ResponseEntity.notFound().build();
        }
        log.info("Venta encontrada con exito");
        return ResponseEntity.ok(ventaService.getVentaById(id));
    }

    @GetMapping("/gananciasDiarias/{fecha}")
    public ResponseEntity<Object> getVentasDiarias(@PathVariable String fecha){
        List<Venta> ventas = ventaService.getAllVentas();
        long totalGananciaDiaria = 0;
        long precioVenta = 0;
        long precioCosto = 0;
        long cantidad = 0;
        long ganancia = 0;
        for (Venta venta : ventas) {
            Date fecha_venta = venta.getFechaVenta();
            LocalDate localDate = fecha_venta.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            log.info(fecha);
            log.info("-----------------------------------------------------------");
            log.info(localDate.toString());
            if(localDate.toString().equals(fecha)){
                List<DetalleVenta> detalleVenta = venta.getDetalleVentas();
                for(DetalleVenta detalleTemp : detalleVenta){
                    precioVenta = detalleTemp.getPrecio();
                    precioCosto = detalleTemp.getPrecioCosto();
                    cantidad = detalleTemp.getCantidad();
                    ganancia = (precioVenta*cantidad)-(precioCosto*cantidad);
                    totalGananciaDiaria = totalGananciaDiaria + ganancia;
                }
            }
        }
        return ResponseEntity.ok("Total ganacia dia "+ fecha + ": " +  formato.format(totalGananciaDiaria));
    }
    @GetMapping("/gananciasPorMes/{mes}-{anio}")
    public ResponseEntity<Object> getVentasPorMes(@PathVariable long mes, @PathVariable long anio) {
        List<Venta> ventas = ventaService.getAllVentas();
        long totalGananciaMes = 0;
        long precioVenta = 0;
        long precioCosto = 0;
        long cantidad = 0;
        long ganancia = 0;
        for (Venta venta : ventas) {
            Date fecha_venta = venta.getFechaVenta();
            LocalDate localDate = fecha_venta.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            long month = localDate.getMonthValue();
            long year = localDate.getYear();
            if(month == mes && year == anio){
                List<DetalleVenta> detalleVenta = venta.getDetalleVentas();
                for(DetalleVenta detalleTemp : detalleVenta){
                    precioVenta = detalleTemp.getPrecio();
                    precioCosto = detalleTemp.getPrecioCosto();
                    cantidad = detalleTemp.getCantidad();
                    ganancia = (precioVenta*cantidad)-(precioCosto*cantidad);
                    System.out.println(ganancia);
                    totalGananciaMes = totalGananciaMes + ganancia;
                }
            }
            
        }
        return ResponseEntity.ok("Total ganacia mes: " + formato.format(totalGananciaMes));
    }

    @GetMapping("/gananciasPorAño/{anio}")
    public ResponseEntity<Object> getVentasPorAnio(@PathVariable int anio) {
        List<Venta> ventas = ventaService.getAllVentas();
        long totalGananciaAnio = 0;
        long precioVenta = 0;
        long precioCosto = 0;
        long cantidad = 0;
        long ganancia = 0;
        for (Venta venta : ventas) {
            Date fecha_venta = venta.getFechaVenta();
            LocalDate localDate = fecha_venta.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            long year = localDate.getYear();
            if(year == anio){
                List<DetalleVenta> detalleVenta = venta.getDetalleVentas();
                for(DetalleVenta detalleTemp : detalleVenta){
                    precioVenta = detalleTemp.getPrecio();
                    precioCosto = detalleTemp.getPrecioCosto();
                    cantidad = detalleTemp.getCantidad();
                    ganancia = (precioVenta*cantidad)-(precioCosto*cantidad);
                    System.out.println(ganancia);
                    totalGananciaAnio = totalGananciaAnio + ganancia;
                }
            }
            
        }
        return ResponseEntity.ok("Total ganacia año: " + formato.format(totalGananciaAnio));
    }

    @PostMapping
    public ResponseEntity<Object> createVenta(@RequestBody Venta venta){
        Venta createdVenta = ventaService.createVenta(venta);
        if(createdVenta == null){
            log.error("Error al crear pelicula {}",venta);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Error al crear la venta")); 
        }
        return ResponseEntity.ok(createdVenta);
    }
    @PutMapping("/{id}")
    public Venta updateVenta(@PathVariable Long id, @RequestBody Venta venta) {
        return ventaService.updateVenta(id, venta);
    }
    @DeleteMapping("/{id}")
    public void deleteVenta(@PathVariable Long id){
        ventaService.deleteVenta(id);
    }

    static class ErrorResponse {
        private final String message;

        public ErrorResponse(String message){
            this.message = message;
        }

        public String getMessage(){
            return message;
        }
    }
}
