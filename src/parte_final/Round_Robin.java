package parte_final;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;

import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.GridLayout;
//import java.awt.SystemColor;
//import javax.swing.UIManager;

/**
 * 
 * 			TORAL MALDONADO ROSA GUADALUPE
 *					 2153045948
 *
 * 				SISTEMAS OPERATIVOS
 * 
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
 * Aplicación que muestra cómo funciona el algoritmo de planificación 
 * Round Robin para el manejo de procesos
 * 
 * La siguiente aplicación muestra una ventana en la que se puede visualizar 
 * el funcionamiento del algoritmo Round Robin. 
 * El usuario puede ingresar el número de ráfagas (pedazo de tiempo) para atender 
 * a los procesos que llegan. Así como podrá repetir el algoritmo con diferentes 
 * datos de procesos.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
 *
 * 
 * 					Versión: 1.0
 * 
 * Fecha de creación: 22 de junio de 2018
 *
 */
public class Round_Robin extends JFrame implements ActionListener{

	/**
	 * ID de la aplicación
	 */
	private static final long serialVersionUID = 1L;
	
	// Elementos gráficos
	private JButton establecer;
	private JButton nuevo;
	private JButton comenzar;
	private JTextField espera1;
	private JTextField respuesta1;
	private JTextField cambios1;
	private JTextField tiempo1;
	private JTextField cabecera1; // Nombre de la columna 1
	private JTextField cabecera2; // Nombre de la columna 2
	private JTextField cabecera3; // Nombre de la columna 3
	private JLabel espera2;
	private JLabel respuesta2;
	private JLabel cambios2;
	private JLabel tiempo2;
	private JLabel arribo;
	private JLabel rafagas;
	private JLabel FIFO;
	private JLabel planificador;
	private JLabel titulo;
	private JSeparator separador;
	private JPanel panel;
	
	// Constantes usadas para las tablas gráficas
	private static final int colTabla = 3;
	private static final int filTabla = 4;
	private static final int colPanel = 40;
	private static final int filPanel = 8;
	
	// Variables usadas en el algoritmo Round Robin
	private LinkedList<Proceso> fifo; // Arreglo que guarda los procesos a atender en el algoritmo
	private Proceso[] procesos; // Arreglo que guarda los procesos mostrados en la matriz
	private boolean atendiendo; // Variable que indica si se está atendiendo a un proceso. Al principio no atiende a nadie
	private ArrayList<Proceso> listaArribo; // Guarda todos los procesos que han llegado en el ciclo actual
	private Proceso p; // Variable que guarda al proceso que se atiende actualmente. Al principio no hay ningún proceso
						// La clase Proceso se declaró al final de este archivo.
	private int tiempo; // El pedazo de tiempo puesto por el usuario
	private int contador; // Cuenta el número de rafagas que se han atendido del proceso actual
	private int cambios; // Guarda el número de cambios hechos a lo largo del algoritmo
	
	// Objetos usados para mostrar los datos
	private JPanel table; // Tabla de procesos
	private JPanel panel2; // Tabla donde se muestran los cambios de los elementos usadosn en el algoritmo
	private JTextField[][] celdas; // Celdas de la tabla de procesos
	private JTextField[][] elementos; // Celdas de la tabla donde se muestran los cambios
	private Color[] colores; // Arreglo que guarda los colores de cada proceso
	
	/** Método main. Inicializa la ventana
	 * @param args
	 */
	public static void main(String[] args) {
		// No se crea una instancia debido a que sólo se necesita para mostrar la ventana
		new Round_Robin();
	}
	
	/** Constructor de la clase Round_Robin
	 */
	private Round_Robin(){
		// Caso para Mac. En Mac no se ven los colores. Se cambia el tema de la ventana para que se vean en Mac
		// try {
		// javax.swing.UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// UIManager.getLookAndFeelDefaults().put( "Button.background", Color.RED );
		// Se inicializa el arreglo de colores y los componentes
		colores = new Color[4];
		procesos = new Proceso[filTabla];
		colores[0] = Color.YELLOW;
		colores[1] = Color.GREEN;
		colores[2] = Color.BLUE;
		colores[3] = Color.MAGENTA;
		inicilizaComponentes();
	}
	
