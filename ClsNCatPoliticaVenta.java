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

public class ClsNCatPoliticaVenta {
    
    Logger log = Logger.getLogger(this.getClass().getName());
    private clsISesion mobjSesion;
    private ClsDCatPoliticaVenta accesoBD;
    
    public ClsNCatPoliticaVenta() {
        try {
//            getMobjSesion();
            
            accesoBD = new ClsDCatPoliticaVenta(mobjSesion);
        } catch (ICONException e) {
            log.error("No se pudo acceder a la clase de acceso a datos en la clase ClsDCatPoliticaVenta " + e.getMessage());
        }
    }
    
    
    public List<ClsDCatPoliticaVentaVo[]> filtraPoliticaVenta(ClsDCatPoliticaVentaVo filtro) throws SQLException{
        List<ClsDCatPoliticaVentaVo[]> listaResultado = new ArrayList<ClsDCatPoliticaVentaVo[]>();
        List<ClsDCatPoliticaVentaVo> listaConsultada = new ArrayList<ClsDCatPoliticaVentaVo>();
        try {
            if(filtro==null){
                listaConsultada = accesoBD.filtraPoliticaVenta(null);
            }else{
             listaConsultada = accesoBD.filtraPoliticaVenta(filtro);
            }
            int totalRegPagina = 15;
            ClsDCatPoliticaVentaVo[] array=new ClsDCatPoliticaVentaVo[totalRegPagina];
            int i=0;
            array = new ClsDCatPoliticaVentaVo[totalRegPagina];
            for(ClsDCatPoliticaVentaVo p : listaConsultada){
                array[i++] = p;
                if(i==totalRegPagina){
                    listaResultado.add(array);   
                    array = new ClsDCatPoliticaVentaVo[totalRegPagina];
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
        List<ClsDCatPoliticaVentaVo[]> listaPoliticasVenta = 
            (List<ClsDCatPoliticaVentaVo[]>)request.getSession().getAttribute("listaCompletaPoliticasVenta");
        Integer page = (Integer)request.getAttribute("numPagina");
        page--;
        JsonArray array = new JsonArray();
        JsonObject objeto;

        for(ClsDCatPoliticaVentaVo pv : listaPoliticasVenta.get(page)){
            if(pv!=null){
                objeto = new JsonObject();
                objeto.addProperty("polVidPk", pv.getPolVidPk());
                objeto.addProperty("polVdescripcion", (pv.getPolVdescripcion()==null? "": pv.getPolVdescripcion()));
                objeto.addProperty("polVfinanciera", pv.getPolVfinanciera()==null?"":pv.getPolVfinanciera());
                objeto.addProperty("polVpau", pv.getPolVpau()==null?"":pv.getPolVpau());
                objeto.addProperty("tvtavVnopagincentivo", pv.getTvtavVnopagincentivo()==null?"":pv.getTvtavVnopagincentivo());
                objeto.addProperty("polVmc", pv.getPolVmc()==null?"":pv.getPolVmc());
                objeto.addProperty("polVte", pv.getPolVte()==null?"":pv.getPolVte());
                objeto.addProperty("vcolateral", pv.getVcolateral()==null?"":pv.getVcolateral());
                objeto.addProperty("vdescolateral", pv.getVdescolateral()==null?"":pv.getVdescolateral());
                objeto.addProperty("vvigencia", pv.getVvigencia()==null?"":pv.getVvigencia());
                objeto.addProperty("vfechamodif", pv.getVfechamodif());
                objeto.addProperty("incNporcFlotilla", pv.getIncNporcFlotilla());
                objeto.addProperty("nidCategoria", pv.getNidCategoria());
                array.add(objeto);
            }
        }
        return array;
    }
    
    public int actualizaPoliticaVenta(ClsDCatPoliticaVentaVo registroActualizar) throws SQLException{
        int numReg = 0;
        try {
            numReg = accesoBD.actualizaPoliticaVenta(registroActualizar);
        } catch (SQLException e) {
            log.error("Hubo error al actualizar la Politica de Venta" + e.getMessage());
            throw e;
        }
        return numReg;
    }
    
    public int insertaPoliticaVenta(ClsDCatPoliticaVentaVo registro)throws SQLException{
        int numReg = 0;
        try {
            numReg= accesoBD.insertaPoliticaVenta(registro);
        } catch (SQLException e) {
            log.error("Error al insertar la Politica de Venta" + e.getMessage());
            //e.setMessage("Error al insertar la Politica de Venta");
            throw e;
        }
        return numReg;
    }
    public int borraPoliticaVenta(String clave) throws SQLException {
        int numReg = 0;
        try {
            numReg=accesoBD.borrarPoliticaVenta(clave);
        } catch (SQLException e) {
            log.error("Hubo error al borrar las Politicas de Venta" + e.getMessage());
            throw e;
        }
        return numReg;
    }
    
    public List<Combo> obtieneOpcionesPolVMC() throws SQLException{
        List<Combo> opciones = new ArrayList<Combo>();
        try {
            opciones= accesoBD.obtieneOpcionesPolVMC();
        } catch (SQLException e) {
            log.error("Hubo error al obtener el catalogo ICO_INV_CTIPO_VMC las Politicas de Venta" + e.getMessage());
            throw e;
        }
        return opciones;
    }
   
}
