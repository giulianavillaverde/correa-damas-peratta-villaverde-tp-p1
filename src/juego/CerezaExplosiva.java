package juego;

import java.awt.Image;
import entorno.Entorno;
import entorno.Herramientas;

public class CerezaExplosiva {
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
    
    // Variables específicas públicas
    public double radioExplosion;
    public boolean explotando;
    public int tickExplosion;
    
    public CerezaExplosiva(double x, double y, Entorno e) {
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
        
        this.resistencia = 1;
        this.resistenciaMaxima = 1;
        this.tiempoRecargaPlantado = 720;
        this.radioExplosion = 200;
        this.explotando = false;
        this.tickExplosion = -1;
        
        try {
            this.imagen = Herramientas.cargarImagen("CerezaExplosiva.png");
        } catch (Exception ex) {
            System.err.println("ERROR: No se pudo cargar CerezaExplosiva.png");
            this.imagen = null;
        }
    }
    
    public void dibujar() {
        if (imagen != null) {
            e.dibujarImagen(imagen, x, y, 0, escala);
        }
    }
    
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
    
    public boolean estaPlantada() {
        return plantada;
    }
    
    // Métodos específicos de CerezaExplosiva
    public void actualizar(int tickActual) {
        if (bajoAtaque && tickActual >= tiempoFinAtaque) {
            bajoAtaque = false;
        }
    }
    
    public void recibirAtaque(int tickActual) {
        this.resistencia--;
        this.bajoAtaque = true;
        this.tiempoFinAtaque = tickActual + 30;
        
        if (this.resistencia <= 0 && !explotando) {
            explotando = true;
            tickExplosion = tickActual;
        }
    }
    
    public void usar(int tickActual) {
        this.disponibleParaPlantar = false;
        this.tiempoUltimoPlantado = tickActual;
    }
    
    public void ejecutarComportamientoEspecifico(int tickActual, Juego juego) {
        if (debeExplotar(tickActual)) {
            juego.ejecutarExplosionCereza(this, tickActual);
        }
    }
    
    public boolean hayZombieCerca(ZombieGrinch[] zombies) {
        if (!plantada || explotando) return false;
        
        for (ZombieGrinch zombie : zombies) {
            if (zombie != null && zombie.vivo) {
                double distancia = Math.sqrt(Math.pow(zombie.x - x, 2) + 
                                           Math.pow(zombie.y - y, 2));
                if (distancia < 50) {
                    explotando = true;
                    tickExplosion = e.numeroDeTick();
                    return true;
                }
            }
        }
        return false;
    }
    
    public boolean debeExplotar(int tickActual) {
        return explotando && tickExplosion != -1 && tickActual >= tickExplosion;
    }
}