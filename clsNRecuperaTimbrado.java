package SS.ICO.V10.Fau;

import org.apache.log4j.Logger;

import org.quartz.Job;
import org.quartz.JobExecutionContext;

public class clsNRecuperaTimbrado implements Job {
    Logger log = Logger.getLogger(this.getClass().getName());
    
    public void execute(JobExecutionContext jobExecutionContext) {
        try{
            ClsNGeneraArchivoFenis obj = new ClsNGeneraArchivoFenis();
            log.info("Inicio recuperacion timbrado...");
            obj.copiaArchivoFenis_BD();
            log.info("Fin recuperacion timbrado...");
        } catch(Exception e){
            log.error(e.getMessage(), e);
        }
    }
}
