package tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

import backend.CableDeRed;
import backend.Localidad;
import backend.Utilidades;

/* ----si los argumentos no son validos
 * nombre != ""
 * provincia != ""
 * -91<latitud<91 
 * -181<longitud<181
 */

public class CableDeRedTest {
	private Localidad san_miguel;
	private Localidad jose_c_paz;
	private Localidad muniz;
	private Localidad morris;

	double costoPorKm = 20;
	double porcentajeSupera300Km = 0.2;
	double costoProvinciasDistintas = 20;
	
	@Before
	public void setup() {
		san_miguel = new Localidad("San Miguel", "Buenos Aires", 50, 30);

		jose_c_paz = new Localidad("Jc.Paz", "Buenos Aires", 50, 29);
		muniz = new Localidad("Muñiz", "Buenos Aires", 10, 30);
		morris= new Localidad("Morris", "Cordoba", 10, 30);
	}
	
	@Test
	public void dosCableDeRedSonIguales() {
		BigDecimal costo = new BigDecimal(Utilidades.obtenerCosto(jose_c_paz, san_miguel, 20, 0.2, 20));
		CableDeRed conexion_1 = new CableDeRed(jose_c_paz, san_miguel, Utilidades.distanciaEnKm(jose_c_paz, san_miguel),costo);
		CableDeRed conexion_2 = new CableDeRed(jose_c_paz, san_miguel, Utilidades.distanciaEnKm(jose_c_paz, san_miguel),costo);

		assertTrue(conexion_1.equals(conexion_2));
	}
	
	@Test
	public void dosCablesDeRedNoSonIgualesDistanciaDistinta() {
		BigDecimal costo = new BigDecimal(Utilidades.obtenerCosto(jose_c_paz, san_miguel, 20, 0.2, 20));
		CableDeRed conexion_1 = new CableDeRed(jose_c_paz, san_miguel, Utilidades.distanciaEnKm(jose_c_paz, san_miguel),costo);
		CableDeRed conexion_2 = new CableDeRed(jose_c_paz, san_miguel, Utilidades.distanciaEnKm(jose_c_paz, san_miguel) + 10  ,costo);

		assertFalse(conexion_1.equals(conexion_2));
	}
	
	@Test
	public void dosCableDeRedSonIgualesCostoDistinto() {
		BigDecimal costo = new BigDecimal(Utilidades.obtenerCosto(jose_c_paz, san_miguel, 20, 0.2, 20));
		CableDeRed conexion_1 = new CableDeRed(jose_c_paz, san_miguel, Utilidades.distanciaEnKm(jose_c_paz, san_miguel),costo);
		
		costo = costo.add( new BigDecimal(100));
		CableDeRed conexion_2 = new CableDeRed(jose_c_paz, san_miguel, Utilidades.distanciaEnKm(jose_c_paz, san_miguel),costo);

		assertFalse(conexion_1.equals(conexion_2));
	}
	
	@Test
	public void dosCableDeRedSonIgualesLocalidadDistinta() {
		BigDecimal costo = new BigDecimal(Utilidades.obtenerCosto(jose_c_paz, san_miguel, 20, 0.2, 20));
		CableDeRed conexion_1 = new CableDeRed(jose_c_paz, san_miguel, Utilidades.distanciaEnKm(jose_c_paz, san_miguel),costo);
		CableDeRed conexion_2 = new CableDeRed(jose_c_paz, muniz, Utilidades.distanciaEnKm(jose_c_paz, san_miguel),costo);

		assertFalse(conexion_1.equals(conexion_2));
	}
	
	@Test
	public void localidadEstaEnCableDeRed() {
		BigDecimal costo = new BigDecimal(Utilidades.obtenerCosto(jose_c_paz, san_miguel, 20, 0.2, 20));
		CableDeRed conexion_1 = new CableDeRed(jose_c_paz, san_miguel, Utilidades.distanciaEnKm(jose_c_paz, san_miguel),costo);
		
		assertTrue(conexion_1.tieneEstaLocalidad(jose_c_paz));
		assertTrue(conexion_1.tieneEstaLocalidad(san_miguel));
	}
	
	@Test
	public void localidadNoEstaEnCableDeRed() {
		BigDecimal costo = new BigDecimal(Utilidades.obtenerCosto(jose_c_paz, san_miguel, 20, 0.2, 20));
		CableDeRed conexion_1 = new CableDeRed(jose_c_paz, san_miguel, Utilidades.distanciaEnKm(jose_c_paz, san_miguel),costo);
		
		assertFalse(conexion_1.tieneEstaLocalidad(morris));
	}

}
