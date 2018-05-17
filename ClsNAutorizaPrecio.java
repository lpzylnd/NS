package SS.ICO.V10.Fau;

import SS.ICO.V10.Common.clsIParametro;
import SS.ICO.V10.Common.clsISesion;
import SS.ICO.V10.Common.clsIUtil;
import SS.ICO.V10.Uti.Layout.clsILog;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import org.apache.log4j.Logger;

public class ClsNAutorizaPrecio {
    public ClsNAutorizaPrecio() {
        super();
    }
    private clsISesion mobjSesion=null;
    private String mensaje;
    private Map<String, ClsDPreciosVo> mapPreciosDis;
    private List<ClsDPreciosVo> listPreciosAutor;
    private List<ClsDPreciosVo> listPreciosOk;
    private List<ClsDPreciosVo> listPreciosErr;
    private String rutaFisica;
    private   String nombreArchivo;
    
    private TreeSet<String> lisEimAuto ;
    private TreeSet<String> lisCuota ;
    private TreeSet<String> lisFecha ;
    private TreeSet<String> lisTipo ;
    
    private GsonBuilder gsonBuilder;
      private Gson gson;
    Logger log = Logger.getLogger(this.getClass().getName());
    private static final String HEAD_REPORTE =
             "No.,CUOTA, MODELO, TIPO, FECHA, PRECIO_BASE, CLAVE_VEHICULAR," + 
             "DESCRIPCION, DESCRIPCION2, CAPACIDAD," +
        "TRASLADO, SEGURO_TRASLADO, PROM_PUBL, ASIST_VIAL," + 
        "IVA, ANDANAC, AMDA, ACTA_PROMEI, SEG_PLAN_PISO, AYUDA_SOCIAL , RESULTADO";

 
    
    /**Inicializa los campos sesion, y fecha.
     * @param lobjSesion, sesion.
     * */
    public void Inicializa(clsISesion lobjSesion){
        this.mobjSesion = lobjSesion;
        this.mapPreciosDis= new LinkedHashMap<String,ClsDPreciosVo>();
        this.listPreciosAutor=new ArrayList <ClsDPreciosVo>();
        this.setListPreciosOk(new ArrayList<ClsDPreciosVo>());
        this.setListPreciosErr(new ArrayList<ClsDPreciosVo>());
        this.mensaje="";
        this.nombreArchivo="resultadoAutorizados.csv";
        this.lisFecha=new TreeSet<String>();
        this.setLisTipo(new TreeSet<String>());
        this.lisCuota=new TreeSet<String>();
        this.lisEimAuto=new TreeSet<String>();
        this.setGsonBuilder(new GsonBuilder());
        this.setGson(getGsonBuilder().create());
    }
    
    
    public void preciosDisponibles(){
        
        this.getMapPreciosDis().clear();
        StringBuffer sql = new StringBuffer();
        this.lisFecha.clear();
        this.getLisTipo().clear();
        this.lisCuota.clear();
        this.lisEimAuto.clear();
       sql.append(" SELECT PM_VDESCRIPCIONUNO, PM_VDESCRIPCIONDOS, PM_NCUOTA, to_char(PM_DFECHA,'dd/mm/yyyy'), PM_VMODELO, PM_NCAPACIDAD, ") ;
       sql.append(" PM_VCLAVE_VEHICULAR, PM_VTIPO,  PM_DIS_NPRECIO_BASE, O_DIS_NPROM_PUBL, ") ;
       sql.append(" PM_DIS_NASIST_VIAL, O_DIS_NTRASLADO, O_DIS_NSEGURO_TRASLADO, O_DIS_SUB_TOTAL_CARGOS_EXTRAS, ") ;
       sql.append(" O_DIS_SUB_TOTAL, O_DIS_NIVA_16, O_DIS_NAMDA, O_DIS_NANDANAC, O_DIS_NCTA_PROMEI, O_DIS_NSEG_PLAN_PISO, ") ;
       sql.append(" O_DIS_NAYUDA_SOCIAL, O_DIS_GRAN_TOTAL, ESTATUS, USUARIO_CARGA, FECHA_CARGA ") ;
       sql.append(" FROM ICO_FAU_REC_PRECIOS_RCN_TEM ") ;
       sql.append(" where ESTATUS='P' order by PM_DFECHA desc") ;
        
        Connection lcnnConnection = null;
        Statement stm = null;
        ResultSet rst = null;
        int cont=0;
        try {
            lcnnConnection = clsIUtil.getConnectionORCL().getConnection();
            stm = lcnnConnection.createStatement();
            rst = stm.executeQuery(sql.toString());
        while (rst.next()) {
            cont++;
            ClsDPreciosVo vo=new ClsDPreciosVo();
            vo.setDescripcionUno(rst.getString(1));
            vo.setDescripcionDos(rst.getString(2));
            vo.setCuota(Integer.parseInt(rst.getString(3)));
            this.getLisCuota().add(rst.getString(3));
            vo.setFvigencia(rst.getString(4));
            this.getLisFecha().add(rst.getString(4));
            vo.setModelo(rst.getString(5));
            this.getLisEimAuto().add(rst.getString(5));
            vo.setCapacidadMotor(rst.getString(6));
            vo.setClaveVehicular(rst.getString(7));
            vo.setTipo(rst.getString(8));
            this.getLisTipo().add(rst.getString(8));
            vo.setPrecioBase(rst.getBigDecimal(9));
            vo.setNProm_publ(rst.getBigDecimal(10));
            vo.setNAsist_vial(rst.getBigDecimal(11));
            vo.setNTraslado(rst.getBigDecimal(12));
            vo.setNseguroTraslado(rst.getBigDecimal(13));
            vo.setSubCargoExt(rst.getBigDecimal(14));
            vo.setSubTotal(rst.getBigDecimal(15));
            vo.setNiva(rst.getBigDecimal(16));
            vo.setNamda(rst.getBigDecimal(17));
            vo.setNandanac(rst.getBigDecimal(18));
            vo.setNcta_promei(rst.getBigDecimal(19));
            vo.setNseg_plan(rst.getBigDecimal(20));
            vo.setNayuda_social(rst.getBigDecimal(21));
            vo.setGranTotal(rst.getBigDecimal(22));
            vo.setFechaModificacion(rst.getString(25));
            vo.setNumero(cont);
            this.getMapPreciosDis().put(Integer.toString(vo.getNumero()), vo);
        }
        
        } catch (Exception e) {
           log.error(e.getMessage(),e); ;
         log.info(": Error al consultar los precios temporales  disponibles.");
            setMensaje("Error al consultar los precios temporales  disponibles. Consultar Administrador");
            
        } finally {
            try {
                stm.close();
                rst.close();
                if(lcnnConnection != null){
                    lcnnConnection.close();
                }
            } catch (Exception e) {
                log.error(e.getMessage(),e); ;
               log.error("Error al cerrar conexion"+ e.getMessage(),e);
               
            }
        }
    }
    
