package craps;

import java.awt.event.*;
import javax.swing.JFrame;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;

/**
 * @author ItKevinR
 *
 * 03:37:05 AM
 */
public class CrapsJuegoFrame extends JFrame
{
	public JPanel contentPane;
	private JMenu 	OpcionesJMenu,
					EstadisticasJMenu,
					AjustesJMenu,
					IntegrantesJMenu;
	
	private JLabel 	jugadorJLabel,
					totJLabel,
					puntosJLabel,
					dado1JLabel,
					dado2JLabel,
					MuestraPuntosJLabel = new JLabel("Puntos"),
					MuestraJugadorJLabel = new JLabel("Jugador:"),
					MuestraTodoJLabel = new JLabel("Tiradas totales"),
					MuestraDado1JLabel= new JLabel("Dado no. 1"),
					MuestraDado2JLabel = new JLabel("Dado no. 2"),
					EstadoJLabel;
	
	private JMenuItem promTiradas = new JMenuItem("Promedio de tiradas por juego"),
					  estadisticasTotales = new JMenuItem("Todas las estadisticas");

	private JButton btnSalida, 
					btnGirar,
					btnNuevoJuego;
	
	private JRadioButtonMenuItem[] 	Colores,
									Estilos;
	
	private ButtonGroup btnGcolor,
						btnGEstilo;

	
	private final Color[] ValDeColor = { 	Color.LIGHT_GRAY, 
											Color.WHITE, 
											Color.BLACK, 
											Color.BLUE, 
											Color.GREEN, 
											Color.ORANGE};

	private UIManager.LookAndFeelInfo[] looks; //Estilos
	private String[] lookNombres; //Nombres
	
	private int misPuntos = 0; // Puntos si gana o pierda la primera tirada
	  
	//Estadisticas
	private int victorias = 0;
	private int juegosTotales = 0;
	private int tiradas = 0;
	private int tiradasTotales = 0;

	// contantes que representan las tiradas comunes de los dados 
	private final static int SNAKE_EYES = 2;
	private final static int TREY = 3;
	private final static int SEVEN = 7;
	private final static int YO_LEVEN = 11;
	private final static int BOX_CARS = 12;

	private int valorDado1;
	private int valorDado2;

	/****************************************************
	* Metodo     : Salida
	*
	* Proposito  : 	Se activa cuándo el jugador presiona el botón "salida o primera tirada"
	* 				la suma de los dados es pasada a este metodo 
	* 				Si el jugador es un ganador, perdedor, o el juego continua dentro del
	* 				punto de tirada o "checkpoint" dependiendo las reglas del juego. Si continua el valor
	* 				el valor misPuntos es seteado.
	* 
	* Parameteros: sumDeDados- El valor es generado por el metodo "RollDado"
	*
	* Retorna    : Nada				
	****************************************************/		
	public void Salida(int sumDeDados)
	{
		// Determina el estado del juego basado en la primera tirada
		switch ( sumDeDados ) 
		{
			case SEVEN: // Gana con un 7 en la primera tirada
				ganador();
				break;
			case YO_LEVEN: // Gana con un 11 en la primera tirada
				ganador();
	   			break;
	   		case SNAKE_EYES: // Pierde con un 2 en la primera tirada
	   			perdedor();
	   			break;
	   		case TREY: // Pierde con un 3 en la primera tirada
	   			perdedor();
	   			break;
	   		case BOX_CARS: // Pierde con un 12 en la primera tirada
	   			perdedor();
	   			break;
	   		default: // no gana ni pierde, suma el punto  
	   			misPuntos = sumDeDados; // Suma el punto
	   			continuarTirando();
	   			break;
		} 
	}

