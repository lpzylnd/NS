package SS.ICO.V10.Fau;

public class ClsDColoresExtVo {
    
    private int anio;               //ANIO_NID_PK
    private int idSector;           //SECT_NID
    private String codigo;          //EXT_VCODIGO_PK
    private String descripcion;     //EXT_VDESCRIPCION
    private String idOrigen;        //ORIG_VID_PK
    private String convColor;        //R_CONV_COL_INT_PAIS
    private String origenDesc;
    private String sectorDesc;
    private String clave;
    
    public ClsDColoresExtVo() {
        super();
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public int getIdSector() {
        return idSector;
    }

    public void setIdSector(int idSector) {
        this.idSector = idSector;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getIdOrigen() {
        return idOrigen;
    }

    public void setIdOrigen(String idOrigen) {
        this.idOrigen = idOrigen;
    }

    public String getConvColor() {
        return convColor;
    }

    public void setConvColor(String convColor) {
        this.convColor = convColor;
    }

    public String getOrigenDesc() {
        return origenDesc;
    }

    public void setOrigenDesc(String origenDesc) {
        this.origenDesc = origenDesc;
    }

    public String getSectorDesc() {
        return sectorDesc;
    }

    public void setSectorDesc(String sectorDesc) {
        this.sectorDesc = sectorDesc;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }
    
    public void calcularClave(){
        this.setClave(getAnio() + getIdOrigen() + getCodigo() + getIdSector());
    }
}
