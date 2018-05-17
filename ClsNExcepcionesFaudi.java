package SS.ICO.V10.Fau;

import SS.ICO.V10.Common.clsISesion;

import SS.ICO.V10.Common.clsIUtil;

import com.google.gson.Gson;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

public class ClsNExcepcionesFaudi {
    
    private String mensaje;
    private String jsonBasico;
    private String cveTmp;
    
    private clsISesion mobjSesion = null;
    private ClsDExcepcionesVo excepcionesVo;
    
    private List<Integer> listCuotas;
    private Map<String, ClsDExcepcionesVo> mapExcepciones;
    
    Logger logger = Logger.getLogger(ClsNExcepcionesFaudi.class);
    
    public ClsNExcepcionesFaudi() {
        super();
    }
    
    /**Inicializa los campos.
     * @param lobjSesion, sesion.
     * */
    public void Inicializa(clsISesion lobjSesion){
        this.jsonBasico             = "";
        this.cveTmp                 = "";
        this.setMobjSesion(lobjSesion);
        this.excepcionesVo          = new ClsDExcepcionesVo();
        this.listCuotas             = new ArrayList<Integer>();
        this.mapExcepciones         = new LinkedHashMap<String, ClsDExcepcionesVo>();
        this.obtenerCuotas();
        this.obtenerListaBasicos();
    }

