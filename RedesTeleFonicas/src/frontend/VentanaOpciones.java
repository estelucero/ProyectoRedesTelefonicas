package frontend;

import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import backend.Sistema;
import controlador.Controlador;
import controlador.Controlador.VENTANA;


@SuppressWarnings("serial")
public class VentanaOpciones extends JFrame {

	JTextField costoPorKm;
	JTextField porcentajePorKm;
	JTextField costoPorProvincia;
	JButton guardar;

	JButton volver;
	JFrame error;
	
	public VentanaOpciones() {
		setUndecorated(true);
		setResizable(false);
		inicializar();
		crearVentana("Opciones",700, 300, 500, 350);
		agregarLabels();
		agregarTextfields();
		agregarbotonGuardar();

		agregarbotonVolver();
		
		 volver.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent e) {
					
				Controlador.abrirVentana(VENTANA.OPCIONES, VENTANA.PRINCIPAL);
				valoresSinGuardar();

				}

			});	 
		 guardar.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent e) {
					
				try {
					costosValidos();
					Controlador.setCostos(Double.parseDouble(costoPorKm.getText()), Double.parseDouble(porcentajePorKm.getText()), Double.parseDouble(costoPorProvincia.getText()));
					lanzarMensaje("Configuracion guardada con exito");
					Controlador.abrirVentana(VENTANA.OPCIONES, VENTANA.PRINCIPAL);
				}catch(Exception error){
					lanzarError(error.getMessage());
					valoresSinGuardar();
					
				}

				}

			});

	}
	
	
	private void valoresSinGuardar() {
		agregarCostoTextfield(costoPorKm);
		agregarCostoTextfield(porcentajePorKm);
		agregarCostoTextfield(costoPorProvincia);
	}
	private void costosValidos() {
		try {
			Double.parseDouble(costoPorKm.getText());
			Double.parseDouble(porcentajePorKm.getText());
			Double.parseDouble(costoPorProvincia.getText());
		}catch(Exception error) {
			throw new RuntimeException("Error los valores no pueden ser letras");
		}
	}
	

	private void agregarbotonGuardar() {

		guardar.setBounds(111,250,100, 35);
		guardar.setText("Guardar");
		guardar.setBackground(Color.white);
		guardar.setFont(new Font(Font.SANS_SERIF,Font.BOLD,15));
		getContentPane().add(guardar);
		
	}



	private void agregarbotonVolver() {
		volver.setBounds(250,250,100, 35);
		volver.setText("Volver");
		volver.setBackground(Color.white);
		volver.setFont(new Font(Font.SANS_SERIF,Font.BOLD,15));
		getContentPane().add(volver);

		
	}


	private void inicializar() {
		costoPorKm=new JTextField();
		porcentajePorKm=new JTextField();
		costoPorProvincia= new JTextField();
		guardar= new JButton();

		volver=new JButton();
		crearFrameError();

		
	}






	private void crearFrameError() {
		error = new JFrame();
		error.setBounds(500, 100, 200, 400);
		error.getContentPane().setLayout(null);
	}


	private void agregarTextfields() {
		int largo=30;
		int ancho=100;
		agregarTextfield(costoPorKm,370,120,ancho,largo,Sistema.PRECIOS.PORKILOMETROS.toString());
		agregarTextfield(porcentajePorKm,370,160,ancho,largo,Sistema.PRECIOS.PORCENTAJESUPERA300KM.toString());
		agregarTextfield(costoPorProvincia,370,200,ancho,largo,Sistema.PRECIOS.DOSPROVINCIASDISTINTAS.toString());

		
	}
	
	private void agregarCostoTextfield(JTextField textfield) {
		String costo= Controlador.getPrecioSistema(textfield.getName().toUpperCase())+"";
		textfield.setText(costo);
	}






	private void agregarTextfield(JTextField textfield,int x,int y,int ancho,int alto,String nombre) {
		textfield.setBounds(x, y, ancho, alto);
		textfield.setName(nombre);
		agregarCostoTextfield(textfield);

		getContentPane().add(textfield);
		
		
	}


	private void agregarLabels() {
		int tamanioTitulo=30;
		int tamanioOpciones=20;
		agregarLabel("Configuracion de Costos",63,0,360,50,tamanioTitulo);
		agregarLabel("Costo Por KM",30,120,150,30,tamanioOpciones);
		agregarLabel("Porcentaje Si Supera Los 300KM",30,160,330,30,tamanioOpciones);
		agregarLabel("Abono por Provincias Diferentes",30,200,330,30,tamanioOpciones);
		
	}


	private void agregarLabel(String msg,int x,int y,int ancho,int alto,int tamanio) {
		
		JLabel label = new JLabel(msg);
		label.setForeground(Color.BLACK);
		label.setBounds(x, y, ancho, alto);
		label.setBackground(Color.white);
		label.setFont(new Font(Font.SANS_SERIF,Font.BOLD,tamanio));
		getContentPane().add(label);
	}


	private void crearVentana(String titulo,int x,int y,int ancho,int alto) {
		// Opciones ventana
		setTitle("Conexion telefonicas");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(x, y, 480, 306);
		getContentPane().setLayout(null);
		
		getContentPane().setBackground(SystemColor.activeCaption);
	}
	private void lanzarError(String msg) {
		JOptionPane.showMessageDialog(error, msg);
	}
	private void lanzarMensaje(String msg) {
		JOptionPane.showInternalMessageDialog(this.getContentPane(), msg);
	}
}
