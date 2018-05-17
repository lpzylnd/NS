package SS.ICO.V10.Fau;

import SS.ICO.V10.Common.Negocio.ICONException;
import SS.ICO.V10.Common.clsIConexion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import SS.ICO.V10.Common.clsISesion;
import SS.ICO.V10.IPu.vo.Cliente;

import SS.ICO.V20.general.model.Combo;

import java.sql.DatabaseMetaData;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

import java.text.SimpleDateFormat;
import SS.ICO.V10.Common.clsIUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.naming.NameNotFoundException;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

public class ClsDGeneraArchivoFenis {
    private Connection cn=null;
    private PreparedStatement ps = null;
    private ResultSet rst = null;
    Logger log = Logger.getLogger(this.getClass().getName());
    public ClsDGeneraArchivoFenis(){ 
         
    }
    
    public Map<Integer, List<ClsDRegistroArchivoFenisVo>> getRegistrosConfigArchivo(int idArchivo) throws SQLException{
        Map<Integer, List<ClsDRegistroArchivoFenisVo>> listaRegistros = new HashMap<Integer, List<ClsDRegistroArchivoFenisVo>>();
        List<ClsDRegistroArchivoFenisVo> renglon =  new ArrayList<ClsDRegistroArchivoFenisVo>();
        String sql="SELECT B.ID_CONFIG_REG, C.TIPOREGISTRO, D.* FROM \n" + 
        "ICO_FAU_CAT_ARCHIVOFENIS A,\n" + 
        "ICO_FAU_CONFIG_ARCHIVOFENIS B,\n" + 
        "ICO_FAU_CONFIG_REGISTROFENIS C,\n" + 
        "ICO_FAU_CAT_CAMPOSFENIS D\n" + 
        "WHERE A.ID_TIPOARCHIVO = B.ID_TIPOARCHIVO\n" + 
        "AND B.TIPOREGISTRO = C.TIPOREGISTRO\n" + 
        "AND C.ID_CAMPO = D.ID_CAMPO\n" + 
        "AND A.ID_TIPOARCHIVO = ? ORDER BY B.ORDEN_REGISTROS, C.ORDEN_CAMPOS";
        
            ps = cn.prepareStatement(sql);
            ps.setInt(1, idArchivo);
            rst = ps.executeQuery();
            ClsDRegistroArchivoFenisVo registro;
            int idConfig = 0;
            int i=1;
            
            while (rst.next()) {
                registro = new ClsDRegistroArchivoFenisVo();
                if(idConfig!=rst.getInt(1) && idConfig!=0){
                    listaRegistros.put(i++, renglon);
                    renglon =  new ArrayList<ClsDRegistroArchivoFenisVo>();
                }
                String configCampo = rst.getString(6);
                int idConfigRegistro = rst.getInt(1);
                
                registro.setTipoRegistro(rst.getString(2));
                registro.setIdCampo(rst.getInt(3));               
                
                String numAdic = (configCampo!=null ? "" : idConfigRegistro + "");
                registro.setNombreCampo(rst.getString(4) + numAdic);
                
                registro.setLogitudCampo(rst.getFloat(5));
                registro.setConfCampo(configCampo);

                renglon.add(registro);
                idConfig = rst.getInt(1);    
            }
            
            listaRegistros.put(i, renglon);
        
        return listaRegistros;
    }
    
   
    public Map<Integer,Map<String,String>> getDatosArchivo(boolean isByScreen, int idArchivo, int idTipoFiltro,
                                        String datoInicial, String datofinal) throws SQLException {
        Map<Integer,Map<String,String>> archivo = new TreeMap<Integer,Map<String, String>>();
        String sql;
        switch(idArchivo){
        case 1:
            sql="SELECT  UNI.UNID_VVIN_PK AS VIN,          \n" + 
            "                   MAY.umay_nnumero_factura AS HD#FACTURA1,  \n" + 
            "                   'F' AS HD#TIPOPEDIDO1,  \n" + 
            "                   TO_CHAR(FAU.FECHA_FACTURA,'YYYYMMDD') AS HD#FECHAFACTURA1,  \n" + 
            "                   TO_CHAR(FAU.FECHA_FACTURA,'HH24MISS') AS HD#HORAFACTURA1,  \n" + 
            "                   POL.POL_VDESCRIPCION AS HD#DESCFACT1,  \n" + 
            "                   FAU.PREFIJO_FACTURA AS HD#SERIE1,  \n" + 
            "                   'MXN' AS HD#MONEDA1,  \n" + 
            "                   '000000017892' AS HD#NUMAPROBACION1,  \n" + 
            "                   '2007' AS HD#ANOAPROBACION1,  \n" + 
            "                   '1600' AS HD#PORCENTAJEIVA1,  \n" + 
            "                   'PPD' AS HD#FORMAPAGO1,  \n" + 
            "                   'MEXICO, CIUDAD DE MEXICO' AS HD#LUGAREXPEDICION1,  \n" + 
            "                   'NO IDENTIFICADO' AS HD#NUMCTAPAGO1,  \n" + 
            "                   '601' AS HD#REGIMEN1,  \n" + 
            "                   '99' AS HD#METODODEPAGO1,  \n" + 
            "                   '3.3' AS HD#VERSION1,  \n" + 
            "                   'I' AS HD#TIPOCOMPROBANTE1,  \n" + 
            "                   LPAD(UNI.DIST_NID, 6, '0') AS DC#CVECLIENTE2,   \n" + 
            "                   DIS.DIST_VRAZON_SOCIAL AS DC#DCRAZONSOCIAL2,  \n" + 
            "                   DIS.DIST_VRFC AS DC#DCRFC2,  \n" + 
            "                   DIS.DIST_VDIRECCION AS DC#DCDIRECCION12,  \n" + 
            "                   DIS.DIST_VPOBLACION AS DC#DCDIRECCION22,  \n" + 
            "                   EDO.EDO_VDESCRIPCION AS DC#DCDIRECCION32,  \n" + 
            "                   'C.P. '||DIS.DIST_NCODIGO_POSTAL AS DC#DCDIRECCION52,  \n" + 
            "                   'G01' AS DC#USOCFDI2,  \n" + 
            "                   POL.POL_VDESCRIPCION AS NP#ORDENCOMPRA3,  \n" + 
            "                   UNI.MOD_VEIM AS NP#MARCA3,  \n" + 
            "                   UNI.ANIO_NID AS NP#MODELO3,  \n" + 
            "                   UNI.UNID_VMOTOR AS NP#MOTOR3,  \n" + 
            "                   FAU.CAPACIDAD_MOTOR AS NP#CAPACIDADMOTOR3,  \n" + 
            "                   UNI.UNID_VVIN_PK AS NP#SERIE3,  \n" + 
            "                   FAU.COLOR_EXT AS NP#COLOR3,  \n" + 
            "                   FAU.DESCRIPCION1 AS NP#TIPO3,  \n" + 
            "                   FAU.DESCRIPCION2 AS NP#TRANSMISION3,  \n" + 
            "                   REPLACE(TO_CHAR(MAY.UMAY_NPRECIO_BASE,'fm000000000.0000'),'.') AS NP#IMPORTELINMONNAL3,  \n" + 
            "                   FAU.CLAVE_VEHICULAR AS NP#CVEVEHICULAR3,  \n" + 
            "                   REPLACE(TO_CHAR(MAY.UMAY_NPRECIO_BASE,'fm000000000.0000'),'.') AS NP#PRECIOUNITARIOMONNAL3,  \n" + 
            "                   'PIEZA' AS NP#UNIDAD3,  \n" + 
            "                   'H87' AS NP#CLAVEUNIDAD3,  \n" + 
            "                   '25101500' AS NP#CLAVEPRODSERV3,  \n" + 
            "                   REPLACE(TO_CHAR(MAY.UMAY_NPRECIO_BASE, 'fm00000000000000.000000'),'.') AS NP#BASEIMPUESTOTRASLRASLADO3,  \n" + 
            "                   '002' AS NP#IMPUESTOTRASLADO3,  \n" + 
            "                   'TASA' AS NP#TIPOFACTORTRASLADADO3,  \n" + 
            "                   '0000160000' AS NP#TASAOCUOTATRASLADO3,  \n" + 
            "                   REPLACE(TO_CHAR(ROUND((MAY.UMAY_NPRECIO_BASE * .16),2), 'fm00000000000000.000000'),'.') AS NP#IMPORTETRASLADO3,  \n" + 
            "                   '0000' AS NP#CAPACIDADMOTOR4,  \n" + 
            "                   'TRASLADO' AS NP#TIPO4,  \n" + 
            "                   REPLACE(TO_CHAR(MAY.UMAY_NTRASLADO,'fm000000000.0000'),'.') AS NP#IMPORTELINMONNAL4,  \n" + 
            "                   REPLACE(TO_CHAR(MAY.UMAY_NTRASLADO,'fm000000000.0000'),'.') AS NP#PRECIOUNITARIOMONNAL4,  \n" + 
            "                   'PIEZA' AS NP#UNIDAD4,  \n" + 
            "                   'E48' AS NP#CLAVEUNIDAD4,  \n" + 
            "                  '01010101' AS NP#CLAVEPRODSERV4,  \n" + 
            "                   REPLACE(TO_CHAR(MAY.UMAY_NTRASLADO, 'fm00000000000000.000000'),'.') AS NP#BASEIMPUESTOTRASLRASLADO4,  \n" + 
            "                   '002' AS NP#IMPUESTOTRASLADO4,  \n" + 
            "                   'TASA' AS NP#TIPOFACTORTRASLADADO4,  \n" + 
            "                   '0000160000' AS NP#TASAOCUOTATRASLADO4,  \n" + 
            "                   REPLACE(TO_CHAR(ROUND((MAY.UMAY_NTRASLADO * .16),2), 'fm00000000000000.000000'),'.') AS NP#IMPORTETRASLADO4,          \n" + 
            "                   '0000' AS NP#CAPACIDADMOTOR5,  \n" + 
            "                   'SEGURO TRASLADAD' AS NP#TIPO5,  \n" + 
            "                   REPLACE(TO_CHAR(MAY.UMAY_NSEGURO,'fm000000000.0000'),'.') AS NP#IMPORTELINMONNAL5,  \n" + 
            "                   REPLACE(TO_CHAR(MAY.UMAY_NSEGURO,'fm000000000.0000'),'.') AS NP#PRECIOUNITARIOMONNAL5,  \n" + 
            "                   'PIEZA' AS NP#UNIDAD5,  \n" + 
            "                   'E48' AS NP#CLAVEUNIDAD5,  \n" + 
            "                   '01010101' AS NP#CLAVEPRODSERV5,  \n" + 
            "                   REPLACE(TO_CHAR(MAY.UMAY_NSEGURO, 'fm00000000000000.000000'),'.') AS NP#BASEIMPUESTOTRASLRASLADO5,  \n" + 
            "                   '002' AS NP#IMPUESTOTRASLADO5,  \n" + 
            "                   'TASA' AS NP#TIPOFACTORTRASLADADO5,  \n" + 
            "                   '0000160000' AS NP#TASAOCUOTATRASLADO5,  \n" + 
            "                   REPLACE(TO_CHAR(ROUND((MAY.UMAY_NSEGURO * .16),2), 'fm00000000000000.000000'),'.') AS NP#IMPORTETRASLADO5,          \n" + 
            "                   '0000' AS NP#CAPACIDADMOTOR6,  \n" + 
            "                   'PROMOCION PUBLIC' AS NP#TIPO6,  \n" + 
            "                   REPLACE(TO_CHAR(MAY.UMAY_NPROMOCION_PUBLICITARIA,'fm000000000.0000'),'.') AS NP#IMPORTELINMONNAL6,  \n" + 
            "                   REPLACE(TO_CHAR(MAY.UMAY_NPROMOCION_PUBLICITARIA,'fm000000000.0000'),'.') AS NP#PRECIOUNITARIOMONNAL6,  \n" + 
            "                   'PIEZA' AS NP#UNIDAD6,  \n" + 
            "                   'E48' AS NP#CLAVEUNIDAD6,  \n" + 
            "                  '01010101' AS NP#CLAVEPRODSERV6,  \n" + 
            "                   REPLACE(TO_CHAR(MAY.UMAY_NPROMOCION_PUBLICITARIA, 'fm00000000000000.000000'),'.') AS NP#BASEIMPUESTOTRASLRASLADO6,  \n" + 
            "                   '002' AS NP#IMPUESTOTRASLADO6,  \n" + 
            "                   'TASA' AS NP#TIPOFACTORTRASLADADO6,  \n" + 
            "                   '0000160000' AS NP#TASAOCUOTATRASLADO6,  \n" + 
            "                   REPLACE(TO_CHAR(ROUND((MAY.UMAY_NPROMOCION_PUBLICITARIA * .16),2), 'fm00000000000000.000000'),'.') AS NP#IMPORTETRASLADO6,  \n" + 
            "                   '0000' AS NP#CAPACIDADMOTOR7,  \n" + 
            "                   'ASISTENCIA VIAL' AS NP#TIPO7,  \n" + 
            "                   REPLACE(TO_CHAR(MAY.UMAY_NASISTENCIA_VIAL,'fm000000000.0000'),'.') AS NP#IMPORTELINMONNAL7,  \n" + 
            "                   REPLACE(TO_CHAR(MAY.UMAY_NASISTENCIA_VIAL,'fm000000000.0000'),'.') AS NP#PRECIOUNITARIOMONNAL7,  \n" + 
            "                   'PIEZA' AS NP#UNIDAD7,  \n" + 
            "                   'E48' AS NP#CLAVEUNIDAD7,  \n" + 
            "                   '01010101' AS NP#CLAVEPRODSERV7,  \n" + 
            "                   REPLACE(TO_CHAR(MAY.UMAY_NASISTENCIA_VIAL, 'fm00000000000000.000000'),'.') AS NP#BASEIMPUESTOTRASLRASLADO7,  \n" + 
            "                   '002' AS NP#IMPUESTOTRASLADO7,  \n" + 
            "                   'TASA' AS NP#TIPOFACTORTRASLADADO7,  \n" + 
            "                   '0000160000' AS NP#TASAOCUOTATRASLADO7,  \n" + 
            "                   REPLACE(TO_CHAR(ROUND((MAY.UMAY_NASISTENCIA_VIAL * .16),2), 'fm00000000000000.000000'),'.') AS NP#IMPORTETRASLADO7,  \n" + 
            "                   '0000' AS NP#CAPACIDADMOTOR8,  \n" + 
            "                   'AMDA' AS NP#TIPO8,  \n" + 
            "                   REPLACE(TO_CHAR(MAY.UMAY_NAMDA,'fm000000000.0000'),'.') AS NP#IMPORTELINMONNAL8,  \n" + 
            "                   REPLACE(TO_CHAR(MAY.UMAY_NAMDA,'fm000000000.0000'),'.') AS NP#PRECIOUNITARIOMONNAL8,  \n" + 
            "                   'PIEZA' AS NP#UNIDAD8,  \n" + 
            "                   'E48' AS NP#CLAVEUNIDAD8,  \n" + 
            "                   '01010101' AS NP#CLAVEPRODSERV8,  \n" + 
            "                   '1.1' AS TC#VERSION9,  \n" + 
            "                   'AND660531FB8' AS TC#RFC9,  \n" + 
            "                   'ASOCIACIÓN NACIONAL DE DISTRIBUIDORES DE AUTOMÓVILES NISSAN, A.C.' AS TC#NOMBRE9,  \n" + 
            "                   'ADOLFO PRIETO' AS TI#CALLE10,  \n" + 
            "                   '624' AS TI#NOEXTERIOR10,  \n" + 
            "                   'DEL VALLE' AS TI#COLONIA10,  \n" + 
            "                   'DELEGACION BENITO JUAREZ' AS TI#MUNICIPIO10,  \n" + 
            "                  'CIUDAD DE MEXICO' AS TI#ESTADO10,  \n" + 
            "                   'MEXICO' AS TI#PAIS10,  \n" + 
            "                   '03100' AS TI#CODIGOPOSTAL10,  \n" + 
            "                   '1' AS TX#CANTIDAD11,  \n" + 
            "                   'AMDA' AS TX#DESCRIPCION11,  \n" + 
            "                   REPLACE(TO_CHAR(MAY.UMAY_NAMDA,'fm00000000000000.000000'),'.') AS TX#VALORUNITARIO11,  \n" + 
            "                   REPLACE(TO_CHAR(MAY.UMAY_NAMDA,'fm00000000000000.000000'),'.') AS TX#IMPORTE11,          \n" + 
            "                   '0000' AS NP#CAPACIDADMOTOR12,  \n" + 
            "                   'ANDANAC' AS NP#TIPO12,  \n" + 
            "                   REPLACE(TO_CHAR(MAY.UMAY_NANDANAC,'fm000000000.0000'),'.') AS NP#IMPORTELINMONNAL12,  \n" + 
            "                   REPLACE(TO_CHAR(MAY.UMAY_NANDANAC,'fm000000000.0000'),'.') AS NP#PRECIOUNITARIOMONNAL12,  \n" + 
            "                   'PIEZA' AS NP#UNIDAD12,  \n" + 
            "                   'E48' AS NP#CLAVEUNIDAD12,  \n" + 
            "                   '01010101' AS NP#CLAVEPRODSERV12,  \n" + 
            "                   '1.1' AS TC#VERSION12,  \n" + 
            "                   'AND660531FB8' AS TC#RFC13,  \n" + 
            "                   'ASOCIACIÓN NACIONAL DE DISTRIBUIDORES DE AUTOMÓVILES NISSAN, A.C.' AS TC#NOMBRE13,  \n" + 
            "                   'ADOLFO PRIETO' TI#CALLE14,  \n" + 
            "                   '624' AS TI#NOEXTERIOR14,  \n" + 
            "                   'DEL VALLE' AS TI#COLONIA14,  \n" + 
            "                   'DELEGACION BENITO JUAREZ' AS TI#MUNICIPIO14,  \n" + 
            "                   'CIUDAD DE MEXICO' AS TI#ESTADO14,  \n" + 
            "                   'MEXICO' AS TI#PAIS14,  \n" + 
            "                   '03100' AS TI#CODIGOPOSTAL14,  \n" + 
            "                   '1' AS TX#CANTIDAD15,  \n" + 
            "                   'ANDANAC' AS TX#DESCRIPCION15,  \n" + 
            "                   REPLACE(TO_CHAR(MAY.UMAY_NANDANAC,'fm00000000000000.000000'),'.') AS TX#VALORUNITARIO15,  \n" + 
            "                   REPLACE(TO_CHAR(MAY.UMAY_NANDANAC,'fm00000000000000.000000'),'.') AS TX#IMPORTE15,          \n" + 
            "                   '0000' AS NP#CAPACIDADMOTOR16,  \n" + 
            "                   'PROMEI' AS NP#TIPO16,  \n" + 
            "                   REPLACE(TO_CHAR(FAU.PROMEI,'fm000000000.0000'),'.') AS NP#IMPORTELINMONNAL16,  \n" + 
            "                   REPLACE(TO_CHAR(FAU.PROMEI,'fm000000000.0000'),'.') AS NP#PRECIOUNITARIOMONNAL16,  \n" + 
            "                   'PIEZA' AS NP#UNIDAD16,  \n" + 
            "                   'E48' AS NP#CLAVEUNIDAD16,  \n" + 
            "                   '01010101' AS NP#CLAVEPRODSERV16,  \n" + 
            "                   'AND660531FB8' AS TC#RFC17,  \n" + 
            "                   'ASOCIACIÓN NACIONAL DE DISTRIBUIDORES DE AUTOMÓVILES NISSAN, A.C.' AS TC#NOMBRE17,  \n" + 
            "                   'ADOLFO PRIETO' TI#CALLE18,  \n" + 
            "                   '624' AS TI#NOEXTERIOR18,  \n" + 
            "                   'DEL VALLE' AS TI#COLONIA18,  \n" + 
            "                   'DELEGACION BENITO JUAREZ' AS TI#MUNICIPIO18,  \n" + 
            "                   'CIUDAD DE MEXICO' AS TI#ESTADO18,  \n" + 
            "                   'MEXICO' AS TI#PAIS18,  \n" + 
            "                   '03100' AS TI#CODIGOPOSTAL18,          \n" + 
            "                   '1' AS TX#CANTIDAD19,  \n" + 
            "                   'PROMEI' AS TX#DESCRIPCION19,  \n" + 
            "                   REPLACE(TO_CHAR(FAU.PROMEI,'fm00000000000000.000000'),'.') AS TX#VALORUNITARIO19,  \n" + 
            "                   REPLACE(TO_CHAR(FAU.PROMEI,'fm00000000000000.000000'),'.') AS TX#IMPORTE19,  \n" + 
            "                   '0000' AS NP#CAPACIDADMOTOR20,  \n" + 
            "                   'SEGURO PLAN PISO' AS NP#TIPO20,  \n" + 
            "                   REPLACE(TO_CHAR(FAU.SEG_PLAN_PISO,'fm000000000.0000'),'.') AS NP#IMPORTELINMONNAL20,  \n" + 
            "                   REPLACE(TO_CHAR(FAU.SEG_PLAN_PISO,'fm000000000.0000'),'.') AS NP#PRECIOUNITARIOMONNAL20,  \n" + 
            "                   'PIEZA' AS NP#UNIDAD20,  \n" + 
            "                   'E48' AS NP#CLAVEUNIDAD20,  \n" + 
            "                   '01010101' AS NP#CLAVEPRODSERV20,  \n" + 
            "                   'AND660531FB8' AS TC#RFC21,  \n" + 
            "                   'ASOCIACIÓN NACIONAL DE DISTRIBUIDORES DE AUTOMÓVILES NISSAN, A.C.' AS TC#NOMBRE21,  \n" + 
            "                   'ADOLFO PRIETO' TI#CALLE22,  \n" + 
            "                   '624' AS TI#NOEXTERIOR22,  \n" + 
            "                   'DEL VALLE' AS TI#COLONIA22,  \n" + 
            "                   'DELEGACION BENITO JUAREZ' AS TI#MUNICIPIO22,  \n" + 
            "                   'CIUDAD DE MEXICO' AS TI#ESTADO22,  \n" + 
            "                   'MEXICO' AS TI#PAIS22,  \n" + 
            "                   '03100' AS TI#CODIGOPOSTAL22,  \n" + 
            "                   '1' AS TX#CANTIDAD23,  \n" + 
            "                   'SEGURO PLAN PISO' AS TX#DESCRIPCION23,  \n" + 
            "                   REPLACE(TO_CHAR(FAU.SEG_PLAN_PISO,'fm00000000000000.000000'),'.') AS TX#VALORUNITARIO23,  \n" + 
            "                   REPLACE(TO_CHAR(FAU.SEG_PLAN_PISO,'fm00000000000000.000000'),'.') AS TX#IMPORTE23,  \n" + 
            "                   '0000' AS NP#CAPACIDADMOTOR24,  \n" + 
            "                   'AYUDA-SOCIAL' AS NP#TIPO24,  \n" + 
            "                   REPLACE(TO_CHAR(MAY.UMAY_NAYUDA_SOCIAL,'fm000000000.0000'),'.') AS NP#IMPORTELINMONNAL24,  \n" + 
            "                   REPLACE(TO_CHAR(MAY.UMAY_NAYUDA_SOCIAL,'fm000000000.0000'),'.') AS NP#PRECIOUNITARIOMONNAL24,  \n" + 
            "                   'PIEZA' AS NP#UNIDAD24,  \n" + 
            "                   'E48' AS NP#CLAVEUNIDAD24,  \n" + 
            "                   '01010101' AS NP#CLAVEPRODSERV24,  \n" + 
            "                   'AND660531FB8' AS TC#RFC25,  \n" + 
            "                   'ASOCIACIÓN NACIONAL DE DISTRIBUIDORES DE AUTOMÓVILES NISSAN, A.C.' AS TC#NOMBRE25,  \n" + 
            "                   'ADOLFO PRIETO' TI#CALLE26,  \n" + 
            "                   '624' AS TI#NOEXTERIOR26,  \n" + 
            "                   'DEL VALLE' AS TI#COLONIA26,  \n" + 
            "                   'DELEGACION BENITO JUAREZ' AS TI#MUNICIPIO26,  \n" + 
            "                   'CIUDAD DE MEXICO' AS TI#ESTADO26,  \n" + 
            "                   'MEXICO' AS TI#PAIS26,  \n" + 
            "                   '03100' AS TI#CODIGOPOSTAL26,          \n" + 
            "                   '1' AS TX#CANTIDAD27,  \n" + 
            "                   'AYUDA-SOCIAL' AS TX#DESCRIPCION27,  \n" + 
            "                   REPLACE(TO_CHAR(MAY.UMAY_NAYUDA_SOCIAL,'fm00000000000000.000000'),'.') AS TX#VALORUNITARIO27,  \n" + 
            "                   REPLACE(TO_CHAR(MAY.UMAY_NAYUDA_SOCIAL,'fm00000000000000.000000'),'.') AS TX#IMPORTE27,  \n" + 
            "                   'PRECIO UNITARIO' AS GT#CONCEPTOPRECIOBASE28,  \n" + 
            "                   REPLACE(TO_CHAR(FAU.IMPORTES_BASE_IVA,'fm0000000000.0000'),'.') AS GT#PRECIOBASE28,  \n" + 
            "                   REPLACE(TO_CHAR(FAU.IMPORTES_BASE_IVA,'fm0000000000.0000'),'.') AS GT#PRECIOBASEMONNAL28,  \n" + 
            "                   'SUBTOTAL' AS GT#DSCSUBTOTAL28,  \n" + 
            "                   REPLACE(TO_CHAR(FAU.SUBTOTAL,'fm0000000000.0000'),'.')  AS GT#SUBTOTAL28,  \n" + 
            "                   REPLACE(TO_CHAR(FAU.SUBTOTAL,'fm0000000000.0000'),'.') AS GT#SUBTOTALMONNAL28,  \n" + 
            "                   REPLACE(TO_CHAR(MAY.UMAY_NPRECIO_TOTAL,'fm0000000000.0000'),'.') AS GT#PRECIOTOTMONNAL28,  \n" + 
            "                   UPPER(expresar_en_letras.numero_a_letras( MAY.UMAY_NPRECIO_TOTAL )) AS GT#CANTLETRA28,  \n" + 
            "                   'TOTAL DE CUOTAS' AS GT#CONCEPTOTOTALCUOTAS28,  \n" + 
            "                   REPLACE(TO_CHAR(FAU.TOTAL_CUOTAS,'fm0000000000.0000'),'.') AS GT#IMPORTEMONNAL28,  \n" + 
            "                   REPLACE(TO_CHAR(FAU.IMPORTES_IVA,'fm00000000000000.000000'),'.') AS GT#TOTALIMPUESTOSTRASLADADO28,  \n" + 
            "                   'TRASLADO' AS G1#DCSIVA29,  \n" + 
            "                   REPLACE(TO_CHAR(MAY.UMAY_NTRASLADO,'fm0000000000.0000'),'.') AS G1#IVAMONNAL29,  \n" + 
            "                   'SEGURO TRASLADO' AS G1#DCSIVA30,  \n" + 
            "                   REPLACE(TO_CHAR(MAY.UMAY_NSEGURO,'fm0000000000.0000'),'.') AS G1#IVAMONNAL30,  \n" + 
            "                   'PROMOCION PUBLICITARIA' AS  G1#DCSIVA31,  \n" + 
            "                   REPLACE(TO_CHAR(MAY.UMAY_NPROMOCION_PUBLICITARIA,'fm0000000000.0000'),'.') AS G1#IVAMONNAL31,  \n" + 
            "                   'ASISTENCIA VIAL' AS G1#DCSIVA32,  \n" + 
            "                   REPLACE(TO_CHAR(MAY.UMAY_NASISTENCIA_VIAL,'fm0000000000.0000'),'.') AS G1#IVAMONNAL32,  \n" + 
            "                   'BASE GRAVABLE' AS G1#DCSIVA33,  \n" + 
            "                   REPLACE(TO_CHAR(FAU.IMPORTES_BASE_IVA,'fm0000000000.0000'),'.') AS G1#IVAMONNAL33,  \n" + 
            "                   'I.V.A.'AS G1#DCSIVA34,  \n" + 
            "                   REPLACE(TO_CHAR(FAU.IMPORTES_IVA,'fm0000000000.0000'),'.') AS G1#IVAMONNAL34,  \n" + 
            "                   'LOS SIGUIENTES CARGOS SON  POR CUENTA Y ORDEN' AS G2#LEYENDAPRECIOSIMP35,  \n" + 
            "                   'DE LA ASOCIACION  NACIONAL DE  DISTRIBUIDORES' AS G2#LEYENDAPRECIOSIMP36,  \n" + 
            "                   'DE AUTOMOVILES NISSAN A. C.' AS G2#LEYENDAPRECIOSIMP37,  \n" + 
            "                   '.' AS G2#LEYENDAPRECIOSIMP38,  \n" + 
            "                   'POR LO  TANTO LOS COMPROBANTES CON REQUISITOS' AS G2#LEYENDAPRECIOSIMP39,  \n" + 
            "                   'FISCALES QUE AMPARAN EL COBRO DE ESTAS CUOTAS' AS G2#LEYENDAPRECIOSIMP40,  \n" + 
            "                   'SERAN  EMITIDOS  POR  LOS  BENEFICIARIOS  DEL' AS G2#LEYENDAPRECIOSIMP41,  \n" + 
            "                   'INGRESO. EN CONSECUENCIA, ESTE DOCUMENTO  NO' AS G2#LEYENDAPRECIOSIMP42,  \n" + 
            "                   'CONSTITUYE  COMPROBANTE  FISCAL DEL  PAGO  DE' AS G2#LEYENDAPRECIOSIMP43,  \n" + 
            "                   'DICHAS  CUOTAS EN LOS TERMINOS DEL ART.29 DEL' AS G2#LEYENDAPRECIOSIMP44,  \n" + 
            "                  'CODIGO FISCAL DE LA FEDERACION.' AS G2#LEYENDAPRECIOSIMP45,  \n" + 
            "                   '' AS G2#LEYENDAPRECIOSIMP46,  \n" + 
            "                   'AMDA' AS G4#GTOSGRALPIE47,  \n" + 
            "                   REPLACE(TO_CHAR(MAY.UMAY_NAMDA,'fm0000000000.0000'),'.') AS G4#IMPTEGTOPIEMONNAL47,  \n" + 
            "                   'ANDANAC' AS G4#GTOSGRALPIE48,  \n" + 
            "                   REPLACE(TO_CHAR(MAY.UMAY_NANDANAC,'fm0000000000.0000'),'.') AS G4#IMPTEGTOPIEMONNAL48,          \n" + 
            "                   'PROMEI' AS G4#GTOSGRALPIE49,  \n" + 
            "                   REPLACE(TO_CHAR(FAU.PROMEI,'fm0000000000.0000'),'.') AS G4#IMPTEGTOPIEMONNAL49,          \n" + 
            "                   'SEGURO PLAN PISO' AS G4#GTOSGRALPIE50,  \n" + 
            "                   REPLACE(TO_CHAR(FAU.SEG_PLAN_PISO,'fm0000000000.0000'),'.') AS G4#IMPTEGTOPIEMONNAL50,          \n" + 
            "                   'AYUDA SOCIAL' AS G4#GTOSGRALPIE51,  \n" + 
            "                   REPLACE(TO_CHAR(MAY.UMAY_NAYUDA_SOCIAL,'fm0000000000.0000'),'.') AS G4#IMPTEGTOPIEMONNAL51,  \n" + 
            "                   'TRASLADADO' AS DI#TIPOIMPUESTO52,  \n" + 
            "                   '002' AS DI#CONCEPTOIMPUESTO52,  \n" + 
            "                   '0000160000' AS DI#TASAOCUOTA52,  \n" + 
            "                   'TASA' AS DI#TIPOFACTOR52,  \n" + 
            "                   REPLACE(TO_CHAR(FAU.IMPORTES_IVA,'fm00000000000000.000000'),'.') AS DI#IMPORTE52  \n" + 
            "           FROM ICO_INV_UNIDADES UNI INNER JOIN ICO_INV_FACTURAS_MAYOREO MAY  \n" + 
            "           ON UNI.UNID_VVIN_PK = MAY.UNID_VVIN_PK  \n" + 
            "           INNER JOIN ICO_FAU_FACTURAS_MAYOREO FAU  \n" + 
            "           ON MAY.UNID_VVIN_PK = FAU.VIN_PK  \n" + 
            "           INNER JOIN ICO_INV_CPOLITICAS_VTA POL  \n" + 
            "           ON UNI.POL_VID = POL.POL_VID_PK  \n" + 
            "           INNER JOIN ICO_FAU_CDISTRIBUIDORES DIS  \n" + 
            "           ON UNI.DIST_VID_MATRIZ = DIS.DIST_VID_PK\n" + 
            "           INNER JOIN ICO_INV_CESTADOS EDO\n" + 
            "           ON DIS.DIST_EDO = EDO.EDO_NID_PK\n" + 
            "           WHERE UNI.ORIG_VID = 'D'";
            break;
        case 2:
            sql="SELECT  UNI.UNID_VVIN_PK AS VIN,          \n" + 
            "                   MAY.umay_nnumero_factura AS HD1#FACTURA53,  \n" + 
            "                   'F' AS HD1#TIPOPEDIDO53,  \n" + 
            "                   TO_CHAR(FAU.FECHA_FACTURA,'YYYYMMDD') AS HD1#FECHAFACTURA53,  \n" + 
            "                   TO_CHAR(FAU.FECHA_FACTURA,'HH24MISS') AS HD1#HORAFACTURA53,  \n" + 
            "                   POL.POL_VDESCRIPCION AS HD1#DESCFACT53,  \n" + 
            "                   FAU.PREFIJO_FACTURA AS HD1#SERIE53,  \n" + 
            "                   'MXN' AS HD1#MONEDA53,  \n" + 
            "                   '000000017892' AS HD1#NUMAPROBACION53,  \n" + 
            "                   '2007' AS HD1#ANOAPROBACION53,  \n" + 
            "                   '1600' AS HD1#PORCENTAJEIVA53,  \n" + 
            "                   'PPD' AS HD1#FORMAPAGO53,  \n" + 
            "                   'MEXICO, CIUDAD DE MEXICO' AS HD1#LUGAREXPEDICION53,  \n" + 
            "                  'NO IDENTIFICADO' AS HD1#NUMCTAPAGO53,  \n" + 
            "                   '601' AS HD1#REGIMEN53,  \n" + 
            "                   '99' AS HD1#METODODEPAGO53,  \n" + 
            "                   '3.3' AS HD1#VERSION53,  \n" + 
            "                   'I' AS HD1#TIPOCOMPROBANTE53,  \n" + 
            "                   LPAD(UNI.DIST_NID, 6, '0') AS DC#CVECLIENTE54,   \n" + 
            "                   DIS.DIST_VRAZON_SOCIAL AS DC#DCRAZONSOCIAL54,  \n" + 
            "                   DIS.DIST_VRFC AS DC#DCRFC54,  \n" + 
            "                   DIS.DIST_VDIRECCION AS DC#DCDIRECCION154,  \n" + 
            "                   DIS.DIST_VPOBLACION AS DC#DCDIRECCION254,  \n" + 
            "                   EDO.EDO_VDESCRIPCION AS DC#DCDIRECCION354,  \n" + 
            "                   'C.P. '||DIS.DIST_NCODIGO_POSTAL AS DC#DCDIRECCION554,  \n" + 
            "                   'G01' AS DC#USOCFDI54,  \n" + 
            "                   PED.UPEDIM_VNUMERO_PEDIMENTO AS EI#NUMEROPEDIMIENTO55,  \n" + 
            "                   ADU.ADU_VDESCRIPCION AS EI#BUQUEADUANA55,  \n" + 
            "                   TO_CHAR(PED.UPEDIM_DFECHA_PEDIMENTO,'YYYYMMDD') AS EI#FECHAAPROXEBARQUE55,  \n" + 
            "                   POL.POL_VDESCRIPCION AS NP#ORDENCOMPRA56,  \n" + 
            "                   UNI.MOD_VEIM AS NP#MARCA56,  \n" + 
            "                   UNI.ANIO_NID AS NP#MODELO56,  \n" + 
            "                   UNI.UNID_VMOTOR AS NP#MOTOR56,  \n" + 
            "                   FAU.CAPACIDAD_MOTOR AS NP#CAPACIDADMOTOR56,  \n" + 
            "                   UNI.UNID_VVIN_PK AS NP#SERIE56,  \n" + 
            "                   FAU.COLOR_EXT AS NP#COLOR56,  \n" + 
            "                   FAU.DESCRIPCION1 AS NP#TIPO56,  \n" + 
            "                   FAU.DESCRIPCION2 AS NP#TRANSMISION56,  \n" + 
            "                   REPLACE(TO_CHAR(MAY.UMAY_NPRECIO_BASE,'fm000000000.0000'),'.') AS NP#IMPORTELINMONNAL56,  \n" + 
            "                   FAU.CLAVE_VEHICULAR AS NP#CVEVEHICULAR56,  \n" + 
            "                   REPLACE(TO_CHAR(MAY.UMAY_NPRECIO_BASE,'fm000000000.0000'),'.') AS NP#PRECIOUNITARIOMONNAL56,  \n" + 
            "                   'PIEZA' AS NP#UNIDAD56,  \n" + 
            "                   'H87' AS NP#CLAVEUNIDAD56,  \n" + 
            "                   '25101500' AS NP#CLAVEPRODSERV56,  \n" + 
            "                   REPLACE(TO_CHAR(MAY.UMAY_NPRECIO_BASE, 'fm00000000000000.000000'),'.') AS NP#BASEIMPUESTOTRASLRASLADO56,  \n" + 
            "                   '002' AS NP#IMPUESTOTRASLADO56,  \n" + 
            "                   'TASA' AS NP#TIPOFACTORTRASLADADO56,  \n" + 
            "                   '0000160000' AS NP#TASAOCUOTATRASLADO56,  \n" + 
            "                   REPLACE(TO_CHAR(ROUND((MAY.UMAY_NPRECIO_BASE * .16),2), 'fm00000000000000.000000'),'.') AS NP#IMPORTETRASLADO56,  \n" + 
            "                   PED.UPEDIM_VNUMERO_PEDIMENTO AS LT#NUMEROADUANA57,  \n" + 
            "                   TO_CHAR(PED.UPEDIM_DFECHA_PEDIMENTO,'YYYYMMDD') AS LT#FECHAADUANA57,  \n" + 
            "                   ADU.ADU_VDESCRIPCION AS LT#NOMBREADUANA57,  \n" + 
            "                   '0000' AS NP#CAPACIDADMOTOR58,  \n" + 
            "                   'TRASLADO' AS NP#TIPO58,  \n" + 
            "                   REPLACE(TO_CHAR(MAY.UMAY_NTRASLADO,'fm000000000.0000'),'.') AS NP#IMPORTELINMONNAL58,  \n" + 
            "                   REPLACE(TO_CHAR(MAY.UMAY_NTRASLADO,'fm000000000.0000'),'.') AS NP#PRECIOUNITARIOMONNAL58,  \n" + 
            "                   'PIEZA' AS NP#UNIDAD58,  \n" + 
            "                   'E48' AS NP#CLAVEUNIDAD58,  \n" + 
            "                   '01010101' AS NP#CLAVEPRODSERV58,  \n" + 
            "                   REPLACE(TO_CHAR(MAY.UMAY_NTRASLADO, 'fm00000000000000.000000'),'.') AS NP#BASEIMPUESTOTRASLRASLADO58,  \n" + 
            "                   '002' AS NP#IMPUESTOTRASLADO58,  \n" + 
            "                   'TASA' AS NP#TIPOFACTORTRASLADADO58,  \n" + 
            "                   '0000160000' AS NP#TASAOCUOTATRASLADO58,  \n" + 
            "                   REPLACE(TO_CHAR(ROUND((MAY.UMAY_NTRASLADO * .16),2), 'fm00000000000000.000000'),'.') AS NP#IMPORTETRASLADO58,          \n" + 
            "                   '0000' AS NP#CAPACIDADMOTOR59,  \n" + 
            "                   'SEGURO TRASLADAD' AS NP#TIPO59,  \n" + 
            "                   REPLACE(TO_CHAR(MAY.UMAY_NSEGURO,'fm000000000.0000'),'.') AS NP#IMPORTELINMONNAL59,  \n" + 
            "                   REPLACE(TO_CHAR(MAY.UMAY_NSEGURO,'fm000000000.0000'),'.') AS NP#PRECIOUNITARIOMONNAL59,  \n" + 
            "                   'PIEZA' AS NP#UNIDAD59,  \n" + 
            "                   'E48' AS NP#CLAVEUNIDAD59,  \n" + 
            "                  '01010101' AS NP#CLAVEPRODSERV59,  \n" + 
            "                   REPLACE(TO_CHAR(MAY.UMAY_NSEGURO, 'fm00000000000000.000000'),'.') AS NP#BASEIMPUESTOTRASLRASLADO59,  \n" + 
            "                   '002' AS NP#IMPUESTOTRASLADO59,  \n" + 
            "                   'TASA' AS NP#TIPOFACTORTRASLADADO59,  \n" + 
            "                   '0000160000' AS NP#TASAOCUOTATRASLADO59,  \n" + 
            "                   REPLACE(TO_CHAR(ROUND((MAY.UMAY_NSEGURO * .16),2), 'fm00000000000000.000000'),'.') AS NP#IMPORTETRASLADO59,          \n" + 
            "                   '0000' AS NP#CAPACIDADMOTOR60,  \n" + 
            "                   'PROMOCION PUBLIC' AS NP#TIPO60,  \n" + 
            "                   REPLACE(TO_CHAR(MAY.UMAY_NPROMOCION_PUBLICITARIA,'fm000000000.0000'),'.') AS NP#IMPORTELINMONNAL60,  \n" + 
            "                   REPLACE(TO_CHAR(MAY.UMAY_NPROMOCION_PUBLICITARIA,'fm000000000.0000'),'.') AS NP#PRECIOUNITARIOMONNAL60,  \n" + 
            "                   'PIEZA' AS NP#UNIDAD60,  \n" + 
            "                   'E48' AS NP#CLAVEUNIDAD60,  \n" + 
            "                   '01010101' AS NP#CLAVEPRODSERV60,  \n" + 
            "                   REPLACE(TO_CHAR(MAY.UMAY_NPROMOCION_PUBLICITARIA, 'fm00000000000000.000000'),'.') AS NP#BASEIMPUESTOTRASLRASLADO60,  \n" + 
            "                   '002' AS NP#IMPUESTOTRASLADO60,  \n" + 
            "                   'TASA' AS NP#TIPOFACTORTRASLADADO60,  \n" + 
            "                   '0000160000' AS NP#TASAOCUOTATRASLADO60,  \n" + 
            "                   REPLACE(TO_CHAR(ROUND((MAY.UMAY_NPROMOCION_PUBLICITARIA * .16),2), 'fm00000000000000.000000'),'.') AS NP#IMPORTETRASLADO60,  \n" + 
            "                   '0000' AS NP#CAPACIDADMOTOR61,  \n" + 
            "                   'ASISTENCIA VIAL' AS NP#TIPO61,  \n" + 
            "                   REPLACE(TO_CHAR(MAY.UMAY_NASISTENCIA_VIAL,'fm000000000.0000'),'.') AS NP#IMPORTELINMONNAL61,  \n" + 
            "                   REPLACE(TO_CHAR(MAY.UMAY_NASISTENCIA_VIAL,'fm000000000.0000'),'.') AS NP#PRECIOUNITARIOMONNAL61,  \n" + 
            "                   'PIEZA' AS NP#UNIDAD61,  \n" + 
            "                   'E48' AS NP#CLAVEUNIDAD61,  \n" + 
            "                   '01010101' AS NP#CLAVEPRODSERV61,  \n" + 
            "                   REPLACE(TO_CHAR(MAY.UMAY_NASISTENCIA_VIAL, 'fm00000000000000.000000'),'.') AS NP#BASEIMPUESTOTRASLRASLADO61,  \n" + 
            "                   '002' AS NP#IMPUESTOTRASLADO61,  \n" + 
            "                   'TASA' AS NP#TIPOFACTORTRASLADADO61,  \n" + 
            "                   '0000160000' AS NP#TASAOCUOTATRASLADO61,  \n" + 
            "                   REPLACE(TO_CHAR(ROUND((MAY.UMAY_NASISTENCIA_VIAL * .16),2), 'fm00000000000000.000000'),'.') AS NP#IMPORTETRASLADO61,  \n" + 
            "                   '0000' AS NP#CAPACIDADMOTOR62,  \n" + 
            "                   'AMDA' AS NP#TIPO62,  \n" + 
            "                   REPLACE(TO_CHAR(MAY.UMAY_NAMDA,'fm000000000.0000'),'.') AS NP#IMPORTELINMONNAL62,  \n" + 
            "                   REPLACE(TO_CHAR(MAY.UMAY_NAMDA,'fm000000000.0000'),'.') AS NP#PRECIOUNITARIOMONNAL62,  \n" + 
            "                   'PIEZA' AS NP#UNIDAD62,  \n" + 
            "                   'E48' AS NP#CLAVEUNIDAD62,  \n" + 
            "                   '01010101' AS NP#CLAVEPRODSERV62,  \n" + 
            "                   '1.1' AS TC#VERSION63,  \n" + 
            "                   'AND660531FB8' AS TC#RFC63,  \n" + 
            "                   'ASOCIACIÓN NACIONAL DE DISTRIBUIDORES DE AUTOMÓVILES NISSAN, A.C.' AS TC#NOMBRE63,  \n" + 
            "                   'ADOLFO PRIETO' AS TI#CALLE64,  \n" + 
            "                   '624' AS TI#NOEXTERIOR64,  \n" + 
            "                   'DEL VALLE' AS TI#COLONIA64,  \n" + 
            "                   'DELEGACION BENITO JUAREZ' AS TI#MUNICIPIO64,  \n" + 
            "                   'CIUDAD DE MEXICO' AS TI#ESTADO64,  \n" + 
            "                   'MEXICO' AS TI#PAIS64,  \n" + 
            "                   '03100' AS TI#CODIGOPOSTAL64,  \n" + 
            "                   '1' AS TX#CANTIDAD65,  \n" + 
            "                   'AMDA' AS TX#DESCRIPCION65,  \n" + 
            "                   REPLACE(TO_CHAR(MAY.UMAY_NAMDA,'fm00000000000000.000000'),'.') AS TX#VALORUNITARIO65,  \n" + 
            "                   REPLACE(TO_CHAR(MAY.UMAY_NAMDA,'fm00000000000000.000000'),'.') AS TX#IMPORTE65,  \n" + 
            "                   '0000' AS NP#CAPACIDADMOTOR66,  \n" + 
            "                   'ANDANAC' AS NP#TIPO66,  \n" + 
            "                   REPLACE(TO_CHAR(MAY.UMAY_NANDANAC,'fm000000000.0000'),'.') AS NP#IMPORTELINMONNAL66,  \n" + 
            "                   REPLACE(TO_CHAR(MAY.UMAY_NANDANAC,'fm000000000.0000'),'.') AS NP#PRECIOUNITARIOMONNAL66,  \n" + 
            "                   'PIEZA' AS NP#UNIDAD66,  \n" + 
            "                   'E48' AS NP#CLAVEUNIDAD66,  \n" + 
            "                   '01010101' AS NP#CLAVEPRODSERV66,  \n" + 
            "                   '1.1' AS TC#VERSION66,  \n" + 
            "                   'AND660531FB8' AS TC#RFC67,  \n" + 
            "                   'ASOCIACIÓN NACIONAL DE DISTRIBUIDORES DE AUTOMÓVILES NISSAN, A.C.' AS TC#NOMBRE67,  \n" + 
            "                   'ADOLFO PRIETO' TI#CALLE68,  \n" + 
            "                   '624' AS TI#NOEXTERIOR68,  \n" + 
            "                   'DEL VALLE' AS TI#COLONIA68,  \n" + 
            "                   'DELEGACION BENITO JUAREZ' AS TI#MUNICIPIO68,  \n" + 
            "                   'CIUDAD DE MEXICO' AS TI#ESTADO68,  \n" + 
            "                   'MEXICO' AS TI#PAIS68,  \n" + 
            "                   '03100' AS TI#CODIGOPOSTAL68,  \n" + 
            "                   '1' AS TX#CANTIDAD69,  \n" + 
            "                   'ANDANAC' AS TX#DESCRIPCION69,  \n" + 
            "                   REPLACE(TO_CHAR(MAY.UMAY_NANDANAC,'fm00000000000000.000000'),'.') AS TX#VALORUNITARIO69,  \n" + 
            "                   REPLACE(TO_CHAR(MAY.UMAY_NANDANAC,'fm00000000000000.000000'),'.') AS TX#IMPORTE69,  \n" + 
            "                   '0000' AS NP#CAPACIDADMOTOR70,  \n" + 
            "                   'PROMEI' AS NP#TIPO70,  \n" + 
            "                   REPLACE(TO_CHAR(FAU.PROMEI,'fm000000000.0000'),'.') AS NP#IMPORTELINMONNAL70,  \n" + 
            "                   REPLACE(TO_CHAR(FAU.PROMEI,'fm000000000.0000'),'.') AS NP#PRECIOUNITARIOMONNAL70,  \n" + 
            "                   'PIEZA' AS NP#UNIDAD70,  \n" + 
            "                   'E48' AS NP#CLAVEUNIDAD70,  \n" + 
            "                   '01010101' AS NP#CLAVEPRODSERV70,  \n" + 
            "                   'AND660531FB8' AS TC#RFC71,  \n" + 
            "                   'ASOCIACIÓN NACIONAL DE DISTRIBUIDORES DE AUTOMÓVILES NISSAN, A.C.' AS TC#NOMBRE71,  \n" + 
            "                   'ADOLFO PRIETO' TI#CALLE72,  \n" + 
            "                   '624' AS TI#NOEXTERIOR72,  \n" + 
            "                   'DEL VALLE' AS TI#COLONIA72,  \n" + 
            "                   'DELEGACION BENITO JUAREZ' AS TI#MUNICIPIO72,  \n" + 
            "                   'CIUDAD DE MEXICO' AS TI#ESTADO72,  \n" + 
            "                   'MEXICO' AS TI#PAIS72,  \n" + 
            "                   '03100' AS TI#CODIGOPOSTAL72,          \n" + 
            "                   '1' AS TX#CANTIDAD73,  \n" + 
            "                   'PROMEI' AS TX#DESCRIPCION73,  \n" + 
            "                   REPLACE(TO_CHAR(FAU.PROMEI,'fm00000000000000.000000'),'.') AS TX#VALORUNITARIO73,  \n" + 
            "                   REPLACE(TO_CHAR(FAU.PROMEI,'fm00000000000000.000000'),'.') AS TX#IMPORTE73,  \n" + 
            "                   '0000' AS NP#CAPACIDADMOTOR74,  \n" + 
            "                   'SEGURO PLAN PISO' AS NP#TIPO74,  \n" + 
            "                   REPLACE(TO_CHAR(FAU.SEG_PLAN_PISO,'fm000000000.0000'),'.') AS NP#IMPORTELINMONNAL74,  \n" + 
            "                   REPLACE(TO_CHAR(FAU.SEG_PLAN_PISO,'fm000000000.0000'),'.') AS NP#PRECIOUNITARIOMONNAL74,  \n" + 
            "                   'PIEZA' AS NP#UNIDAD74,  \n" + 
            "                   'E48' AS NP#CLAVEUNIDAD74,  \n" + 
            "                   '01010101' AS NP#CLAVEPRODSERV74,  \n" + 
            "                   'AND660531FB8' AS TC#RFC75,  \n" + 
            "                   'ASOCIACIÓN NACIONAL DE DISTRIBUIDORES DE AUTOMÓVILES NISSAN, A.C.' AS TC#NOMBRE75,  \n" + 
            "                   'ADOLFO PRIETO' TI#CALLE76,  \n" + 
            "                   '624' AS TI#NOEXTERIOR76,  \n" + 
            "                   'DEL VALLE' AS TI#COLONIA76,  \n" + 
            "                   'DELEGACION BENITO JUAREZ' AS TI#MUNICIPIO76,  \n" + 
            "                   'CIUDAD DE MEXICO' AS TI#ESTADO76,  \n" + 
            "                   'MEXICO' AS TI#PAIS76,  \n" + 
            "                   '03100' AS TI#CODIGOPOSTAL76,  \n" + 
            "                   '1' AS TX#CANTIDAD77,  \n" + 
            "                   'SEGURO PLAN PISO' AS TX#DESCRIPCION77,  \n" + 
            "                   REPLACE(TO_CHAR(FAU.SEG_PLAN_PISO,'fm00000000000000.000000'),'.') AS TX#VALORUNITARIO77,  \n" + 
            "                   REPLACE(TO_CHAR(FAU.SEG_PLAN_PISO,'fm00000000000000.000000'),'.') AS TX#IMPORTE77,  \n" + 
            "                   '0000' AS NP#CAPACIDADMOTOR78,  \n" + 
            "                   'AYUDA-SOCIAL' AS NP#TIPO78,  \n" + 
            "                   REPLACE(TO_CHAR(MAY.UMAY_NAYUDA_SOCIAL,'fm000000000.0000'),'.') AS NP#IMPORTELINMONNAL78,  \n" + 
            "                   REPLACE(TO_CHAR(MAY.UMAY_NAYUDA_SOCIAL,'fm000000000.0000'),'.') AS NP#PRECIOUNITARIOMONNAL78,  \n" + 
            "                   'PIEZA' AS NP#UNIDAD78,  \n" + 
            "                   'E48' AS NP#CLAVEUNIDAD78,  \n" + 
            "                   '01010101' AS NP#CLAVEPRODSERV78,  \n" + 
            "                   'AND660531FB8' AS TC#RFC79,  \n" + 
            "                   'ASOCIACIÓN NACIONAL DE DISTRIBUIDORES DE AUTOMÓVILES NISSAN, A.C.' AS TC#NOMBRE79,  \n" + 
            "                   'ADOLFO PRIETO' TI#CALLE80,  \n" + 
            "                   '624' AS TI#NOEXTERIOR80,  \n" + 
            "                   'DEL VALLE' AS TI#COLONIA80,  \n" + 
            "                   'DELEGACION BENITO JUAREZ' AS TI#MUNICIPIO80,  \n" + 
            "                   'CIUDAD DE MEXICO' AS TI#ESTADO80,  \n" + 
            "                   'MEXICO' AS TI#PAIS80,  \n" + 
            "                   '03100' AS TI#CODIGOPOSTAL80,          \n" + 
            "                   '1' AS TX#CANTIDAD81,  \n" + 
            "                   'AYUDA-SOCIAL' AS TX#DESCRIPCION81,  \n" + 
            "                   REPLACE(TO_CHAR(MAY.UMAY_NAYUDA_SOCIAL,'fm00000000000000.000000'),'.') AS TX#VALORUNITARIO81,  \n" + 
            "                   REPLACE(TO_CHAR(MAY.UMAY_NAYUDA_SOCIAL,'fm00000000000000.000000'),'.') AS TX#IMPORTE81,  \n" + 
            "                   TO_CHAR(PED.UPEDIM_DFECHA_PEDIMENTO,'YY')||'  '||PED.UPEDIM_NNUMERO_ADUANA||'  '||SUBSTR(PED.UPEDIM_VNUMERO_PEDIMENTO,0,4)||'  '||SUBSTR(PED.UPEDIM_VNUMERO_PEDIMENTO,5,10) AS LG#PEDIMENTO82,  \n" + 
            "                   'PRECIO UNITARIO' AS GT#CONCEPTOPRECIOBASE83,  \n" + 
            "                   REPLACE(TO_CHAR(FAU.IMPORTES_BASE_IVA,'fm0000000000.0000'),'.') AS GT#PRECIOBASE83,  \n" + 
            "                   REPLACE(TO_CHAR(FAU.IMPORTES_BASE_IVA,'fm0000000000.0000'),'.') AS GT#PRECIOBASEMONNAL83,  \n" + 
            "                   'SUBTOTAL' AS GT#DSCSUBTOTAL83,  \n" + 
            "                   REPLACE(TO_CHAR(FAU.SUBTOTAL,'fm0000000000.0000'),'.')  AS GT#SUBTOTAL83,  \n" + 
            "                   REPLACE(TO_CHAR(FAU.SUBTOTAL,'fm0000000000.0000'),'.') AS GT#SUBTOTALMONNAL83,  \n" + 
            "                   REPLACE(TO_CHAR(MAY.UMAY_NPRECIO_TOTAL,'fm0000000000.0000'),'.') AS GT#PRECIOTOTMONNAL83,  \n" + 
            "                   UPPER(expresar_en_letras.numero_a_letras( MAY.UMAY_NPRECIO_TOTAL )) AS GT#CANTLETRA83,  \n" + 
            "                   'TOTAL DE CUOTAS' AS GT#CONCEPTOTOTALCUOTAS83,  \n" + 
            "                   REPLACE(TO_CHAR(FAU.TOTAL_CUOTAS,'fm0000000000.0000'),'.') AS GT#IMPORTEMONNAL83,  \n" + 
            "                   REPLACE(TO_CHAR(FAU.IMPORTES_IVA,'fm00000000000000.000000'),'.') AS GT#TOTALIMPUESTOSTRASLADADO83,  \n" + 
            "                   'TRASLADO' AS G1#DCSIVA84,  \n" + 
            "                   REPLACE(TO_CHAR(MAY.UMAY_NTRASLADO,'fm0000000000.0000'),'.') AS G1#IVAMONNAL84,  \n" + 
            "                   'SEGURO TRASLADO' AS G1#DCSIVA85,  \n" + 
            "                   REPLACE(TO_CHAR(MAY.UMAY_NSEGURO,'fm0000000000.0000'),'.') AS G1#IVAMONNAL85,  \n" + 
            "                   'PROMOCION PUBLICITARIA' AS  G1#DCSIVA86,  \n" + 
            "                   REPLACE(TO_CHAR(MAY.UMAY_NPROMOCION_PUBLICITARIA,'fm0000000000.0000'),'.') AS G1#IVAMONNAL86,  \n" + 
            "                   'ASISTENCIA VIAL' AS G1#DCSIVA87,  \n" + 
            "                   REPLACE(TO_CHAR(MAY.UMAY_NASISTENCIA_VIAL,'fm0000000000.0000'),'.') AS G1#IVAMONNAL87,  \n" + 
            "                   'BASE GRAVABLE' AS G1#DCSIVA88,  \n" + 
            "                   REPLACE(TO_CHAR(FAU.IMPORTES_BASE_IVA,'fm0000000000.0000'),'.') AS G1#IVAMONNAL88,  \n" + 
            "                   'I.V.A.'AS G1#DCSIVA89,  \n" + 
            "                   REPLACE(TO_CHAR(FAU.IMPORTES_IVA,'fm0000000000.0000'),'.') AS G1#IVAMONNAL89,  \n" + 
            "                   'LOS SIGUIENTES CARGOS SON  POR CUENTA Y ORDEN' AS G2#LEYENDAPRECIOSIMP90,  \n" + 
            "                   'DE LA ASOCIACION  NACIONAL DE  DISTRIBUIDORES' AS G2#LEYENDAPRECIOSIMP91,  \n" + 
            "                   'DE AUTOMOVILES NISSAN A. C.' AS G2#LEYENDAPRECIOSIMP92,  \n" + 
            "                   '.' AS G2#LEYENDAPRECIOSIMP93,  \n" + 
            "                   'POR LO  TANTO LOS COMPROBANTES CON REQUISITOS' AS G2#LEYENDAPRECIOSIMP94,  \n" + 
            "                   'FISCALES QUE AMPARAN EL COBRO DE ESTAS CUOTAS' AS G2#LEYENDAPRECIOSIMP95,  \n" + 
            "                   'SERAN  EMITIDOS  POR  LOS  BENEFICIARIOS  DEL' AS G2#LEYENDAPRECIOSIMP96,  \n" + 
            "                   'INGRESO. EN CONSECUENCIA, ESTE DOCUMENTO  NO' AS G2#LEYENDAPRECIOSIMP97,  \n" + 
            "                   'CONSTITUYE  COMPROBANTE  FISCAL DEL  PAGO  DE' AS G2#LEYENDAPRECIOSIMP98,  \n" + 
            "                   'DICHAS  CUOTAS EN LOS TERMINOS DEL ART.29 DEL' AS G2#LEYENDAPRECIOSIMP99,  \n" + 
            "                   'CODIGO FISCAL DE LA FEDERACION.' AS G2#LEYENDAPRECIOSIMP100,  \n" + 
            "                   '' AS G2#LEYENDAPRECIOSIMP101,  \n" + 
            "                   'AMDA' AS G4#GTOSGRALPIE102,  \n" + 
            "                   REPLACE(TO_CHAR(MAY.UMAY_NAMDA,'fm0000000000.0000'),'.') AS G4#IMPTEGTOPIEMONNAL102,  \n" + 
            "                   'ANDANAC' AS G4#GTOSGRALPIE103,  \n" + 
            "                   REPLACE(TO_CHAR(MAY.UMAY_NANDANAC,'fm0000000000.0000'),'.') AS G4#IMPTEGTOPIEMONNAL103,          \n" + 
            "                   'PROMEI' AS G4#GTOSGRALPIE104,  \n" + 
            "                   REPLACE(TO_CHAR(FAU.PROMEI,'fm0000000000.0000'),'.') AS G4#IMPTEGTOPIEMONNAL104,          \n" + 
            "                   'SEGURO PLAN PISO' AS G4#GTOSGRALPIE105,  \n" + 
            "                   REPLACE(TO_CHAR(FAU.SEG_PLAN_PISO,'fm0000000000.0000'),'.') AS G4#IMPTEGTOPIEMONNAL105,          \n" + 
            "                   'AYUDA SOCIAL' AS G4#GTOSGRALPIE106,  \n" + 
            "                   REPLACE(TO_CHAR(MAY.UMAY_NAYUDA_SOCIAL,'fm0000000000.0000'),'.') AS G4#IMPTEGTOPIEMONNAL106,  \n" + 
            "                   'TRASLADADO' AS DI#TIPOIMPUESTO107,  \n" + 
            "                   '002' AS DI#CONCEPTOIMPUESTO107,  \n" + 
            "                   '0000160000' AS DI#TASAOCUOTA107,  \n" + 
            "                   'TASA' AS DI#TIPOFACTOR107,  \n" + 
            "                   REPLACE(TO_CHAR(FAU.IMPORTES_IVA,'fm00000000000000.000000'),'.') AS DI#IMPORTE107  \n" + 
            "           FROM ICO_INV_UNIDADES UNI INNER JOIN ICO_INV_FACTURAS_MAYOREO MAY  \n" + 
            "           ON UNI.UNID_VVIN_PK = MAY.UNID_VVIN_PK  \n" + 
            "           INNER JOIN ICO_FAU_FACTURAS_MAYOREO FAU  \n" + 
            "           ON MAY.UNID_VVIN_PK = FAU.VIN_PK  \n" + 
            "           INNER JOIN ICO_INV_CPOLITICAS_VTA POL  \n" + 
            "           ON UNI.POL_VID = POL.POL_VID_PK  \n" + 
            "           INNER JOIN ICO_FAU_CDISTRIBUIDORES DIS  \n" + 
            "           ON UNI.DIST_VID_MATRIZ = DIS.DIST_VID_PK  \n" + 
            "           INNER JOIN ICO_INV_CMODELOS MO  \n" + 
            "           ON UNI.MOD_VEIM = MO.MOD_VEIM_PK  \n" + 
            "           AND UNI.ANIO_NID = MO.ANIO_NID_PK  \n" + 
            "           INNER JOIN ICO_INV_PEDIMENTOS PED  \n" + 
            "           ON UNI.UNID_VVIN_PK = PED.UNID_VVIN_PK  \n" + 
            "           INNER JOIN ICO_NIS_CADUANAS ADU  \n" + 
            "           ON PED.UPEDIM_NNUMERO_ADUANA = ADU.ADU_NID_PK\n" + 
            "           INNER JOIN ICO_INV_CESTADOS EDO\n" + 
            "           ON DIS.DIST_EDO = EDO.EDO_NID_PK  \n" + 
            "           WHERE UNI.ORIG_VID = 'I'  \n" + 
            "           AND MO.SECT_NID = 10";
            break;
        case 3:
            sql="SELECT  UNI.UNID_VVIN_PK AS VIN,          \n" + 
            "                   MAY.umay_nnumero_factura AS HD1#FACTURA108,  \n" + 
            "                   'F' AS HD1#TIPOPEDIDO108,  \n" + 
            "                   TO_CHAR(FAU.FECHA_FACTURA,'YYYYMMDD') AS HD1#FECHAFACTURA108,  \n" + 
            "                   TO_CHAR(FAU.FECHA_FACTURA,'HH24MISS') AS HD1#HORAFACTURA108,  \n" + 
            "                   POL.POL_VDESCRIPCION AS HD1#DESCFACT108,  \n" + 
            "                   FAU.PREFIJO_FACTURA AS HD1#SERIE108,  \n" + 
            "                   'MXN' AS HD1#MONEDA108,  \n" + 
            "                   '000000017892' AS HD1#NUMAPROBACION108,  \n" + 
            "                   '2007' AS HD1#ANOAPROBACION108,  \n" + 
            "                   '1600' AS HD1#PORCENTAJEIVA108,  \n" + 
            "                   'PPD' AS HD1#FORMAPAGO108,  \n" + 
            "                   'MEXICO, CIUDAD DE MEXICO' AS HD1#LUGAREXPEDICION108,  \n" + 
            "                   'NO IDENTIFICADO' AS HD1#NUMCTAPAGO108,  \n" + 
            "                   '601' AS HD1#REGIMEN108,  \n" + 
            "                   '99' AS HD#METODODEPAGO108,  \n" + 
            "                   '3.3' AS HD1#VERSION108,  \n" + 
            "                   'I' AS HD1#TIPOCOMPROBANTE108,  \n" + 
            "                   LPAD(UNI.DIST_NID, 6, '0') AS DC#CVECLIENTE109,   \n" + 
            "                   DIS.DIST_VRAZON_SOCIAL AS DC#DCRAZONSOCIAL109,  \n" + 
            "                   DIS.DIST_VRFC AS DC#DCRFC109,  \n" + 
            "                   DIS.DIST_VDIRECCION AS DC#DCDIRECCION1109,  \n" + 
            "                   DIS.DIST_VPOBLACION AS DC#DCDIRECCION2109,  \n" + 
            "                   EDO.EDO_VDESCRIPCION AS DC#DCDIRECCION3109,  \n" + 
            "                   'C.P. '||DIS.DIST_NCODIGO_POSTAL AS DC#DCDIRECCION5109,  \n" + 
            "                   'G01' AS DC#USOCFDI109,  \n" + 
            "                   PED.UPEDIM_VNUMERO_PEDIMENTO AS EI#NUMEROPEDIMIENTO110,  \n" + 
            "                   ADU.ADU_VDESCRIPCION AS EI#BUQUEADUANA110,  \n" + 
            "                   TO_CHAR(PED.UPEDIM_DFECHA_PEDIMENTO,'YYYYMMDD') AS EI#FECHAAPROXEBARQUE110,                      \n" + 
            "                   POL.POL_VDESCRIPCION AS NP#ORDENCOMPRA111,  \n" + 
            "                   UNI.MOD_VEIM AS NP#MARCA111,  \n" + 
            "                   UNI.ANIO_NID AS NP#MODELO111,  \n" + 
            "                   UNI.UNID_VMOTOR AS NP#MOTOR111,  \n" + 
            "                   FAU.CAPACIDAD_MOTOR AS NP#CAPACIDADMOTOR111,  \n" + 
            "                   UNI.UNID_VVIN_PK AS NP#SERIE111,  \n" + 
            "                   FAU.COLOR_EXT AS NP#COLOR111,  \n" + 
            "                   FAU.DESCRIPCION1 AS NP#TIPO111,  \n" + 
            "                   FAU.DESCRIPCION2 AS NP#TRANSMISION111,  \n" + 
            "                   REPLACE(TO_CHAR(MAY.UMAY_NPRECIO_BASE,'fm000000000.0000'),'.') AS NP#IMPORTELINMONNAL111,  \n" + 
            "                   FAU.CLAVE_VEHICULAR AS NP#CVEVEHICULAR111,  \n" + 
            "                   REPLACE(TO_CHAR(MAY.UMAY_NPRECIO_BASE,'fm000000000.0000'),'.') AS NP#PRECIOUNITARIOMONNAL111,  \n" + 
            "                   'PIEZA' AS NP#UNIDAD111,  \n" + 
            "                   'H87' AS NP#CLAVEUNIDAD111,  \n" + 
            "                   '25101500' AS NP#CLAVEPRODSERV111,  \n" + 
            "                   REPLACE(TO_CHAR(MAY.UMAY_NPRECIO_BASE, 'fm00000000000000.000000'),'.') AS NP#BASEIMPUESTOTRASLRASLADO111,  \n" + 
            "                   '002' AS NP#IMPUESTOTRASLADO111,  \n" + 
            "                   'TASA' AS NP#TIPOFACTORTRASLADADO111,  \n" + 
            "                   '0000160000' AS NP#TASAOCUOTATRASLADO111,  \n" + 
            "                   REPLACE(TO_CHAR(ROUND((MAY.UMAY_NPRECIO_BASE * .16),2), 'fm00000000000000.000000'),'.') AS NP#IMPORTETRASLADO111,  \n" + 
            "                   PED.UPEDIM_VNUMERO_PEDIMENTO AS LT#NUMEROADUANA112,  \n" + 
            "                   TO_CHAR(PED.UPEDIM_DFECHA_PEDIMENTO,'YYYYMMDD') AS LT#FECHAADUANA112,  \n" + 
            "                   ADU.ADU_VDESCRIPCION AS LT#NOMBREADUANA112,  \n" + 
            "                   '0000' AS NP#CAPACIDADMOTOR113,  \n" + 
            "                   'TRASLADO' AS NP#TIPO113,  \n" + 
            "                   REPLACE(TO_CHAR(MAY.UMAY_NTRASLADO,'fm000000000.0000'),'.') AS NP#IMPORTELINMONNAL113,  \n" + 
            "                  REPLACE(TO_CHAR(MAY.UMAY_NTRASLADO,'fm000000000.0000'),'.') AS NP#PRECIOUNITARIOMONNAL113,  \n" + 
            "                   'PIEZA' AS NP#UNIDAD113,  \n" + 
            "                   'E48' AS NP#CLAVEUNIDAD113,  \n" + 
            "                   '01010101' AS NP#CLAVEPRODSERV113,  \n" + 
            "                   REPLACE(TO_CHAR(MAY.UMAY_NTRASLADO, 'fm00000000000000.000000'),'.') AS NP#BASEIMPUESTOTRASLRASLADO113,  \n" + 
            "                   '002' AS NP#IMPUESTOTRASLADO113,  \n" + 
            "                   'TASA' AS NP#TIPOFACTORTRASLADADO113,  \n" + 
            "                   '0000160000' AS NP#TASAOCUOTATRASLADO113,  \n" + 
            "                   REPLACE(TO_CHAR(ROUND((MAY.UMAY_NTRASLADO * .16),2), 'fm00000000000000.000000'),'.') AS NP#IMPORTETRASLADO113,          \n" + 
            "                   '0000' AS NP#CAPACIDADMOTOR114,  \n" + 
            "                   'SEGURO TRASLADAD' AS NP#TIPO114,  \n" + 
            "                   REPLACE(TO_CHAR(MAY.UMAY_NSEGURO,'fm000000000.0000'),'.') AS NP#IMPORTELINMONNAL114,  \n" + 
            "                   REPLACE(TO_CHAR(MAY.UMAY_NSEGURO,'fm000000000.0000'),'.') AS NP#PRECIOUNITARIOMONNAL114,  \n" + 
            "                   'PIEZA' AS NP#UNIDAD114,  \n" + 
            "                   'E48' AS NP#CLAVEUNIDAD114,  \n" + 
            "                   '01010101' AS NP#CLAVEPRODSERV114,  \n" + 
            "                   REPLACE(TO_CHAR(MAY.UMAY_NSEGURO, 'fm00000000000000.000000'),'.') AS NP#BASEIMPUESTOTRASLRASLADO114,  \n" + 
            "                   '002' AS NP#IMPUESTOTRASLADO114,  \n" + 
            "                   'TASA' AS NP#TIPOFACTORTRASLADADO114,  \n" + 
            "                   '0000160000' AS NP#TASAOCUOTATRASLADO114,  \n" + 
            "                   REPLACE(TO_CHAR(ROUND((MAY.UMAY_NSEGURO * .16),2), 'fm00000000000000.000000'),'.') AS NP#IMPORTETRASLADO114,          \n" + 
            "                   '0000' AS NP#CAPACIDADMOTOR115,  \n" + 
            "                   'PROMOCION PUBLIC' AS NP#TIPO115,  \n" + 
            "                   REPLACE(TO_CHAR(MAY.UMAY_NPROMOCION_PUBLICITARIA,'fm000000000.0000'),'.') AS NP#IMPORTELINMONNAL115,  \n" + 
            "                   REPLACE(TO_CHAR(MAY.UMAY_NPROMOCION_PUBLICITARIA,'fm000000000.0000'),'.') AS NP#PRECIOUNITARIOMONNAL115,  \n" + 
            "                   'PIEZA' AS NP#UNIDAD115,  \n" + 
            "                   'E48' AS NP#CLAVEUNIDAD115,  \n" + 
            "                   '01010101' AS NP#CLAVEPRODSERV115,  \n" + 
            "                   REPLACE(TO_CHAR(MAY.UMAY_NPROMOCION_PUBLICITARIA, 'fm00000000000000.000000'),'.') AS NP#BASEIMPUESTOTRASLRASLADO115,  \n" + 
            "                   '002' AS NP#IMPUESTOTRASLADO115,  \n" + 
            "                   'TASA' AS NP#TIPOFACTORTRASLADADO115,  \n" + 
            "                   '0000160000' AS NP#TASAOCUOTATRASLADO115,  \n" + 
            "                   REPLACE(TO_CHAR(ROUND((MAY.UMAY_NPROMOCION_PUBLICITARIA * .16),2), 'fm00000000000000.000000'),'.') AS NP#IMPORTETRASLADO115,  \n" + 
            "                   '0000' AS NP#CAPACIDADMOTOR116,  \n" + 
            "                   'ASISTENCIA VIAL' AS NP#TIPO116,  \n" + 
            "                   REPLACE(TO_CHAR(MAY.UMAY_NASISTENCIA_VIAL,'fm000000000.0000'),'.') AS NP#IMPORTELINMONNAL116,  \n" + 
            "                   REPLACE(TO_CHAR(MAY.UMAY_NASISTENCIA_VIAL,'fm000000000.0000'),'.') AS NP#PRECIOUNITARIOMONNAL116,  \n" + 
            "                   'PIEZA' AS NP#UNIDAD116,  \n" + 
            "                   'E48' AS NP#CLAVEUNIDAD116,  \n" + 
            "                   '01010101' AS NP#CLAVEPRODSERV116,  \n" + 
            "                   REPLACE(TO_CHAR(MAY.UMAY_NASISTENCIA_VIAL, 'fm00000000000000.000000'),'.') AS NP#BASEIMPUESTOTRASLRASLADO116,  \n" + 
            "                   '002' AS NP#IMPUESTOTRASLADO116,  \n" + 
            "                   'TASA' AS NP#TIPOFACTORTRASLADADO116,  \n" + 
            "                   '0000160000' AS NP#TASAOCUOTATRASLADO116,  \n" + 
            "                  REPLACE(TO_CHAR(ROUND((MAY.UMAY_NASISTENCIA_VIAL * .16),2), 'fm00000000000000.000000'),'.') AS NP#IMPORTETRASLADO116,  \n" + 
            "                   '0000' AS NP#CAPACIDADMOTOR117,  \n" + 
            "                   'AMDA' AS NP#TIPO117,  \n" + 
            "                   REPLACE(TO_CHAR(MAY.UMAY_NAMDA,'fm000000000.0000'),'.') AS NP#IMPORTELINMONNAL117,  \n" + 
            "                   REPLACE(TO_CHAR(MAY.UMAY_NAMDA,'fm000000000.0000'),'.') AS NP#PRECIOUNITARIOMONNAL117,  \n" + 
            "                   'PIEZA' AS NP#UNIDAD117,  \n" + 
            "                   'E48' AS NP#CLAVEUNIDAD117,  \n" + 
            "                   '01010101' AS NP#CLAVEPRODSERV117,  \n" + 
            "                   '1.1' AS TC#VERSION118,  \n" + 
            "                   'AND660531FB8' AS TC#RFC118,  \n" + 
            "                   'ASOCIACIÓN NACIONAL DE DISTRIBUIDORES DE AUTOMÓVILES NISSAN, A.C.' AS TC#NOMBRE118,  \n" + 
            "                   'ADOLFO PRIETO' AS TI#CALLE119,  \n" + 
            "                   '624' AS TI#NOEXTERIOR119,  \n" + 
            "                   'DEL VALLE' AS TI#COLONIA119,  \n" + 
            "                   'DELEGACION BENITO JUAREZ' AS TI#MUNICIPIO119,  \n" + 
            "                   'CIUDAD DE MEXICO' AS TI#ESTADO119,  \n" + 
            "                   'MEXICO' AS TI#PAIS119,  \n" + 
            "                   '03100' AS TI#CODIGOPOSTAL119,  \n" + 
            "                   '1' AS TX#CANTIDAD120,  \n" + 
            "                   'AMDA' AS TX#DESCRIPCION120,  \n" + 
            "                   REPLACE(TO_CHAR(MAY.UMAY_NAMDA,'fm00000000000000.000000'),'.') AS TX#VALORUNITARIO120,  \n" + 
            "                   REPLACE(TO_CHAR(MAY.UMAY_NAMDA,'fm00000000000000.000000'),'.') AS TX#IMPORTE120,  \n" + 
            "                   '0000' AS NP#CAPACIDADMOTOR121,  \n" + 
            "                   'ANDANAC' AS NP#TIPO121,  \n" + 
            "                   REPLACE(TO_CHAR(MAY.UMAY_NANDANAC,'fm000000000.0000'),'.') AS NP#IMPORTELINMONNAL121,  \n" + 
            "                   REPLACE(TO_CHAR(MAY.UMAY_NANDANAC,'fm000000000.0000'),'.') AS NP#PRECIOUNITARIOMONNAL121,  \n" + 
            "                   'PIEZA' AS NP#UNIDAD121,  \n" + 
            "                   'E48' AS NP#CLAVEUNIDAD121,  \n" + 
            "                   '01010101' AS NP#CLAVEPRODSERV121,  \n" + 
            "                   '1.1' AS TC#VERSION121,  \n" + 
            "                   'AND660531FB8' AS TC#RFC122,  \n" + 
            "                   'ASOCIACIÓN NACIONAL DE DISTRIBUIDORES DE AUTOMÓVILES NISSAN, A.C.' AS TC#NOMBRE122,  \n" + 
            "                   'ADOLFO PRIETO' TI#CALLE123,  \n" + 
            "                   '624' AS TI#NOEXTERIOR123,  \n" + 
            "                   'DEL VALLE' AS TI#COLONIA123,  \n" + 
            "                   'DELEGACION BENITO JUAREZ' AS TI#MUNICIPIO123,  \n" + 
            "                   'CIUDAD DE MEXICO' AS TI#ESTADO123,  \n" + 
            "                   'MEXICO' AS TI#PAIS123,  \n" + 
            "                   '03100' AS TI#CODIGOPOSTAL123,  \n" + 
            "                   '1' AS TX#CANTIDAD124,  \n" + 
            "                   'ANDANAC' AS TX#DESCRIPCION124,  \n" + 
            "                   REPLACE(TO_CHAR(MAY.UMAY_NANDANAC,'fm00000000000000.000000'),'.') AS TX#VALORUNITARIO124,  \n" + 
            "                   REPLACE(TO_CHAR(MAY.UMAY_NANDANAC,'fm00000000000000.000000'),'.') AS TX#IMPORTE124,  \n" + 
            "                   '0000' AS NP#CAPACIDADMOTOR125,  \n" + 
            "                   'PROMEI' AS NP#TIPO125,  \n" + 
            "                   REPLACE(TO_CHAR(FAU.PROMEI,'fm000000000.0000'),'.') AS NP#IMPORTELINMONNAL125,  \n" + 
            "                   REPLACE(TO_CHAR(FAU.PROMEI,'fm000000000.0000'),'.') AS NP#PRECIOUNITARIOMONNAL125,  \n" + 
            "                   'PIEZA' AS NP#UNIDAD125,  \n" + 
            "                   'E48' AS NP#CLAVEUNIDAD125,  \n" + 
            "                   '01010101' AS NP#CLAVEPRODSERV125,  \n" + 
            "                   'AND660531FB8' AS TC#RFC126,  \n" + 
            "                   'ASOCIACIÓN NACIONAL DE DISTRIBUIDORES DE AUTOMÓVILES NISSAN, A.C.' AS TC#NOMBRE126,  \n" + 
            "                   'ADOLFO PRIETO' TI#CALLE127,  \n" + 
            "                   '624' AS TI#NOEXTERIOR127,  \n" + 
            "                   'DEL VALLE' AS TI#COLONIA127,  \n" + 
            "                   'DELEGACION BENITO JUAREZ' AS TI#MUNICIPIO127,  \n" + 
            "                   'CIUDAD DE MEXICO' AS TI#ESTADO127,  \n" + 
            "                   'MEXICO' AS TI#PAIS127,  \n" + 
            "                   '03100' AS TI#CODIGOPOSTAL127,          \n" + 
            "                   '1' AS TX#CANTIDAD128,  \n" + 
            "                   'PROMEI' AS TX#DESCRIPCION128,  \n" + 
            "                   REPLACE(TO_CHAR(FAU.PROMEI,'fm00000000000000.000000'),'.') AS TX#VALORUNITARIO128,  \n" + 
            "                   REPLACE(TO_CHAR(FAU.PROMEI,'fm00000000000000.000000'),'.') AS TX#IMPORTE128,  \n" + 
            "                   '0000' AS NP#CAPACIDADMOTOR129,  \n" + 
            "                   'SEGURO PLAN PISO' AS NP#TIPO129,  \n" + 
            "                   REPLACE(TO_CHAR(FAU.SEG_PLAN_PISO,'fm000000000.0000'),'.') AS NP#IMPORTELINMONNAL129,  \n" + 
            "                   REPLACE(TO_CHAR(FAU.SEG_PLAN_PISO,'fm000000000.0000'),'.') AS NP#PRECIOUNITARIOMONNAL129,  \n" + 
            "                   'PIEZA' AS NP#UNIDAD129,  \n" + 
            "                   'E48' AS NP#CLAVEUNIDAD129,  \n" + 
            "                   '01010101' AS NP#CLAVEPRODSERV129,  \n" + 
            "                   'AND660531FB8' AS TC#RFC130,  \n" + 
            "                   'ASOCIACIÓN NACIONAL DE DISTRIBUIDORES DE AUTOMÓVILES NISSAN, A.C.' AS TC#NOMBRE130,  \n" + 
            "                   'ADOLFO PRIETO' TI#CALLE131,  \n" + 
            "                   '624' AS TI#NOEXTERIOR131,  \n" + 
            "                   'DEL VALLE' AS TI#COLONIA131,  \n" + 
            "                   'DELEGACION BENITO JUAREZ' AS TI#MUNICIPIO131,  \n" + 
            "                   'CIUDAD DE MEXICO' AS TI#ESTADO131,  \n" + 
            "                   'MEXICO' AS TI#PAIS131,  \n" + 
            "                   '03100' AS TI#CODIGOPOSTAL131,  \n" + 
            "                   '1' AS TX#CANTIDAD132,  \n" + 
            "                   'SEGURO PLAN PISO' AS TX#DESCRIPCION132,  \n" + 
            "                   REPLACE(TO_CHAR(FAU.SEG_PLAN_PISO,'fm00000000000000.000000'),'.') AS TX#VALORUNITARIO132,  \n" + 
            "                   REPLACE(TO_CHAR(FAU.SEG_PLAN_PISO,'fm00000000000000.000000'),'.') AS TX#IMPORTE132,  \n" + 
            "                   '0000' AS NP#CAPACIDADMOTOR133,  \n" + 
            "                   'AYUDA-SOCIAL' AS NP#TIPO133,  \n" + 
            "                   REPLACE(TO_CHAR(MAY.UMAY_NAYUDA_SOCIAL,'fm000000000.0000'),'.') AS NP#IMPORTELINMONNAL133,  \n" + 
            "                   REPLACE(TO_CHAR(MAY.UMAY_NAYUDA_SOCIAL,'fm000000000.0000'),'.') AS NP#PRECIOUNITARIOMONNAL133,  \n" + 
            "                   'PIEZA' AS NP#UNIDAD133,  \n" + 
            "                   'E48' AS NP#CLAVEUNIDAD133,  \n" + 
            "                   '01010101' AS NP#CLAVEPRODSERV133,  \n" + 
            "                   'AND660531FB8' AS TC#RFC134,  \n" + 
            "                   'ASOCIACIÓN NACIONAL DE DISTRIBUIDORES DE AUTOMÓVILES NISSAN, A.C.' AS TC#NOMBRE134,  \n" + 
            "                   'ADOLFO PRIETO' TI#CALLE135,  \n" + 
            "                   '624' AS TI#NOEXTERIOR135,  \n" + 
            "                   'DEL VALLE' AS TI#COLONIA135,  \n" + 
            "                   'DELEGACION BENITO JUAREZ' AS TI#MUNICIPIO135,  \n" + 
            "                   'CIUDAD DE MEXICO' AS TI#ESTADO135,  \n" + 
            "                   'MEXICO' AS TI#PAIS135,  \n" + 
            "                   '03100' AS TI#CODIGOPOSTAL135,          \n" + 
            "                   '1' AS TX#CANTIDAD136,  \n" + 
            "                   'AYUDA-SOCIAL' AS TX#DESCRIPCION136,  \n" + 
            "                   REPLACE(TO_CHAR(MAY.UMAY_NAYUDA_SOCIAL,'fm00000000000000.000000'),'.') AS TX#VALORUNITARIO136,  \n" + 
            "                   REPLACE(TO_CHAR(MAY.UMAY_NAYUDA_SOCIAL,'fm00000000000000.000000'),'.') AS TX#IMPORTE136,  \n" + 
            "                   TO_CHAR(PED.UPEDIM_DFECHA_PEDIMENTO,'YY')||'  '||PED.UPEDIM_NNUMERO_ADUANA||'  '||SUBSTR(PED.UPEDIM_VNUMERO_PEDIMENTO,0,4)||'  '||SUBSTR(PED.UPEDIM_VNUMERO_PEDIMENTO,5,10) AS LG#PEDIMENTO137,  \n" + 
            "                   'PRECIO UNITARIO' AS GT#CONCEPTOPRECIOBASE138,  \n" + 
            "                   REPLACE(TO_CHAR(FAU.IMPORTES_BASE_IVA,'fm0000000000.0000'),'.') AS GT#PRECIOBASE138,  \n" + 
            "                   REPLACE(TO_CHAR(FAU.IMPORTES_BASE_IVA,'fm0000000000.0000'),'.') AS GT#PRECIOBASEMONNAL138,  \n" + 
            "                   'SUBTOTAL' AS GT#DSCSUBTOTAL138,  \n" + 
            "                   REPLACE(TO_CHAR(FAU.SUBTOTAL,'fm0000000000.0000'),'.')  AS GT#SUBTOTAL138,  \n" + 
            "                   REPLACE(TO_CHAR(FAU.SUBTOTAL,'fm0000000000.0000'),'.') AS GT#SUBTOTALMONNAL138,  \n" + 
            "                   REPLACE(TO_CHAR(MAY.UMAY_NPRECIO_TOTAL,'fm0000000000.0000'),'.') AS GT#PRECIOTOTMONNAL138,  \n" + 
            "                   UPPER(expresar_en_letras.numero_a_letras( MAY.UMAY_NPRECIO_TOTAL )) AS GT#CANTLETRA138,  \n" + 
            "                   'TOTAL DE CUOTAS' AS GT#CONCEPTOTOTALCUOTAS138,  \n" + 
            "                   REPLACE(TO_CHAR(FAU.TOTAL_CUOTAS,'fm0000000000.0000'),'.') AS GT#IMPORTEMONNAL138,  \n" + 
            "                   REPLACE(TO_CHAR(FAU.IMPORTES_IVA,'fm00000000000000.000000'),'.') AS GT#TOTALIMPUESTOSTRASLADADO138,  \n" + 
            "                  'TRASLADO' AS G1#DCSIVA139,  \n" + 
            "                   REPLACE(TO_CHAR(MAY.UMAY_NTRASLADO,'fm0000000000.0000'),'.') AS G1#IVAMONNAL139,  \n" + 
            "                   'SEGURO TRASLADO' AS G1#DCSIVA140,  \n" + 
            "                   REPLACE(TO_CHAR(MAY.UMAY_NSEGURO,'fm0000000000.0000'),'.') AS G1#IVAMONNAL140,  \n" + 
            "                   'PROMOCION PUBLICITARIA' AS  G1#DCSIVA141,  \n" + 
            "                   REPLACE(TO_CHAR(MAY.UMAY_NPROMOCION_PUBLICITARIA,'fm0000000000.0000'),'.') AS G1#IVAMONNAL141,  \n" + 
            "                   'ASISTENCIA VIAL' AS G1#DCSIVA142,  \n" + 
            "                   REPLACE(TO_CHAR(MAY.UMAY_NASISTENCIA_VIAL,'fm0000000000.0000'),'.') AS G1#IVAMONNAL142,  \n" + 
            "                   'BASE GRAVABLE' AS G1#DCSIVA143,  \n" + 
            "                   REPLACE(TO_CHAR(FAU.IMPORTES_BASE_IVA,'fm0000000000.0000'),'.') AS G1#IVAMONNAL143,  \n" + 
            "                   'I.V.A.'AS G1#DCSIVA144,  \n" + 
            "                   REPLACE(TO_CHAR(FAU.IMPORTES_IVA,'fm0000000000.0000'),'.') AS G1#IVAMONNAL144,  \n" + 
            "                   'LOS SIGUIENTES CARGOS SON  POR CUENTA Y ORDEN' AS G2#LEYENDAPRECIOSIMP145,  \n" + 
            "                   'DE LA ASOCIACION  NACIONAL DE  DISTRIBUIDORES' AS G2#LEYENDAPRECIOSIMP146,  \n" + 
            "                   'DE AUTOMOVILES NISSAN A. C.' AS G2#LEYENDAPRECIOSIMP147,  \n" + 
            "                   '.' AS G2#LEYENDAPRECIOSIMP148,  \n" + 
            "                   'POR LO  TANTO LOS COMPROBANTES CON REQUISITOS' AS G2#LEYENDAPRECIOSIMP149,  \n" + 
            "                   'FISCALES QUE AMPARAN EL COBRO DE ESTAS CUOTAS' AS G2#LEYENDAPRECIOSIMP150,  \n" + 
            "                   'SERAN  EMITIDOS  POR  LOS  BENEFICIARIOS  DEL' AS G2#LEYENDAPRECIOSIMP151,  \n" + 
            "                   'INGRESO. EN CONSECUENCIA, ESTE DOCUMENTO  NO' AS G2#LEYENDAPRECIOSIMP152,  \n" + 
            "                   'CONSTITUYE  COMPROBANTE  FISCAL DEL  PAGO  DE' AS G2#LEYENDAPRECIOSIMP153,  \n" + 
            "                   'DICHAS  CUOTAS EN LOS TERMINOS DEL ART.29 DEL' AS G2#LEYENDAPRECIOSIMP154,  \n" + 
            "                   'CODIGO FISCAL DE LA FEDERACION.' AS G2#LEYENDAPRECIOSIMP155,  \n" + 
            "                   '' AS G2#LEYENDAPRECIOSIMP156,  \n" + 
            "                   'AMDA' AS G4#GTOSGRALPIE157,  \n" + 
            "                   REPLACE(TO_CHAR(MAY.UMAY_NAMDA,'fm0000000000.0000'),'.') AS G4#IMPTEGTOPIEMONNAL157,  \n" + 
            "                   'ANDANAC' AS G4#GTOSGRALPIE158,  \n" + 
            "                   REPLACE(TO_CHAR(MAY.UMAY_NANDANAC,'fm0000000000.0000'),'.') AS G4#IMPTEGTOPIEMONNAL158,          \n" + 
            "                   'PROMEI' AS G4#GTOSGRALPIE159,  \n" + 
            "                   REPLACE(TO_CHAR(FAU.PROMEI,'fm0000000000.0000'),'.') AS G4#IMPTEGTOPIEMONNAL159,          \n" + 
            "                   'SEGURO PLAN PISO' AS G4#GTOSGRALPIE160,  \n" + 
            "                   REPLACE(TO_CHAR(FAU.SEG_PLAN_PISO,'fm0000000000.0000'),'.') AS G4#IMPTEGTOPIEMONNAL160,          \n" + 
            "                   'AYUDA SOCIAL' AS G4#GTOSGRALPIE161,  \n" + 
            "                   REPLACE(TO_CHAR(MAY.UMAY_NAYUDA_SOCIAL,'fm0000000000.0000'),'.') AS G4#IMPTEGTOPIEMONNAL161,  \n" + 
            "                   'TRASLADADO' AS DI#TIPOIMPUESTO162,  \n" + 
            "                   '002' AS DI#CONCEPTOIMPUESTO162,  \n" + 
            "                   '0000160000' AS DI#TASAOCUOTA162,  \n" + 
            "                   'TASA' AS DI#TIPOFACTOR162,  \n" + 
            "                   REPLACE(TO_CHAR(FAU.IMPORTES_IVA,'fm00000000000000.000000'),'.') AS DI#IMPORTE162  \n" + 
            "           FROM ICO_INV_UNIDADES UNI INNER JOIN ICO_INV_FACTURAS_MAYOREO MAY  \n" + 
            "           ON UNI.UNID_VVIN_PK = MAY.UNID_VVIN_PK  \n" + 
            "           INNER JOIN ICO_FAU_FACTURAS_MAYOREO FAU  \n" + 
            "           ON MAY.UNID_VVIN_PK = FAU.VIN_PK  \n" + 
            "           INNER JOIN ICO_INV_CPOLITICAS_VTA POL  \n" + 
            "           ON UNI.POL_VID = POL.POL_VID_PK  \n" + 
            "           INNER JOIN ICO_FAU_CDISTRIBUIDORES DIS  \n" + 
            "           ON UNI.DIST_VID_MATRIZ = DIS.DIST_VID_PK  \n" + 
            "           INNER JOIN ICO_INV_CMODELOS MO  \n" + 
            "           ON UNI.MOD_VEIM = MO.MOD_VEIM_PK  \n" + 
            "           AND UNI.ANIO_NID = MO.ANIO_NID_PK  \n" + 
            "           INNER JOIN ICO_INV_PEDIMENTOS PED  \n" + 
            "           ON UNI.UNID_VVIN_PK = PED.UNID_VVIN_PK  \n" + 
            "           INNER JOIN ICO_NIS_CADUANAS ADU  \n" + 
            "           ON PED.UPEDIM_NNUMERO_ADUANA = ADU.ADU_NID_PK  \n" + 
            "           INNER JOIN ICO_INV_CESTADOS EDO\n" + 
            "           ON DIS.DIST_EDO = EDO.EDO_NID_PK  \n" + 
            "           WHERE UNI.ORIG_VID = 'I'  \n" + 
            "           AND MO.SECT_NID = 20";
            break;
        default:
            sql="";
            break;
        }
        
        if(isByScreen){
            if(idTipoFiltro==1){
                sql = sql + "AND to_char(UMAY_DFECHA_FACTURA_MAYOREO, 'dd/MM/yyyy') BETWEEN '" + datoInicial + "' and '" + datofinal + "'";
            }else{
                sql = sql + "AND UMAY_NNUMERO_FACTURA BETWEEN " + datoInicial + " and " + datofinal;
            }
        }else{
            sql = sql + "AND FAU.ENVIO_FENIS = 0\n";
        }
        log.info(sql);
        ps = cn.prepareStatement(sql);
        rst = ps.executeQuery();
        Map<String, String> renglon;
        ResultSetMetaData metadataRs = rst.getMetaData(); 
        int numRenglon=1;
        while(rst.next()){
            renglon = new HashMap<String, String>();
            for(int i=1; i<=metadataRs.getColumnCount(); i++){
                renglon.put(metadataRs.getColumnName(i), rst.getString(i)==null?"":rst.getString(i));
            }
            archivo.put(numRenglon++, renglon);
        }
        return archivo;
    }
    
    public void iniciaConexion(Connection c){
        cn = c;
    }
    
    public void cierraStaments(){
        try{
            ps.close();
            rst.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
