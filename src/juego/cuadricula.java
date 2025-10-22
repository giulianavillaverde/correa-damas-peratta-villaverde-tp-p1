package juego;

import java.awt.Image;
import java.awt.Point;
import entorno.Entorno;
import entorno.Herramientas;

public class cuadricula {
    double x,y;
    double escala;
    double ancho, alto;
    Image imagen1,imagen2;
    Entorno e;
    double[] coorX;
    double[] coorY;
    boolean [] [] ocupado;
    
    public cuadricula(double x, double y, Entorno e) {
        this.x = x;
        this.y = y;
        this.e = e;
        this.imagen1 = Herramientas.cargarImagen("pasto1.jpg");
        this.imagen2 = Herramientas.cargarImagen("pasto2.jpg");
        this.escala = 0.143;
        this.ancho = this.imagen1.getWidth(null) * this.escala;
        this.alto = this.imagen1.getHeight(null) * this.escala;
        double[] aux1 = {70,170,270,370,470,570,670,770,870,980};
        double[] aux2 = {135,235,335,435,535};
        this.coorX = aux1;
        this.coorY = aux2;
        this.ocupado = new boolean [10] [5];
        for( int  j=0 ; j < 10; j++) {
            for (int i=0; i < 5; i++) {
                this.ocupado [j][i] = false;
                if (j==0) {
                    this.ocupado[j][i] = true;
                }
            }
        }
    }
    
    public void dibujar( ) {
        Image img;
        for (int i=0; i < 10 ; i++) {
            for (int j=0; j < 5 ; j++) {
                if ( (i+j) % 2 == 0) {
                    img = imagen1;
                }else {
                    img= imagen2;
                }
                e.dibujarImagen(img, this.coorX[i],  this.coorY[j], 0, escala);
            }
        }
    }
    
    public double distancia (double x1, double y1, double x2, double y2) {
        return Math.sqrt((x1-x2) * (x1-x2) + (y1-y2) * (y1-y2));
    }
    
    public Point cercanoL (double xM, double yM) {
        int im = 0;
        int jm = 0;
        for (int j=1; j < 10; j++) {
            for (int i=0; i < 5; i++) {
                if(!ocupado[j][i]) {
                    im = i;
                    jm = j;
                    break;
                }
            }
        }
        double distanciaM = distancia(xM, yM, coorX[jm], coorY[im]);
        for (int j=1; j < 10; j++) {
            for (int i=0; i < 5; i++) {
                if (distancia(xM, yM, coorX[j], coorY[i]) < distanciaM && !ocupado[j][i]) {
                    im = i;
                    jm = j;
                    distanciaM = distancia(xM, yM, coorX[j], coorY[i]);
                }
            }
        } 
        return new Point(jm, im);
    }
    
    public Point cercano(double xM, double yM) {
        int im =4;
        int jm =7;
        double distanciaM = distancia(xM,yM,coorX[jm],coorY[im]);
        for(int j=1;j< 8;j++) {
            for(int i=0; i < 5; i++) {
                if(distancia(xM,yM,coorX[j],coorY[i]) < distanciaM) {
                    im = i;
                    jm = j;
                    distanciaM = distancia(xM,yM,coorX[j],coorY[i]);
                }
            }
        }
        return new Point(jm,im);
    }
    
    // NUEVO MÉTODO PARA CENTRAR PLANTAS
    public void centrarPlanta(planta p, int indiceX, int indiceY) {
        p.x = this.coorX[indiceX];
        p.y = this.coorY[indiceY];
    }
}


