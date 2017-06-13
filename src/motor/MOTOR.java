/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package motor;

import org.hibernate.Session;
import org.hibernate.Transaction;
import hibernate.Cliente;
import hibernate.Coche;
import hibernate.CocheCliente;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.PropertyValueException;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.StaleStateException;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.exception.GenericJDBCException;
import org.hibernate.exception.SQLGrammarException;
import org.hibernate.id.IdentifierGenerationException;

/**
 * Clase que carga el driver para conecarse a SQL en memoria, establece la
 * conexion, y modifica, inserta borrar registros.
 *
 * @author Cosmin Firsov
 */
public class MOTOR {
    // Se obtiene la instancia de fábrica de sesiones
    SessionFactory fabricaSesiones = SessionFactoryUtil.getSessionFactory();
    // Se crea una sesión
    Session sesion = (Session) fabricaSesiones.openSession();
    // Se inicia una transacción en la sesión creada

    /**
     * Método que inserta una registro en la tabla cliente. Utiliza operaciones
     * elementales
     *
     * @param cliente el coche a insertar
     * @throws ExceptionMotor excepcion que integra el mensaje de error al
     * usuario, el codigo de error y el mensaje de error que nos ha devuelto la
     * base de datos.
     */
    public void insertarCliente(Cliente cliente) throws ExceptionMotor {
        // Se inicia una transacción en la sesión creada
        Transaction t = sesion.beginTransaction();

        try {

            sesion.save(cliente);
            // Se confirma la transacción
            t.commit();
        } catch (IdentifierGenerationException igEx) {
            ExceptionMotor ex = new ExceptionMotor();
            throw ex;
        } catch (ConstraintViolationException igEx) {
            ExceptionMotor ex = new ExceptionMotor();
            if (igEx.getErrorCode() == 2290) {
                ex.setCodigoErrorAdministrador(igEx.getErrorCode());
                ex.setSentenciaSQL(igEx.getSQL());
                ex.setMensajeErrorUsuario("Error. El campo telefono debe empezar por 6 o 9 y contener 9 caracteres numéricos y el email debe acabar en .com o .es");
                ex.setMensajeErrorAdministrador(igEx.getMessage());
            } else {
                ex.setCodigoErrorAdministrador(igEx.getErrorCode());
                ex.setSentenciaSQL(igEx.getSQL());
                ex.setMensajeErrorUsuario("Error. Los siguientes campos son únicos: dni, email y telefono");
                ex.setMensajeErrorAdministrador(igEx.getMessage());
            }
            throw ex;
        } catch (PropertyValueException pvEx) {
            ExceptionMotor ex = new ExceptionMotor();

            ex.setMensajeErrorUsuario("Error. Los siguientes campos son obligatorios: nombre, apellido1, email, telefono, dni");
            ex.setMensajeErrorAdministrador(pvEx.getMessage());
            throw ex;
        } catch (SQLGrammarException sqlEx) {
            ExceptionMotor ex = new ExceptionMotor();

            ex.setCodigoErrorAdministrador(sqlEx.getErrorCode());
            ex.setSentenciaSQL(sqlEx.getSQL());
            ex.setMensajeErrorUsuario("Error. El campo telefono es un campo numerico");
            ex.setMensajeErrorAdministrador(sqlEx.getMessage());

            throw ex;
        } catch (GenericJDBCException jdbcEx) {
            ExceptionMotor ex = new ExceptionMotor();
            if (jdbcEx.getErrorCode() == 20004) {
                ex.setCodigoErrorAdministrador(jdbcEx.getErrorCode());
                ex.setSentenciaSQL(jdbcEx.getSQL());
                ex.setMensajeErrorUsuario("Error. El taller esta cerrado");
                ex.setMensajeErrorAdministrador(jdbcEx.getMessage());
            }
            if (jdbcEx.getErrorCode() == 20005) {
                ex.setCodigoErrorAdministrador(jdbcEx.getErrorCode());
                ex.setSentenciaSQL(jdbcEx.getSQL());
                ex.setMensajeErrorUsuario("Error. El email debe acabar por .com o .es");
                ex.setMensajeErrorAdministrador(jdbcEx.getMessage());
            }
            if (jdbcEx.getErrorCode() == 20006) {
                ex.setCodigoErrorAdministrador(jdbcEx.getErrorCode());
                ex.setSentenciaSQL(jdbcEx.getSQL());
                ex.setMensajeErrorUsuario("Error. El campo telefono es un campo numerico de 9 caracteres");
                ex.setMensajeErrorAdministrador(jdbcEx.getMessage());
            }

            throw ex;
        } catch (Exception e) {
            ExceptionMotor ex = new ExceptionMotor();

            ex.setCodigoErrorAdministrador(0);
            ex.setMensajeErrorUsuario("Error general en el sistema, Consulte con el administrador");
            ex.setMensajeErrorAdministrador(e.getMessage());

            throw ex;
        }

    }

