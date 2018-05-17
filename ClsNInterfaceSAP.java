package SS.ICO.V10.Fau;

import SS.ICO.V10.Common.Log.clsIEjecutableObj;
import SS.ICO.V10.Common.Log.clsILogProcesos;
import SS.ICO.V10.Common.Log.clsILogueable;
import SS.ICO.V10.Common.clsIConexion;
import SS.ICO.V10.Common.clsIParametro;
import SS.ICO.V10.Common.clsISesion;

import SS.ICO.V10.Uti.Automatico.clsISesionAutomatica;
import SS.ICO.V10.Uti.Layout.clsIGeneraLineaLayout;
import SS.ICO.V10.Uti.Resultado.clsIDesplegador;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

@SuppressWarnings("oracle.jdeveloper.java.semantic-warning")
public class ClsNInterfaceSAP extends clsIDesplegador implements clsILogueable,clsIEjecutableObj {

    private String mstrHead                 = "";
    private String mstrPrograma             = "";
    private String mstrDescripcion          = "";
    private String mstrFuente               = "";
    private String mstrTipoReg              = "";
    private String mstrNombreArchivo        = "";
    private String mstrDirLocal             = "";
    private String mstrMoneda               = "";
    private String mstrDet                  = "";
    private String fechaFactura             = "";
    
    private int mstrTotalRenglones          = 0;
    
    private String[] mstrDetalle;
    
    private clsISesion mobjSesion           = null;
    private clsIConexion mobjConexion       = null;
    private Connection mcnnConexion         = null;
    private clsIParametro mobjParametros;
    private clsILogProcesos mobjMensajes;
    private PrintWriter lobjSalida;
    
    private static Logger logger = Logger.getLogger(ClsNInterfaceSAP.class);
    
    public ClsNInterfaceSAP() {
        super();
    }
    
    public static void main(String arg[]){
        logger.info("Inicia proceso INTERFACE FACTURACION SAP");
        clsISesion lobjsesion2          = null;
        clsISesionAutomatica lobjsesion = new clsISesionAutomatica();
        String[] args                   = { 
            "-clase", "SS.ICO.V10.Fau.ClsNInterfaceSAP", 
            "-db", "jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS=(PROTOCOL=TCP)(HOST=MXNAGCX101)(PORT=1521))(CONNECT_DATA=(SERVER=DEDICATED)(SERVICE_NAME=DBNISDEV)))", 
            "-drv", "oracle.jdbc.driver.OracleDriver", 
            "-id", "icon", 
            "-password", "nissan", 
            "-idDB", "icon_l90", 
            "-passwordDB", "iconl90", 
            "-reproceso", "1", 
            "-IDProceso", "208", 
            "-clase", 
            "-IDArchivo", "ClsNInterfaceSAP", 
            "-IDTabla", "ClsNInterfaceSAP", 
            "-IDProcesos", "ClsNInterfaceSAP", 
            "-IDLog", "208"
            //,"-fecha", "10/04/2018"};
            };
        if(!lobjsesion.CrearSesion(arg)){
            logger.info("FALLA: La sesion no se Concreto correctamente");
        }
        lobjsesion2                     = lobjsesion.Sesion;
        String lstridproceso            = lobjsesion.ObtenerParametro(arg,"-IDProceso");
        String lstrparametros           = "-db "            + lobjsesion.ObtenerParametro(arg,"-db")+" ";
               lstrparametros           += "-drv "          + lobjsesion.ObtenerParametro(arg,"-drv")+" ";
               lstrparametros           += "-idDB "         + lobjsesion.ObtenerParametro(arg,"-idDB")+" ";
               lstrparametros           += "-IDProceso "    + lobjsesion.ObtenerParametro(arg,"-IDProceso")+" ";
               lstrparametros           += "-clase "        + lobjsesion.ObtenerParametro(arg,"-clase")+" ";

        ClsNInterfaceSAP interfaceSAP   = new ClsNInterfaceSAP();
        interfaceSAP.fechaFactura       = lobjsesion.ObtenerParametro(arg,"-fecha");

        if(!interfaceSAP.CreaLog(lobjsesion2,Integer.parseInt(lstridproceso),0,lstrparametros,"prueba clase ClsNInterfaceSAP","ICON")){
            logger.info("Problemas al intentar crear el objeto Log");
            return;
        }

        interfaceSAP.Ejecuta(arg);
        
        if(interfaceSAP.TerminaLog("E","Termino inesperado de ClsNInterfaceSAP")){
            logger.info("La clase que realiza el proceso principal no finalizó correctamente el Log");
            return;
        }
        logger.info("Finaliza proceso INTERFACE FACTURACION SAP");
    }

