package backend;

import java.util.Objects;

public class Localidad {
	private String nombre;
	private String provincia;
	private float latitud;
	private float longitud;

	public Localidad(String _nombreLocalidad, String _provinciaLocalidad, float _latitud, float _longitud) 
	{
		validarLocalidad(_nombreLocalidad, _provinciaLocalidad, _latitud, _longitud);
		
		this.nombre = _nombreLocalidad;
		this.provincia = _provinciaLocalidad;
		this.latitud = _latitud;
		this.longitud = _longitud;
	}

	private void validarLocalidad(String _nombreLocalidad, String _provinciaLocalidad, float _latitud,
			float _longitud) {
		if(_nombreLocalidad == null || _nombreLocalidad == "" || _provinciaLocalidad == null || _provinciaLocalidad == "") {
			throw new IllegalArgumentException("Localidad o provincia no pueden ser null o estar vacios.");
		}
		if(!(_latitud < 91 && _longitud < 181 && _latitud > -90 && _longitud > -181)) {
			throw new IllegalArgumentException("La latitud y longitud deben ser validas.");
		}
	};
	
	@Override
	public boolean equals(Object otraLocalidad) {
		if(otraLocalidad == null || otraLocalidad.getClass() != this.getClass()) 
		{
			return false;
		}
		
		Localidad otra = (Localidad) otraLocalidad;
		
		return this.provincia.equals(otra.getProvincia()) && this.nombre.equals(otra.getNombre())
				&& this.latitud == otra.getLatitud() && this.longitud == otra.getLongitud();
	}
	
	@Override
	public String toString() {
		String localidad = "LOCALIDAD: " + this.nombre;
		String provincia = " PROVINCIA: " + this.provincia;
		String latitud 	 = " LATITUD: "   + this.latitud;
		String longitud  = " LONGITUD: "  + this.longitud;
		
		return localidad + provincia + latitud + longitud;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(nombre, provincia);
	}
	
	public String getNombre() 
	{
		return nombre;
	}

	public String getProvincia() 
	{
		return provincia;
	}

	public Float getLatitud() 
	{
		return latitud;
	}

	public Float getLongitud() 
	{
		return longitud;
	}

}
