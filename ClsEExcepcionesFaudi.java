package SS.ICO.V10.Fau;

import SS.ICO.V10.Common.clsIEjecutable;
import SS.ICO.V10.Common.clsISesion;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.PageContext;

import org.apache.log4j.Logger;

public class ClsEExcepcionesFaudi extends clsIEjecutable {
    
    /**
    * Almacena la acci&oacuten que realizar&aacute el ejecutable.
    */
    private String mstrAccion;
    /**
    * Almacena la sesi&oacuten de la clase.
    */
    private clsISesion mobjSesion;
    
    Logger logger = Logger.getLogger(ClsEExcepcionesFaudi.class);
    
    public ClsEExcepcionesFaudi() {
        super();
    }
    
    public ClsEExcepcionesFaudi(String accion) {
        this.mstrAccion = accion;
    }

    @Override
    public String Procesa(HttpServletRequest lobjRequest, HttpServletResponse lobjResponse, PageContext lobjContext) {
        HttpSession lsesSesion      = lobjRequest.getSession(true);        
        /* String con la direccion del JSP de resultado */
        String lstrJSP                  = "../Common/clsJAcercaDe.jsp";  
        /* Se crea el objeto de negocio del proceso */
        ClsNExcepcionesFaudi mobjBase   = null;
        
        logger.info("Procesa( " + mstrAccion + " )");
        logger.info("Inicia accion = " + mstrAccion);
        
        if(mstrAccion.compareTo("Inicio") == 0) {
            lsesSesion.removeAttribute("mobjBase");
            mobjBase                = new ClsNExcepcionesFaudi();
            mobjBase.Inicializa(mobjSesion);
            mobjBase.setMensaje("");  
            
            lstrJSP                 =  "./clsJCatExcepcionesFaudi.jsp"; 
            lsesSesion.setAttribute("mobjBase",mobjBase);
        }else if(mstrAccion.equals("Buscar")){
            mobjBase                = (ClsNExcepcionesFaudi) lsesSesion.getAttribute("mobjBase");
            lsesSesion.removeAttribute("mobjBase");
            mobjBase.setMensaje(""); 
            
            String cuota            = lobjRequest.getParameter("txtCuota");
            String basico           = lobjRequest.getParameter("txtBasico");
            String fecha            = lobjRequest.getParameter("txtFecha");
            
            ClsDExcepcionesVo vo      = new ClsDExcepcionesVo();
            vo.setCuota(cuota.equals("") ? 0 : Integer.parseInt(cuota));
            vo.setBasico(basico);
            vo.setFecha(fecha);
             
            mobjBase.obtenerListExcepciones(vo);
            
            lstrJSP                 =  "./clsJCatExcepcionesFaudi.jsp"; 
            lsesSesion.setAttribute("mobjBase",mobjBase);
        }else if(mstrAccion.equals("Agregar")){
            mobjBase                = (ClsNExcepcionesFaudi) lsesSesion.getAttribute("mobjBase");
            lsesSesion.removeAttribute("mobjBase");
            mobjBase.setMensaje("");  
            
            ClsDExcepcionesVo vo      = new ClsDExcepcionesVo();
            vo.setCuota(0);
            vo.setBasico("");
            vo.setFecha("");
            vo.setCabecera("");
            vo.setDistribuidor(0);
            vo.setTraslado(0);
            
            mobjBase.setExcepcionesVo(vo);
            
            lstrJSP                 =  "./clsJAltaExcepcionesFaudi.jsp"; 
            lsesSesion.setAttribute("mobjBase",mobjBase);
        }else if(mstrAccion.equals("Editar")){
            mobjBase = (ClsNExcepcionesFaudi) lsesSesion.getAttribute("mobjBase");
            lsesSesion.removeAttribute("mobjBase");
            mobjBase.setMensaje("");  
            
            mobjBase.setCveTmp(lobjRequest.getParameter("txtClave").trim());
            mobjBase.setExcepcionesVo(mobjBase.getMapExcepciones().get(mobjBase.getCveTmp()));
            
            lstrJSP =  "./clsJAltaExcepcionesFaudi.jsp"; 
            lsesSesion.setAttribute("mobjBase",mobjBase);
        }else if(mstrAccion.equals("Eliminar")){
            mobjBase                = (ClsNExcepcionesFaudi) lsesSesion.getAttribute("mobjBase");
            lsesSesion.removeAttribute("mobjBase");
            mobjBase.setMensaje("");  
            
            String clave            = lobjRequest.getParameter("txtClave").trim();
            ClsDExcepcionesVo vo      = mobjBase.getMapExcepciones().get(clave);
            
            mobjBase.eliminarExcepcion(vo);
            mobjBase.Inicializa(mobjSesion);
            
            lstrJSP =  "./clsJCatExcepcionesFaudi.jsp"; 
            lsesSesion.setAttribute("mobjBase",mobjBase);
        }else if(mstrAccion.equals("Guardar")){
            mobjBase                    = (ClsNExcepcionesFaudi) lsesSesion.getAttribute("mobjBase");
            lsesSesion.removeAttribute("mobjBase");
            mobjBase.setMensaje("");  
            
            String cabecera             = lobjRequest.getParameter("cabecera");
            String distribuidor         = lobjRequest.getParameter("distribuidor");
            String traslado             = lobjRequest.getParameter("traslado");
            
            if(mobjBase.getCveTmp() != ""){
                mobjBase.setExcepcionesVo(mobjBase.getMapExcepciones().get(mobjBase.getCveTmp()));
                mobjBase.getExcepcionesVo().setCabecera(cabecera);
                mobjBase.getExcepcionesVo().setDistribuidor(Integer.parseInt(distribuidor));
                mobjBase.getExcepcionesVo().setTraslado(Double.parseDouble(traslado));
                
                mobjBase.actualizarExcepcion(mobjBase.getExcepcionesVo());
            }else{
                String cuota            = lobjRequest.getParameter("cuota");
                String basico           = lobjRequest.getParameter("basico");
                String fecha            = lobjRequest.getParameter("fecha");
                
                ClsDExcepcionesVo vo      = new ClsDExcepcionesVo();
                vo.setCuota(Integer.parseInt(cuota));
                vo.setBasico(basico);
                vo.setFecha(fecha);
                vo.setCabecera(cabecera);
                vo.setDistribuidor(Integer.parseInt(distribuidor));
                vo.setTraslado(Double.parseDouble(traslado));
                mobjBase.setExcepcionesVo(vo);
                
                mobjBase.insertarExcepcion(mobjBase.getExcepcionesVo());
                   
            }
            mobjBase.Inicializa(mobjSesion);
            lstrJSP =  "./clsJCatExcepcionesFaudi.jsp";
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