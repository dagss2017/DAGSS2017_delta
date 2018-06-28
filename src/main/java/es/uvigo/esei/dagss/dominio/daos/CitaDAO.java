/*
 Proyecto Java EE, DAGSS-2014
 */

package es.uvigo.esei.dagss.dominio.daos;

import es.uvigo.esei.dagss.dominio.entidades.Cita;
import java.util.Date;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.TypedQuery;


@Stateless
@LocalBean
public class CitaDAO  extends GenericoDAO<Cita>{    

   public List<Cita> obtenerCitas(long id) {
        Date fecha = new Date();
        TypedQuery<Cita> q = em.createQuery("SELECT c FROM Cita AS c "
                                            + "WHERE c.medico.id = :med_id "
                                            + "AND c.fecha = :fecha "
                                            + "ORDER BY c.hora "
                                            , Cita.class);
        
        q.setParameter("med_id", id);
        q.setParameter("fecha", fecha);
        return q.getResultList();  
    }
}
