package SS.ICO.V10.Fau;

import SS.ICO.V10.Common.clsISesion;
import SS.ICO.V10.Common.clsIUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ClsNCatModelosFaudi {
    public ClsNCatModelosFaudi() {
        super();
    }
   


    

   
        private clsISesion mobjSesion=null;
        private Map<String, ClsDModeloVo> mapModelosDis;
        private String mensaje;
        private String claveMod;
        private ClsDModeloVo voSelec;
       
        /**Inicializa los campos sesion, y fecha.
         * @param lobjSesion, sesion.
         * */
        public void Inicializa(clsISesion lobjSesion){
            this.mobjSesion = lobjSesion;
            this.mapModelosDis = new LinkedHashMap<String, ClsDModeloVo>();
            this.claveMod="";
            this.voSelec=new ClsDModeloVo();
         
        }
        
        public void modelosDisponibles (ClsDModeloVo mod )  {

            this.getMapModelosDis().clear();
            StringBuffer sql = new StringBuffer();
            String cadena=" ";
            
            int filtro=0;
            if (mod.getEim()!=null){
                if(filtro<1){
                    cadena=cadena+" Where MOD_VEIM_PK=" +mod.getEim();
                        filtro=1;
                }else{
                    cadena=cadena+" and MOD_VEIM_PK=" +mod.getEim();
                }
            }
            if (mod.getCuota()>0){
                if(filtro<1){
                    cadena=cadena+" Where MOD_ANIO_NID_PK=" +mod.getCuota();
                        filtro=1;
                }else{
                    cadena=cadena+" and MOD_ANIO_NID_PK=" +mod.getCuota();
                }
            }
            
            /* Se construye la consulta */
            sql.append("SELECT MOD_VEIM_PK, MOD_ANIO_NID_PK, MOD_CAB, MOD_ORIGEN, MOD_SECTOR,") ;
                  sql.append(" MOD_DESC, MOD_GRUPO, MOD_SECUENCIA_MAYOREO " );
                  sql.append("  FROM ICO_INV_CCAT_MODELOS_FAUDI " ); 
            sql.append(cadena);
                  sql.append(" order by MOD_ANIO_NID_PK DESC ");
         
        System.out.println("Modelos activos "+sql);
            Connection lcnnConnection = null;
            Statement stm = null;
            ResultSet rst = null;
            
            try {
                lcnnConnection = clsIUtil.getConnectionORCL().getConnection();
                stm = lcnnConnection.createStatement();
                rst = stm.executeQuery(sql.toString());
                
                /* Se iteran los resultados y se agregan al objeto de salida. */
                while (rst.next()) {
                    ClsDModeloVo vo=new ClsDModeloVo();
                    vo.setEim(rst.getString(1));
                    vo.setCuota(rst.getInt(2));
                    vo.setCabecera(rst.getString(3));
                    vo.setOrigen(rst.getString(4));
                    vo.setSector(rst.getInt(5));
                    vo.setDescripcion(rst.getString(6));
                    vo.setGrupoMaterial(rst.getNString(7));
                   
                    this.getMapModelosDis().put(vo.getEim(), vo);
                }
               // mensaje = "Los Distribuidores están disponibles para seleccionarse.";
            } catch (Exception e) {
                System.out.println("Catalogo Modelos Catch 001: " + e.getMessage());
                System.out.println(": Error al consultar los modelos disponibles.");
                setMensaje("Error al consultar los modelos disponibles. Consultar Administrador");
                       
            } finally {
                try {
                    stm.close();
                    rst.close();
                    if(lcnnConnection != null){
                        lcnnConnection.close();
                    }
                } catch (Exception e) {
                    System.out.println("Error al cerrar conexion"+ e.getMessage());
                   
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
                        System.out.println("Catalogo distribuidores Catch 002: " + e.getMessage());
                        System.out.println(": Error al consultar los distribuidores disponibles.");
                        setMensaje("Error al consultar los estados disponibles. Consultar Administrador");
                               
                    } finally {
                        try {
                            stm.close();
                            rst.close();
                            if(lcnnConnection != null){
                                lcnnConnection.close();
                            }
                        } catch (Exception e) {
                            System.out.println("Error al cerrar conexion"+ e.getMessage());
                           
                        }
                    }
              
            
            return vec;
        }
        public Map<String, ClsDModeloVo> getMapModelosDis() {
            return mapModelosDis;
        }

        public void setMapDistribuidoresDis(Map<String, ClsDModeloVo> mapModelosDis) {
            this.mapModelosDis = mapModelosDis;
        }

        public String getMensaje() {
            return mensaje;
        }

        public void setMensaje(String mensaje) {
            this.mensaje = mensaje;
        }

        public String getClaveDis() {
            return claveMod;
        }

        public void setClaveDis(String claveDis) {
            this.claveMod = claveDis;
        }

        public ClsDModeloVo getVoSelec() {
            return voSelec;
        }

        public void setVoSelec(ClsDModeloVo voSelec) {
            this.voSelec = voSelec;
        }

}