    public void generarArchivoSAP() {
        this.obtenerParametros();
        this.generaArchivo();
    }
    
    /************* PARAMETROS *************/
    
    private boolean obtenerParametros() {
        logger.info("Obteniendo parametros INTERFACE FACTURACION SAP");
        boolean mbBandera               = true;
        mobjParametros                  = new clsIParametro();
        
        mobjParametros.Recupera(53019, this.mcnnConexion);
        this.mstrDirLocal               = mobjParametros.VVALOR;
        
        mobjParametros.Recupera(53020, this.mcnnConexion);
        this.mstrNombreArchivo          = mobjParametros.VVALOR;
        
        mobjParametros.Recupera(53021, this.mcnnConexion);
        this.mstrHead                   = mobjParametros.VVALOR;
        
        mobjParametros.Recupera(53022, this.mcnnConexion);
        this.mstrPrograma               = mobjParametros.VVALOR;
        
        mobjParametros.Recupera(53023, this.mcnnConexion);
        this.mstrDescripcion            = mobjParametros.VVALOR;
        
        mobjParametros.Recupera(53024, this.mcnnConexion);
        this.mstrFuente                 = mobjParametros.VVALOR;
        
        mobjParametros.Recupera(53025, this.mcnnConexion);
        this.mstrDet                    = mobjParametros.VVALOR;
        
        mobjParametros.Recupera(53026, this.mcnnConexion);
        this.mstrTipoReg                = mobjParametros.VVALOR;
        
        mobjParametros.Recupera(53027, this.mcnnConexion);
        this.mstrMoneda                 = mobjParametros.VVALOR;
        
        this.mstrDetalle                = new String[] { "DET1PR00","DET1YCE4","DET1YKSG","DET1YKTR","DET1YCE1","DET1YCE2","DET1YKPP","DET1YKAV","DET1YCE5","DET1YCE6" };
        
        return mbBandera;
    }
    
    /************* GENERACION DE ARCHIVO *************/
    
    private void generaArchivo(){
        logger.info("Inicia generacion de INTERFACE FACTURACION SAP");
        this.abreArchivo();
        this.armaEncabezado();
        this.generaCuerpoArchivo();
        this.armaPie();
        this.cierraArchivo();
    }
    
    private void abreArchivo() {
        try {
            FileWriter lobjFile     = new FileWriter(this.mstrDirLocal + this.mstrNombreArchivo);                    
            this.lobjSalida         = new PrintWriter(lobjFile);
        }catch(IOException ioe) {
            logger.error(ioe.getMessage(), ioe);
        }
    }
    
    private boolean cierraArchivo() {
        this.lobjSalida.close();
        return true;
    }

    private void armaEncabezado() {    
        logger.info("Armando encabezado INTERFACE FACTURACION SAP");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddkkmmss");
        clsIGeneraLineaLayout lobjLinea=new clsIGeneraLineaLayout();
        lobjLinea.newLinea();
        lobjLinea.addCampo(this.mstrHead, lobjLinea.AligIzquierda, 4, " ");
        lobjLinea.addCampo(this.mstrPrograma, lobjLinea.AligIzquierda, 8, " ");
        lobjLinea.addCampo(sdf.format(new Date()), lobjLinea.AligIzquierda, 14, " ");
        lobjLinea.addCampo(this.mstrDescripcion, lobjLinea.AligDerecha, 20, " ");
        lobjLinea.addCampo(this.mstrFuente, lobjLinea.AligIzquierda, 20, " ");
        lobjLinea.addEspacio(159, " ");
        lobjLinea.addEspacio(24, " ");
        this.escribeArchivo(lobjLinea.getLinea());
    }

