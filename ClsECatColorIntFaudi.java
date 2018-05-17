package SS.ICO.V10.Fau;

import SS.ICO.V10.Common.clsIEjecutable;
import SS.ICO.V10.Common.clsISesion;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.PageContext;

import org.apache.log4j.Logger;

public class ClsECatColorIntFaudi extends clsIEjecutable {
    
    /**
    * Almacena la acci&oacuten que realizar&aacute el ejecutable.
    */
    private String mstrAccion;
    /**
    * Almacena la sesi&oacuten de la clase.
    */
    private clsISesion mobjSesion;
    
    Logger logger = Logger.getLogger(ClsECatColorIntFaudi.class);
    
    public ClsECatColorIntFaudi() {
        super();
    }
    
    public ClsECatColorIntFaudi(String accion) {
        this.mstrAccion = accion;
    }

    @Override
    public String Procesa(HttpServletRequest lobjRequest, HttpServletResponse lobjResponse, PageContext lobjContext) {
        
        HttpSession lsesSesion      = lobjRequest.getSession(true);        
        /* String con la direccion del JSP de resultado */
        String lstrJSP              = "../Common/clsJAcercaDe.jsp";  
        /* Se crea el objeto de negocio del proceso */
        ClsNCatColorIntFaudi mobjBase = null;
        
        logger.info("Procesa( " + mstrAccion + " )");
        logger.info("Inicia accion = " + mstrAccion);
        
        if(mstrAccion.compareTo("Inicio") == 0) {
            lsesSesion.removeAttribute("mobjBase");
            mobjBase = new ClsNCatColorIntFaudi();
            mobjBase.Inicializa(mobjSesion);
            mobjBase.setMensaje("");  
           
            lstrJSP =  "./clsJCatColorIntFaudi.jsp"; 
            lsesSesion.setAttribute("mobjBase",mobjBase);
        }else if(mstrAccion.equals("Buscar")){
            mobjBase = (ClsNCatColorIntFaudi) lsesSesion.getAttribute("mobjBase");
            lsesSesion.removeAttribute("mobjBase");
            mobjBase.setMensaje("");  
            
            int anio            = Integer.parseInt(lobjRequest.getParameter("txtAnio"));
            String origen       = lobjRequest.getParameter("txtOrigen");
            String codigo       = lobjRequest.getParameter("txtCodigo");
            String pais         = lobjRequest.getParameter("txtPais");
            String descripcion  = lobjRequest.getParameter("txtDescripcion");
            
            ClsDColoresIntVo vo = new ClsDColoresIntVo();
            vo.setAnio(anio);
            vo.setIdOrigen(origen);
            vo.setCodigo(codigo);
            vo.setIdPais(pais);
            vo.setDescripcion(descripcion);
            
            mobjBase.obtenerListaColoresInt(vo);
            
            lstrJSP =  "./clsJCatColorIntFaudi.jsp"; 
            lsesSesion.setAttribute("mobjBase",mobjBase);
        }else if(mstrAccion.equals("Agregar")){
            mobjBase = (ClsNCatColorIntFaudi) lsesSesion.getAttribute("mobjBase");
            lsesSesion.removeAttribute("mobjBase");
            mobjBase.setMensaje("");  
            
            ClsDColoresIntVo vo = new ClsDColoresIntVo();
            vo.setAnio(0);
            vo.setIdOrigen("");
            vo.setCodigo("");
            vo.setIdPais("");
            vo.setDescripcion("");
            vo.setConvColor("");
            
            mobjBase.setColoresIntVo(vo);
            mobjBase.setCveTmp("");
            
            lstrJSP =  "./clsJAltaColorInt.jsp"; 
            lsesSesion.setAttribute("mobjBase",mobjBase);
        }else if(mstrAccion.equals("Editar")){
            mobjBase = (ClsNCatColorIntFaudi) lsesSesion.getAttribute("mobjBase");
            lsesSesion.removeAttribute("mobjBase");
            mobjBase.setMensaje("");  
            
            mobjBase.setCveTmp(lobjRequest.getParameter("txtClave").trim());
            mobjBase.setColoresIntVo(mobjBase.getMapColoresInt().get(mobjBase.getCveTmp()));
            
            lstrJSP =  "./clsJAltaColorInt.jsp"; 
            lsesSesion.setAttribute("mobjBase",mobjBase);
        }else if(mstrAccion.equals("Eliminar")){
            mobjBase = (ClsNCatColorIntFaudi) lsesSesion.getAttribute("mobjBase");
            lsesSesion.removeAttribute("mobjBase");
            mobjBase.setMensaje("");  
            
            String clave = lobjRequest.getParameter("txtClave").trim();
            ClsDColoresIntVo vo = mobjBase.getMapColoresInt().get(clave);
            
            mobjBase.eliminarColorInt(vo);
            mobjBase.Inicializa(mobjSesion);
            
            lstrJSP =  "./clsJCatColorIntFaudi.jsp"; 
            lsesSesion.setAttribute("mobjBase",mobjBase);
        }else if(mstrAccion.equals("Guardar")){
            mobjBase = (ClsNCatColorIntFaudi) lsesSesion.getAttribute("mobjBase");
            lsesSesion.removeAttribute("mobjBase");
            mobjBase.setMensaje("");  
            lstrJSP =  "./clsJCatColorIntFaudi.jsp";
            
            String descripcion = lobjRequest.getParameter("descripcion");
            String conversion = lobjRequest.getParameter("conversion");
            
            if(mobjBase.getCveTmp() != ""){
                mobjBase.setColoresIntVo(mobjBase.getMapColoresInt().get(mobjBase.getCveTmp()));
                mobjBase.getColoresIntVo().setDescripcion(descripcion.toUpperCase());
                mobjBase.getColoresIntVo().setConvColor(conversion.toUpperCase());
                
                mobjBase.actualizarColor(mobjBase.getColoresIntVo());
            }else{
                ClsDColoresIntVo vo = new ClsDColoresIntVo();
                vo.setAnio(Integer.parseInt(lobjRequest.getParameter("anio")));
                vo.setIdOrigen(lobjRequest.getParameter("origen"));
                vo.setCodigo(lobjRequest.getParameter("codigo").toUpperCase());
                vo.setIdPais(lobjRequest.getParameter("pais"));
                vo.setDescripcion(descripcion.toUpperCase());
                vo.setConvColor(conversion.toUpperCase());
                mobjBase.setColoresIntVo(vo);
                
                boolean resp = mobjBase.existeRegistro(mobjBase.getColoresIntVo());
                if(resp){
                    mobjBase.setMensaje("El color ya existe en la Base de Datos.");
                    lstrJSP =  "./clsJAltaColorInt.jsp"; 
                }else{
                    mobjBase.insertarColorInt(mobjBase.getColoresIntVo());
                }   
            }
            mobjBase.Inicializa(mobjSesion);
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
