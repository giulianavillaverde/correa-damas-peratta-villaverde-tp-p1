package juego;

import java.awt.Image;
import entorno.Entorno;
import entorno.Herramientas;

public class BolaNieve {
    public double x, y;
    public double velocidad;
    public boolean activa;
    public int danio;
    public Entorno e;
    public Image imagen;
    
    public BolaNieve(double x, double y, Entorno e) {
        this.x = x;
        this.y = y;
        this.velocidad = 2;
        this.activa = true;
        this.danio = 1;
        this.e = e;
        
        // CARGAR IMAGEN DE BOLA DE NIEVE
        try {
            this.imagen = Herramientas.cargarImagen("Snowball.png");
        } catch (Exception ex) {
            this.imagen = null;
        }
    }
    
    public void mover() {
        if (activa) {
            x -= velocidad;
            if (x < -50) {
                activa = false;
            }
        }
    }
    
    public void dibujar() {
        if (activa && imagen != null) {
            e.dibujarImagen(imagen, x, y, 0, 0.05);
        }
    }
    
    public boolean colisionaCon(planta p) {
        if (!activa || p == null || !p.plantada) return false;
        
        double distancia = Math.sqrt(Math.pow(x - p.x, 2) + Math.pow(y - p.y, 2));
        return distancia < 30;
    }
}