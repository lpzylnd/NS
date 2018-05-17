package SS.ICO.V10.Fau;

import SS.ICO.V10.Common.clsISesion;
import SS.ICO.V10.Common.clsIUtil;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeSet;

import org.apache.log4j.Logger;


public class ClsNCatPrecioFaudi {
    public ClsNCatPrecioFaudi() {
        super();
    }
    
    private clsISesion mobjSesion=null;
    private Map<String , ClsDPreciosVo> mapPreciosDis;
    private String mensaje;
     private ClsDPreciosVo voSelec;
  private    Gson  jsonArrEim ;
    private GsonBuilder gsonBuilder;
    private Gson gson;
   
    private TreeSet<String> lisEimAuto ;
    private TreeSet<String> lisCuota ;
    private TreeSet<String> lisCabecera ;
    private TreeSet<String> listGrupo ;
    private TreeSet<String> listOrigen ;
    private ClsDPreciosVo voAlta;
    private String fechaIni;
    private String fechaFin;
    Logger log = Logger.getLogger(this.getClass().getName());
    /**Inicializa los campos sesion, y fecha.
     * @param lobjSesion, sesion.
     * */
    public void Inicializa(clsISesion lobjSesion){
        this.setMobjSesion(lobjSesion);
        this.mapPreciosDis=new LinkedHashMap<String, ClsDPreciosVo>();
       
        this.setVoSelec(new ClsDPreciosVo());
        
        this.jsonArrEim=new Gson();
        this.setGsonBuilder(new GsonBuilder());
        this.setGson(getGsonBuilder().create());
        this.lisCabecera=new TreeSet<String>();
        this.lisCuota=new TreeSet<String>();
        this.lisEimAuto=new TreeSet<String>();
    }
    
