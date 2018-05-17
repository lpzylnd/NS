package SS.ICO.V10.Fau;

import SS.ICO.V10.Common.clsIEjecutable;
import SS.ICO.V10.Common.clsISesion;

import SS.ICO.V20.general.model.Combo;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionActivationListener;
import javax.servlet.jsp.PageContext;

import net.sf.json.JSONArray;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

public class ClsECatPoliticaVenta extends clsIEjecutable{
    
    Logger log = Logger.getLogger(this.getClass().getName());
    /**
     * Almacena la acci&oacuten que realizar&aacute el jsp.
     */
    protected String mstrAccion;
    
    /**
     * Almacena la sesi&oacuten de la clase.
     */
    private clsISesion mobjSesion;
    
    public ClsECatPoliticaVenta(String lstrAction) {
        mstrAccion = lstrAction;
    }


    @Override
    public String Procesa(HttpServletRequest request, HttpServletResponse lobjResponse, PageContext lobjContext) {
        ClsNCatPoliticaVenta negocio = new ClsNCatPoliticaVenta();
        HttpSession lsesSesion = request.getSession(true);   
        ClsNCatPoliticaVenta mobjBase = new ClsNCatPoliticaVenta();
        String jsp = "./clsJCatPoliticaVenta.jsp";
        List<ClsDCatPoliticaVentaVo[]> listaPoliticasVenta = new ArrayList<ClsDCatPoliticaVentaVo[]>();
        List<Combo> opcionesPolVMC = new ArrayList<Combo>();
        ClsDCatPoliticaVentaVo datos = null;
        String msgError = "";
        String msg = "";
        boolean bandera=false;
        int numReg = 0;
        try{
            lsesSesion.removeAttribute("mobjBase");
            mobjBase = (ClsNCatPoliticaVenta)lsesSesion.getAttribute("mobjBase");
            datos =(ClsDCatPoliticaVentaVo)request.getAttribute("politicaVentaVO");
            opcionesPolVMC = negocio.obtieneOpcionesPolVMC();
            if (mstrAccion.equals("Consultar")){
                try{
                    if(datos!=null && !datos.isEmpty()){
                        listaPoliticasVenta = negocio.filtraPoliticaVenta(datos);
                    }else{
                        listaPoliticasVenta = negocio.filtraPoliticaVenta(null);
                    }
                    msg="Busqueda Completada";
                    bandera=true;
                }catch(Exception e){
                    log.error(e.getMessage(),e);
                    msgError="Error al consultar con los siguientes parametros : " + 
                             datos.toString() + "<br/>" + e.getMessage();
                }
            }else{
                if(mstrAccion.equals("Actualizar")){
                    try{
                        numReg =negocio.actualizaPoliticaVenta(datos);
                            if(numReg>0){
                                datos=null;
                                msg="Actualizacion terminada. " + numReg + " registro(s) actualizado(s)";

                            }else{
                                msg="<label class='DetalleRojo'>Actualizacion terminada. " + numReg + " registro(s) actualizado(s)</label>";
                            }
                    }catch(Exception e){
                        log.error(e.getMessage(), e);
                        msgError="Error al Actualizar con los siguientes parametros : " + 
                                 datos.toString() + "<br/>" + e.getMessage();
                    }
                }else if(mstrAccion.equals("Insertar")){
                    try{
                        numReg =negocio.insertaPoliticaVenta(datos);   
                            if(numReg>0){
                                datos=null;
                                msg="Insercion terminada. "+ numReg + " registro insertado";
                            }else{
                                msg="<label class='DetalleRojo'>Insercion terminada. "+ numReg + " registro insertado</label>";
                            }
                    }catch(Exception e){
                        log.error(e.getMessage(), e);
                        msgError="Error al insertar los siguientes parametros : " + 
                                 datos.toString() + "<br/>" + e.getMessage();
                    }
                }else if(mstrAccion.equals("Borrar")){
                    try{
                        numReg =negocio.borraPoliticaVenta(datos.getPolVidPk());
                            if(numReg>0){
                                datos=null;
                                msg="Borrado terminado. "+ numReg + " registro(s) borrado(s)";
                            }else{
                                msg="<label class='DetalleRojo'>Borrado terminado. "+ numReg + " registro(s) borrado(s)</label>";
                            }
                    }catch(Exception e){
                        log.error(e.getMessage(), e);
                        msgError="Error al borrar con los siguientes parametros : " + 
                                 datos.toString() + "<br/>" + e.getMessage();
                    }
                }
                listaPoliticasVenta = negocio.filtraPoliticaVenta(null);
            }
        }catch(Exception e){
            msgError="Error en la clase ejecutora : " + e.getCause();
            log.error("Error en la clase ejecutora : " + e.getStackTrace(), e);
             
        }
        if(!msgError.isEmpty()){
            request.setAttribute("Error", msgError);
        }else{
            request.setAttribute("Mensaje", msg);    
        }
        request.setAttribute("politicaVentaVO", datos);
        Long page = 0L;
        if(listaPoliticasVenta.size()>0 || bandera){
            request.getSession().setAttribute("listaCompletaPoliticasVenta", listaPoliticasVenta);    
        }
        request.setAttribute("opcionesPolVMC", opcionesPolVMC);
        
        return jsp;
    }

    

    @Override
    public boolean NoLogin (clsISesion lobjSesion) {
        boolean lbNoLogin = true;
        mobjSesion = lobjSesion;
        if (mobjSesion != null) {
            if (mobjSesion.Menu() != null) {
                if (mobjSesion.Menu().size()>0) {
                    lbNoLogin=false;
                }
            }
        }
        return lbNoLogin;
    }

    @Override
    public boolean NoAcceso (clsISesion lobjSesion) {
        boolean lbNoAcceso = true;
        mobjSesion = lobjSesion;
        if (mobjSesion!=null) {
            if (mobjSesion.TieneAcceso(5309)) {
               lbNoAcceso = false;
            }
        }
        return lbNoAcceso;
    }
}
