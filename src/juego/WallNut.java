package juego;

import java.awt.Image;
import java.awt.Color;
import entorno.Entorno;
import entorno.Herramientas;

public class WallNut {
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
    
    public WallNut(double x, double y, Entorno e) {
        this.x = x;
        this.y = y;
        this.xInicial = x;
        this.yInicial = y;
        this.e = e;
        this.plantada = false;
        this.seleccionada = false;
        this.disponibleParaPlantar = true;
        this.escala = 0.11;
        this.bajoAtaque = false;
        this.tiempoFinAtaque = 0;
        
        this.resistencia = 10;
        this.resistenciaMaxima = 10;
        this.tiempoRecargaPlantado = 360;
        
        try {
            this.imagen = Herramientas.cargarImagen("wallnut.png");
        } catch (Exception ex) {
            System.err.println("ERROR: No se pudo cargar wallnut.png");
            this.imagen = null;
        }
    }
    
    // DIBUJA LA PLANTA CON EFECTO VISUAL CUANDO ES ATACADA
    public void dibujar() {
        if (imagen != null) {
            e.dibujarImagen(imagen, x, y, 0, escala);
        }
        
        // Comportamiento específico de WallNut: círculo rojo cuando es atacada
        if (bajoAtaque && plantada) {
            e.dibujarCirculo(x, y, 45, new Color(255, 0, 0, 100));
        }
    }
    
    // MÉTODOS DE MOVIMIENTO Y POSICIÓN
    public void posicionActualPlanta(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    public void guardarPosicionInicial(double x, double y) {
        this.xInicial = x;
        this.yInicial = y;
    }
    
    public void arrastrar(double x, double y) {
        if (seleccionada) {
            posicionActualPlanta(x, y);
        }
    }
    
    // Verifica colisión para selección con mouse
    public boolean encima(double x, double y) {
        return Math.abs(this.x - x) < 40 && Math.abs(this.y - y) < 40;
    }
    
    // MÉTODOS DE RECARGA Y ESTADO
    public boolean estaEnRecarga(int tickActual) {
        return (tickActual - tiempoUltimoPlantado) < tiempoRecargaPlantado;
    }
    
    public double porcentajeRecarga(int tickActual) {
        if (tiempoUltimoPlantado == 0) return 1.0;
        int tiempoTranscurrido = tickActual - tiempoUltimoPlantado;
        return Math.min(1.0, (double) tiempoTranscurrido / tiempoRecargaPlantado);
    }
    
    public boolean estaPlantada() {
        return plantada;
    }
    
    // MÉTODOS ESPECÍFICOS DE WALLNUT 
    public void actualizar(int tickActual) {
        if (bajoAtaque && tickActual >= tiempoFinAtaque) {
            bajoAtaque = false;
        }
    }
    
    public void recibirAtaque(int tickActual) {
        this.resistencia--;
        this.bajoAtaque = true;
        this.tiempoFinAtaque = tickActual + 30;
        
        if (this.resistencia <= 0) {
            this.plantada = false;
        }
    }
    
    public void usar(int tickActual) {
        this.disponibleParaPlantar = false;
        this.tiempoUltimoPlantado = tickActual;
    }
    
   
    public void ejecutarComportamientoEspecifico(int tickActual, Juego juego) {
        // WallNut no tiene comportamiento especial, solo defiende
    }
}