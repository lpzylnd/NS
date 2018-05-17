package SS.ICO.V10.Fau;

import SS.ICO.V10.Common.clsIEjecutable;
import SS.ICO.V10.Common.clsISesion;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.PageContext;

import org.apache.log4j.Logger;

public class ClsECatOpcionalesFaudi extends clsIEjecutable {
    
    /**
    * Almacena la acci&oacuten que realizar&aacute el ejecutable.
    */
    private String mstrAccion;
    /**
    * Almacena la sesi&oacuten de la clase.
    */
    private clsISesion mobjSesion;
    
    Logger logger = Logger.getLogger(ClsECatOpcionalesFaudi.class);
    
    public ClsECatOpcionalesFaudi() {
        super();
    }
    
    public ClsECatOpcionalesFaudi(String accion) {
        this.mstrAccion = accion;
    }

    @Override
    public String Procesa(HttpServletRequest lobjRequest, HttpServletResponse lobjResponse, PageContext lobjContext) {
        HttpSession lsesSesion      = lobjRequest.getSession(true);        
        /* String con la direccion del JSP de resultado */
        String lstrJSP              = "../Common/clsJAcercaDe.jsp";  
        /* Se crea el objeto de negocio del proceso */
        ClsNCatOpcionalesFaudi mobjBase = null;
        
        logger.info("Procesa( " + mstrAccion + " )");
        logger.info("Inicia accion = " + mstrAccion);
        
        if(mstrAccion.compareTo("Inicio") == 0) {
            lsesSesion.removeAttribute("mobjBase");
            mobjBase                = new ClsNCatOpcionalesFaudi();
            mobjBase.Inicializa(mobjSesion);
            mobjBase.setMensaje("");  
            
            lstrJSP                 =  "./clsJCatOpcionalesFaudi.jsp"; 
            lsesSesion.setAttribute("mobjBase",mobjBase);
        }else if(mstrAccion.equals("Buscar")){
            mobjBase                = (ClsNCatOpcionalesFaudi) lsesSesion.getAttribute("mobjBase");
            lsesSesion.removeAttribute("mobjBase");
            mobjBase.setMensaje("");  
            
            String cuota            = lobjRequest.getParameter("txtCuota");
            String basico           = lobjRequest.getParameter("txtBasico");
            String fecha            = lobjRequest.getParameter("txtFecha");
            
            ClsDCOpcionales vo      = new ClsDCOpcionales();
            vo.setCuota(cuota.equals("") ? 0 : Integer.parseInt(cuota));
            vo.setBasico(basico);
            vo.setFecha(fecha);
             
            mobjBase.obtenerListOpcionales(vo);
            
            lstrJSP                 =  "./clsJCatOpcionalesFaudi.jsp"; 
            lsesSesion.setAttribute("mobjBase",mobjBase);
        }else if(mstrAccion.equals("Agregar")){
            mobjBase                = (ClsNCatOpcionalesFaudi) lsesSesion.getAttribute("mobjBase");
            lsesSesion.removeAttribute("mobjBase");
            mobjBase.setMensaje("");  
            
            ClsDCOpcionales vo      = new ClsDCOpcionales();
            vo.setCuota(0);
            vo.setBasico("");
            vo.setFecha("");
            vo.setTraslado(0);
            vo.setSeguroTraslado(0);
            vo.setPromPubl(0);
            vo.setAsistVial(0);
            vo.setIva(0);
            vo.setAndanac(0);
            vo.setAmda(0);
            vo.setCtaPromei(0);
            vo.setSegPlanPiso(0);
            vo.setAyudaSocial(0);
            vo.setCompania("");
            
            mobjBase.setOpcionalesVo(vo);
            
            lstrJSP                 =  "./clsJAltaOpcionalesFaudi.jsp"; 
            lsesSesion.setAttribute("mobjBase",mobjBase);
        }else if(mstrAccion.equals("Editar")){
            mobjBase = (ClsNCatOpcionalesFaudi) lsesSesion.getAttribute("mobjBase");
            lsesSesion.removeAttribute("mobjBase");
            mobjBase.setMensaje("");  
            
            mobjBase.setCveTmp(lobjRequest.getParameter("txtClave").trim());
            mobjBase.setOpcionalesVo(mobjBase.getMapOpcionales().get(mobjBase.getCveTmp()));
            
            lstrJSP =  "./clsJAltaOpcionalesFaudi.jsp"; 
            lsesSesion.setAttribute("mobjBase",mobjBase);
        }else if(mstrAccion.equals("Eliminar")){
            mobjBase                = (ClsNCatOpcionalesFaudi) lsesSesion.getAttribute("mobjBase");
            lsesSesion.removeAttribute("mobjBase");
            mobjBase.setMensaje("");  
            
            String clave            = lobjRequest.getParameter("txtClave").trim();
            ClsDCOpcionales vo      = mobjBase.getMapOpcionales().get(clave);
            
            mobjBase.eliminarOpcional(vo);
            mobjBase.Inicializa(mobjSesion);
            
            lstrJSP =  "./clsJCatOpcionalesFaudi.jsp"; 
            lsesSesion.setAttribute("mobjBase",mobjBase);
        }else if(mstrAccion.equals("Guardar")){
            mobjBase = (ClsNCatOpcionalesFaudi) lsesSesion.getAttribute("mobjBase");
            lsesSesion.removeAttribute("mobjBase");
            mobjBase.setMensaje("");  
            
            String traslado             = lobjRequest.getParameter("traslado");
            String seguroTraslado       = lobjRequest.getParameter("seguroTraslado");
            String promPubl             = lobjRequest.getParameter("promPubl");
            String asistVial            = lobjRequest.getParameter("asistVial");
            String iva                  = lobjRequest.getParameter("iva");
            String andanac              = lobjRequest.getParameter("andanac");
            String amda                 = lobjRequest.getParameter("amda");
            String ctaPromei            = lobjRequest.getParameter("ctaPromei");
            String segPlanPiso          = lobjRequest.getParameter("segPlanPiso");
            String ayudaSocial          = lobjRequest.getParameter("ayudaSocial");
            String compania             = lobjRequest.getParameter("compania");
            
            if(mobjBase.getCveTmp() != ""){
                mobjBase.setOpcionalesVo(mobjBase.getMapOpcionales().get(mobjBase.getCveTmp()));
                mobjBase.getOpcionalesVo().setTraslado(Double.parseDouble(traslado));
                mobjBase.getOpcionalesVo().setSeguroTraslado(Double.parseDouble(seguroTraslado));
                mobjBase.getOpcionalesVo().setPromPubl(Double.parseDouble(promPubl));
                mobjBase.getOpcionalesVo().setAsistVial(Double.parseDouble(asistVial));
                mobjBase.getOpcionalesVo().setIva(Integer.parseInt(iva));
                mobjBase.getOpcionalesVo().setAndanac(Double.parseDouble(andanac));
                mobjBase.getOpcionalesVo().setAmda(Double.parseDouble(amda));
                mobjBase.getOpcionalesVo().setCtaPromei(Double.parseDouble(ctaPromei));
                mobjBase.getOpcionalesVo().setSegPlanPiso(Double.parseDouble(segPlanPiso));
                mobjBase.getOpcionalesVo().setAyudaSocial(Double.parseDouble(ayudaSocial));
                mobjBase.getOpcionalesVo().setCompania(compania.toUpperCase());
                
                mobjBase.actualizarOpcional(mobjBase.getOpcionalesVo());
            }else{
                String cuota            = lobjRequest.getParameter("cuota");
                String basico           = lobjRequest.getParameter("basico");
                String fecha            = lobjRequest.getParameter("fecha");
                
                ClsDCOpcionales vo      = new ClsDCOpcionales();
                vo.setCuota(Integer.parseInt(cuota));
                vo.setBasico(basico);
                vo.setFecha(fecha);
                vo.setTraslado(Double.parseDouble(traslado));
                vo.setSeguroTraslado(Double.parseDouble(seguroTraslado));
                vo.setPromPubl(Double.parseDouble(promPubl));
                vo.setAsistVial(Double.parseDouble(asistVial));
                vo.setIva(Integer.parseInt(iva));
                vo.setAndanac(Double.parseDouble(andanac));
                vo.setAmda(Double.parseDouble(amda));
                vo.setCtaPromei(Double.parseDouble(ctaPromei));
                vo.setSegPlanPiso(Double.parseDouble(segPlanPiso));
                vo.setAyudaSocial(Double.parseDouble(ayudaSocial));
                vo.setCompania(compania.toUpperCase());
                mobjBase.setOpcionalesVo(vo);
                
                mobjBase.insertarOpcional(mobjBase.getOpcionalesVo());
                   
            }
            mobjBase.Inicializa(mobjSesion);
            lstrJSP =  "./clsJCatOpcionalesFaudi.jsp";
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