    private void armaPie() {
        logger.info("Inicia armado de pie INTERFACE FACTURACION SAP");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddkkmmss");
        clsIGeneraLineaLayout lobjLinea=new clsIGeneraLineaLayout();
        lobjLinea.newLinea();
        lobjLinea.addCampo(this.mstrTipoReg, lobjLinea.AligIzquierda, 4, " ");
        lobjLinea.addCampo(this.mstrPrograma, lobjLinea.AligIzquierda, 8, " ");
        lobjLinea.addCampo(sdf.format(new Date()), lobjLinea.AligIzquierda, 14, " ");
        lobjLinea.addCampo(++mstrTotalRenglones, lobjLinea.AligDerecha, 8, " ");
        lobjLinea.addEspacio(8, " ");
        lobjLinea.addEspacio(32, " ");
        this.escribeArchivo(lobjLinea.getLinea());
    }
    
    private void generaCuerpoArchivo(){
        String[] arr;
        StringBuilder qry           = new StringBuilder();
        
        qry.append("SELECT  ");
        qry.append("  RPAD('CAB', 4) AS CAB,  ");
        qry.append("  RPAD(CASE WHEN UNI.ORIG_VID = 'D' THEN  ");
        qry.append("      CASE WHEN UNI.TVMC_VID = 'F' THEN 'ZDF' ELSE 'ZD' END  ");
        qry.append("      ELSE  ");
        qry.append("      CASE WHEN UNI.TVMC_VID = 'F' THEN 'ZIVF' ELSE 'ZIV' END  ");
        qry.append("      END, 4) AS PEDIDO,  ");
        qry.append("  '0180' AS ORG_VTS,  ");
        qry.append("  '10' AS CAN_DIST,  ");
        qry.append("  CASE WHEN UNI.ORIG_VID = 'D' THEN '10' ");
        qry.append("  ELSE ");
        qry.append("  (SELECT TO_CHAR(SEC.SECT_NID_PK) ");
        qry.append("    FROM  ");
        qry.append("      ICO_INV_CMODELOS CMOD, ");
        qry.append("      ICO_INV_UNIDADES UNI, ");
        qry.append("      ICO_INV_CSECTORES SEC ");
        qry.append("    WHERE  ");
        qry.append("          CMOD.ANIO_NID_PK = UNI.ANIO_NID ");
        qry.append("      AND CMOD.MOD_VEIM_PK = UNI.MOD_VEIM ");
        qry.append("      AND CMOD.SECT_NID    = SEC.SECT_NID_PK ");
        qry.append("      AND UNI.UNID_VVIN_PK = FAC.UNID_VVIN_PK) END AS SECTOR,  ");
        qry.append("  RPAD('MXDN0' || TO_CHAR(NVL(UNI.DIST_NID, ' ')), 10) AS DISTRIBUIDOR,  ");
        qry.append("  RPAD(NVL(TO_CHAR(FAC.UMAY_NNUMERO_FACTURA), ' '), 35) AS FACTURA,  ");
        qry.append("  RPAD(NVL(TO_CHAR(FAC.UMAY_DFECHA_FACTURA_MAYOREO,'yyyymmdd'), ' '), 8) AS FECHA_FACTURA,  ");
        qry.append("  RPAD(NVL(UNI.POL_VID, ' '), 4) AS CONDICION_PAGO,  ");
        qry.append("  RPAD('FOB', 3) AS INCOTERM,  ");
        qry.append("  RPAD(NVL(UNI.MOD_VEIM, ' '), 18) AS MATERIAL,  ");
        qry.append("  LPAD('1', 15) AS CANTIDAD,  ");
        qry.append("  RPAD(NVL(FAC.UNID_VVIN_PK, ' '), 18) AS VIN,  ");
        qry.append("  RPAD(CASE WHEN UNI.PLTA_VID = 'L1' THEN '6035'  ");
        qry.append("      WHEN UNI.PLTA_VID = 'N1' THEN '6030'  ");
        qry.append("      WHEN UNI.PLTA_VID = 'P1' THEN '6050'  ");
        qry.append("      ELSE ' ' END, 4, ' ') AS PLANTA,  ");
        qry.append("  RPAD(NVL(TO_CHAR(UNI.ANIO_NID), ' '), 5) AS MODELO,  ");
        qry.append("  LPAD(NVL(TO_CHAR(FAC.UMAY_NPRECIO_TOTAL), ' '), 11) AS TOTAL,  ");
        qry.append("  RPAD(NVL(SUBSTR(ORD.CAB_VCABECERA_PK,0,2), ' '), 2) AS CABECERA,  ");
        qry.append("  RPAD(NVL(SUBSTR(ORD.CAB_VCABECERA_PK,3,1), ' '), 1) AS TRANSMISION,  ");
        qry.append("  RPAD(NVL(SUBSTR(ORD.CAB_VCABECERA_PK,4), ' '), 2) AS TIPIFICACION,  ");
        qry.append("  RPAD(NVL(UNI.UNID_VMOTOR, ' '), 14) AS MOTOR,  ");
        qry.append("  RPAD(NVL(TO_CHAR(FFM.CLAVE_VEHICULAR), ' '), 12) AS CVE_VEHICULAR,  ");
        qry.append("  RPAD(NVL(TO_CHAR(PED.UPEDIM_DFECHA_PEDIMENTO,'yyyymmdd'), ' '), 8) AS FECHA_PEDIMENTO,  ");
        qry.append("  RPAD(NVL(TO_CHAR(PED.UPEDIM_VNUMERO_PEDIMENTO), ' '), 11) AS NUMERO_PEDIMENTO,  ");
        qry.append("  RPAD(NVL(TO_CHAR(PED.UPEDIM_NNUMERO_ADUANA), ' '), 3) AS NUMERO_ADUANA,  ");
        qry.append("  RPAD(NVL(FFM.COLOR_EXT, ' '), 15) AS COLOR_EXTERIOR,  ");
        qry.append("  RPAD('0', 10) AS SICREA,  ");
        qry.append("  RPAD(NVL(POL.VDESCOLATERAL, ' '), 8) AS COLATERAL,  ");
        qry.append("  RPAD(NVL(ORD.VNUMERO_AUTORIZA_NRFM, ' '), 10) AS CREDIT_NUMBER,  ");
        qry.append("  RPAD(NVL(FAC.CCBENEF_VID_PK, ' '), 2) AS CENTRO_COSTO,  ");
        qry.append("  RPAD(ORD.VPC, 4) AS VPC  ");
        qry.append("FROM  ");
        qry.append("  ICO_INV_UNIDADES UNI,  ");
        qry.append("  ICO_INV_FACTURAS_MAYOREO FAC,  ");
        qry.append("  ICO_INV_PEDIMENTOS PED,  ");
        qry.append("  ICO_NIS_ORDEN ORD, ");
        qry.append("  ICO_FAU_FACTURAS_MAYOREO FFM, ");
        qry.append("  ICO_INV_CPOLITICAS_VTA POL ");
        qry.append("WHERE  ");
        qry.append("      UNI.UNID_VVIN_PK = FAC.UNID_VVIN_PK  ");
        qry.append("  AND UNI.UNID_VVIN_PK = ORD.ORDEN_VIN ");
        qry.append("  AND UNI.POL_VID      = POL.POL_VID_PK ");
        qry.append("  AND FAC.UNID_VVIN_PK = FFM.VIN_PK ");
        qry.append("  AND UNI.UNID_VVIN_PK = PED.UNID_VVIN_PK(+) ");
        qry.append("  AND FAC.UMAY_VSTATUS_TIMB = 'OK' ");
        if(this.fechaFactura != null && !this.fechaFactura.equals("")){
            qry.append("  AND TO_DATE(TO_CHAR(FAC.UMAY_DFECHA_FACTURA_MAYOREO, 'DD/MM/YYYY'),'DD/MM/YYYY') = TO_DATE(TO_CHAR('" + this.fechaFactura + "', 'DD/MM/YYYY'),'DD/MM/YYYY')");
        }else{
            qry.append("  AND TO_DATE(TO_CHAR(FAC.UMAY_DFECHA_FACTURA_MAYOREO, 'DD/MM/YYYY'),'DD/MM/YYYY') = TO_DATE(TO_CHAR(SYSDATE - 1, 'DD/MM/YYYY'),'DD/MM/YYYY')");
        }
        
        Statement stm               = null;
        ResultSet rst               = null;
        try {
            stm                     = this.mcnnConexion.createStatement();
            rst                     = stm.executeQuery(qry.toString());
            while (rst.next()) {
                arr = new String[30];
                for(int i = 0; i < 30; i++){
                    arr[i] = rst.getString(i+1);
                }
                this.armarCabecera(arr);
                this.armaDetalle(rst.getString("VIN").trim());
            }
        }catch (Exception e) {
            logger.error(e.getMessage(), e);
        }finally{
            try{
                if(stm != null){ stm.close(); }
                if(rst != null){ rst.close(); }
            }catch(SQLException e){
                logger.error(e.getMessage(), e);
            }
        }
    }
    