    /**
     * Método que elimina registros de una base de datos. Elimina un cliente
     * buscandolo por su identificador de coche.Utiliza operaciones elementales
     *
     * @param clienteId: el identificador del cliente a eliminar
     * @throws ExceptionMotor excepcion que integra el mensaje de error al
     * usuario, el codigo de error y el mensaje de error que nos ha devuelto la
     * base de datos
     */
    public void eliminarClientes(BigDecimal clienteId) throws ExceptionMotor {
        Transaction t = sesion.beginTransaction();

        //Se crea un objeto Cliente transitorio
        Cliente cliente = new Cliente(clienteId, "", "", "", "", "");

        try {

            sesion.delete(cliente);
            // Se confirma la transacción
            t.commit();

        } catch (StaleStateException ssEx) {
            ExceptionMotor ex = new ExceptionMotor();

            ex.setMensajeErrorUsuario("Error. El identificador de cliente no existe");
            ex.setMensajeErrorAdministrador(ssEx.getMessage());

            throw ex;
        } catch (ConstraintViolationException igEx) {
            ExceptionMotor ex = new ExceptionMotor();

            ex.setCodigoErrorAdministrador(igEx.getErrorCode());
            ex.setSentenciaSQL(igEx.getSQL());
            ex.setMensajeErrorUsuario("Error. Hay coches asociados a este cliente");
            ex.setMensajeErrorAdministrador(igEx.getMessage());

            throw ex;
        } catch (Exception e) {
            ExceptionMotor ex = new ExceptionMotor();

            ex.setCodigoErrorAdministrador(0);
            ex.setMensajeErrorUsuario("Error general en el sistema, Consulte con el administrador");
            ex.setMensajeErrorAdministrador(e.getMessage());

            throw ex;
        }

    }

    /**
     * Método que modifica los registros de un cliente ya existente. Utiliza
     * operaciones elementales
     *
     * @param cliente los datos del coche a modificar
     * @throws ExceptionMotor excepcion que integra el mensaje de error al
     * usuario, el codigo de error y el mensaje de error que nos ha devuelto la
     * base de datos
     */
    public void modificarCliente(Cliente cliente) throws ExceptionMotor {
        Transaction t = sesion.beginTransaction();

        try {

            sesion.update(cliente);
            // Se confirma la transacción
            t.commit();

        } catch (ConstraintViolationException cvEx) {
            ExceptionMotor ex = new ExceptionMotor();

            if (cvEx.getErrorCode() == 2290) {
                ex.setCodigoErrorAdministrador(cvEx.getErrorCode());
                ex.setSentenciaSQL(cvEx.getSQL());
                ex.setMensajeErrorUsuario("Error. El campo telefono debe empezar por 6 o 9 y contener 9 caracteres numéricos y el email debe acabar en .com o .es");
                ex.setMensajeErrorAdministrador(cvEx.getMessage());

            } else {
                ex.setCodigoErrorAdministrador(cvEx.getErrorCode());
                ex.setSentenciaSQL(cvEx.getSQL());
                ex.setMensajeErrorUsuario("Error. Los siguientes campos son unicos: dni, email y telefono");
                ex.setMensajeErrorAdministrador(cvEx.getMessage());
            }
            throw ex;
        } catch (PropertyValueException pvEx) {
            ExceptionMotor ex = new ExceptionMotor();

            ex.setMensajeErrorUsuario("Error. Los siguientes campos son obligatorios: nombre, apellido1, email, telefono, dni");
            ex.setMensajeErrorAdministrador(pvEx.getMessage());

            throw ex;
        } catch (SQLGrammarException sqlEx) {
            ExceptionMotor ex = new ExceptionMotor();

            ex.setCodigoErrorAdministrador(sqlEx.getErrorCode());
            ex.setSentenciaSQL(sqlEx.getSQL());
            ex.setMensajeErrorUsuario("Error. El telefono es un campo numerico");
            ex.setMensajeErrorAdministrador(sqlEx.getMessage());

            throw ex;
        } catch (GenericJDBCException jdbcEx) {
            ExceptionMotor ex = new ExceptionMotor();

            if (jdbcEx.getErrorCode() == 20004) {
                ex.setCodigoErrorAdministrador(jdbcEx.getErrorCode());
                ex.setSentenciaSQL(jdbcEx.getSQL());
                ex.setMensajeErrorUsuario("Error. El taller esta cerrado");
                ex.setMensajeErrorAdministrador(jdbcEx.getMessage());
            }
            if (jdbcEx.getErrorCode() == 20005) {
                ex.setCodigoErrorAdministrador(jdbcEx.getErrorCode());
                ex.setSentenciaSQL(jdbcEx.getSQL());
                ex.setMensajeErrorUsuario("Error. El email debe acabar por .com o .es");
                ex.setMensajeErrorAdministrador(jdbcEx.getMessage());
            }
            if (jdbcEx.getErrorCode() == 20006) {
                ex.setCodigoErrorAdministrador(jdbcEx.getErrorCode());
                ex.setSentenciaSQL(jdbcEx.getSQL());
                ex.setMensajeErrorUsuario("Error. El campo telefono es un campo numerico de 9 caracteres");
                ex.setMensajeErrorAdministrador(jdbcEx.getMessage());
            }
            throw ex;
        } catch (StaleStateException ssEx) {
            ExceptionMotor ex = new ExceptionMotor();

            ex.setMensajeErrorUsuario("Error. No existe ese cliente");

            throw ex;
        } catch (Exception e) {
            ExceptionMotor ex = new ExceptionMotor();

            ex.setCodigoErrorAdministrador(0);
            ex.setMensajeErrorUsuario("Error general en el sistema, Consulte con el administrador");
            ex.setMensajeErrorAdministrador(e.getMessage());

            throw ex;
        }

    }

