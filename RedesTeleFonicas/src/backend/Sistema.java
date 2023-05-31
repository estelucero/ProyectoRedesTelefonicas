package backend;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.openstreetmap.gui.jmapviewer.Coordinate;

public class Sistema {

	static GrafoLocalidad grafoAGM;
	static GrafoLocalidadCompleto grafoCompleto;
	static Set<Localidad> localidadesCargadas;
	static Set<CableDeRed> conexionesEstablecidas;
	static double costoPorKm= 20;
	static double porcentajeSupera300Km= 0.2;
	static double costoProvinciasDistintas = 20;

	public enum PRECIOS {
		PORKILOMETROS, PORCENTAJESUPERA300KM, DOSPROVINCIASDISTINTAS
	}

	/**
	 * Inicializa las variables necesarias para el sistema
	 */
	public static void inicializar() {
		localidadesCargadas = new HashSet<Localidad>(); // Usamos un Set ya que asi no se agregan localidades repetidas
		conexionesEstablecidas = new HashSet<CableDeRed>();
		grafoCompleto = new GrafoLocalidadCompleto(costoPorKm, porcentajeSupera300Km, costoProvinciasDistintas);

		
		
	

		
	}

	/**
	 * Reinicia las variables necesarias del sistema
	 */
	public static void reiniciar() {
		grafoAGM = null;
		inicializar();
	}

