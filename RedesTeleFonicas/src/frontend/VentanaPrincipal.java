package frontend;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.openstreetmap.gui.jmapviewer.Coordinate;

import backend.Sistema;
import controlador.Controlador;
import controlador.Controlador.VENTANA;

import java.awt.SystemColor;
import javax.swing.SwingConstants;

@SuppressWarnings("serial")
public class VentanaPrincipal extends JFrame {

	private JPanel panelControl;
	private JPanel contentPane;
	private JPanel panelMapa;
	private MapaPerzonalizado mapa;
	private Controlador controlador;


	private JLabel costoTototalText;

	private JTextField provinciaTextField;
	private JTextField localidadTextField;
	private JTextField latitudTextField;
	private JTextField longitudTextField;

	private JScrollPane provinciaScrollPane;
	private JScrollPane localidadScrollPane;
	private JList<?> localidades;
	private JList<?> provincias;
	
	private JPanel panelControlAbajo;
	private JButton opciones;
	
	private JFrame error;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public VentanaPrincipal() {
		getContentPane().setBackground(SystemColor.inactiveCaption);
		
		controlador = new Controlador(); 
		
		// Opciones ventana
		setTitle("Conexion telefonicas");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(10, 100, 1000, 850);
		getContentPane().setLayout(null);
		setResizable(false);

		// Aca tenemos el panel de el mapa
		panelMapa = new JPanel();
		panelMapa.setOpaque(false);
		panelMapa.setBounds(10, 11, 600, 750);
		panelMapa.setLayout(null);

		getContentPane().add(panelMapa);
		mapa = new MapaPerzonalizado();
		mapa.setDisplayPosition(new Coordinate(-34.521, -58.7008), 9);

		mapa.setBounds(0, 0, 610, 750);

		panelMapa.add(mapa);

		// Aca tenemos el panel de controles
		panelControl = new JPanel();
		panelControl.setOpaque(false);
		panelControl.setBounds(609, 11, 375, 750);
		panelControl.setLayout(null);
		getContentPane().add(panelControl);
		// Aca tenemos el panel de controles de Abajo
		panelControlAbajo = new JPanel();
		panelControlAbajo.setOpaque(false);
		panelControlAbajo.setBounds(0, 770, 984, 41);
		panelControlAbajo.setLayout(null);
		getContentPane().add(panelControlAbajo);

		// --- frame de error ---
		error = new JFrame();
		error.setBounds(500, 100, 200, 400);
		error.getContentPane().setLayout(null);

		// --- JList de Provincias---

		provinciaScrollPane = new JScrollPane(provincias);
		provinciaScrollPane.setBounds(20, 90, 160, 300);

		panelControl.add(provinciaScrollPane);
		provincias = new JList();
		provincias.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		provincias.setModel(new AbstractListModel() {
			@SuppressWarnings("static-access")
			String[] values = controlador.getProvincias();

			public int getSize() {
				return values.length;
			}

			public Object getElementAt(int index) {
				return values[index];
			}
		});

		provincias.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				localidades.setModel(new AbstractListModel() {
					@SuppressWarnings("static-access")
					String[] values = controlador.getLocalidades((String) provincias.getSelectedValue());

					public int getSize() {
						return values.length;
					}

					public Object getElementAt(int index) {
						return values[index];
					}
				});

			}
		});

		provinciaScrollPane.setViewportView(provincias);
		panelControl.add(provinciaScrollPane);
		// --- JList de Localidades---
		localidadScrollPane = new JScrollPane();
		localidadScrollPane.setBounds(200, 90, 160, 300);
		panelControl.add(localidadScrollPane);

		localidades = new JList();

		localidadScrollPane.setViewportView(localidades);
		panelControl.add(localidadScrollPane);

		// --- boton de salir ---
		JButton botonSalir = new JButton("Salir");
		botonSalir.setBounds(900, 15, 70, 23);
		panelControlAbajo.add(botonSalir);
		botonSalir.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseReleased(MouseEvent e) {
				System.exit(0);
			}
		});

		// --- boton de Guardar ---
		JButton botonGuardar = new JButton("Guardar");
		botonGuardar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				guardar();
			}
		});
		botonGuardar.setBounds(165, 11, 135, 23);
		panelControlAbajo.add(botonGuardar);

		// --- boton de Cargar ---
		JButton botonCargar = new JButton("Cargar");
		botonCargar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cargar();
			}
		});
		botonCargar.setBounds(319, 11, 135, 23);
		panelControlAbajo.add(botonCargar);

		// --- boton de Añadir Localidad Personalizada ---
		JButton botonAnadirLocalidadPerzonalizada = new JButton("Añadir Localidad");

		botonAnadirLocalidadPerzonalizada.setBounds(90, 582, 200, 23);
		panelControl.add(botonAnadirLocalidadPerzonalizada);

		botonAnadirLocalidadPerzonalizada.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				
				if(!campoAgregarCompleto())
				{
					lanzarError("Error tiene que llenar todos los campos");
					return;
				}
				
				if(!campoAgregarSinError()) {
					lanzarError("Los campos estan mal completados");
					return;
				}
				
				
				
				else if(Controlador.cargarLocalidad(localidadTextField.getText(), provinciaTextField.getText(), Float.parseFloat(latitudTextField.getText()), Float.parseFloat(longitudTextField.getText()))){
					mapa.agregarMarcador(Float.parseFloat(latitudTextField.getText()), Float.parseFloat(longitudTextField.getText()), localidadTextField.getText());

				}else {
					lanzarError("La latitud y longitud estan fuera del rango de Argentina");
				}

			}

		});

		// --- boton de Añadir Localidad Predeterminada---
		JButton botonAnadirLocalidadPredeterminada = new JButton("Añadir Localidad");

		botonAnadirLocalidadPredeterminada.setBounds(90, 400, 200, 23);
		panelControl.add(botonAnadirLocalidadPredeterminada);

		botonAnadirLocalidadPredeterminada.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (localidades.getSelectedValue() == null || localidades.getSelectedValue().toString().isEmpty()) {
					lanzarError("Error tiene que llenar todos los campos");

				}

				else if (controlador.cargarLocalidad((String) localidades.getSelectedValue(),
						(String) provincias.getSelectedValue())) {
					Coordinate cordenada = Sistema.obtenerCoordenadasLocalidad((String) localidades.getSelectedValue(),
							(String) provincias.getSelectedValue());

					(mapa).agregarMarcador(cordenada.getLat(), cordenada.getLon(),
							(String) localidades.getSelectedValue());

				}

			}

		});

		// --- boton de Crear AGM ---
		JButton crearAGMBoton = new JButton("Crear AGM");

		crearAGMBoton.setBounds(90, 627, 200, 40);
		panelControl.add(crearAGMBoton);

		crearAGMBoton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!Controlador.estaGrafoVacio()) {
					mapa.agregarAGM(Controlador.obtenerGrafoAGM());
					costoTototalText.setText("$" + Controlador.obtenerCostoTotalAGM().toString());
					Controlador.abrirVentana(VENTANA.ESTADO);
				}else {
					lanzarMensaje("¡Debe ingresar localidades al mapa!");
				}
				
				
				
			}

		});
		// --- boton de Borrar---
		JButton borrar = new JButton("Limpiar Mapa");

		borrar.setBounds(474, 11, 135, 23);
		panelControlAbajo.add(borrar);

		borrar.addActionListener(new ActionListener() {
			@SuppressWarnings("static-access")
			public void actionPerformed(ActionEvent e) {
				mapa.reiniciar();
				controlador.reiniciarSistema();

			}

		});

		// --- Altitud Text Fields ---
		JLabel latitudLabel = new JLabel("Latitud:");
		latitudLabel.setHorizontalAlignment(SwingConstants.CENTER);
		latitudLabel.setBounds(20, 484, 45, 23);
		panelControl.add(latitudLabel);

		latitudTextField = new JTextField();
		latitudTextField.setBounds(75, 484, 78, 23);
		panelControl.add(latitudTextField);

		// --- Latitud Text Fields ---
		JLabel longitudLabel = new JLabel("Longitud:");
		longitudLabel.setHorizontalAlignment(SwingConstants.CENTER);
		longitudLabel.setBounds(20, 537, 53, 23);
		panelControl.add(longitudLabel);

		longitudTextField = new JTextField();
		longitudTextField.setBounds(75, 537, 78, 23);
		panelControl.add(longitudTextField);

		// --- Provincia Text Fields ---
		JLabel provinciaLabel = new JLabel("Provincia:");
		provinciaLabel.setHorizontalAlignment(SwingConstants.CENTER);
		provinciaLabel.setBounds(163, 484, 60, 23);
		panelControl.add(provinciaLabel);

		provinciaTextField = new JTextField();
		provinciaTextField.setBounds(231, 484, 134, 23);
		panelControl.add(provinciaTextField);

		// --- Localidad Text Fields ---
		JLabel localidadLabel = new JLabel("Localidad:");
		localidadLabel.setHorizontalAlignment(SwingConstants.CENTER);
		localidadLabel.setBounds(166, 537, 60, 23);
		panelControl.add(localidadLabel);

		localidadTextField = new JTextField();
		localidadTextField.setBounds(231, 537, 134, 23);
		panelControl.add(localidadTextField);

		// --- LocalidadPersonalizada Label ---
		JLabel localidadPersonalizada = new JLabel("Carga de Localidad Personalizada");
		localidadPersonalizada.setBounds(100, 450, 200, 23);
		panelControl.add(localidadPersonalizada);

		// --- ProvinciaLista Label ---
		JLabel provinciaListaLabel = new JLabel("Provincia");
		provinciaListaLabel.setHorizontalAlignment(SwingConstants.CENTER);
		provinciaListaLabel.setBounds(20, 60, 160, 23);
		panelControl.add(provinciaListaLabel);
		// --- LocalidadLista Label ---
		JLabel localidadListaLabel = new JLabel("Localidad");
		localidadListaLabel.setHorizontalAlignment(SwingConstants.CENTER);
		localidadListaLabel.setBounds(200, 60, 160, 23);
		panelControl.add(localidadListaLabel);

		// --- ProvinciaYLocalidadesPredefinidas Label ---

		JLabel predefinidasLabel = new JLabel("Lista de Provinicas y Localidades Predefinidas");
		predefinidasLabel.setHorizontalAlignment(SwingConstants.CENTER);
		predefinidasLabel.setBounds(0, 20, 360, 23);
		panelControl.add(predefinidasLabel);
		// --- Costo  ---

		JLabel costoLabel = new JLabel("Costo Total:");
		costoLabel.setHorizontalAlignment(SwingConstants.CENTER);
		costoLabel.setBounds(60, 700, 93, 23);
		costoLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		panelControl.add(costoLabel);
		
		 costoTototalText= new JLabel();
		 costoTototalText.setOpaque(true);
		 costoTototalText.setBounds(160, 700, 100, 25);
		 panelControl.add(costoTototalText);
		 
		 crearBotonOpciones();
		 
		 opciones.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent e) {
					
				Controlador.abrirVentana(VENTANA.OPCIONES);
				}

			});	 
		 
	}


	
	
	public JFrame getFrame() {
		return this;
	}
	
	private void crearBotonOpciones() {
		opciones=new JButton("Opciones");
		panelControlAbajo.add(opciones);
		opciones.setBounds(10, 11, 135, 23);
		
	}

	private boolean campoAgregarSinError() {
		try {
			Float.parseFloat(latitudTextField.getText());
			Float.parseFloat(longitudTextField.getText());
		}catch(Exception error) {
			return false;
		}
		
		return true;
		
	}

	private  boolean campoAgregarCompleto() {
		return !longitudTextField.getText().isBlank() && !latitudTextField.getText().isBlank()
				&& !provinciaTextField.getText().isBlank() && !localidadTextField.getText().isBlank();
	}
	


	private void lanzarError(String msg) {
		JOptionPane.showMessageDialog(error, msg);
	}
	
	private String lanzarInput(String msg) {
		String entrada = JOptionPane.showInputDialog(msg);
		if(entrada.isEmpty()) {
			lanzarInput(msg);
		}
		return entrada;
		
	}

	private void lanzarMensaje(String msg) {
		JOptionPane.showInternalMessageDialog(this.getContentPane(), msg);
	}

	private void guardar() {
		// Si el mapa tiene un grafo AGM 
		if(!mapa.getMapPolygonList().isEmpty()) {
			
			String nombreArchivo = lanzarInput("Ingrese el nombre del archivo: ");
			if(controlador.guardarGrafoAGM(nombreArchivo)) {
				lanzarMensaje("Se guardo con exito.");
			}
			else {
				lanzarMensaje("No se pudo guardar.");
			}
			
		}else {
			lanzarError("Primero cree el Grafo AGM para guardar.");
		}
	}
	


	protected void cargar() {
		//Creamos el objeto JFileChooser
		JFileChooser fc=new JFileChooser(System.getProperty("user.dir") + "//grafos//");
		 
		//Abrimos la ventana, guardamos la opcion seleccionada por el usuario
		int seleccion=fc.showOpenDialog(contentPane);
		
		//Si el usuario, pincha en aceptar
		if(seleccion==JFileChooser.APPROVE_OPTION){
		 
			String direccionArchivo = fc.getSelectedFile().getName();
			
			// Cargo el grafo en el Sistema
			if(controlador.cargarGrafoAGM(direccionArchivo)) {
				// Actualizo el mapa
				mapa.agregarAGM(Controlador.obtenerGrafoAGM());
				
				// Actualizo el costo
				costoTototalText.setText("$" + Controlador.obtenerCostoTotalAGM().toString());
			
				lanzarMensaje("Se cargo con exito el archivo.");
				
				Controlador.abrirVentana(VENTANA.ESTADO);
			};
			
			
		}
	}
	
	
	
	public void actualizarMapa() {
		mapa.removeAllMapMarkers();
		mapa.removeAllMapPolygons();
		mapa.agregarAGM(Sistema.obtenerGrafoAGM());
		costoTototalText.setText("$" + Sistema.obtenerCostoTotalAGM());
	}


}