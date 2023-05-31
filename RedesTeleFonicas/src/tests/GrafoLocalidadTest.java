package tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;

import backend.ArbolGeneradorMinimo;
import backend.CableDeRed;
import backend.GrafoLocalidad;
import backend.Localidad;

public class GrafoLocalidadTest {

	private GrafoLocalidad g;
	private Localidad jose_c_paz;
	private Localidad san_miguel;
	private Localidad pilar;
	private Localidad bella_vista;
	private Localidad moreno;
	private Localidad villa_del_parque;

	double costoPorKm = 20;
	double porcentajeSupera300Km = 0.2;
	double costoProvinciasDistintas = 20;
	
	@Before
	public void setup() {
		jose_c_paz = new Localidad("Jose.C.Paz", "Buenos Aires", 20, 30);
		san_miguel = new Localidad("San Miguel", "Buenos Aires", 50, 30);
		pilar = new Localidad("Pilar", "Buenos Aires", 10, 30);
		bella_vista = new Localidad("Bella Vista", "Buenos Aires", 70, 30);
		moreno = new Localidad("Moreno", "Buenos Aires", 80, 42);
		villa_del_parque = new Localidad("Villa Del Parque", "Buenos Aires", 48, 23);
		g = new GrafoLocalidad(costoPorKm, porcentajeSupera300Km, costoProvinciasDistintas,jose_c_paz, san_miguel, pilar, bella_vista, moreno, villa_del_parque);
	}

	@Test
	public void grafoSinLocalidades() {
		g = new GrafoLocalidad(costoPorKm, porcentajeSupera300Km, costoProvinciasDistintas);
	}

	@Test
	public void grafoConUnaLocalidad() {
		g = new GrafoLocalidad(costoPorKm, porcentajeSupera300Km, costoProvinciasDistintas, jose_c_paz);
	}

	@Test
	public void grafoConDosLocalidades() {
		g = new GrafoLocalidad(costoPorKm, porcentajeSupera300Km, costoProvinciasDistintas, jose_c_paz, villa_del_parque);
	}

	@Test
	public void existeLocalidadNullTest() {

		g = new GrafoLocalidad(costoPorKm, porcentajeSupera300Km, costoPorKm);
		assertFalse(g.existeVertice(null));

	}

	@Test
	public void existeLocalidadNoEsta() {
		Localidad localidadNoAgregada = new Localidad("Matanza", "Buenos Aires", -40, -30);
		assertFalse(g.existeVertice(localidadNoAgregada));
	}

	@Test
	public void existeLocalidadSiEsta() {
		Localidad localidadYaAgregada = new Localidad("San Miguel", "Buenos Aires", 50, 30);
		assertTrue(g.existeVertice(localidadYaAgregada));
	}

	@Test
	public void agregarLocalidad() {
		Localidad nuevaLocalidad = new Localidad("Capital Federal", "Buenos Aires", 60, 40);
		g.agregarVertice(nuevaLocalidad);
		assertTrue(g.existeVertice(nuevaLocalidad));
	}

	@Test
	public void agregarLocalidadQueYaEstaba() {
		Localidad localidadYaExistia = new Localidad("Jose.C.Paz", "Buenos Aires", 60, 40);
		g.agregarVertice(localidadYaExistia);
		assertTrue(g.existeVertice(localidadYaExistia)); // No la agrega solo se fija en el Nombre y Provincia
		assertTrue(g.existeVertice(new Localidad("Jose.C.Paz", "Buenos Aires", 60, 40)));
	}

	@Test
	public void agregarLocalidadNull() {
		g.agregarVertice(null);
	}

	@Test
	public void agregarArista() {
		g.agregarArista(jose_c_paz, bella_vista);
		assertTrue(g.existeArista(jose_c_paz, bella_vista));
		assertFalse(g.existeArista(jose_c_paz, moreno));
	}

