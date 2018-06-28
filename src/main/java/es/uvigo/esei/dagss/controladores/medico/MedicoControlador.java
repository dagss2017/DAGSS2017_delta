/*
 Proyecto Java EE, DAGSS-2013
 */
package es.uvigo.esei.dagss.controladores.medico;

import es.uvigo.esei.dagss.controladores.autenticacion.AutenticacionControlador;
import es.uvigo.esei.dagss.dominio.daos.CitaDAO;
import es.uvigo.esei.dagss.dominio.daos.MedicamentoDAO;
import es.uvigo.esei.dagss.dominio.daos.MedicoDAO;
import es.uvigo.esei.dagss.dominio.daos.PlanDeRecetas;
import es.uvigo.esei.dagss.dominio.daos.PrescripcionDAO;
import es.uvigo.esei.dagss.dominio.daos.RecetaDAO;
import es.uvigo.esei.dagss.dominio.entidades.Cita;
import es.uvigo.esei.dagss.dominio.entidades.Medicamento;
import es.uvigo.esei.dagss.dominio.entidades.Medico;
import es.uvigo.esei.dagss.dominio.entidades.Paciente;
import es.uvigo.esei.dagss.dominio.entidades.Prescripcion;
import es.uvigo.esei.dagss.dominio.entidades.Receta;
import es.uvigo.esei.dagss.dominio.entidades.TipoUsuario;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

/**
 *
 * @author ribadas
 */

@Named(value = "medicoControlador")
@SessionScoped
public class MedicoControlador implements Serializable {

    private Medico medicoActual;
    private String dni;
    private String numeroColegiado;
    private String password;
    private List<Cita> citas;
    private String campoBusqueda;
    private List<Medicamento> medicamentos;
    private Medicamento medicamentoSeleccionado;
    private Prescripcion prescripcion;
    private Paciente pacienteActual;
    private int dosis;
    private String fechaFin;
    private String indicaciones;
    private List<Receta> listaRecetas;

    @Inject
    private AutenticacionControlador autenticacionControlador;

    @EJB
    private MedicoDAO medicoDAO;
    
    @EJB
    private CitaDAO citaDAO;

    @EJB
    private MedicamentoDAO medicamentoDAO;
    
    @EJB
    private PrescripcionDAO prescripcionDAO;
    
    @EJB
    private PlanDeRecetas plan;
    
    @EJB
    private RecetaDAO recetaDAO;
    
