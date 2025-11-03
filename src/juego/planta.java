package juego;

import java.awt.Image;
import entorno.Entorno;
import entorno.Herramientas;

public abstract class planta {
    // Variables comunes a todas las plantas
    public double x, y;
    public double xInicial, yInicial;
    public Entorno e;
    public Image imagen;
    public boolean plantada;
    public boolean seleccionada;
    public boolean disponibleParaPlantar;
    public int resistencia;
    public int resistenciaMaxima;
    public int tiempoRecargaPlantado;
    public int tiempoUltimoPlantado;
    public boolean bajoAtaque;
    public int tiempoFinAtaque;
    public double escala;
    
    public planta(double x, double y, Entorno e, String imagenPath, String imagenSeleccionadaPath, double escala) {
        this.x = x;
        this.y = y;
        this.xInicial = x;
        this.yInicial = y;
        this.e = e;
        this.plantada = false;
        this.seleccionada = false;
        this.disponibleParaPlantar = true;
        this.escala = escala;
        this.bajoAtaque = false;
        this.tiempoFinAtaque = 0;
        
        try {
            this.imagen = Herramientas.cargarImagen(imagenPath);
        } catch (Exception ex) {
            System.err.println("ERROR: No se pudo cargar " + imagenPath);
            this.imagen = null;
        }
    }
    
    public void dibujar() {
        if (imagen != null) {
            e.dibujarImagen(imagen, x, y, 0, escala);
        }
    }
    
    public void setPosicion(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    public void setPosicionInicial(double x, double y) {
        this.xInicial = x;
        this.yInicial = y;
    }
    
    public void arrastrar(double x, double y) {
        if (seleccionada) {
            setPosicion(x, y);
        }
    }
    
    public boolean encima(double x, double y) {
        return Math.abs(this.x - x) < 40 && Math.abs(this.y - y) < 40;
    }
    
    public boolean estaEnRecarga(int tickActual) {
        return (tickActual - tiempoUltimoPlantado) < tiempoRecargaPlantado;
    }
    
    public double porcentajeRecarga(int tickActual) {
        if (tiempoUltimoPlantado == 0) return 1.0;
        int tiempoTranscurrido = tickActual - tiempoUltimoPlantado;
        return Math.min(1.0, (double) tiempoTranscurrido / tiempoRecargaPlantado);
    }
    
    public boolean isPlantada() {
        return plantada;
    }
    
    
    public abstract void actualizar(int tickActual);
    public abstract void usar(int tickActual);
    public abstract void ejecutarComportamientoEspecifico(int tickActual, Juego juego);
    public abstract void recibirAtaque(int tickActual);
}