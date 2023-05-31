package tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import backend.GrafoLocalidadCompleto;
import backend.Localidad;

public class GrafoLocalidadCompletoTest {
	GrafoLocalidadCompleto grafo;
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
	public void setUp() {
		jose_c_paz = new Localidad("Jose.C.Paz", "Buenos Aires", 20, 30);
		san_miguel = new Localidad("San Miguel", "Buenos Aires", 50, 30);
		pilar = new Localidad("Pilar", "Buenos Aires", 10, 30);
		bella_vista = new Localidad("Bella Vista", "Buenos Aires", 70, 30);
		moreno = new Localidad("Moreno", "Buenos Aires", 80, 42);
		villa_del_parque = new Localidad("Villa Del Parque", "Buenos Aires", 48, 23);
		
		grafo=new GrafoLocalidadCompleto(costoPorKm, porcentajeSupera300Km, costoProvinciasDistintas);
	}
	
	@Test
	public void grafoEstaCompletoSinAristaTest() {
		assertTrue(grafo.estaCompleto());
	}
	
	@Test
	public void grafoEstaCompleto1AristaTest() {
		grafo.agregarVertice(jose_c_paz);
		assertTrue(grafo.estaCompleto());
	}
	
	@Test 
	public void grafoEstaCompleto2AristaTest() {
		grafo.agregarVertice(bella_vista);
		grafo.agregarVertice(jose_c_paz);
		assertTrue(grafo.estaCompleto());
	}
	@Test
	public void grafoEstaCompletoMuchasAristasTest() {
		grafo.agregarVertice(bella_vista);
		grafo.agregarVertice(jose_c_paz);
		grafo.agregarVertice(pilar);
		grafo.agregarVertice(villa_del_parque);
		grafo.agregarVertice(moreno);
		assertTrue(grafo.estaCompleto());
		
	}
	@Test
	public void grafoEstaCompletoSacoVertice() {
		grafo.agregarVertice(bella_vista);
		grafo.agregarVertice(jose_c_paz);
		grafo.agregarVertice(pilar);
		grafo.agregarVertice(villa_del_parque);
		grafo.agregarVertice(moreno);
		grafo.quitarVertice(jose_c_paz);
		assertTrue(grafo.estaCompleto());
	}
	@Test
	public void grafoEstaCompletoVacioSacoVertice() {

		grafo.quitarVertice(jose_c_paz);
		assertTrue(grafo.estaCompleto());
	}
	
	@Test(expected = RuntimeException.class)
	public void agregoAristaTest() {
		grafo.agregarArista(jose_c_paz, bella_vista);
	}
	@Test(expected = RuntimeException.class)
	public void sacoAristaTest() {
		grafo.quitarArista(jose_c_paz, bella_vista);;
	}
	
	@Test
	public void eliminarUnVertice() {
		grafo.agregarVertice(bella_vista);
		assertTrue(grafo.tamanio() == 1);
		grafo.quitarVertice(bella_vista);
		assertTrue(grafo.tamanio() == 0);
	}
	
	@Test
	public void eliminarDosVertices() {
		grafo.agregarVertice(bella_vista);
		grafo.agregarVertice(san_miguel);
		assertTrue(grafo.tamanio() == 2);
		grafo.quitarVertice(bella_vista);
		grafo.quitarVertice(san_miguel);
		assertTrue(grafo.tamanio() == 0);
	}
	
	@Test
	public void eliminoVerticesGrafoSigueCompletoTest() {
		grafo.agregarVertice(bella_vista);
		grafo.agregarVertice(jose_c_paz);
		grafo.agregarVertice(san_miguel);
		grafo.agregarVertice(pilar);
		grafo.agregarVertice(villa_del_parque);
		assertTrue(grafo.estaCompleto());
		assertTrue(grafo.tamanio() == 5);
		
		grafo.quitarVertice(bella_vista);
		assertTrue(grafo.estaCompleto());
		assertTrue(grafo.tamanio() == 4);
		
		grafo.quitarVertice(san_miguel);
		assertTrue(grafo.estaCompleto());
		assertTrue(grafo.tamanio() == 3);;
	}

}