    /**
     * Método que ejecuta una select en una base de datos. Lee los datos de un
     * coche buscandolo por su identificador de coche. Utiliza operaciones
     * elementales
     *
     * @param clienteId el identificador de cliente
     * @return devuelve el cliente con cliente_id = clienteId
     * @throws ExceptionMotor excepcion que integra el mensaje de error al
     * usuario, el codigo de error y el mensaje de error que nos ha devuelto la
     * base de datos
     */
    public Cliente leerCliente(BigDecimal clienteId) throws ExceptionMotor {

        Cliente cliente = null;
        Cliente clienteAux = new Cliente();

        try {
            // Se busca el registro en cuestión y se carga en un objeto de clase Regions
            cliente = (Cliente) sesion.load(Cliente.class, clienteId);

            //Clono el objeto cliente para que al cerrar la sesión no lo pierda
            if (cliente != null) {
                clienteAux.setClienteId(cliente.getClienteId());

                clienteAux.setNombre(cliente.getNombre());

                clienteAux.setApellido1(cliente.getApellido1());

                clienteAux.setApellido2(cliente.getApellido2());

                clienteAux.setEmail(cliente.getEmail());

                clienteAux.setTelefono(cliente.getTelefono());

                clienteAux.setDni(cliente.getDni());
            }

        } catch (ObjectNotFoundException onfEx) {
            // Se capturará esta excepción en el caso de que el registro buscado no exista
            ExceptionMotor ex = new ExceptionMotor();

            ex.setMensajeErrorUsuario("Error. El cliente no existe");
            ex.setMensajeErrorAdministrador(onfEx.getMessage());

            throw ex;
        } catch (Exception e) {
            ExceptionMotor ex = new ExceptionMotor();

            ex.setCodigoErrorAdministrador(0);
            ex.setMensajeErrorUsuario("Error general en el sistema, Consulte con el administrador");
            ex.setMensajeErrorAdministrador(e.getMessage());

            throw ex;
        }

        return clienteAux;
    }