    private void armaDetalle(String vin){
        List<String> arrDet         = new ArrayList<String>();
        List<String> etiquetas      = new ArrayList<String>();
        DecimalFormat df = new DecimalFormat("#.00");
        double data;
        StringBuilder qry           = new StringBuilder();
        
        qry.append("SELECT ");
        qry.append("  NVL(FAC.UMAY_NPRECIO_BASE, 0.0) AS PRECIO_BASE, ");
        qry.append("  NVL(FFM.SEG_PLAN_PISO, 0.0) AS SEGURO, ");
        qry.append("  NVL(FFM.NSEGURO_TRASLADO, 0.0) AS SEGURO_TRASLADO, ");
        qry.append("  NVL(FAC.UMAY_NTRASLADO, 0.0) AS TRASLADO, ");
        qry.append("  NVL(FAC.UMAY_NAMDA, 0.0) AS AMDA, ");
        qry.append("  NVL(FAC.UMAY_NANDANAC, 0.0) AS ANDANAC, ");
        qry.append("  NVL(FAC.UMAY_NPROMOCION_PUBLICITARIA, 0.0) AS PROMOCION_PUBLICITARIA, ");
        qry.append("  NVL(FAC.UMAY_NASISTENCIA_VIAL, 0.0) AS ASISTENCIA_VIAL, ");
        qry.append("  NVL(FAC.UMAY_NAYUDA_SOCIAL, 0.0) AS AYUDA_SOCIAL, ");
        qry.append("  NVL(FFM.PROMEI, 0.0) AS PROMEI, ");
        qry.append("  UNI.ORIG_VID AS ORIGEN ");
        qry.append("FROM ");
        qry.append("  ICO_INV_FACTURAS_MAYOREO FAC, ");
        qry.append("  ICO_FAU_FACTURAS_MAYOREO FFM, ");
        qry.append("  ICO_INV_UNIDADES UNI ");
        qry.append("WHERE ");
        qry.append("      FAC.UNID_VVIN_PK = FFM.VIN_PK ");
        qry.append("  AND FAC.UNID_VVIN_PK = UNI.UNID_VVIN_PK ");
        qry.append("  AND FFM.VIN_PK       = UNI.UNID_VVIN_PK ");
        qry.append("  AND FAC.UNID_VVIN_PK = ? ");
                
        PreparedStatement ps        = null;
        ResultSet rst               = null;
        try {
            ps                      = this.mcnnConexion.prepareStatement(qry.toString());
            ps.setString(1, vin);
            rst                     = ps.executeQuery();
            while (rst.next()) {
                for(int i = 1; i < 11; i++){
                    data = rst.getDouble(i);
                    if(rst.getString("ORIGEN").equals("I") && i == 5 && rst.getDouble(4) > 0){
                        arrDet.add("0.00");
                        etiquetas.add("DET1YCE3");
                    }else if(data > 0){
                        arrDet.add(df.format(data));
                        etiquetas.add(mstrDetalle[i-1]);
                    }
                }
                this.armarDetalle(arrDet, etiquetas);
            }
        }catch (Exception e) {
            logger.error(e.getMessage(), e);
        }finally{
            try{
                if(rst != null){ rst.close(); }
                if(ps != null){ ps.close(); }
            }catch(SQLException e){
                logger.error(e.getMessage(), e);
            }
        }
    }
    
