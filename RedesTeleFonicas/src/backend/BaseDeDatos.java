package backend;

import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

public class BaseDeDatos {

	static String direccionUsuario = System.getProperty("user.dir");
	static String direccionGrafos = direccionUsuario + "//grafos//";
	static String direccionLocalidades = direccionUsuario + "//localidades//";
	
	// Objetos para leer y escribir .json
	private static Gson gson = new GsonBuilder().setPrettyPrinting().create();
	private static JsonReader reader;
	
	// Datos almacenados
	static LinkedList<Localidad> todasLasLocalidades = cargarTodasLasLocalidades();
	static Map<String, LinkedList<Localidad>> provincias = cargarProvincias();;	
	static Map<String, GrafoLocalidad> grafos = cargarGrafos();
	
	
	
	
	/**
	 * Retornar una LinkedList con las localidades de dicha provincia
	 * @param provincia
	 * @return
	 */
	public static LinkedList<Localidad> localidadesDe(String provincia){
		if(!provincias.containsKey(provincia)) {
			throw new IllegalAccessError("No existe la provincia ingresada");
		}
		
		return provincias.get(provincia);
	}

	/**
	 * Retorna una LinkedList de String que representa la lista de Provincias disponibles
	 * @return
	 */
	public static LinkedList<String> provinciasDisponibles(){
		return new LinkedList<String>(provincias.keySet());
	}

	/**
	 * Retorna una LinkedList con todas las localidades
	 * @return
	 */
	public static LinkedList<Localidad> darTodasLasLocalidades(){
		return todasLasLocalidades;
	}
	
	/**
	 * Carga todas las provincias con sus respectivas localidades. Para ello usa un Map<String, LinkedList<Localidad>>, luego de 
	 * cargar todo retorna dicha estructura de dato.
	 * @return
	 */
	private static Map<String, LinkedList<Localidad>> cargarProvincias() {
		Map<String, LinkedList<Localidad>> provinciasLocalidades = new HashMap<String, LinkedList<Localidad>>();
		try {
			String[] provincias = {"Buenos Aires", "Catamarca", "Chaco", "Chubut", "Cordoba", "Corrientes", "Entre Rios"
								,"Formosa", "Jujuy", "La Pampa", "La Rioja", "Mendoza", "Misiones", "Neuquen", "Rio Negro"
								,"Salta", "San Juan", "San Luis", "Santa Cruz", "Santa Fe", "Santiago del Estero", "Tucuman"};
		
			// Lectura de archivos
			for(String provincia: provincias) {
				reader = new JsonReader(new FileReader(direccionLocalidades + provincia + ".json"));
				LinkedList<Localidad> localidades = obtenerListaLocalidades(reader);
				provinciasLocalidades.put(provincia, localidades);
			}
		}catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		
		return provinciasLocalidades;
	}

	/**
	 * Recibe un JsonReader, lee y obtiene el array de Localidad, y las pasa a una List luego convierte  a Linkedlist y la retorna.
	 * @param lector
	 * @return
	 */
	private static LinkedList<Localidad> obtenerListaLocalidades(JsonReader lector) {
		Localidad[] cargarLocalidades = gson.fromJson(reader, Localidad[].class);
		List<Localidad> lista = Arrays.asList(cargarLocalidades);
		LinkedList<Localidad> listaEnlazada =  new LinkedList<Localidad>();
		listaEnlazada.addAll(lista);
		return listaEnlazada;
	}
			