	@Test
	public void agregoMismaArista() {
		g.agregarArista(jose_c_paz, bella_vista);
		g.agregarArista(jose_c_paz, bella_vista);
		assertTrue(g.existeArista(jose_c_paz, bella_vista));
	}

	@Test
	public void vecinosDeUnVericeHappyCase() {
		g.agregarArista(jose_c_paz, san_miguel);
		g.agregarArista(jose_c_paz, bella_vista);
		g.agregarArista(jose_c_paz, moreno);
		LinkedList<Localidad> vecinos = g.vecinos(jose_c_paz);

		assertTrue(vecinos.contains(san_miguel));
		assertTrue(vecinos.contains(bella_vista));
		assertTrue(vecinos.contains(moreno));
		assertFalse(vecinos.contains(pilar));
		assertFalse(vecinos.contains(villa_del_parque));
	}

	@Test
	public void mismosVecinosEnDosLocalidades() {
		g.agregarArista(jose_c_paz, san_miguel);
		g.agregarArista(jose_c_paz, bella_vista);
		g.agregarArista(jose_c_paz, moreno);
		LinkedList<Localidad> vecinosJoseCPaz = g.vecinos(jose_c_paz);

		g.agregarArista(pilar, san_miguel);
		g.agregarArista(pilar, bella_vista);
		g.agregarArista(pilar, moreno);
		LinkedList<Localidad> vecinosPilar = g.vecinos(pilar);

		for (Localidad vecinoJoseCPaz : vecinosJoseCPaz) {
			assertTrue(vecinosPilar.contains(vecinoJoseCPaz));
		}

	}

	@Test
	public void existeAristaNoEsta() {
		assertFalse(g.existeArista(jose_c_paz, bella_vista));
	}

	@Test
	public void existeAristaYsiEsta() {
		g.agregarArista(jose_c_paz, bella_vista);
		assertTrue(g.existeArista(jose_c_paz, bella_vista));
	}

	@Test
	public void existeAristaNULL() {
		g.agregarArista(null, bella_vista);
		assertFalse(g.existeArista(null, bella_vista));
	}

	@Test
	public void tamanioDos() {
		g = new GrafoLocalidad(costoPorKm, porcentajeSupera300Km, costoProvinciasDistintas,jose_c_paz, san_miguel);
		assertTrue(g.tamanio() == 2);

	}
	
	@Test
	public void tamanioTres() {
		g = new GrafoLocalidad(costoPorKm, porcentajeSupera300Km, costoProvinciasDistintas, jose_c_paz, san_miguel, villa_del_parque);
		assertTrue(g.tamanio() == 3);
	}
	
	@Test 
	public void eliminarVerticeNull() {
		g = new GrafoLocalidad(costoPorKm, porcentajeSupera300Km, costoProvinciasDistintas, jose_c_paz, san_miguel, villa_del_parque, moreno, bella_vista);
		assertTrue(g.tamanio() == 5);
		g.quitarVertice(null);
		assertTrue(g.tamanio() == 5);
	}
	
	@Test
	public void eliminarVerticeNoExiste() {
		g = new GrafoLocalidad(costoPorKm, porcentajeSupera300Km, costoProvinciasDistintas);
		g.quitarVertice(bella_vista);
		assertTrue(g.tamanio() == 0);
	}
	
	@Test
	public void eliminarUnVertice() {
		g = new GrafoLocalidad(costoPorKm, porcentajeSupera300Km, costoProvinciasDistintas, jose_c_paz, san_miguel, villa_del_parque, moreno, bella_vista);
		assertTrue(g.tamanio() == 5);
		g.quitarVertice(bella_vista);
		assertTrue(g.tamanio() == 4);
	}
	
	@Test
	public void eliminarDosVertices() {
		g = new GrafoLocalidad(costoPorKm, porcentajeSupera300Km, costoProvinciasDistintas, jose_c_paz, san_miguel, villa_del_parque, moreno, bella_vista);
		assertTrue(g.tamanio() == 5);
		g.quitarVertice(bella_vista);
		g.quitarVertice(jose_c_paz);
		assertTrue(g.tamanio() == 3);
	}
	