    public void consultarPreciosDis(ClsDPreciosVo filtro){
        this.getListPreciosErr().clear();
        this.getListPreciosOk().clear();
        this.getListPreciosAutor().clear();
        this.getMapPreciosDis().clear();
        StringBuffer sql = new StringBuffer();
        String cadena=" ";
        
        if(!"N".equals(filtro.getModelo())){
            cadena=cadena+" AND PM_VMODELO='"+filtro.getModelo()+"' ";
        }
        if(!"N".equals(filtro.getTipo())){
            cadena=cadena+" AND PM_VTIPO='"+filtro.getTipo()+"' ";
        }
        if(filtro.getCuota()>0){
            cadena=cadena+" AND PM_NCUOTA='"+filtro.getCuota()+"' ";
        }
        if(!"N".equals(filtro.getFvigencia())){
            cadena=cadena+" AND to_date( to_char(PM_DFECHA,'dd/mm/yyyy'),'dd/mm/yyyy') = to_date('"+filtro.getFvigencia()+"','dd/mm/yyyy') ";
        }
        if(!"N".equals(filtro.getFechaModificacion())){
            cadena=cadena+" AND to_date( to_char(FECHA_CARGA,'dd/mm/yyyy'),'dd/mm/yyyy') = to_date('"+filtro.getFechaModificacion()+"','dd/mm/yyyy') ";
        }
       sql.append(" SELECT PM_VDESCRIPCIONUNO, PM_VDESCRIPCIONDOS, PM_NCUOTA, to_char(PM_DFECHA,'dd/mm/yyyy'), PM_VMODELO, PM_NCAPACIDAD, ") ;
       sql.append(" PM_VCLAVE_VEHICULAR, PM_VTIPO,  PM_DIS_NPRECIO_BASE, O_DIS_NPROM_PUBL, ") ;
       sql.append(" PM_DIS_NASIST_VIAL, O_DIS_NTRASLADO, O_DIS_NSEGURO_TRASLADO, O_DIS_SUB_TOTAL_CARGOS_EXTRAS, ") ;
       sql.append(" O_DIS_SUB_TOTAL, O_DIS_NIVA_16, O_DIS_NAMDA, O_DIS_NANDANAC, O_DIS_NCTA_PROMEI, O_DIS_NSEG_PLAN_PISO, ") ;
       sql.append(" O_DIS_NAYUDA_SOCIAL, O_DIS_GRAN_TOTAL, ESTATUS, USUARIO_CARGA, FECHA_CARGA ") ;
       sql.append(" FROM ICO_FAU_REC_PRECIOS_RCN_TEM ") ;
       sql.append(" where ESTATUS='P' ") ;
        sql.append(cadena);
        sql.append(" order by PM_DFECHA desc ");
        Connection lcnnConnection = null;
        Statement stm = null;
        ResultSet rst = null;
        int cont=0;
        try {
            lcnnConnection = clsIUtil.getConnectionORCL().getConnection();
            stm = lcnnConnection.createStatement();
            rst = stm.executeQuery(sql.toString());
        while (rst.next()) {
            cont++;
            ClsDPreciosVo vo=new ClsDPreciosVo();
            vo.setDescripcionUno(rst.getString(1));
            vo.setDescripcionDos(rst.getString(2));
            vo.setCuota(Integer.parseInt(rst.getString(3)));
            vo.setFvigencia(rst.getString(4));
            vo.setModelo(rst.getString(5));
            vo.setCapacidadMotor(rst.getString(6));
            vo.setClaveVehicular(rst.getString(7));
            vo.setTipo(rst.getString(8));
            vo.setPrecioBase(rst.getBigDecimal(9));
            vo.setNProm_publ(rst.getBigDecimal(10));
            vo.setNAsist_vial(rst.getBigDecimal(11));
            vo.setNTraslado(rst.getBigDecimal(12));
            vo.setNseguroTraslado(rst.getBigDecimal(13));
            vo.setSubCargoExt(rst.getBigDecimal(14));
            vo.setSubTotal(rst.getBigDecimal(15));
            vo.setNiva(rst.getBigDecimal(16));
            vo.setNamda(rst.getBigDecimal(17));
            vo.setNandanac(rst.getBigDecimal(18));
            vo.setNcta_promei(rst.getBigDecimal(19));
            vo.setNseg_plan(rst.getBigDecimal(20));
            vo.setNayuda_social(rst.getBigDecimal(21));
            vo.setGranTotal(rst.getBigDecimal(22));
            vo.setFechaModificacion(rst.getString(25));
            vo.setNumero(cont);
            this.getMapPreciosDis().put(Integer.toString(vo.getNumero()), vo);
        }
            if(this.getMapPreciosDis().isEmpty()){
                this.setMensaje("No Existen registros  con esos criterios.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error(": Error al consultar los precios temporales  disponibles.");
            setMensaje("Error al consultar los precios temporales  disponibles. Consultar Administrador");
            
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
    public void autorizaPrecioModelo (String [] precios) {
        Connection cn = null;
        PreparedStatement ps = null;
        CallableStatement callst = null;
        this.mensaje="";
        int delete = -1;
        String sql = "";
        /* Se consulta el folio a asignar */
        int result=0;
        this.getListPreciosAutor().clear();
        this.getListPreciosErr().clear();
        this.getListPreciosOk().clear();
     String dato="";
        for(int i=0;i<precios.length;i++){
            
            this.getListPreciosAutor().add(this.getMapPreciosDis().get(precios[i]));
        }
        
      sql=" { call ICO_FAU_INSERT_PRECIOSYOP(?,?,?,?,?," +
                                              "?,?,?,?,?," +
                                              "?,?,?,?,?," +
                                              "?,?,?,?,?," +
                                              "?,?,?,?,?)} ";
        int contador=0;   
        String  idUser = this.mobjSesion.UserID();
        try {
          cn =  clsIUtil.getConnectionORCL().getConnection();
        callst = cn.prepareCall(sql);
            
            for(ClsDPreciosVo base:this.getListPreciosAutor()){
               callst.clearParameters();
        /*
 vpm_vdescripcionuno, vpm_vdescripciondos , vpm_ncuota , vpm_dfecha IN VARCHAR2, vpm_vmodelo IN VARCHAR2,
 vpm_ncapacidad IN NUMBER,vpm_vclave_vehicular IN VARCHAR2,vpm_vtipo IN NUMBER, vpm_dis_nprecio_base IN NUMBER, vo_dis_nprom_publ IN NUMBER,
 vo_dis_nasist_vial IN NUMBER,vo_dis_ntraslado IN NUMBER, vo_dis_nseguro_traslado IN NUMBER, vo_dis_sub_total_cargos_extras IN NUMBER,vo_dis_sub_total IN NUMBER,
 vo_dis_niva_16 IN NUMBER,vo_dis_namda IN NUMBER, vo_dis_nandanac IN NUMBER,vo_dis_ncta_promei IN NUMBER,vo_dis_nseg_plan_piso IN NUMBER,
 vo_dis_nayuda_social IN NUMBER, vo_dis_gran_total IN NUMBER,usuario in VARCHAR2, outError OUT  VARCHAR2,outBandera out NUMBER 
         
         */
                callst.setString(1, base.getDescripcionUno());
                callst.setString(2, base.getDescripcionDos());
                callst.setInt(3, base.getCuota());
                callst.setString(4, base.getFvigencia() );
                callst.setString(5, base.getModelo());
                callst.setString(6, base.getCapacidadMotor());
                callst.setString(7, base.getClaveVehicular());
                callst.setString(8,base.getTipo());
                callst.setBigDecimal(9,base.getPrecioBase());
                callst.setBigDecimal(10,base.getNProm_publ());
                callst.setBigDecimal(11,base.getNAsist_vial());
                callst.setBigDecimal(12,base.getNTraslado());
                callst.setBigDecimal(13,base.getNseguroTraslado());
                callst.setBigDecimal(14,base.getSubCargoExt());
                callst.setBigDecimal(15,base.getSubTotal());
                callst.setBigDecimal(16,base.getNiva());
                callst.setBigDecimal(17,base.getNamda());
                callst.setBigDecimal(18,base.getNandanac());
                callst.setBigDecimal(19,base.getNcta_promei());
                callst.setBigDecimal(20,base.getNseg_plan());
                callst.setBigDecimal(21,base.getNayuda_social());
                callst.setBigDecimal(22,base.getGranTotal());
                callst.setString(23,this.getMobjSesion().UserID());
                callst.registerOutParameter(24, Types.VARCHAR);// Bandera de erroneo o exitoso
                callst.registerOutParameter(25, Types.INTEGER);// Detalle de error
                  /* Se ejecuta la eliminación */
                  callst.execute();
                  dato=  callst.getString(24);
                 contador=callst.getInt(25);
                 if(contador > 0){
                     
                 base.setResultado("Registro procesado OK"); 
                 this.getListPreciosOk().add(base);
                
                 } else{
                     base.setResultado(dato);
                    this.getListPreciosErr().add(base);
                 }
                       
                     
            }      
            
            this.mensaje="Se procesaron los  registros";
            
        } catch (SQLException e) {
            log.error("ClsNCatPrecioFaudi " + e.getMessage());
            log.error(" Error al insertar un precio.");
            this.mensaje= this.mensaje+"Error  no se guardaron favor de comunicarse con el administrador";
            
           
        }          catch (Exception e) {
            log.error("ClsNCatPrecioFaudi " + e.getMessage());
            log.error(" Error al insertar un precio.");
            this.mensaje= this.mensaje+"Error  no se guardaron favor de comunicarse con el administrador";
            
           
        }finally {
            try {
                callst.close();
                if(cn != null){
                    cn.close();
                }
            } catch (Exception e) {
                log.error( e.getMessage());
                log.error(" Error al insertar alta precio modelo.");
            }
        }
        
       
             
    }
    
  
    
    public boolean generaReporte(String cad){
        //Se obtiene una coneccion temporal para extraer parametros de BD
        clsIParametro lobjParametros=new clsIParametro();
        Connection conn = null;      
        String nombre = "";
        boolean salida = false;        
        try{
            conn = clsIUtil.getConnectionORCL().getConnection();
        }
        catch(Exception e){
            log.error(e.getMessage(), e);        
        }
        
        lobjParametros.Recupera(53003,conn);//Recupera la ruta fisica donde se creara el archivo
        setRutaFisica(lobjParametros.VVALOR.trim());
        
        //se cierra la coneccion de los parametros
        try{
            conn.close();
        }
        catch(Exception e){
           log.error(e.getMessage(), e); 
        }
       int contador=1;
        try{
            clsILog mobjLog = new clsILog(getNombreArchivo(), this.getRutaFisica(), false);                                    
            mobjLog.agregarMensaje("");
            mobjLog.agregarMensaje("");
            mobjLog.agregarMensaje("");
            mobjLog.agregarMensaje("");
           
            mobjLog.agregarMensaje("NISSAN MEXICANA S.A. DE C.V.");
            mobjLog.agregarMensaje("SISTEMA DE VENTA DE UNIDADES");
            mobjLog.agregarMensaje("CATALOGOS DE DISTRIBUIDORES");
           
                mobjLog.agregarMensaje("LISTADO DE REGISTROS");
            mobjLog.agregarMensaje("");
            mobjLog.agregarMensaje("");
                mobjLog.agregarMensaje(HEAD_REPORTE); 
         //   "DIST_VID_PK, DIST_VRAZON_SOCIAL, DIST_VRFC, DIST_EDO, DIST_ZONA,DIST_DIAS_TRAS,DIST_VDIRECCION, DIST_VCOLONIA, DIST_VPOBLACION, DIST_NCODIGO_POSTAL, USUARIO_MPDIFICA, FECHA_MODOFICA, ESTATUS " ;
            if("OK".equals(cad)){
                for(ClsDPreciosVo elem : this.getListPreciosOk()){
                    
                    mobjLog.agregarMensaje(contador+","+elem.getCuota()+","+elem.getModelo()+","+elem.getTipo()+","+
                        elem.getFvigencia()+","+elem.getPrecioBase()+","+elem.getClaveVehicular()+","+elem.getDescripcionUno()+
                                           ","+elem.getDescripcionDos()+","+elem.getCapacidadMotor()+","+elem.getNTraslado()+
                                           ","+elem.getNseguroTraslado()+","+elem.getNProm_publ()+","+elem.getNAsist_vial()+
                                           ","+elem.getNandanac()+","+elem.getNamda()+","+elem.getNcta_promei()+ 
                                            ","+elem.getNseg_plan()+","+elem.getNayuda_social()+","+elem.getResultado());
                    contador++;
                }
            
            }else{
                for(ClsDPreciosVo elem : this.getListPreciosErr()){
                    
                    mobjLog.agregarMensaje(contador+","+elem.getCuota()+","+elem.getModelo()+","+elem.getTipo()+","+
                        elem.getFvigencia()+","+elem.getPrecioBase()+","+elem.getClaveVehicular()+","+elem.getDescripcionUno()+
                                           ","+elem.getDescripcionDos()+","+elem.getCapacidadMotor()+","+elem.getNTraslado()+
                                           ","+elem.getNseguroTraslado()+","+elem.getNProm_publ()+","+elem.getNAsist_vial()+
                                           ","+elem.getNandanac()+","+elem.getNamda()+","+elem.getNcta_promei()+ 
                                            ","+elem.getNseg_plan()+","+elem.getNayuda_social()+","+elem.getResultado());
                    contador++;
                }
                
            }
        
            
           
           
            
            salida = true;
            mobjLog.cerrarLog();
        } catch(Exception e){
            log.error(e.getMessage(), e); 
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
            log.error("El archivo ha sido borrado satisfactoriamente");
        else
            log.error("El archivo no puede ser borrado");
        return false;

    }
    public clsISesion getMobjSesion() {
        return mobjSesion;
    }

    public void setMobjSesion(clsISesion mobjSesion) {
        this.mobjSesion = mobjSesion;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Map<String, ClsDPreciosVo> getMapPreciosDis() {
        return mapPreciosDis;
    }

    public void setMapPreciosDis(Map<String, ClsDPreciosVo> mapPreciosDis) {
        this.mapPreciosDis = mapPreciosDis;
    }

    public List<ClsDPreciosVo> getListPreciosAutor() {
        return listPreciosAutor;
    }

    public void setListPreciosAutor(List<ClsDPreciosVo> listPreciosAutor) {
        this.listPreciosAutor = listPreciosAutor;
    }

    public List<ClsDPreciosVo> getListPreciosOk() {
        return listPreciosOk;
    }

    public void setListPreciosOk(List<ClsDPreciosVo> listPreciosOk) {
        this.listPreciosOk = listPreciosOk;
    }

    public List<ClsDPreciosVo> getListPreciosErr() {
        return listPreciosErr;
    }

    public void setListPreciosErr(List<ClsDPreciosVo> listPreciosErr) {
        this.listPreciosErr = listPreciosErr;
    }

    public String getRutaFisica() {
        return rutaFisica;
    }

    public void setRutaFisica(String rutaFisica) {
        this.rutaFisica = rutaFisica;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
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

    public TreeSet<String> getLisFecha() {
        return lisFecha;
    }

    public void setLisFecha(TreeSet<String> lisFecha) {
        this.lisFecha = lisFecha;
    }

    public Gson getGson() {
        return gson;
    }

    public void setGson(Gson gson) {
        this.gson = gson;
    }

    public GsonBuilder getGsonBuilder() {
        return gsonBuilder;
    }

    public void setGsonBuilder(GsonBuilder gsonBuilder) {
        this.gsonBuilder = gsonBuilder;
    }

    public TreeSet<String> getLisTipo() {
        return lisTipo;
    }

    public void setLisTipo(TreeSet<String> lisTipo) {
        this.lisTipo = lisTipo;
    }
}