	/** Método para inicializar los componentes gráficos de la ventana 
	 */
	private void inicilizaComponentes() {
		// Se inicializan las matrices celdas y elementos
		celdas = new JTextField[filTabla][colTabla];
		elementos = new JTextField[filPanel][colPanel];

		getContentPane().setLayout(null);

		Font fuente = new Font("Currier New", Font.BOLD, 14);

		espera1 = new JTextField();
		espera1.setEditable(false);
		espera1.setFont(fuente);
		espera1.setBounds(60, 42, 128, 30);

		respuesta1 = new JTextField();
		respuesta1.setEditable(false);
		respuesta1.setFont(fuente);
		respuesta1.setBounds(60, 114, 128, 30);

		cambios1 = new JTextField();
		cambios1.setEditable(false);
		cambios1.setFont(fuente);
		cambios1.setBounds(60, 186, 128, 30);

		espera2 = new JLabel("Tiempo promedio de espera:");
		espera2.setBounds(40, 11, 215, 20);
		espera2.setFont(fuente);

		respuesta2 = new JLabel("Tiempo promedio de respuesta:");
		respuesta2.setBounds(40, 83, 235, 20);
		respuesta2.setFont(fuente);

		cambios2 = new JLabel("Cambios de proceso:");
		cambios2.setBounds(40, 155, 148, 20);
		cambios2.setFont(fuente);

		tiempo2 = new JLabel("Pedazo de tiempo:");
		tiempo2.setBounds(188, 359, 156, 20);
		tiempo2.setFont(fuente);

		arribo = new JLabel("Arribo");
		arribo.setFont(fuente);
		arribo.setBounds(40, 445, 67, 20);

		planificador = new JLabel("Planificador");
		planificador.setFont(fuente);
		planificador.setBounds(40, 475, 83, 20);

		rafagas = new JLabel("Rafagas");
		rafagas.setFont(fuente);
		rafagas.setBounds(40, 414, 60, 20);

		FIFO = new JLabel("FIFO");
		FIFO.setFont(fuente);
		FIFO.setBounds(40, 560, 83, 20);

		fuente = new Font("Berlin Sans FB Demi", Font.PLAIN, 14);

		cabecera1 = new JTextField();
		cabecera1.setEditable(false);
		cabecera1.setText("Procesos");
		cabecera1.setFont(fuente);
		cabecera1.setBounds(189, 95, 194, 40);

		cabecera2 = new JTextField();
		cabecera2.setEditable(false);
		cabecera2.setText("Tiempo de llegada / arribo");
		cabecera2.setFont(fuente);
		cabecera2.setBounds(383, 95, 194, 40);

		cabecera3 = new JTextField();
		cabecera3.setEditable(false);
		cabecera3.setText("Rafagas de tiempo");
		cabecera3.setFont(fuente);
		cabecera3.setBounds(577, 95, 194, 40);

		table = new JPanel();
		table.setBounds(188, 134, 584, 191);
		table.setBackground(new Color(240, 240, 240));
		table.setLayout(new GridLayout(filTabla, colTabla));

		inicializaTabla();

		panel2 = new JPanel();
		panel2.setBackground(new Color(240, 240, 240));
		panel2.setBounds(133, 414, 1031, 256);
		panel2.setLayout(new GridLayout(filPanel, colPanel));

		inicializaPanel();

		panel = new JPanel();
		panel.setBorder(BorderFactory.createTitledBorder(""));
		panel.setBounds(826, 95, 299, 227);
		panel.setVisible(true);
		panel.setLayout(null);

		establecer = new JButton("Establecer");
		establecer.setBounds(708, 357, 102, 26);
		establecer.setFont(fuente);
		establecer.addActionListener(this);

		nuevo = new JButton("Nueva");
		nuevo.addActionListener(this);
		nuevo.setFont(fuente);
		nuevo.setBounds(865, 357, 102, 26);

		comenzar = new JButton("Comenzar");
		comenzar.addActionListener(this);
		comenzar.setFont(fuente);
		comenzar.setBounds(1023, 357, 102, 26);

		fuente = new Font("Berlin Sans FB Demi", Font.PLAIN, 26);

		titulo = new JLabel("Algoritmo Round Robin");
		titulo.setFont(fuente);
		titulo.setBounds(87, 11, 299, 40);

		separador = new JSeparator();
		separador.setBounds(64, 62, 1082, 17);

		tiempo1 = new JTextField();
		tiempo1.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent ev) {
				// Si la tecla que se presiona es la de borrar, no hará nada
				if(ev.getKeyCode() != KeyEvent.VK_BACK_SPACE || ev.getKeyCode() != KeyEvent.VK_ENTER)
				try {
					int aux = Integer.parseInt(tiempo1.getText());
					if (aux > 10 || aux < 0) {
						JOptionPane.showMessageDialog(null, "Debe ingresar un número entre 1 y 10", "Fuera de Rango", JOptionPane.WARNING_MESSAGE);
						tiempo1.setText("");
					}
				} catch (NumberFormatException e) {
					JOptionPane.showMessageDialog(null, "Ingresa sólo números", "Formato de número incorrecto", JOptionPane.ERROR_MESSAGE);
					tiempo1.setText("");
				}
				
			}
		});
		tiempo1.setBounds(354, 356, 83, 30);
		getContentPane().add(tiempo1);

		panel.add(espera1);
		panel.add(espera2);
		panel.add(respuesta1);
		panel.add(respuesta2);
		panel.add(cambios1);
		panel.add(cambios2);
		getContentPane().add(panel);
		getContentPane().add(table);
		getContentPane().add(panel2);
		getContentPane().add(separador);
		getContentPane().add(cabecera1);
		getContentPane().add(cabecera2);
		getContentPane().add(cabecera3);
		getContentPane().add(tiempo2);
		getContentPane().add(establecer);
		getContentPane().add(comenzar);
		getContentPane().add(establecer);
		getContentPane().add(nuevo);
		getContentPane().add(arribo);
		getContentPane().add(rafagas);
		getContentPane().add(FIFO);
		getContentPane().add(planificador);
		getContentPane().add(titulo);

		setBounds(0, 0, 1246, 730);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		setLocationRelativeTo(null);
	}

	/** Método para inicializar la tabla de procesos
	 */
	private void inicializaTabla(){
		Random r = new Random();
		Font fuente = new Font("Currier New", Font.BOLD, 16);

		// Se inicializa la matriz celdas y se agrega al panel table
		for (int i = 0; i < filTabla; i++)
			for (int j = 0; j < colTabla; j++) {
				// Inicializar el arreglo de botones
				celdas[i][j] = new JTextField("");
				celdas[i][j].setFont(fuente);
				celdas[i][j].setForeground(Color.BLACK);
				celdas[i][j].setBackground(Color.WHITE);
				table.add(celdas[i][j]);
			}
		
		// Llenado de las celdas con los datos de un proceso
		for (int i = 0; i < filTabla; i++) {
			p = new Proceso("Proceso " + (i + 1), r.nextInt(10), r.nextInt(10) + 1);
			procesos[i] = p; // Se guarda el proceso en el arreglo
			// Se llenan las celdas con los datos del proceso
			celdas[i][0].setText(p.getNombre());
			celdas[i][1].setText(String.valueOf(p.getArribo()));
			celdas[i][2].setText(String.valueOf(p.getRafagas()));
			
			// Asignacion de los colores a la tabla
			for (int j = 0; j < colTabla; j++) {
				celdas[i][j].setBackground(colores[i]);
				celdas[i][j].setEditable(false);

			}
		}
	}
	
	/** Método para inicializar la tabla de cambios
	 */
	private void inicializaPanel(){
		Font fuente = new Font("Currier New", Font.BOLD, 11);
		// Se inicializa la matriz elementos y se agrega al panel panel2
		for (int i = 0; i < filPanel; i++)
			for (int j = 0; j < colPanel; j++) {
				elementos[i][j] = new JTextField("");
				elementos[i][j].setForeground(Color.BLACK);
				elementos[i][j].setBackground(Color.WHITE);
				elementos[i][j].setEditable(false);
				elementos[i][j].setVisible(false);
				elementos[i][j].setFont(fuente);
				panel2.add(elementos[i][j]);
			}
	}
	
	/** Método que inicia una nueva simulación
	 */
	private void nuevaSimulacion(){
		Random r = new Random();
		// Se pone en blanco todos los elementos de la matriz celdas
		for (int i = 0; i < filTabla; i++)
			for (int j = 0; j < colTabla; j++)
				celdas[i][j].setText("");

		// Se llena con otros valores. Los colores no se reasignan debido a que son siempre son los mismos
		for (int i = 0; i < filTabla; i++) {
			p = new Proceso("Proceso " + (i + 1), r.nextInt(10), r.nextInt(10) + 1);
			procesos[i] = p;
			celdas[i][0].setText((String) p.getNombre());
			celdas[i][1].setText(String.valueOf(p.getArribo()));
			celdas[i][2].setText(String.valueOf(p.getRafagas()));
		}
		// Se pone en blanco todos los elementos de la matriz elementos
		for (int i = 0; i < filPanel; i++)
			for (int j = 0; j < colPanel; j++) {
				elementos[i][j].setForeground(Color.BLACK);
				elementos[i][j].setBackground(Color.WHITE);
				elementos[i][j].setText("");
				elementos[i][j].setVisible(false);
			}
		// Se reinicia el contenido de los campos 
		tiempo1.setText("");
		cambios1.setText("");
		espera1.setText("");
		respuesta1.setText("");
	}
		
	/** Método que aplica el algoritmo Round Robin
	 * 
	 * El algoritmo esta diseñado de tal manera que obtiene los valores del ciclo actual para usarlos en el próximo ciclo. 
	 * Es decir, en el ciclo i se atiende al proceso actual y se guarda el estado de las variables utilizadas para analizarlas
	 * en el ciclo i+1. Y así, determinar cuál es el siguiente proceso a atender en este ciclo.
	 * El estado de la tabla de cambios es mostrado en el ciclo actual.
	 */
	private void comenzarSimulacion() {
		int paro = 0; // Variable de paro. Se incrementa cada que un proceso ya no tiene rafagas por atender
		int i = 0; // Variable usada para las iteraciones en el ciclo
		cambios = 0;
		contador = 0;
		atendiendo = false; // Al principio no se atiende a nadie
		fifo = new LinkedList<>();
		listaArribo = new ArrayList<>();
		p = null;// Al principio no hay ningún proceso por atender

		// Ciclo que permite llenar la matriz elementos
		for (i = 0; i < colPanel; i++) {
			// Se muestra el valor del ciclo actual en la ventana. Para ello se le suma 1 al índice i
			elementos[0][i].setText(String.valueOf(i+1));

			// Si llega uno o varios procesos se guardan en el arreglo listaArribo llegoProceso(int i) es un método
			if (llegoProceso(i)) {
				// Si no se está atendiendo a nadie y la fifo está vacía, se reinicia el contador a 0.
				// Responde al caso en que los primeros procesos han sido atendidos por completo y el siguiente 
				// 		proceso llega hasta mucho después de haber terminado los primeros.
				if (atendiendo == false && fifo.isEmpty() && contador != 0) // Si ya es cero, no es necesario sobreescribirlo
					contador = 0;
				
				// Si llegan procesos siempre se meten a la fifo, incluso si no se está atendiendo a nadie.
				while (!listaArribo.isEmpty())
					fifo.addLast(listaArribo.remove(0));
			}

			// El try es para atender el caso de que no llega ningún proceso y no hay ningún proceso atendiendose
			try {
				// Condición de la variable contador
				// Se verifica primero si el contador es igual al tiempo puesto por el usuario
				// Esto para atender de una vez al siguiente proceso en la fifo.
				/* Responde al caso en que el proceso que acaba de llegar, es el siguiente a atender. Se saca de la fifo 
				 * 		y se atiende En la siguiente sección de código se atienden los procesos obtenidos de la fifo. 
				 * Responde al caso en que sólo hay un proceso por atender. Se mete a la fifo, y en la siguiente
				 * 		sección de código se saca para ser atendido nuevamente
				 */
				if (contador == tiempo) {
					contador = 0; // Se reinicia el tiempo que se va a atender a cada proceso

					/* Para cuando el proceso actual ya no necesita ser atendido. En un ciclo anterior, se verificó 
					 * 		si el proceso actual no necesitaba ser atendido. de ser así, la variable atendiendo se puso 
					 * 		en false, por lo que no es necesario sobreescribirla 
					 * Para la variable cambios ocurre lo mismo si es que llega anteriormente del caso en que el
					 * 		proceso actual ya no necesita ser atendido. Si es que se entra en la condición la condición 
					 * 		para saber si un proceso debe seguir atendiendose o no, cambios ya ha sido incrementado, y 
					 * 		si entra en esta también, ya no es necesario incrementarla
					 */
					if (atendiendo != false) {
						atendiendo = false; // Se avisa que no se esta atendiendo a nadie

						// Se verifica, si la fifo no esta vacía y el proceso actual todavía puede ser atendido
						if (!fifo.isEmpty() && p.getRafagas() != 0) {
							// Si el proceso que se atiende actualmente es diferente del siguiente en la fifo, se incrementa
							// 		el número de cambios. Este es para el caso en que hay sólo un proceso por atender en la fifo
							if (!p.equals(fifo.peekFirst()))
								cambios++;
						}
					}

					// El proceso actual se agrega a la fifo sólo si aún necesita ser atendido
					if (p.getRafagas() != 0)
						fifo.addLast(p);
				}

				// Condición de la variable atendiendo
				// Si no se está atendiendo a nadie, se saca al siguiente en la fifo
				/*
				 * Responde al caso en que llegan procesos. Se han agregado los procesos a la fifo y se atiende al siguiente 
				 * 		en la fifo
				 * Responde al caso en que el primer proceso que llega es el que se atiende. Se ha agregado a la fifo y se saca 
				 * 		inmediatamente para ser atendido 
				 * Responde al caso en que no han llegado procesos. Si la fifo está vacía, no se atiende a nadie; de lo
				 * 		contraio, se atiende al siguiente en la fifo Responde al caso en que el contador no es igual al 
				 * 		número de designado por el usuario. Saca al siguiente en la fifo y el contador sigue su incremento normal.
				 */
				if (atendiendo == false) {
					// Si hay algo en la fifo, se saca para ser atendido y se muestra en pantalla al proceso actual
					if (!fifo.isEmpty()) {
						p = fifo.removeFirst();

						// Dependiendo del nombre del proceso se muestra P1, P2, P3 o P4
						switch (p.getNombre()) {
						case "Proceso 1": elementos[3][i].setText("P1");
							break;
						case "Proceso 2": elementos[3][i].setText("P2");
							break;
						case "Proceso 3": elementos[3][i].setText("P3");
							break;
						case "Proceso 4": elementos[3][i].setText("P4");
							break;
						}

						// Se pone en la casilla actual el color correspondiente al proceso actual
						// buscaProcesoEnTabla es un método
						elementos[3][i].setBackground(colores[buscaProcesoEnTabla()]);
					}

					// De lo contrario, no se atiende a nadie
					// Este es el caso en que no llega ningún proceso y no hay nadie en la fifo por atender
					// Al poner al proceso actual p en nulo, cuando se requiera atender, salta de inmediato al catch
					else
						if(p != null) // Si ya es nulo no es necesario sobreescribir la variable
							p = null;
				}

				// Se atiende al proceso actual. Si esl proceso actual es nulo, salta de inmediato al catch
				// planificater(int i) es un método
				planificater(i);
				contador++; // Se incrementa el contador ya que se ha atendido a un ciclo

				// Condición para saber si se debe seguir atendiendo a un proceso o no
				// En caso de que el proceso actual ya no necesite ser atendido se incrementa la variable de paro
				// Los casos que atiende son los mismos que cuando el contador es igual al pedazo de tiempo establecido
				/* Responde al caso en que se termina de atender por completo al proceso actual y el contador es menor 
				 * 		al número designado por el usuario. Para el siguiente ciclo, no entraría la condición del contador, 
				 * 		entraría a la condicion de la variable atendiendo: como es false, se atendería al siguiente proceso
				 * 		en la fifo. 
				 * Responde al caso en el que se termina de atender por completo al proceso actual y el contador es igual 
				 * 		al número designado por el usuario. En el siguiente ciclo, pone el contador a 0
				 */
				if (p.getRafagas() == 0) {
					atendiendo = false; // Se avisa que no se está atendiendo a nadie
					paro++; // Se incrementa la variable de paro
					cambios++; // Se incrementa el número de cambios, ya que se atenderá a otro proceso.
				}

				// Cuando no llega ningún proceso y no hay ningún proceso por atender, aún así, se debe incrementar el contador
			} catch (NullPointerException e) {
				contador++;
			}

			// Muestra el contenido de la fifo actualmente
			// muestraFifo(int i) es un método
			muestraFifo(i);

			// Se muestran el resto de la columna actual de la matriz elementos. Debido a que se inicializó la matriz con
			// 		anterioridad, solamente se pone la visibilidad en true
			for (int j = 0; j < filPanel; j++)
				elementos[j][i].setVisible(true);

			// Si el paro es igual a 4, ya se atendieron a todos los procesos, se detiene el algoritmo
			if (paro == 4)
				break;
			/* El algoritmo responde de manera correcta a los demás casos: 
			 * 		Llegan procesos y no se esta atendidendo a ningún proceso 
			 * 		Llegan procesos y se está atendiendo a algún proceso 
			 * 		No llegan procesos y se está atendiendo a algún proceso
			 */
		}
		
		// Se muestra el número de cambios hechos por el algoritmo
		// Se le resta 1 a cambios debido a que la condición para saber si un proceso debe seguir atendiendose o no
		// 		incrementa cambios siempre que ya no es necesario atender a un proceso
		cambios1.setText(String.valueOf(cambios-1)); 
		
		// Se muestra el tiempo promedio de espera. Se le pasa i+1 debido a que la i guarda la última casilla en la que
		//		 se detuvo el algoritmo
		espera1.setText(String.valueOf(tiempoEspera(i+1)));
		
		// Se muestra el tiempo promedio de respuesta. Se le pasa i+1 por la misma razón anterior
		respuesta1.setText(String.valueOf(tiempoRespuesta(i+1)));
		
	}
	
	/** Método que simula el momento cuando se atiende a un proceso
	 * @param i índice que indica la columna en donde hay que mostrar lo que se atendió del proceso
	 */
	private void planificater(int i){
		p.setRafagas(p.getRafagas() - 1); // Se le decrementan las rafagas que ocupa el proceso actual
		if(atendiendo != true) // Si ya se está atendiendo a algún proceso, no es necesario sobreescribir la variable
			atendiendo = true; // Se indica que se está atendiendo a un proceso
		
		// Se pone el color designado al proceso actual
		elementos[2][i].setBackground(colores[buscaProcesoEnTabla()]);
	}
	
	/** Método que indica si llegó un proceso y lo guarda en el arreglo listaArribo
	 * @param i indice que indica la columna donde hay que mostrar que llegó el proceso
	 * @return true si llegó algún proceso, false si no
	 */
	private boolean llegoProceso(int i){
		// Se recorre el arreglo procesos para saber cual ha llegado en el ciclo actual
		for (Proceso pr : procesos)
			// Si llegan procesos se agregan a listaArribo. Se compara contra i+1 debido a que éste es el valor actual
			// 		del ciclo
			if ((i+1) == (pr.getArribo() + 1))
				listaArribo.add(pr);
		
		// Se muestra el primer proceso que llegó (Debido a que no hay espacio para mostrar el resto)
		if (!listaArribo.isEmpty()) {
			String procesosLlegados = "";// Variable que servirá para mostrar el resto de los procesos
			
			switch (listaArribo.get(0).getNombre()) {
			case "Proceso 1": elementos[1][i].setText("P1");
				break;
			case "Proceso 2": elementos[1][i].setText("P2");
				break;
			case "Proceso 3": elementos[1][i].setText("P3");
				break;
			case "Proceso 4": elementos[1][i].setText("P4");
				break;
			}

			// Se colocan colores llamativos para indicar que llegó un proceso
			elementos[1][i].setBackground(Color.BLACK);
			elementos[1][i].setForeground(Color.WHITE);
			
			// Adicionalmente se mostrará en pantalla un mensaje de los procesos llegados en el ciclo actual
			// 		(Se mostrará cuando se arrastre el botón a la ventana, sólo en caso de que hayan llegado más
			//		de un proceso al mismo ciclo)
			if(listaArribo.size() > 1){
				// Se concatena a la variable procesosLlegados el nombre del proceso siguiente en listaArribo
				for(Proceso pr: listaArribo)
					procesosLlegados = procesosLlegados + "  " + pr.getNombre(); 
				elementos[1][i].setToolTipText(procesosLlegados);
			}
			
		}

		return listaArribo.size() != 0;
	}

	/** Método que calcua el tiempo de espera promedio
	 * El algoritmo va a recorrer la fila 2 de la matriz elementos, ya que es aquí donde se muestra cómo fueron atendidos
	 * los procesos. Se fijará en los colores, cada vez que encuentre uno distinto, incrementará una variable.
	 * Hará esto hasta que ya no hayan rafagas por contar de cada proceso.
	 * 
	 * @param j índice que indica hasta que columna se lleno la matriz elementos.
	 * @return un double con el tiempo promedio claculado
	 */
	private double tiempoEspera(int j){
		double tiempoP = 0; // Variable que va a sumar todos los tiempos de espera de cada proceso

		// Variable de paro, cuando sea igual el número de rafagas por atender de cada proceso, entonces se deja de contar
		// el tiempo de espera
		int noRafagas = 0;


		// Se comienza a contar. Recorre el arreglo procesos, colores y la matriz celdas
		for (int k = 0; k < filTabla; k++) {
			// Para contar las rafagas de cada proceso
			noRafagas = 0;
			// Se comienza a contar a partir del tiempo de llegada del proceso
			for (int i = procesos[k].getArribo(); i < j; i++) {
				// Si el color actual no es igual al del proceso, no se está atendiendo, entonces se incrementa el tiempo de espera
				// Si el color k en colores no es igual al color de la casilla k en elementos, se incrementa tiempoP.
				if (colores[k] != elementos[2][i].getBackground())
					tiempoP += 1;
				// De otra manera, se incrementa el número de rafagas
				else
					noRafagas++;
				// Si el número de rafagas eacumulado es igual al número de rafagas del proceso, no es necesario seguir contando,
				// se sale del ciclo for más interno
				if (noRafagas == Integer.parseInt(celdas[k][2].getText()))
					break;
			}

		}// Lo anterior es posible, ya que la posición de cada proceso en el arreglo procesos y en la tabla de procesos
		 // coinciden entre sí  y con las posiciones de sus colores correspondientes en el arreglo colores
		
		// Se regresa el tiempo promedio de espera
		return tiempoP / 4;
		
	}
	
	/** Método que calcula el tiempo promedio de respuesta
	 * El algoritmo contará desde que llegó el proceso hasta la primera vez que se atendió.
	 * Se fijará en los colores de la matriz elementos y del arreglo colores. Cada vez que sean distintos 
	 * incrementará una variable.
	 * Hará esto para cada proceso
	 * 
	 * @param j índice que indica hasta que columna se lleno la matriz elementos.
	 * @return un double con el tiempo promedio calculado
	 */
	private double tiempoRespuesta(int j){
		double tiempoP = 0.0; // Variable que va a sumar los tiempos de respuesta de cada proceso

		// Se comienza a contar. Recorre los arreglos colores, procesos y la matriz elementos
		for (int k = 0; k < filTabla; k++) {
			// Se comienza a contar a partir de donde llegó el proceso
			for (int i = procesos[k].getArribo(); i < j; i++) {
				// Si el color actual no es igual al del proceso, no se está atendiendo, entonces se
				// incrementa el tiempo de respuesta
				// Si el color k no es igual al color de la casilla k en elementos, se incrementa tiempoP
				if (colores[k] != elementos[2][i].getBackground())
					tiempoP += 1;
				// De otra manera, el proceso ha sido atendido, deja de contar
				else
					break;
			}
		}

		// Se regrea el tiempo promedio de respuesta
		return tiempoP / 4;
	}
	
	/** Método que busca en la tabla al proceso actual. Este método solamente es usado para mostrar los colores correctos
	 * según el proceso que se esté atendiendo actualmente
	 * @return un entero con el índice donde se encuentra el proceso actual en la tabla
	 */
	private int buscaProcesoEnTabla(){
		int i = 0;
		// Compara los nombres, si son iguales, se sale del ciclo y regresa el valor de i
		while(i < filTabla && !celdas[i][0].getText().equals(p.getNombre()))
			i++;
		return i;
	}
	
	/** Método que muestra el contenido actual de la fifo
	 * @param i indice que indica la columna de la matriz elementos en donde hay que mostrar a la fifo
	 */
	private void muestraFifo(int i){
		// Muestra el contenido sólo si la fifo no es vacía
		if(!fifo.isEmpty())
			for(int j = 0; j < fifo.size(); j++)
				// Dependiendo del nombre del proceso se va a mostrar P1, P2, P3 o P4
				switch(fifo.get(j).getNombre()){
				// Se muestra en la fila j+5 ya que es el lugar designado para la fifo
					case "Proceso 1": elementos[j+5][i].setText("P1"); break;
					case "Proceso 2": elementos[j+5][i].setText("P2"); break;
					case "Proceso 3": elementos[j+5][i].setText("P3"); break;
					case "Proceso 4": elementos[j+5][i].setText("P4"); break;
			}
	}
	
	/** Método que sirve para reiniciar los valores dados en la tabla. 
	 * Es para cuando el usuario desea ponerle otro intervalo de tiempo a los mismos procesos de recien.
	 */
	private void reiniciaValores(){	
		// Dado a que se trata de los mismos procesos, se vuelve a copiar las rafagas de tiempo por atender a ccada proceso
		for (int i = 0; i < filTabla; i++)
			procesos[i].setRafagas(Integer.parseInt(celdas[i][2].getText()));
		
		// La matriz elementos también se reinicia para evitar que se encimen los valores
		for (int i = 0; i < filPanel; i++)
			for (int j = 0; j < colPanel; j++) {
				elementos[i][j].setForeground(Color.BLACK);
				elementos[i][j].setBackground(Color.WHITE);
				elementos[i][j].setText("");
				elementos[i][j].setVisible(false);
			}
	}
	
	/** Método para asignar las acciones a los botones
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getSource() == establecer){
			if(tiempo1.getText().equals(""))
				JOptionPane.showMessageDialog(null, "Ingrese un numero primero", "Sin ciclos por realizar", JOptionPane.INFORMATION_MESSAGE);
			else
				tiempo = Integer.parseInt(tiempo1.getText());
		}
		if(arg0.getSource() == comenzar){
			if(tiempo == 0)
				JOptionPane.showMessageDialog(null, "Ingrese un numero primero", "Sin ciclos por realizar", JOptionPane.INFORMATION_MESSAGE);
			else{
				// Se reinician los valores actuales de los procesos y de la matriz elementos
				reiniciaValores();
				comenzarSimulacion();
			}
		}
		if(arg0.getSource() == nuevo){
			nuevaSimulacion();
		}
	}
}

/** Clase que representa a los procesos
 */
class Proceso{
	private int arribo; // Tiempo en que llega un proceso
	private int rafagas; // Número de rafagas de CPU
	private String nombre; // Nombre del proceso

	/*
	 * Constructor de la clase interna
	 */
	public Proceso(String nombre, int arribo, int rafagas) {
		this.nombre = nombre;
		this.arribo = arribo;
		this.rafagas = rafagas;
	}

	/*
	 * Getters y Setters usados en la clase interna
	 *
	 */

	String getNombre() {
		return nombre;
	}

	int getRafagas() {
		return rafagas;
	}

	int getArribo() {
		return arribo;
	}

	void setRafagas(int rafagas) {
		this.rafagas = rafagas;
	}

}//Clase Proceso

