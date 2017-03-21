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
        //todas las variables cogen los valores sacados de los metodos, menos el mapa
        this.mapa = map;
        this.num_horizontal = cuentahorizontal(map);
        this.num_vertical = cuentavertical(map);
        this.cuadricula = rellenarray();
    }

    public String run() {
        String resultado = "";
        char dir;

        //bucle infinito quee solo se rompera al encontrar el caracter final
        while (true) {
            //cada vuelta se movera el robot en la direccion adecuada y mete la direccion en el resultado final
            dir = direccion();
            resultado += dir;
            //determina la posicion actual del robot
            char actual = cuadricula[eje_y][eje_x];

            //mientras sea camino libre
            if (actual == ' ' || actual == 'X') {
                while (actual == ' ' || actual == 'X') {
                    //si la posicion siguiente es una pared, dejara de ir en esa direccion y saldra del bucle
                    if (siguiente(dir) == '#') {
                        break;
                    }
                    //el robot se mueve, se añade la direccion en el resultado, y se actualiza la posicion del robot
                    moverRobot(dir);
                    resultado += dir;
                    actual = cuadricula[eje_y][eje_x];

                    //en caso de que el siguiente movimiento sea a un portal, llamara al metodo para teletransportarse al otro portal
                    //y volvera a actualizar la posicion del robot
                    if( actual == 'T') {
                        portal();
                        moverRobot(dir);
                        resultado+=dir;
                        actual = cuadricula[eje_y][eje_x];
                    }
                }
            }

            //si es portal, se cambiara la posicion a donde este el otro portal
            if( actual == 'T') {
                portal();
                moverRobot(dir);
                resultado+=dir;
                actual = cuadricula[eje_y][eje_x];
            }

            //en caso de que sea posicion final, se acabara el programa
            if(actual == '$') {
                break;
            }

            //en caso de que te tenga que invertir la lista, se cambia el valor booleano de la variable invertido
            if (actual == 'I'){
                invertido = !invertido;
            }
        }
        return resultado;
    }

    private int cuentahorizontal(String mapa) {
        //averigua la longitud horizontal, contando caracteres hasta llegar al \n
        int longitud = 0;
        for (int i = 0; mapa.charAt(i) != '\n'; i++) {
            longitud++;
        }
        return longitud;
    }

    private int cuentavertical(String mapa){
        //averigua la longitud vertical, contando los saltos de linea
        int longitud = 0;
        for (int i = 0; i < mapa.length(); i++) {
            if (mapa.charAt(i) == '\n') {
                longitud ++;
            }
        }
        longitud++;
        return longitud;
    }

    private char[][] rellenarray(){
        //crea array bidimensional con la longitud vertial y horizontal del mapa
        char[][] array = new char[num_vertical][num_horizontal];
        int separacion = 0;
        //rellena array
        for (int i = 0; i<num_vertical; i++) {
            int aux = 0;
            for (int j = 0; aux<num_horizontal;j++) {
                //evita que se guarden los caracteres de \n en el array
                if (mapa.charAt(j+separacion) != '\n') {
                    array[i][aux] = mapa.charAt(j + separacion);
                    //cuando se encuentre la x, guardar esa posicion como inicial
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
        //segun la direccion recibida cambiara el eje x o y
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

        //en caso de que no este invertido, ira comprobando las direcciones por orden,
        //asegurandose de que el siguiente paso en esa direccion no es una pared
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
        //si esta invertido, cambia el orden de movimiento
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
        //especifica la posicion siguiente segun la direccion
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
        //busca el caracter T en el mapa, asegurandose que no es la posicion actual
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