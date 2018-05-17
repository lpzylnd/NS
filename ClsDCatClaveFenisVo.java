package SS.ICO.V10.Fau;

import java.util.Date;

public class ClsDCatClaveFenisVo {
    public ClsDCatClaveFenisVo() {
        super();
    }
    
        private String polVidPk = null;
        private String polVdescripcion = null;
        private String letra = null;
        private String fechaModifica = null;
        private String status = null;
        private String usuarioModifica = null;
        
    public String getPolVidPk() {
        return polVidPk;
    }

    public void setPolVidPk(String polVidPk) {
        this.polVidPk = polVidPk;
    }

    public String getPolVdescripcion() {
        return polVdescripcion;
    }

    public void setPolVdescripcion(String polVdescripcion) {
        this.polVdescripcion = polVdescripcion;
    }

    public String getLetra() {
        return letra;
    }

    public void setLetra(String letra) {
        this.letra = letra;
    }

    public String getFechaModifica() {
        return fechaModifica;
    }

    public void setFechaModifica(String fechaModifica) {
        this.fechaModifica = fechaModifica;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUsuarioModifica() {
        return usuarioModifica;
    }

    public void setUsuarioModifica(String usuarioModifica) {
        this.usuarioModifica = usuarioModifica;
    }

    public boolean isEmpty(){
        if(getPolVidPk() != null || getPolVdescripcion() != null ||
            getLetra() != null || getStatus() != null ){
            return false;
        }else{
            return true;
        }
    }
    
    public String toString(){
        StringBuffer s= new StringBuffer();
        s.append("Valores = [ ");
        s.append(getPolVidPk() != null ?  getPolVidPk() : "");
        s.append(" | ");
        s.append(getPolVdescripcion() != null ? getPolVdescripcion() : "");
        s.append(" | ");
        s.append(getLetra() != null ? getLetra() : "");
        s.append(" | ");
        s.append(getStatus() != null ? getStatus() : "");
        s.append(" | ");
        s.append(getUsuarioModifica() != null ? getUsuarioModifica() : "");
        s.append(" ] ");
        return s.toString();
    }


}
