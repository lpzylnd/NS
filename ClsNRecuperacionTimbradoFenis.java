package SS.ICO.V10.Fau;

import SS.Base.ComponenteSeg.clsComponenteDecodificador;
import SS.Base.ComponenteSeg.clsSeparate;

import SS.ICO.V10.Common.Negocio.ICONException;
import SS.ICO.V10.Common.clsIParametro;
import SS.ICO.V10.Common.clsISesion;

import SS.ICO.V10.Common.clsIUtil;
import SS.ICO.V10.Eta.providers.model.FTP.ProvFTPClient;
import SS.ICO.V10.Eta.providers.model.FTP.ProvFTPException;
import SS.ICO.V10.Eta.providers.model.FTP.ProvFTPTransferType;
import SS.ICO.V10.Uti.FTP.FTPClient;

import SS.ICO.V10.Uti.FTP.FTPException;

import SS.ICO.V10.Uti.FTP.FTPTransferType;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.io.InputStreamReader;
import java.io.LineNumberReader;

import java.io.PrintWriter;

import java.sql.Connection;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.util.StringTokenizer;
import SS.ICO.V10.Uti.Mail.clsIMail;

import org.apache.log4j.Logger;

public class ClsNRecuperacionTimbradoFenis {
    
    Logger log = Logger.getLogger(this.getClass().getName());
    private clsIParametro lobjParametros=new clsIParametro();
    private ClsNArchivosFenisComun clsComunArchivosFenis = new ClsNArchivosFenisComun();
    private ClsDRecuperacionTimbradoFenis accesoBD;
    private Connection cn;
    private final int rutaConexionFTP=53009;
    private final int puertoFTP=53010;
    private final int rutaLocalFTP=53011;
    private final int rutaRemotaFTP=53012;
    private final int destinosEMAIL = 53013;
    private final int subjectEMAIL = 53014;
    private final int mensajeEMAIL = 53015;
    
    
    public ClsNRecuperacionTimbradoFenis() {
        accesoBD = new ClsDRecuperacionTimbradoFenis();
    }

    private void mueveArchivosAServerLocal(String rLocal, ProvFTPClient ftp) throws FTPException {
        String[] listaArchivos;
        String rRemota="";
        try {            
            if(lobjParametros.Recupera(rutaRemotaFTP)){
                rRemota =  lobjParametros.VVALOR;                
            }else{
                log.error("No se pudo recuperar el valor del parametro " + rutaRemotaFTP
                          + " requerido para realizar la recuperacion de archivos del FTP");
                return;
            }
            ftp.chdir(rRemota);
            listaArchivos = ftp.dir(rRemota);
            String nombreArchivoLocal=""; 
            for(String arch : listaArchivos){
                nombreArchivoLocal = arch.substring(arch.lastIndexOf("/"));
                ftp.setType(ProvFTPTransferType.BINARY);
                ftp.get(rLocal+nombreArchivoLocal, arch);
                //ftp.delete(arch);
            }
            //ftp.quit();
        } catch(Exception e){
            log.error(e.getMessage(), e);
            throw new FTPException("Hubo un error en al acceder al FTP, " + e.getMessage());
        }
        log.info("Se movieron los archivos de : "+ rRemota +" al server local: " + rLocal);
    }
    
