package backend;

import java.util.LinkedList;

public class GrafoLocalidadCompleto extends GrafoLocalidad {

	public GrafoLocalidadCompleto(double costo, double costoPorProvincia, double porcentajeSupera300km) {
		super(costo, costoPorProvincia,porcentajeSupera300km);
	}

	public GrafoLocalidadCompleto(LinkedList<Localidad> listaVertices, double costo, double costoPorProvincia, double porcentajeSupera300km) {
		super(costo, costoPorProvincia, porcentajeSupera300km);
		agregarVertices(listaVertices);
	}

	private void agregarVertices(LinkedList<Localidad> listaVertices) {
		for(Localidad localidad:listaVertices) {
			agregarVertice(localidad);
		}
	}

	@Override
	public void agregarVertice(Localidad vertice) {
		if(vertice == null || this.vecinos.containsKey(vertice))
			return;
		super.agregarVertice(vertice);
		generoVerticeCompleto(vertice);

	}

	private void generoVerticeCompleto(Localidad marcado) {	
		// Añado todas las aristas posible al vertice
		for(Localidad vertice: this.vecinos.keySet()) {
			super.agregarArista(marcado, vertice);
		}
	}
	public boolean estaCompleto() {
		return todosLosVerticesSonCompletos();
	}

	private boolean todosLosVerticesSonCompletos() {
		boolean acum=true;
		for(Localidad localidad : this.vecinos.keySet()) {
			acum = acum && this.vecinos.get(localidad).size()-1 == this.vecinos.size();
		}
		return acum;
	}

	@Override
	public void agregarArista(Localidad v1, Localidad v2) {
		throw new RuntimeException("Error no se puede agregar una arista en el GrafoLocalidadCompleto");
	}
	
	@Override
	public void quitarArista(Localidad v1, Localidad v2) {
		throw new RuntimeException("Error no se puede sacar una arista en el GrafoLocalidadCompleto");
	}
}
