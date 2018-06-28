/*
 Proyecto Java EE, DAGSS-2016
 */
package es.uvigo.esei.dagss.dominio.daos;

import es.uvigo.esei.dagss.dominio.entidades.Prescripcion;
import es.uvigo.esei.dagss.dominio.entidades.Receta;
import es.uvigo.esei.dagss.dominio.entidades.Medicamento;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

@Stateless
@LocalBean
public class PlanDeRecetas {
    
    public List<Receta> hacerPlan(Prescripcion prescripcion, Medicamento medicamentoSeleccionado) {
        List<Receta> listaRecetas = new ArrayList<>();
        Date d = new Date();
        int milisSemana = 604800000;
        int aux3;
        long aux = ((prescripcion.getFechaFin().getTime() - d.getTime()) / 86400000) + 1;
        int dias = (int) aux;
        int dosisTotal = dias * prescripcion.getDosis();
        double aux2 = Math.ceil((float) dosisTotal / (float) medicamentoSeleccionado.getNumeroDosis());
        int numRecetas = (int) aux2;
        Receta r = new Receta(prescripcion, medicamentoSeleccionado.getNumeroDosis(), null, null, "Generada");

        for (int i = 0; i < numRecetas; i++) {
            // Semana actual
            aux3 = i * milisSemana;
            d = new Date();
            d.setTime(d.getTime() + aux3);

            if (i == 0) {
                // Semana antes de la fecha de inicio salvo en la primera receta
                r.setInicioValidez(prescripcion.getFechaInicio());
            } else {
                // Semana de margen
                d.setTime(d.getTime()- milisSemana);
                r.setInicioValidez(d);
            }
            // Semana despues de la fecha de fin
            d = new Date();
            d.setTime(d.getTime() + aux3 + milisSemana);
            r.setFinValidez(d);
            listaRecetas.add(r);
        }
        
        return listaRecetas;
    }
    
}
