package SS.ICO.V10.Fau;

import SS.ICO.V10.Common.clsISesion;
import SS.ICO.V10.Common.clsIUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class ClsNCatParametros {
    public ClsNCatParametros() {
        super();
    }
    private clsISesion mobjSesion=null;
   
    private String mensaje;
  
    private ClsDParametrosVo voSelec;
    private List<ClsDParametrosVo> lstParametros;
    
   
    Logger log = Logger.getLogger(this.getClass().getName());
    
    
    /**Inicializa los campos sesion, y fecha.
     * @param lobjSesion, sesion.
     * */
    public void Inicializa(clsISesion lobjSesion){
        this.setMobjSesion(lobjSesion);

        this.setVoSelec(new ClsDParametrosVo());
        this.setLstParametros(new ArrayList<ClsDParametrosVo>());
      
    }
    
    public void parDisponibles ( )  {

        this.getLstParametros().clear();
        StringBuffer sql = new StringBuffer();
       
        /* Se construye la consulta */
              sql.append("SELECT PAR_ID, PAR_CAMPO, PAR_DESCRIPCION, PAR_FECHA_MOD, PAR_USUARIOM " ); 
              sql.append("  FROM ICO_FAU_PARAMETROS " );
              sql.append("   order by 1 " ); 
             
     
     
     log.info("materiales activos "+sql);
        Connection lcnnConnection = null;
        Statement stm = null;
        ResultSet rst = null;
        ResultSet rstInf = null;
        int cont=0;
        try {
            lcnnConnection = clsIUtil.getConnectionORCL().getConnection();
            stm = lcnnConnection.createStatement();
            rst = stm.executeQuery(sql.toString());
            
            /* Se iteran los resultados y se agregan al objeto de salida. */
            while (rst.next()) {
                ClsDParametrosVo vo=new ClsDParametrosVo();
                vo.setId(rst.getInt(1));
                vo.setCampo(rst.getString(2));
                vo.setDescripcion(rst.getString(3));
                this.getLstParametros().add( vo);
              
            }
           
          
        } catch (Exception e) {
           log.error( "Error al consultar los parametros disponibles:"+e.getMessage(),e);
            
            setMensaje("Error al consultar los materiales disponibles. Consultar Administrador");
                   
        } finally {
            try {
                stm.close();
                rst.close();
                
                if(lcnnConnection != null){
                    lcnnConnection.close();
                }
            } catch (Exception e) {
                log.error("Error al cerrar conexion"+ e.getMessage(),e);
               
            }
        }

        
        
    }
    
    public void actualizaPara( ClsDParametrosVo vo) {
        Connection cn = null;
        PreparedStatement ps = null;
        this.setMensaje("");
        int delete = -1;
        StringBuffer sql = new StringBuffer();
        /* Se consulta el folio a asignar */
        
        /* Se construye el delete */
        sql.append(" UPDATE ICO_FAU_PARAMETROS SET  PAR_CAMPO = ?, PAR_DESCRIPCION = ?, PAR_FECHA_MOD = sysdate, PAR_USUARIOM = ?  " );
        sql.append(" WHERE PAR_ID = ?" );
   
       
           int contador=0;   
        try {
                   cn = clsIUtil.getConnectionORCL().getConnection();
                   ps  = cn.prepareStatement(sql.toString());
                   
                   
                    ps.setString(1, vo.getCampo());
                    ps.setString(2, vo.getDescripcion());
                    ps.setString(3, this.mobjSesion.UserID());
                    ps.setInt(4,vo.getId());
                   
                
                  /* Se ejecuta la eliminación */
                
                    delete = ps.executeUpdate();
                if (delete == 1) {

                        this.setMensaje(this.getMensaje() + "Se actualizo el parametro  correctamente  ");
                    
                }else{
                        this.setMensaje(this.getMensaje() + "No actualizo el parametro informar al administrador.   ");
            }
            
            
        } catch (Exception e) {
           log.error("ClsNCatParametros " + e.getMessage(),e);
         
            this.setMensaje(" Error al actualizar el Parametro  <br>");
            
           
        } 
        finally {
            try {
                ps.close();
                cn.close();
            } catch (Exception e) {
                log.error("ClsNCatParametros " + e.getMessage(),e);
               
            }
        }
        
       
              
    }
    
   
   
    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }


    public clsISesion getMobjSesion() {
        return mobjSesion;
    }

    public void setMobjSesion(clsISesion mobjSesion) {
        this.mobjSesion = mobjSesion;
    }

    public ClsDParametrosVo getVoSelec() {
        return voSelec;
    }

    public void setVoSelec(ClsDParametrosVo voSelec) {
        this.voSelec = voSelec;
    }

    public List<ClsDParametrosVo> getLstParametros() {
        return lstParametros;
    }

    public void setLstParametros(List<ClsDParametrosVo> lstParametros) {
        this.lstParametros = lstParametros;
    }
}