	/**
	 * Crea el grafoCompleto con las localidades cargadas en el Sistema. Retorna
	 * true si se creo correctamente, false si no.
	 * 
	 * @return
	 */
	public static boolean crearGrafoCompleto() {
		try {
			grafoCompleto = new GrafoLocalidadCompleto(new LinkedList<Localidad>(localidadesCargadas), costoPorKm,
					porcentajeSupera300Km, costoProvinciasDistintas);
			actualizarConexionesEstablecidas(grafoCompleto);

			return true;

		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Crea el Arbol Generador Minimo partiendo del grafoCompleto. Retorna true si
	 * se creo, false si no se pudo crear.
	 * 
	 * @return
	 */
	public static boolean crearGrafoAGM() {
		if (grafoCompleto == null || grafoCompleto.tamanio() <= 0) {
			throw new RuntimeException("Debe agregar vertices para poder crear el AGM");
		}
		if (grafoCompleto != null) {
			grafoAGM = ArbolGeneradorMinimo.prim(grafoCompleto);
			actualizarConexionesEstablecidas(grafoAGM);
			return true;
		}
		return false;
	}

	/**
	 * Retorna el grafoAGM del sistema. (Tipo de dato GrafoLocalidad)
	 * 
	 * @return
	 */
	public static GrafoLocalidad obtenerGrafoAGM() {
		return grafoAGM;
	}

	/**
	 * Retorna el grafoCompleto del sistema. (Tipo de dato GrafoLocalidadCompleto)
	 * 
	 * @return
	 */
	public static GrafoLocalidadCompleto obtenerGrafoCompleto() {
		return grafoCompleto;
	}

	/**
	 * Actualiza las conexiones establecidas hasta el momento en el Sistema. (Es
	 * llamada al crear grafoCompleto y grafoAGM)
	 * 
	 * @param grafoActual
	 */
	private static void actualizarConexionesEstablecidas(GrafoLocalidad grafoActual) {
		conexionesEstablecidas = new HashSet<CableDeRed>();
		for (CableDeRed arista : grafoActual.getAristas()) {
			conexionesEstablecidas.add(arista);
		}
	}

	/**
	 * Carga una localidad que contenga los siguientes parametros nombre ,
	 * provincia, latitud y longitud. Retorna true si es agregada, false en caso
	 * contrario.
	 * 
	 * @param nombre
	 * @param provincia : Debe ser de Argentina
	 * @param latitud   : Valor debe estar en (-90,91)
	 * @param longitud  : Valor debe estar en (-181, 181)
	 * @return
	 */
	public static boolean cargarLocalidad(String nombre, String provincia, Float latitud, Float longitud) {
		if (!validarLocalidad(nombre, provincia, latitud, longitud)) {
			return false;
		}
		Localidad localidad = new Localidad(nombre, provincia, latitud, longitud);

		return localidadesCargadas.add(localidad);
	}

	/**
	 * Carga una localidad a partir de el nombre y la provincia. Retorna true si se
	 * cargo correctamente false en caso contrario
	 * 
	 * @param nombre
	 * @param provincia : Debe ser de Argentina
	 * @return
	 */
	public static boolean cargarLocalidad(String nombre, String provincia) {
		if (nombre == null || nombre.isEmpty() || provincia == null || provincia.isEmpty())
			return false;

		for (Localidad localidad : BaseDeDatos.localidadesDe(provincia)) {
			// Si coincide el nombre
			if (localidad.getNombre().equals(nombre)) {
				// Si esta agregada retorno false
				if (localidadEstaAgregada(localidad)) {
					return false;
				}
				// Si no la agrego y retorno true
				localidadesCargadas.add(localidad);
				return true;
			}
		}
		return false;
	}

	/**
	 * Retorna true si la localidad ya ha sido agregada, false caso contrario.
	 * 
	 * @param localidad
	 * @return
	 */
	private static boolean localidadEstaAgregada(Localidad localidad) {
		return localidadesCargadas.contains(localidad);
	}

	/**
	 * Nos devuelve si el grafoCompleto esta vacio
	 * 
	 * @return
	 */

	public static boolean estaGrafoVacio() {
		return localidadesCargadas.isEmpty();
	}

	/**
	 * Esta funcion puede ser llamada antes de cargar una localidad a la lista de
	 * localidades Retorna true de ser una localidad valida, false caso contrario.
	 * Una localidad es valida si: - Su latitud esta en (-90,91) - Su longitud esta
	 * en (-181,181) - Si su provincia y nombre no son vacios
	 * 
	 * @param nombre
	 * @param provincia
	 * @param latitud
	 * @param longitud
	 * @return
	 */
	private static boolean validarLocalidad(String nombre, String provincia, Float latitud, Float longitud) {
		// Comentar esta validacion
		// ------------------------
		return latitud < -21 && latitud > -67 && longitud < -53 && longitud > -73 && nombre != "" && provincia != "";
	}

	/**
	 * Retorna una lista de String que representa las provincias disponibles
	 * 
	 * @return
	 */
	public static List<String> provinciasDisponibles() {
		List<String> provinciasDisponibles = BaseDeDatos.provinciasDisponibles();
		return provinciasDisponibles;
	}

	/**
	 * Retorna una lista de String que representa las localidades disponibles de la
	 * provincia pasada como parametro
	 * 
	 * @param provincia
	 * @return
	 */
	public static List<String> localidadesDisponibles(String provincia) {
		List<String> localidadesDisponibles = new ArrayList<String>();

		for (Localidad localidad : BaseDeDatos.localidadesDe(provincia)) {
			localidadesDisponibles.add(localidad.getNombre());
		}

		return localidadesDisponibles;
	}

	/**
	 * Recibe el nombre de archivo donde se guardara el grafoAGM del sistema.
	 * Retorna true si se guardo sin problemas y false en caso contrario.
	 * 
	 * @param nombreArchivo
	 * @return
	 */
	public static boolean guardarAGM(String nombreArchivo) {
		try {
			BaseDeDatos.guardarGrafoDesdeLocalidades(grafoAGM, nombreArchivo);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Retorna una lista con las Localidades cargadas en el Sistema.
	 * 
	 * @return
	 */
	public static Set<Localidad> localidadesCargadas() {
		return localidadesCargadas;
	}

	public static Set<CableDeRed> cablesCargados() {
		return conexionesEstablecidas;
	}

	/**
	 * Retorna la coordenada de una localidad cargada Retorna null si no existe y la
	 * coordenada si se encuentra creado.
	 * 
	 * @param nombre
	 * @param provincia
	 * @return
	 */
	public static Coordinate obtenerCoordenadasLocalidad(String nombre, String provincia) {
		for (Localidad localidad : localidadesCargadas) {
			if (localidad.getNombre().equals(nombre) && localidad.getProvincia().equals(provincia)) {
				return new Coordinate(localidad.getLatitud(), localidad.getLongitud());
			}
		}
		return null;
	}

	/**
	 * Retorna una localidad que este cargada en el SIstema.
	 * 
	 * @param nombre
	 * @param provincia
	 * @return
	 */
	public static Localidad obtenerLocalidadCargada(String nombre, String provincia) {
		for (Localidad localidad : localidadesCargadas) {
			if (localidad.getNombre().equals(nombre) && localidad.getProvincia().equals(provincia)) {
				return localidad;
			}
		}
		return null;
	}



	public static boolean cargarGrafoAGM(String direccionArchivo) {
		try {
			grafoAGM = BaseDeDatos.obtenerGrafo(direccionArchivo);
			actualizarGrafoCompleto(grafoAGM);
			actualizarConexionesEstablecidas(grafoAGM);
			actualizarLocalidades(grafoAGM);
			actualizarCostos(grafoAGM);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private static void actualizarCostos(GrafoLocalidad grafo) {
		if(grafo != null) {
			setCostoPorKm(grafo.getCostoPorKm());
			setPorcentajeSupera300Km(grafo.getPorcentajeSupera300km());
			setCostoProvinciaDistinta(grafo.getCostoPorProvincia());
		}
	}

	private static void actualizarGrafoCompleto(GrafoLocalidad grafo) {
		if (grafo != null) {
			grafoCompleto = new GrafoLocalidadCompleto(new LinkedList<Localidad>(grafo.getLocalidades()), costoPorKm,
					porcentajeSupera300Km, costoProvinciasDistintas);
		}
	}

	private static void actualizarLocalidades(GrafoLocalidad grafo) {
		if (grafo != null) {
			localidadesCargadas = new HashSet<Localidad>();
			for (Localidad localidad : grafo.getLocalidades()) {
				localidadesCargadas.add(localidad);
			}
		}
	}

	public static BigDecimal obtenerCostoTotalAGM() {
		return grafoAGM.getCostoTotal();
	}

	public static double getPrecioBase(PRECIOS precioPedido) {
		if (precioPedido == null) {
			throw new IllegalArgumentException(
					"Debe pedir un precio dentro de el SISTEMA. PRECIOS.PORKILOMETOS, PRECIOS.PORCENTAJESUPERA300KM, PRECIOS.DOSPROVINCIASDISTINTAS");
		}
		switch (precioPedido) {
		case PORKILOMETROS: {
			return Sistema.costoPorKm;
		}
		case PORCENTAJESUPERA300KM: {
			return Sistema.porcentajeSupera300Km;
		}
		case DOSPROVINCIASDISTINTAS: {
			return Sistema.costoProvinciasDistintas;
		}
		default:
			throw new IllegalArgumentException(
					"Debe pedir un precio dentro de el SISTEMA. PRECIOS.PORKILOMETOS, PRECIOS.PORCENTAJESUPERA300KM, PRECIOS.DOSPROVINCIASDISTINTAS");
		}
	}

	public static void setCostoPorKm(double precio) {
		if (precio <= 0) {
			throw new IllegalArgumentException("El costo por KILOMETRO no puede ser menor o igual a cero.");
		}

		costoPorKm = precio;
	}
	public static void setPorcentajeSupera300Km(double precio) { 
		if (precio < 0 ) {
			throw new IllegalArgumentException("El porcentaje agregado si supera 300KM no puede ser negativo.");
		}
		if(precio>1) {
			throw new IllegalArgumentException("El porcentaje agregado si supera 300KM no puede ser mayor a 1.");
		}
		porcentajeSupera300Km=precio;
	}
	public static void setCostoProvinciaDistinta(double precio) {
		if (precio < 0) {
			throw new IllegalArgumentException(
					"El costo fijo agregado por provincias distintas no puede ser negativo.");
		}
		costoProvinciasDistintas=precio;
	}
	
	
	
	
	/**
	 * Cambia una arista por en otra en el grafoAGM del sistema: Cambia la arista
	 * (origen,destinoOriginal) por la nueva arista (origen, nuevoDestino) Retorna
	 * true si se pudo cambiar y false si no fue posible.
	 * 
	 * @param origen
	 * @param destinoOriginal
	 * @param nuevoDestino
	 * @return
	 */
	public static boolean cambiarAristaPorOtra(Localidad origen, Localidad destinoOriginal, Localidad nuevoDestino) {
		if (grafoAGM == null) {
			throw new RuntimeException("Debe crear el grafo AGM primero. ");
		}
		
		if(origen == null || destinoOriginal == null || nuevoDestino == null) {
			throw new IllegalArgumentException("Los veertices no pueden ser nulos. ");
		}

		GrafoLocalidad grafoAux = GrafoLocalidad.crearApartirDeAristas(costoPorKm, costoProvinciasDistintas, porcentajeSupera300Km, grafoAGM.getAristas());
				
		
		if (grafoAux.existeArista(origen, destinoOriginal) && grafoAux.existeVertice(nuevoDestino)
				&& !grafoAux.existeArista(origen, nuevoDestino)) {

			grafoAux.cambiarAristaPorOtra(origen, destinoOriginal, nuevoDestino);
			
			if(BFS.esConexo(grafoAux)) {
				grafoAGM.cambiarAristaPorOtra(origen, destinoOriginal, nuevoDestino);
				actualizarConexionesEstablecidas(grafoAGM);
				return grafoAGM.existeArista(origen, nuevoDestino);
			}else {
				throw new RuntimeException("EL GRAFO DEBE SEGUIR SIENDO CONEXO");
			}
		}

		return false;
	}
	
	
	
	public static List<Localidad> listaDePosiblesDestinosAcambiar(Localidad origen){

		List<Localidad> localidades = new LinkedList<Localidad>();
		
		for(Localidad vecino: grafoAGM.vecinos(origen)) {
			if(!vecino.equals(origen) && grafoAGM.vecinos(vecino).size() > 1) {
				localidades.add(vecino);
			}
		}
		return localidades;
	}
	
	
	public static List<Localidad> listaDeNuevosDestinosAcambiar(Localidad origen, Localidad destino){
		if(origen == null || destino == null) {
			throw new IllegalArgumentException("Origen y destino deben ser validos.");
		}
		
		List<Localidad> localidadesPosibles = new LinkedList<Localidad>();
		
		LinkedList<Localidad> vecinos = grafoAGM.vecinos(origen);
		
		
		if(vecinos.size() < localidadesCargadas.size() - 1) {
			for(Localidad nuevoDestino: grafoAGM.getLocalidades()) {
				if(!vecinos.contains(nuevoDestino) && !nuevoDestino.equals(origen)) {
					localidadesPosibles.add(nuevoDestino);
				}
			}
		}
		
		
		return localidadesPosibles;
	}
	
}
