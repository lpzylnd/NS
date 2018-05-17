package SS.ICO.V10.Fau;

import org.apache.log4j.Logger;


public class clsNEjecutaArchivosFenis extends Thread implements Runnable {
    private int totalDom;
    private int totalImp;
    private int totalInf;
    
    Logger log = Logger.getLogger(this.getClass().getName());
    
    @Override
    public void run() {
        try{
            if(getTotalDom() > 0) {
                ClsNGeneraArchivoFenis dom = new ClsNGeneraArchivoFenis();
                dom.crearArchivosFenis(1);
            }
            
            if(getTotalImp() > 0) {
                ClsNGeneraArchivoFenis imp = new ClsNGeneraArchivoFenis();
                imp.crearArchivosFenis(2);
            }
            
            if(getTotalInf() > 0) {
                ClsNGeneraArchivoFenis inf = new ClsNGeneraArchivoFenis();
                inf.crearArchivosFenis(3);
            }
            
            
            
        } catch(Exception e){
            log.error(e.getMessage(), e);
        }
    }

    public int getTotalDom() {
        return totalDom;
    }

    public void setTotalDom(int totalDom) {
        this.totalDom = totalDom;
    }

    public int getTotalImp() {
        return totalImp;
    }

    public void setTotalImp(int totalImp) {
        this.totalImp = totalImp;
    }

    public int getTotalInf() {
        return totalInf;
    }

    public void setTotalInf(int totalInf) {
        this.totalInf = totalInf;
    }
}
