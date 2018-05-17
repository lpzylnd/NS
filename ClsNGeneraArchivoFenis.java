package SS.ICO.V10.Fau;

import SS.ICO.V10.Common.Negocio.ICONException;

import SS.ICO.V10.Common.clsIParametro;
import SS.ICO.V10.Common.clsISesion;

import SS.ICO.V10.Common.clsIUtil;
import SS.ICO.V10.Eta.providers.model.FTP.ProvFTPClient;
import SS.ICO.V10.Eta.providers.model.FTP.ProvFTPException;
import SS.ICO.V10.Eta.providers.model.FTP.ProvFTPTransferType;
import SS.ICO.V20.general.model.Combo;

import com.google.gson.JsonArray;

import com.google.gson.JsonObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import java.io.IOException;

import java.io.PrintWriter;

import java.nio.charset.StandardCharsets;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


import java.util.Map;

import java.util.StringTokenizer;

import javax.naming.NameNotFoundException;
import javax.naming.NamingException;

import javax.servlet.http.HttpServletRequest;


import net.sf.json.JSONArray;

import org.apache.log4j.Logger; 

public class ClsNGeneraArchivoFenis{
    
    Logger log = Logger.getLogger(this.getClass().getName());
    //private clsISesion mobjSesion;
    private ClsDGeneraArchivoFenis accesoBD;
    private ClsNArchivosFenisComun clsComunArchivosFenis = new ClsNArchivosFenisComun();
    private Connection cn;
    private clsIParametro lobjParametros=new clsIParametro();
    private final int rutaTempFenis = 53016;
    private final int rutaCarpetaFTPDestino=53017;
    private final int rutaConexionFTP=53009;
    private final int puerto=53010;

    public ClsNGeneraArchivoFenis() {
            accesoBD = new ClsDGeneraArchivoFenis();
    }
    
    private void generaArchivoFenis(boolean isByScreen, String nombreCompletoArchivo, int idArchivo, int idTipoFiltro, String datoInicial, String datoFinal) throws Exception {
        Map<Integer, List<ClsDRegistroArchivoFenisVo>> listaOrdenRegistrosArchivo =  new HashMap<Integer, List<ClsDRegistroArchivoFenisVo>>();
        Map<Integer,Map<String,String>> datosArchivo = null;
        String mensajeError="";
        File file = new File(nombreCompletoArchivo);
        PrintWriter pw =null;
        try{
            listaOrdenRegistrosArchivo = accesoBD.getRegistrosConfigArchivo(idArchivo);
        }catch(Exception ioe){
            mensajeError = "Error al obtener la configuracion de campos que integran el archivo : " + idArchivo 
                      + ioe.getMessage();
            throw new Exception(mensajeError);
        }
        try{
            datosArchivo = accesoBD.getDatosArchivo(isByScreen, idArchivo, idTipoFiltro, datoInicial, datoFinal);
        }catch(Exception e){
            mensajeError = "Error al obtener los datos que integran el archivo : " + idArchivo + e.getMessage();
            throw new Exception(mensajeError);
        }
        try{
            pw = new PrintWriter(file, "UTF-8");
        }catch(Exception e){
            mensajeError = "Error crear el archivo : " + nombreCompletoArchivo + ". Causa: "+ e.getMessage();
            throw new Exception(mensajeError);
        }
        accesoBD.cierraStaments();
        StringBuffer renglon = new StringBuffer();
        String tipoRegistro = "";
        Map<String,String> renglonDatos=null;
        for(int numRenglon=1; numRenglon<=datosArchivo.size(); numRenglon++){
            renglonDatos = datosArchivo.get(numRenglon);
            for(int numConfigCampo=1; numConfigCampo<=listaOrdenRegistrosArchivo.size(); numConfigCampo++){
                renglon = new StringBuffer(); 
                List<ClsDRegistroArchivoFenisVo> listaRegs = listaOrdenRegistrosArchivo.get(numConfigCampo);
                for(ClsDRegistroArchivoFenisVo reg :listaRegs){
                    tipoRegistro = reg.getTipoRegistro();
                    Float longCampo = Math.abs(reg.getLogitudCampo());
                    int longitud = longCampo.intValue();
                    if(reg.getConfCampo()!=null){
                        renglon.append(reg.getConfCampo());
                    }else{
                        String nombreCampo="";
                        if(reg.getNombreCampo()!=null){
                            nombreCampo = reg.getTipoRegistro()+ "#" + reg.getNombreCampo().trim();
                        }
                        String dato= renglonDatos.get(nombreCampo);
                        dato = dato==null?"":dato;
                        int tamDato = 0;
                        if(dato.length()>0){
                            tamDato = dato.length();
                        }
                        if(tamDato>=longitud){
                            dato = dato.substring(0, longitud);
                            renglon.append(dato);
                        }else{
                            renglon.append(dato);
                            if(tamDato>0){
                                while(tamDato < longitud){
                                    renglon.append(" ");
                                    tamDato++;
                                }    
                            }
                        }
                    }
                    renglon.append("|");
                }
                if(renglon.length()>0)
                    renglon.deleteCharAt(renglon.length()-1);
                pw.println(renglon);
            }
        }
        pw.close();
        log.info("Termina la generacion del archivo.");
    }
    
