package juego;

import java.awt.Image;
import java.awt.Color;
import entorno.Entorno;
import entorno.Herramientas;

public abstract class planta {
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
    
    // ATRIBUTOS UNIFICADOS PARA TODAS LAS PLANTAS
    protected int tiempoRecargaPlantado;
    protected int tiempoUltimoPlantado;
    protected boolean disponibleParaPlantar;
    
    public planta(double x, double y, Entorno e, String imgNormal, String imgSeleccionada, double escala) {
        this.x = x;
        this.y = y;
        this.xInicial = x;
        this.yInicial = y;
        this.e = e;
        this.escala = escala;
        this.seleccionada = false;
        this.plantada = false;
        this.resistencia = 1;
        this.resistenciaMaxima = 1;
        this.bajoAtaque = false;
        this.tiempoFinAtaque = 0;
        
        this.tiempoRecargaPlantado = 0;
        this.tiempoUltimoPlantado = -100;
        this.disponibleParaPlantar = true;
        
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
        if (bajoAtaque && e.numeroDeTick() >= tiempoFinAtaque) {
            bajoAtaque = false;
        }
        
        Image imgActual = seleccionada && imagenSeleccionada != null ? imagenSeleccionada : imagen;
        if (imgActual != null) {
            e.dibujarImagen(imgActual, x, y, 0, escala);
        }
        
        if (bajoAtaque && plantada) {
            e.dibujarCirculo(x, y, 35, new Color(255, 0, 0, 100));
        }
    }
    
    // MÉTODO UNIFICADO PARA RECIBIR ATAQUE
    public void recibirAtaque(int tickActual) {
        this.resistencia--;
        this.bajoAtaque = true;
        this.tiempoFinAtaque = tickActual + 30;
        
        if (resistencia <= 0) {
            plantada = false;
        }
    }
    
    // MÉTODOS ABSTRACTOS QUE CADA PLANTA DEBE IMPLEMENTAR
    public abstract void actualizar(int tickActual);
    public abstract void usar(int tickActual);
    
    // MÉTODO PARA MANEJAR COMPORTAMIENTO ESPECÍFICO DE CADA PLANTA
    public abstract void ejecutarComportamientoEspecifico(int tickActual, Juego juego);
    
    // MÉTODOS COMUNES PARA TODAS LAS PLANTAS
    public boolean encima(double xM, double yM) {
        double distancia = Math.sqrt((x - xM) * (x - xM) + (y - yM) * (y - yM));
        return distancia < 30;
    }
    
    public void arrastrar(double xM, double yM) {
        this.x = xM;
        this.y = yM;
    }
    
    public boolean estaEnRecarga(int tickActual) {
        if (disponibleParaPlantar) return false;
        
        if (tickActual - tiempoUltimoPlantado >= tiempoRecargaPlantado) {
            disponibleParaPlantar = true;
            return false;
        }
        return true;
    }
    
    public double porcentajeRecarga(int tickActual) {
        if (disponibleParaPlantar) return 1.0;
        int tiempoTranscurrido = tickActual - tiempoUltimoPlantado;
        return Math.min(1.0, (double) tiempoTranscurrido / tiempoRecargaPlantado);
    }
    
    // GETTERS Y SETTERS
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
    public int getTiempoRecargaPlantado() { return tiempoRecargaPlantado; }
    public int getTiempoUltimoPlantado() { return tiempoUltimoPlantado; }
    
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