    /**
     * Creates a new instance of AdministradorControlador
     */
    public MedicoControlador() {
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public List<Cita> getCitas() {
        return citas;
    }

    public void setCitas(List<Cita> citas) {
        this.citas = citas;
    }

    public String getNumeroColegiado() {
        return numeroColegiado;
    }

    public void setNumeroColegiado(String numeroColegiado) {
        this.numeroColegiado = numeroColegiado;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Medico getMedicoActual() {
        return medicoActual;
    }

    public void setMedicoActual(Medico medicoActual) {
        this.medicoActual = medicoActual;
    }

    public String getCampoBusqueda() {
        return campoBusqueda;
    }

    public void setCampoBusqueda(String campoBusqueda) {
        this.campoBusqueda = campoBusqueda;
    }

    public List<Medicamento> getMedicamentos() {
        return medicamentos;
    }

    public void setMedicamentos(List<Medicamento> medicamentos) {
        this.medicamentos = medicamentos;
    }

    public Medicamento getMedicamentoSeleccionado() {
        return medicamentoSeleccionado;
    }

    public void setMedicamentoSeleccionado(Medicamento medicamentoSeleccionado) {
        this.medicamentoSeleccionado = medicamentoSeleccionado;
    }

    public Prescripcion getPrescripcion() {
        return prescripcion;
    }

    public void setPrescripcion(Prescripcion prescripcion) {
        this.prescripcion = prescripcion;
    }

    public int getDosis() {
        return dosis;
    }

    public void setDosis(int dosis) {
        this.dosis = dosis;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getIndicaciones() {
        return indicaciones;
    }

    public void setIndicaciones(String indicaciones) {
        this.indicaciones = indicaciones;
    }
    
    private boolean parametrosAccesoInvalidos() {
        return (((dni == null) && (numeroColegiado == null)) || (password == null));
    }

    private Medico recuperarDatosMedico() {
        Medico medico = null;
        if (dni != null) {
            medico = medicoDAO.buscarPorDNI(dni);
        }
        if ((medico == null) && (numeroColegiado != null)) {
            medico = medicoDAO.buscarPorNumeroColegiado(numeroColegiado);
        }
        return medico;
    }

    public String doLogin() {
        String destino = null;
        if (parametrosAccesoInvalidos()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "No se ha indicado un DNI ó un número de colegiado o una contraseña", ""));
        } else {
            Medico medico = recuperarDatosMedico();
            if (medico == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "No existe ningún médico con los datos indicados", ""));
            } else {
                if (autenticacionControlador.autenticarUsuario(medico.getId(), password,
                        TipoUsuario.MEDICO.getEtiqueta().toLowerCase())) {
                    medicoActual = medico;
                    destino = "privado/index";
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Credenciales de acceso incorrectas", ""));
                }
            }
        }
        return destino;
    }

    //Acciones
    public String doShowCita() {
        return "detallesCita";
    }
    
    public void obtenerCitas(long id) {
        this.citas = citaDAO.obtenerCitas(id);
    }
    
    public boolean estaPendiente(Cita c) {
        return c.getEstado().trim().equalsIgnoreCase("pendiente");
    }
    
    public String nuevaPrescripcion(Paciente p) {
        this.pacienteActual = p;
        return "buscarMedicamento";
    }
    
    public String buscarMedicamento() {
        medicamentos = medicamentoDAO.buscar(campoBusqueda);
        
        if (medicamentos == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "No existe ningún medicamento con los datos indicados", ""));
        }

        return "buscarMedicamento";
    }
    
    public boolean medicamentosVacio() {
        return medicamentos == null;
    }
    
    public String seleccionarMedicamento(Medicamento m) {
        this.medicamentoSeleccionado = m;
        return "crearPrescripcion";
    }
    
    public String crearPrescripcion() {
        Date fechaInicio = new Date();
        Date fechaTest;
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        
        prescripcion = new Prescripcion();        
        prescripcion.setFechaInicio(fechaInicio);
        prescripcion.setIndicaciones(indicaciones);
        prescripcion.setMedicamento(medicamentoSeleccionado);
        prescripcion.setMedico(medicoActual);
        prescripcion.setPaciente(pacienteActual);
        
        try {
            if(dosis < 1) {
                throw new NumberFormatException("La dosis debe ser mayor de 0");
            }
            prescripcion.setDosis(dosis);
            fechaTest = f.parse(fechaFin);
            if(f.format(fechaTest).compareTo(f.format(fechaInicio)) < 0){
                throw new DateTimeException("La fecha de fin no debe ser anterior a la fecha actual");
            }
            prescripcion.setFechaFin(fechaTest);
            prescripcionDAO.crear(prescripcion);
            
            listaRecetas = plan.hacerPlan(prescripcion, medicamentoSeleccionado);
            
            for (Receta receta: listaRecetas) {
                recetaDAO.crear(receta);
            } 
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Se ha creado la prescripción", ""));
        } catch(NumberFormatException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), ""));
        } catch(DateTimeException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), ""));
        } catch(ParseException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ParseException", ""));
        } catch(Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fallo al crear la prescripción", ""));
        }
        
        return "formPaciente";
    }
    
    public String eliminarPrescripcion(long id) {
        prescripcionDAO.eliminar(prescripcionDAO.buscarPorId(id));
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Se ha eliminado la prescripción", ""));
        return "formPaciente";  
    }
}
