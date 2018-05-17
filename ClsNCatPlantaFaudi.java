package SS.ICO.V10.Fau;

import SS.ICO.V10.Common.clsISesion;

import SS.ICO.V10.Common.clsIUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.log4j.Logger;

public class ClsNCatPlantaFaudi {
    
    private clsISesion mobjSesion = null;
    
    private String planta;
    private String equivalenciaSap;
    private String mensaje;
    
    private boolean consultaPlanta = false;
    
    private List<ClsDPlantasVo> plantas;
    
    Logger logger = Logger.getLogger(ClsNCatPlantaFaudi.class);
    
    public ClsNCatPlantaFaudi() {
        super();
    }
    
    /**Inicializa los campos.
     * @param lobjSesion, sesion.
     * */
    public void Inicializa(clsISesion lobjSesion){
        this.mobjSesion = lobjSesion;
        this.consultaPlanta = false;
        this.planta = "";
        this.equivalenciaSap = "";
        this.plantas = new ArrayList<ClsDPlantasVo>();
        this.obtenerPlantas();
    }
    
    /**
     * Obtiene las plantas de la tabla ICO_INV_CPLANTAS
     * para mostrar en pantalla
     */
    public void obtenerPlantas(){
        this.plantas.clear();
        StringBuilder qry = new StringBuilder();
        
        qry.append("SELECT PLTA_VID_PK, PLTA_VDESCRIPCION ");
        qry.append("FROM ICO_INV_CPLANTAS ");
        qry.append("ORDER BY PLTA_VDESCRIPCION ");
        
        Connection lcnnConnection = null;
        Statement stm = null;
        ResultSet rst = null;
        
        try {
            lcnnConnection = clsIUtil.getConnectionORCL().getConnection();
            stm = lcnnConnection.createStatement();
            rst = stm.executeQuery(qry.toString());
            
            while (rst.next()) {
                ClsDPlantasVo vo = new ClsDPlantasVo();
                vo.setId(rst.getString(1));
                vo.setDescripcion(rst.getString(2));
                this.plantas.add(vo);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            setMensaje("Ocurrio un error al consultar las plantas.");
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
    
    /**
     * Se obtiene la PLTA_EQUIVALENCIAS_SAP para agregar, 
     * modificar o eliminar
     * @param planta - Id de la planta a consultar
     */
    public void obtenerEquivalenciaSap(String planta){
        
        StringBuilder qry = new StringBuilder();
        
        qry.append("SELECT PLTA_VID_PK, PLTA_VDESCRIPCION, PLTA_EQUIVALENCIAS_SAP ");
        qry.append("FROM ICO_INV_CPLANTAS ");
        qry.append("WHERE PLTA_VID_PK = ? ");
        
        Connection lcnnConnection = null;
        PreparedStatement ps = null;
        ResultSet rst = null;
        
        try {
            lcnnConnection = clsIUtil.getConnectionORCL().getConnection();
            ps = lcnnConnection.prepareStatement(qry.toString());
            ps.setString(1, planta);
            rst = ps.executeQuery();
            
            while (rst.next()) {
                this.setPlanta(rst.getString(1) + " - " + rst.getString(2));
                this.setEquivalenciaSap(rst.getString(3) != null ? rst.getString(3) : "");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            setMensaje("Ocurrio un error al obtener la Equivalencia SAP de la planta " + planta);
        } finally {
            try {
                if(ps != null){ ps.close(); }
                if(rst != null){ rst.close(); }
                if(lcnnConnection != null){ lcnnConnection.close(); }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    }
    
    /**
     *
     * @param planta - Id de la planta a modificar
     * @param equivalenciaSap - 
     */
    public void actualizaEquivalenciaSap(String planta, String equivalenciaSap){
        
        StringBuilder qry = new StringBuilder();
        
        qry.append("UPDATE ICO_INV_CPLANTAS ");
        qry.append("SET PLTA_EQUIVALENCIAS_SAP = ? ");
        qry.append("WHERE PLTA_VID_PK = ? ");
        
        Connection lcnnConnection = null;
        PreparedStatement ps = null;
        int result = 0;
        
        try {
            lcnnConnection = clsIUtil.getConnectionORCL().getConnection();
            ps = lcnnConnection.prepareStatement(qry.toString());
            ps.setString(1, equivalenciaSap);
            ps.setString(2, planta);
            result = ps.executeUpdate();
            
            if(result > 0){
                this.setMensaje("La Equivalencia SAP se modifico correctamente");
            }else{
                this.setMensaje("Ocurrio un error al modificar la Equivalencia SAP");
            }
            
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            setMensaje("Ocurrio un error al Actualizar la Equivalencia SAP");
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

    public List<ClsDPlantasVo> getPlantas() {
        return plantas;
    }

    public void setPlantas(List<ClsDPlantasVo> plantas) {
        this.plantas = plantas;
    }

    public String getPlanta() {
        return planta;
    }

    public void setPlanta(String planta) {
        this.planta = planta;
    }

    public String getEquivalenciaSap() {
        return equivalenciaSap;
    }

    public void setEquivalenciaSap(String equivalenciaSap) {
        this.equivalenciaSap = equivalenciaSap;
    }

    public boolean isConsultaPlanta() {
        return consultaPlanta;
    }

    public void setConsultaPlanta(boolean consultaPlanta) {
        this.consultaPlanta = consultaPlanta;
    }
    
}