    private void armarCabecera(String[] dataCabecera){
        StringBuilder str = new StringBuilder();
        for(String data : dataCabecera){
            str.append(data);
        }
        this.escribeArchivo(str.toString());
    }

    private void armarDetalle(List<String> dataDetalle, List<String> etiquetas){
        clsIGeneraLineaLayout lobjLinea;
        for(int i = 0; i < dataDetalle.size(); i++ ){
            lobjLinea                   = new clsIGeneraLineaLayout();
            lobjLinea.addCampo(etiquetas.get(i), lobjLinea.AligIzquierda, 8, " ");
            lobjLinea.addCampo(dataDetalle.get(i).toString(), lobjLinea.AligDerecha, 11, " ");
            lobjLinea.addCampo(mstrMoneda, lobjLinea.AligIzquierda, 5, " ");
            lobjLinea.addEspacio(225, " ");
            this.escribeArchivo(lobjLinea.getLinea());
        }
    }
    
    private boolean escribeArchivo(String lstrContenido){
        mstrTotalRenglones++;
        this.lobjSalida.println(lstrContenido);
        return true;
    }

    @Override
    public boolean CreaLog(clsISesion lobjSesion, int lintIdproceso, int lintEjecucion, String lstrParametros,
                           String lstrObservaciones, String lstrUsuario) {
        try{
            mobjMensajes = new clsILogProcesos(lobjSesion,lintIdproceso,lintEjecucion, lstrParametros,lstrObservaciones,lstrUsuario);
            if(mobjMensajes == null){ return false; }
            return true;
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            return false;
        }
    }

