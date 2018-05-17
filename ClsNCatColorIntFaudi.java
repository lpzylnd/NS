package SS.ICO.V10.Fau;

import SS.ICO.V10.Common.clsISesion;

import SS.ICO.V10.Common.clsIUtil;

import com.google.gson.Gson;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.record.formula.functions.True;

public class ClsNCatColorIntFaudi {
    
    private String mensaje;
    private String jsonDescripciones;
    private String cveTmp;
    
    private clsISesion mobjSesion = null;
    private ClsDColoresIntVo coloresIntVo;
    
    private List<Integer> listAnios;
    private List<String[]> listOrigenes;
    private List<String> listCodigos;
    private List<String[]> listPaises;
    private Map<String, ClsDColoresIntVo> mapColoresInt;
    
    Logger logger = Logger.getLogger(ClsNCatColorIntFaudi.class);
    
    public ClsNCatColorIntFaudi() {
        super();
    }
    
    /**Inicializa los campos.
     * @param lobjSesion, sesion.
     * */
    public void Inicializa(clsISesion lobjSesion){
        this.jsonDescripciones  = "";
        this.cveTmp             = "";
        this.coloresIntVo       = new ClsDColoresIntVo();
        this.mobjSesion         = lobjSesion;
        this.listAnios          = new ArrayList<Integer>();
        this.listOrigenes       = new ArrayList<String[]>();
        this.listCodigos        = new ArrayList<String>();
        this.listPaises         = new ArrayList<String[]>();
        this.mapColoresInt      = new LinkedHashMap<String, ClsDColoresIntVo>();
        obtenerAnios();
        obtenerOrigenes();
        obtenerCodigos();
        obtenerPaises();
        obtenerDescripciones();
    }
    
