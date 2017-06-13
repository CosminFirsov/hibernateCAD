/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package motor;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class SessionFactoryUtil {
    private static final SessionFactory sf;
        static {
            try {
                // Se inicializa el entorno Hibernate y se carga el fichero hibernate.cfg.xml
                // Se crea la fábrica de sesiones
                sf = new Configuration().configure().buildSessionFactory();
            } catch (HibernateException ex) {
                System.out.println("Error: " + ex.getMessage());
                throw new ExceptionInInitializerError(ex);
            }
        }

        public static SessionFactory getSessionFactory() {
            return sf;
        }
    
}
