package SS.ICO.V10.Fau;

import SS.ICO.V10.Common.Negocio.ICONException;

import SS.ICO.V10.Common.clsISesion;

import SS.ICO.V20.general.model.Combo;

import com.google.gson.JsonArray;

import com.google.gson.JsonObject;

import java.sql.SQLException;

import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.List;


import javax.servlet.http.HttpServletRequest;


import net.sf.json.JSONArray;

import org.apache.log4j.Logger; 

public class ClsNCatClaveFenis {
    
    Logger log = Logger.getLogger(this.getClass().getName());
    private clsISesion mobjSesion;
    private ClsDCatClaveFenis accesoBD;
    
    public ClsNCatClaveFenis() {
        try {
            accesoBD = new ClsDCatClaveFenis(mobjSesion);
        } catch (ICONException e) {
            log.error("No se pudo acceder a la clase de acceso a datos en la clase ClsDCatClaveFenis " + e.getMessage());
        }
    }
    
    
    public List<ClsDCatClaveFenisVo[]> filtraClavesFenis(ClsDCatClaveFenisVo filtro) throws SQLException{
        List<ClsDCatClaveFenisVo[]> listaResultado = new ArrayList<ClsDCatClaveFenisVo[]>();
        List<ClsDCatClaveFenisVo> listaConsultada = new ArrayList<ClsDCatClaveFenisVo>();
        try {
            if(filtro==null){
                listaConsultada = accesoBD.filtraClaveFenis(null);
            }else{
             listaConsultada = accesoBD.filtraClaveFenis(filtro);
            }
            int totalRegPagina = 15;
            ClsDCatClaveFenisVo[] array=new ClsDCatClaveFenisVo[totalRegPagina];
            int i=0;
            for(ClsDCatClaveFenisVo p : listaConsultada){
                array[i++] = p;
                if(i==totalRegPagina){
                    listaResultado.add(array);   
                    array = new ClsDCatClaveFenisVo[totalRegPagina];
                    i=0;
                }
            } 
            if(array[0]!=null){
                listaResultado.add(array);   
            }
        } catch (SQLException e) {
            log.error("Hubo error al filtrar las Politicas de Venta" + e.getMessage());
            throw e;
        }
        return listaResultado;
    }

    public clsISesion getMobjSesion() {
        return mobjSesion;
    }

    public void setMobjSesion(clsISesion mobjSesion) {
        this.mobjSesion = mobjSesion;
    }
    
    
    public JsonArray getPage(HttpServletRequest request) throws SQLException{
        List<ClsDCatClaveFenisVo[]> listaClavesFenis = 
            (List<ClsDCatClaveFenisVo[]>)request.getSession().getAttribute("listaCompletaClavesFenis");
        Integer page = (Integer)request.getAttribute("numPagina");
        page--;
        JsonArray array = new JsonArray();
        JsonObject objeto;

        for(ClsDCatClaveFenisVo cf : listaClavesFenis.get(page)){
            if(cf!=null){
                objeto = new JsonObject();
                objeto.addProperty("polVidPk", cf.getPolVidPk());
                objeto.addProperty("polVdescripcion", (cf.getPolVdescripcion()==null? "": cf.getPolVdescripcion()));
                objeto.addProperty("letra", cf.getLetra()==null?"":cf.getLetra());
                objeto.addProperty("fechaModifica", cf.getFechaModifica()==null?"":cf.getFechaModifica());
                objeto.addProperty("status", cf.getStatus()==null?"":cf.getStatus());
                objeto.addProperty("usuarioModifica", cf.getUsuarioModifica()==null?"":cf.getUsuarioModifica());
                array.add(objeto);
            }
        }
        return array;
    }
    
    public int actualizaClaveFenis(ClsDCatClaveFenisVo registroActualizar) throws SQLException{
        int numReg = 0;
        try {
            numReg = accesoBD.actualizaClaveFenis(registroActualizar);
        } catch (SQLException e) {
            log.error("Hubo error al actualizar la Clave Fenis " + e.getMessage());
            throw e;
        }
        return numReg;
    }
    
    public int insertaClaveFenis(ClsDCatClaveFenisVo registro)throws SQLException{
        int numReg = 0;
        try {
            numReg= accesoBD.insertaClaveFenis(registro);
        } catch (SQLException e) {
            log.error("Error al insertar la Clave Fenis " + e.getMessage());
            //e.setMessage("Error al insertar la Politica de Venta");
            throw e;
        }
        return numReg;
    }
    public int borraClaveFenis(String clave) throws SQLException {
        int numReg = 0;
        try {
            numReg=accesoBD.borrarClaveFenis(clave);
        } catch (SQLException e) {
            log.error("Hubo error al borrar  Clave Fenis " + e.getMessage());
            throw e;
        }
        return numReg;
    }
    
   
}
