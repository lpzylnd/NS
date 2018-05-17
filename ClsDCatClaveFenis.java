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

import java.sql.Statement;

import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.List;

public class ClsDCatClaveFenis {
    private final clsIConexion conexion;
    
    public ClsDCatClaveFenis(clsISesion sesion) throws ICONException { 
        conexion = new clsIConexion(sesion);
    }
    
    public List<ClsDCatClaveFenisVo> filtraClaveFenis(ClsDCatClaveFenisVo filtro) throws SQLException{
        List<ClsDCatClaveFenisVo> listaClavesFenis = new ArrayList<ClsDCatClaveFenisVo>();
        String sql="";
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rst = null;
        List<Object> parametros = new ArrayList<Object>();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            cn = conexion.Conecta();
            sql = "SELECT a.POL_VID_PK, a.pol_vdescripcion, b.LETRA, " +
                    "b.FECHA_MODIFICA, b.ESTATUS, b.USUARIO_MODIFICA \n" + 
                    "FROM ICO_INV_CPOLITICAS_VTA a, ICO_FAU_CAVES_FENIS b \n" + 
                    "where a.POL_VID_PK = b.POL_VID_PK(+) ";
            if(filtro!=null){
                if (!filtro.isEmpty()){
                    if(filtro.getPolVidPk()!=null && !filtro.getPolVidPk().isEmpty()){
                        sql = sql + " and a.POL_VID_PK = ? ";
                        parametros.add(filtro.getPolVidPk());
                    }
                    if(filtro.getPolVdescripcion()!=null && !filtro.getPolVdescripcion().isEmpty()){
                        sql = sql + " and a.pol_vdescripcion = ? ";
                        parametros.add(filtro.getPolVdescripcion());
                    }
                    if(filtro.getLetra()!=null && !filtro.getLetra().isEmpty()){
                        sql = sql + " and b.LETRA = ? ";
                        parametros.add(filtro.getLetra());
                    }
                    if(filtro.getStatus()!=null && !filtro.getStatus().isEmpty()){
                        sql = sql + " and b.ESTATUS = ? ";
                        parametros.add(filtro.getStatus());
                    }
                } 
            }
            sql = sql + " order by 1";
            ps = cn.prepareStatement(sql);
            Object o;
            for(int i=0; i<parametros.size(); i++){
                o = parametros.get(i);
                if( o instanceof String){
                    ps.setString((i+1), (String)parametros.get(i));
                }else if(o instanceof Integer){
                    ps.setInt((i+1), (Integer)parametros.get(i));
                }
            }
            rst = ps.executeQuery();
            ClsDCatClaveFenisVo claveFenis;
            while (rst.next()) {
                claveFenis= new ClsDCatClaveFenisVo();
                claveFenis.setPolVidPk(rst.getString(1));
                claveFenis.setPolVdescripcion(rst.getString(2));
                claveFenis.setLetra(rst.getString(3));
                claveFenis.setFechaModifica(rst.getDate(4)==null?"":sdf.format(rst.getDate(4)));
                claveFenis.setStatus(rst.getString(5));
                claveFenis.setUsuarioModifica(rst.getString(6));
                listaClavesFenis.add(claveFenis);
            }
            ps.close();
            rst.close();
        } catch (SQLException ex) {
            throw ex;
        } finally {
            conexion.Desconecta(cn);
        }
        return listaClavesFenis;
    }

    public int actualizaClaveFenis(ClsDCatClaveFenisVo registroActualizar) throws SQLException {
        int numReg = 0;
        StringBuffer sql = new StringBuffer();
        Connection con = null;
        PreparedStatement ps = null;
        sql.append("UPDATE ICO_FAU_CAVES_FENIS SET ");
        List<Object> parametros = new ArrayList<Object>();
        try{
            con = conexion.Conecta();
            if (!registroActualizar.isEmpty()){
                String consultaAdicional = "select * from ICO_INV_CPOLITICAS_VTA where POL_VID_PK = ?";
                ps = con.prepareStatement(consultaAdicional);
                ps.setString(1, registroActualizar.getPolVidPk());
                ResultSet rst = ps.executeQuery();
                if(rst.next()==false){
                    throw new SQLException("No se puede Actualizar este registro porque no existe en las Politicas de Venta");
                }
                
                sql.append("LETRA = ?, ");
                parametros.add(registroActualizar.getLetra());
           
                sql.append("ESTATUS = ?, ");
                parametros.add(registroActualizar.getStatus());
                
                sql.append("FECHA_MODIFICA = sysdate,");
                sql.append("USUARIO_MODIFICA = ? ");
                parametros.add(registroActualizar.getUsuarioModifica());
                sql.append(" WHERE POL_VID_PK = '" + registroActualizar.getPolVidPk() + "'");
            }else{
                sql.append(" ESTATUS=ESTATUS ");
                sql.append(" WHERE 1=2;");
            }
            ps = con.prepareStatement(sql.toString());
            Object o;
            for(int i=0; i<parametros.size(); i++){
                o = parametros.get(i);
                if( o instanceof String){
                    ps.setString((i+1), (String)parametros.get(i));
                }else if(o instanceof Integer){
                    ps.setInt((i+1), (Integer)parametros.get(i));
                }else if(o==null){
                        ps.setNull((i+1),java.sql.Types.VARCHAR);
                }
            }
            numReg = ps.executeUpdate();
            ps.close();
            
        } catch (SQLException ex) {
            ps.close();
            throw ex;
        } finally {
            conexion.Desconecta(con);
        }
        return numReg;
    }

    public int insertaClaveFenis(ClsDCatClaveFenisVo registro) throws SQLException {
        int numReg = 0;
        StringBuffer sql = new StringBuffer();
        Connection con = null;
        PreparedStatement ps = null;
        String campos = "";
        String valores = "";
        List<Object> parametros = new ArrayList<Object>();
        try{
            con = conexion.Conecta();
            if(registro.getPolVidPk()!=null && !registro.getPolVidPk().isEmpty()){
                campos = campos + "POL_VID_PK, ";
                valores = valores + "?,";
                parametros.add(registro.getPolVidPk());
                String consultaAdicional = "select * from ICO_INV_CPOLITICAS_VTA where POL_VID_PK = ?";
                ps = con.prepareStatement(consultaAdicional);
                ps.setString(1, registro.getPolVidPk());
                ResultSet rst = ps.executeQuery();
                if(rst.next()==false){
                    throw new SQLException("No se puede Insertar este registro porque no existe en las Politicas de Venta");
                }
            }
            if(registro.getLetra()!=null && !registro.getLetra().isEmpty()){
                campos = campos + "LETRA, ";
                valores = valores + "?,";
                parametros.add(registro.getLetra());
            }
            if(registro.getStatus()!=null && !registro.getStatus().isEmpty()){
                campos = campos + "ESTATUS, ";
                valores = valores + "?,";
                parametros.add(registro.getStatus());
            }
            campos = campos + "FECHA_MODIFICA, ";
            valores = valores + "sysdate,";      
            
            campos = campos + "USUARIO_MODIFICA";
            valores = valores + "?";
            parametros.add(registro.getUsuarioModifica());
            
            sql.append("INSERT INTO ICO_FAU_CAVES_FENIS(" + campos +") VALUES(" + valores + ")");
            ps = con.prepareStatement(sql.toString());
            Object o;
            for(int i=0; i<parametros.size(); i++){
                o = parametros.get(i);
                if( o instanceof String){
                    ps.setString((i+1), (String)parametros.get(i));
                }else if(o instanceof Integer){
                    ps.setInt((i+1), (Integer)parametros.get(i));
                }
            }
            numReg = ps.executeUpdate();
            ps.close();
        } catch (SQLException ex) {
            ps.close();
            throw ex;
        } finally {
            conexion.Desconecta(con);
        }
        return numReg;
    }

    public int borrarClaveFenis(String clave) throws SQLException {
        int numReg = 0;
        Connection con = null;
        PreparedStatement ps = null;
        String sql = "DELETE FROM ICO_FAU_CAVES_FENIS WHERE POL_VID_PK = ?";
        try{
            con = conexion.Conecta();
            ps = con.prepareStatement(sql);
            ps.setString(1, clave);
            numReg = ps.executeUpdate();
            ps.close();
        } catch (SQLException ex) {
            ps.close();
            throw ex;
        } finally {
            conexion.Desconecta(con);
        }
        return numReg;
    }
    
    
}