	@Test
	public void eliminarDosVerticesYquedaVacioElGrafo() {
		g = new GrafoLocalidad(costoPorKm, porcentajeSupera300Km, costoProvinciasDistintas, jose_c_paz, san_miguel);
		assertTrue(g.tamanio() == 2);
		g.quitarVertice(jose_c_paz);
		g.quitarVertice(san_miguel);
		assertTrue(g.tamanio() == 0);
	}
	
	/** ---------- GRAFO IS AGM ----------- **/
	@Test
	public void grafoEsAGM() {
		g = inicializarGrafoCompleto();
		g = ArbolGeneradorMinimo.prim(g);
		assertTrue(g.isAGM());
	}
	
	@Test
	public void grafoNoEsAGM() {
		g = inicializarGrafoInconexo();
		assertFalse(g.isAGM());
	}
	
	private GrafoLocalidad inicializarGrafoInconexo() 
	{
		GrafoLocalidad g = new GrafoLocalidad(costoPorKm, porcentajeSupera300Km, costoProvinciasDistintas, jose_c_paz, pilar, san_miguel, villa_del_parque, bella_vista);
		
		g.agregarArista(jose_c_paz, moreno);
		g.agregarArista(jose_c_paz, pilar);
		g.agregarArista(villa_del_parque, san_miguel);
		
		return g;		
	}
	
	private GrafoLocalidad inicializarGrafoCompleto() 
	{
		GrafoLocalidad g = new GrafoLocalidad(costoPorKm, porcentajeSupera300Km, costoProvinciasDistintas, jose_c_paz, pilar, san_miguel, villa_del_parque, bella_vista);
		
		g.agregarArista(jose_c_paz, pilar);
		g.agregarArista(jose_c_paz, san_miguel);
		g.agregarArista(jose_c_paz, villa_del_parque);
		g.agregarArista(jose_c_paz, bella_vista);
		g.agregarArista(pilar, san_miguel);
		g.agregarArista(pilar, villa_del_parque);
		g.agregarArista(pilar, bella_vista);
		g.agregarArista(san_miguel, villa_del_parque);
		g.agregarArista(san_miguel, bella_vista);
		g.agregarArista(villa_del_parque, bella_vista);

		return g;		
	}

	/** --------- Cambiar arista por otra ---------- **/
	@Test
	public void cambiarAristaPorOtraQueNoExiste() {
		g.agregarArista(jose_c_paz, bella_vista);
		
		assertFalse(g.existeArista(jose_c_paz, moreno));
		g.cambiarAristaPorOtra(jose_c_paz, bella_vista, moreno);
		
		assertTrue(g.existeArista(jose_c_paz, moreno));
		assertFalse(g.existeArista(jose_c_paz, bella_vista));
	}
	
	@Test
	public void cambiarAristaPorOtraQueEsLaMisma() {
		g.agregarArista(jose_c_paz, bella_vista);
		g.agregarArista(moreno, bella_vista);
		g.cambiarAristaPorOtra(jose_c_paz, bella_vista, bella_vista);
	}
	
	@Test
	public void cambiarAristaPorOtraQueEsNull() {
		g.agregarArista(jose_c_paz, bella_vista);
		g.cambiarAristaPorOtra(jose_c_paz, bella_vista, null);
	}
	
