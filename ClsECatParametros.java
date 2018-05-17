package SS.ICO.V10.Fau;

import SS.ICO.V10.Common.clsIEjecutable;
import SS.ICO.V10.Common.clsISesion;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.PageContext;

import org.apache.log4j.Logger;

public class ClsECatParametros extends clsIEjecutable{
    public ClsECatParametros() {
        super();
    }
    /**
     * Almacena la acci&oacuten que realizar&aacute el ejecutable.
     */
    private String mstrAccion;
    /**
     * Almacena la sesi&oacuten de la clase.
     */
    private clsISesion mobjSesion;
            Logger log = Logger.getLogger(this.getClass().getName());
    public ClsECatParametros( String acc) {
       this.mstrAccion=acc;
    }
    @Override
    public String Procesa(HttpServletRequest lobjRequest, HttpServletResponse lobjResponse, PageContext lobjContext) {
            HttpSession lsesSesion = lobjRequest.getSession(true);        
            log.info("Procesa( " + mstrAccion + " )");
            log.info("Inicia accion = " + mstrAccion); 
        /* String con la direccion del JSP de resultado */
        String lstrJSP = "../Common/clsJAcercaDe.jsp";  
        
        /* Se crea el objeto de negocio del proceso */
         ClsNCatParametros mobjBase=null;
         
        if (mstrAccion.compareTo("Inicio") == 0) {
            /* Carga inicial de la pantalla de la opción: Parámetros */
                    
             lsesSesion.removeAttribute("mobjBase");
             mobjBase=   new ClsNCatParametros();
             mobjBase.Inicializa(mobjSesion);
             mobjBase.setMensaje("");  
           
             mobjBase.parDisponibles();
            lstrJSP =  "./clsJCatParametros.jsp"; 
            lsesSesion.setAttribute("mobjBase",mobjBase);
        } else if(mstrAccion.equals("actualiza")){
                    mobjBase = (ClsNCatParametros)lsesSesion.getAttribute("mobjBase");
                    lsesSesion.removeAttribute("mobjBase");
            ClsDParametrosVo vo= new ClsDParametrosVo();
            vo.setId(Integer.parseInt(lobjRequest.getParameter("txtId")));
            vo.setCampo(lobjRequest.getParameter("textC"+vo.getId()));
            vo.setDescripcion(lobjRequest.getParameter("textD"+vo.getId()));
           
         
             
            mobjBase.actualizaPara(vo);
            mobjBase.parDisponibles();
             lstrJSP =  "./clsJCatParametros.jsp";  
             lsesSesion.setAttribute("mobjBase",mobjBase); 
         
        } 
            log.info("Acción = " + mstrAccion + " finalizada.");
            return lstrJSP;
        }

  
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
            if (mobjSesion.TieneAcceso(5312)) {
                lbNoAcceso = false;
            }
        }
        return lbNoAcceso;
        }
        }