    /**
     * Método que ejecuta una select en una base de datos. Lee todos los coches
     * que hay y los devuelve en un ArrayList. Utiliza query HQL
     *
     * @return un ArrayList de clientes
     * @throws ExceptionMotor excepcion que integra el mensaje de error al
     * usuario, el codigo de error y el mensaje de error que nos ha devuelto la
     * base de datos
     */
    public ArrayList<Cliente> leerClientes() throws ExceptionMotor {
        ArrayList<Cliente> clientesAux = new ArrayList();
        ArrayList<Cliente> clientes = new ArrayList();

        try {
            // Se crea la query HQL
            Query q = sesion.createQuery("from Cliente");

            clientesAux = (ArrayList) q.list();
            clientes = (ArrayList) clientesAux.clone();
        } catch (ObjectNotFoundException onfEx) {
            // Se capturará esta excepción en el caso de que el registro buscado no exista
            ExceptionMotor ex = new ExceptionMotor();

            ex.setMensajeErrorUsuario("Error. No existe ese cliente");
            ex.setMensajeErrorAdministrador(onfEx.getMessage());

            throw ex;
        } catch (Exception e) {
            ExceptionMotor ex = new ExceptionMotor();

            ex.setCodigoErrorAdministrador(0);
            ex.setMensajeErrorUsuario("Error general en el sistema, Consulte con el administrador");
            ex.setMensajeErrorAdministrador(e.getMessage());

            throw ex;
        }

        return clientes;
    }

    /**
     * Método que ejecuta una select en una base de datos. Lee todos los
     * clientes que hay atendiendo a un filtro y un orden y los devuelve en un
     * ArrayList. Usa query HQL parametrizada
     *
     * @param filtro Un array de dos dimensiones. En la primera posicion se
     * introduce el campo filtro. Debe ser parte de una sentencia sql, formara
     * parte de la clausula where y en dicha clausula se podran incorporar la
     * condicion que se desee atendiendo a los nombres de cada campo que se
     * quiera ver. Valores que se pueden introducir: clienteId, nombre,
     * apellido1, apellido2, email, telefono, dni. En la segunda posicion se
     * introduce el filtro a buscar por el usuario
     * @param orden debe ser parte de una sentencia sql, formara parte de la
     * clausula order by y en dicha clausula se podran incorporar la condicion
     * que se desee para conseguir el orden deseado. Valores que se pueden
     * introducir:clienteId, nombre, apellido1, apellido2, email, telefono, dni.
     * ej: order by nombre. Además, se puede añadir ascend or descend al final
     * para indicar si el orden es ascendente o descendente
     * @return un ArrayList de cliente con todos los clientes
     * @throws ExceptionMotor excepcion que integra el mensaje de error al
     * usuario, el codigo de error y el mensaje de error que nos ha devuelto la
     * base de datos
     */
    public ArrayList<Cliente> leerClientes(String[] filtro, String orden) throws ExceptionMotor {
        ArrayList<Cliente> clientesAux = new ArrayList<>();
        ArrayList<Cliente> clientes = new ArrayList<>();

        try {
            // Se crea la query HQL
            Query q = sesion.createQuery("from Cliente where " + filtro[0] + " = :filtro order by " + orden);
            q.setString("filtro", filtro[1]);

            clientesAux = (ArrayList) q.list();
            clientes = (ArrayList) clientesAux.clone();

        } catch (Exception e) {
            ExceptionMotor ex = new ExceptionMotor();

            ex.setCodigoErrorAdministrador(0);
            ex.setMensajeErrorUsuario("Error general en el sistema, Consulte con el administrador");
            ex.setMensajeErrorAdministrador(e.getMessage());

            throw ex;
        }

        return clientes;
    }

