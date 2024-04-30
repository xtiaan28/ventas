package com.example.ventas.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;
import java.time.LocalDate;

import java.time.ZoneId;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

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
    public CollectionModel<EntityModel<Venta>> getAllVentas() {
        List<Venta> ventas = ventaService.getAllVentas();
        log.info("GET /ventas");
        log.info("Retornando todos lss ventas");
        List<EntityModel<Venta>> ventasResources = ventas.stream()
            .map( venta -> EntityModel.of(venta,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getVentasById(venta.getIdVenta())).withSelfRel()
            ))
            .collect(Collectors.toList());

        WebMvcLinkBuilder linkTo = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getAllVentas());
        CollectionModel<EntityModel<Venta>> resources = CollectionModel.of(ventasResources, linkTo.withRel("ventas"));

        return resources;
    }
    /*@GetMapping
    public List<Venta> getVentas(){
        log.info("GET/ventas");
        log.info("Retornando todos las ventas");
        return ventaService.getAllVentas();
    }*/
    @GetMapping("/{id}")
    public EntityModel<Venta> getVentasById(@PathVariable Long id) {
        Optional<Venta> venta = ventaService.getVentaById(id);

        if (venta.isPresent()) {
            return EntityModel.of(venta.get(),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getVentasById(id)).withSelfRel(),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getAllVentas()).withRel("all-ventas"));
        } else {
            throw new VentaNotFoundException("Venta not found with id: " + id);
        }
    }
    /*@GetMapping("/{id}")
    public ResponseEntity <Object> getVentasById(@PathVariable Long id) {
        Optional<Venta> venta = ventaService.getVentaById(id);
        if(venta.isEmpty()){
            log.error("No se encontro venta con ID {}", id);
            
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("No se encontro venta con ID " + id));
            //return ResponseEntity.notFound().build();
        }
        log.info("Venta encontrada con exito");
        return ResponseEntity.ok(ventaService.getVentaById(id));
    }*/

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
        return ResponseEntity.ok("Total ganacia mes "+mes+"-"+anio+": " + formato.format(totalGananciaMes));
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
        return ResponseEntity.ok("Total ganacia año "+anio+": " + formato.format(totalGananciaAnio));
    }
    @PostMapping
    public EntityModel<Venta> createVenta(@Validated @RequestBody Venta venta) {
        Venta createdVenta = ventaService.createVenta(venta);
            return EntityModel.of(createdVenta,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getVentasById(createdVenta.getIdVenta())).withSelfRel(),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getAllVentas()).withRel("all-ventas"));

    }
    /*@PostMapping
    public ResponseEntity<Object> createVenta(@RequestBody Venta venta){
        Venta createdVenta = ventaService.createVenta(venta);
        if(createdVenta == null){
            log.error("Error al crear pelicula {}",venta);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Error al crear la venta")); 
        }
        return ResponseEntity.ok(createdVenta);
    }*/
    @PutMapping("/{id}")
    public EntityModel<Venta> updateVenta(@PathVariable Long id, @RequestBody Venta venta) {
        Venta updatedVenta = ventaService.updateVenta(id, venta);
        return EntityModel.of(updatedVenta,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getVentasById(id)).withSelfRel(),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getAllVentas()).withRel("all-ventas"));

    }
    /*@PutMapping("/{id}")
    public ResponseEntity<Object> updateVenta(@PathVariable Long id, @RequestBody Venta venta) {
        Venta updatedVenta = ventaService.updateVenta(id, venta);
        if(updatedVenta == null){
            log.error("Error al actualizar {}",venta);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Error al actualizar la venta id "+id)); 
        }
        return ResponseEntity.ok(updatedVenta);
    }*/
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteVenta(@PathVariable Long id){
        ventaService.deleteVenta(id);
        ventaService.deleteVenta(id);
        return ResponseEntity.ok("ID "+id+ " eliminado correctamente");
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
