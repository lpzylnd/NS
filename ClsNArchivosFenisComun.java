package SS.ICO.V10.Fau;

import SS.Base.ComponenteSeg.clsComponenteDecodificador;
import SS.Base.ComponenteSeg.clsSeparate;

import SS.ICO.V10.Common.clsIUtil;
import SS.ICO.V10.Common.clsIParametro;
import SS.ICO.V10.Eta.providers.model.FTP.ProvFTPClient;
import SS.ICO.V10.Eta.providers.model.FTP.ProvFTPException;

import SS.ICO.V10.Uti.Mail.clsIMail;

import java.io.File;
import java.io.IOException;

import java.sql.Connection;

import java.util.StringTokenizer;

import org.apache.log4j.Logger;

public class ClsNArchivosFenisComun {
    
    Logger log = Logger.getLogger(this.getClass().getName());
    private final int remitenteEMAIL = 81905;
    private final int passwordEMAIL = 81906;
    private final int servidorEMAIL = 81907;
    
    public ClsNArchivosFenisComun() {
        super();
    }
    
    public ProvFTPClient conectaFTP(clsIParametro lobjParametros, int rutaConexionFTP, int parametroPuertoFTP) throws Exception {
        int port = 21;
        String u="";
        String pwd="";
        String h ="";
        String cadenaConexionFTP;
        if(lobjParametros.Recupera(rutaConexionFTP)){
            cadenaConexionFTP =  lobjParametros.VVALOR;                
        }else{
            log.error("No se pudo recuperar el valor del parametro " + rutaConexionFTP
                      + " requerido para realizar la recuperacion de archivos del FTP");
            return null;
        }
        String cadena_conexion=clsComponenteDecodificador.desencripta_cadena_conexion(cadenaConexionFTP);
        clsSeparate d= new clsSeparate(cadena_conexion);
        h = d.getHost();
        u = d.getUser();
        pwd = d.getPass();
    
        if(lobjParametros.Recupera(parametroPuertoFTP)){
            try{
                port = Integer.parseInt(lobjParametros.VVALOR);
            }catch(Exception e){
                log.error("El valor del parametro " + parametroPuertoFTP + " no es numerico " +
                    " y es requerido para realizar la conexion al FTP");
            }
        }else{
            log.error("No se pudo recuperar el valor del parametro " + parametroPuertoFTP 
                      + " requerido para realizar la conexion al FTP");
        }
        ProvFTPClient ftp = null;
        //log.info("Datos de conexion ftp:" + h  + " - " + u + " - " + pwd + " - " + port);
        System.out.println("Datos de conexion ftp:" + h  + " - " + u + " - " + pwd + " - " + port);
        try {
            ftp = new ProvFTPClient(h, port);
            ftp.login(u, pwd);
        } catch (ProvFTPException e) {
            log.error(e.getMessage(), e);
            throw new Exception(e.getMessage());
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new Exception(e.getMessage());
        }
        return ftp;
    }
    
    public Connection conectaBD(){
        Connection cn = null;
        try{
            cn = clsIUtil.getConnectionORCL().getConnection();
        } catch (Exception e) {
            log.error("Error al obtener la conexion a BD " + e.getMessage());
        }
        return cn;
    }
    
    public void desconectaBD(Connection cn){
        try{
            cn.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void enviarMailErrores(clsIParametro lobjParametros, int destinosEMAIL, int subjectEMAIL,
                                   int mensajeEMAIL, File archivoErrores) {
            //Se obtiene una coneccion temporal para extraer parametros de BD
            String lstrDestinos;
            try {
                if (lobjParametros.Recupera(destinosEMAIL)) {
                    lstrDestinos = lobjParametros.VVALOR;
                } else {
                    throw new RuntimeException("No existe el parametro : " + destinosEMAIL);
                }

                String lstrRemitente;
                if (lobjParametros.Recupera(remitenteEMAIL)) {
                    lstrRemitente = lobjParametros.VVALOR;
                } else {
                    throw new RuntimeException("No existe el parametro : "+remitenteEMAIL);
                }

                String lstrPassword;
                if (lobjParametros.Recupera(passwordEMAIL)) {
                    lstrPassword = lobjParametros.VVALOR;
                } else {
                    throw new RuntimeException("No existe el parametro : " + passwordEMAIL);
                }


                String lstrServidor;
                if (lobjParametros.Recupera(servidorEMAIL)) {
                    lstrServidor = lobjParametros.VVALOR;
                } else {
                    throw new RuntimeException("No existe el parametro : "+servidorEMAIL);
                }

                String lstrSubject;
                if (lobjParametros.Recupera(subjectEMAIL)) {
                    lstrSubject = lobjParametros.VVALOR;
                } else {
                    throw new RuntimeException("No existe el parametro : "+subjectEMAIL);
                }
                String lstrMensaje;
                if (lobjParametros.Recupera(mensajeEMAIL)) {
                    lstrMensaje = lobjParametros.VVALOR;
                } else {
                    throw new RuntimeException("No existe el parametro : "+mensajeEMAIL);
                }

                StringTokenizer misDest = new StringTokenizer(lstrDestinos, ";");

                clsIMail correos = new clsIMail();
                correos.setRemitente(lstrRemitente, lstrPassword);
                correos.setServidorCorreo(lstrServidor);
                while (misDest.hasMoreElements()) {
                    correos.addDestino(misDest.nextElement().toString().trim());
                }
                correos.setSubject(lstrSubject);
                correos.setMensaje(lstrMensaje);

                correos.addArchAdjunto(archivoErrores.getAbsolutePath());
                            
                correos.enviarMail(true);
            } catch (Exception fe) {
                fe.printStackTrace();
                log.error("Error en salida de entrega de correo " + fe.getMessage(), fe);            
            }
        }
}
