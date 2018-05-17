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

public class ClsNCatOpcionalesFaudi {
    
    private String mensaje;
    private String jsonBasico;
    private String cveTmp;
    
    private clsISesion mobjSesion = null;
    private ClsDCOpcionales opcionalesVo;
    
    private List<Integer> listCuotas;
    private Map<String, ClsDCOpcionales> mapOpcionales;
    
    Logger logger = Logger.getLogger(ClsNCatOpcionalesFaudi.class);
    
    public ClsNCatOpcionalesFaudi() {
        super();
    }
    
    /**Inicializa los campos.
     * @param lobjSesion, sesion.
     * */
    public void Inicializa(clsISesion lobjSesion){
        this.jsonBasico         = "";
        this.cveTmp             = "";
        this.setMobjSesion(lobjSesion);
        this.opcionalesVo       = new ClsDCOpcionales();
        this.listCuotas         = new ArrayList<Integer>();
        this.mapOpcionales      = new LinkedHashMap<String, ClsDCOpcionales>();
        this.obtenerCuotas();
        this.obtenerListaBasicos();
    }
    
    public void obtenerCuotas(){
        this.getListCuotas().clear();
        String qry                  = "SELECT DISTINCT NCUOTA FROM ICO_INV_COPCIONALES ORDER BY NCUOTA DESC ";
        
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
        String qry                  = "SELECT DISTINCT VBASICO FROM ICO_INV_COPCIONALES ORDER BY VBASICO";
        
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
            setMensaje("Ocurrio un error al consultar los basicos.");
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
    
    public void obtenerListOpcionales(ClsDCOpcionales vo){
        this.mapOpcionales.clear();
        
        ClsDCOpcionales data;
        StringBuilder qry           = new StringBuilder();
        StringBuilder qryWhere      = new StringBuilder();
        
        qry.append("SELECT NCUOTA, VBASICO, TO_CHAR(DFECHA,'dd/mm/yyyy') DFECHA, NTRASLADO, NSEGURO_TRASLADO, NPROM_PUBL, NASIST_VIAL, ");
            qry.append("NIVA, NANDANAC, NAMDA, NCTA_PROMEI, NSEG_PLAN_PISO, NAYUDA_SOCIAL, VCOMPANIA, VFECHA_MODIFICA ");
        qry.append("FROM ICO_INV_COPCIONALES ");
        
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
                data = new ClsDCOpcionales();
                data.setCuota(rst.getInt("NCUOTA"));
                data.setBasico(rst.getString("VBASICO"));
                data.setFecha(rst.getString("DFECHA"));
                data.setTraslado(rst.getDouble("NTRASLADO"));
                data.setSeguroTraslado(rst.getDouble("NSEGURO_TRASLADO"));
                data.setPromPubl(rst.getDouble("NPROM_PUBL"));
                data.setAsistVial(rst.getDouble("NASIST_VIAL"));
                data.setIva(rst.getInt("NIVA"));
                data.setAndanac(rst.getDouble("NANDANAC"));
                data.setAmda(rst.getDouble("NAMDA"));
                data.setCtaPromei(rst.getDouble("NCTA_PROMEI"));
                data.setSegPlanPiso(rst.getDouble("NSEG_PLAN_PISO"));
                data.setAyudaSocial(rst.getDouble("NAYUDA_SOCIAL"));
                data.setCompania(rst.getString("VCOMPANIA"));
                data.setFechaModifica(rst.getDate("VFECHA_MODIFICA"));
                data.generarClave();
                this.mapOpcionales.put(data.getCve(), data);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            setMensaje("Ocurrio un error al obtener la lista de Precios Opcionales.");
        } finally {
            try {
                if(stm != null){ stm.close(); }
                if(rst != null){ rst.close(); }
                if(lcnnConnection != null){ lcnnConnection.close(); }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
        if(this.mapOpcionales.size() == 0){ setMensaje("No se encontraron registros para la búsqueda."); }        
    }
    
    public void insertarOpcional(ClsDCOpcionales vo){
        int result                  = 0;
        StringBuilder qry           = new StringBuilder();
        
        qry.append("INSERT INTO ICO_INV_COPCIONALES (NCUOTA, VBASICO, DFECHA, NTRASLADO, NSEGURO_TRASLADO, NPROM_PUBL,  ");
            qry.append("NASIST_VIAL, NIVA, NANDANAC, NAMDA, NCTA_PROMEI, NSEG_PLAN_PISO, NAYUDA_SOCIAL, VCOMPANIA, VFECHA_MODIFICA )");
        qry.append("VALUES (?,?,TO_DATE(?, 'dd/mm/yyyy'),?,?,?,?,?,?,?,?,?,?,?,SYSDATE)");
        
        Connection lcnnConnection   = null;
        PreparedStatement ps        = null;
        try {
            lcnnConnection          = clsIUtil.getConnectionORCL().getConnection();
            ps = lcnnConnection.prepareStatement(qry.toString());
            ps.setInt(1, vo.getCuota());
            ps.setString(2, vo.getBasico());
            ps.setString(3, vo.getFecha());
            ps.setDouble(4, vo.getTraslado());
            ps.setDouble(5, vo.getSeguroTraslado());
            ps.setDouble(6, vo.getPromPubl());
            ps.setDouble(7, vo.getAsistVial());
            ps.setInt(8, vo.getIva());
            ps.setDouble(9, vo.getAndanac());
            ps.setDouble(10, vo.getAmda());
            ps.setDouble(11, vo.getCtaPromei());
            ps.setDouble(12, vo.getSegPlanPiso());
            ps.setDouble(13, vo.getAyudaSocial());
            ps.setString(14, vo.getCompania());
            result                  = ps.executeUpdate();
            if(result > 0){
                this.setMensaje("El Precio Opcional se inserto correctamente");
            }else{
                this.setMensaje("Ocurrio un error al insertar el Precio Opcional");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
            setMensaje("Ocurrio un error al insertar el Precio Opcional.");
        } finally {
            try {
                if(ps != null){ ps.close(); }
                if(lcnnConnection != null){ lcnnConnection.close(); }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    }
    
    public void actualizarOpcional(ClsDCOpcionales vo){
        int result                  = 0;
        StringBuilder qry           = new StringBuilder();
        
        qry.append("UPDATE ICO_INV_COPCIONALES SET NTRASLADO = ?, NSEGURO_TRASLADO = ?, NPROM_PUBL = ?, NASIST_VIAL = ?, ");
            qry.append("NIVA = ?, NANDANAC = ?, NAMDA = ?, NCTA_PROMEI = ?, NSEG_PLAN_PISO = ?, NAYUDA_SOCIAL = ?, VCOMPANIA = ?, VFECHA_MODIFICA = SYSDATE ");
        qry.append("WHERE NCUOTA = ? ");
            qry.append("AND VBASICO = ? ");
            qry.append("AND TO_DATE( TO_CHAR(DFECHA,'dd/mm/yyyy'),'dd/mm/yyyy') = TO_DATE(?,'dd/mm/yyyy') ");
            
        Connection lcnnConnection   = null;
        PreparedStatement ps        = null;
        try {
            lcnnConnection          = clsIUtil.getConnectionORCL().getConnection();
            ps = lcnnConnection.prepareStatement(qry.toString());
            ps.setDouble(1, vo.getTraslado());
            ps.setDouble(2, vo.getSeguroTraslado());
            ps.setDouble(3, vo.getPromPubl());
            ps.setDouble(4, vo.getAsistVial());
            ps.setInt(5, vo.getIva());
            ps.setDouble(6, vo.getAndanac());
            ps.setDouble(7, vo.getAmda());
            ps.setDouble(8, vo.getCtaPromei());
            ps.setDouble(9, vo.getSegPlanPiso());
            ps.setDouble(10, vo.getAyudaSocial());
            ps.setString(11, vo.getCompania());
            ps.setInt(12, vo.getCuota());
            ps.setString(13, vo.getBasico());
            ps.setString(14, vo.getFecha());
            result                  = ps.executeUpdate();
            if(result > 0){
                this.setMensaje("El Precio Opcional se actualizo correctamente");
            }else{
                this.setMensaje("Ocurrio un error al actualizar el Precio Opcional");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
            setMensaje("Ocurrio un error al actualizar el Precio Opcional.");
        } finally {
            try {
                if(ps != null){ ps.close(); }
                if(lcnnConnection != null){ lcnnConnection.close(); }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    }
    
    public void eliminarOpcional(ClsDCOpcionales vo){
        int result                  = 0;
        StringBuilder qry           = new StringBuilder();
        
        qry.append("DELETE FROM ICO_INV_COPCIONALES ");
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
                this.setMensaje("El Precio Opcional se elimino correctamente");
                generarBitacora(vo);
            }else{
                this.setMensaje("Ocurrio un error al eliminar el Precio Opcional");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            setMensaje("Ocurrio un error al eliminar el Precio Opcional.");
        } finally {
            try {
                if(ps != null){ ps.close(); }
                if(lcnnConnection != null){ lcnnConnection.close(); }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    }
    
    public void generarBitacora(ClsDCOpcionales vo){
        int result                  = 0;
        StringBuilder qry           = new StringBuilder();
        
        qry.append("INSERT INTO ICO_FAU_BITACORA_COPCIONALES (NCUOTA, VBASICO, DFECHA, NTRASLADO, NSEGURO_TRASLADO, NPROM_PUBL,  ");
            qry.append("NASIST_VIAL, NIVA, NANDANAC, NAMDA, NCTA_PROMEI, NSEG_PLAN_PISO, NAYUDA_SOCIAL, VCOMPANIA, VFECHA_MODIFICA, DFECHA_REGISTRO, VUSUARIO )");
        qry.append("VALUES (?,?,TO_DATE(?, 'dd/mm/yyyy'),?,?,?,?,?,?,?,?,?,?,?,?,SYSDATE,?)");
        
        Connection lcnnConnection   = null;
        PreparedStatement ps        = null;
        try {
            lcnnConnection          = clsIUtil.getConnectionORCL().getConnection();
            ps = lcnnConnection.prepareStatement(qry.toString());
            ps.setInt(1, vo.getCuota());
            ps.setString(2, vo.getBasico());
            ps.setString(3, vo.getFecha());
            ps.setDouble(4, vo.getTraslado());
            ps.setDouble(5, vo.getSeguroTraslado());
            ps.setDouble(6, vo.getPromPubl());
            ps.setDouble(7, vo.getAsistVial());
            ps.setInt(8, vo.getIva());
            ps.setDouble(9, vo.getAndanac());
            ps.setDouble(10, vo.getAmda());
            ps.setDouble(11, vo.getCtaPromei());
            ps.setDouble(12, vo.getSegPlanPiso());
            ps.setDouble(13, vo.getAyudaSocial());
            ps.setString(14, vo.getCompania());
            ps.setDate(15, new Date(vo.getFechaModifica().getTime()));
            ps.setString(16, this.getMobjSesion().UserID());
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

    public List<Integer> getListCuotas() {
        return listCuotas;
    }

    public void setListCuotas(List<Integer> listCuotas) {
        this.listCuotas = listCuotas;
    }

    public Map<String, ClsDCOpcionales> getMapOpcionales() {
        return mapOpcionales;
    }

    public void setMapOpcionales(Map<String, ClsDCOpcionales> mapOpcionales) {
        this.mapOpcionales = mapOpcionales;
    }

    public ClsDCOpcionales getOpcionalesVo() {
        return opcionalesVo;
    }

    public void setOpcionalesVo(ClsDCOpcionales opcionalesVo) {
        this.opcionalesVo = opcionalesVo;
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
}
