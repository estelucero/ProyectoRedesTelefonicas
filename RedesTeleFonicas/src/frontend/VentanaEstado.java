package frontend;

import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.AbstractListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import backend.Localidad;
import backend.Sistema;
import controlador.Controlador;

public class VentanaEstado extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JPanel contentPane;
	private JButton btnIntercambiar;
	
	private JScrollPane verticesOrigen;
	private JScrollPane verticesDestino;
	private JScrollPane verticesNuevoDestino;
	
	
	private  Map<String, Localidad> localidades; 
	@SuppressWarnings("rawtypes")
	private JList listaOrigen; 
	@SuppressWarnings("rawtypes")
	private JList listaDestino;
	@SuppressWarnings("rawtypes")
	private JList listaNuevoDestino;

	public VentanaEstado() {
		
		setBounds(1050, 300, 658, 470);
		contentPane = new JPanel();
		contentPane.setBackground(SystemColor.activeCaption);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		

		/** Intercambiar Boton **/
		btnIntercambiar = new JButton("Intercambiar");
		btnIntercambiar.setBounds(270, 401, 107, 23);
		contentPane.add(btnIntercambiar);
		
		/*+ Las listas se inicializan an setVisible de la ventana usando la funcion inicializarListas() **/
		
		btnIntercambiar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(camposInvalidos()) {
					System.out.println("RELLENE LOS CAMPOS");
				}else {
					try {
						Localidad v1 = localidades.get((String)listaOrigen.getSelectedValue());
						Localidad v2 = localidades.get((String) listaDestino.getSelectedValue());
						Localidad v3 = localidades.get((String) listaNuevoDestino.getSelectedValue());
						Sistema.cambiarAristaPorOtra(v1, v2, v3);
						Controlador.intercambiarAristaNotificar();
						DesmarcarListas();
					}catch(Exception ex) {
						JOptionPane.showMessageDialog(contentPane, ex.getMessage());
					}
		
				}

				
			}
		});
		
	}

	protected void DesmarcarListas() {
		listaOrigen.clearSelection();
		listaDestino.clearSelection();
		listaNuevoDestino.clearSelection();
	}

	protected boolean camposInvalidos() {
		if(localidades == null ) {
			return true;
		}
		
		if(listaOrigen.getSelectedValue() == null || listaDestino.getSelectedValue() == null || 
				listaNuevoDestino.getSelectedValue() == null) {
			return true;
		}
		return false;
	}

	@SuppressWarnings({ "unchecked", "serial", "rawtypes" })
	private void inicializarListas() {		
		
			
		// ------------- LISTA ORIGEN -------------
		JLabel lblOrigen = new JLabel("ORIGEN");
		lblOrigen.setHorizontalAlignment(SwingConstants.CENTER);
		lblOrigen.setBounds(10, 17, 200, 14);
		contentPane.add(lblOrigen);
		
	    listaOrigen = new JList();
		listaOrigen.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		listaOrigen.setModel( new AbstractListModel() {
			String[] values =  obtenerVertices();
			
			@Override
			public int getSize() {
				return values.length;
			}

			@Override
			public Object getElementAt(int index) {
				return values[index];
			}
			
		});
		
		verticesOrigen = new JScrollPane(listaOrigen);
		verticesOrigen.setBounds(10, 42, 200, 348);
		contentPane.add(verticesOrigen);
		
		// ------------- LISTA DESTINO --------------
		JLabel lblDestino = new JLabel("DESTINO");
		lblDestino.setHorizontalAlignment(SwingConstants.CENTER);
		lblDestino.setBounds(220, 17, 200, 14);
		contentPane.add(lblDestino);
		
		
		listaDestino = new JList();
		listaDestino.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);	
		
		verticesDestino = new JScrollPane(listaDestino);
		verticesDestino.setBounds(220, 42, 200, 348);
		contentPane.add(verticesDestino);
		
		// -------------- LISTA NUEVO DESTINO -----------
		JLabel lblNuevoDestino = new JLabel("NUEVO DESTINO");
		lblNuevoDestino.setHorizontalAlignment(SwingConstants.CENTER);
		lblNuevoDestino.setBounds(430, 17, 200, 14);
		contentPane.add(lblNuevoDestino);

		
		verticesNuevoDestino = new JScrollPane();
		verticesNuevoDestino.setBounds(430, 42, 200, 348);
		contentPane.add(verticesNuevoDestino);
		
		listaNuevoDestino = new JList();
		listaNuevoDestino.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		verticesNuevoDestino.setViewportView(listaNuevoDestino);
		
		
		listaOrigen.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				listaDestino.setModel(new AbstractListModel() {
					String[] values = obtenerVecinos( (String) listaOrigen.getSelectedValue());

					public int getSize() {
						return values.length;
					}

					public Object getElementAt(int index) {
						return values[index];
					}
				});

				
			}
		});
		
		listaDestino.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				listaNuevoDestino.setModel(new AbstractListModel() {
					String[] values = obtenerPosiblesVertices( (String) listaOrigen.getSelectedValue(), (String) listaDestino.getSelectedValue());

					public int getSize() {
						return values.length;
					}

					public Object getElementAt(int index) {
						return values[index];
					}
				});
			}
		});
	}
	
	protected String[] obtenerPosiblesVertices(String vertice, String destino) {	
		if(vertice == null || destino == null) {
			return new String[0];
		}
		
		List<Localidad> posiblesNuevosDestinos = Sistema.listaDeNuevosDestinosAcambiar(localidades.get(vertice), localidades.get(destino));
		
		if(posiblesNuevosDestinos == null || posiblesNuevosDestinos.size() == 0) {
			return new String[0];
		}	
		
		String[] nuevosDestinos = new String[posiblesNuevosDestinos.size()];
		
		int i = 0;
		for(Localidad localidad: posiblesNuevosDestinos) {
			nuevosDestinos[i] = localidad.getProvincia() + "," + localidad.getNombre();
			i++;
		}
		
		return nuevosDestinos;
	}

	protected String[] obtenerVertices() {
		
		localidades = new HashMap<String, Localidad>();
		
		Set<Localidad> localidadesSistema = Sistema.localidadesCargadas();
		String[] vertices = new String[localidadesSistema.size()];
	
		// Esto para poder mapear las selecciones de la lista
		int i = 0;
		for(Localidad localidad: localidadesSistema) {
			vertices[i] = localidad.getProvincia() + "," + localidad.getNombre();
			localidades.put(vertices[i], localidad);
			i++;
		}
		
		
		return vertices;
	}

	private String[] obtenerVecinos(String seleccionOrigen) {
		if(seleccionOrigen == null) {
			return new String[0];
		}
		
		List<Localidad> posiblesDestinos = Sistema.listaDePosiblesDestinosAcambiar(localidades.get(seleccionOrigen));		
		
		if(posiblesDestinos == null || posiblesDestinos.size() == 0) {
			return new String[0];
		}
		
		String[] vecinos = new String[posiblesDestinos.size()];
		
		int i = 0;
		for(Localidad localidad: posiblesDestinos) {
			vecinos[i] = localidad.getProvincia() + "," + localidad.getNombre();
			i++;
		}
		
		return vecinos;		
	}

	
	
	@Override
	public void setVisible(boolean visible) {
		if(visible) inicializarListas();
		super.setVisible(visible);
	}
	
}
