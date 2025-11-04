package juego;

import java.awt.Image;
import java.awt.Point;
import java.awt.Color;
import entorno.Entorno;
import entorno.Herramientas;

public class cuadricula {
    public double x, y;
    public double escala;
    public double ancho, alto;
    public Image imagen1, imagen2;
    public Entorno e;
    double[] coorX;
    double[] coorY;
    public boolean[][] ocupado;
    
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
        double[] aux1 = {70,170,270,370,470,570,670,770,870,970};
        double[] aux2 = {135,235,335,435,535};
        this.coorX = aux1;
        this.coorY = aux2;
        this.ocupado = new boolean[10][5];
        
        for(int j = 0; j < 10; j++) {
            for (int i = 0; i < 5; i++) {
                this.ocupado[j][i] = false;
                if (j == 0) {
                    this.ocupado[j][i] = true;
                }
            }
        }
    }
    
    public void dibujar() {
        Image img;
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 5; j++) {
                if (imagen1 != null && imagen2 != null) {
                    if ((i + j) % 2 == 0) {
                        img = imagen1;
                    } else {
                        img = imagen2;
                    }
                    e.dibujarImagen(img, this.coorX[i], this.coorY[j], 0, escala);
                } else {
                    Color color = ((i + j) % 2 == 0) ? new Color(100, 200, 100) : new Color(120, 220, 120);
                    e.dibujarRectangulo(coorX[i], coorY[j], 90, 90, 0, color);
                }
            }
        }
    }
    
    private double distancia(double x1, double y1, double x2, double y2) {
        return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }
    
    public Point cercanoL(double xM, double yM) {
        int im = 0;
        int jm = 1;
        double distanciaM = 10000;
        
        for (int j = 1; j < 10; j++) {
            for (int i = 0; i < 5; i++) {
                double dist = distancia(xM, yM, coorX[j], coorY[i]);
                if (dist < distanciaM) {
                    im = i;
                    jm = j;
                    distanciaM = dist;
                }
            }
        } 
        return new Point(jm, im);
    }
    
    public Point cercano(double xM, double yM) {
        int im = 0;
        int jm = 1;
        double distanciaM = 10000;
        
        for(int j = 1; j < 10; j++) {
            for(int i = 0; i < 5; i++) {
                double dist = distancia(xM, yM, coorX[j], coorY[i]);
                if(dist < distanciaM) {
                    im = i;
                    jm = j;
                    distanciaM = dist;
                }
            }
        }
        return new Point(jm, im);
    }
    
 // En la clase cuadricula, asegúrate de tener estos métodos:
    public void centrarWallnut(WallNut planta, int indiceX, int indiceY) {
        planta.x = coorX[indiceX];
        planta.y = coorY[indiceY];
    }

    public void centrarPlantaHielo(PlantaDeHielo planta, int indiceX, int indiceY) {
        planta.x = coorX[indiceX];
        planta.y = coorY[indiceY];
    }

    public void centrarRoseBlade(RoseBlade planta, int indiceX, int indiceY) {
        planta.x = coorX[indiceX];
        planta.y = coorY[indiceY];
    }

    public void centrarCereza(CerezaExplosiva planta, int indiceX, int indiceY) {
        planta.x = coorX[indiceX];
        planta.y = coorY[indiceY];
    }
    
    public void Ocupado(int x, int y, boolean valor) {
        if (x >= 0 && x < ocupado.length && y >= 0 && y < ocupado[0].length) {
            ocupado[x][y] = valor;
        }
    }
    
    public boolean estaOcupado(int x, int y) {
        if (x >= 0 && x < ocupado.length && y >= 0 && y < ocupado[0].length) {
            return ocupado[x][y];
        }
        return true;
    }
}