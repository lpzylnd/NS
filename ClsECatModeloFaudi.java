package SS.ICO.V10.Fau;

import SS.ICO.V10.Common.clsIEjecutable;
import SS.ICO.V10.Common.clsISesion;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.PageContext;

public class ClsECatModeloFaudi extends clsIEjecutable{
    public ClsECatModeloFaudi() {
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
        
        
        public ClsECatModeloFaudi(String acc) {
           this.mstrAccion=acc;
        }

    public String Procesa(HttpServletRequest lobjRequest, HttpServletResponse lobjResponse, PageContext lobjContext) {
            HttpSession lsesSesion = lobjRequest.getSession(true);        
            /* String con la direccion del JSP de resultado */
            String lstrJSP = "../Common/clsJAcercaDe.jsp";  
            
            /* Se crea el objeto de negocio del proceso */
             ClsNCatModelosFaudi mobjBase=null;
             
            if (mstrAccion.compareTo("Inicio") == 0) {
                /* Carga inicial de la pantalla de la opción: Parámetros */
                
                 lsesSesion.removeAttribute("mobjBase");
                 mobjBase=   new ClsNCatModelosFaudi();
                 mobjBase.Inicializa(mobjSesion);
                 mobjBase.setMensaje("");  
               
               
                lstrJSP =  "./clsCatModelosFaudi.jsp"; 
                lsesSesion.setAttribute("mobjBase",mobjBase);
            } else if(mstrAccion.equals("consultar")){
                mobjBase = (ClsNCatModelosFaudi)lsesSesion.getAttribute("mobjBase");
                lsesSesion.removeAttribute("mobjBase");
                ClsDModeloVo vo= new ClsDModeloVo();
                vo.setEim(lobjRequest.getParameter("claveEim"));
                vo.setCuota(0);
                vo.setCabecera(lobjRequest.getParameter("txtCabecera"));
               
              if(lobjRequest.getParameter("cbxsec")!=null)  
                vo.setSector(Integer.parseInt(lobjRequest.getParameter("cbxsec")));
               
             if(lobjRequest.getParameter("txtCuota")!=null)  
                  vo.setCuota(Integer.parseInt(lobjRequest.getParameter("txtCuota")));
                  
                vo.setOrigen(lobjRequest.getParameter("txtOrigen"));
                vo.setGrupoMaterial(lobjRequest.getParameter("txtGrupo"));
                
                
                mobjBase.modelosDisponibles(vo);
                lstrJSP =  "./clsCatModelosFaudi.jsp";  
                lsesSesion.setAttribute("mobjBase",mobjBase);            
            }else if(mstrAccion.equals("alta")){
                mobjBase = (ClsNCatModelosFaudi)lsesSesion.getAttribute("mobjBase");
                lsesSesion.removeAttribute("mobjBase");
                ClsDModeloVo vo= new ClsDModeloVo();
                vo.setEim("");
                vo.setCuota(0);
                vo.setCabecera("");
                vo.setDescripcion("");
                vo.setSector(0);
                vo.setOrigen("");
                vo.setGrupoMaterial("");
                mobjBase.setVoSelec(vo);
                lstrJSP =  "./clsJAltaModelos.jsp";  
                lsesSesion.setAttribute("mobjBase",mobjBase);            
            }else if(mstrAccion.equals("editar")){
                mobjBase = (ClsNCatModelosFaudi)lsesSesion.getAttribute("mobjBase");
                lsesSesion.removeAttribute("mobjBase");
                String eim= lobjRequest.getParameter("claveEim");
                mobjBase.setVoSelec(mobjBase.getMapModelosDis().get(eim));
                lstrJSP =  "./clsJAltaModelo.jsp";  
                lsesSesion.setAttribute("mobjBase",mobjBase);            
            }
             
             
             
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

