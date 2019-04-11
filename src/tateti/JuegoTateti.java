package tateti;

import java.util.Arrays;
import java.util.Random;

/**
 *
 * @author Matías Bruno
 */
public class JuegoTateti {
    private int turno;
    private int puntos1;
    private int puntos2;
    private int[] tablero;
    private int ganador;
    private boolean juegoEnCurso;
    private int level;
    private boolean jugadorPrimero;
    
    static int[][] lineasDe3 = {{0,1,2},
                                {3,4,5},
                                {6,7,8},
                                {0,3,6},
                                {1,4,7},
                                {2,5,8},
                                {0,4,8},
                                {2,4,6}};
    static int[] pares = {0,2,6,8};
    static int[] impares = {1,3,5,7};
    
    public JuegoTateti() {
        turno = 0;
        puntos1 = 0;
        puntos2 = 0;
        juegoEnCurso = true;
        ganador = -1;
        tablero = new int[9];
        Arrays.fill(tablero, -1);
        level = 1;
        jugadorPrimero = true;
    }

    public boolean isJugadorPrimero() {
        return jugadorPrimero;
    }

    public boolean isJuegoEnCurso() {
        return juegoEnCurso;
    }
    
    public int getGanador() {
        return ganador;
    }

    public int getTurno() {
        return turno;
    }

    public int getPuntos1() {
        return puntos1;
    }

    public int getPuntos2() {
        return puntos2;
    }

    public void setPuntos1(int puntos1) {
        this.puntos1 = puntos1;
    }

    public void setPuntos2(int puntos2) {
        this.puntos2 = puntos2;
    }
    
    public int[] getTablero() {
        return tablero;
    }
    
    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
    
    public boolean jugadorPrimero() {
        return jugadorPrimero;
    }

    public void setJugadorPrimero(boolean jugadorPrimero) {
        this.jugadorPrimero = jugadorPrimero;
    }
    
    //Método para saber si el juego ya se definió.
    public boolean jugadorGana() {
        boolean gana = false;
        int valor1, valor2,valor3;
        for(int i = 0; i < 8 && !gana; ++i) {
            valor1 = tablero[lineasDe3[i][0]];
            valor2 = tablero[lineasDe3[i][1]];
            valor3 = tablero[lineasDe3[i][2]];
            if(valor1 != -1 && valor1 == valor2 && valor2 == valor3)
                gana = true;
        }
        if(gana == true) {
            if((turno % 2 == 0) == (jugadorPrimero == true)) {
                puntos1++;
                ganador = 1;
            }
            else {
                puntos2++;
                ganador = 2;
            }
            juegoEnCurso = false;
        }
        else {
            turno++;
            if(turno == 9) {
                ganador = -1;
                juegoEnCurso = false;
            }
        }
        return gana;
    }
    //Nos interesa el momento antes de hacer Tateti, que posicion marcar.
    private int revisarLinea(int i, int valor) {
        int valor1 = tablero[lineasDe3[i][0]];
        int valor2 = tablero[lineasDe3[i][1]];
        int valor3 = tablero[lineasDe3[i][2]];
        if(valor1 == valor && valor1 == valor2 && valor3 == -1)
            return lineasDe3[i][2];
        if(valor1 == valor && valor1 == valor3 && valor2 == -1)
            return lineasDe3[i][1];
        if(valor2 == valor && valor2 == valor3 && valor1 == -1)
            return lineasDe3[i][0];
        return -1;
    }
    //Acceso al tablero para marcar una posición
    public boolean jugada(int i, int valor) {
        if(tablero[i] == -1) {
            tablero[i] = valor;
            return true;
        }
        return false;
    }
    public boolean jugadaCPU(int valor) {
        int valorRival = 1 - valor;
        int posicion;
        //Para ver si se puede hacer Tateti
        for(int i = 0; i < 8; ++i) {
            posicion = revisarLinea(i,valor);
            if(posicion != -1) {
                return jugada(posicion,valor);
            }    
        }
        //Para evitar que el rival haga Tateti
        for(int i = 0; i < 8; ++i) {
            posicion = revisarLinea(i,valorRival);
            if(posicion != -1) {
                return jugada(posicion,valor);
            }    
        }
        //Elige una jugada con 2 criterios posibles según el nivel de dificultad
        posicion = elegirJugadaCPU();
        if(posicion != -1) {
            return jugada(posicion,valor);
        }
        return false;
    }
    int elegirJugadaCPU() {
        Random r = new Random();
        int num;
        if(level == 2) {//Elige primero el centro, luego las esquinas, más dificil
            if(tablero[4] == -1)
                return 4;
            num = r.nextInt(4);
            for(int i = 0; i < 4; ++i)
                if(tablero[pares[(num + i) % 4]] == -1)
                    return pares[(num + i) % 4];
            for(int i = 0; i < 4; ++i)
                if(tablero[impares[(num + i) % 4]] == -1)
                    return impares[(num + i) % 4];
        }
        else if(level == 1) {//Elige aleatoriamente, para que sea más fácil de vencer
            num = r.nextInt(9);
            for(int i = 0; i < 9; ++i)
                if(tablero[(num + i) % 9] == -1)
                    return (num + i) % 9;
        }
        return -1;
    }
    //Vaciar todo para jugar de nuevo
    public void limpiar() {
        turno = 0;
        Arrays.fill(tablero, -1);
        juegoEnCurso = true;
        ganador = -1;
    }
}
