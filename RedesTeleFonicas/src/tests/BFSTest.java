package tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;

import backend.BFS;
import backend.GrafoLocalidad;
import backend.Localidad;

public class BFSTest {	
	
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
	public void setup() 
	{
		jose_c_paz = new Localidad("Jose.C.Paz", "Buenos Aires", 20, 30);
		san_miguel = new Localidad("San Miguel", "Buenos Aires", 50, 30);
		pilar = new Localidad("Pilar", "Buenos Aires", 10, 30);
		bella_vista = new Localidad("Bella Vista", "Buenos Aires", 70, 30);
		moreno = new Localidad("Moreno", "Buenos Aires", 80, 42);
		villa_del_parque = new Localidad("Villa Del Parque", "Buenos Aires", 48, 23);
		g = new GrafoLocalidad(costoPorKm, porcentajeSupera300Km, costoProvinciasDistintas);
	}
	

	@Test
	public void alcanzablesHappyTest() 
	{
		GrafoLocalidad g = new GrafoLocalidad(costoPorKm, porcentajeSupera300Km, costoProvinciasDistintas, jose_c_paz, pilar, san_miguel, villa_del_parque, bella_vista);
		
		g.agregarArista(jose_c_paz, bella_vista);
		g.agregarArista(bella_vista, villa_del_parque);
		g.agregarArista(villa_del_parque, san_miguel);
		g.agregarArista(jose_c_paz, pilar);
		
		assertTrue(BFS.esConexo(g));
	}
	
	
	@Test
	public void grafoInconexoTest() 
	{
		g = inicializarGrafoInconexo();
		assertFalse(BFS.esConexo(g));
	}
	
	
	
	@Test
	public void grafoCompletoTest() 
	{
		g = inicializarGrafoCompleto();
		assertTrue(BFS.esConexo(g));
	}
	
	@Test
	public void alcanzablesGrafoCompletoTest() 
	{
		LinkedList<Localidad> esperadas = new LinkedList<Localidad>();
		esperadas.add(jose_c_paz);
		esperadas.add(san_miguel);
		esperadas.add(bella_vista);
		esperadas.add(moreno);
		esperadas.add(villa_del_parque);
		esperadas.add(pilar);
		
		GrafoLocalidad g = inicializarGrafoCompleto();

		
		for(Localidad alcanzada: BFS.alcanzables(g, jose_c_paz)) {
			assertTrue(esperadas.contains(alcanzada));
		}
	}

	
	@Test
	public void alcanzablesInconexoTest() 
	{
		LinkedList<Localidad> esperadas = new LinkedList<Localidad>();
		esperadas.add(jose_c_paz);
		esperadas.add(moreno);
		esperadas.add(pilar);
		
		g = inicializarGrafoInconexo();
		
		for(Localidad alcanzada: BFS.alcanzables(g, jose_c_paz)) {
			assertTrue(esperadas.contains(alcanzada));
		}
	}
	
	GrafoLocalidad inicializarGrafoInconexo() 
	{

		GrafoLocalidad g = new GrafoLocalidad(costoPorKm, porcentajeSupera300Km, costoProvinciasDistintas, jose_c_paz, pilar, san_miguel, villa_del_parque, bella_vista);
		
		g.agregarArista(jose_c_paz, moreno);
		g.agregarArista(jose_c_paz, pilar);
		g.agregarArista(villa_del_parque, san_miguel);
		
		return g;		
	}
	
	GrafoLocalidad inicializarGrafoCompleto() 
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

	
}
