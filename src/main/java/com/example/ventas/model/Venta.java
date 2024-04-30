package com.example.ventas.model;

import org.springframework.hateoas.RepresentationModel;
import java.util.Date;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.*;


@Entity
@Table(name = "venta")
public class Venta extends RepresentationModel<Venta>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_venta")
    private Long idVenta;

    @NotBlank(message = "No puede ingresar un vendedor vacio")
    @Column(name= "vendedor")
    private String vendedor;
    @Column(name= "fecha_venta")
    private Date fechaVenta;
    @OneToMany(mappedBy = "idVentaDet")
    private List<DetalleVenta> detalleVenta;

    public Venta() { }

    public Venta(Long idVenta, String vendedor, Date fechaVenta, List<DetalleVenta> detalleVenta){
        this.idVenta = idVenta;
        this.vendedor = vendedor;
        this.fechaVenta = fechaVenta;
        this.detalleVenta = detalleVenta;
    }

    public Long getIdVenta(){
        return idVenta;
    }

    public String getVendedor(){
        return vendedor;
    }

    public Date getFechaVenta(){
        return fechaVenta;
    }
    public List<DetalleVenta> getDetalleVentas(){
        return detalleVenta;
    }
    public void setIdVenta(Long idVenta) {
        this.idVenta = idVenta;
    }
    public void setVendedor(String vendedor) {
        this.vendedor = vendedor;
    }
    public void setFechaVenta(Date fechaVenta) {
        this.fechaVenta = fechaVenta;
    }
    public void setDetalleVentas(List<DetalleVenta> detalleVenta) { 
        this.detalleVenta = detalleVenta; 
    }

}
