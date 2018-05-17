package SS.ICO.V10.Fau;

import SS.ICO.V10.Common.clsIEjecutable;
import SS.ICO.V10.Common.clsISesion;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.PageContext;

import org.apache.log4j.Logger;

public class ClsECatColorExtFaudi extends clsIEjecutable {
    
    /**
    * Almacena la acci&oacuten que realizar&aacute el ejecutable.
    */
    private String mstrAccion;
    /**
    * Almacena la sesi&oacuten de la clase.
    */
    private clsISesion mobjSesion;
    
    Logger logger = Logger.getLogger(ClsECatColorExtFaudi.class);
    
    public ClsECatColorExtFaudi() {
        super();
    }
    
    public ClsECatColorExtFaudi(String accion) {
        this.mstrAccion = accion;
    }

    @Override
    public String Procesa(HttpServletRequest lobjRequest, HttpServletResponse lobjResponse, PageContext lobjContext) {
        
        HttpSession lsesSesion      = lobjRequest.getSession(true);        
        /* String con la direccion del JSP de resultado */
        String lstrJSP              = "../Common/clsJAcercaDe.jsp";  
        /* Se crea el objeto de negocio del proceso */
        ClsNCatColorExtFaudi mobjBase = null;
        
        logger.info("Procesa( " + mstrAccion + " )");
        logger.info("Inicia accion = " + mstrAccion);
        
        if(mstrAccion.compareTo("Inicio") == 0) {
            lsesSesion.removeAttribute("mobjBase");
            mobjBase = new ClsNCatColorExtFaudi();
            mobjBase.Inicializa(mobjSesion);
            mobjBase.setMensaje("");  
            
            lstrJSP =  "./clsJCatColorExtFaudi.jsp"; 
            lsesSesion.setAttribute("mobjBase",mobjBase);
        }else if(mstrAccion.equals("Buscar")){
            mobjBase = (ClsNCatColorExtFaudi) lsesSesion.getAttribute("mobjBase");
            lsesSesion.removeAttribute("mobjBase");
            mobjBase.setMensaje("");  
            
            int anio            = Integer.parseInt(lobjRequest.getParameter("txtAnio"));
            String origen       = lobjRequest.getParameter("txtOrigen");
            String codigo       = lobjRequest.getParameter("txtCodigo");
            int sector          = Integer.parseInt(lobjRequest.getParameter("txtSector"));
            String descripcion  = lobjRequest.getParameter("txtDescripcion");
            
            ClsDColoresExtVo vo = new ClsDColoresExtVo();
            vo.setAnio(anio);
            vo.setIdOrigen(origen);
            vo.setCodigo(codigo);
            vo.setIdSector(sector);
            vo.setDescripcion(descripcion);
            
            mobjBase.obtenerListaColoresExt(vo);
            
            lstrJSP =  "./clsJCatColorExtFaudi.jsp"; 
            lsesSesion.setAttribute("mobjBase",mobjBase);
        }else if(mstrAccion.equals("Agregar")){
            mobjBase = (ClsNCatColorExtFaudi) lsesSesion.getAttribute("mobjBase");
            lsesSesion.removeAttribute("mobjBase");
            mobjBase.setMensaje("");  
            
            ClsDColoresExtVo vo = new ClsDColoresExtVo();
            vo.setAnio(0);
            vo.setIdOrigen("");
            vo.setCodigo("");
            vo.setIdSector(0);
            vo.setDescripcion("");
            vo.setConvColor("");
            
            mobjBase.setColoresExtVo(vo);
            mobjBase.setCveTmp("");
            
            lstrJSP =  "./clsJAltaColorExt.jsp"; 
            lsesSesion.setAttribute("mobjBase",mobjBase);
        }else if(mstrAccion.equals("Editar")){
            mobjBase = (ClsNCatColorExtFaudi) lsesSesion.getAttribute("mobjBase");
            lsesSesion.removeAttribute("mobjBase");
            mobjBase.setMensaje("");  
            
            mobjBase.setCveTmp(lobjRequest.getParameter("txtClave").trim());
            mobjBase.setColoresExtVo(mobjBase.getMapColoresExt().get(mobjBase.getCveTmp()));
            
            lstrJSP =  "./clsJAltaColorExt.jsp"; 
            lsesSesion.setAttribute("mobjBase",mobjBase);
        }else if(mstrAccion.equals("Eliminar")){
            mobjBase = (ClsNCatColorExtFaudi) lsesSesion.getAttribute("mobjBase");
            lsesSesion.removeAttribute("mobjBase");
            mobjBase.setMensaje("");  
            
            String clave = lobjRequest.getParameter("txtClave").trim();
            ClsDColoresExtVo vo = mobjBase.getMapColoresExt().get(clave);
            
            mobjBase.eliminarColorExt(vo);
            mobjBase.Inicializa(mobjSesion);
            
            lstrJSP =  "./clsJCatColorExtFaudi.jsp"; 
            lsesSesion.setAttribute("mobjBase",mobjBase);
        }else if(mstrAccion.equals("Guardar")){
            mobjBase = (ClsNCatColorExtFaudi) lsesSesion.getAttribute("mobjBase");
            lsesSesion.removeAttribute("mobjBase");
            mobjBase.setMensaje("");  
            
            String descripcion = lobjRequest.getParameter("descripcion");
            String conversion = lobjRequest.getParameter("conversion");
            
            if(mobjBase.getCveTmp() != ""){
                mobjBase.setColoresExtVo(mobjBase.getMapColoresExt().get(mobjBase.getCveTmp()));
                mobjBase.getColoresExtVo().setDescripcion(descripcion.toUpperCase());
                mobjBase.getColoresExtVo().setConvColor(conversion.toUpperCase());
                
                mobjBase.actualizarColor(mobjBase.getColoresExtVo());
            }else{
                ClsDColoresExtVo vo = new ClsDColoresExtVo();
                vo.setAnio(Integer.parseInt(lobjRequest.getParameter("anio")));
                vo.setIdOrigen(lobjRequest.getParameter("origen"));
                vo.setCodigo(lobjRequest.getParameter("codigo").toUpperCase());
                vo.setIdSector(Integer.parseInt(lobjRequest.getParameter("sector")));
                vo.setDescripcion(descripcion.toUpperCase());
                vo.setConvColor(conversion.toUpperCase());
                mobjBase.setColoresExtVo(vo);
                
                mobjBase.insertarColorExt(mobjBase.getColoresExtVo());
                   
            }
            mobjBase.Inicializa(mobjSesion);
            lstrJSP =  "./clsJCatColorExtFaudi.jsp";
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