	/****************************************************
	* Metodo     : tirarDeNuevo
	*
	* Proposito  : 	la suma para el método tirada de dados
	* 				es usada para determinar si el valor misPuntos is concordante con el resultado 
	* 				de juego ganado o si la suma equivale a 7 el jugador pierde.
	* 				Si ni un 7 o misPuntos son determinados en las siguientes tiradas el juego continuara 
	* 				y el jugador necesitara girar el dado de nuevo.
	* 			
	* 
	* Parameteros: sumDeDados- El valor generado el el metodo "tirar dado"
	*
	* Retorna    : Nada			
	****************************************************/	
	public void tirarDeNuevo(int sumDeDados)
	{
		// Determina el estatus del juego
		if( sumDeDados == misPuntos ) // Gana si los puntos coinciden
		{
			ganador();
		}
		else if( sumDeDados == SEVEN ) // pierde si sale 7 antes de misPuntos
		{
			perdedor();
		}
		else
		{
			continuarTirando(); //Deberia pasar al siguiente jugador
		}
	}

	/****************************************************
	* Metodo    : tirar dado
	*
	* Proposito  : 	tira el dado, calcula la suma y muestra el resultado al valor del dado 1
	* 				y el dado 2 y el valor total de la suma (se muestra en pantalla)
	* 
	* Parameteros: Ninguno
	*
	* Retorna    : La suma del dado 1 y del dado 2					
	****************************************************/	
	public int tirarDado()
	{
		// dado
		Dado dado1 = new Dado();
		Dado dado2 = new Dado();
	    	
		tiradas++;

		// tirar cada dado
		dado1.tirar();
		dado2.tirar();
			
		valorDado1 = dado1.getValor();
		valorDado2 = dado2.getValor();
			
		// suma de los valores de los dados
		int sum = dado1.getValor() + dado2.getValor();
	  
		return sum; // retorna la suma de los dados
	} // FIN DEL METODO

	/****************************************************
	* Metodo     : ganador
	*
	* Proposito  : 	si la tirada del dado resulta en victoria, este metodo
	* 				incrementara las estadisticas de victoria, llama al metodo FinJuego para más modificaiones de estadisticas,
	* 				resetea los botones para las estadisticas de un nuevo juego y avisa al jugador el estado.
	* 
	* Parameteros: Nada
	*
	* Retorna    : Nada	
	****************************************************/	
	public void ganador()
	{
		victorias++;
		FinJuego();
		EstadoJLabel.setText("¡Ganador!");
		btnGirar.setEnabled(false);
		btnSalida.setEnabled(false);
		btnNuevoJuego.setEnabled(true);
	}
	
	/****************************************************
	* Metodo     : ContinuarTirando
	*
	* Proposito  : 	Si la tirada del dado resulta en continuar, Este metodo
	* 				mostrara en pantalla el valor de misPuntos al jugador, informado al usuario que debe continuar tirando,
	* 				establece los botones donde solo el volver a tirar el dado es posible.
	* 
	* Parametero : Ninguno
	*
	* Retorna    : Nada		
	****************************************************/	
	public void continuarTirando()
	{
		puntosJLabel.setText(Integer.toString(misPuntos));
		EstadoJLabel.setText("¡Vuelve a tirar!");
		btnSalida.setEnabled(false);
		btnGirar.setEnabled(true);
	}
	
	/****************************************************
	* Metodo     : perdedor
	*
	* Proposito  : 	si la tirada del dado resulta en perdida,
	* 				 llama al metodo FinJuego para más modificaiones de estadisticas,
	* 				resetea los botones para las estadisticas de un nuevo juego 
	* 				y avisa al jugador el estado.
	* 
	* Parameteros: Nada
	*
	* Retorna    : Nada	
	****************************************************/		
	public void perdedor()
	{
		FinJuego();
		EstadoJLabel.setText("¡Perdiste, intentalo otra vez!");
		btnGirar.setEnabled(false);
		btnSalida.setEnabled(false);
		btnNuevoJuego.setEnabled(true);
	}
	
	/****************************************************
	* Metodo     : FinJuego
	*
	* Proposito  : 	este metodo moficara las estadisticas habilitando el promedio de tiradas por juego
	* 				y estadisticas totales.
	* 				Incrementara la estadistica "juegos totales" e incrementara la estadistica de "tiradas totales" 
	* 				por la cantidad de tiradas hechas en el juego anterior
	* 				se resetearan las contadas de las tiradas para el sig juego y restea también los valores de misPuntos.
	* 
	* Parametero : Ninguno
	*
	* Retorna    : Nada		
	****************************************************/	
	public void FinJuego()
	{
		promTiradas.setEnabled(true);
		estadisticasTotales.setEnabled(true);
		juegosTotales++;
		tiradasTotales += tiradas; 
		tiradas = 0;
		misPuntos = 0;
	}
	
