package SS.ICO.V10.Fau;

import java.util.Date;

public class ClsDExcepcionesVo {
    
    private int cuota;              //NCUOTA
    private String basico;          //VBASICO
    private String fecha;           //DFECHA
    private String cabecera;        //VCABECERA
    private int distribuidor;       //NDISTRIBUIDOR
    private double traslado;        //NTRASLADO
    private Date fechaModifica;     //VFECHA_MODIFICA
    private String cve;
    
    public ClsDExcepcionesVo() {
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

    public String getCabecera() {
        return cabecera;
    }

    public void setCabecera(String cabecera) {
        this.cabecera = cabecera;
    }

    public int getDistribuidor() {
        return distribuidor;
    }

    public void setDistribuidor(int distribuidor) {
        this.distribuidor = distribuidor;
    }

    public double getTraslado() {
        return traslado;
    }

    public void setTraslado(double traslado) {
        this.traslado = traslado;
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