import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dgarcia on 15/03/2017.
 */
public class Bender {

    private String mapa;
    private int num_horizontal;
    private int num_vertical;
    private char [][] cuadricula;
    private int eje_x;
    private int eje_y;
    private boolean invertido = false;

    public Bender(String map) {
        this.mapa = map;
        this.num_horizontal = cuentahorizontal(map);
        this.num_vertical = cuentavertical(map);
        this.cuadricula = rellenarray();
    }

    public String run() {
        String resultado = "";
        char dir;

        while (true) {
            dir = direccion();
            resultado += dir;
            char actual = cuadricula[eje_y][eje_x];

            if (actual == ' ' || actual == 'X') {
                while (actual == ' ' || actual == 'X') {
                    if (siguiente(dir) == '#') {
                        break;
                    }
                    moverRobot(dir);
                    resultado += dir;
                    actual = cuadricula[eje_y][eje_x];

                    if( actual == 'T') {
                        portal();
                        moverRobot(dir);
                        resultado+=dir;
                        actual = cuadricula[eje_y][eje_x];
                    }
                }
            }

            if( actual == 'T') {
                portal();
                moverRobot(dir);
                resultado+=dir;
                actual = cuadricula[eje_y][eje_x];
            }

            if(actual == '$') {
                break;
            }

            if (actual == 'I'){
                invertido = !invertido;
            }
        }
        return resultado;
    }

    public int cuentahorizontal(String mapa) {
        int longitud = 0;
        for (int i = 0; mapa.charAt(i) != '\n'; i++) {
            longitud++;
        }
        return longitud;
    }

    public int cuentavertical(String mapa){
        int longitud = 0;
        for (int i = 0; i < mapa.length(); i++) {
            if (mapa.charAt(i) == '\n') {
                longitud ++;
            }
        }
        longitud++;
        return longitud;
    }

    public char[][] rellenarray(){
        char[][] array = new char[num_vertical][num_horizontal];
        int separacion = 0;
        for (int i = 0; i<num_vertical; i++) {
            int aux = 0;
            for (int j = 0; aux<num_horizontal;j++) {
                if (mapa.charAt(j+separacion) != '\n') {
                    array[i][aux] = mapa.charAt(j + separacion);
                    if(mapa.charAt(j+separacion) == 'X') {
                        this.eje_y = i;
                        this.eje_x = aux;
                    }
                    aux++;
                }
            }
            separacion += num_horizontal;
            if(i<num_vertical-1) {
                separacion += 1;
            }
        }
        return array;
    }

    private void moverRobot(char direccion) {
        switch (direccion) {
            case 'S':
                eje_y +=1;
                break;
            case 'N':
                eje_y -=1;
                break;
            case 'W':
                eje_x-=1;
                break;
            case 'E':
                eje_x+=1;
        }
    }

    private char direccion() {
        char dir = '\0';
        List<Character> l1 = new ArrayList<>();

        l1.add('S');
        l1.add('E');
        l1.add('N');
        l1.add('W');

        

        if (!invertido) {
            if (cuadricula[eje_y + 1][eje_x] != '#') {
                moverRobot('S');
                dir = 'S';
            } else if (cuadricula[eje_y][eje_x + 1] != '#') {
                moverRobot('E');
                dir = 'E';
            } else if (cuadricula[eje_y - 1][eje_x] != '#') {
                moverRobot('N');
                dir = 'N';
            } else if (cuadricula[eje_y][eje_x - 1] != '#') {
                dir = 'W';
                moverRobot('W');
            }
        }
        if (invertido) {
            if (cuadricula[eje_y - 1][eje_x] != '#') {
                moverRobot('N');
                dir = 'N';
            }
            else if (cuadricula[eje_y][eje_x - 1] != '#') {
                dir = 'W';
                moverRobot('W');
            }
            else if (cuadricula[eje_y + 1][eje_x] != '#') {
                moverRobot('S');
                dir = 'S';
            }
            else if (cuadricula[eje_y][eje_x + 1] != '#') {
                moverRobot('E');
                dir = 'E';
            }
        }
        return dir;
    }

    private char siguiente(char dir) {
        char seguent = '\0';
        switch (dir) {
            case 'S':
                seguent = cuadricula[eje_y + 1][eje_x];
                break;
            case 'E':
                seguent = cuadricula[eje_y][eje_x + 1];
                break;
            case 'N':
                seguent = cuadricula[eje_y - 1][eje_x];
                break;
            case 'W':
                seguent = cuadricula[eje_y][eje_x - 1];
        }
        return seguent;
    }

    private void portal() {
        for(int i = 0;i<num_vertical; i++) {
            for(int j =0; j<num_horizontal; j++) {
                if(cuadricula[i][j] == 'T' && i != eje_y && j != eje_x ) {
                    eje_y = i;
                    eje_x = j;
                    return;
                }
            }
        }
    }

}