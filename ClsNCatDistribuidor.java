package SS.ICO.V10.Fau;


import SS.ICO.V10.Common.clsIParametro;
import SS.ICO.V10.Common.clsISesion;
import SS.ICO.V10.Common.clsIUtil;
import SS.ICO.V10.Uti.Layout.clsILog;

import java.io.File;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

public class ClsNCatDistribuidor {
    public ClsNCatDistribuidor() {
        super();
    }
    private clsISesion mobjSesion=null;
    private Map<String, ClsDDistribuidorVo> mapDistribuidoresDis;
    private String mensaje;
    private String claveDis;
    private ClsDDistribuidorVo voSelec;
    private List<ClsDEdosVo> edos;
    private String rutaFisica;
    private String nombreReporte;
    private static final String HEAD_REPORTE =
             "CLAVE, RAZON SOCIAL, R.F.C., ESTADO, ZONA,DIAS TRASLADO,DIRECCION,  POBLACION, CODIGO POSTAL, USUARIO, FECHA_MODOFICA, ESTATUS ";

    Logger log = Logger.getLogger(this.getClass().getName());
    
    
    /**Inicializa los campos sesion, y fecha.
     * @param lobjSesion, sesion.
     * */
    public void Inicializa(clsISesion lobjSesion){
        this.mobjSesion = lobjSesion;
        this.mapDistribuidoresDis = new LinkedHashMap<String, ClsDDistribuidorVo>();
        this.claveDis="";
        this.voSelec=new ClsDDistribuidorVo();
        this.edos=new ArrayList<ClsDEdosVo>();
        this.rutaFisica="";
        this.nombreReporte="";
    }
    