    private void operaArchivosFenis(String rLocal, ProvFTPClient ftp){
        File dirArchivosFenis = new File(rLocal);
        File[] archivosFenis = dirArchivosFenis.listFiles();
        File archivoErrores;
        PrintWriter p;
        boolean bandError=false;
        try {
            accesoBD.conectaBDSP(cn);
            archivoErrores = new File (dirArchivosFenis + File.separator + "ArchivoErrores.log");
            p = new PrintWriter(archivoErrores);
        } catch (SQLException e) {
            log.error("Error al realizar la conexion a BD para guardar los archivos FENIS. " + 
                      e.getMessage(), e);
            return;
        } catch (IOException e) {
            log.error("Error al generar el archivo de errores "+ e.getMessage(),e);
            return;
        }
        String line = null;
        String msg="";
        int i=0;
        for(File archivo : archivosFenis){
            if(archivo.isFile()){
                bandError=false;
                try {
                    FileReader a = new FileReader(archivo);
                    LineNumberReader in = new LineNumberReader(a);
                    List<Object> campos;
                    String campo="";
                    i=0;
                    while((line = in.readLine()) != null){
                        if(line.length()>0){
                            i++;
                        }else{continue;}
                        campos = new ArrayList<Object>();
                        if(line.length()==64){              
                            campo=line.substring(0,2);  //prefijo
                            campos.add(campo);          
                            
                            campo=line.substring(5,12);//factura
                            int campoEntero = Integer.parseInt(campo); 
                            campos.add(campoEntero);
                            
                            campo=line.substring(12,48);//cfdi-folio
                            campos.add(campo);
                           
                            campo=line.substring(48,62);//cfdi-fecha + cfdi-status
                            campos.add(campo);
                            
                            campo=line.substring(62,64);//cfdi-status
                            campos.add(campo);
                        
                            try {
                                log.info("Inicia SP del archivo: " + archivo.getName());
                                msg = accesoBD.ejecutaSPActualizaTimbrado(campos);
                                log.info("Termina SP del archivo: " + archivo.getName());
                                if (msg!=null){
                                    bandError=true;
                                    generaArchivoErrores(archivo.getName(), i, msg, line, p);
                                }
                            } catch (SQLException e) {
                                bandError=true;
                                msg=e.getMessage();
                                generaArchivoErrores(archivo.getName(), i, msg, line, p);
                            }
                        }else{
                            msg="Este registro no cumple con el layout especificado ";
                            generaArchivoErrores(archivo.getName(), i, msg, line, p);
                        }
                    }
                    a.close();
                    in.close();
                    //archivo.delete();
                } catch (Exception e) {
                    msg="Error al leer el archivo "+ e.getMessage();
                    generaArchivoErrores(archivo.getName(), i, msg, line, p);
                }
                try{
                    ftp.delete(archivo.getName());
                    if(!bandError){
                        archivo.delete();
                    }
                }catch (Exception e) {
                    log.error("Error al borrar el archivo " + archivo.getName() + " del ftp. "+ e.getMessage());
                }
            }
        }
        p.close();
        if(bandError){
            clsComunArchivosFenis.enviarMailErrores(lobjParametros, destinosEMAIL, subjectEMAIL, mensajeEMAIL, archivoErrores);
        }
        archivoErrores.delete();
        try {
            accesoBD.desconectaBDSP();
        } catch (SQLException e) {
            log.error("Error al desconectarse de la BD despues de guardar los archivos FENIS. " + 
                      e.getMessage(), e);
        }
        log.info("Se termino de procesar la lista de archivos");
    }

    private void generaArchivoErrores(String nombreArchivo, int i, String msgError, String line, PrintWriter p){
        p.print(nombreArchivo);
        p.print("|");
        p.print(i);
        p.print("|");
        p.print(msgError);
        p.print("|");
        p.println(line); 
    }                  
   
    public void copiaArchivoFenis_BD() throws Exception {
        cn = clsComunArchivosFenis.conectaBD();
        lobjParametros.Recupera(cn);
        String msg = "";
        ProvFTPClient ftp = null;       
        String rLocal="";
        if(lobjParametros.Recupera(rutaLocalFTP)){
            rLocal =  lobjParametros.VVALOR;                
        }else{
            msg = "No se pudo recuperar el valor del parametro " + rutaLocalFTP
                  + " requerido para realizar la recuperacion de archivos del FTP";
            log.error(msg);
            throw new Exception(msg);
        }
        try{
            ftp = clsComunArchivosFenis.conectaFTP(lobjParametros, rutaConexionFTP, puertoFTP);
            mueveArchivosAServerLocal(rLocal, ftp);
            operaArchivosFenis(rLocal, ftp);
            try{
                ftp.quit();     
            }catch(Exception e){
                log.error("Error sin afectacion al ejecutar el siguiente comando: ftp.quit()");
            }
            log.info("Se termina el procesamiento de los archivos mediante SP");
        }catch(Exception e){
            msg = "Error al copiaArchivoFenis a BD:" + e.getMessage();
            log.error(msg);
            throw new Exception(msg);
        }finally{
            clsComunArchivosFenis.desconectaBD(cn);
            
        }
       
    }

}
