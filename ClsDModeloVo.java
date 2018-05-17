package SS.ICO.V10.Fau;


public class ClsDModeloVo {
    public ClsDModeloVo() {
        super();
    }
    private int numero;
   private int  cuota;  // (Año modelo)
   private String eim ;   //     (EIM )
   private String cabecera ;  // (Cabecera)
   private String origen ; //(Doméstico /Importado)
   private int sector ;   //      (10-Nissan/20-Infinity)
   private String descripcion; //del EIM    
   private String grupoMaterial ; //(Catálogo DE equivalencias SAP)
   private String secuenciaMayoreo; //Secuencia Menudeo
   private String fechaAlta;
   private String fechaCambio;
   private String usuarioCambio;
    public int getCuota() {
        return cuota;
    }

    public void setCuota(int cuota) {
        this.cuota = cuota;
    }

    public String getCabecera() {
        return cabecera;
    }

    public void setCabecera(String modelos) {
        this.cabecera = modelos;
    }

    public String getEim() {
        return eim;
    }

    public void setEim(String tipo) {
        this.eim = tipo;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public int getSector() {
        return sector;
    }

    public void setSector(int sector) {
        this.sector = sector;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getGrupoMaterial() {
        return grupoMaterial;
    }

    public void setGrupoMaterial(String grupoMaterial) {
        this.grupoMaterial = grupoMaterial;
    }

    public String getSecuenciaMayoreo() {
        return secuenciaMayoreo;
    }

    public void setSecuenciaMayoreo(String secuenciaMayoreo) {
        this.secuenciaMayoreo = secuenciaMayoreo;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(String fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public String getFechaCambio() {
        return fechaCambio;
    }

    public void setFechaCambio(String fechaCambio) {
        this.fechaCambio = fechaCambio;
    }

    public String getUsuarioCambio() {
        return usuarioCambio;
    }

    public void setUsuarioCambio(String usuarioCambio) {
        this.usuarioCambio = usuarioCambio;
    }
}
