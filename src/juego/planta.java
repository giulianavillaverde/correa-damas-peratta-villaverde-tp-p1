package juego;

import java.awt.Image;
import java.awt.Color;
import entorno.Entorno;
import entorno.Herramientas;

public class planta {
    public double x, y;
    public double xInicial, yInicial;
    public double escala;
    public Image imagen, imagenSeleccionada;
    public Entorno e;
    public boolean seleccionada;
    public boolean plantada;
    
    // CONSTRUCTOR ACTUALIZADO - acepta parámetros de imágenes
    public planta(double x, double y, Entorno e, String imgNormal, String imgSeleccionada, double escala) {
        this.x = x;
        this.y = y;
        this.xInicial = x;
        this.yInicial = y;
        this.e = e;
        this.imagen = Herramientas.cargarImagen(imgNormal);
        this.imagenSeleccionada = Herramientas.cargarImagen(imgSeleccionada);
        this.escala = escala;
        this.seleccionada = false;
        this.plantada = false;
    }
    
    // Constructor viejo (por si acaso)
    public planta(double x, double y, Entorno e) {
        this(x, y, e, "planta1.jpg", "plantaSeleccionada.jpg", 0.10);
    }
    
    public void dibujar() {
        if (seleccionada) {
            e.dibujarCirculo(x, y, 40, Color.YELLOW);
            e.dibujarImagen(imagenSeleccionada, x, y, 0, escala);
        } else {
            e.dibujarImagen(imagen, x, y, 0, escala);
        }
    }
    
    public boolean encima(double xM, double yM) {
        double distancia = Math.sqrt((x - xM) * (x - xM) + (y - yM) * (y - yM));
        return distancia < 30;
    }
    
    public void arrastrar(double xM, double yM) {
        this.x = xM;
        this.y = yM;
    }
}