package frontend;

import java.awt.Color;
import java.awt.Point;

import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.MapMarkerDot;
import org.openstreetmap.gui.jmapviewer.MapPolygonImpl;
import org.openstreetmap.gui.jmapviewer.interfaces.MapMarker;
import org.openstreetmap.gui.jmapviewer.interfaces.MapPolygon;

import backend.CableDeRed;
import backend.GrafoLocalidad;
import backend.Localidad;

@SuppressWarnings("serial")
public class MapaPerzonalizado extends JMapViewer {
	private int minZoomPosible;
	private int maxZoomPosible;

	public MapaPerzonalizado() {
		this.minZoomPosible = 3;
		this.maxZoomPosible = 13;
	}

	public int getMinZoomPosible() {
		return minZoomPosible;
	}

	public void setMaxZoomLevel(int minZoom) {
		this.minZoomPosible = minZoom;
	}

	@Override
	public void setZoom(int zoom, Point mapPoint) {
		if (zoom > getMinZoomPosible() && zoom < getMaxZoomPosible())
			super.setZoom(zoom, mapPoint);
	}

	private int getMaxZoomPosible() {
		return maxZoomPosible;

	}

	public void agregarMarcador(double x, double y, String localidad) {
		Coordinate cordenada = new Coordinate(x, y);
		MapMarker marcador = new MapMarkerDot(localidad, cordenada);
		marcador.getStyle().setBackColor(Color.blue);
		marcador.getStyle().setColor(new Color(255, 251, 0));
		this.addMapMarker(marcador);

	}

	public void agregarPoligono(Float x1, Float y1, Float x2, Float y2, String peso) {
		Coordinate cordenada1 = new Coordinate(x1, y1);
		Coordinate cordenada2 = new Coordinate(x2, y2);
		MapPolygon poligono = new MapPolygonImpl(peso, cordenada1, cordenada2, cordenada1);
		poligono.getStyle().setColor(new Color(182, 179, 26));

		this.addMapPolygon(poligono);
	}

	public void agregarAGM(GrafoLocalidad grafo) {
		try {
			this.removeAllMapPolygons();
			this.removeAllMapMarkers();
			for(Localidad localidad:grafo.getLocalidades()) {
				this.agregarMarcador(localidad.getLatitud(), localidad.getLongitud(), localidad.getNombre());
			}
			for (CableDeRed cable : grafo.getAristas()) {
				this.agregarPoligono(cable.getVertice1().getLatitud(), cable.getVertice1().getLongitud(),
						cable.getVertice2().getLatitud(), cable.getVertice2().getLongitud(),
						"$" + cable.getCosto().toString());
			}
		} catch (Exception e) {

		}

	}

	public void reiniciar() {
		this.removeAllMapPolygons();
		this.removeAllMapMarkers();
	}

}
