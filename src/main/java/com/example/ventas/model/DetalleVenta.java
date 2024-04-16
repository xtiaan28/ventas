package com.example.ventas.model;

import jakarta.persistence.*;

@Entity
@Table(name = "detalle_venta")

public class DetalleVenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle_venta")
    private Long idDetalleVenta;
    @Column(name = "producto")
    private String producto;
    @Column(name = "precio_venta")
    private Long precio;
    @Column(name = "precio_costo")
    private Long precioCosto;
    @Column(name = "cantidad")
    private Long cantidad;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_venta_detalle")
    private Venta idVentaDet;

    public DetalleVenta() { }
    public DetalleVenta(Long idDetalleVenta, String producto, Long precio, Long precioCosto, Long cantidad){
        this.idDetalleVenta = idDetalleVenta;
        this.producto = producto;
        this.precio = precio;
        this.precioCosto = precioCosto;
        this.cantidad = cantidad;
    }
    public Long getIdDetalleVenta(){
        return idDetalleVenta;
    }
    public String getProducto(){
        return producto;
    }
    public Long getPrecio(){
        return precio;
    }

    public Long getPrecioCosto(){
        return precioCosto;
    }

    public Long getCantidad(){
        return cantidad;
    }
}