    @Override
    public boolean TerminaLog(String lstrEstatus, String lstrObservacion) {
        boolean lbolEstatusLogCerrado = false;
        if(!mobjMensajes.getLogCerrado()){
            if(mobjMensajes.FinalizaLog(lstrEstatus,lstrObservacion)){
                lbolEstatusLogCerrado = true;
            }
        }
        return lbolEstatusLogCerrado;
    }

    @Override
    public boolean Ejecuta(String[] args) {
        clsISesionAutomatica lsesSesion = new clsISesionAutomatica();
        if(lsesSesion.CrearSesion(args)) {
            this.mobjSesion             = lsesSesion.Sesion;
            if(this.AbreConexion()){
                this.generarArchivoSAP();
            }
        }else{
            return false;
        }
        this.CerrarConexion();
        mobjMensajes.FinalizaLog("T", "Termino ClsNInterfaceSAP desde la clase");
        return true;
    }
    
    public boolean AbreConexion(){
        if(this.mobjSesion == null){
            this.mobjMensajes.EstableceDetalle("Clase ClsNInterfaceSAP - Método AbreConexion:", " Falta inicializar Sesion ");
            this.mrdsResultados.addErrorParametrizado("ErrorBaseDatos", " Falta inicializar Sesion ");
            return false;
        }
        try {
            this.mobjMensajes.EstableceDetalle("Clase ClsNInterfaceSAP - Método AbreConexion: ", "Conectando con la Base de Datos ...");
            mobjConexion = new clsIConexion(mobjSesion);
            mcnnConexion = mobjConexion.Conecta();
            this.mobjMensajes.EstableceDetalle("Clase ClsNInterfaceSAP - Método AbreConexion: ", " OK ");
        }catch(SQLException e){
            logger.error(e.getMessage(), e);
            this.mobjMensajes.EstableceDetalle("Clase ClsNInterfaceSAP - Método AbreConexion: ", " ERROR ");
            this.mobjMensajes.EstableceDetalle("Clase ClsNInterfaceSAP - Método AbreConexion: ", "Error Carga Parametros " + e);
            this.mrdsResultados.addErrorParametrizado("ErrorBaseDatos", e.getMessage());
            this.EstablecerTitulo("SinResTitulo");
            return false;
        }
        return true;
    }
    
    public void CerrarConexion(){
        this.mobjMensajes.EstableceDetalle("Clase ClsNInterfaceSAP - Método CerrarConexion:","Cerrando Base de Datos ...");
        mobjConexion.Desconecta(mcnnConexion);
        this.mobjMensajes.EstableceDetalle("Clase ClsNInterfaceSAP - Método CerrarConexion:"," OK ");
    }
    
}