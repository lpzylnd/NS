package SS.ICO.V10.Fau;

public class ClsDPlantasVo {
    
    private String id;                  //PLTA_VID_PK
    private String descripcion;         //PLTA_VDESCRIPCION
    private String equivalenciasSap;    //PLTA_EQUIVALENCIAS_SAP
    
    public ClsDPlantasVo() {
        super();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEquivalenciasSap() {
        return equivalenciasSap;
    }

    public void setEquivalenciasSap(String equivalenciasSap) {
        this.equivalenciasSap = equivalenciasSap;
    }
}
