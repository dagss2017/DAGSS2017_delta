/*
 Proyecto Java EE, DAGSS-2014
 */
package es.uvigo.esei.dagss.dominio.daos;

import es.uvigo.esei.dagss.dominio.entidades.Medicamento;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.TypedQuery;

@Stateless
@LocalBean
public class MedicamentoDAO extends GenericoDAO<Medicamento> {

    public List<Medicamento> buscar(String campo) {
        TypedQuery<Medicamento> q = em.createQuery("SELECT m FROM Medicamento AS m "
                                                + "WHERE m.nombre LIKE '%" + campo + "%' "
                                                + "OR m.fabricante LIKE '%" + campo + "%' "
                                                + "OR m.familia LIKE '%" + campo + "%' "
                                                + "OR m.principioActivo LIKE '%" + campo + "%' "
                                                + "ORDER BY m.nombre "
                                                , Medicamento.class);
        return q.getResultList();
    }
}
