package tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import backend.Localidad;

public class LocalidadTest {
	
	Localidad localidad;
	
	// ----------si el nombre es invalido de la localidad.
	@Test(expected = IllegalArgumentException.class)
	public void nombreInvalidoTest() {
		localidad = new Localidad("", "Buenos Aires", 60,50);
	}

	// ----------si la provincia es invalida de la localidad.
	@Test(expected = IllegalArgumentException.class)
	public void provinciaInvalidaTest() {
		localidad = new Localidad("San Miguel", "", 60,50);
	}

	// si la latitud es menor a la valida de la localidad.
	@Test(expected = IllegalArgumentException.class)
	public void latitudMenorInvalidaTest() {
		localidad = new Localidad("Prueba", "Buenos Aires", -91, 30);
	}

	// si la latitud es invalida de la localidad.
	@Test(expected = IllegalArgumentException.class)
	public void latitudMayorInvalidaTest() {
		localidad = new Localidad("Prueba", "Buenos Aires", 91, 30);
	}

	// si la longitud es invalida de la localidad.
	@Test(expected = IllegalArgumentException.class)
	public void longitudMenorInvalidaTest() {
		localidad = new Localidad("Prueba", "Buenos Aires", 20, -181);
	}

	// si la longitud es invalida de la localidad.
	@Test(expected = IllegalArgumentException.class)
	public void longitudMayorInvalidaTest() {
		localidad = new Localidad("Prueba", "Buenos Aires", 20, 181);
	}

	@Test
	public void dosLocalidadesSonIguales() {
		Localidad localidad_1 = new Localidad("Prueba", "Buenos Aires", 60, 40);
		Localidad localidad_2 = new Localidad("Prueba", "Buenos Aires", 60, 40);
		assertTrue(localidad_1.equals(localidad_2));
	}
	
	@Test
	public void dosLocalidadesNoSonIgualesNombre() {
		Localidad localidad_1 = new Localidad("Prueba", "Buenos Aires", 60, 40);
		Localidad localidad_2 = new Localidad("PruebaNOIGUAL", "Buenos Aires", 60, 40);
		assertFalse(localidad_1.equals(localidad_2));
	}
	
	@Test
	public void dosLocalidadesNoSonIgualesProvincia() {
		Localidad localidad_1 = new Localidad("Prueba", "Buenos Aires", 60, 40);
		Localidad localidad_2 = new Localidad("Prueba", "Buenos AiresNOIGUAL", 60, 40);
		assertFalse(localidad_1.equals(localidad_2));
	}
	
	@Test
	public void dosLocalidadesNoSonIgualesLatitud() {
		Localidad localidad_1 = new Localidad("Prueba", "Buenos Aires", 80, 40);
		Localidad localidad_2 = new Localidad("Prueba", "Buenos Aires", 60, 40);
		assertFalse(localidad_1.equals(localidad_2));
	}
	
	@Test
	public void dosLocalidadesNoSonIgualesLongitud() {
		Localidad localidad_1 = new Localidad("Prueba", "Buenos Aires", 60, 40);
		Localidad localidad_2 = new Localidad("Prueba", "Buenos Aires", 60, 80);
		assertFalse(localidad_1.equals(localidad_2));
	}
	
}
