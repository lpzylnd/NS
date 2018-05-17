package SS.ICO.V10.Fau;

import SS.ICO.V10.Common.clsIEjecutable;
import SS.ICO.V10.Common.clsISesion;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.PageContext;

import org.apache.log4j.Logger;

public class ClsECatPlantaFaudi extends clsIEjecutable {
    
    /**
    * Almacena la acci&oacuten que realizar&aacute el ejecutable.
    */
    private String mstrAccion;
    /**
    * Almacena la sesi&oacuten de la clase.
    */
    private clsISesion mobjSesion;
    
    Logger logger = Logger.getLogger(ClsECatPlantaFaudi.class);
    
    public ClsECatPlantaFaudi() {
        super();
    }

    public ClsECatPlantaFaudi(String accion) {
        this.mstrAccion = accion;
    }

    @Override
    public String Procesa(HttpServletRequest lobjRequest, HttpServletResponse lobjResponse, PageContext lobjContext) {
        
        HttpSession lsesSesion      = lobjRequest.getSession(true);        
        /* String con la direccion del JSP de resultado */
        String lstrJSP              = "../Common/clsJAcercaDe.jsp";  
        /* Se crea el objeto de negocio del proceso */
        ClsNCatPlantaFaudi mobjBase = null;
        
        logger.info("Procesa( " + mstrAccion + " )");
        logger.info("Inicia accion = " + mstrAccion);
        
        if (mstrAccion.compareTo("Inicio") == 0) {
            /* Carga inicial de la pantalla de la opción: Parámetros */
            lsesSesion.removeAttribute("mobjBase");
            mobjBase = new ClsNCatPlantaFaudi();
            mobjBase.Inicializa(mobjSesion);
            mobjBase.setMensaje("");  
           
            lstrJSP =  "./clsJCatPlantaFaudi.jsp"; 
            lsesSesion.setAttribute("mobjBase",mobjBase);
        }else if(mstrAccion.equals("Consultar")){
            mobjBase = (ClsNCatPlantaFaudi) lsesSesion.getAttribute("mobjBase");
            lsesSesion.removeAttribute("mobjBase");
            mobjBase.setMensaje("");
            
            String planta = lobjRequest.getParameter("txtPlanta");
            mobjBase.obtenerEquivalenciaSap(planta);
            
            mobjBase.setConsultaPlanta(true);
            
            lstrJSP =  "./clsJCatPlantaFaudi.jsp"; 
            lsesSesion.setAttribute("mobjBase",mobjBase);
        }else if(mstrAccion.equals("Guardar")){
            mobjBase = (ClsNCatPlantaFaudi) lsesSesion.getAttribute("mobjBase");
            lsesSesion.removeAttribute("mobjBase");
            mobjBase.setMensaje("");
            
            String[] planta           = mobjBase.getPlanta().split("-");
            String equivalenciaSap    = lobjRequest.getParameter("txtEquivalenciasSap");
            
            mobjBase.actualizaEquivalenciaSap(planta[0].trim(), equivalenciaSap);
            mobjBase.Inicializa(mobjSesion);
            
            lstrJSP =  "./clsJCatPlantaFaudi.jsp"; 
            lsesSesion.setAttribute("mobjBase",mobjBase);
        }else if(mstrAccion.equals("Eliminar")){
            mobjBase = (ClsNCatPlantaFaudi) lsesSesion.getAttribute("mobjBase");
            lsesSesion.removeAttribute("mobjBase");
            mobjBase.setMensaje("");
            
            String[] planta = mobjBase.getPlanta().split("-");
            
            mobjBase.actualizaEquivalenciaSap(planta[0].trim(), "");
            mobjBase.Inicializa(mobjSesion);
            
            lstrJSP =  "./clsJCatPlantaFaudi.jsp"; 
            lsesSesion.setAttribute("mobjBase",mobjBase);
        }
        
        return lstrJSP;
    }

    @Override
    public boolean NoLogin(clsISesion lobjSesion) {
        boolean lbNoLogin = true;
        mobjSesion = lobjSesion;

        if (mobjSesion != null) {
            if (mobjSesion.Menu() != null) {
                if (mobjSesion.Menu().size() > 0) {
                    lbNoLogin = false;
                }
            }
        }
        return lbNoLogin;
    }

    @Override
    public boolean NoAcceso(clsISesion lobjSesion) {
        boolean lbNoAcceso = true;
        mobjSesion = lobjSesion;

        if (mobjSesion != null) {
            if (mobjSesion.TieneAcceso(4829)) {
                lbNoAcceso = false;
            }
        }
        return lbNoAcceso;
    }
}
