package SS.ICO.V10.Fau;


public class ClsDBasicoVo {
    public ClsDBasicoVo() {
        super();
    }
    private   String  bas_vid_pk;
      private String bas_linea_modelo_pk;
      private String bas_vdescripcion;
      private String bas_vactivo;
      private int  sect_nid;

    public String getBas_vid_pk() {
        return bas_vid_pk;
    }

    public void setBas_vid_pk(String bas_vid_pk) {
        this.bas_vid_pk = bas_vid_pk;
    }

    public String getBas_linea_modelo_pk() {
        return bas_linea_modelo_pk;
    }

    public void setBas_linea_modelo_pk(String bas_linea_modelo_pk) {
        this.bas_linea_modelo_pk = bas_linea_modelo_pk;
    }

    public String getBas_vdescripcion() {
        return bas_vdescripcion;
    }

    public void setBas_vdescripcion(String bas_vdescripcion) {
        this.bas_vdescripcion = bas_vdescripcion;
    }

    public String getBas_vactivo() {
        return bas_vactivo;
    }

    public void setBas_vactivo(String bas_vactivo) {
        this.bas_vactivo = bas_vactivo;
    }

    public int getSect_nid() {
        return sect_nid;
    }

    public void setSect_nid(int sect_nid) {
        this.sect_nid = sect_nid;
    }
}
