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

public class ClsNCatColorExtFaudi {
    
    private String mensaje;
    private String jsonDescripciones;
    private String jsonCodigos;
    private String cveTmp;
    
    private clsISesion mobjSesion = null;
    private ClsDColoresExtVo coloresExtVo;
    
    private List<Integer> listAnios;
    private List<String[]> listOrigenes;
    private List<String[]> listSectores;
    private Map<String, ClsDColoresExtVo> mapColoresExt;
    
    Logger logger = Logger.getLogger(ClsNCatColorExtFaudi.class);
    
    public ClsNCatColorExtFaudi() {
        super();
    }
    
    /**Inicializa los campos.
     * @param lobjSesion, sesion.
     * */
    public void Inicializa(clsISesion lobjSesion){
        this.setJsonDescripciones("");
        this.setJsonCodigos("");
        this.setCveTmp("");
        this.setColoresExtVo(new ClsDColoresExtVo());
        this.setMobjSesion(lobjSesion);
        this.setListAnios(new ArrayList<Integer>());
        this.setListOrigenes(new ArrayList<String[]>());
        this.setListSectores(new ArrayList<String[]>());
        this.setMapColoresExt(new LinkedHashMap<String, ClsDColoresExtVo>());
        obtenerAnios();
        obtenerOrigenes();
        obtenerCodigos();
        obtenerSectores();
        obtenerDescripciones();
    }
    
