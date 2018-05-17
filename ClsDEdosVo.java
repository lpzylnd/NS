package SS.ICO.V10.Fau;


public class ClsDEdosVo {
    public ClsDEdosVo() {
        super();
    }
    private int id;
    private String clvDist;
    private String descripcion;
    private String clave;
    private String sector;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getClvDist() {
        return clvDist;
    }

    public void setClvDist(String clvDist) {
        this.clvDist = clvDist;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }
}
