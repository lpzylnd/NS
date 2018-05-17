package SS.ICO.V10.Fau;

import java.util.Date;

public class ClsDCOpcionales {

    private int cuota;              //NCUOTA
    private String basico;          //VBASICO
    private String fecha;           //DFECHA
    private double traslado;        //NTRASLADO
    private double seguroTraslado;  //NSEGURO_TRASLADO
    private double promPubl;        //NPROM_PUBL
    private double asistVial;       //NASIST_VIAL
    private int iva;                //NIVA
    private double andanac;         //NANDANAC
    private double amda;            //NAMDA
    private double ctaPromei;       //NCTA_PROMEI
    private double segPlanPiso;     //NSEG_PLAN_PISO
    private double ayudaSocial;     //NAYUDA_SOCIAL
    private String compania;        //VCOMPANIA
    private Date fechaModifica;     //VFECHA_MODIFICA
    private String cve;
    
    public ClsDCOpcionales() {
        super();
    }

    public int getCuota() {
        return cuota;
    }

    public void setCuota(int cuota) {
        this.cuota = cuota;
    }

    public String getBasico() {
        return basico;
    }

    public void setBasico(String basico) {
        this.basico = basico;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public double getTraslado() {
        return traslado;
    }

    public void setTraslado(double traslado) {
        this.traslado = traslado;
    }

    public double getSeguroTraslado() {
        return seguroTraslado;
    }

    public void setSeguroTraslado(double seguroTraslado) {
        this.seguroTraslado = seguroTraslado;
    }

    public double getPromPubl() {
        return promPubl;
    }

    public void setPromPubl(double promPubl) {
        this.promPubl = promPubl;
    }

    public double getAsistVial() {
        return asistVial;
    }

    public void setAsistVial(double asistVial) {
        this.asistVial = asistVial;
    }

    public int getIva() {
        return iva;
    }

    public void setIva(int iva) {
        this.iva = iva;
    }

    public double getAndanac() {
        return andanac;
    }

    public void setAndanac(double andanac) {
        this.andanac = andanac;
    }

    public double getAmda() {
        return amda;
    }

    public void setAmda(double amda) {
        this.amda = amda;
    }

    public double getCtaPromei() {
        return ctaPromei;
    }

    public void setCtaPromei(double ctaPromei) {
        this.ctaPromei = ctaPromei;
    }

    public double getSegPlanPiso() {
        return segPlanPiso;
    }

    public void setSegPlanPiso(double segPlanPiso) {
        this.segPlanPiso = segPlanPiso;
    }

    public double getAyudaSocial() {
        return ayudaSocial;
    }

    public void setAyudaSocial(double ayudaSocial) {
        this.ayudaSocial = ayudaSocial;
    }

    public String getCompania() {
        return compania;
    }

    public void setCompania(String compania) {
        this.compania = compania;
    }

    public Date getFechaModifica() {
        return fechaModifica;
    }

    public void setFechaModifica(Date fechaModifica) {
        this.fechaModifica = fechaModifica;
    }

    public String getCve() {
        return cve;
    }

    public void setCve(String cve) {
        this.cve = cve;
    }
    
    public void generarClave(){
        this.setCve(this.getCuota() + this.getBasico() + this.getFecha());
    }
    
}