    public void obtenerAnios(){
        this.listAnios.clear();
        String qry                  = "SELECT * FROM ICO_INV_CANIOS_MODELO ORDER BY ANIO_NID_PK DESC";
        
        Connection lcnnConnection   = null;
        Statement stm               = null;
        ResultSet rst               = null;
        try {
            lcnnConnection          = clsIUtil.getConnectionORCL().getConnection();
            stm                     = lcnnConnection.createStatement();
            rst                     = stm.executeQuery(qry);
            while (rst.next()) {
                this.listAnios.add(rst.getInt(1));
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            setMensaje("Ocurrio un error al consultar años.");
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
    
    public void obtenerOrigenes(){
        this.listOrigenes.clear();
        String qry                  = "SELECT * FROM ICO_INV_CORIGENES";
        
        Connection lcnnConnection   = null;
        Statement stm               = null;
        ResultSet rst               = null;
        try {
            lcnnConnection          = clsIUtil.getConnectionORCL().getConnection();
            stm                     = lcnnConnection.createStatement();
            rst                     = stm.executeQuery(qry);
            while (rst.next()) {
                String[] origenes   = { rst.getString(1), rst.getString(2) };
                this.listOrigenes.add(origenes);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            setMensaje("Ocurrio un error al consultar origenes.");
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
    
    public void obtenerCodigos(){
        this.listCodigos.clear();
        String qry                  = "SELECT DISTINCT INT_VCODIGO_PK FROM ICO_INV_CCOLORES_INT ORDER BY INT_VCODIGO_PK";
        
        Connection lcnnConnection   = null;
        Statement stm               = null;
        ResultSet rst               = null;
        try {
            lcnnConnection          = clsIUtil.getConnectionORCL().getConnection();
            stm                     = lcnnConnection.createStatement();
            rst                     = stm.executeQuery(qry);
            while (rst.next()) {
                this.listCodigos.add(rst.getString(1));
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            setMensaje("Ocurrio un error al consultar codigos.");
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
    
    public void obtenerPaises(){
        this.listPaises.clear();
        String qry                  = "SELECT PAIS_VID_PK, PAIS_VDESCRIPCION FROM ICO_INV_CPAISES";
        
        Connection lcnnConnection   = null;
        Statement stm               = null;
        ResultSet rst               = null;
        try {
            lcnnConnection          = clsIUtil.getConnectionORCL().getConnection();
            stm                     = lcnnConnection.createStatement();
            rst                     = stm.executeQuery(qry);
            while (rst.next()) {
                String[] paises     = { rst.getString(1), rst.getString(2) };
                this.listPaises.add(paises);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            setMensaje("Ocurrio un error al consultar paises.");
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

    public void obtenerDescripciones(){

        String qry                      = "SELECT DISTINCT INT_VDESCRIPCION FROM ICO_INV_CCOLORES_INT ORDER BY INT_VDESCRIPCION";

        List<String> listDescripciones  = new ArrayList<String>();

        Connection lcnnConnection       = null;
        Statement stm                   = null;
        ResultSet rst                   = null;
        try {
            lcnnConnection              = clsIUtil.getConnectionORCL().getConnection();
            stm                         = lcnnConnection.createStatement();
            rst                         = stm.executeQuery(qry);
            while (rst.next()) {
                listDescripciones.add(rst.getString(1));
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            setMensaje("Ocurrio un error al consultar descripciones.");
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
        this.jsonDescripciones = gson.toJson(listDescripciones);
    }
    
    public void obtenerListaColoresInt(ClsDColoresIntVo vo){
        this.mapColoresInt.clear();
        
        ClsDColoresIntVo data;
        StringBuilder qry           = new StringBuilder();
        
        qry.append("SELECT C.ANIO_NID_PK, O.ORIG_VID_PK, O.ORIG_VDESCRIPCION, C.INT_VCODIGO_PK, ");
            qry.append("P.PAIS_VID_PK, P.PAIS_VDESCRIPCION, C.INT_VDESCRIPCION, C.R_CONV_COL_INT_PAIS ");
        qry.append("FROM ICO_NIS_CCOLORES_INT_PAIS C, ");
            qry.append("ICO_INV_CPAISES P, ICO_INV_CORIGENES O ");
        qry.append("WHERE C.PAIS_VID_PK = P.PAIS_VID_PK ");
            qry.append("AND C.ORIG_VID_PK = O.ORIG_VID_PK ");
        
        if(vo.getAnio() > 0){
            qry.append("AND C.ANIO_NID_PK = " + vo.getAnio() + " ");
        }
        if(vo.getIdOrigen() != null && !vo.getIdOrigen().equals("")){
            qry.append("AND C.ORIG_VID_PK = '" + vo.getIdOrigen() + "' ");
        }
        if(vo.getCodigo() != null && !vo.getCodigo().equals("")){
            qry.append("AND C.INT_VCODIGO_PK = '" + vo.getCodigo() + "' ");
        }
        if(vo.getIdPais() != null && !vo.getIdPais().equals("")){
            qry.append("AND C.PAIS_VID_PK = '" + vo.getIdPais() + "' ");
        }
        if(vo.getDescripcion() != null && !vo.getDescripcion().equals("")){
            qry.append("AND C.INT_VDESCRIPCION = '" + vo.getDescripcion() + "' ");
        }
        qry.append("ORDER BY C.ANIO_NID_PK DESC ");
        
        Connection lcnnConnection   = null;
        Statement stm               = null;
        ResultSet rst               = null;
        try {
            lcnnConnection          = clsIUtil.getConnectionORCL().getConnection();
            stm                     = lcnnConnection.createStatement();
            rst                     = stm.executeQuery(qry.toString());
            while (rst.next()) {
                data = new ClsDColoresIntVo();
                data.setAnio(rst.getInt("ANIO_NID_PK"));
                data.setIdOrigen(rst.getString("ORIG_VID_PK"));
                data.setOrigenDesc(rst.getString("ORIG_VDESCRIPCION"));
                data.setCodigo(rst.getString("INT_VCODIGO_PK"));
                data.setIdPais(rst.getString("PAIS_VID_PK"));
                data.setPaisDesc(rst.getString("PAIS_VDESCRIPCION"));
                data.setDescripcion(rst.getString("INT_VDESCRIPCION"));
                data.setConvColor(rst.getString("R_CONV_COL_INT_PAIS") == null ? "" : rst.getString("R_CONV_COL_INT_PAIS"));
                data.calcularClave();
                this.mapColoresInt.put(data.getClave(), data);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            setMensaje("Ocurrio un error al obtener la lista de colores.");
        } finally {
            try {
                if(stm != null){ stm.close(); }
                if(rst != null){ rst.close(); }
                if(lcnnConnection != null){ lcnnConnection.close(); }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
        if(this.mapColoresInt.size() == 0){ setMensaje("No se encontraron registros para la búsqueda."); }
    }
    
    public boolean existeRegistro(ClsDColoresIntVo vo){
        boolean existe              = false;
        StringBuilder qry           = new StringBuilder();
        
        qry.append("SELECT * FROM ICO_NIS_CCOLORES_INT_PAIS ");
        qry.append("WHERE ANIO_NID_PK = ? ");
            qry.append("AND ORIG_VID_PK = ? ");
            qry.append("AND INT_VCODIGO_PK = ? ");
            qry.append("AND PAIS_VID_PK = ? ");
        
        Connection lcnnConnection   = null;
        PreparedStatement ps        = null;
        ResultSet rst               = null;
        try {
            lcnnConnection          = clsIUtil.getConnectionORCL().getConnection();
            ps = lcnnConnection.prepareStatement(qry.toString());
            ps.setInt(1, vo.getAnio());
            ps.setString(2, vo.getIdOrigen());
            ps.setString(3, vo.getCodigo());
            ps.setString(4, vo.getIdPais());
            rst                     = ps.executeQuery();
            while (rst.next()) {
                existe = true;
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            setMensaje("Ocurrio un error al consultar si un Color existe.");
        } finally {
            try {
                if(ps != null){ ps.close(); }
                if(rst != null){ rst.close(); }
                if(lcnnConnection != null){ lcnnConnection.close(); }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
        return existe;
    }
    
    public void eliminarColorInt(ClsDColoresIntVo vo){
        int result                  = 0;
        StringBuilder qry           = new StringBuilder();
        
        qry.append("DELETE FROM ICO_NIS_CCOLORES_INT_PAIS ");
        qry.append("WHERE ANIO_NID_PK = ? ");
            qry.append("AND ORIG_VID_PK = ? ");
            qry.append("AND INT_VCODIGO_PK = ? ");
            qry.append("AND PAIS_VID_PK = ? ");
        
        Connection lcnnConnection   = null;
        PreparedStatement ps        = null;
        try {
            lcnnConnection          = clsIUtil.getConnectionORCL().getConnection();
            ps = lcnnConnection.prepareStatement(qry.toString());
            ps.setInt(1, vo.getAnio());
            ps.setString(2, vo.getIdOrigen());
            ps.setString(3, vo.getCodigo());
            ps.setString(4, vo.getIdPais());
            result                  = ps.executeUpdate();
            if(result > 0){
                eliminarCColorInt(vo, lcnnConnection);
            }else{
                this.setMensaje("Ocurrio un error al aliminar el Color");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            setMensaje("Ocurrio un error al eliminar el Color.");
        } finally {
            try {
                if(ps != null){ ps.close(); }
                if(lcnnConnection != null){ lcnnConnection.close(); }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    }
    
    public void eliminarCColorInt(ClsDColoresIntVo vo, Connection lcnnConnection){
        int result                  = 0;
        StringBuilder qry           = new StringBuilder();
        
        qry.append("DELETE FROM ICO_INV_CCOLORES_INT ");
        qry.append("WHERE ANIO_NID_PK = ? ");
            qry.append("AND ORIG_VID_PK = ? ");
            qry.append("AND INT_VCODIGO_PK = ? ");
        
        PreparedStatement ps        = null;
        try {
            ps                      = lcnnConnection.prepareStatement(qry.toString());
            ps.setInt(1, vo.getAnio());
            ps.setString(2, vo.getIdOrigen());
            ps.setString(3, vo.getCodigo());
            result                  = ps.executeUpdate();
            if(result > 0){
                crearBitacora(vo, lcnnConnection);
                this.setMensaje("El Color se elimino correctamente");
            }else{
                this.setMensaje("Ocurrio un error al aliminar el Color");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            setMensaje("Ocurrio un error al eliminar el Color.");
        } finally {
            try {
                if(ps != null){ ps.close(); }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    }
    
    public void insertarColorInt(ClsDColoresIntVo vo){
        int result                  = 0;
        StringBuilder qry           = new StringBuilder();
        
        qry.append("INSERT INTO ICO_NIS_CCOLORES_INT_PAIS (ANIO_NID_PK, ORIG_VID_PK, INT_VCODIGO_PK, PAIS_VID_PK, INT_VDESCRIPCION, R_CONV_COL_INT_PAIS)");
        qry.append("VALUES (?,?,?,?,?,?)");
        
        Connection lcnnConnection   = null;
        PreparedStatement ps        = null;
        try {
            lcnnConnection          = clsIUtil.getConnectionORCL().getConnection();
            ps = lcnnConnection.prepareStatement(qry.toString());
            ps.setInt(1, vo.getAnio());
            ps.setString(2, vo.getIdOrigen());
            ps.setString(3, vo.getCodigo());
            ps.setString(4, vo.getIdPais());
            ps.setString(5, vo.getDescripcion());
            ps.setString(6, vo.getConvColor());
            result                  = ps.executeUpdate();
            if(result > 0){
                this.setMensaje("La Color se inserto correctamente");
            }else{
                this.setMensaje("Ocurrio un error al insertar el Color");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            setMensaje("Ocurrio un error al insertar el Color.");
        } finally {
            try {
                if(ps != null){ ps.close(); }
                if(lcnnConnection != null){ lcnnConnection.close(); }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    }
    
    public void actualizarColor(ClsDColoresIntVo vo){
        int result                  = 0;
        StringBuilder qry           = new StringBuilder();
        
        qry.append("UPDATE ICO_NIS_CCOLORES_INT_PAIS SET INT_VDESCRIPCION = ?, R_CONV_COL_INT_PAIS = ? ");
        qry.append("WHERE ANIO_NID_PK = ? ");
            qry.append("AND ORIG_VID_PK = ? ");
            qry.append("AND INT_VCODIGO_PK = ? ");
            qry.append("AND PAIS_VID_PK = ? ");
        
        Connection lcnnConnection   = null;
        PreparedStatement ps        = null;
        try {
            lcnnConnection          = clsIUtil.getConnectionORCL().getConnection();
            ps = lcnnConnection.prepareStatement(qry.toString());
            ps.setString(1, vo.getDescripcion());
            ps.setString(2, vo.getConvColor());
            ps.setInt(3, vo.getAnio());
            ps.setString(4, vo.getIdOrigen());
            ps.setString(5, vo.getCodigo());
            ps.setString(6, vo.getIdPais());
            result                  = ps.executeUpdate();
            if(result > 0){
                this.setMensaje("La Color se actualizo correctamente");
            }else{
                this.setMensaje("Ocurrio un error al actualizo el Color");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            setMensaje("Ocurrio un error al actualizar el Color.");
        } finally {
            try {
                if(ps != null){ ps.close(); }
                if(lcnnConnection != null){ lcnnConnection.close(); }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    }
    
    public void crearBitacora(ClsDColoresIntVo vo, Connection lcnnConnection){
        int result                  = 0;
        StringBuilder qry           = new StringBuilder();
        
        qry.append("INSERT INTO ICO_FAU_BITACORA_COLOR_INT (ANIO_NID_PK, ORIG_VID_PK, INT_VCODIGO_PK, PAIS_VID_PK, INT_VDESCRIPCION, R_CONV_COL_INT_PAIS, DFECHA_REGISTRO, VUSUARIO)");
        qry.append("VALUES (?,?,?,?,?,?,SYSDATE,?)");
        
        PreparedStatement ps        = null;
        try {
            lcnnConnection          = clsIUtil.getConnectionORCL().getConnection();
            ps                      = lcnnConnection.prepareStatement(qry.toString());
            ps.setInt(1, vo.getAnio());
            ps.setString(2, vo.getIdOrigen());
            ps.setString(3, vo.getCodigo());
            ps.setString(4, vo.getIdPais());
            ps.setString(5, vo.getDescripcion());
            ps.setString(6, vo.getConvColor());
            ps.setString(7, this.mobjSesion.UserID());
            result                  = ps.executeUpdate();
            if(result > 0){
                logger.info("La Bitacora se inserto correctamente");
            }else{
                logger.info("Ocurrio un error al insertar en la Bitacora");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            setMensaje("Ocurrio un error al insertar en la Bitacora.");
        } finally {
            try {
                if(ps != null){ ps.close(); }
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

    public List<Integer> getListAnios() {
        return listAnios;
    }

    public void setListAnios(List<Integer> listAnios) {
        this.listAnios = listAnios;
    }

    public List<String[]> getListOrigenes() {
        return listOrigenes;
    }

    public void setListOrigenes(List<String[]> listOrigenes) {
        this.listOrigenes = listOrigenes;
    }

    public List<String> getListCodigos() {
        return listCodigos;
    }

    public void setListCodigos(List<String> listCodigos) {
        this.listCodigos = listCodigos;
    }

    public List<String[]> getListPaises() {
        return listPaises;
    }

    public void setListPaises(List<String[]> listPaises) {
        this.listPaises = listPaises;
    }

    public String getJsonDescripciones() {
        return jsonDescripciones;
    }

    public void setJsonDescripciones(String jsonDescripciones) {
        this.jsonDescripciones = jsonDescripciones;
    }

    public Map<String, ClsDColoresIntVo> getMapColoresInt() {
        return mapColoresInt;
    }

    public void setMapColoresInt(Map<String, ClsDColoresIntVo> mapColoresInt) {
        this.mapColoresInt = mapColoresInt;
    }

    public ClsDColoresIntVo getColoresIntVo() {
        return coloresIntVo;
    }

    public void setColoresIntVo(ClsDColoresIntVo coloresIntVo) {
        this.coloresIntVo = coloresIntVo;
    }

    public String getCveTmp() {
        return cveTmp;
    }

    public void setCveTmp(String cveTmp) {
        this.cveTmp = cveTmp;
    }
}
