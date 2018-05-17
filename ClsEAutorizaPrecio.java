package SS.ICO.V10.Fau;

import SS.ICO.V10.Common.clsIEjecutable;
import SS.ICO.V10.Common.clsISesion;

import java.io.FileInputStream;
import java.io.InputStream;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.PageContext;

import org.apache.log4j.Logger;

public class ClsEAutorizaPrecio  extends clsIEjecutable{
    public ClsEAutorizaPrecio() {
        super();
    }
     public ClsEAutorizaPrecio(String acc) {
           this.mstrAccion=acc;
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

    public String Procesa(HttpServletRequest lobjRequest, HttpServletResponse lobjResponse, PageContext lobjContext) {
        HttpSession lsesSesion = lobjRequest.getSession(true); 
        /* String con la direccion del JSP de resultado */
        String lstrJSP = "../Common/clsJAcercaDe.jsp";  
        log.info("Procesa( " + mstrAccion + " )");
        log.info("Inicia accion = " + mstrAccion);
        /* Se crea el objeto de negocio del proceso */
        
        ClsNAutorizaPrecio mobjBase=null;
        
        if (mstrAccion.compareTo("Inicio") == 0) {
            /* Carga inicial de la pantalla de la opción: Parámetros */
            
             lsesSesion.removeAttribute("mobjBase");
             mobjBase=   new ClsNAutorizaPrecio();
             mobjBase.Inicializa(mobjSesion);
             mobjBase.setMensaje("");  
            mobjBase.preciosDisponibles();  
         
            lstrJSP =  "./clsJAutorizaPrecios.jsp"; 
            lsesSesion.setAttribute("mobjBase",mobjBase);
        } else if(mstrAccion.equals("consultar")){
                    mobjBase = (ClsNAutorizaPrecio)lsesSesion.getAttribute("mobjBase");
                    lsesSesion.removeAttribute("mobjBase");
                    mobjBase.setMensaje("");  
                   
                    ClsDPreciosVo vo= new ClsDPreciosVo();
                    if(lobjRequest.getParameter("txtFecha")!=null && !("").equals(lobjRequest.getParameter("txtFecha") )){
                        vo.setFvigencia(lobjRequest.getParameter("txtFecha"));
                    }else{
                        vo.setFvigencia("N");
                    }
                    if(lobjRequest.getParameter("txtFechaC")!=null && !("").equals(lobjRequest.getParameter("txtFechaC") )){
                        vo.setFechaModificacion(lobjRequest.getParameter("txtFechaC"));
                    }else{
                        vo.setFechaModificacion("N");
                    }
                    if(lobjRequest.getParameter("txtEim")!=null && !("").equals(lobjRequest.getParameter("txtEim") )){
                        vo.setModelo(lobjRequest.getParameter("txtEim"));
                    }else{
                        vo.setModelo("N");
                    }
                    if(lobjRequest.getParameter("txtCuota")!=null && !("").equals(lobjRequest.getParameter("txtCuota") )){
                          vo.setCuota(Integer.parseInt(lobjRequest.getParameter("txtCuota")));
                    }else{
                        vo.setCuota(0);
                    }
                  
                    if(lobjRequest.getParameter("txtTipo")!=null && !("").equals(lobjRequest.getParameter("txtTipo") )){
                          vo.setTipo(lobjRequest.getParameter("txtTipo"));
                    }else{
                        vo.setTipo("N");
                    }
                    mobjBase.consultarPreciosDis(vo);
                    lstrJSP =  "./clsJAutorizaPrecios.jsp";  
                    lsesSesion.setAttribute("mobjBase",mobjBase);            
                }else if(mstrAccion.equals("autoriza")){
                    mobjBase = (ClsNAutorizaPrecio)lsesSesion.getAttribute("mobjBase");
                    lsesSesion.removeAttribute("mobjBase");
                    mobjBase.setMensaje("");
                    String[] elegidos = lobjRequest.getParameterValues("chkAceptar");
                    
                    mobjBase.autorizaPrecioModelo(elegidos);
                    mobjBase.preciosDisponibles(); 
                   lstrJSP =  "./clsJResultadoAutorizar.jsp";  
                    lsesSesion.setAttribute("mobjBase",mobjBase);            
         }else if(mstrAccion.equals("reportePrecios")){
            mobjBase = (ClsNAutorizaPrecio)lsesSesion.getAttribute("mobjBase");
            lsesSesion.removeAttribute("mobjBase");
          String clv=  lobjRequest.getParameter("txtClave");
            
                if(mobjBase.generaReporte(clv)){
                    ServletOutputStream op;
                    ServletContext context;
                    String mimetype;
                    
                    try {
                        op = lobjResponse.getOutputStream();
                        context = lsesSesion.getServletContext();
                       
                        mimetype = context.getMimeType(mobjBase.getNombreArchivo());
                        lobjResponse.reset();
                        lobjResponse.setContentType((mimetype != null) ? mimetype: "application/csv");
                        lobjResponse.setHeader("Content-Disposition","attachment; filename="+ mobjBase.getNombreArchivo());

                        InputStream in = new FileInputStream(mobjBase.getRutaFisica()+mobjBase.getNombreArchivo());
                        int length = 0;
                        while ((in != null) && ((length = in.read()) != -1)) {
                                op.write(length);
                        }

                        in.close();
                        op.flush();
                        op.close();
                        
                        //mobjBase.eraseFile(mobjBase.getRutaFisica()+mobjBase.getNombreReporte());
                    } catch(Exception e){
                     log.error(e.getMessage(),e);  
                    }
                }
            
            lstrJSP =  "./clsJResultadoAutorizar.jsp.jsp";  
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

    
    public boolean NoAcceso(clsISesion lobjSesion) {
        boolean lbNoAcceso = true;
        mobjSesion = lobjSesion;

        if (mobjSesion != null) {
            if (mobjSesion.TieneAcceso(5313)) {
                lbNoAcceso = false;
            }
        }
        return lbNoAcceso;
    }
}
