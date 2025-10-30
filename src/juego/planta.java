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
    public int resistencia; // NUEVO: Resistencia para todas las plantas
    public int resistenciaMaxima; // NUEVO: Resistencia máxima
    public boolean bajoAtaque; // NUEVO: Indicador visual de ataque
    
    public planta(double x, double y, Entorno e, String imgNormal, String imgSeleccionada, double escala) {
        this.x = x;
        this.y = y;
        this.xInicial = x;
        this.yInicial = y;
        this.e = e;
        this.escala = escala;
        this.seleccionada = false;
        this.plantada = false;
        this.resistencia = 1; // Valor por defecto
        this.resistenciaMaxima = 1; // Valor por defecto
        this.bajoAtaque = false;
        
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
    
    public void dibujar() {
        if (seleccionada) {
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
        
        // NUEVO: Efecto visual cuando está bajo ataque
        if (bajoAtaque && plantada) {
            e.dibujarCirculo(x, y, 35, new Color(255, 0, 0, 100));
        }
    }
    
    // NUEVO: Método para recibir daño
    public void recibirAtaque() {
        this.resistencia--;
        this.bajoAtaque = true;
        System.out.println(this.getClass().getSimpleName() + " recibió ataque, resistencia: " + resistencia + "/" + resistenciaMaxima);
        
        // Quitar el efecto visual después de un tiempo
        new java.util.Timer().schedule( 
            new java.util.TimerTask() {
                @Override
                public void run() {
                    bajoAtaque = false;
                }
            }, 
            500
        );
        
        if (resistencia <= 0) {
            System.out.println(this.getClass().getSimpleName() + " destruida!");
            plantada = false;
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