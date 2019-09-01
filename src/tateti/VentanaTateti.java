package tateti;
/**
 *
 * @author Matías Bruno
 */

import java.awt.GridLayout;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.UIManager;
//import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;
import java.awt.Color;
import java.awt.Image;

public class VentanaTateti extends JFrame implements ActionListener, ItemListener {
    private JPanel panel1;
    private JButton btnJugar;
    private JButton btnTateti[][];
    private JLabel lblNivel;
    private JLabel lblPrimero;
    private ButtonGroup bgNivel;
    private ButtonGroup bgPrimero;
    private JRadioButton rbFacil;
    private JRadioButton rbDificil;
    private JRadioButton rbHumano;
    private JRadioButton rbCPU;
    private JLabel lblResultado;
    
    ImageIcon imagen1;
    ImageIcon imagen2;
    
    private JuegoTateti juego;
    
    //Un panel con un GridLayout de 3x3 con un botón en cada lugar.
    private JPanel crearPanel() {
        panel1 = new JPanel(new GridLayout(3,3,10,10));
        panel1.setBounds(50,100,500,500);
        panel1.setBackground(Color.black);
        btnTateti = new JButton[3][3];
        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 3; ++j) {
                btnTateti[i][j] = new JButton();
                btnTateti[i][j].addActionListener(this);
                btnTateti[i][j].setFocusable(false);
                panel1.add(btnTateti[i][j]);
            }
        }
        return panel1;
    }
    //En el constructor se arma toda la interfaz gráfica.
    public VentanaTateti() {
        setSize(600,650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Tatetí");
        setLayout(null);
        setResizable(false);
        setLocationRelativeTo(null);
        //Aqui empiezan 2 grupos con 2 opciones cada uno
        bgNivel = new ButtonGroup();
        bgPrimero = new ButtonGroup();
        rbFacil = new JRadioButton("Fácil", true);
        rbDificil = new JRadioButton("Difícil");
        rbHumano = new JRadioButton("Humano", true);
        rbCPU = new JRadioButton("CPU");
        rbFacil.setBounds(100,40,80,30);
        rbDificil.setBounds(100,65,80,30);
        rbHumano.setBounds(425,40,100,30);
        rbCPU.setBounds(425,65,80,30);
        rbFacil.addItemListener(this);
        rbDificil.addItemListener(this);
        rbHumano.addItemListener(this);
        rbCPU.addItemListener(this);
        add(rbFacil);
        add(rbDificil);
        add(rbHumano);
        add(rbCPU);
        bgNivel.add(rbFacil);
        bgNivel.add(rbDificil);
        bgPrimero.add(rbHumano);
        bgPrimero.add(rbCPU);
        //Hasta aquí los grupos
        panel1 = crearPanel();
        add(panel1);
        lblNivel = new JLabel("Nivel de dificultad");
        lblNivel.setBounds(100,20,150,20);
        add(lblNivel);
        lblPrimero = new JLabel("Juega primero");
        lblPrimero.setBounds(425,20,150,20);
        add(lblPrimero);
        btnJugar = new JButton("Jugar");
        btnJugar.setBounds(250,50,100,30);
        btnJugar.addActionListener(this);
        lblResultado = new JLabel("J1 [0 - 0] J2");
        lblResultado.setBounds(265,15,150,30);
        add(lblResultado);
        add(btnJugar);
        setVisible(true);
        
        imagen1 = getImagen(btnTateti[0][0],"/recursos/circulo.png");
        imagen2 = getImagen(btnTateti[0][0],"/recursos/cruz.png");
        
        juego = new JuegoTateti();
    }
    public void actualizar() {
        int[] tablero = juego.getTablero();
        for(int i = 0; i < 9; ++i) {
            if(tablero[i] == 0)
                btnTateti[i/3][i%3].setIcon(imagen1);
            else if(tablero[i] == 1)
                btnTateti[i/3][i%3].setIcon(imagen2);      
        }
        if(juego.jugadorGana() == true) {
            lblResultado.setText("J1 [" + juego.getPuntos1() + " - " +
                                            juego.getPuntos2() + "] J2");
            int g = juego.getGanador();
            if(g != -1) {
                JOptionPane.showMessageDialog(null,"El ganador es jugador nro " + g + "!");
            }
        }
        else if(!juego.isJuegoEnCurso()) {
            JOptionPane.showMessageDialog(null, "Este juego terminó empatado!"); 
        }
    }
    public void reiniciar() {
        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 3; ++j) {
                btnTateti[i][j].setText("");
                btnTateti[i][j].setEnabled(true);
                btnTateti[i][j].setIcon(null);
            }
        }
    }
    public ImageIcon getImagen(JButton boton, String nombreArchivo) {
        ImageIcon imagen = new ImageIcon(getClass().getResource(nombreArchivo));
        int alto = boton.getHeight() / 2;
        int ancho = boton.getWidth() / 2;
        ImageIcon imgBoton = new ImageIcon(imagen.getImage().getScaledInstance(ancho, alto, Image.SCALE_DEFAULT));
        return imgBoton;
    }
    //Manejamos los eventos de hacer click en los botones.
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == btnJugar) {
            if(juego.isJuegoEnCurso() == false || JOptionPane.showConfirmDialog(null,
                                                    "Juego en curso, ¿Desea empezar de nuevo?") == 0) {
                juego.limpiar();
                juego.setLevel((rbFacil.isSelected() == true)? 1 : 2);
                juego.setJugadorPrimero(rbHumano.isSelected());
                reiniciar();
                if(juego.jugadorPrimero() == false) {
                    juego.jugadaCPU(1);
                    actualizar();
                }
            }
        }
        else {
            int fila = -1, col = -1;
            for(int i = 0; i < 3; ++i) {
                for(int j = 0; j < 3; ++j) {
                    if(e.getSource() == btnTateti[i][j]) {
                        fila = i;
                        col = j;
                    }
                }
            }
            int posicion = fila * 3 + col;
            if(fila != -1 && col != -1 && juego.isJuegoEnCurso() && juego.jugada(posicion,0)) {
                actualizar();
                if(juego.isJuegoEnCurso()) {
                    juego.jugadaCPU(1);
                    actualizar();
                }
            }
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if(juego.isJuegoEnCurso() == true) {
            if(e.getSource() == rbFacil || e.getSource() == rbFacil ||
                    e.getSource() == rbHumano || e.getSource() == rbCPU) {
                JOptionPane.showMessageDialog(null, "La nueva configuración se aplicará al volver a jugar");
            }
        }
    }
    
    public static void main(String[] args) throws Exception {
    	//Esto no sirve para Linux el look and feel de Windows
        //UIManager.setLookAndFeel(new WindowsLookAndFeel());
        /*SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new VentanaTateti().setVisible(true);
            }
        });*/
        //Usando una expresión lambda en vez de la forma clásica
        SwingUtilities.invokeLater( () -> new VentanaTateti().setVisible(true) );
    }
}