    /**
     * Método que inserta un registro en la tabla coche. Utiliza operaciones
     * elementales
     *
     * @param coche El objeto coche
     * @throws ExceptionMotor excepcion que integra el mensaje de error al
     * usuario, el codigo de error y el mensaje de error que nos ha devuelto la
     * base de datos
     */
    public void insertarCoche(Coche coche) throws ExceptionMotor {
        // Se inicia una transacción en la sesión creada
        Transaction t = sesion.beginTransaction();

        try {

            sesion.save(coche);
            // Se confirma la transacción
            t.commit();
        } catch (PropertyValueException pvEx) {
            ExceptionMotor ex = new ExceptionMotor();

            ex.setMensajeErrorUsuario("Error. Todos los campos son obligatorios");
            ex.setMensajeErrorAdministrador(pvEx.getMessage());

            throw ex;

        } catch (ConstraintViolationException igEx) {
            ExceptionMotor ex = new ExceptionMotor();

            if (igEx.getErrorCode() == 2291) {
                ex.setCodigoErrorAdministrador(igEx.getErrorCode());
                ex.setSentenciaSQL(igEx.getSQL());
                ex.setMensajeErrorUsuario("Error. No existe ese cliente");
                ex.setMensajeErrorAdministrador("VIOLACION DE CHECK CONSTRAINT");
            } else if (igEx.getErrorCode() == 1) {
                ex.setCodigoErrorAdministrador(igEx.getErrorCode());
                ex.setSentenciaSQL(igEx.getSQL());
                ex.setMensajeErrorUsuario("Error. Los siguientes campos son unicos: matricula");
                ex.setMensajeErrorAdministrador("VIOLACION DE UNIQUE KEY");
            }
            throw ex;

        } catch (GenericJDBCException gjdbcEx) {
            ExceptionMotor ex = new ExceptionMotor();

            if (gjdbcEx.getErrorCode() == 20002) {
                ex.setCodigoErrorAdministrador(gjdbcEx.getErrorCode());
                ex.setSentenciaSQL(gjdbcEx.getSQL());
                ex.setMensajeErrorUsuario("Error. La ITV debe estar pasada");
                ex.setMensajeErrorAdministrador("VIOLACION DE TRIGGER ITV_PASADA");
            }
            if (gjdbcEx.getErrorCode() == 20004) {
                ex.setCodigoErrorAdministrador(gjdbcEx.getErrorCode());
                ex.setSentenciaSQL(gjdbcEx.getSQL());
                ex.setMensajeErrorUsuario("Error. El taller esta cerrado");
                ex.setMensajeErrorAdministrador("VIOLACION DE TRIGGER TALLER_ABIERTO");
            }
            throw ex;

        } catch (Exception e) {
            ExceptionMotor ex = new ExceptionMotor();

            ex.setCodigoErrorAdministrador(0);
            ex.setMensajeErrorUsuario("Error general en el sistema, Consulte con el administrador");
            ex.setMensajeErrorAdministrador(e.getMessage());

            throw ex;
        }

    }

    /**
     * Método que elimina una fila de una base de datos. Elimina un coche
     * buscandolo por su identificador de coche. Utiliza HQL
     *
     * @param cocheId el identificador del coche a eliminar
     * @throws ExceptionMotor excepcion que integra el mensaje de error al
     * usuario, el codigo de error y el mensaje de error que nos ha devuelto la
     * base de datos
     */
    public void eliminarCoche(int cocheId) throws ExceptionMotor {
        // Se inicia una transacción en la sesión creada
        Transaction t = sesion.beginTransaction();

        // Se crea la query HQL
        Query q = sesion.createQuery("delete Coche where cocheId = " + cocheId);
        try {
            int registrosAfectados = q.executeUpdate();

            // Se confirma la transacción
            t.commit();
        } catch (Exception e) {
            ExceptionMotor ex = new ExceptionMotor();

            ex.setCodigoErrorAdministrador(0);
            ex.setMensajeErrorUsuario("Error general en el sistema, Consulte con el administrador");
            ex.setMensajeErrorAdministrador(e.getMessage());

            throw ex;
        }

    }

