package juego;

import java.awt.Image;
import java.awt.Color;
import entorno.Entorno;
import entorno.Herramientas;

public class planta {
    private double x, y;
    private double xInicial, yInicial;
    private double escala;
    private Image imagen, imagenSeleccionada;
    private Entorno e;
    private boolean seleccionada;
    private boolean plantada;
    private int resistencia;
    private int resistenciaMaxima;
    private boolean bajoAtaque;
    private int tiempoFinAtaque;
    
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
        this.tiempoFinAtaque = 0;
        
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
        // Manejar efecto de ataque basado en ticks
        if (bajoAtaque && e.numeroDeTick() >= tiempoFinAtaque) {
            bajoAtaque = false;
        }
        
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
        
        // Efecto visual cuando está bajo ataque
        if (bajoAtaque && plantada) {
            e.dibujarCirculo(x, y, 35, new Color(255, 0, 0, 100));
        }
    }
    
    // Método para recibir daño
    public void recibirAtaque(int tickActual) {
        this.resistencia--;
        this.bajoAtaque = true;
        this.tiempoFinAtaque = tickActual + 30; // 30 ticks ≈ 500ms
        System.out.println(this.getClass().getSimpleName() + " recibió ataque, resistencia: " + resistencia + "/" + resistenciaMaxima);
        
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
    
    // Getters
    public double getX() { return x; }
    public double getY() { return y; }
    public double getXInicial() { return xInicial; }
    public double getYInicial() { return yInicial; }
    public boolean isSeleccionada() { return seleccionada; }
    public boolean isPlantada() { return plantada; }
    public int getResistencia() { return resistencia; }
    public int getResistenciaMaxima() { return resistenciaMaxima; }
    public boolean isBajoAtaque() { return bajoAtaque; }
    public int getTiempoFinAtaque() { return tiempoFinAtaque; }
    public Image getImagen() { return imagen; }
    public Image getImagenSeleccionada() { return imagenSeleccionada; }
    public Entorno getEntorno() { return e; }
    
    // Setters
    public void setSeleccionada(boolean seleccionada) { this.seleccionada = seleccionada; }
    public void setPlantada(boolean plantada) { this.plantada = plantada; }
    public void setResistencia(int resistencia) { this.resistencia = resistencia; }
    public void setResistenciaMaxima(int resistenciaMaxima) { this.resistenciaMaxima = resistenciaMaxima; }
    public void setBajoAtaque(boolean bajoAtaque) { this.bajoAtaque = bajoAtaque; }
    public void setTiempoFinAtaque(int tiempoFinAtaque) { this.tiempoFinAtaque = tiempoFinAtaque; }
    public void setPosicion(double x, double y) { 
        this.x = x; 
        this.y = y; 
    }
    public void setPosicionInicial(double x, double y) { 
        this.xInicial = x; 
        this.yInicial = y; 
    }
}