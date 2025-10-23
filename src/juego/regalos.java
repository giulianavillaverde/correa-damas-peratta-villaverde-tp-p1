package juego;

import java.awt.Image;
import java.awt.Color;
import entorno.Entorno;
import entorno.Herramientas;

public class regalos {
    double x,y,escala,alto,ancho;
    double angulo;
    Entorno e;
    Image imagen;
    
    public regalos(double x, double y, Entorno e) {
        this.x = x;
        this.y = y;
        this.e = e;
        this.escala = 0.15;
        this.angulo = 0;
        
        try {
            this.imagen = Herramientas.cargarImagen("regalo.png");
        } catch (Exception ex) {
            System.err.println("ERROR: No se pudo cargar regalo.png");
            this.imagen = null;
        }
        
        this.ancho = 30;
        this.alto = 30;
    }
    
    public void dibujar() {
        if (imagen != null) {
            e.dibujarImagen(imagen, x, y, this.angulo, this.escala);
        } else {
            // Fallback si no hay imagen
            e.dibujarRectangulo(x, y, 25, 25, 0, Color.RED);
            e.dibujarRectangulo(x, y, 15, 25, 0, Color.GREEN);
        }
    }
}