	@Test
	public void cambiarAristaMismosVerticesEnTodos() {
		g.agregarArista(jose_c_paz, bella_vista);
		g.cambiarAristaPorOtra(jose_c_paz, jose_c_paz, jose_c_paz);
	}
	/** --------- Verficar costos ---------- **/

	
	@Test
	public void costoSinLocalidad() {
		g=new GrafoLocalidad(costoPorKm, porcentajeSupera300Km, costoProvinciasDistintas);
		assertTrue(g.getCostoTotal().floatValue()==0);
		
	}
	@Test
	public void costoUnaLocalidad() {
		g=new GrafoLocalidad(costoPorKm, porcentajeSupera300Km, costoProvinciasDistintas);
		g.agregarVertice(bella_vista);
		assertTrue(g.getCostoTotal().floatValue()==0);
		
	}
	@Test
	public void costoDosLocalidades() {
		g=new GrafoLocalidad(costoPorKm, porcentajeSupera300Km, costoProvinciasDistintas);
		g.agregarVertice(bella_vista);
		g.agregarVertice(jose_c_paz);
		assertTrue(g.getCostoTotal().floatValue()==0);
		
	}
	@Test
	public void costoDosLocalidadesUnaArista() {
		g=new GrafoLocalidad(costoPorKm, porcentajeSupera300Km, costoProvinciasDistintas);
		g.agregarVertice(bella_vista);
		g.agregarVertice(jose_c_paz);
		g.agregarArista(jose_c_paz, bella_vista);

		assertTrue(g.getCostoTotal().floatValue()==costoTodasAristas(g));
		
	}
	@Test
	public void costoNLocalidadesNArista() {
		g=new GrafoLocalidad(costoPorKm, porcentajeSupera300Km, costoProvinciasDistintas);
		g.agregarVertice(bella_vista);
		g.agregarVertice(jose_c_paz);
		g.agregarVertice(villa_del_parque);
		g.agregarVertice(san_miguel);
		g.agregarArista(villa_del_parque, bella_vista);
		g.agregarArista(jose_c_paz, bella_vista);
		g.agregarArista(jose_c_paz, villa_del_parque);
		g.agregarArista(jose_c_paz, san_miguel);
		g.agregarArista(san_miguel, villa_del_parque);
		
		assertTrue(g.getCostoTotal().floatValue()==costoTodasAristas(g));
		
	}
	@Test
	public void costoDosLocalidadesSacoArista() {
		g=new GrafoLocalidad(costoPorKm, porcentajeSupera300Km, costoProvinciasDistintas);
		g.agregarVertice(bella_vista);
		g.agregarVertice(jose_c_paz);
		g.agregarArista(jose_c_paz, bella_vista);
		g.quitarArista(jose_c_paz, bella_vista);

		assertTrue(g.getCostoTotal().floatValue()==costoTodasAristas(g));
		
	}
	@Test
	public void costoNLocalidadesNAristaSacoAristas() {
		g=new GrafoLocalidad(costoPorKm, porcentajeSupera300Km, costoProvinciasDistintas);
		g.agregarVertice(bella_vista);
		g.agregarVertice(jose_c_paz);
		g.agregarVertice(villa_del_parque);
		g.agregarVertice(san_miguel);
		g.agregarArista(villa_del_parque, bella_vista);
		g.agregarArista(jose_c_paz, bella_vista);
		g.agregarArista(jose_c_paz, villa_del_parque);
		g.agregarArista(jose_c_paz, san_miguel);
		g.agregarArista(san_miguel, villa_del_parque);
		g.quitarArista(jose_c_paz, bella_vista);
		g.quitarArista(villa_del_parque, bella_vista);
		g.quitarArista(jose_c_paz, villa_del_parque);

		assertTrue(g.getCostoTotal().floatValue()==costoTodasAristas(g));
		
	}

	private float costoTodasAristas(GrafoLocalidad g) {
		BigDecimal costoTotal=new BigDecimal (0);
		costoTotal.setScale(2, RoundingMode.CEILING);
		
		for(CableDeRed cable:g.getAristas()) {
			
			costoTotal=costoTotal.add(cable.getCosto());
		}
		
		costoTotal=costoTotal.setScale(2,RoundingMode.CEILING);
		return costoTotal.floatValue();
	}
}