    public void obtenerAnios(){
        this.getListAnios().clear();
        String qry                  = "SELECT * FROM ICO_INV_CANIOS_MODELO ORDER BY ANIO_NID_PK DESC";
        
        Connection lcnnConnection   = null;
        Statement stm               = null;
        ResultSet rst               = null;
        try {
            lcnnConnection          = clsIUtil.getConnectionORCL().getConnection();
            stm                     = lcnnConnection.createStatement();
            rst                     = stm.executeQuery(qry);
            while (rst.next()) {
                this.getListAnios().add(rst.getInt(1));
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
        this.getListOrigenes().clear();
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
                this.getListOrigenes().add(origenes);
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
        String qry                  = "SELECT DISTINCT EXT_VCODIGO_PK FROM ICO_INV_CCOLORES_EXT ORDER BY EXT_VCODIGO_PK";
        
        List<String> listCodigos    = new ArrayList<String>();
        
        Connection lcnnConnection   = null;
        Statement stm               = null;
        ResultSet rst               = null;
        try {
            lcnnConnection          = clsIUtil.getConnectionORCL().getConnection();
            stm                     = lcnnConnection.createStatement();
            rst                     = stm.executeQuery(qry);
            while (rst.next()) {
                listCodigos.add(rst.getString(1));
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
        Gson gson = new Gson();
        this.setJsonCodigos(gson.toJson(listCodigos));
    }
    
    public void obtenerSectores(){
        this.getListSectores().clear();
        String qry                  = "SELECT SECT_NID_PK, SECT_VDESCRIPCION FROM ICO_INV_CSECTORES";
        
        Connection lcnnConnection   = null;
        Statement stm               = null;
        ResultSet rst               = null;
        try {
            lcnnConnection          = clsIUtil.getConnectionORCL().getConnection();
            stm                     = lcnnConnection.createStatement();
            rst                     = stm.executeQuery(qry);
            while (rst.next()) {
                String[] paises     = { rst.getString(1), rst.getString(2) };
                this.getListSectores().add(paises);
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

        String qry                      = "SELECT DISTINCT EXT_VDESCRIPCION FROM ICO_INV_CCOLORES_EXT ORDER BY EXT_VDESCRIPCION";

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
        this.setJsonDescripciones(gson.toJson(listDescripciones));
    }
    
    public void obtenerListaColoresExt(ClsDColoresExtVo vo){
        this.mapColoresExt.clear();
        
        ClsDColoresExtVo data;
        StringBuilder qry           = new StringBuilder();
        
        qry.append("SELECT C.ANIO_NID_PK, C.ORIG_VID_PK, O.ORIG_VDESCRIPCION, C.SECT_NID, ");
            qry.append("S.SECT_VDESCRIPCION, C.EXT_VCODIGO_PK, C.EXT_VDESCRIPCION, C.R_CONV_COL_EXT ");
        qry.append("FROM ICO_INV_CCOLORES_EXT C, ");
            qry.append("ICO_INV_CORIGENES O, ICO_INV_CSECTORES S ");
        qry.append("WHERE C.ORIG_VID_PK = O.ORIG_VID_PK ");
            qry.append("AND C.SECT_NID = S.SECT_NID_PK ");
        
        if(vo.getAnio() > 0){
            qry.append("AND C.ANIO_NID_PK = " + vo.getAnio() + " ");
        }
        if(vo.getIdOrigen() != null && !vo.getIdOrigen().equals("")){
            qry.append("AND C.ORIG_VID_PK = '" + vo.getIdOrigen() + "' ");
        }
        if(vo.getCodigo() != null && !vo.getCodigo().equals("")){
            qry.append("AND C.EXT_VCODIGO_PK = '" + vo.getCodigo() + "' ");
        }
        if(vo.getIdSector() > 0){
            qry.append("AND C.SECT_NID = '" + vo.getIdSector() + "' ");
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
                data = new ClsDColoresExtVo();
                data.setAnio(rst.getInt("ANIO_NID_PK"));
                data.setIdOrigen(rst.getString("ORIG_VID_PK"));
                data.setOrigenDesc(rst.getString("ORIG_VDESCRIPCION"));
                data.setCodigo(rst.getString("EXT_VCODIGO_PK"));
                data.setIdSector(rst.getInt("SECT_NID"));
                data.setSectorDesc(rst.getString("SECT_VDESCRIPCION"));
                data.setDescripcion(rst.getString("EXT_VDESCRIPCION"));
                data.setConvColor(rst.getString("R_CONV_COL_EXT") == null ? "" : rst.getString("R_CONV_COL_EXT"));
                data.calcularClave();
                this.mapColoresExt.put(data.getClave(), data);
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
        if(this.mapColoresExt.size() == 0){ setMensaje("No se encontraron registros para la búsqueda."); }
    }
    
    public void insertarColorExt(ClsDColoresExtVo vo){
        int result                  = 0;
        StringBuilder qry           = new StringBuilder();
        
        qry.append("INSERT INTO ICO_INV_CCOLORES_EXT (ANIO_NID_PK, ORIG_VID_PK, EXT_VCODIGO_PK, EXT_VDESCRIPCION, R_CONV_COL_EXT, SECT_NID) ");
        qry.append("VALUES (?,?,?,?,?,?)");
        
        Connection lcnnConnection   = null;
        PreparedStatement ps        = null;
        try {
            lcnnConnection          = clsIUtil.getConnectionORCL().getConnection();
            ps = lcnnConnection.prepareStatement(qry.toString());
            ps.setInt(1, vo.getAnio());
            ps.setString(2, vo.getIdOrigen());
            ps.setString(3, vo.getCodigo());
            ps.setString(4, vo.getDescripcion());
            ps.setString(5, vo.getConvColor());
            ps.setInt(6, vo.getIdSector());
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
    
    public void actualizarColor(ClsDColoresExtVo vo){
        int result                  = 0;
        StringBuilder qry           = new StringBuilder();
        
        qry.append("UPDATE ICO_INV_CCOLORES_EXT SET EXT_VDESCRIPCION = ?, R_CONV_COL_EXT = ? ");
        qry.append("WHERE ANIO_NID_PK = ? ");
            qry.append("AND ORIG_VID_PK = ? ");
            qry.append("AND EXT_VCODIGO_PK = ? ");
            qry.append("AND SECT_NID = ? ");
        
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
            ps.setInt(6, vo.getIdSector());
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
    
    public void eliminarColorExt(ClsDColoresExtVo vo){
        int result                  = 0;
        StringBuilder qry           = new StringBuilder();
        
        qry.append("DELETE FROM ICO_INV_CCOLORES_EXT ");
        qry.append("WHERE ANIO_NID_PK = ? ");
            qry.append("AND ORIG_VID_PK = ? ");
            qry.append("AND EXT_VCODIGO_PK = ? ");
            qry.append("AND SECT_NID = ? ");
        
        Connection lcnnConnection   = null;
        PreparedStatement ps        = null;
        try {
            lcnnConnection          = clsIUtil.getConnectionORCL().getConnection();
            ps = lcnnConnection.prepareStatement(qry.toString());
            ps.setInt(1, vo.getAnio());
            ps.setString(2, vo.getIdOrigen());
            ps.setString(3, vo.getCodigo());
            ps.setInt(4, vo.getIdSector());
            result                  = ps.executeUpdate();
            if(result > 0){
                this.setMensaje("El Color se elimino correctamente");
                crearBitacora(vo, lcnnConnection);
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
    
    public void crearBitacora(ClsDColoresExtVo vo, Connection lcnnConnection){
        int result                  = 0;
        StringBuilder qry           = new StringBuilder();
        
        qry.append("INSERT INTO ICO_FAU_BITACORA_COLOR_EXT (ANIO_NID_PK, ORIG_VID_PK, EXT_VCODIGO_PK, EXT_VDESCRIPCION, R_CONV_COL_EXT, SECT_NID, DFECHA_REGISTRO, VUSUARIO)");
        qry.append("VALUES (?,?,?,?,?,?,SYSDATE,?)");
        
        PreparedStatement ps        = null;
        try {
            lcnnConnection          = clsIUtil.getConnectionORCL().getConnection();
            ps                      = lcnnConnection.prepareStatement(qry.toString());
            ps.setInt(1, vo.getAnio());
            ps.setString(2, vo.getIdOrigen());
            ps.setString(3, vo.getCodigo());
            ps.setString(4, vo.getDescripcion());
            ps.setString(5, vo.getConvColor());
            ps.setInt(6, vo.getIdSector());
            ps.setString(7, this.mobjSesion.UserID());
            result                  = ps.executeUpdate();
            if(result > 0){
                logger.info("La Bitacora se inserto correctamente");
            }else{
                logger.info("Ocurrio un error al insertar en la Bitacora");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            setMensaje("Ocurrio un error al insertar en Bitacora.");
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

    public String getJsonDescripciones() {
        return jsonDescripciones;
    }

    public void setJsonDescripciones(String jsonDescripciones) {
        this.jsonDescripciones = jsonDescripciones;
    }

    public String getJsonCodigos() {
        return jsonCodigos;
    }

    public void setJsonCodigos(String jsonCodigos) {
        this.jsonCodigos = jsonCodigos;
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

    public ClsDColoresExtVo getColoresExtVo() {
        return coloresExtVo;
    }

    public void setColoresExtVo(ClsDColoresExtVo coloresExtVo) {
        this.coloresExtVo = coloresExtVo;
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

    public List<String[]> getListSectores() {
        return listSectores;
    }

    public void setListSectores(List<String[]> listSectores) {
        this.listSectores = listSectores;
    }

    public Map<String, ClsDColoresExtVo> getMapColoresExt() {
        return mapColoresExt;
    }

    public void setMapColoresExt(Map<String, ClsDColoresExtVo> mapColoresExt) {
        this.mapColoresExt = mapColoresExt;
    }
}
