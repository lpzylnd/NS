package SS.ICO.V10.Fau;

import SS.ICO.V10.Common.clsIEjecutable;
import SS.ICO.V10.Common.clsISesion;

import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.PageContext;

import org.apache.log4j.Logger;

public class ClsECatPrecioFaudi  extends clsIEjecutable {
    public ClsECatPrecioFaudi() {
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
    public ClsECatPrecioFaudi(String acc) {
               this.mstrAccion=acc;
            }
            Logger log = Logger.getLogger(this.getClass().getName());
        public String Procesa(HttpServletRequest lobjRequest, HttpServletResponse lobjResponse, PageContext lobjContext) {
                HttpSession lsesSesion = lobjRequest.getSession(true);        
                /* String con la direccion del JSP de resultado */
                String lstrJSP = "../Common/clsJAcercaDe.jsp";  
                log.info("Procesa( " + mstrAccion + " )");
                log.info("Inicia accion = " + mstrAccion);
                /* Se crea el objeto de negocio del proceso */
                 ClsNCatPrecioFaudi mobjBase=null;
                 
                if (mstrAccion.compareTo("Inicio") == 0) {
                    /* Carga inicial de la pantalla de la opción: Parámetros */
                    
                     lsesSesion.removeAttribute("mobjBase");
                     mobjBase=   new ClsNCatPrecioFaudi();
                     mobjBase.Inicializa(mobjSesion);
                     mobjBase.setMensaje("");  
                    mobjBase.preciosDisponibles();  
                 
                    lstrJSP =  "./clsJCatPreciosFaudi.jsp"; 
                    lsesSesion.setAttribute("mobjBase",mobjBase);
                } else if(mstrAccion.equals("consultar")){
                    mobjBase = (ClsNCatPrecioFaudi)lsesSesion.getAttribute("mobjBase");
                    lsesSesion.removeAttribute("mobjBase");
                    mobjBase.setMensaje("");  
                   String ini=lobjRequest.getParameter("txtFechaIni");
                   String fin=lobjRequest.getParameter("txtFechaFin");
                   mobjBase.setFechaIni(ini);
                    mobjBase.setFechaFin(fin);
                    ClsDPreciosVo vo= new ClsDPreciosVo();
                    vo.setFechaInicial(ini);
                    vo.setFechaFinal(fin);
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
                    mobjBase.consultarPrecio(vo);
                    lstrJSP =  "./clsJCatPreciosFaudi.jsp";  
                    lsesSesion.setAttribute("mobjBase",mobjBase);            
                }else if(mstrAccion.equals("alta")){
                    mobjBase = (ClsNCatPrecioFaudi)lsesSesion.getAttribute("mobjBase");
                    lsesSesion.removeAttribute("mobjBase");
                    mobjBase.setMensaje(""); 
                    ClsDPreciosVo vo= new ClsDPreciosVo();
                   // vo.setFvigencia("");
                    vo.setModelo("");
                    vo.setCapacidadMotor( "");
                    vo.setCuota(0);
                    vo.setTipo("");
                    vo.setPrecioBase(new BigDecimal(0));
                    vo.setDescripcionUno("");
                    vo.setDescripcionDos("");
                    vo.setDescripcionTres("");
                    vo.setClaveVehicular("");
                    mobjBase.setVoAlta(vo);
                     lstrJSP =  "./clsJAltaPrecios.jsp";  
                    lsesSesion.setAttribute("mobjBase",mobjBase);            
                }else if(mstrAccion.equals("guardar")){
                    mobjBase = (ClsNCatPrecioFaudi)lsesSesion.getAttribute("mobjBase");
                    lsesSesion.removeAttribute("mobjBase");
                    mobjBase.setMensaje(""); 
                    
                    ClsDPreciosVo vo= new ClsDPreciosVo();
                    vo.setFvigencia(lobjRequest.getParameter("txtFecha"));
                    vo.setModelo(lobjRequest.getParameter("txtEim"));
                    vo.setCapacidadMotor( lobjRequest.getParameter("txtCapacidad"));
                    vo.setCuota(Integer.parseInt(lobjRequest.getParameter("txtCuota")));
                    vo.setTipo(lobjRequest.getParameter("txtTipo"));
                    vo.setPrecioBase(new BigDecimal( lobjRequest.getParameter("txtPrecioBase")));
                    vo.setDescripcionUno(lobjRequest.getParameter("txtDesc1"));
                    vo.setDescripcionDos(lobjRequest.getParameter("txtDesc2"));
                    vo.setDescripcionTres(lobjRequest.getParameter("txtDesc3"));
                    vo.setClaveVehicular(lobjRequest.getParameter("txtClave"));
                    mobjBase.setVoAlta(vo);
                    if(mobjBase.insertaPrecioModelo(vo)<1){
                   
                     lstrJSP =  "./clsJAltaPrecios.jsp";  
                    }    else{                
                     mobjBase.preciosDisponibles();
                    
                        lstrJSP =  "./clsJCatPreciosFaudi.jsp";
                    }
                    lsesSesion.setAttribute("mobjBase",mobjBase);            
                }else if(mstrAccion.equals("editar")){
                    mobjBase = (ClsNCatPrecioFaudi)lsesSesion.getAttribute("mobjBase");
                    lsesSesion.removeAttribute("mobjBase");
                    mobjBase.setMensaje("");
                    String num=lobjRequest.getParameter("txtEditar");
                    ClsDPreciosVo vo= mobjBase.getMapPreciosDis().get(num.trim());
                    mobjBase.setVoSelec( vo);
                  // mobjBase.editarPrecioModelo(mobjBase.getMapPreciosDis().get(lobjRequest.getParameter("txtEditar")));
                    lstrJSP =  "./clsJEditarPrecio.jsp";  
                    lsesSesion.setAttribute("mobjBase",mobjBase);            
                }else if(mstrAccion.equals("actualiza")){
                    mobjBase = (ClsNCatPrecioFaudi)lsesSesion.getAttribute("mobjBase");
                    lsesSesion.removeAttribute("mobjBase");
                    mobjBase.setMensaje("");
                   // mobjBase.setVoSelec( mobjBase.getMapPreciosDis().get(lobjRequest.getParameter("txtEditar")));
                    ClsDPreciosVo vo= new ClsDPreciosVo();
                    vo.setFvigencia(lobjRequest.getParameter("txtFecha"));
                    vo.setModelo(lobjRequest.getParameter("txtEim"));
                    vo.setCapacidadMotor( lobjRequest.getParameter("txtCapacidad"));
                    vo.setCuota(Integer.parseInt(lobjRequest.getParameter("txtCuota")));
                  //  vo.setTipo(lobjRequest.getParameter("txtTipo"));
                    vo.setTipo(mobjBase.getVoSelec().getTipo());
                    vo.setPrecioBase(new BigDecimal( lobjRequest.getParameter("txtPrecioBase")));
                    vo.setDescripcionUno(lobjRequest.getParameter("txtDesc1"));
                    vo.setDescripcionDos(lobjRequest.getParameter("txtDesc2"));
                    vo.setDescripcionTres(lobjRequest.getParameter("txtDesc3"));
                    vo.setClaveVehicular(lobjRequest.getParameter("txtClave"));
                   int res= mobjBase.editarPrecioModelo(mobjBase.getVoSelec(),vo);
                   
                   if(res>0){
                       vo.setFechaInicial(mobjBase.getFechaIni());
                       vo.setFechaFinal(mobjBase.getFechaFin());
                       mobjBase.consultarPrecio(vo);
                        lstrJSP =  "./clsJCatPreciosFaudi.jsp";  
                   }else{
                       //mobjBase.preciosDisponibles(); 
                       lstrJSP =  "./clsJEditarPrecio.jsp";
                   }
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
                    if (mobjSesion.TieneAcceso(4829)) {
                        lbNoAcceso = false;
                    }
                }
                return lbNoAcceso;
            }
        }

