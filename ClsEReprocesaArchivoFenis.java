package SS.ICO.V10.Fau;

import SS.ICO.V10.Common.clsIEjecutable;
import SS.ICO.V10.Common.clsISesion;

import java.text.SimpleDateFormat;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.PageContext;

import org.apache.log4j.Logger;

public class ClsEReprocesaArchivoFenis extends clsIEjecutable{
    
    Logger log = Logger.getLogger(this.getClass().getName());
    /**
     * Almacena la acci&oacuten que realizar&aacute el jsp.
     */
    protected String mstrAccion;
    
    /**
     * Almacena la sesi&oacuten de la clase.
     */
    private clsISesion mobjSesion;
    
    public ClsEReprocesaArchivoFenis() {
        super();
    }
    
    public ClsEReprocesaArchivoFenis(String lstrAction) {
        mstrAccion = lstrAction;
    }

    @Override
    public String Procesa(HttpServletRequest lobjRequest, HttpServletResponse lobjResponse, PageContext lobjContext) {
        String jsp = "./clsJReprocesarArchivoFenis.jsp";
        HttpSession lsesSesion = lobjRequest.getSession(true);   
        ClsNGeneraArchivoFenis mobjBase = null;
        String msg = "";
        boolean bandera =  true;
        if(mstrAccion.equals("Inicio")){
            mobjBase = new ClsNGeneraArchivoFenis();
            lsesSesion.setAttribute("mobjBase", mobjBase);
        }else if(mstrAccion.equals("Regenerar")){
            try{
                mobjBase = (ClsNGeneraArchivoFenis)lsesSesion.getAttribute("mobjBase");
                String tipoArchivo =  lobjRequest.getParameter("rdTipoArchivo");
                String tipoFiltro = lobjRequest.getParameter("txtTipoFiltro");
                String[] fechas = lobjRequest.getParameterValues("txtFecha");
                String[] folios = lobjRequest.getParameterValues("txtFolio");
               
                int idTipoArchivo = Integer.parseInt(tipoArchivo);
                int idTipoFiltro = Integer.parseInt(tipoFiltro);
                log.info("*****Inicia creacion de archivos fenis: " + new Date());
                if(tipoFiltro.equals("1")){
                    mobjBase.crearArchivosFenis(true, idTipoArchivo, idTipoFiltro, fechas[0], fechas[1]);
                }else if(tipoFiltro.equals("2")){
                    mobjBase.crearArchivosFenis(true, idTipoArchivo, idTipoFiltro, folios[0], folios[1]);
                }
                log.info("*****Termina creacion de archivos fenis: " + new Date());
                msg = "Regeneracion de archivos Fenis exitosa, archivos cargados en el ftp";
            }catch(Exception e){
                bandera=false;
                msg = "Error al regenerar los archivos Fenis, "+ e.getMessage();
            }finally{
                if(bandera){
                    lobjRequest.setAttribute("ErrorRegenerar",msg);
                }else{
                    lobjRequest.setAttribute("MensajeRegenerar",msg);
                }
            }
        }else if(mstrAccion.equals("Reprocesar")){
            try{
                mobjBase = (ClsNGeneraArchivoFenis)lsesSesion.getAttribute("mobjBase");
                mobjBase.copiaArchivoFenis_BD();
                msg = "Reprocesamiento de archivos Fenis exitosa, archivos cargados en BD";
            }catch(Exception e){
                bandera=false;
                msg = "Error al regenerar los archivos Fenis, "+ e.getMessage();
            }finally{
                if(bandera){
                    lobjRequest.setAttribute("ErrorReprocesar",msg);
                }else{
                    lobjRequest.setAttribute("MensajeReprocesar",msg);
                }
            }
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
