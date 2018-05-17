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

public class ClsECatDisFaudi extends clsIEjecutable{
    public ClsECatDisFaudi() {
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
    public ClsECatDisFaudi(String acc) {
       this.mstrAccion=acc;
    }
    
    @Override
        public String Procesa(HttpServletRequest lobjRequest, HttpServletResponse lobjResponse, PageContext lobjContext) {
        HttpSession lsesSesion = lobjRequest.getSession(true);        
        /* String con la direccion del JSP de resultado */
        String lstrJSP = "../Common/clsJAcercaDe.jsp";  
        log.info("Procesa( " + mstrAccion + " )");
        log.info("Inicia accion = " + mstrAccion);
        /* Se crea el objeto de negocio del proceso */
         ClsNCatDistribuidor mobjBase=null;
         
        if (mstrAccion.compareTo("Inicio") == 0) {
            /* Carga inicial de la pantalla de la opción: Parámetros */
            
             lsesSesion.removeAttribute("mobjBase");
             mobjBase=   new ClsNCatDistribuidor();
             mobjBase.Inicializa(mobjSesion);
             mobjBase.setMensaje("");  
           
             mobjBase.distDisponibles();
            lstrJSP =  "./clsJCatDistribuidores.jsp"; 
            lsesSesion.setAttribute("mobjBase",mobjBase);
        } else if(mstrAccion.equals("alta")){
            mobjBase = (ClsNCatDistribuidor)lsesSesion.getAttribute("mobjBase");
            lsesSesion.removeAttribute("mobjBase");
            mobjBase.setMensaje("");  
           mobjBase.setEdos(mobjBase.estadosdisponibles());
            lstrJSP =  "./clsJCatAltaDistribuidor.jsp";  
            lsesSesion.setAttribute("mobjBase",mobjBase);            
        }else if(mstrAccion.equals("editar")){
            mobjBase = (ClsNCatDistribuidor)lsesSesion.getAttribute("mobjBase");
            lsesSesion.removeAttribute("mobjBase");
            mobjBase.setMensaje("");  
            String dis= lobjRequest.getParameter("txtClave");
            mobjBase.setClaveDis(dis);
            mobjBase.setEdos(mobjBase.estadosdisponibles());
            mobjBase.setVoSelec(mobjBase.getMapDistribuidoresDis().get(dis.trim()));
            lstrJSP =  "./clsCatEditaDistribuidor.jsp";  
            lsesSesion.setAttribute("mobjBase",mobjBase);            
        }
        else if(mstrAccion.equals("guardar")){
                    mobjBase = (ClsNCatDistribuidor)lsesSesion.getAttribute("mobjBase");
                    lsesSesion.removeAttribute("mobjBase");
            mobjBase.setMensaje("");  
            ClsDDistribuidorVo vo= new ClsDDistribuidorVo();
            vo.setDist_vid_pk(lobjRequest.getParameter("txtClave"));
            vo.setDist_vrazon_social(lobjRequest.getParameter("txtRazonSocial"));
            vo.setDist_vabreviacion(lobjRequest.getParameter("txtAbreviacion"));
            vo.setDist_vrfc(lobjRequest.getParameter("txtRfc"));
            vo.setDist_vdireccion(lobjRequest.getParameter("txtDireccion"));
         //   vo.setDist_vcolonia(lobjRequest.getParameter("txtColonia"));
            vo.setDist_vpoblacion(lobjRequest.getParameter("txtPobloacion"));
            vo.setDist_ncodigo_postal(lobjRequest.getParameter("txtCodio"));
            vo.setDist_edo(Integer.parseInt(lobjRequest.getParameter("cbxEstado")));
           
                    vo.setDist_zona(lobjRequest.getParameter("txtZona"));
            
            vo.setDist_financiera(lobjRequest.getParameter("cbxFin"));
            vo.setDist_mat_pago(lobjRequest.getParameter("txtMpago"));
            //String  no=(lobjRequest.getParameter("txtNcuenta").equals(""))?"0":lobjRequest.getParameter("txtNcuenta");
           
                vo.setDist_no_cuenta(lobjRequest.getParameter("txtNcuenta"));
           
            vo.setDist_traslado(lobjRequest.getParameter("txtTraslado"));
            vo.setDist_descuento(lobjRequest.getParameter("txtDescuento"));
            vo.setDist_autorizacion(lobjRequest.getParameter("txtAutorizacion"));
            vo.setDist_surtir(lobjRequest.getParameter("txtSurtir"));
            if( "".equals(lobjRequest.getParameter("txtDiast"))){
                vo.setDist_dias_tras(0);
            } else{
                vo.setDist_dias_tras(Integer.parseInt(lobjRequest.getParameter("txtDiast")));
            }
            
                   mobjBase.insertaDistribuidor(vo);
             lstrJSP =  "./clsJCatAltaDistribuidor.jsp";  
             lsesSesion.setAttribute("mobjBase",mobjBase); 
         
        }        else if(mstrAccion.equals("actualiza")){
                    mobjBase = (ClsNCatDistribuidor)lsesSesion.getAttribute("mobjBase");
                    lsesSesion.removeAttribute("mobjBase");
            ClsDDistribuidorVo vo= new ClsDDistribuidorVo();
            mobjBase.setMensaje("");  
            vo.setDist_vid_pk(mobjBase.getClaveDis());
            vo.setDist_vrazon_social(lobjRequest.getParameter("txtRazonSocial"));
            vo.setDist_vabreviacion(lobjRequest.getParameter("txtAbreviacion"));
            vo.setDist_vrfc(lobjRequest.getParameter("txtRfc"));
            vo.setDist_vdireccion(lobjRequest.getParameter("txtDireccion"));
            //vo.setDist_vcolonia(lobjRequest.getParameter("txtColonia"));
            vo.setDist_vpoblacion(lobjRequest.getParameter("txtPobloacion"));
            vo.setDist_ncodigo_postal(lobjRequest.getParameter("txtCodio"));
            vo.setDist_edo(Integer.parseInt(lobjRequest.getParameter("cbxEstado")));
            
                    vo.setDist_zona(lobjRequest.getParameter("txtZona"));
            
            vo.setDist_financiera(lobjRequest.getParameter("cbxFin"));
            vo.setDist_mat_pago(lobjRequest.getParameter("txtMpago"));
             vo.setDist_no_cuenta(lobjRequest.getParameter("txtNcuenta"));
            vo.setDist_traslado(lobjRequest.getParameter("txtTraslado"));
            vo.setDist_descuento(lobjRequest.getParameter("txtDescuento"));
            vo.setDist_autorizacion(lobjRequest.getParameter("txtAutorizacion"));
            vo.setDist_surtir(lobjRequest.getParameter("txtSurtir"));
            if( "".equals(lobjRequest.getParameter("txtDiast"))){
                vo.setDist_dias_tras(0);
            } else{
                vo.setDist_dias_tras(new Integer(lobjRequest.getParameter("txtDiast")));
            }
         
            vo.setEstatus(Integer.parseInt(lobjRequest.getParameter("cbxEstatus")));  
                   mobjBase.actualizaDistribuidor(vo);
            mobjBase.distDisponibles();
             lstrJSP =  "./clsJCatDistribuidores.jsp";  
             lsesSesion.setAttribute("mobjBase",mobjBase); 
         
        } else if(mstrAccion.equals("baja")){
                    mobjBase = (ClsNCatDistribuidor)lsesSesion.getAttribute("mobjBase");
                    lsesSesion.removeAttribute("mobjBase");
          
            String dis= lobjRequest.getParameter("txtClave");
            mobjBase.setClaveDis(dis);
            mobjBase.setMensaje("");  
              mobjBase.eliminaDistribuidor(dis);
            mobjBase.distDisponibles();
             lstrJSP =  "./clsJCatDistribuidores.jsp";  
             lsesSesion.setAttribute("mobjBase",mobjBase); 
         
        }else if(mstrAccion.equals("ReporteDistribuidores")){
            mobjBase = (ClsNCatDistribuidor)lsesSesion.getAttribute("mobjBase");
            lsesSesion.removeAttribute("mobjBase");
            
            
                if(mobjBase.generaReporte()){
                    ServletOutputStream op;
                    ServletContext context;
                    String mimetype;
                    
                    try {
                        op = lobjResponse.getOutputStream();
                        context = lsesSesion.getServletContext();
                       
                        mimetype = context.getMimeType(mobjBase.getNombreReporte());
                        lobjResponse.reset();
                        lobjResponse.setContentType((mimetype != null) ? mimetype: "application/csv");
                        lobjResponse.setHeader("Content-Disposition","attachment; filename="+ mobjBase.getNombreReporte());

                        InputStream in = new FileInputStream(mobjBase.getRutaFisica()+mobjBase.getNombreReporte());
                        int length = 0;
                        while ((in != null) && ((length = in.read()) != -1)) {
                                op.write(length);
                        }

                        in.close();
                        op.flush();
                        op.close();
                        
                        //mobjBase.eraseFile(mobjBase.getRutaFisica()+mobjBase.getNombreReporte());
                    } catch(Exception e){
                       log.error (e.getMessage(),e);
                    }
                }
            
            lstrJSP =  "./clsJGeneraAllgoritmo.jsp";  
            lsesSesion.setAttribute("mobjBase",mobjBase);            
        }
        log.info("Acción = " + mstrAccion + " finalizada.");
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
