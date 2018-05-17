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

public class ClsECatClaveFenis extends clsIEjecutable{
    
    Logger log = Logger.getLogger(this.getClass().getName());
    /**
     * Almacena la acci&oacuten que realizar&aacute el jsp.
     */
    protected String mstrAccion;
    
    /**
     * Almacena la sesi&oacuten de la clase.
     */
    private clsISesion mobjSesion;
    
    public ClsECatClaveFenis(String lstrAction) {
        mstrAccion = lstrAction;
    }


    @Override
    public String Procesa(HttpServletRequest request, HttpServletResponse lobjResponse, PageContext lobjContext) {
        ClsNCatClaveFenis negocio = new ClsNCatClaveFenis();
        HttpSession lsesSesion = request.getSession(true);   
        ClsNCatClaveFenis mobjBase;
        String jsp = "./clsJCatClaveFenis.jsp";
        List<ClsDCatClaveFenisVo[]> listaClavesFenis = new ArrayList<ClsDCatClaveFenisVo[]>();
        //List<Combo> opcionesPolVMC = new ArrayList<Combo>();
        ClsDCatClaveFenisVo datos = null;
        String msgError = "";
        String msg = "";
        boolean bandera=false;
        int numReg = 0;
        try{
            mobjBase = new ClsNCatClaveFenis();
            lsesSesion.setAttribute("mobjBase", mobjBase);
            datos =(ClsDCatClaveFenisVo)request.getAttribute("claveFenisVo");
            //opcionesPolVMC = negocio.obtieneOpcionesPolVMC();
            if (mstrAccion.equals("Consultar")){
                try{
                    if(datos!=null && !datos.isEmpty()){
                        listaClavesFenis = negocio.filtraClavesFenis(datos);
                    }else{
                        listaClavesFenis = negocio.filtraClavesFenis(null);
                    }
                    msg="Busqueda Completada";
                    bandera=true;
                }catch(Exception e){
                    log.error(e.getMessage(), e);
                    msgError="Error al consultar con los siguientes parametros : " + 
                             datos.toString() + "<br/>" + e.getMessage();
                }
            }else{
                if(datos!=null){
                    datos.setUsuarioModifica(mobjSesion.UserID());
                }
                if(mstrAccion.equals("Actualizar")){
                    try{
                        numReg =negocio.actualizaClaveFenis(datos);
                        if(numReg>0){
                            datos=null;
                            msg="Actualizacion terminada. " + numReg + " registro(s) actualizado(s)";

                        }else{
                            msg="<label class='DetalleRojo'>Actualizacion terminada. " + numReg + " registro(s) actualizado(s)</label>";
                        }
                    }catch(Exception e){
                        log.error(e.getMessage(),e);
                        msgError="Error al Actualizar con los siguientes parametros : " + 
                                 datos.toString() + "<br/>" + e.getMessage();
                    }
                }else if(mstrAccion.equals("Insertar")){
                    try{
                        numReg =negocio.insertaClaveFenis(datos);   
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
                        numReg =negocio.borraClaveFenis(datos.getPolVidPk());
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
                listaClavesFenis = negocio.filtraClavesFenis(null);
            }
        }catch(Exception e){
            msgError="Error en la clase ejecutora : " + e.getCause();
            log.error("Error en la clase ejecutora : " + e.getMessage(), e);
             
        }
        if(!msgError.isEmpty()){
            request.setAttribute("Error", msgError);
        }else{
            request.setAttribute("Mensaje", msg);    
        }
        request.setAttribute("claveFenisVo", datos);
        Long page = 0L;
        if(listaClavesFenis.size()>0 || bandera){
            request.getSession().setAttribute("listaCompletaClavesFenis", listaClavesFenis);    
        }
        
        return jsp;
    }

    

    @Override
    public boolean NoLogin (clsISesion lobjSesion) {
        boolean lbNoLogin = true;
        setMobjSesion(lobjSesion);
        if (getMobjSesion() != null) {
            if (getMobjSesion().Menu() != null) {
                if (getMobjSesion().Menu().size()>0) {
                    lbNoLogin=false;
                }
            }
        }
        return lbNoLogin;
    }

    @Override
    public boolean NoAcceso (clsISesion lobjSesion) {
        boolean lbNoAcceso = true;
        setMobjSesion(lobjSesion);
        if (getMobjSesion() != null) {
            if (getMobjSesion().TieneAcceso(5311)) {
               lbNoAcceso = false;
            }
        }
        return lbNoAcceso;
    }

    public clsISesion getMobjSesion() {
        return mobjSesion;
    }

    public void setMobjSesion(clsISesion mobjSesion) {
        this.mobjSesion = mobjSesion;
    }
}
