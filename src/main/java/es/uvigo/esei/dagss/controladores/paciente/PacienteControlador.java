/*
 Proyecto Java EE, DAGSS-2013
 */
package es.uvigo.esei.dagss.controladores.paciente;

import es.uvigo.esei.dagss.controladores.autenticacion.AutenticacionControlador;
import es.uvigo.esei.dagss.dominio.daos.CitaDAO;
import es.uvigo.esei.dagss.dominio.daos.PacienteDAO;
import es.uvigo.esei.dagss.dominio.daos.PrescripcionDAO;
import es.uvigo.esei.dagss.dominio.entidades.Cita;
import es.uvigo.esei.dagss.dominio.entidades.Paciente;
import es.uvigo.esei.dagss.dominio.entidades.Prescripcion;
import es.uvigo.esei.dagss.dominio.entidades.TipoUsuario;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

/**
 *
 * @author ribadas
 */
@Named(value = "pacienteControlador")
@SessionScoped
public class PacienteControlador implements Serializable {

    private Paciente pacienteActual;
    private String dni;
    private String numeroTarjetaSanitaria;
    private String numeroSeguridadSocial;
    private String password;
    private Cita citaActual;

    @Inject
    private AutenticacionControlador autenticacionControlador;

    @Inject
    private PacienteDAO pacienteDAO;
    
    @Inject
    private PrescripcionDAO prescripcionDAO;
    
    @Inject
    private CitaDAO citaDAO;

    /**
     * Creates a new instance of AdministradorControlador
     */
    public PacienteControlador() {
    }

    @PostConstruct
    public void inicializar() {
    }

    public Paciente getPacienteActual() {
        return pacienteActual;
    }

    public void setPacienteActual(Paciente pacienteActual) {
        this.pacienteActual = pacienteActual;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNumeroTarjetaSanitaria() {
        return numeroTarjetaSanitaria;
    }

    public void setNumeroTarjetaSanitaria(String numeroTarjetaSanitaria) {
        this.numeroTarjetaSanitaria = numeroTarjetaSanitaria;
    }

    public String getNumeroSeguridadSocial() {
        return numeroSeguridadSocial;
    }

    public void setNumeroSeguridadSocial(String numeroSeguridadSocial) {
        this.numeroSeguridadSocial = numeroSeguridadSocial;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Cita getCitaActual() {
        return citaActual;
    }

    public void setCitaActual(Cita citaActual) {
        this.citaActual = citaActual;
    }
    
    private boolean parametrosAccesoInvalidos() {
        return (((dni == null) && (numeroSeguridadSocial == null) && (numeroTarjetaSanitaria == null))
                || (password == null));
    }

    private Paciente recuperarDatosPaciente() {
        Paciente paciente = null;
        if (dni != null) {
            paciente = pacienteDAO.buscarPorDNI(dni);
        }
        if ((paciente == null) && (numeroSeguridadSocial != null)) {
            paciente = pacienteDAO.buscarPorNumeroSeguridadSocial(numeroSeguridadSocial);
        }
        if ((paciente == null) && (numeroTarjetaSanitaria != null)) {
            paciente = pacienteDAO.buscarPorTarjetaSanitaria(numeroTarjetaSanitaria);
        }
        return paciente;
    }

    public String doLogin() {
        String destino = null;
        if (parametrosAccesoInvalidos()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "No se ha indicado suficientes datos de autenticación", ""));
        } else {
            Paciente paciente = recuperarDatosPaciente();
            if (paciente == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "No existe ningún paciente con los datos indicados", ""));
            } else {
                if (autenticacionControlador.autenticarUsuario(
                        paciente.getId(),
                        password,
                        TipoUsuario.PACIENTE.getEtiqueta().toLowerCase())) {

                    pacienteActual = paciente;

// TODO:  revisar acceso sin password en primer uso
//                    if (paciente.getPassword().equals("")) {
//                        destino = "privado/cambiarPassword";
//                    } else {
                        destino = "privado/index";
//                    }
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Credenciales de acceso incorrectas", ""));
                }
            }
        }
        return destino;
    }
    
    public String buscarPaciente() {
        String destino = null;
        
        if (numeroTarjetaSanitaria == null || numeroTarjetaSanitaria.length() == 0) {
            destino = "index";
        } else {
            pacienteActual = pacienteDAO.buscarPorTarjetaSanitaria(numeroTarjetaSanitaria);
            if (pacienteActual == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "No existe ningún paciente con los datos indicados", ""));
            } else {
                destino = "recetas";
            }
        }
        
        return destino;
    }
    
    public String verPrescripciones(Cita c) {
        this.citaActual = c;
        this.pacienteActual = c.getPaciente();
        return "formPaciente";
    }
    
    public List<Prescripcion> obtenerPrescripciones() {
        return prescripcionDAO.obtenerPrescripciones(pacienteActual.getId());
    }
    
    public String completarCita() {
        citaActual.setEstado("Realizada");
        
        try {
            citaDAO.actualizar(citaActual);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "La cita se ha completado", ""));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "No se ha podido completar la cita", ""));
        }
       
        return "citas";
    }
    
    public String ausentarCita() {
        citaActual.setEstado("Ausente");
        
        try {
            citaDAO.actualizar(citaActual);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "El paciente no se ha presentado a la cita", ""));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "No se ha podido modificar el estado de la cita", ""));
        }
       
        return "citas";
    }
}
