package entities;

/**
 * Clase Empresa (A) - Entidad principal con relación 1→1 unidireccional a DomicilioFiscal.
 */
public class Empresa {
    
    private Long id;
    private Boolean eliminado;
    private String razonSocial;
    private String cuit;
    private String actividadPrincipal;
    private String email;
    private DomicilioFiscal domicilioFiscal;
    
    // Constructores
    public Empresa() {
        this.eliminado = false;
    }
    
    public Empresa(String razonSocial, String cuit, String actividadPrincipal, String email) {
        this();
        this.razonSocial = razonSocial;
        this.cuit = cuit;
        this.actividadPrincipal = actividadPrincipal;
        this.email = email;
    }
    
    public Empresa(Long id, Boolean eliminado, String razonSocial, String cuit, 
                   String actividadPrincipal, String email, DomicilioFiscal domicilioFiscal) {
        this.id = id;
        this.eliminado = eliminado;
        this.razonSocial = razonSocial;
        this.cuit = cuit;
        this.actividadPrincipal = actividadPrincipal;
        this.email = email;
        this.domicilioFiscal = domicilioFiscal;
    }
    
    // Getters y Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Boolean getEliminado() {
        return eliminado;
    }
    
    public void setEliminado(Boolean eliminado) {
        this.eliminado = eliminado;
    }
    
    public String getRazonSocial() {
        return razonSocial;
    }
    
    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }
    
    public String getCuit() {
        return cuit;
    }
    
    public void setCuit(String cuit) {
        this.cuit = cuit;
    }
    
    public String getActividadPrincipal() {
        return actividadPrincipal;
    }
    
    public void setActividadPrincipal(String actividadPrincipal) {
        this.actividadPrincipal = actividadPrincipal;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public DomicilioFiscal getDomicilioFiscal() {
        return domicilioFiscal;
    }
    
    public void setDomicilioFiscal(DomicilioFiscal domicilioFiscal) {
        this.domicilioFiscal = domicilioFiscal;
    }
    
    @Override
    public String toString() {
        return "Empresa{" +
                "id=" + id +
                ", razonSocial='" + razonSocial + '\'' +
                ", cuit='" + cuit + '\'' +
                ", actividadPrincipal='" + actividadPrincipal + '\'' +
                ", email='" + email + '\'' +
                ", domicilioFiscal=" + (domicilioFiscal != null ? "Sí" : "No") +
                '}';
    }
}
