/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package motor;

import hibernate.Cliente;
import hibernate.Coche;
import hibernate.CocheCliente;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Oracle
 */
public class Pruebas {
    public static void main(String[] args){
        MOTOR motor = new MOTOR();
        BigDecimal bd = new BigDecimal(1);
        String[] filtro = {"matricula", "qwe     "};
        String[] filtro1 = {"telefono", "666      "};
        String orden = "modelo";
        String orden1 = "nombre";
        Cliente cliente = new Cliente(bd,null,null,null,null,null,null,null);
        try {
            //motor.insertarCliente(new Cliente(bd,"Aa","Aa","Aa","Aaaa","6","Aaaa",null));
            //motor.eliminarClientes(bd);
            //motor.modificarCliente(new Cliente(bd,"www","ww",null,"PEPMAIL.COM","666","wwwwww",null));
            //System.out.println(motor.leerCliente(bd).getNombre());
            /*
            ArrayList<Cliente> clientes = motor.leerClientes(filtro1, orden1);
            while (!clientes.isEmpty()){
                System.out.println(clientes.get(0).getTelefono());
                clientes.remove(0);
            }*/
            SimpleDateFormat dateformat = new SimpleDateFormat("dd/MM/yyyy");
            Date date = dateformat.parse("20/11/2020");
            //motor.insertarCoche(new Coche(new BigDecimal(50), cliente, "marcaA", "modeloO", "1129AAA",date));
            //motor.eliminarCoche(18);
            //motor.modificarCoche(new Coche(new BigDecimal(12), cliente, "ma", "modeloO", "45AA",date));
            //System.out.println(motor.leerCoche(2).getMatricula());
            /*
            ArrayList<CocheCliente> coches = motor.leerCoches();
            while (!coches.isEmpty()){
                System.out.println(coches.get(0).getMatricula());
                coches.remove(0);
            }*/
            
            
            ArrayList<Coche> coches1 = motor.leerCoches(filtro, orden);
            while (!coches1.isEmpty()){
                System.out.println(coches1.get(0).getMatricula());
                coches1.remove(0);
            }
            //motor.cerrarSesion();
            System.out.println("Exito");
        } catch (ExceptionMotor ex) {
            System.out.println("Codigo error: " + ex.getMensajeErrorUsuario());
            System.out.println("Mensaje error administrador: " + ex.getMensajeErrorAdministrador());
            System.out.println("Mensaje error usuario: " + ex.getMensajeErrorUsuario());
            System.out.println("Sentencia SQL: " + ex.getSentenciaSQL());
        } catch (ParseException ex) {
        }
        motor.cerrarSesion();
        
    }
    
}
