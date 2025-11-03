package juego;

import java.awt.Image;
import entorno.Entorno;
import entorno.Herramientas;

public class Banner {
    private double x, y, escala;
    private Image imagen;
    private Entorno e;
    
    // Constructor: inicializa el banner con posición, escala y carga la imagen
    public Banner(Entorno e) {
        this.x = -5;           // Posición X inicial 
        this.y = 28;           // Posición Y 
        this.e = e;            // Referencia 
        this.escala = 3;       // Amplia de la imagen (triplica el tamaño original)
        
        // Carga la imagen del banner
        this.imagen = Herramientas.cargarImagen("Bannerrr.jpg");
    }
    
    // Dibuja el banner en el entorno usando la imagen 
    public void dibujar() {
        // Pone la imagen en las coordenadas especificadas con la escala aplicada
        e.dibujarImagen(imagen, x, y, 0.0, escala);
    }
    
    // Getters para acceder a las propiedades del banner
    public double getX() { return x; }           // Obtiene la posición X actual
    public double getY() { return y; }           // Obtiene la posición Y actual
    public double getEscala() { return escala; } // Obtiene el factor de escala aplicado
    public Image getImagen() { return imagen; }  // Obtiene la referencia directa a la imagen
}