    public void preciosDisponibles ( )  {

        this.getMapPreciosDis().clear();
        StringBuffer sql = new StringBuffer();
        String cadena=" ";
        
        
        
        /* Se construye la consulta */
        sql.append(" SELECT NCUOTA, VMODELO, VTIPO, to_char(DFECHA,'dd/mm/yyyy') FECHA, NPRECIO_BASE, VCLAVE_VEHICULAR,") ;
              sql.append("  VDESCRIPCION, VDESCRIPCION2, NCAPACIDAD, " );
              sql.append("  VDESCRIPCION3, VCOMPANIA, VMONEDA, DFECHA_MODIF " ); 
        sql.append(" FROM ICO_INV_PRECIO_MODELO  WHERE  VTIPO is not null ");
              sql.append(" order by DFECHA DESC ");
     
    log.info("Modelos activos "+sql);
        Connection lcnnConnection = null;
        Statement stm = null;
        ResultSet rst = null;
       
      
        try {
            lcnnConnection = clsIUtil.getConnectionORCL().getConnection();
            stm = lcnnConnection.createStatement();
            rst = stm.executeQuery(sql.toString());
            
            /* Se iteran los resultados y se agregan al objeto de salida. */
            while (rst.next()) {
              
                getLisCuota().add(rst.getString(1));
                getLisEimAuto().add(rst.getString(2));
                getLisCabecera().add(rst.getString(3));
                  
            }
         
        //    log.error("Gson: " + getGson().toJson(getEimAuto()));
         
          // log.error("Gson: " + getGson().toString());
           // mensaje = "Los Distribuidores están disponibles para seleccionarse.";
        } catch (Exception e) {
            
            log.error("Catalogo precios Catch 001: " + e.getMessage(),e);
            log.error(": Error al consultar los modelos disponibles.");
            setMensaje("Error al consultar los modelos disponibles. Consultar Administrador");
                   
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
    public int insertaPrecioModelo (ClsDPreciosVo vo) {
        Connection cn = null;
        CallableStatement callst = null;
        this.mensaje="";
        int delete = -1;
        String sql = "";
      
        int res=0;
        /* Se construye el delete 
        sql.append(" INSERT INTO ICO_INV_PRECIO_MODELO (NCUOTA, VMODELO, VTIPO, DFECHA, NPRECIO_BASE, VCLAVE_VEHICULAR," );
        sql.append("VDESCRIPCION, VDESCRIPCION2, NCAPACIDAD, VDESCRIPCION3, VCOMPANIA, VMONEDA, DFECHA_MODIF)" );
        sql.append("VALUES (?, ?, ?, ?, ?,?, ?, ?, ?, ?, 'N', '000', ?) " );
        */
        int contador=0;   
        try {
          cn =  clsIUtil.getConnectionORCL().getConnection();
            sql=" { call ICO_FAU_INSERT_PRECIOMODELO(?,?,?,?,?,?,?,?,?,?,?,?,?)} ";
            
            String  idUser = this.mobjSesion.UserID();
            /* cuota IN NUMBER,
            eim IN VARCHAR2,
            tipo IN VARCHAR2,
            fecha in VARCHAR2,
            precio_base IN NUMBER,
            clave_vehicular in VARCHAR2,
            descrpcion1 in VARCHAR2,
            descrpcion2 in VARCHAR2,
            descrpcion3 in VARCHAR2,
            capacidad IN NUMBER,
            usuario in VARCHAR2,
            outError OUT  VARCHAR2,
            outBandera out NUMBER*/
                callst = cn.prepareCall(sql);
                callst.clearParameters();
          
                   
                    callst.setInt(1,  vo.getCuota());
                    callst.setString(2, vo.getModelo());
                    callst.setString(3, vo.getTipo());
                    callst.setString(4, vo.getFvigencia());
                    callst.setBigDecimal(5,vo.getPrecioBase());
                    callst.setString(6,vo.getClaveVehicular());
                    callst.setString(7,vo.getDescripcionUno());
            if(vo.getDescripcionDos()!= null && !"".equals(vo.getDescripcionDos()))
                    callst.setString(8,vo.getDescripcionDos());
                 else
                     callst.setString(8,"N");
                if(vo.getDescripcionTres()!= null && !"".equals(vo.getDescripcionTres()))
                    callst.setString(9, vo.getDescripcionTres());
                else
                    callst.setString(9, "N");   
                
                callst.setString(10, vo.getCapacidadMotor());
                callst.setString(11,idUser);
                callst.registerOutParameter(12, Types.VARCHAR);// Bandera de erroneo o exitoso
                callst.registerOutParameter(13, Types.INTEGER);// Detalle de error
                      /* Se ejecuta la eliminación */
                      callst.execute();
                      this.mensaje=  callst.getString(12);
                         contador=callst.getInt(13);
                        
            if(contador > 0){
              //  this.versionAlgoritmo = lstSelect.getInt(4);
                res = 1;
            } 
       
        
        } catch (Exception e) {
            log.error("ClsNCatPrecioFaudi " + e.getMessage(),e);
            log.error(" Error al actualizar un precio.");
          
            this.mensaje= "Error  no se actualizo favor de comunicarse con el administrador";
            
        }finally {
            try {
                callst.close();
                if(cn != null){
                    cn.close();
                }
            } catch (Exception e) {
                log.error( e.getMessage(),e);
                log.error(" Error al actualizar alta precio modelo.");
            }
        }
        
       return res;
              
    }
    
    public int editarPrecioModelo (ClsDPreciosVo base,ClsDPreciosVo vo) {
        Connection cn = null;
        PreparedStatement ps = null;
        CallableStatement callst = null;
        this.mensaje="";
        int delete = -1;
        String sql = "";
        /* Se consulta el folio a asignar */
        int result=0;
        /* Se construye el delete 
        sql.append(" UPDATE ICO_INV_PRECIO_MODELO SET NCUOTA = ?, VMODELO = ?, VTIPO = ?, DFECHA = ?, NPRECIO_BASE = ?, ");
        sql.append(" VCLAVE_VEHICULAR = ?, VDESCRIPCION = ?, VDESCRIPCION2 = ?, " );
        sql.append(" NCAPACIDAD = ?, VDESCRIPCION3 = ? , DFECHA_MODIF=sysdate ");
        sql.append(" WHERE NCUOTA = ? and VMODELO = ? and DFECHA = ? " );*/
        sql=" { call ICO_FAU_UP_PRECIOMODELO(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)} ";
        int contador=0;   
        String  idUser = this.mobjSesion.UserID();
        try {
          cn =  clsIUtil.getConnectionORCL().getConnection();
         
            callst = cn.prepareCall(sql);
            callst.clearParameters();
            /*base_cuota IN NUMBER,
 base_eim IN VARCHAR2,
 base_fecha in VARCHAR2,
cuota IN NUMBER,
 eim IN VARCHAR2,
 tipo IN VARCHAR2,
 fecha in VARCHAR2,
 precio_base IN NUMBER,
 clave_vehicular in VARCHAR2,
 descrpcion1 in VARCHAR2,
  descrpcion2 in VARCHAR2,
   descrpcion3 in VARCHAR2,
   capacidad IN NUMBER,
   usuario in VARCHAR2,
 outError OUT  VARCHAR2,
 outBandera out NUMBER*/
                callst.setInt(1, base.getCuota());
                callst.setString(2, base.getModelo());
                callst.setString(3, base.getFvigencia());
                callst.setInt(4,  vo.getCuota());
                callst.setString(5, vo.getModelo());
                callst.setString(6, vo.getTipo());
                callst.setString(7, vo.getFvigencia());
                callst.setBigDecimal(8,vo.getPrecioBase());
                callst.setString(9,vo.getClaveVehicular());
                callst.setString(10,vo.getDescripcionUno());
        if(vo.getDescripcionDos()!= null && !"".equals(vo.getDescripcionDos()))
                callst.setString(11,vo.getDescripcionDos());
             else
                 callst.setString(11,"N");
            if(vo.getDescripcionTres()!= null && !"".equals(vo.getDescripcionTres()))
                callst.setString(12, vo.getDescripcionTres());
            else
                callst.setString(12, "N");   
            
            callst.setString(13, vo.getCapacidadMotor());
            callst.setString(14,idUser);
            callst.registerOutParameter(15, Types.VARCHAR);// Bandera de erroneo o exitoso
            callst.registerOutParameter(16, Types.INTEGER);// Detalle de error
                  /* Se ejecuta la eliminación */
                  callst.execute();
                     
                    this.mensaje=  callst.getString(15);
            contador=callst.getInt(16);
            if(contador > 0){
              //  this.versionAlgoritmo = lstSelect.getInt(4);
                result = 1;
            } 
                       
                     
                   
            
                
            
        } catch (SQLException e) {
            log.error("ClsNCatPrecioFaudi " + e.getMessage(),e);
            log.error(" Error al insertar un precio.");
            this.mensaje= this.mensaje+"Error  no se guardaron favor de comunicarse con el administrador";
            
           
        }          catch (Exception e) {
            log.error("ClsNCatPrecioFaudi " + e.getMessage(),e);
            log.error(" Error al insertar un precio.");
            this.mensaje= this.mensaje+"Error  no se guardaron favor de comunicarse con el administrador";
            
           
        }finally {
            try {
                callst.close();
                if(cn != null){
                    cn.close();
                }
            } catch (Exception e) {
                log.error( e.getMessage(),e);
                log.error(" Error al insertar alta precio modelo.");
            }
        }
        
       
       return result;       
    }
    public void consultarPrecio (ClsDPreciosVo filtro )  {

        this.getMapPreciosDis().clear();
        StringBuffer sql = new StringBuffer();
        String cadena=" ";
        
        if(!"N".equals(filtro.getModelo())){
            cadena=cadena+" AND VMODELO='"+filtro.getModelo()+"' ";
        }
        if(!"N".equals(filtro.getTipo())){
            cadena=cadena+" AND VTIPO='"+filtro.getTipo()+"' ";
        }
        if(filtro.getCuota()>0){
            cadena=cadena+" AND NCUOTA='"+filtro.getCuota()+"' ";
        }
        /* Se construye la consulta */
        sql.append(" SELECT NCUOTA, VMODELO, VTIPO, to_char(DFECHA,'dd/mm/yyyy') FECHA, NPRECIO_BASE, VCLAVE_VEHICULAR,") ;
        sql.append("  VDESCRIPCION, VDESCRIPCION2, NCAPACIDAD, " );
        sql.append("  VDESCRIPCION3, VCOMPANIA, VMONEDA, DFECHA_MODIF " ); 
        sql.append(" FROM ICO_INV_PRECIO_MODELO ");
        sql.append(" WHERE  to_date( to_char(DFECHA,'dd/mm/yyyy'),'dd/mm/yyyy') BETWEEN to_date('"+filtro.getFechaInicial()+"','dd/mm/yyyy') and  to_date('"+filtro.getFechaFinal()+"','dd/mm/yyyy') ");
        sql.append(cadena);
        sql.append(" order by DFECHA DESC ");
    log.info("precios activos "+sql);
        Connection lcnnConnection = null;
        Statement stm = null;
        ResultSet rst = null;
        
        try {
            lcnnConnection = clsIUtil.getConnectionORCL().getConnection();
            stm = lcnnConnection.createStatement();
            rst = stm.executeQuery(sql.toString());
            int contador=1;
            /* Se iteran los resultados y se agregan al objeto de salida. */
            while (rst.next()) {
                ClsDPreciosVo vo=new ClsDPreciosVo();
                vo.setCuota(rst.getInt(1));
                vo.setNumero(contador);
                vo.setModelo(rst.getString(2));
                vo.setTipo(rst.getString(3));
                vo.setFvigencia(rst.getString(4));
                vo.setPrecioBase(rst.getBigDecimal(5));
                vo.setClaveVehicular(rst.getString(6));
                vo.setDescripcionUno(rst.getString(7));
               if(rst.getString(8)!=null)
                vo.setDescripcionDos(rst.getString(8));
                vo.setCapacidadMotor(rst.getString(9));
                if(rst.getString(10)!=null)
                vo.setDescripcionTres(rst.getString(10));
                vo.setVcompania(rst.getString(11));
                vo.setVmoneda(rst.getString(12));
                String  num =Integer.toString(contador);
                this.getMapPreciosDis().put(num, vo);
                contador++;
            }
            
            if( this.getMapPreciosDis().isEmpty()){
            setMensaje("No se encontraron datos. ");
            }
           // mensaje = "Los Distribuidores están disponibles para seleccionarse.";
        } catch (Exception e) {
            log.error("Catalogo precios Catch 001: " + e.getMessage());
            log.error(": Error al consultar los modelos disponibles.");
            setMensaje("Error al consultar los modelos disponibles. Consultar Administrador");
                   
        } finally {
            try {
                stm.close();
                rst.close();
                if(lcnnConnection != null){
                    lcnnConnection.close();
                }
            } catch (Exception e) {
                log.error("Error al cerrar conexion"+ e.getMessage());
               
            }
        }

        
        
    }

    public clsISesion getMobjSesion() {
        return mobjSesion;
    }

    public void setMobjSesion(clsISesion mobjSesion) {
        this.mobjSesion = mobjSesion;
    }

    public Map<String, ClsDPreciosVo> getMapPreciosDis() {
        return mapPreciosDis;
    }

    public void setMapPreciosDis(Map<String, ClsDPreciosVo> mapPreciosDis) {
        this.mapPreciosDis = mapPreciosDis;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public ClsDPreciosVo getVoSelec() {
        return voSelec;
    }

    public void setVoSelec(ClsDPreciosVo voSelec) {
        this.voSelec = voSelec;
    }

  

 
    public Gson getJsonArrEim() {
        return jsonArrEim;
    }

    public void setJsonArrEim(Gson jsonArrEim) {
        this.jsonArrEim = jsonArrEim;
    }

    public GsonBuilder getGsonBuilder() {
        return gsonBuilder;
    }

    public void setGsonBuilder(GsonBuilder gsonBuilder) {
        this.gsonBuilder = gsonBuilder;
    }

    public Gson getGson() {
        return gson;
    }

    public void setGson(Gson gson) {
        this.gson = gson;
    }

    public String getFechaIni() {
        return fechaIni;
    }

    public void setFechaIni(String fechaIni) {
        this.fechaIni = fechaIni;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }

    public TreeSet<String> getLisEimAuto() {
        return lisEimAuto;
    }

    public void setLisEimAuto(TreeSet<String> lisEimAuto) {
        this.lisEimAuto = lisEimAuto;
    }

    public TreeSet<String> getLisCuota() {
        return lisCuota;
    }

    public void setLisCuota(TreeSet<String> lisCuota) {
        this.lisCuota = lisCuota;
    }

    public TreeSet<String> getLisCabecera() {
        return lisCabecera;
    }

    public void setLisCabecera(TreeSet<String> lisCabecera) {
        this.lisCabecera = lisCabecera;
    }

    public TreeSet<String> getListGrupo() {
        return listGrupo;
    }

    public void setListGrupo(TreeSet<String> listGrupo) {
        this.listGrupo = listGrupo;
    }

    public TreeSet<String> getListOrigen() {
        return listOrigen;
    }

    public void setListOrigen(TreeSet<String> listOrigen) {
        this.listOrigen = listOrigen;
    }

    public ClsDPreciosVo getVoAlta() {
        return voAlta;
    }

    public void setVoAlta(ClsDPreciosVo voAlta) {
        this.voAlta = voAlta;
    }
}