	/****************************************************
	* Metodo     : CrapsJuegoFrame
	*
	* Proposito  : Creas y modifica el juego dependiendo dele stado del mismo.
	* 
	* Parameteros: Ninguno
	*
	* Retorna    : Nada
	****************************************************/	
	public CrapsJuegoFrame() 
	{
		super("Juego de Craps");
		
		OpcionesJMenu = OpcionesMenu();
		EstadisticasJMenu = EstadisticasMenu();
		AjustesJMenu = AjustesMenu();
		IntegrantesJMenu = IntegrantesMenu();
		getContentPane().setLayout(null);
		
		jugadorJLabel = new JLabel("'Opciones' -> 'Nuevo jugador'");
		jugadorJLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		jugadorJLabel.setBounds(378, 6, 253, 23);
		getContentPane().add(jugadorJLabel);
		EstadoJLabel = new JLabel("¡Buena suerte!");
		EstadoJLabel.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 18));
		EstadoJLabel.setBounds(316, 40, 273, 32);
		EstadoJLabel.setVisible(true);
		
		btnNuevoJuego = new JButton("Nuevo juego");
		btnNuevoJuego.setBounds(10, 283, 126, 24);
		btnSalida = new JButton("Tirar");
		btnSalida.setBounds(146, 284, 126, 23);
		btnGirar = new JButton("Volver a tirar");
		btnGirar.setBounds(276, 284, 97, 23);
		
		btnNuevoJuego.setEnabled(false);
		btnSalida.setEnabled(false);
		btnGirar.setEnabled(false);
		
		Icon dadoImg = new ImageIcon(getClass().getResource("crapsimagen.jpg"));
		JLabel dadoLabel = new JLabel(dadoImg);
		dadoLabel.setBounds(0, 0, 306, 273);
		getContentPane().add(dadoLabel);
		
		JPanel panel_3 = new JPanel();
		panel_3.setBackground(SystemColor.activeCaption);
		panel_3.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Tu punto", TitledBorder.CENTER, TitledBorder.TOP, null, null));
		panel_3.setBounds(444, 204, 79, 73);
		getContentPane().add(panel_3);
		panel_3.setLayout(null);
		
		puntosJLabel = new JLabel(" ");
		puntosJLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		puntosJLabel.setHorizontalAlignment(SwingConstants.CENTER);
		puntosJLabel.setBounds(6, 16, 63, 46);
		panel_3.add(puntosJLabel);
		puntosJLabel.setVisible(true);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBackground(SystemColor.activeCaption);
		panel_2.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Suma", TitledBorder.CENTER, TitledBorder.TOP, null, null));
		panel_2.setBounds(326, 206, 79, 73);
		getContentPane().add(panel_2);
		panel_2.setLayout(null);
		totJLabel = new JLabel(" ");
		totJLabel.setHorizontalAlignment(SwingConstants.CENTER);
		totJLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		totJLabel.setBounds(10, 11, 59, 51);
		panel_2.add(totJLabel);
		
		totJLabel.setVisible(true);
		
		JPanel panel = new JPanel();
		panel.setBackground(SystemColor.activeCaptionBorder);
		panel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Dado 1", TitledBorder.CENTER, TitledBorder.TOP, null, null));
		panel.setBounds(326, 96, 79, 73);
		getContentPane().add(panel);
		panel.setLayout(null);
		dado1JLabel = new JLabel(" ");
		dado1JLabel.setHorizontalAlignment(SwingConstants.CENTER);
		dado1JLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		dado1JLabel.setBounds(10, 16, 59, 46);
		panel.add(dado1JLabel);
		dado1JLabel.setBackground(SystemColor.activeCaptionBorder);
		dado1JLabel.setVisible(true);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(SystemColor.activeCaptionBorder);
		panel_1.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Dado 2", TitledBorder.CENTER, TitledBorder.TOP, null, null));
		panel_1.setBounds(444, 96, 79, 73);
		getContentPane().add(panel_1);
		panel_1.setLayout(null);
		dado2JLabel = new JLabel(" ");
		dado2JLabel.setHorizontalAlignment(SwingConstants.CENTER);
		dado2JLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		dado2JLabel.setBounds(10, 11, 59, 51);
		panel_1.add(dado2JLabel);
		dado2JLabel.setVisible(true);				
		getContentPane().add(EstadoJLabel);
		getContentPane().add(btnNuevoJuego);
		getContentPane().add(btnSalida);
		getContentPane().add(btnGirar);
		getContentPane().add(MuestraPuntosJLabel);
		getContentPane().add(MuestraJugadorJLabel);
		getContentPane().add(MuestraTodoJLabel);
		getContentPane().add(MuestraDado1JLabel);
		getContentPane().add(MuestraDado2JLabel);
		
		JLabel lblJugador = new JLabel("Jugador:");
		lblJugador.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblJugador.setBounds(316, 6, 79, 23);
		getContentPane().add(lblJugador);
		
		btnNuevoJuego.addActionListener(
			new ActionListener()
			{
				public void actionPerformed(ActionEvent event)
				{
					btnSalida.setEnabled(true);
					btnNuevoJuego.setEnabled(false);
					event.getActionCommand();

				}
			}
		);
		
		btnSalida.addActionListener(
			new ActionListener()
			{
				public void actionPerformed(ActionEvent event)
				{
					int sum = tirarDado();
					totJLabel.setText(Integer.toString(sum));
					dado1JLabel.setText(Integer.toString(valorDado1));
					dado2JLabel.setText(Integer.toString(valorDado2));
					Salida(sum);
					event.getActionCommand();
				}
			}
		);
		
		btnGirar.addActionListener(
			new ActionListener()
			{
				public void actionPerformed(ActionEvent event)
				{
					int sum = tirarDado();
					totJLabel.setText(Integer.toString(sum));
					dado1JLabel.setText(Integer.toString(valorDado1));
					dado2JLabel.setText(Integer.toString(valorDado2));
					tirarDeNuevo(sum);
					event.getActionCommand();
						
				}
			}
		);
		
		JMenuBar bar = new JMenuBar();
		setJMenuBar(bar);
		bar.add(OpcionesJMenu);
		bar.add(EstadisticasJMenu);
		bar.add(AjustesJMenu);
		bar.add(IntegrantesJMenu);
		

	}

	/****************************************************
	* Metodo     : OpcionesMenu
	*
	* Proposito  : 	Permite al jugador crear un nuevo usuario y
	* 				salir del juego.
	* 
	* Parameteros: Ninguno
	*
	* Retorna    : Nada
	****************************************************/	
	public JMenu OpcionesMenu()
	{
		JMenu OpcionesMenu = new JMenu("Opciones");
		OpcionesMenu.setMnemonic('F');
		
		JMenuItem nuevoUsuario = new JMenuItem("Nuevo Jugador");
		nuevoUsuario.setMnemonic('n');
		
		JMenuItem Salir = new JMenuItem("Salir");
		nuevoUsuario.setMnemonic('Q');

		OpcionesMenu.add(nuevoUsuario);
		OpcionesMenu.add(Salir);
		
		nuevoUsuario.addActionListener(
			new ActionListener()
			{
				public void actionPerformed(ActionEvent event)
				{
					btnNuevoJuego.setEnabled(true);
					btnGirar.setEnabled(false);
					btnSalida.setEnabled(false);
					
					dado1JLabel.setText(" ");
					dado2JLabel.setText(" ");
					totJLabel.setText(" ");
					puntosJLabel.setText(" ");
					
					victorias = 0;
					juegosTotales = 0;
					tiradasTotales = 0;
					valorDado1 = 0;
					valorDado2 = 0;
					tiradas = 0;
					promTiradas.setEnabled(false);
					estadisticasTotales.setEnabled(false);
					EstadoJLabel.setText("¡Buena suerte!");

					jugadorJLabel.setText(JOptionPane.showInputDialog("Ingresa el nombre del jugador:"));
				}
			}
		);
		
		Salir.addActionListener(
			new ActionListener()
			{
				public void actionPerformed(ActionEvent event)
				{
					System.exit(0);
				}	
			}
		);
		
		return OpcionesMenu;	
	}

	/****************************************************
	* Metetodo   : EstadisticasMenu
	*
	* Proposito  : 	Muestra las estadisticas al jugador, el promedio tirada/juego y los menus totales estan
	* 				desabilitados hasta el ultimo juego jugado asi que el promaga no permite dividir
	* 				entre cero.
	* 
	* Parameteros: Ninguno
	*
	* Retorna    : Nada
	****************************************************/	
	public JMenu EstadisticasMenu()
	{
		JMenu EstadisticasMenu = new JMenu("Estadisticas del juego");
		EstadisticasMenu.setMnemonic('G');
		
		JMenuItem partidasTotales = new JMenuItem("Juegos totales");
		partidasTotales.setMnemonic('g');
		
		JMenuItem VictoriasTotales = new JMenuItem("Victorias totales");
		VictoriasTotales.setMnemonic('w');
		
		promTiradas.setMnemonic('r');
		estadisticasTotales.setMnemonic('s');

		partidasTotales.addActionListener(
				new ActionListener()
				{
					public void actionPerformed(ActionEvent event)
					{
						JOptionPane.showMessageDialog(null,	"Partidas toales jugadas: " + juegosTotales, "Estadisticas", JOptionPane.PLAIN_MESSAGE);
					}	
				}
			);
		
		VictoriasTotales.addActionListener(
				new ActionListener()
				{
					public void actionPerformed(ActionEvent event)
					{
						JOptionPane.showMessageDialog(null,	"Partidad totales ganadas: " + victorias, "Estadisticas", JOptionPane.PLAIN_MESSAGE);
					}	
				}
			);
		
		promTiradas.addActionListener(
				new ActionListener()
				{
					public void actionPerformed(ActionEvent event)
					{
						double promTiros = tiradasTotales/juegosTotales;
						JOptionPane.showMessageDialog(null,	"Prom tiradas por juego: " + promTiros, "Estadisticas", JOptionPane.PLAIN_MESSAGE);
					}	
				}
			);
		
		estadisticasTotales.addActionListener(
				new ActionListener()
				{
					public void actionPerformed(ActionEvent event)
					{
						double promTiros = tiradasTotales/juegosTotales;
						JOptionPane.showMessageDialog(null,		"Total patridas jugadas: " 	+ juegosTotales 
															+ 	"\nTotal partidas ganadas: " 	+ victorias 
															+ 	"\nPromedio de tiradas por juego: " + promTiros 
															, "Todas las estadisticas", JOptionPane.PLAIN_MESSAGE);
					}	
				}
			);
		
		EstadisticasMenu.add(partidasTotales);
		EstadisticasMenu.add(VictoriasTotales);
		EstadisticasMenu.add(promTiradas);
		if(juegosTotales == 0)
		{
			promTiradas.setEnabled(false);
			estadisticasTotales.setEnabled(false);
		}
		EstadisticasMenu.add(estadisticasTotales);
		
		return EstadisticasMenu;	
	}

	
	/****************************************************
	* Metodo     : ajustesMenu
	*
	* Proposito  : 	Permite al usuario escoger distintos fondos de color o
	* 				cambiar el estido de la ventana.
	* 
	* Parameteros: Ninguno
	*
	* Retorna    : Nada
	****************************************************/	
	public JMenu AjustesMenu()
	{
		JMenu ajustesMenu = new JMenu("Preferencias");
		ajustesMenu.setMnemonic('P');
		
		//Fondo del menu
		JMenuItem bgMenu = new JMenu("Color de fondo");
		bgMenu.setMnemonic('c');

		String[] colores = {"Gris", 
							"Blanco", 
							"Negro", 
							"Azul", 
							"Verde", 
							"Naranja"};
		Colores = new JRadioButtonMenuItem[colores.length];
		btnGcolor = new ButtonGroup();
		manipuladorDeColor handleColor = new manipuladorDeColor();
		
		for(int c = 0; c < colores.length; c++)
		{
			Colores[c] = new JRadioButtonMenuItem(colores[c]);
			bgMenu.add(Colores[c]);
			btnGcolor.add(Colores[c]);
			Colores[c].addActionListener(handleColor);
		}
		
		Colores[1].setSelected(true);
		
		ajustesMenu.add(bgMenu);
		ajustesMenu.addSeparator();
		
		//Estilachos 
		JMenuItem estiloMenu = new JMenu("Estilos");
		estiloMenu.setMnemonic('f');
		
		looks = UIManager.getInstalledLookAndFeels();
		
		lookNombres = new String [looks.length];
		for(int i = 0; i < looks.length; i++)
		{
			lookNombres[i] = looks[i].getName();
			
		}
		
		Estilos = new JRadioButtonMenuItem[looks.length];
		btnGEstilo = new ButtonGroup();
		ManejadorDeEstilos handleLook = new ManejadorDeEstilos();
		
		for(int c = 0; c < looks.length; c++)
		{
			Estilos[c] = new JRadioButtonMenuItem(lookNombres[c]);
			estiloMenu.add(Estilos[c]);
			btnGEstilo.add(Estilos[c]);
			Estilos[c].addItemListener(handleLook);
		}
		
    	Estilos[0].setSelected(true);
		ajustesMenu.add(estiloMenu);
		
		
		
		return ajustesMenu;	
	}
	
	
	
	//Accion para cambiar los colores del fondo
	private class manipuladorDeColor implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			for(int count = 0; count< Colores.length; count++)
			{
				if(Colores[count].isSelected())
				{
					getContentPane().setBackground(ValDeColor[count]);
					break;
				}
			}
			repaint();
		}
	}
	
	//Accion para cambiar los estilos de la ventana
	private class ManejadorDeEstilos implements ItemListener
	{
		public void itemStateChanged(ItemEvent event)
		{
			for(int count = 0; count< Estilos.length; count++)
			{
				if(Estilos[count].isSelected())
				{
					cambiarElEstido(count);
	//				break;
			//		event.getActionCommand();
				}
			}
		}
	}
	
	/****************************************************
	* Metodo     : cambiarElEstilo
	*
	* Proposito  : Llamado por el manipulador de estilo. Cambiara
	* 				el estilo de la ventada a lo que seleccione el usuario.
	* 
	* Parameteros: El index de los estilos
	*
	* Retorna    : Nada	
	****************************************************/	
	private void cambiarElEstido(int value)
	{
		try
	  	{
	  		UIManager.setLookAndFeel(looks[value].getClassName());
 			SwingUtilities.updateComponentTreeUI(this);
	  	}
	  	catch(Exception e)
	  	{
	  		e.printStackTrace();
	  	}
	}

	/****************************************************
	* Metodo     : IntegrantesMenu
	*
	* Proposito  : Permite al usuario ver los integrantes que conforman el projecto
	* 
	* Parameteros: Ninguno
	*
	* Retorna    : Nada		
	****************************************************/	
	public JMenu IntegrantesMenu()
	{
		JMenu IntegMenu = new JMenu("Proyecto");
		IntegMenu.setMnemonic('A');
	
		JMenuItem projItem = new JMenuItem("Integrantes");
		projItem.setMnemonic('p');

		IntegMenu.add(projItem);
		
		
		projItem.addActionListener(
				new ActionListener()
				{
					public void actionPerformed(ActionEvent event)
					{
						String projInfo = 
								"DOROTEO RAMIREZ ANA VALERIA\n"
								+"GOMEZ HERNANDEZ LAURA ALEJANDRA\n"
								+"MENDIETA ARIAS LUIS ALBERTO\n"
								+"REYNAGA CANO KEVIN ALBERTO\n"
								+"VALLEJO RAMIREZ JOSHUA ALEJANDRO\n";
						
						JOptionPane.showMessageDialog(null,	projInfo);
					}	
				}
			);

		
			
		
		return IntegMenu;	
	}
}
