package SS.ICO.V10.Fau;

import java.util.Date;

public class ClsDCatPoliticaVentaVo {
    public ClsDCatPoliticaVentaVo() {
        super();
    }
    
        private String polVidPk = null;
        private String polVdescripcion = null;
        private String polVfinanciera = null;
        private String polVpau = null;
        private String tvtavVnopagincentivo = null;
        private String polVmc = null;
        private String polVte = null;
        private String vcolateral = null;
        private String vdescolateral = null;
        private String vvigencia = null;
        private String vfechamodif = null;
        private Float incNporcFlotilla = null;
        private Integer nidCategoria = null;

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

    public String getPolVfinanciera() {
        return polVfinanciera;
    }

    public void setPolVfinanciera(String polVfinanciera) {
        this.polVfinanciera = polVfinanciera;
    }

    public String getPolVpau() {
        return polVpau;
    }

    public void setPolVpau(String polVpau) {
        this.polVpau = polVpau;
    }

    public String getTvtavVnopagincentivo() {
        return tvtavVnopagincentivo;
    }

    public void setTvtavVnopagincentivo(String tvtavVnopagincentivo) {
        this.tvtavVnopagincentivo = tvtavVnopagincentivo;
    }

    public String getPolVmc() {
        return polVmc;
    }

    public void setPolVmc(String polVmc) {
        this.polVmc = polVmc;
    }

    public String getPolVte() {
        return polVte;
    }

    public void setPolVte(String polVte) {
        this.polVte = polVte;
    }

    public String getVcolateral() {
        return vcolateral;
    }

    public void setVcolateral(String vcolateral) {
        this.vcolateral = vcolateral;
    }

    public String getVdescolateral() {
        return vdescolateral;
    }

    public void setVdescolateral(String vdescolateral) {
        this.vdescolateral = vdescolateral;
    }

    public String getVvigencia() {
        return vvigencia;
    }

    public void setVvigencia(String vvigencia) {
        this.vvigencia = vvigencia;
    }

    public String getVfechamodif() {
        return vfechamodif;
    }

    public void setVfechamodif(String vfechamodif) {
        this.vfechamodif = vfechamodif;
    }

    public Float getIncNporcFlotilla() {
        return incNporcFlotilla;
    }

    public void setIncNporcFlotilla(Float incNporcFlotilla) {
        this.incNporcFlotilla = incNporcFlotilla;
    }

    public Integer getNidCategoria() {
        return nidCategoria;
    }

    public void setNidCategoria(Integer nidCategoria) {
        this.nidCategoria = nidCategoria;
    }
    
    public boolean isEmpty(){
        if(polVidPk !=null || polVdescripcion!=null || polVfinanciera!=null ||
            polVpau!=null || tvtavVnopagincentivo !=null || polVmc!=null ||
            polVte!=null || vcolateral!=null || vdescolateral!=null ||
            vvigencia!=null || incNporcFlotilla!=null || nidCategoria!=null){
            return false;
        }else{
            return true;
        }
        
    }
    
    public String toString(){
        StringBuffer s= new StringBuffer();
        s.append("Valores = [ ");
        s.append(polVidPk!=null? polVidPk: "");
        s.append(" | ");
        s.append(polVdescripcion!=null? polVdescripcion : "");
        s.append(" | ");
        s.append(polVfinanciera!=null? polVfinanciera: "");
        s.append(" | ");
        s.append(polVpau!=null? polVpau : "");
        s.append(" | ");
        s.append(tvtavVnopagincentivo!=null? tvtavVnopagincentivo : ""); 
        s.append(" | ");
        s.append(polVmc!=null? polVmc : "");
        s.append(" | ");
        s.append(polVte!=null? polVte : "");
        s.append(" | ");
        s.append(vcolateral!=null? vcolateral : "");
        s.append(" | ");
        s.append(vdescolateral!=null? vdescolateral : "");
        s.append(" | ");
        s.append(vvigencia!=null? vvigencia : "");
        s.append(" | ");
        s.append(incNporcFlotilla!=null? incNporcFlotilla : "");
        s.append(" | ");
        s.append(nidCategoria!=null? nidCategoria : "");
        s.append(" ] ");
        return s.toString();
    }
}