    public void distDisponibles ( )  {

        this.getMapDistribuidoresDis().clear();
        StringBuffer sql = new StringBuffer();
      
        /* Se construye la consulta */
              sql.append("SELECT DIST_VID_PK, DIST_VRAZON_SOCIAL, DIST_VRFC, DIST_VABREVIACION, DIST_VDIRECCION,DIST_VPOBLACION,") ;
              sql.append("  DIST_NCODIGO_POSTAL, DIST_EDO, DIST_ZONA, DIST_SURTIR, DIST_DIAS_TRAS, DIST_AUTORIZACION, DIST_TRASLADO, " );
              sql.append("  DIST_DESCUENTO,  " ); 
              sql.append("  DIST_FINANCIERA, DIST_MAT_PAGO, DIST_NO_CUENTA ,USUARIO_MPDIFICA,FECHA_MODOFICA,ESTATUS ,EDO_VDESCRIPCION " ); 
              sql.append("   FROM ICO_FAU_CDISTRIBUIDORES  ,ICO_INV_CESTADOS where EDO_NID_PK= DIST_EDO " );
              sql.append(" order by DIST_VID_PK ");
     
    log.info("distribuidores activos "+sql);
        Connection lcnnConnection = null;
        Statement stm = null;
        ResultSet rst = null;
        int cont=1;
        try {
            lcnnConnection = clsIUtil.getConnectionORCL().getConnection();
            stm = lcnnConnection.createStatement();
            rst = stm.executeQuery(sql.toString());
            
            /* Se iteran los resultados y se agregan al objeto de salida. */
            while (rst.next()) {
                ClsDDistribuidorVo vo=new ClsDDistribuidorVo();
                vo.setNumero(cont++);
                vo.setDist_vid_pk(rst.getString(1));
                vo.setDist_vrazon_social(rst.getString(2));
                vo.setDist_vrfc(rst.getString(3));
                vo.setDist_vabreviacion(rst.getString(4));
                vo.setDist_vdireccion(rst.getString(5));
             //   vo.setDist_vcolonia(rst.getString(6));
                vo.setDist_vpoblacion(rst.getString(6));
                vo.setDist_ncodigo_postal(rst.getString(7));
                vo.setDist_edo(rst.getInt(8));
                vo.setDist_zona(rst.getString(9));
                vo.setDist_surtir(rst.getString(10));
                vo.setDist_dias_tras(rst.getInt(11));
                vo.setDist_descuento(rst.getString(12));
                vo.setDist_autorizacion(rst.getString(13));
                vo.setDist_traslado(rst.getString(14));
                vo.setDist_financiera(rst.getString(15));
                vo.setDist_mat_pago(rst.getString(16));
                vo.setDist_no_cuenta(rst.getString(17));
                vo.setUsuarioModifica(rst.getString(18));
                vo.setFechaModifica(rst.getString(19));
               
                vo.setEstatus(rst.getInt(20));
                vo.setDesEstado(rst.getString(21));
              
                this.mapDistribuidoresDis.put(vo.getDist_vid_pk(), vo);
              
            }
           // mensaje = "Los Distribuidores están disponibles para seleccionarse.";
        } catch (Exception e) {
            
            log.error("Catalogo distribuidores Catch 001: " + e.getMessage(),e);
            log.error(": Error al consultar los distribuidores disponibles.");
            setMensaje("Error al consultar los distribuidores disponibles. Consultar Administrador");
                   
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
    
    
    public void insertaDistribuidor ( ClsDDistribuidorVo vo) {
        Connection cn = null;
        PreparedStatement ps = null;
        this.mensaje="";
        int delete = -1;
        StringBuffer sql = new StringBuffer();
        /* Se consulta el folio a asignar */
        
        /* Se construye el delete */
        sql.append(" INSERT INTO ICO_FAU_CDISTRIBUIDORES (DIST_VID_PK, DIST_VRAZON_SOCIAL, DIST_VRFC, DIST_VABREVIACION, DIST_VDIRECCION, " );
        sql.append("  DIST_VPOBLACION, DIST_NCODIGO_POSTAL, DIST_EDO, DIST_ZONA, DIST_SURTIR, DIST_DIAS_TRAS, DIST_AUTORIZACION," );
        sql.append("DIST_TRASLADO, DIST_DESCUENTO, DIST_FINANCIERA, DIST_MAT_PAGO, " );
        sql.append("DIST_NO_CUENTA, USUARIO_MPDIFICA, FECHA_MODOFICA, ESTATUS) " );
        sql.append("VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, SYSDATE, 1)");
           int contador=0;   
        try {
            if(!mapDistribuidoresDis.containsKey(vo.getDist_vid_pk())){
                        cn = clsIUtil.getConnectionORCL().getConnection();
               
                   ps = cn.prepareStatement(sql.toString());
                   
                    ps.setString(1,  vo.getDist_vid_pk());
                    ps.setString(2, vo.getDist_vrazon_social());
                    ps.setString(3, vo.getDist_vrfc());
                    ps.setString(4, vo.getDist_vabreviacion());
                    ps.setString(5,vo.getDist_vdireccion());
                  //  ps.setString(6,vo.getDist_vcolonia());
                    ps.setString(6,vo.getDist_vpoblacion());
                    ps.setString(7,vo.getDist_ncodigo_postal());
                    ps.setInt(8, vo.getDist_edo());
                    ps.setString(9,vo.getDist_zona());
                    ps.setString(10,vo.getDist_surtir());
                    ps.setInt(11,vo.getDist_dias_tras());
                    ps.setString(12,vo.getDist_autorizacion());
                    ps.setString(13,vo.getDist_traslado());
                    ps.setString(14,vo.getDist_descuento());
                    ps.setString(15,vo.getDist_financiera());
                    ps.setString(16,vo.getDist_mat_pago());
                    ps.setString(17,vo.getDist_no_cuenta());
                    ps.setString(18,this.mobjSesion.UserID());
                    
                
                  /* Se ejecuta la eliminación */
                try{
                    delete = ps.executeUpdate();
                if (delete == 1) {
                    this.mensaje= this.mensaje+"Se inserto el distribuidor:"+vo.getDist_vid_pk()+"  correctamente  ";
                    
                   
                }else{
                    this.mensaje= this.mensaje+"No inserto el distribuidor:"+vo.getDist_vid_pk()+"  correctamente  ";
                    
                }
            
                } catch (SQLException e) {
                    log.error("ClsNCatDistribuidor " + e.getMessage(),e);
                    log.error(" Error al insertar un distribuidor.");
                    log.error(" Error "+e.getErrorCode());
                   if( e.getErrorCode()==1){
                     this.mensaje= this.mensaje+"El registro: "+ vo.getDist_vid_pk()+" ya Existe <br>";
                    
                   }else{
                       this.mensaje= this.mensaje+"El registro: "+ vo.getDist_vid_pk()+" con error <br>";
                       
                   }
            
                }
            
            
           
          
        
           
                
            }else{
                this.mensaje= this.mensaje+"El registro: "+ vo.getDist_vid_pk()+" ya Existe <br>";
                
            }
        } catch (Exception e) {
            log.error("ClsNCatDistribuidor " + e.getMessage(),e);
            log.error(" Error al insertar un distribuidor.");
          
            try {
                cn.rollback();
            } catch (SQLException f) {
                log.error("ClsNCatDistribuidor " + e.getMessage(),e);
            }
        } 
        finally {
            try {
                ps.close();
                cn.close();
            } catch (Exception e) {
                log.error("ClsNCatDistribuidor  " + e.getMessage(),e);
                log.error(" Error al insertar distribuidor.");
            }
        }
        
       
              
    }
    
    public void actualizaDistribuidor ( ClsDDistribuidorVo vo) {
        Connection cn = null;
        PreparedStatement ps = null;
        this.mensaje="";
        int delete = -1;
        StringBuffer sql = new StringBuffer();
        /* Se consulta el folio a asignar */
        
        /* Se construye el delete */
        sql.append(" UPDATE ICO_FAU_CDISTRIBUIDORES SET  DIST_VRAZON_SOCIAL = ?, DIST_VRFC = ?, DIST_VABREVIACION = ?,  " );
        sql.append(" DIST_VDIRECCION = ?,  DIST_VPOBLACION = ?, DIST_NCODIGO_POSTAL = ?, DIST_EDO = ?, DIST_ZONA = ?," );
        sql.append(" DIST_SURTIR = ?, DIST_DIAS_TRAS = ?, DIST_AUTORIZACION = ?, DIST_TRASLADO = ?, DIST_DESCUENTO = ?,  " );
        sql.append(" DIST_FINANCIERA = ?, DIST_MAT_PAGO = ?, DIST_NO_CUENTA = ?, " );
        sql.append(" USUARIO_MPDIFICA = ?, FECHA_MODOFICA = SYSDATE, ESTATUS = ?");
        sql.append(" WHERE DIST_VID_PK = ?");
           int contador=0;   
        try {
            if(mapDistribuidoresDis.containsKey(vo.getDist_vid_pk().trim())){
                        cn = clsIUtil.getConnectionORCL().getConnection();
               
                   ps = cn.prepareStatement(sql.toString());
                   
                   
                    ps.setString(1, vo.getDist_vrazon_social());
                    ps.setString(2, vo.getDist_vrfc());
                    ps.setString(3, vo.getDist_vabreviacion());
                    ps.setString(4,vo.getDist_vdireccion());
                //    ps.setString(5,vo.getDist_vcolonia());
                    ps.setString(5,vo.getDist_vpoblacion());
                    ps.setString(6,vo.getDist_ncodigo_postal());
                    ps.setInt(7, vo.getDist_edo());
                    ps.setString(8,vo.getDist_zona());
                    ps.setString(9,vo.getDist_surtir());
                    ps.setInt(10,vo.getDist_dias_tras());
                    ps.setString(11,vo.getDist_autorizacion());
                    ps.setString(12,vo.getDist_traslado());
                    ps.setString(13,vo.getDist_descuento());
                    ps.setString(14,vo.getDist_financiera());
                    ps.setString(15,vo.getDist_mat_pago());
                    ps.setString(16,vo.getDist_no_cuenta());
                    ps.setString(17,this.mobjSesion.UserID());
                    ps.setInt(18,vo.getEstatus());
                    ps.setString(19,  vo.getDist_vid_pk());
                
                  /* Se ejecuta la eliminación */
                try{
                    delete = ps.executeUpdate();
                if (delete == 1) {
                    
                    this.mensaje= this.mensaje+"Se actualizo el distribuidor:"+vo.getDist_vid_pk()+"  correctamente  ";
                    
                }else{
                    this.mensaje= this.mensaje+"No actualizo el distribuidor:"+vo.getDist_vid_pk()+" informal al administrador.   ";
                    
                }
            
                } catch (SQLException e) {
                    log.error("ClsNCatDistribuidor " + e.getMessage(),e);
                    log.error(" Error al actualizar un distribuidor.");
                 
                   if( e.getErrorCode()==1){
                     this.mensaje= this.mensaje+"El registro: "+ vo.getDist_vid_pk()+" ya Existe <br>";
                    
                   }else{
                       this.mensaje= this.mensaje+"El registro: "+ vo.getDist_vid_pk()+" con error <br>";
                       
                   }
            
                }
            
           
            }else{
                log.info(" No existe el distribuidor no se puiede actualizar .");
            }
        } catch (Exception e) {
            log.error("ClsNCatDistribuidor " + e.getMessage(),e);
            log.error(" Error al actualizar un distribuidor.");
            this.mensaje= " Error al actualizar el distribuidor.: "+ vo.getDist_vid_pk()+"  <br>";
            
           
        } 
        finally {
            try {
                ps.close();
                cn.close();
            } catch (Exception e) {
                log.error("ClsNCatDistribuidor  " + e.getMessage(),e);
                log.error(" Error al actualizar distribuidor.");
            }
        }
        
       
              
    }
    
    public void eliminaDistribuidor (String clave) {
            
        
        
        Connection cn = null;
        PreparedStatement ps = null;
        int delete = -1;
        String sql;
        
        /* Se construye el delete */
        sql = "UPDATE ICO_FAU_CDISTRIBUIDORES SET    USUARIO_MPDIFICA = ?, FECHA_MODOFICA = SYSDATE, ESTATUS = 0   WHERE DIST_VID_PK =? ";
           int contador=0;   
        try {
            
            cn = clsIUtil.getConnectionORCL().getConnection();;
            ps = cn.prepareStatement(sql);
            
        
                     
                ps.setString(1, this.mobjSesion.UserID());
                    ps.setString(2, clave);
                   
                    
                   
                    /* Se ejecuta la eliminación */
                    delete = ps.executeUpdate();
                if (delete == 1) {
                    contador++; 
                   
                }
            
          
        
            if (delete > 0) {
                mensaje="";

                setMensaje("Se dio de baja  correctamente el distribuidor:"+clave+"  ");
                
            }else{
                mensaje="";

                setMensaje("Error no se dio de baja el distribuidor   favor de comunicarse con el administrador");
            }
        } catch (Exception e) {
            log.error("ClsNCatDistribuidor " + e.getMessage(),e);
            log.error(" Error al borrar un distribuidor.");
        } finally {
            try {
                ps.close();
                cn.close();
            } catch (Exception e) {
                log.error("ClsNCatDistribuidor  " + e.getMessage(),e);
                log.error(" Error al borrar distribuidor.");
            }
        }
        
       
              
    }
    
    
    
    public List<ClsDEdosVo> estadosdisponibles() {
        String query="SELECT EDO_NID_PK,EDO_VDESCRIPCION FROM ICO_INV_CESTADOS ORDER by  EDO_VDESCRIPCION ASC";
        List<ClsDEdosVo> vec=new ArrayList<ClsDEdosVo>();
        
            Connection lcnnConnection = null;
            Statement stm=null;
            ResultSet rst=null;
            try {
                lcnnConnection = clsIUtil.getConnectionORCL().getConnection();
                stm = lcnnConnection.createStatement();
                rst = stm.executeQuery(query);
                
                while (rst.next()) {
                   ClsDEdosVo lobjConsulta = new ClsDEdosVo();
                    lobjConsulta.setId(rst.getInt(1));
                    lobjConsulta.setDescripcion(rst.getString(2)) ;
                    vec.add(lobjConsulta);                     
                   
                }

               
                
                } catch (Exception e) {
                    log.error("Catalogo distribuidores Catch 002: " + e.getMessage(),e);
                    log.error(": Error al consultar los distribuidores disponibles.");
                    setMensaje("Error al consultar los estados disponibles. Consultar Administrador");
                           
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
          
        
        return vec;
    }
    
    public boolean generaReporte(){
        //Se obtiene una coneccion temporal para extraer parametros de BD
        clsIParametro lobjParametros=new clsIParametro();
        Connection conn = null;      
        String nombre = "";
        boolean salida = false;        
        try{
            conn = clsIUtil.getConnectionORCL().getConnection();
        }
        catch(Exception e){
            log.error("Error al abrir conexion"+ e.getMessage(),e);       
        }
        
        lobjParametros.Recupera(53003,conn);//Recupera la ruta fisica donde se creara el archivo
        setRutaFisica(lobjParametros.VVALOR.trim());
        lobjParametros.Recupera(53004,conn);//Recupera la ruta fisica donde se creara el archivo
        setNombreReporte(lobjParametros.VVALOR.trim());
              
        //se cierra la coneccion de los parametros
        try{
            conn.close();
        }
        catch(Exception e){
            log.error("Error al cerrar conexion"+ e.getMessage(),e); 
        }
       
        try{
            clsILog mobjLog = new clsILog(this.nombreReporte, this.rutaFisica, false);                                    
            mobjLog.agregarMensaje("");
            mobjLog.agregarMensaje("");
            mobjLog.agregarMensaje("");
            mobjLog.agregarMensaje("");
           
            mobjLog.agregarMensaje("NISSAN MEXICANA S.A. DE C.V.");
            mobjLog.agregarMensaje("SISTEMA DE VENTA DE UNIDADES");
            mobjLog.agregarMensaje("CATALOGOS DE DISTRIBUIDORES");
           
                mobjLog.agregarMensaje("LISTADO DE LA RED DE DISTRIBUIDORES");
            mobjLog.agregarMensaje("");
            mobjLog.agregarMensaje("");
                mobjLog.agregarMensaje(HEAD_REPORTE); 
         //   "DIST_VID_PK, DIST_VRAZON_SOCIAL, DIST_VRFC, DIST_EDO, DIST_ZONA,DIST_DIAS_TRAS,DIST_VDIRECCION, DIST_VCOLONIA, DIST_VPOBLACION, DIST_NCODIGO_POSTAL, USUARIO_MPDIFICA, FECHA_MODOFICA, ESTATUS " ;
  
                for(ClsDDistribuidorVo elem : this.mapDistribuidoresDis.values()){
                    mobjLog.agregarMensaje(elem.getDist_vid_pk()+","+elem.getDist_vrazon_social()+","+elem.getDist_vrfc()+","+
                        elem.getDesEstado()+","+elem.getDist_zona()+","+elem.getDist_dias_tras()+","+elem.getDist_vdireccion()+
                                           ","+elem.getDist_vpoblacion()+","+elem.getDist_ncodigo_postal()+
                                           ","+elem.getUsuarioModifica()+","+elem.getFechaModifica()+","+((elem.getEstatus()==1)?"Activo":"Baja"));
                }
                
            
           
           
            
            salida = true;
            mobjLog.cerrarLog();
        } catch(Exception e){
            log.error("Error "+ e.getMessage(),e); 
            salida = false;
        }
        return salida;
    }
    
    /**
     * Metodo que sirve para borrar el archivo
     * @param file
     * @return
     */
    public boolean eraseFile(File file) {
        if (file.delete())
            log.info("El archivo ha sido borrado satisfactoriamente");
        else
            log.info("El archivo no puede ser borrado");
        return false;

    }
    public boolean eraseFile(String archivo) {
        File file = new File(archivo);
        return this.eraseFile(file);
    }
    public Map<String, ClsDDistribuidorVo> getMapDistribuidoresDis() {
        return mapDistribuidoresDis;
    }

    public void setMapDistribuidoresDis(Map<String, ClsDDistribuidorVo> mapDistribuidoresDis) {
        this.mapDistribuidoresDis = mapDistribuidoresDis;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getClaveDis() {
        return claveDis;
    }

    public void setClaveDis(String claveDis) {
        this.claveDis = claveDis;
    }

    public ClsDDistribuidorVo getVoSelec() {
        return voSelec;
    }

    public void setVoSelec(ClsDDistribuidorVo voSelec) {
        this.voSelec = voSelec;
    }

    public List<ClsDEdosVo> getEdos() {
        return edos;
    }

    public void setEdos(List<ClsDEdosVo> edos) {
        this.edos = edos;
    }

    public String getRutaFisica() {
        return rutaFisica;
    }

    public void setRutaFisica(String rutaFisica) {
        this.rutaFisica = rutaFisica;
    }

    public String getNombreReporte() {
        return nombreReporte;
    }

    public void setNombreReporte(String nombreReporte) {
        this.nombreReporte = nombreReporte;
    }
}
