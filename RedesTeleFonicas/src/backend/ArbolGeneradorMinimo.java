package backend;

import java.util.LinkedList;

public class ArbolGeneradorMinimo {

	static GrafoLocalidad grafoAGM;
	static LinkedList<Localidad> verticesMarcados;

	public static GrafoLocalidad prim(GrafoLocalidad grafo) {
		if (grafo == null || !BFS.esConexo(grafo)) 
			throw new IllegalArgumentException();

		// Agrego un vertice el cual sera el vertice origen
		Localidad origen = grafo.getLocalidades().get(0);
		grafoAGM = new GrafoLocalidad(grafo.getCostoPorKm(), grafo.getPorcentajeSupera300km(), grafo.getCostoPorProvincia(), origen);

		verticesMarcados = new LinkedList<Localidad>();
		verticesMarcados.add(origen);

		CableDeRed aristaMin = null;
		Float minimoCosto = null;
		Localidad verticeNoMarcado = null;
		
		LinkedList<CableDeRed> aristas = grafo.getAristas();

		int cantAristas = 0;
		while (cantAristas < grafo.tamanio() - 1) { 

			aristaMin = null;
			minimoCosto = Float.POSITIVE_INFINITY;

			// Recorro la lista de aristas del grafo conexo pasado como parametro
			for (CableDeRed arista : aristas) 
				if (soloUnoMarcado(arista) && aristaEsMenorQueAristaMin(arista, minimoCosto)) {
					aristaMin = arista;
					minimoCosto = aristaMin.getCosto().floatValue();
				}
			
			verticeNoMarcado = obtenerNoMarcado(aristaMin);
			
			// Agrego el vertice no marcado a la lista de marcados.
			grafoAGM.agregarVertice(verticeNoMarcado);
			verticesMarcados.add(verticeNoMarcado);

			// Agrego la arista al grafo AGM
			grafoAGM.agregarArista(aristaMin.getVertice1(), aristaMin.getVertice2());
			cantAristas++;
			
			aristas.remove(aristaMin);
		}

		return grafoAGM;
	}

	private static boolean aristaEsMenorQueAristaMin(CableDeRed arista, Float minimoCosto) {
		return arista.getCosto().floatValue() < minimoCosto;
	}

	private static boolean soloUnoMarcado(CableDeRed arista) {
		boolean primerCaso = verticesMarcados.contains(arista.getVertice1())
				&& !verticesMarcados.contains(arista.getVertice2());
		boolean segundoCaso = !verticesMarcados.contains(arista.getVertice1())
				&& verticesMarcados.contains(arista.getVertice2());
		return primerCaso || segundoCaso;
	}

	private static Localidad obtenerNoMarcado(CableDeRed arista) {
		if (verticesMarcados.contains(arista.getVertice1()))
			return arista.getVertice2();
		return arista.getVertice1();
	}

}
