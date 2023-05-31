package backend;

import java.math.BigDecimal;

//arista: calcula la distancia entre 2 localidades.
public class CableDeRed {
	

	private Localidad primerLocalidad;
	private Localidad segundaLocalidad;
	private Double distanciaEnKm;
	private BigDecimal costo;

	public CableDeRed(Localidad v1, Localidad v2, double distancia, BigDecimal costo) {
		if(v1 == null || v2 == null)
			throw new IllegalArgumentException("No pueden ser nulos los argumentos.");
			
		primerLocalidad = v1;
		segundaLocalidad = v2;
		distanciaEnKm = distancia;
		this.costo = costo;
	}


	public boolean tieneEstaLocalidad(Localidad localidad) {

		return primerLocalidad.equals(localidad) || segundaLocalidad.equals(localidad);
	}

	@Override
	public boolean equals(Object otroCableDeRed) {
		if (otroCableDeRed == null || otroCableDeRed.getClass() != this.getClass())
			return false;

		CableDeRed otroCable = (CableDeRed) otroCableDeRed;

		return ((this.primerLocalidad.equals(otroCable.primerLocalidad)
				&& this.segundaLocalidad.equals(otroCable.segundaLocalidad))
				|| (this.segundaLocalidad.equals(otroCable.primerLocalidad))
						&& this.primerLocalidad.equals(otroCable.segundaLocalidad)) 
						&& this.distanciaEnKm.equals(otroCable.getDistancia())
						&& this.costo.equals(otroCable.getCosto());
	}


	public BigDecimal getCosto() {
		return costo;
	}

	public Double getDistancia() {
		return distanciaEnKm;
	}

	public Localidad getVertice1() {
		return primerLocalidad;
	}

	public Localidad getVertice2() {
		return segundaLocalidad;
	}

}
