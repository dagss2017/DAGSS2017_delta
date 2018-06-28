/*
 Proyecto Java EE, DAGSS-2014
 */

package es.uvigo.esei.dagss.dominio.daos;

import es.uvigo.esei.dagss.dominio.entidades.Receta;
import java.util.Date;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.TypedQuery;

@Stateless
@LocalBean
public class RecetaDAO extends GenericoDAO<Receta>{

    public List<Receta> obtenerRecetas(long id) {
        Date fecha = new Date();
        TypedQuery<Receta> q = em.createQuery("SELECT r FROM Receta AS r "
                                            + "WHERE r.prescripcion.paciente.id = :pac_id "
                                            + "AND (r.inicioValidez >= :fecha "
                                            + "OR r.finValidez >= :fecha) "
                                            + "ORDER BY r.inicioValidez"
                                            , Receta.class);
        
        q.setParameter("pac_id", id);
        q.setParameter("fecha", fecha);
        return q.getResultList();  
    }
}
