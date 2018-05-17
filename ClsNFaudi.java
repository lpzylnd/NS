package SS.ICO.V10.Fau;

import SS.ICO.V10.Common.clsIDisparador;
import SS.ICO.V10.Common.clsIError;

/**
 * Clase que se encarga de asociar las opciones y pantallas con
 *  los Ejecutables que preparan la información de las patallas
 *  del módulo de Faudi.
 * @author sinersys
 * @version 1 , 23/01/2018
 */
public class ClsNFaudi  extends clsIDisparador {
    public ClsNFaudi() {
        super();
        //Cat Distribuidores
        Agrega("CatDistribuidor","Inicio",new ClsECatDisFaudi("Inicio"));
        Agrega("CatDistribuidor","alta",new ClsECatDisFaudi("alta"));
        Agrega("CatDistribuidor","editar",new ClsECatDisFaudi("editar"));
        Agrega("CatDistribuidor","guardar",new ClsECatDisFaudi("guardar"));
        Agrega("CatDistribuidor","actualiza",new ClsECatDisFaudi("actualiza"));
        Agrega("CatDistribuidor","baja",new ClsECatDisFaudi("baja"));
        Agrega("CatDistribuidor","ReporteDistribuidores",new ClsECatDisFaudi("ReporteDistribuidores"));
        
        //Cat Modelos
        Agrega("CatModelos","Inicio",new ClsECatModeloFaudi("Inicio"));
        Agrega("CatModelos","alta",new ClsECatModeloFaudi("alta"));
        Agrega("CatModelos","editar",new ClsECatModeloFaudi("editar"));
        Agrega("CatModelos","baja",new ClsECatModeloFaudi("baja"));
        Agrega("CatModelos","consultar",new ClsECatModeloFaudi("consultar"));
    
    
        //Cat Precios
        Agrega("CatPrecios","Inicio",new ClsECatPrecioFaudi("Inicio"));
        Agrega("CatPrecios","alta",new ClsECatPrecioFaudi("alta"));
        Agrega("CatPrecios","editar",new ClsECatPrecioFaudi("editar"));
        Agrega("CatPrecios","baja",new ClsECatPrecioFaudi("baja"));
        Agrega("CatPrecios","consultar",new ClsECatPrecioFaudi("consultar"));
        Agrega("CatPrecios","guardar",new ClsECatPrecioFaudi("guardar"));
        Agrega("CatPrecios","actualiza",new ClsECatPrecioFaudi("actualiza"));
        
        //Cat Plantas
        Agrega("CatPlantas","Inicio", new ClsECatPlantaFaudi("Inicio"));
        Agrega("CatPlantas","Consultar", new ClsECatPlantaFaudi("Consultar"));
        Agrega("CatPlantas","Guardar", new ClsECatPlantaFaudi("Guardar"));
        Agrega("CatPlantas","Eliminar", new ClsECatPlantaFaudi("Eliminar"));
        
        //Cat Colores Interior
        Agrega("CatColoresInt","Inicio", new ClsECatColorIntFaudi("Inicio"));
        Agrega("CatColoresInt","Buscar", new ClsECatColorIntFaudi("Buscar"));
        Agrega("CatColoresInt","Agregar", new ClsECatColorIntFaudi("Agregar"));
        Agrega("CatColoresInt","Eliminar", new ClsECatColorIntFaudi("Eliminar"));
        Agrega("CatColoresInt","Editar", new ClsECatColorIntFaudi("Editar"));
        Agrega("CatColoresInt","Guardar", new ClsECatColorIntFaudi("Guardar"));
        
        //Cat Colores Exterior
        Agrega("CatColoresExt","Inicio", new ClsECatColorExtFaudi("Inicio"));
        Agrega("CatColoresExt","Buscar", new ClsECatColorExtFaudi("Buscar"));
        Agrega("CatColoresExt","Agregar", new ClsECatColorExtFaudi("Agregar"));
        Agrega("CatColoresExt","Eliminar", new ClsECatColorExtFaudi("Eliminar"));
        Agrega("CatColoresExt","Editar", new ClsECatColorExtFaudi("Editar"));
        Agrega("CatColoresExt","Guardar", new ClsECatColorExtFaudi("Guardar"));
        
		//Cat Politica Venta
        Agrega("CatPoliticaVenta", "Inicio", new ClsECatPoliticaVenta("Inicio"));
        Agrega("CatPoliticaVenta", "Consultar", new ClsECatPoliticaVenta("Consultar"));
        Agrega("CatPoliticaVenta", "Actualizar", new ClsECatPoliticaVenta("Actualizar"));
        Agrega("CatPoliticaVenta", "Insertar", new ClsECatPoliticaVenta("Insertar"));
        Agrega("CatPoliticaVenta", "Borrar", new ClsECatPoliticaVenta("Borrar"));
        
        //Cat Clave Fenis
        Agrega("CatClavesFenis", "Inicio", new ClsECatClaveFenis("Inicio"));
        Agrega("CatClavesFenis", "Consultar", new ClsECatClaveFenis("Consultar"));
        Agrega("CatClavesFenis", "Actualizar", new ClsECatClaveFenis("Actualizar"));
        Agrega("CatClavesFenis", "Insertar", new ClsECatClaveFenis("Insertar"));
        Agrega("CatClavesFenis", "Borrar", new ClsECatClaveFenis("Borrar"));
		
        //Cat Opcinales
        Agrega("CatOpcionales","Inicio", new ClsECatOpcionalesFaudi("Inicio"));
        Agrega("CatOpcionales","Buscar", new ClsECatOpcionalesFaudi("Buscar"));
        Agrega("CatOpcionales","Agregar", new ClsECatOpcionalesFaudi("Agregar"));
        Agrega("CatOpcionales","Eliminar", new ClsECatOpcionalesFaudi("Eliminar"));
        Agrega("CatOpcionales","Editar", new ClsECatOpcionalesFaudi("Editar"));
        Agrega("CatOpcionales","Guardar", new ClsECatOpcionalesFaudi("Guardar"));
        
        //Cat Excepciones
        Agrega("CatExcepciones","Inicio", new ClsEExcepcionesFaudi("Inicio"));
        Agrega("CatExcepciones","Buscar", new ClsEExcepcionesFaudi("Buscar"));
        Agrega("CatExcepciones","Agregar", new ClsEExcepcionesFaudi("Agregar"));
        Agrega("CatExcepciones","Eliminar", new ClsEExcepcionesFaudi("Eliminar"));
        Agrega("CatExcepciones","Editar", new ClsEExcepcionesFaudi("Editar"));
        Agrega("CatExcepciones","Guardar", new ClsEExcepcionesFaudi("Guardar"));
		
		  //Aprovacion de precios
        Agrega("AutorizaPrecio","Inicio",new ClsEAutorizaPrecio("Inicio"));
        Agrega("AutorizaPrecio","autoriza",new ClsEAutorizaPrecio("autoriza"));
        Agrega("AutorizaPrecio","reportePrecios",new ClsEAutorizaPrecio("reportePrecios"));
        Agrega("AutorizaPrecio","consultar",new ClsEAutorizaPrecio("consultar"));
        
        
        //Aprovacion de precios
        Agrega("CatParametros","Inicio",new ClsECatParametros("Inicio"));
        Agrega("CatParametros","actualiza",new ClsECatParametros("actualiza"));
		
		//ArchivosFenis
		Agrega("ReprocesaArchivosFenis", "Inicio", new ClsEReprocesaArchivoFenis("Inicio"));
        Agrega("ReprocesaArchivosFenis", "Regenerar", new ClsEReprocesaArchivoFenis("Regenerar"));
        Agrega("ReprocesaArchivosFenis", "Reprocesar", new ClsEReprocesaArchivoFenis("Reprocesar"));
    }
    /** Método que crea una instancia a la clase clsIError.
     * @param lstrError Descripción del error.
     * @return El objeto de la clase clsIError.
     **/
    @Override
    public clsIError Error(String lstrError) {
        if (lstrError.compareTo("NoPantalla") == 0) {

            clsIError lobjError =

                new clsIError("3", "No existe la pantalla solicitada", 
                              "<A HREF='../Common/clsJMain.jsp' target=_top>Intente nuevamente</A>");

            return lobjError;

        } else if (lstrError.compareTo("NoLogin") == 0) {

            clsIError lobjError =

                new clsIError("2", "No esta firmado", 
                              "<A HREF='../Common/Login.html' target=_top>Login</A>");

            return lobjError;

        } else if (lstrError.compareTo("NoAcceso") == 0) {

            clsIError lobjError =

                new clsIError("2", "No tiene el acceso", 
                              "<A HREF='../Common/clsJMain.jsp' target=_top>Intente nuevamente</A>");

            return lobjError;

        }
        return null;
    }
}
