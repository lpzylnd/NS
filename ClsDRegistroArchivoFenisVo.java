package SS.ICO.V10.Fau;

public class ClsDRegistroArchivoFenisVo {
    public ClsDRegistroArchivoFenisVo() {
        super();
    }
    
    private int idConfig;
    private String tipoRegistro;
    private int idCampo;
    private String nombreCampo;
    private float logitudCampo;
    private String confCampo;

    public String getTipoRegistro() {
        return tipoRegistro;
    }

    public void setTipoRegistro(String tipoRegistro) {
        this.tipoRegistro = tipoRegistro;
    }

    public int getIdCampo() {
        return idCampo;
    }

    public void setIdCampo(int idCampo) {
        this.idCampo = idCampo;
    }

    public String getNombreCampo() {
        return nombreCampo;
    }

    public void setNombreCampo(String nombreCampo) {
        this.nombreCampo = nombreCampo;
    }

    public float getLogitudCampo() {
        return logitudCampo;
    }

    public void setLogitudCampo(float logitudCampo) {
        this.logitudCampo = logitudCampo;
    }

    public String getConfCampo() {
        return confCampo;
    }

    public void setConfCampo(String confCampo) {
        this.confCampo = confCampo;
    }

    public int getIdConfig() {
        return idConfig;
    }

    public void setIdConfig(int idConfig) {
        this.idConfig = idConfig;
    }
    
    public String toString(){
        return this.getTipoRegistro() + this.getNombreCampo();
    }
    
//    @Override
//    public boolean equals(Object obj){
//        if(obj==null){
//            return false;
//        }
//        if(!(obj instanceof ClsDRegistroArchivoFenisVo)){
//            return false;
//        }
//        ClsDRegistroArchivoFenisVo o = (ClsDRegistroArchivoFenisVo)obj;
//        String claveInt = this.getTipoRegistro() + this.getNombreCampo();
//        String claveExt = o.getTipoRegistro()+o.getNombreCampo();
//        if(claveInt.equals(claveExt)){
//            return true;
//        }else{
//            return false;
//        }
//    }
//    
//    @Override
//    public int hashCode(){
//        int result=21;
//        result = 31*result+idConfig;
//        result = 31*result+(tipoRegistro!=null?tipoRegistro.hashCode():0);
//        result = 31*result+idCampo;
//        result = 31*result+(nombreCampo!=null?nombreCampo.hashCode():0);
//        result = 31*result+(confCampo!=null?confCampo.hashCode():0);
//        return result;
//    }
}
