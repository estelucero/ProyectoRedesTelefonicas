package backend;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

public class GrafoLocalidad {

	LinkedList<CableDeRed> aristas;
	Map<Localidad, LinkedList<Localidad>> vecinos;
	
	BigDecimal costoTotal;
	double costoPorKm;
	double costoPorProvincia;
	double porcentajeSupera300km;

	public GrafoLocalidad(double costo, double costoPorProvincia, double porcentajeSupera300km) {
		vecinos = new HashMap<Localidad, LinkedList<Localidad>>();
		aristas = new LinkedList<CableDeRed>();
		costoTotal =   new BigDecimal(0);
		costoTotal = costoTotal.setScale(2,RoundingMode.CEILING);
		
		this.costoPorKm = costo;
		this.costoPorProvincia = costoPorProvincia;
		this.porcentajeSupera300km = porcentajeSupera300km;
	}

	public GrafoLocalidad(LinkedList<Localidad> listaVertices, double costo, double costoPorProvincia, double porcentajeSupera300km) {
		vecinos = new HashMap<Localidad, LinkedList<Localidad>>();
		aristas = new LinkedList<CableDeRed>();
		costoTotal =   new BigDecimal(0);
		costoTotal =costoTotal.setScale(2,RoundingMode.CEILING);
		// Agrego los vertices
		for (Localidad vertice : listaVertices)
			vecinos.put(vertice, new LinkedList<Localidad>());
		
		///////////////////////// HACER VERIFICACION DE VARIABLES ////////////////////////
		this.costoPorKm = costo;
		this.costoPorProvincia = costoPorProvincia;
		this.porcentajeSupera300km = porcentajeSupera300km;
	}

	public GrafoLocalidad(double costo,double costoPorProvincia, double porcentajeSupera300km, Localidad... localidades) {
		vecinos = new HashMap<Localidad, LinkedList<Localidad>>();
		aristas = new LinkedList<CableDeRed>();
		costoTotal =   new BigDecimal(0);
		costoTotal =costoTotal.setScale(2,RoundingMode.CEILING);
		for (Localidad localidad : localidades)
			vecinos.put(localidad, new LinkedList<Localidad>());
		
		this.costoPorKm = costo;
		this.costoPorProvincia = costoPorProvincia;
		this.porcentajeSupera300km = porcentajeSupera300km;

	}

	public void agregarVertice(Localidad v) {
		if (v != null && !vecinos.containsKey(v))
			vecinos.put(v, new LinkedList<Localidad>());

	}

	public void quitarVertice(Localidad verticeAquitar) {
		// Remuevo la key asociada al vertice
		for (Localidad vertice : vecinos.keySet())
			// Remuevo el valor en cada lista de vecinos
			vecinos.get(vertice).remove(verticeAquitar);
		
		// Lo remuevo de las aristas del grafo
		Iterator<CableDeRed> arista = aristas.iterator();
		while (arista.hasNext()) {
			CableDeRed conexion = arista.next();
			if (conexion.tieneEstaLocalidad(verticeAquitar)) {
				restarCosto(conexion.getCosto());
				arista.remove();
			}
		}

		vecinos.remove(verticeAquitar);
	}

	public boolean existeVertice(Localidad v) {
		return vecinos.keySet().contains(v);
	}

	public void agregarArista(Localidad v1, Localidad v2) {
		if (verificarVertices(v1, v2))
			return;

		BigDecimal costoArista =  new BigDecimal(Utilidades.obtenerCosto(v1, v2, costoPorKm, porcentajeSupera300km, costoPorProvincia));
		costoArista = costoArista.setScale(2, RoundingMode.CEILING);
		CableDeRed nuevoCable = new CableDeRed(v1, v2, Utilidades.distanciaEnKm(v1, v2), costoArista);
		if (!aristas.contains(nuevoCable)) {
			aristas.add(nuevoCable);
			vecinos.get(v1).add(v2);
			vecinos.get(v2).add(v1);
			sumarCosto(costoArista);
		}
	}

	private void sumarCosto(BigDecimal costo) {
		costoTotal = costoTotal.add(costo);
		costoTotal = costoTotal.setScale(2, RoundingMode.CEILING);
	}
	