    /**
     * Método que modifica los datos de un coche ya existente. Usa HQL
     * parametrizado
     *
     * @return el numero de registros afectados
     * @param coche el objeto coche con los datos a modificar
     * @throws ExceptionMotor excepcion que integra el mensaje de error al
     * usuario, el codigo de error y el mensaje de error que nos ha devuelto la
     * base de datos
     */
    public int modificarCoche(Coche coche) throws ExceptionMotor {
        int registrosAfectados = -1;

        // Se inicia una transacción en la sesión creada
        Transaction t = sesion.beginTransaction();

        // Se crea la query HQL
        Query q = sesion.createQuery("update Coche set cliente.clienteId = :coclienteId,"
                + "marca = :comarca, modelo = :comodelo, matricula = :comatricula , "
                + "itv = :coitv "
                + "where cocheId = :cococheId");
        q.setString("comarca", coche.getMarca());
        q.setString("comodelo", coche.getModelo());
        q.setString("comatricula", coche.getMatricula());
        q.setDate("coitv", coche.getItv());
        q.setBigDecimal("cococheId", coche.getCocheId());
        q.setBigDecimal("coclienteId", coche.getCliente().getClienteId());

        try {
            // Se ejecuta la query q
            registrosAfectados = q.executeUpdate();

            // Se confirma la transacción
            t.commit();
        } catch (GenericJDBCException jdbcEx) {
            ExceptionMotor ex = new ExceptionMotor();

            if (jdbcEx.getErrorCode() == 2291) {
                ex.setCodigoErrorAdministrador(jdbcEx.getErrorCode());
                ex.setSentenciaSQL(jdbcEx.getSQL());
                ex.setMensajeErrorUsuario("Error. No existe ese ciente");
                ex.setMensajeErrorAdministrador("NO SE HA INTRODUCIDO UN COCHE_ID QUE EXISTA");
            }
            if (jdbcEx.getErrorCode() == 1407) {
                ex.setCodigoErrorAdministrador(jdbcEx.getErrorCode());
                ex.setSentenciaSQL(jdbcEx.getSQL());
                ex.setMensajeErrorUsuario("Error. Los siguientes campos son obligastorios: marca, modelo, matricula, itv");
                ex.setMensajeErrorAdministrador("VIOLACION DE NOT_NULL");
            }
            if (jdbcEx.getErrorCode() == 20002) {
                ex.setCodigoErrorAdministrador(jdbcEx.getErrorCode());
                ex.setSentenciaSQL(jdbcEx.getSQL());
                ex.setMensajeErrorUsuario("Error. La ITV debe estar pasada");
                ex.setMensajeErrorAdministrador("VILACION DE TRIGGER ITV_PASADA");
            }
            if (jdbcEx.getErrorCode() == 20004) {
                ex.setCodigoErrorAdministrador(jdbcEx.getErrorCode());
                ex.setSentenciaSQL(jdbcEx.getSQL());
                ex.setMensajeErrorUsuario("Error. El taller esta cerrado");
                ex.setMensajeErrorAdministrador("VIOLACION DE TRIGGER TALLER ABIERTO");
            }
            throw ex;
        } catch (ConstraintViolationException cvEx) {
            ExceptionMotor ex = new ExceptionMotor();

            if (cvEx.getErrorCode() == 1) {
                ex.setCodigoErrorAdministrador(cvEx.getErrorCode());
                ex.setSentenciaSQL(cvEx.getSQL());
                ex.setMensajeErrorUsuario("Error. El campo matricula es unico");
                ex.setMensajeErrorAdministrador("VIOLACION DE UNIQUE KEY");
            }
            throw ex;
        } catch (StaleStateException ssEx) {
            ExceptionMotor ex = new ExceptionMotor();

            ex.setMensajeErrorUsuario("Error. No existe ese coche");
            ex.setMensajeErrorAdministrador("SE HA INTRODUCIDO UN COCHE_ID QUE NO EXISTE");
            throw ex;
        } catch (Exception e) {
            ExceptionMotor ex = new ExceptionMotor();

            ex.setCodigoErrorAdministrador(0);
            ex.setMensajeErrorUsuario("Error general en el sistema, Consulte con el administrador");
            ex.setMensajeErrorAdministrador(e.getMessage());

            throw ex;
        }
        return registrosAfectados;
    }

    /**
     * Metodo que lee un coche buscandolo por su identificador de coche. Usa
     * query HQL
     *
     * @param cocheId el identificador del coche
     * @return un coche Coche
     * @throws ExceptionMotor excepcion que integra el mensaje de error al
     * usuario, el codigo de error y el mensaje de error que nos ha devuelto la
     * base de datos
     */
    public Coche leerCoche(int cocheId) throws ExceptionMotor {

        Coche coche = new Coche();
        Coche cocheAux = new Coche();

        try {
            // Se crea la query HQL
            Query q = sesion.createQuery("from Coche where cocheId=" + cocheId);
            coche = (Coche) q.uniqueResult();
            if (coche != null) {
                cocheAux.setCliente(coche.getCliente());

                cocheAux.setCocheId(coche.getCocheId());

                cocheAux.setItv(coche.getItv());

                cocheAux.setMarca(coche.getMarca());

                cocheAux.setMatricula(coche.getMatricula());

                cocheAux.setModelo(coche.getModelo());
            }

        } catch (ObjectNotFoundException onfEx) {
            // Se capturará esta excepción en el caso de que el registro buscado no exista
            ExceptionMotor ex = new ExceptionMotor();

            ex.setMensajeErrorUsuario("Error. El registro no existe");
            ex.setMensajeErrorAdministrador("EL REGISTRO NO EXISTE");

            throw ex;
        } catch (Exception e) {
            ExceptionMotor ex = new ExceptionMotor();

            ex.setCodigoErrorAdministrador(0);
            ex.setMensajeErrorUsuario("Error general en el sistema, Consulte con el administrador");
            ex.setMensajeErrorAdministrador(e.getMessage());

            throw ex;
        }

        return cocheAux;

    }


