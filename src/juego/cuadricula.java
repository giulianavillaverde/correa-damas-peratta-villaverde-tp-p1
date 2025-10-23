package juego;

import java.awt.Image;
import java.awt.Point;
import java.awt.Color;
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
        
        try {
            this.imagen1 = Herramientas.cargarImagen("pasto1.jpg");
        } catch (Exception ex) {
            System.err.println("ERROR: No se pudo cargar pasto1.jpg");
            this.imagen1 = null;
        }
        
        try {
            this.imagen2 = Herramientas.cargarImagen("pasto2.jpg");
        } catch (Exception ex) {
            System.err.println("ERROR: No se pudo cargar pasto2.jpg");
            this.imagen2 = null;
        }
        
        this.escala = 0.143;
        this.ancho = 100;
        this.alto = 100;
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
                if (imagen1 != null && imagen2 != null) {
                    if ( (i+j) % 2 == 0) {
                        img = imagen1;
                    } else {
                        img = imagen2;
                    }
                    e.dibujarImagen(img, this.coorX[i],  this.coorY[j], 0, escala);
                } else {
                    // Fallback si no hay imágenes
                    Color color = ((i+j) % 2 == 0) ? new Color(100, 200, 100) : new Color(120, 220, 120);
                    e.dibujarRectangulo(coorX[i], coorY[j], 90, 90, 0, color);
                }
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
        int im = 4;
        int jm = 7;
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
    
    public void centrarPlanta(planta p, int indiceX, int indiceY) {
        p.x = this.coorX[indiceX];
        p.y = this.coorY[indiceY];
    }
} 