	public void quitarArista(Localidad v1, Localidad v2) {
		if (verificarVertices(v1, v2))
			return;

		BigDecimal costoArista =  new BigDecimal(Utilidades.obtenerCosto(v1, v2, costoPorKm, porcentajeSupera300km, costoPorProvincia));
		costoArista = costoArista.setScale(2, RoundingMode.CEILING);
		CableDeRed cable = new CableDeRed(v1, v2, Utilidades.distanciaEnKm(v1, v2), costoArista);
		if (aristas.contains(cable)) {
			restarCosto(cable.getCosto());
			aristas.remove(cable);
			vecinos.get(v1).remove(v2);
			vecinos.get(v2).remove(v1);
		}
	}

	private void restarCosto(BigDecimal costo) {
		costoTotal=costoTotal.subtract(costo);
		costoTotal = costoTotal.setScale(2, RoundingMode.CEILING);
	}

	public boolean existeArista(Localidad v1, Localidad v2) {
		if (v1 == null || v2 == null) {
			return false;
		}
		
		BigDecimal costoArista =  new BigDecimal(Utilidades.obtenerCosto(v1, v2, costoPorKm, porcentajeSupera300km, costoPorProvincia));
		costoArista = costoArista.setScale(2, RoundingMode.CEILING);
		CableDeRed arista = new CableDeRed(v1, v2, Utilidades.distanciaEnKm(v1, v2), costoArista);
		
		return aristas.contains(arista);
	}

	public LinkedList<Localidad> vecinos(Localidad v) {
		return vecinos.get(v);
	}

	public int tamanio() {
		return vecinos.size();
	}

	public void imprimirLocalidades() {
		for (Localidad localidad : vecinos.keySet())
			System.out.println(localidad.toString());

	}

	private boolean verificarVertices(Localidad v1, Localidad v2) {
		return v1 == null || v2 == null || !vecinos.keySet().contains(v1) || !vecinos.keySet().contains(v2);
	}

	public ArrayList<Localidad> getLocalidades() {
		return new ArrayList<Localidad>(vecinos.keySet());
	}

	public LinkedList<CableDeRed> getAristas() {
		return aristas;
	}

	@Override
	public String toString() {
		StringBuilder cadena = new StringBuilder();

		for (Localidad localidad : vecinos.keySet()) {
			cadena.append(localidad.getNombre() + ": ");
			for (Localidad vecino : vecinos.get(localidad)) {
				cadena.append(vecino.getNombre() + " -- ");
			}
			cadena.append("\n");
		}

		for (CableDeRed conexion : aristas) {
			cadena.append(conexion.getVertice1().getNombre() + " ------ " + conexion.getVertice2().getNombre() + " : "
					+ conexion.getDistancia() + "KM " + "$" + conexion.getCosto());
			cadena.append("\n");
		}

		return cadena.toString();
	}

	public boolean isAGM() {
		return aristas.size() == tamanio() - 1;
	}

	public static GrafoLocalidad crearApartirDeAristas(double costoPorKm2, double costoProvinciasDistintas, double porcentajeSupera300Km2, LinkedList<CableDeRed> aristasGrafo) {
		GrafoLocalidad g = new GrafoLocalidad(costoPorKm2, costoProvinciasDistintas, porcentajeSupera300Km2);
		for (CableDeRed arista : aristasGrafo) {
			g.agregarVertice(arista.getVertice1());
			g.agregarVertice(arista.getVertice2());
			g.agregarArista(arista.getVertice1(), arista.getVertice2());
		}
		return g;
	}

	public void cambiarAristaPorOtra(Localidad origen, Localidad destinoOriginal, Localidad nuevoDestino) {
		if (existeArista(origen, destinoOriginal) && !existeArista(origen, nuevoDestino)) {
			agregarArista(origen, nuevoDestino);
			quitarArista(origen, destinoOriginal);
		}
	}
	
	public double getCostoPorKm() {
		return costoPorKm;
	}
	
	public double getCostoPorProvincia() {
		return costoPorProvincia;
	}
	
	public double getPorcentajeSupera300km() {
		return porcentajeSupera300km;
	}
	
	public BigDecimal getCostoTotal() {
		return costoTotal;
	}
	
}
