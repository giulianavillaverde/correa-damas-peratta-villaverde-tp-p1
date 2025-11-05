package juego;

import java.awt.Image;
import entorno.Entorno;
import entorno.Herramientas;

public class Banner {
    // Variables 
    public double x, y, escala;
    public Image imagen;
    public Entorno e;
    
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
} 