    public void obtenerCuotas(){
        this.getListCuotas().clear();
        String qry                  = "SELECT DISTINCT NCUOTA FROM ICO_INV_EXCEPCIONES_OPC ORDER BY NCUOTA DESC";
        
        Connection lcnnConnection   = null;
        Statement stm               = null;
        ResultSet rst               = null;
        try {
            lcnnConnection          = clsIUtil.getConnectionORCL().getConnection();
            stm                     = lcnnConnection.createStatement();
            rst                     = stm.executeQuery(qry);
            while (rst.next()) {
                this.getListCuotas().add(rst.getInt(1));
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            setMensaje("Ocurrio un error al consultar las cuotas.");
        } finally {
            try {
                if(stm != null){ stm.close(); }
                if(rst != null){ rst.close(); }
                if(lcnnConnection != null){ lcnnConnection.close(); }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    }
    
    public void obtenerListaBasicos(){
        String qry                  = "SELECT DISTINCT VBASICO FROM ICO_INV_EXCEPCIONES_OPC ORDER BY VBASICO";
        
        List<String> listTmp        = new ArrayList<String>();
        
        Connection lcnnConnection   = null;
        Statement stm               = null;
        ResultSet rst               = null;
        try {
            lcnnConnection          = clsIUtil.getConnectionORCL().getConnection();
            stm                     = lcnnConnection.createStatement();
            rst                     = stm.executeQuery(qry);
            while (rst.next()) {
                listTmp.add(rst.getString(1));
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            setMensaje("Ocurrio un error al consultar lista de basicos.");
        } finally {
            try {
                if(stm != null){ stm.close(); }
                if(rst != null){ rst.close(); }
                if(lcnnConnection != null){ lcnnConnection.close(); }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
        Gson gson = new Gson();
        this.setJsonBasico(gson.toJson(listTmp));
    }
    
    public void obtenerListExcepciones(ClsDExcepcionesVo vo){
        this.mapExcepciones.clear();
        
        ClsDExcepcionesVo data;
        StringBuilder qry           = new StringBuilder();
        StringBuilder qryWhere      = new StringBuilder();
        
        qry.append("SELECT NCUOTA, VBASICO, TO_CHAR(DFECHA,'dd/mm/yyyy') DFECHA, NVL(VCABECERA, ' ') VCABECERA, NDISTRIBUIDOR, NTRASLADO, VFECHA_MODIFICA ");
        qry.append("FROM ICO_INV_EXCEPCIONES_OPC ");
        
        if(vo.getCuota() > 0){
            qryWhere.append(qryWhere.length() == 0 ? "WHERE " : "AND ");
            qryWhere.append("NCUOTA = " + vo.getCuota() + " ");
        }
        if(vo.getBasico() != null && !vo.getBasico().equals("")){
            qryWhere.append(qryWhere.length() == 0 ? "WHERE " : "AND ");
            qryWhere.append("VBASICO = '" + vo.getBasico() + "' ");
        }
        if(vo.getFecha() != null && !vo.getFecha().equals("")){
            qryWhere.append(qryWhere.length() == 0 ? "WHERE " : "AND ");
            qryWhere.append("TO_DATE( TO_CHAR(DFECHA,'dd/mm/yyyy'),'dd/mm/yyyy') = TO_DATE('" + vo.getFecha() + "','dd/mm/yyyy')" + " ");
        }
        
        qry.append(qryWhere.toString());
        qry.append("ORDER BY NCUOTA DESC ");
        
        Connection lcnnConnection   = null;
        Statement stm               = null;
        ResultSet rst               = null;
        try {
            lcnnConnection          = clsIUtil.getConnectionORCL().getConnection();
            stm                     = lcnnConnection.createStatement();
            rst                     = stm.executeQuery(qry.toString());
            while (rst.next()) {
                data = new ClsDExcepcionesVo();
                data.setCuota(rst.getInt("NCUOTA"));
                data.setBasico(rst.getString("VBASICO"));
                data.setFecha(rst.getString("DFECHA"));
                data.setCabecera(rst.getString("VCABECERA"));
                data.setDistribuidor(rst.getInt("NDISTRIBUIDOR"));
                data.setTraslado(rst.getDouble("NTRASLADO"));
                data.setFechaModifica(rst.getDate("VFECHA_MODIFICA"));
                data.generarClave();
                this.mapExcepciones.put(data.getCve(), data);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            setMensaje("Ocurrio un error al obtener la lista de Excepciones.");
        } finally {
            try {
                if(stm != null){ stm.close(); }
                if(rst != null){ rst.close(); }
                if(lcnnConnection != null){ lcnnConnection.close(); }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
        if(this.mapExcepciones.size() == 0){ setMensaje("No se encontraron registros para la búsqueda."); } 
    }
    
    public void insertarExcepcion(ClsDExcepcionesVo vo){
        int result                  = 0;
        StringBuilder qry           = new StringBuilder();
        
        qry.append("INSERT INTO ICO_INV_EXCEPCIONES_OPC (NCUOTA, VBASICO, DFECHA, VCABECERA, NDISTRIBUIDOR, NTRASLADO, VFECHA_MODIFICA) ");
        qry.append("VALUES (?,?,TO_DATE(?, 'dd/mm/yyyy'),?,?,?,SYSDATE)");
        
        Connection lcnnConnection   = null;
        PreparedStatement ps        = null;
        try {
            lcnnConnection          = clsIUtil.getConnectionORCL().getConnection();
            ps = lcnnConnection.prepareStatement(qry.toString());
            ps.setInt(1, vo.getCuota());
            ps.setString(2, vo.getBasico());
            ps.setString(3, vo.getFecha());
            ps.setString(4, vo.getCabecera());
            ps.setInt(5, vo.getDistribuidor());
            ps.setDouble(6, vo.getTraslado());
            result                  = ps.executeUpdate();
            if(result > 0){
                this.setMensaje("La excepción se inserto correctamente");
            }else{
                this.setMensaje("Ocurrio un error al insertar la excepción");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
            setMensaje("Ocurrio un error al insertar la excepción.");
        } finally {
            try {
                if(ps != null){ ps.close(); }
                if(lcnnConnection != null){ lcnnConnection.close(); }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    }
    
    public void actualizarExcepcion(ClsDExcepcionesVo vo){
        int result                  = 0;
        StringBuilder qry           = new StringBuilder();
        
        qry.append("UPDATE ICO_INV_EXCEPCIONES_OPC SET VCABECERA = ?, NDISTRIBUIDOR = ?, NTRASLADO = ?, VFECHA_MODIFICA = SYSDATE ");
        qry.append("WHERE NCUOTA = ? ");
            qry.append("AND VBASICO = ? ");
            qry.append("AND TO_DATE( TO_CHAR(DFECHA,'dd/mm/yyyy'),'dd/mm/yyyy') = TO_DATE(?,'dd/mm/yyyy') ");
            
        Connection lcnnConnection   = null;
        PreparedStatement ps        = null;
        try {
            lcnnConnection          = clsIUtil.getConnectionORCL().getConnection();
            ps = lcnnConnection.prepareStatement(qry.toString());
            ps.setString(1, vo.getCabecera());
            ps.setInt(2, vo.getDistribuidor());
            ps.setDouble(3, vo.getTraslado());
            ps.setInt(4, vo.getCuota());
            ps.setString(5, vo.getBasico());
            ps.setString(6, vo.getFecha());
            result                  = ps.executeUpdate();
            if(result > 0){
                this.setMensaje("La excepción se actualizo correctamente");
            }else{
                this.setMensaje("Ocurrio un error al actualizar la excepción");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
            setMensaje("Ocurrio un error al actualizar la excepción.");
        } finally {
            try {
                if(ps != null){ ps.close(); }
                if(lcnnConnection != null){ lcnnConnection.close(); }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    }
    
    public void eliminarExcepcion(ClsDExcepcionesVo vo){
        int result                  = 0;
        StringBuilder qry           = new StringBuilder();
        
        qry.append("DELETE FROM ICO_INV_EXCEPCIONES_OPC ");
        qry.append("WHERE NCUOTA = ? ");
            qry.append("AND VBASICO = ? ");
            qry.append("AND TO_DATE( TO_CHAR(DFECHA,'dd/mm/yyyy'),'dd/mm/yyyy') = TO_DATE(?,'dd/mm/yyyy') ");
        
        Connection lcnnConnection   = null;
        PreparedStatement ps        = null;
        try {
            lcnnConnection          = clsIUtil.getConnectionORCL().getConnection();
            ps = lcnnConnection.prepareStatement(qry.toString());
            ps.setInt(1, vo.getCuota());
            ps.setString(2, vo.getBasico());
            ps.setString(3, vo.getFecha());
            result                  = ps.executeUpdate();
            if(result > 0){
                this.setMensaje("La excepción se elimino correctamente");
                generarBitacora(vo);
            }else{
                this.setMensaje("Ocurrio un error al eliminar la excepción");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            setMensaje("Ocurrio un error al eliminar la excepción");
        } finally {
            try {
                if(ps != null){ ps.close(); }
                if(lcnnConnection != null){ lcnnConnection.close(); }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    }
    
    public void generarBitacora(ClsDExcepcionesVo vo){
        int result                  = 0;
        StringBuilder qry           = new StringBuilder();
        
        qry.append("INSERT INTO ICO_FAU_BITACORA_EXCEPCIONES (NCUOTA, VBASICO, DFECHA, VCABECERA, NDISTRIBUIDOR, NTRASLADO, VFECHA_MODIFICA, DFECHA_REGISTRO, VUSUARIO) ");
        qry.append("VALUES (?,?,TO_DATE(?, 'dd/mm/yyyy'),?,?,?,?,SYSDATE,?)");
        
        Connection lcnnConnection   = null;
        PreparedStatement ps        = null;
        try {
            lcnnConnection          = clsIUtil.getConnectionORCL().getConnection();
            ps = lcnnConnection.prepareStatement(qry.toString());
            ps.setInt(1, vo.getCuota());
            ps.setString(2, vo.getBasico());
            ps.setString(3, vo.getFecha());
            ps.setString(4, vo.getCabecera());
            ps.setInt(5, vo.getDistribuidor());
            ps.setDouble(6, vo.getTraslado());
            ps.setDate(7, new Date(vo.getFechaModifica().getTime()));
            ps.setString(8, this.getMobjSesion().UserID());
            result                  = ps.executeUpdate();
            if(result > 0){
                logger.info("La bitacora se inserto correctamente");
            }else{
                logger.info("Ocurrio un error al insertar la bitacora");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            try {
                if(ps != null){ ps.close(); }
                if(lcnnConnection != null){ lcnnConnection.close(); }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getJsonBasico() {
        return jsonBasico;
    }

    public void setJsonBasico(String jsonBasico) {
        this.jsonBasico = jsonBasico;
    }

    public String getCveTmp() {
        return cveTmp;
    }

    public void setCveTmp(String cveTmp) {
        this.cveTmp = cveTmp;
    }

    public clsISesion getMobjSesion() {
        return mobjSesion;
    }

    public void setMobjSesion(clsISesion mobjSesion) {
        this.mobjSesion = mobjSesion;
    }

    public ClsDExcepcionesVo getExcepcionesVo() {
        return excepcionesVo;
    }

    public void setExcepcionesVo(ClsDExcepcionesVo excepcionesVo) {
        this.excepcionesVo = excepcionesVo;
    }

    public List<Integer> getListCuotas() {
        return listCuotas;
    }

    public void setListCuotas(List<Integer> listCuotas) {
        this.listCuotas = listCuotas;
    }

    public Map<String, ClsDExcepcionesVo> getMapExcepciones() {
        return mapExcepciones;
    }

    public void setMapExcepciones(Map<String, ClsDExcepcionesVo> mapExcepciones) {
        this.mapExcepciones = mapExcepciones;
    }
}
