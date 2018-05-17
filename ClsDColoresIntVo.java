package SS.ICO.V10.Fau;

public class ClsDColoresIntVo {
    
    private int anio;               //ANIO_NID_PK
    private String codigo;          //INT_VCODIGO_PK
    private String descripcion;     //INT_VDESCRIPCION
    private String idOrigen;        //ORIG_VID_PK
    private String idPais;          //PAIS_VID_PK
    private String convColor;        //R_CONV_COL_INT_PAIS
    private String clave;
    private String origenDesc;
    private String paisDesc;
    
    public ClsDColoresIntVo() {
        super();
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
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

    public String getIdPais() {
        return idPais;
    }

    public void setIdPais(String idPais) {
        this.idPais = idPais;
    }

    public String getConvColor() {
        return convColor;
    }

    public void setConvColor(String convColor) {
        this.convColor = convColor;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }
    
    public void calcularClave(){
        setClave(getAnio() + getIdOrigen() + getCodigo() + getIdPais());
    }

    public String getOrigenDesc() {
        return origenDesc;
    }

    public void setOrigenDesc(String origenDesc) {
        this.origenDesc = origenDesc;
    }

    public String getPaisDesc() {
        return paisDesc;
    }

    public void setPaisDesc(String paisDesc) {
        this.paisDesc = paisDesc;
    }
}
