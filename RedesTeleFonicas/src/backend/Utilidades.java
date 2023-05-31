package backend;

public class Utilidades {

	
	private static int radioTierraKm = 6371;
	
	// calcula la distancia entre localidades y las pone en kilometros con la
	// formula de haversine
	public static double distanciaEnKm(Localidad localidadUno, Localidad localidadDos) {

		double x1 = gradosARadianes(localidadUno.getLatitud());
		double y1 = gradosARadianes(localidadUno.getLongitud());
		double x2 = gradosARadianes(localidadDos.getLatitud());
		double y2 = gradosARadianes(localidadDos.getLongitud());

		double diferenciaEntreLongitudes = (y2 - y1);
		double diferenciaEntreLatitudes = (x2 - x1);

		double Haversine = Math.pow(Math.sin(diferenciaEntreLatitudes / 2.0), 2)
				+ Math.cos(x1) * Math.cos(x2) * Math.pow(Math.sin(diferenciaEntreLongitudes / 2.0), 2);

		double kmDistancia = 2 * Math.atan2(Math.sqrt(Haversine), Math.sqrt(1 - Haversine));

		return radioTierraKm * kmDistancia;
	}
		
	private static double gradosARadianes(float grados) {
		return grados * Math.PI / 180;
	}
	
	public static double obtenerCosto(Localidad v1, Localidad v2, double costoPorKm, double porcentajeSupera300Km, double costoProvinciasDistancias) {
		double distancia = distanciaEnKm(v1, v2);
		double costoTotal = distancia * costoPorKm;
		boolean distanciaSupera300km = distancia > 300;
		boolean provinciasDistintas = v1.getProvincia().equals(v2.getProvincia());
		costoTotal =  distanciaSupera300km? costoTotal + costoTotal * porcentajeSupera300Km: costoTotal; 
		costoTotal = provinciasDistintas? costoTotal + costoProvinciasDistancias: costoTotal;
		return costoTotal;
	}
}
