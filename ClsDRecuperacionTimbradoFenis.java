package SS.ICO.V10.Fau;

import SS.ICO.V10.Common.clsIConexion;
import SS.ICO.V10.Common.clsISesion;

import SS.ICO.V10.Common.clsIUtil;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.sql.Types;

import java.util.List;
import java.util.Map;

import javax.naming.NameNotFoundException;
import javax.naming.NamingException;

import org.apache.poi.hssf.record.formula.functions.Int;

public class ClsDRecuperacionTimbradoFenis {
    
    private Connection cn=null;
    //private Connection cn = null;
    private CallableStatement cs = null;
    
    public ClsDRecuperacionTimbradoFenis(){
    }
    
    public void conectaBDSP(Connection c) throws SQLException {
        String storeProcedure="{ call ICO_FAU_ACTUALIZA_TIMBRADO(?,?,?,?,to_char(sysdate,'YYYYMMDDHH24MISS'),?,?,?)}";
        try {
            cn = c;
            cs = cn.prepareCall(storeProcedure);
        } catch (Exception e) {
            cs.close();
            cn.close();
            throw new SQLException(e);
        }
        
    }
    
    public String ejecutaSPActualizaTimbrado(List<Object> parametros) throws SQLException{
        String msgSP=null;
        Object parametro="";
        cs.clearParameters();
        for(int i=0; i<parametros.size(); i++){
            parametro= parametros.get(i);
            if(parametro instanceof String){
                cs.setString(i+1, (String)parametro);    
            }else{
                cs.setInt(i+1, (Integer)parametro);
            }
        }
        cs.registerOutParameter(6, Types.NUMERIC);
        cs.registerOutParameter(7, Types.VARCHAR);
        cs.executeQuery();
        int codError = cs.getInt(6);
        if(codError!=0){
            msgSP=cs.getString(7);
        }
    return msgSP;
    }
    
    public void desconectaBDSP() throws SQLException{
            cs.close();
    }
    
    
}
