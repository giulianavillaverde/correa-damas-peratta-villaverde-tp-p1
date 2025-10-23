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
    
    public planta(double x, double y, Entorno e, String imgNormal, String imgSeleccionada, double escala) {
        this.x = x;
        this.y = y;
        this.xInicial = x;
        this.yInicial = y;
        this.e = e;
        this.escala = escala;
        this.seleccionada = false;
        this.plantada = false;
        
        try {
            this.imagen = Herramientas.cargarImagen(imgNormal);
        } catch (Exception ex) {
            System.err.println("ERROR: No se pudo cargar " + imgNormal);
            this.imagen = null;
        }
        
        try {
            this.imagenSeleccionada = Herramientas.cargarImagen(imgSeleccionada);
        } catch (Exception ex) {
            System.err.println("ERROR: No se pudo cargar " + imgSeleccionada);
            this.imagenSeleccionada = null;
        }
    }
    
    public planta(double x, double y, Entorno e) {
        this(x, y, e, "planta1.jpg", "planta1.jpg", 0.10);
    }
    
    public void dibujar() {
        if (seleccionada) {
            // SOLO cambia la imagen, sin círculo amarillo
            if (imagenSeleccionada != null) {
                e.dibujarImagen(imagenSeleccionada, x, y, 0, escala);
            } else if (imagen != null) {
                e.dibujarImagen(imagen, x, y, 0, escala);
            }
        } else {
            if (imagen != null) {
                e.dibujarImagen(imagen, x, y, 0, escala);
            }
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