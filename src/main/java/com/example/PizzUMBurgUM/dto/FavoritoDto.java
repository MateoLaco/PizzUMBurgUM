package com.example.PizzUMBurgUM.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FavoritoDto {
    private Long id;
    private String nombre;
    private String descripcion;
    private Double precio;
    private String tipo; // "PIZZA" o "HAMBURGUESA"
    /**
     * Objeto libre con el detalle de la creación (pizza u hamburguesa).
     * Se serializa a JSON para guardarlo en la BD.
     */
    private Object detalles;
    private LocalDate fechaCreacion;

    // Campos específicos para Hamburguesa
    private String carne;
    private Integer cantidadCarne;
    private String pan;
    private List<String> toppings;
    private List<String> salsas;

    // Campos específicos para Pizza
    private String tamaño;
    private String masa;
    private String salsa;
    private String queso;
    private List<String> ingredientes;


    public FavoritoDto(List<String> ingredientes, String queso, String salsa, String masa, String tamaño, List<String> salsas, List<String> toppings, String pan, Integer cantidadCarne, String carne, String tipo, Double precio, String descripcion, String nombre) {
        this.ingredientes = ingredientes;
        this.queso = queso;
        this.salsa = salsa;
        this.masa = masa;
        this.tamaño = tamaño;
        this.salsas = salsas;
        this.toppings = toppings;
        this.pan = pan;
        this.cantidadCarne = cantidadCarne;
        this.carne = carne;
        this.tipo = tipo;
        this.precio = precio;
        this.descripcion = descripcion;
        this.nombre = nombre;
    }

    public FavoritoDto(Long id, String nombre, String descripcion, Double precio, String tipo, Object detalles, LocalDateTime fechaCreacion) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.tipo = tipo;
        this.detalles = detalles;
        this.fechaCreacion = fechaCreacion != null ? fechaCreacion.toLocalDate() : null;
    }

    public static FavoritoDto fromEntity(com.example.PizzUMBurgUM.entidades.Favorito favorito) {
        return new FavoritoDto(
                favorito.getId(),
                favorito.getNombre(),
                favorito.getDescripcion(),
                favorito.getPrecio(),
                favorito.getTipo(),
                favorito.getDetalles(),
                favorito.getFechaCreacion()
        );
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getCarne() {
        return carne;
    }

    public void setCarne(String carne) {
        this.carne = carne;
    }

    public Integer getCantidadCarne() {
        return cantidadCarne;
    }

    public void setCantidadCarne(Integer cantidadCarne) {
        this.cantidadCarne = cantidadCarne;
    }

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    public List<String> getToppings() {
        return toppings;
    }

    public void setToppings(List<String> toppings) {
        this.toppings = toppings;
    }

    public List<String> getSalsas() {
        return salsas;
    }

    public void setSalsas(List<String> salsas) {
        this.salsas = salsas;
    }

    public String getTamaño() {
        return tamaño;
    }

    public void setTamaño(String tamaño) {
        this.tamaño = tamaño;
    }

    public String getMasa() {
        return masa;
    }

    public void setMasa(String masa) {
        this.masa = masa;
    }

    public String getSalsa() {
        return salsa;
    }

    public void setSalsa(String salsa) {
        this.salsa = salsa;
    }

    public String getQueso() {
        return queso;
    }

    public void setQueso(String queso) {
        this.queso = queso;
    }

    public List<String> getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(List<String> ingredientes) {
        this.ingredientes = ingredientes;
    }
}