    /**
     * Metodo que devuelve todos los coches con sus respectivos clientes. Usa
     * query HQL
     *
     * @return todos los coches
     * @throws ExceptionMotor excepcion que integra el mensaje de error al
     * usuario, el codigo de error y el mensaje de error que nos ha devuelto la
     * base de datos
     */
    public ArrayList<CocheCliente> leerCoches() throws ExceptionMotor {
        ArrayList<CocheCliente> coches = new ArrayList();

        try {
            // Se crea la query HQL
            Query q = sesion.createQuery("select new hibernate.CocheCliente"
                    + "(cl.clienteId, cl.nombre, cl.apellido1, cl.apellido2, "
                    + "cl.email, cl.telefono, cl.dni, co.cocheId, co.marca,"
                    + " co.modelo, co.matricula, co.itv)"
                    + " from Coche co, Cliente cl "
                    + "where co.cliente.clienteId = cl.clienteId");

            CocheCliente cocheCliente;
            
            List<CocheCliente> lista = q.list();
            
            Iterator<CocheCliente> iterador = lista.iterator();
            
            while(iterador.hasNext()){
                coches.add(iterador.next());
            }
        } catch (Exception e) {
            ExceptionMotor ex = new ExceptionMotor();

            ex.setCodigoErrorAdministrador(0);
            ex.setMensajeErrorUsuario("Error general en el sistema, Consulte con el administrador");
            ex.setMensajeErrorAdministrador(e.getMessage());

            throw ex;
        }

        return coches;
    }

    /**
     * Método que ejecuta una select en una base de datos. Lee todos los coches
     * que hay atendiendo a un filtro y un orden y los devuelve en un ArrayList.
     * Usa query HQL parametrizada
     *
     * @param filtro Un array de dos dimensiones. En la primera posicion se
     * introduce el campo filtro. Debe ser parte de una sentencia sql, formara
     * parte de la clausula where y en dicha clausula se podran incorporar la
     * condicion que se desee atendiendo a los nombres de cada campo que se
     * quiera ver. Valores que se pueden introducir: cocheId, marca, modelo,
     * matricula, itv. En la segunda posicion se introduce el filtro a buscar
     * por el usuario
     * @param orden debe ser parte de una sentencia sql, formara parte de la
     * clausula order by y en dicha clausula se podran incorporar la condicion
     * que se desee para conseguir el orden deseado. Valores que se pueden
     * introducir:cocheId, marca, modelo, matricula, itv. ej: order by nombre.
     * Además, se puede añadir ascend or descend al final para indicar si el
     * orden es ascendente o descendente
     * @return un ArrayList de coche con todos los coches
     * @throws ExceptionMotor excepcion que integra el mensaje de error al
     * usuario, el codigo de error y el mensaje de error que nos ha devuelto la
     * base de datos
     */
    public ArrayList<Coche> leerCoches(String[] filtro, String orden) throws ExceptionMotor {
        ArrayList<Coche> cochesAux = new ArrayList<>();
        ArrayList<Coche> coches = new ArrayList<>();
        // Se obtiene la instancia de fábrica de sesiones
        //filtra solo por el DNI y el email, y el orden se añade al final sin 

        try {
            // Se crea la query HQL
            Query q = sesion.createQuery("from Coche where " + filtro[0] + " = :filtro order by " + orden);
            q.setString("filtro", filtro[1]);

            cochesAux = (ArrayList) q.list();
            coches = (ArrayList) cochesAux.clone();

        } catch (Exception e) {
            ExceptionMotor ex = new ExceptionMotor();

            ex.setCodigoErrorAdministrador(0);
            ex.setMensajeErrorUsuario("Error general en el sistema, Consulte con el administrador");
            ex.setMensajeErrorAdministrador(e.getMessage());

            throw ex;
        }

        return coches;

    }

    public void cerrarSesion() {
        // Se cierra la sesión
        sesion.close();
        //Se cierra la fábrica de sesiones
        fabricaSesiones.close();
    }
}
