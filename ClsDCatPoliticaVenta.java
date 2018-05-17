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

public class ClsDCatPoliticaVenta {
    private final clsIConexion conexion;
    
    public ClsDCatPoliticaVenta(clsISesion sesion) throws ICONException { 
        conexion = new clsIConexion(sesion);
    }
    
    public List<ClsDCatPoliticaVentaVo> filtraPoliticaVenta(ClsDCatPoliticaVentaVo filtro) throws SQLException{
        List<ClsDCatPoliticaVentaVo> listaPoliticasVenta = new ArrayList<ClsDCatPoliticaVentaVo>();
        String sql="";
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rst = null;
        List<Object> parametros = new ArrayList<Object>();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            cn = conexion.Conecta();
            if(filtro==null){
                sql = "select * from ICO_INV_CPOLITICAS_VTA order by 1";
            }else{
                sql = "select * from ICO_INV_CPOLITICAS_VTA where 1=1";
            
                if (!filtro.isEmpty()){
                    if(filtro.getPolVidPk()!=null && !filtro.getPolVidPk().isEmpty()){
                        sql = sql + " and POL_VID_PK = ? ";
                        parametros.add(filtro.getPolVidPk());
                    }
                    if(filtro.getPolVdescripcion()!=null && !filtro.getPolVdescripcion().isEmpty()){
                        sql = sql + " and POL_VDESCRIPCION = ? ";
                        parametros.add(filtro.getPolVdescripcion());
                    }
                    if(filtro.getPolVfinanciera()!=null && !filtro.getPolVfinanciera().isEmpty()){
                        sql = sql + " and POL_VFINANCIERA = ? ";
                        parametros.add(filtro.getPolVfinanciera());
                    }
                    if(filtro.getPolVpau()!=null && !filtro.getPolVpau().isEmpty()){
                        sql = sql + " and POL_VPAU = ? ";
                        parametros.add(filtro.getPolVpau());
                    }
                    if(filtro.getTvtavVnopagincentivo()!=null && !filtro.getTvtavVnopagincentivo().isEmpty()){
                        sql = sql + " and TVTAV_VNOPAGINCENTIVO = ? ";
                        parametros.add(filtro.getTvtavVnopagincentivo());
                    }
                    if(filtro.getPolVmc()!=null && !filtro.getPolVmc().isEmpty()){
                        sql = sql + " and POL_VMC = ? ";
                        parametros.add(filtro.getPolVmc());
                    }
                    if(filtro.getPolVte()!=null && !filtro.getPolVte().isEmpty()){
                        sql = sql + " and POL_CTE = ? ";
                        parametros.add(filtro.getPolVte());
                    }
                    if(filtro.getVcolateral()!=null && !filtro.getVcolateral().isEmpty()){
                        sql = sql + " and VCOLATERAL = ? ";
                        parametros.add(filtro.getVcolateral());
                    }
                    if(filtro.getVdescolateral()!=null && !filtro.getVdescolateral().isEmpty()){
                        sql = sql + " and VDESCOLATERAL = ? ";
                        parametros.add(filtro.getVdescolateral());
                    }
                    if(filtro.getVvigencia()!=null && !filtro.getVvigencia().isEmpty()){
                        sql = sql + " and VVIGENCIA = ? ";
                        parametros.add(filtro.getVvigencia());
                    }
                    if(filtro.getVfechamodif()!=null && !filtro.getVfechamodif().isEmpty()){
                        sql = sql + " and VFECHAMODIF = ? ";
                        parametros.add(filtro.getVfechamodif());
                    }
                    if(filtro.getIncNporcFlotilla()!=null ){
                        sql = sql + " and INC_NPORC_FLOTILLA = ? ";
                        parametros.add(filtro.getIncNporcFlotilla());
                    }
                    if(filtro.getNidCategoria()!=null ){
                        sql = sql + " and NID_CATEGORIA = ? ";
                       parametros.add(filtro.getNidCategoria());
                    }
                } 
            }
            ps = cn.prepareStatement(sql);
            Object o;
            for(int i=0; i<parametros.size(); i++){
                o = parametros.get(i);
                if( o instanceof String){
                    ps.setString((i+1), (String)parametros.get(i));
                }else if(o instanceof Float){
                    ps.setFloat((i+1), (Float)parametros.get(i));
                }else if(o instanceof Integer){
                    ps.setInt((i+1), (Integer)parametros.get(i));
                }
            }
            
            rst = ps.executeQuery();
            ClsDCatPoliticaVentaVo politica;
            while (rst.next()) {
                politica= new ClsDCatPoliticaVentaVo();
                politica.setPolVidPk(rst.getString(1));
                politica.setPolVdescripcion(rst.getString(2));
                politica.setPolVfinanciera(rst.getString(3));
                politica.setPolVpau(rst.getString(4));
                politica.setTvtavVnopagincentivo(rst.getString(5));
                politica.setPolVmc(rst.getString(6));
                politica.setPolVte(rst.getString(7));
                politica.setVcolateral(rst.getString(8));
                politica.setVdescolateral(rst.getString(9));
                politica.setVvigencia(rst.getString(10));
                politica.setVfechamodif(rst.getDate(11)==null?"":sdf.format(rst.getDate(11)));
                politica.setIncNporcFlotilla(rst.getFloat(12));
                politica.setNidCategoria(rst.getInt(13));
                listaPoliticasVenta.add(politica);
                }
            ps.close();
            rst.close();
        } catch (SQLException ex) {
            ps.close();
            rst.close();
            throw ex;
        } finally {
            conexion.Desconecta(cn);
        }
        return listaPoliticasVenta;
    }

    public int actualizaPoliticaVenta(ClsDCatPoliticaVentaVo registroActualizar) throws SQLException {
        int numReg = 0;
        StringBuffer sql = new StringBuffer();
        Connection con = null;
        PreparedStatement ps = null;
        sql.append("UPDATE ICO_INV_CPOLITICAS_VTA SET ");
        String registroRelacionado = "";
        List<Object> parametros = new ArrayList<Object>();
        try{
            con = conexion.Conecta();
            if(!registroActualizar.isEmpty()){
                if(registroActualizar.getPolVidPk()!=null && !registroActualizar.getPolVidPk().isEmpty()){
                    sql.append("POL_VID_PK = ?, ");
                    parametros.add(registroActualizar.getPolVidPk());
                    registroRelacionado = "UPDATE ICO_FAU_CAVES_FENIS SET POL_VID_PK = ? WHERE POL_VID_PK = ?";
                }
                if(registroActualizar.getPolVdescripcion()!=null && !registroActualizar.getPolVdescripcion().isEmpty()){
                    sql.append("POL_VDESCRIPCION = ?, ");
                    parametros.add(registroActualizar.getPolVdescripcion());
                }
                //if(registroActualizar.getPolVfinanciera()!=null && !registroActualizar.getPolVfinanciera().isEmpty()){
                    sql.append("POL_VFINANCIERA = ?, ");
                    parametros.add(registroActualizar.getPolVfinanciera());
                //}
                //if(registroActualizar.getPolVpau()!=null && !registroActualizar.getPolVpau().isEmpty()){
                    sql.append("POL_VPAU = ?, ");
                    parametros.add(registroActualizar.getPolVpau());
                //}
                //if(registroActualizar.getTvtavVnopagincentivo()!=null && !registroActualizar.getTvtavVnopagincentivo().isEmpty()){
                    sql.append("TVTAV_VNOPAGINCENTIVO = ?, ");
                    parametros.add(registroActualizar.getTvtavVnopagincentivo());
                //}
                if(registroActualizar.getPolVmc()!=null && !registroActualizar.getPolVmc().isEmpty()){
                    sql.append("POL_VMC = ?, ");
                    parametros.add(registroActualizar.getPolVmc());
                }
                if(registroActualizar.getPolVte()!=null && !registroActualizar.getPolVte().isEmpty()){
                    sql.append("POL_CTE = ?, ");
                    parametros.add(registroActualizar.getPolVte());
                }
                //if(registroActualizar.getVcolateral()!=null && !registroActualizar.getVcolateral().isEmpty()){
                    sql.append("VCOLATERAL = ?, ");
                    parametros.add(registroActualizar.getVcolateral());
                //}
                //if(registroActualizar.getVdescolateral()!=null && !registroActualizar.getVdescolateral().isEmpty()){
                    sql.append("VDESCOLATERAL = ?, ");
                    parametros.add(registroActualizar.getVdescolateral());
                //}
                //if(registroActualizar.getVvigencia()!=null && !registroActualizar.getVvigencia().isEmpty()){
                    sql.append("VVIGENCIA = ?, ");
                    parametros.add(registroActualizar.getVvigencia());
                //}
                if(registroActualizar.getIncNporcFlotilla()!=null ){
                    sql.append("INC_NPORC_FLOTILLA = ?, ");
                    parametros.add(registroActualizar.getIncNporcFlotilla());
                }
                //if(registroActualizar.getNidCategoria()!=null ){
                    sql.append("NID_CATEGORIA = ?, ");
                    parametros.add(registroActualizar.getNidCategoria());
                //}
                sql.append("VFECHAMODIF = sysdate ");

                sql.append(" WHERE POL_VID_PK = '" + registroActualizar.getPolVidPk() + "'");
            }else{
                sql.append(" VFECHAMODIF=VFECHAMODIF ");
                sql.append(" WHERE 1=2;");
            }
            ps = con.prepareStatement(sql.toString());
            Object o;
            for(int i=0; i<parametros.size(); i++){
                o = parametros.get(i);
                if( o instanceof String){
                    ps.setString((i+1), (String)parametros.get(i));
                }else if(o instanceof Float){
                    ps.setFloat((i+1), (Float)parametros.get(i));
                }else if(o instanceof Integer){
                    ps.setInt((i+1), (Integer)parametros.get(i));
                }else if(o==null){
                    if((i+1)==11 ||(i+1)==12){
                        ps.setNull((i+1),java.sql.Types.INTEGER);
                    }else{
                        ps.setNull((i+1),java.sql.Types.VARCHAR);
                    }
                }
                
            }
            numReg = ps.executeUpdate();
            if(!registroRelacionado.isEmpty()){
                ps = con.prepareStatement(registroRelacionado);
                ps.setString(1, registroActualizar.getPolVidPk());
                ps.setString(2, registroActualizar.getPolVidPk());
                ps.executeUpdate();
            }
            ps.close();
            
        } catch (SQLException ex) {
            ps.close();
            throw ex;
        } finally {
            conexion.Desconecta(con);
        }
        return numReg;
    }

    public int insertaPoliticaVenta(ClsDCatPoliticaVentaVo registro) throws SQLException {
        int numReg = 0;
        StringBuffer sql = new StringBuffer();
        Connection con = null;
        PreparedStatement ps = null;
        String campos = "";
        String valores = "";
        List<Object> parametros = new ArrayList<Object>();
        try{
            con = conexion.Conecta();
                if(registro.getPolVidPk()!=null){
                    campos = campos + "POL_VID_PK, ";
                    valores = valores + "?,";
                    parametros.add(registro.getPolVidPk());
                }
                if(registro.getPolVdescripcion()!=null && !registro.getPolVdescripcion().isEmpty()){
                    campos = campos + "POL_VDESCRIPCION, ";
                    valores = valores + "?,";
                   parametros.add(registro.getPolVdescripcion());
                }
                if(registro.getPolVfinanciera()!=null && !registro.getPolVfinanciera().isEmpty()){
                    campos = campos + "POL_VFINANCIERA, ";
                    valores = valores + "?,";
                     parametros.add(registro.getPolVfinanciera());
                }
                if(registro.getPolVpau()!=null && !registro.getPolVpau().isEmpty()){
                    campos = campos + "POL_VPAU, ";
                    valores = valores + "?,";
                     parametros.add(registro.getPolVpau());
                }
                if(registro.getTvtavVnopagincentivo()!=null && !registro.getTvtavVnopagincentivo().isEmpty()){
                    campos = campos + "TVTAV_VNOPAGINCENTIVO, ";
                    valores = valores + "?,";
                     parametros.add(registro.getTvtavVnopagincentivo());
                }
                if(registro.getPolVmc()!=null && !registro.getPolVmc().isEmpty()){
                    campos = campos + "POL_VMC, ";
                    valores = valores + "?,";
                     parametros.add(registro.getPolVmc());
                }
                if(registro.getPolVte()!=null && !registro.getPolVte().isEmpty()){
                    campos = campos + "POL_CTE, ";
                    valores = valores + "?,";
                     parametros.add(registro.getPolVte());
                }
                if(registro.getVcolateral()!=null && !registro.getVcolateral().isEmpty()){
                    campos = campos + "VCOLATERAL, ";
                    valores = valores + "?,";
                     parametros.add(registro.getVcolateral());
                }
                if(registro.getVdescolateral()!=null && !registro.getVdescolateral().isEmpty()){
                    campos = campos + "VDESCOLATERAL, ";
                    valores = valores + "?,";
                     parametros.add(registro.getVdescolateral());
                }
                if(registro.getVvigencia()!=null && !registro.getVvigencia().isEmpty()){
                    campos = campos + "VVIGENCIA, ";
                    valores = valores + "?,";
                     parametros.add(registro.getVvigencia());
                }
                if(registro.getIncNporcFlotilla()!=null ){
                    campos = campos + "INC_NPORC_FLOTILLA, ";
                    valores = valores + "?,";
                     parametros.add(registro.getIncNporcFlotilla());
                }
                if(registro.getNidCategoria()!=null ){
                    campos = campos + "NID_CATEGORIA, ";
                    valores = valores + "?,";
                     parametros.add(registro.getNidCategoria());
                }
            campos = campos + "VFECHAMODIF ";
            valores = valores + "sysdate";            
            sql.append("INSERT INTO ICO_INV_CPOLITICAS_VTA(" + campos +") VALUES(" + valores + ")");
            ps = con.prepareStatement(sql.toString());
            Object o;
            for(int i=0; i<parametros.size(); i++){
                o = parametros.get(i);
                if( o instanceof String){
                    ps.setString((i+1), (String)parametros.get(i));
                }else if(o instanceof Float){
                    ps.setFloat((i+1), (Float)parametros.get(i));
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

    public int borrarPoliticaVenta(String clave) throws SQLException {
        int numReg = 0;
        Connection con = null;
        PreparedStatement ps = null;
        String sql = "DELETE FROM ICO_INV_CPOLITICAS_VTA WHERE POL_VID_PK = ?";
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
    
    public List<Combo> obtieneOpcionesPolVMC() throws SQLException {
        List<Combo> opciones = new ArrayList<Combo>();
        String sql="select * from ICO_INV_CTIPO_VMC";
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rst = null;
        try {
            cn = conexion.Conecta();
            ps = cn.prepareStatement(sql);
            rst = ps.executeQuery();
            Combo c;
            while (rst.next()) {
                c= new Combo();
                c.setValor(rst.getString(1));
                c.setDescripcion(rst.getString(2));
                opciones.add(c);
                }
            ps.close();
            rst.close();
        } catch (SQLException ex) {
            rst.close();
            ps.close();
            throw ex;
        } finally {
            conexion.Desconecta(cn);
        }
        return opciones;
    }
}
