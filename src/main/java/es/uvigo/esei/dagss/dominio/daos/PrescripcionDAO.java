/*
 Proyecto Java EE, DAGSS-2016
 */
package es.uvigo.esei.dagss.dominio.daos;

import es.uvigo.esei.dagss.dominio.entidades.Prescripcion;
import java.util.Date;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.TypedQuery;

@Stateless
@LocalBean
public class PrescripcionDAO extends GenericoDAO<Prescripcion> {

    public Prescripcion buscarPorIdConRecetas(Long id) {
        TypedQuery<Prescripcion> q = em.createQuery("SELECT p FROM Prescripcion AS p JOIN FETCH p.recetas"
                                                  + "  WHERE p.id = :id", Prescripcion.class);
        q.setParameter("id", id);
        
        return q.getSingleResult();
    }
    
    public List<Prescripcion> obtenerPrescripciones(long id) {
        Date fecha = new Date();
        TypedQuery<Prescripcion> q = em.createQuery("SELECT p FROM Prescripcion AS p "
                                            + "WHERE p.paciente.id = :pac_id "
                                            + "AND p.fechaFin >= :fecha "
                                            + "ORDER BY p.fechaInicio "
                                            , Prescripcion.class);
        
        q.setParameter("pac_id", id);
        q.setParameter("fecha", fecha);
        return q.getResultList();
    }
}
