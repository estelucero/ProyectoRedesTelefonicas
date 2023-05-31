package backend;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class BFS {

	private static List<Localidad> L;
	
	public static boolean esConexo(GrafoLocalidad g)
	{
		if(g == null)
			throw new IllegalArgumentException("El grafo no puede ser nulo.");
				
		return g.tamanio() == 0|| alcanzables(g, g.getLocalidades().get(0)).size() == g.tamanio();
	}
	
	
	
	public static Set<Localidad> alcanzables(GrafoLocalidad g, Localidad localidad)
	{
		Set<Localidad> localidadesMarcadas = new HashSet<Localidad>();
		L = new LinkedList<Localidad>();
		L.add(localidad); // Agrego un unico elemento a la lista
		
		
		
		while(!L.isEmpty()) {
			Localidad localidad_i = L.get(0);	
			localidadesMarcadas.add(localidad_i);
			agregarLocalidadesNoMarcadas(localidadesMarcadas, g.vecinos(localidad_i));
			L.remove(0);
		}
		
		
		return localidadesMarcadas;
	}



	private static void agregarLocalidadesNoMarcadas(Set<Localidad> localidadesMarcadas, List<Localidad> vecinosDei) {
		for(Localidad vecino: vecinosDei) {
			if(!localidadesMarcadas.contains(vecino))
				L.add(vecino);
		}
	}
	
	
	
	

	
	
}
