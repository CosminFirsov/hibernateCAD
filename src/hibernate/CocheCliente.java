/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hibernate;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author Oracle
 */
public class CocheCliente {
    private BigDecimal clienteId;
     private String nombre;
     private String apellido1;
     private String apellido2;
     private String email;
     private String telefono;
     private String dni;
     private BigDecimal cocheId;
     private String marca;
     private String modelo;
     private String matricula;
     private Date itv;

    public CocheCliente() {
    }

    public CocheCliente(BigDecimal clienteId, String nombre, String apellido1, String apellido2, String email, String telefono, String dni, BigDecimal cocheId, String marca, String modelo, String matricula, Date itv) {
        this.clienteId = clienteId;
        this.nombre = nombre;
        this.apellido1 = apellido1;
        this.apellido2 = apellido2;
        this.email = email;
        this.telefono = telefono;
        this.dni = dni;
        this.cocheId = cocheId;
        this.marca = marca;
        this.modelo = modelo;
        this.matricula = matricula;
        this.itv = itv;
    }

    public BigDecimal getClienteId() {
        return clienteId;
    }

    public void setClienteId(BigDecimal clienteId) {
        this.clienteId = clienteId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido1() {
        return apellido1;
    }

    public void setApellido1(String apellido1) {
        this.apellido1 = apellido1;
    }

    public String getApellido2() {
        return apellido2;
    }

    public void setApellido2(String apellido2) {
        this.apellido2 = apellido2;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public BigDecimal getCocheId() {
        return cocheId;
    }

    public void setCocheId(BigDecimal cocheId) {
        this.cocheId = cocheId;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public Date getItv() {
        return itv;
    }

    public void setItv(Date itv) {
        this.itv = itv;
    }
     
     
     
}