    private void mueveArchivoAFTP(String rutaArchivoLocal, String nombreArchivo) throws IOException, ProvFTPException,
                                                                                         Exception {
        lobjParametros.Recupera(cn);
        ProvFTPClient ftp = null;
        String rutaRemotaArchivo;
        try {
            ftp = clsComunArchivosFenis.conectaFTP(lobjParametros, rutaConexionFTP, puerto);
            if (lobjParametros.Recupera(rutaCarpetaFTPDestino)) {
                rutaRemotaArchivo = lobjParametros.VVALOR;
            } else {
                throw new RuntimeException("No existe el parametro : " + rutaCarpetaFTPDestino);
            }
            ftp.setType(ProvFTPTransferType.BINARY);
            rutaArchivoLocal = rutaArchivoLocal + File.separator +nombreArchivo;
            rutaRemotaArchivo = rutaRemotaArchivo + "/"+ nombreArchivo;
            ftp.put(rutaArchivoLocal, rutaRemotaArchivo);
            ftp.quit();
        } catch(Exception e){
            ftp.quit();
            log.error("Error al conectar al ftp. " + e.getMessage(), e);
            throw new Exception("Error al conectar al ftp. " + e.getMessage());
        }
        log.info("Se movio el archivo al ftp en la ruta: "+rutaRemotaArchivo);
    }
    
    public void crearArchivosFenis(int idTipoArchivo) throws Exception {
        crearArchivosFenis(false, idTipoArchivo, 0, null, null);
    }
    
    
    public void crearArchivosFenis(boolean isByScreen, int idTipoArchivo, int idTipoFiltro, String datoInicial, String datoFinal) throws Exception {
        try {
            String rutaArchivo;
            String nombreCompletoArchivo;
            cn = clsComunArchivosFenis.conectaBD();
            accesoBD.iniciaConexion(cn);
            lobjParametros.Recupera(cn);
            if (lobjParametros.Recupera(rutaTempFenis)) {
                rutaArchivo = lobjParametros.VVALOR;
            } else {
                throw new RuntimeException("No existe el parametro : " + rutaTempFenis);
            }
            Date fechaActual = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            String fechaHora = sdf.format(fechaActual);
            String nombreArchivo="";
            if(idTipoArchivo==(1)){
                nombreArchivo = "F06" + fechaHora;
            }else{
                nombreArchivo = "F07" + fechaHora;
            }
            nombreArchivo = nombreArchivo + ".txt";
            nombreCompletoArchivo = rutaArchivo + File.separator + nombreArchivo ;
            log.info("Se obtiene el nombre completo del archivo: " + nombreCompletoArchivo);
            generaArchivoFenis(isByScreen, nombreCompletoArchivo, idTipoArchivo, idTipoFiltro, datoInicial, datoFinal);
            mueveArchivoAFTP(rutaArchivo, nombreArchivo);    
            respaldaArchivo(rutaArchivo, nombreArchivo);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            System.out.print(e);
            throw new Exception(e.getMessage());
        }finally{
            clsComunArchivosFenis.desconectaBD(cn);
        }
    }

    private void respaldaArchivo(String rutaArchivo, String nombreArchivo) {
        File archivo =  new File(rutaArchivo+ File.separator+nombreArchivo);
        String archivoResp = rutaArchivo + File.separator + "Respaldo" + File.separator + nombreArchivo;
        File archivoRespaldado =  new File(archivoResp);
        boolean copiado = archivo.renameTo(archivoRespaldado);
        log.info("Se respaldo en archivo en : " + archivoResp + ". Respaldado:"+copiado);
    }
    
    public void copiaArchivoFenis_BD() throws Exception { 
        ClsNRecuperacionTimbradoFenis recover = new ClsNRecuperacionTimbradoFenis();
        recover.copiaArchivoFenis_BD();
    }
}