	/**
	 * Carga todas las localidades del archivo todasLasLocalidades.json. Y retorna una LinkedList con cada Localidad.
	 * @return
	 */
	private static LinkedList<Localidad> cargarTodasLasLocalidades() {
		
		try {
			// Lectura de archivos
			reader = new JsonReader(new FileReader(direccionLocalidades + "todasLasLocalidades.json"));
			LinkedList<Localidad> todasLasLocalidades = obtenerListaLocalidades(reader);
			return todasLasLocalidades;
		}catch (Exception e) {
			return null;
		}
	}	
	
	
	/**
	 * Muestra una lista de archivos con todos los grafos disponibles para cargar
	 * @return
	 */
	public static List<String> grafosGuardadosJSON(){
		List<String> listaDeArchivos = new LinkedList<String>(grafos.keySet());
		return listaDeArchivos;
	}
	
	
	/**
	 * Guarda un grafo pasado como parametro, en un archivo con el nombre pasado como parametro.
	 * (No hace falta incluir el formato dentro del nombre del archivo).
	 * @param grafo
	 * @param nombreArchivo
	 * @throws IOException
	 */
	public static void guardarGrafoDesdeLocalidades(GrafoLocalidad grafo, String nombreArchivo) throws IOException {
		if(grafo == null || nombreArchivo == null || nombreArchivo == "") {
			throw new IllegalArgumentException("No pueden estar vacios o ser nulos los argumnetos.");
		}

		JsonParser parser = new JsonParser();
		
		// Datos del Grafo
		
		double costoPorKm = grafo.getCostoPorKm();
		double porcentajeSupera300Km = grafo.getPorcentajeSupera300km();
		double costoDistintasProvincias = grafo.getCostoPorProvincia();
		LinkedList<Localidad> localidades = new LinkedList<Localidad> (grafo.getLocalidades());
		String localidadesString = gson.toJson(localidades);
		
		JsonObject parseando = new JsonObject();
		parseando.addProperty("costoPorKm", costoPorKm);
		parseando.addProperty("porcentajeSupera300Km", porcentajeSupera300Km);
		parseando.addProperty("costoDistintasProvincias", costoDistintasProvincias);
		parseando.add("localidades", parser.parse(localidadesString));
				
		FileWriter fichero = new FileWriter(direccionGrafos + nombreArchivo + ".json");
        PrintWriter pw = new PrintWriter(fichero);
        pw.write(gson.toJson(parseando));
   
        pw.close();
        fichero.close();
        
        grafos.put(nombreArchivo + ".json", grafo);
	}	
	
		
	private static Map<String, GrafoLocalidad> cargarGrafos() {
		Map<String,GrafoLocalidad> grafos = new HashMap<String, GrafoLocalidad>();
		
		
		
		// Leo primero todos los archivos en el directorio 
		String sDirectorio = direccionGrafos;
		File directorio = new File(sDirectorio);
		
		// Creo el filtro para que solo lea Json
		FileFilter filtrarSoloJson = new FileFilter() {
			
			@Override
			public boolean accept(File pathname) {
				// Si es un archivo y es .json devuelvo true
				return pathname.isFile() && pathname.getName().contains(".json");
			}
		};
		
		if(directorio.exists()) {
			File[] ficheros = directorio.listFiles(filtrarSoloJson);
			for(File fichero: ficheros) {
				// Si es un grafo lo agrego al map si no lo paso de largo.
				GrafoLocalidad grafo = levantarGrafoJsonDesdeLocalidades(fichero.getAbsolutePath());
				if(grafo != null) 
					grafos.put(fichero.getName(), grafo);
				
			}
		}
		
		// Retorno el map
				
		return grafos;
	}
	
	
	private static GrafoLocalidad levantarGrafoJsonDesdeLocalidades(String direccionArchivo) {
		try {
			// Parseando de Json a Grafo
			
			JsonParser parser = new JsonParser();
			Object objeto = parser.parse(new FileReader(direccionArchivo));
			JsonObject graf = (JsonObject) objeto;
						
			// Datos del Json
			
			double costoPorKm = graf.get("costoPorKm").getAsDouble();
			double porcentajeProvinciasDistintas = graf.get("porcentajeSupera300Km").getAsDouble();
			double costoDistintasProvincias = graf.get("costoDistintasProvincias").getAsDouble();
			
			Localidad[] localidadesArray = gson.fromJson( graf.get("localidades").getAsJsonArray(), Localidad[].class); 
			
			List<Localidad> lista = Arrays.asList(localidadesArray);
			LinkedList<Localidad> localidades = new LinkedList<Localidad>(lista);
			
			// Creadno el Grafo
			
			GrafoLocalidadCompleto grafo = new GrafoLocalidadCompleto(localidades, costoPorKm, porcentajeProvinciasDistintas, costoDistintasProvincias);
			GrafoLocalidad grafoAGM = ArbolGeneradorMinimo.prim(grafo);
						
			return grafoAGM;
			
		} catch (Exception e) {
			System.out.println("NO PUDE LEVANTAR LOS DATOS");
			System.out.println(e.getMessage() + "\n" + e.getCause());
			return null;
		}
	
	}	
	
	/**
	 * Obtiene un grafo a partir de una direccionDeArchivo
	 * @param direccionArchivo
	 * @return
	 */
	public static GrafoLocalidad obtenerGrafo(String direccionArchivo) {
		if(!grafos.containsKey(direccionArchivo)) {
			throw new IllegalAccessError("Direccion Invalida.");
		}
		return grafos.get(direccionArchivo);
	}
}
