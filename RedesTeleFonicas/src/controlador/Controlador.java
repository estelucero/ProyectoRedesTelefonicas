package controlador;

import java.math.BigDecimal;
import java.util.EnumMap;

import javax.swing.JFrame;

import backend.GrafoLocalidad;
import backend.Sistema;
import frontend.VentanaEstado;
import frontend.VentanaOpciones;
import frontend.VentanaPrincipal;


public class Controlador {

	public enum VENTANA{ PRINCIPAL, OPCIONES, ESTADO}
	
	final static EnumMap<VENTANA, JFrame> ventanas = cargarVentanas();
	
	private static EnumMap<VENTANA, JFrame> cargarVentanas() {
		EnumMap<VENTANA, JFrame> v = new EnumMap<VENTANA, JFrame>(VENTANA.class);
		v.put(VENTANA.PRINCIPAL, new VentanaPrincipal());
		v.put(VENTANA.OPCIONES, new VentanaOpciones());
		v.put(VENTANA.ESTADO, new VentanaEstado());
		return v;
	}
	
	public static void abrirVentana(VENTANA ventana) {
		if(VENTANA.ESTADO == ventana) {
			ventanas.get(ventana).dispose();
			ventanas.put(ventana, new VentanaEstado());
		}
		ventanas.get(ventana).setVisible(true);
	}
	
	public static void abrirVentana(VENTANA ventanaQueCierra, VENTANA ventanaQueAbre) {
		ventanas.get(ventanaQueCierra).setVisible(false);
		ventanas.get(ventanaQueAbre).setVisible(true);
	}
		
	public Controlador() {
		Sistema.inicializar();
	}
	
	public static boolean cargarLocalidad(String nombre, String provincia, Float latitud, Float longitud)
	{
		
		return Sistema.cargarLocalidad(nombre, provincia, latitud, longitud);
	}
	public  boolean cargarLocalidad(String localidad, String provincia) {
		return Sistema.cargarLocalidad(localidad, provincia);
	}
	public static GrafoLocalidad obtenerGrafoAGM() {
		
		Sistema.crearGrafoCompleto();
		Sistema.crearGrafoAGM();
		
		return Sistema.obtenerGrafoAGM();
	}
	public static void reiniciarSistema() {
		Sistema.reiniciar();
		ventanas.get(VENTANA.ESTADO).setVisible(false);
	}
	public boolean guardarGrafoAGM(String nombreArchivo) {
		return Sistema.guardarAGM(nombreArchivo);
	}
	public boolean cargarGrafoAGM(String direccionArchivo) {
		return Sistema.cargarGrafoAGM(direccionArchivo);
	}
	
	public static boolean estaGrafoVacio() {
		return Sistema.estaGrafoVacio();
	}
	public static BigDecimal obtenerCostoTotalAGM() {
		return Sistema.obtenerCostoTotalAGM();
	}
	
	public static void setCostos(double costoPorKm,double porcentajePor300Km,double costoProvinciaDistintas) {
		Sistema.setCostoPorKm(costoPorKm);
		Sistema.setPorcentajeSupera300Km(porcentajePor300Km);
		Sistema.setCostoProvinciaDistinta(costoProvinciaDistintas);
	}


	public static double getPrecioSistema(String precioPedido) {
		if(precioPedido.toUpperCase().equals(Sistema.PRECIOS.PORKILOMETROS.toString())) {
			return Sistema.getPrecioBase(Sistema.PRECIOS.PORKILOMETROS);
		}else if(precioPedido.toUpperCase().equals(Sistema.PRECIOS.PORCENTAJESUPERA300KM.toString())) {
			return Sistema.getPrecioBase(Sistema.PRECIOS.PORCENTAJESUPERA300KM);
		}else if(precioPedido.toUpperCase().equals(Sistema.PRECIOS.DOSPROVINCIASDISTINTAS.toString())){
			return Sistema.getPrecioBase(Sistema.PRECIOS.DOSPROVINCIASDISTINTAS);
		}
		return -1;
	}
	
	public static String[] getProvincias() {
		 String[] itemsArray = new String[Sistema.provinciasDisponibles().size()];
	        itemsArray = Sistema.provinciasDisponibles().toArray(itemsArray);
	        return itemsArray;
	}
	public static String[] getLocalidades(String provincia) {
		String[] itemsArray = new String[Sistema.localidadesDisponibles(provincia).size()];
       itemsArray = Sistema.localidadesDisponibles(provincia).toArray(itemsArray);
       return itemsArray;
       
	}

	public static void intercambiarAristaNotificar() {
		((VentanaPrincipal) ventanas.get(VENTANA.PRINCIPAL)).actualizarMapa();
	}

}
