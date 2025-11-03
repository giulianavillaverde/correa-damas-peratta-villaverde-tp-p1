package juego;

import java.awt.Image;
import entorno.Entorno;
import entorno.Herramientas;

public class PlantaDeHielo {
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
    
    // Variables específicas 
    public int tiempoRecargaDisparo;
    public int tiempoUltimoDisparo;
    public boolean puedeDisparar;
    
    public PlantaDeHielo(double x, double y, Entorno e) {
        this.x = x;
        this.y = y;
        this.xInicial = x;
        this.yInicial = y;
        this.e = e;
        this.plantada = false;
        this.seleccionada = false;
        this.disponibleParaPlantar = true;
        this.escala = 0.12;
        this.bajoAtaque = false;
        this.tiempoFinAtaque = 0;
        
        this.resistencia = 5;
        this.resistenciaMaxima = 5;
        this.tiempoRecargaPlantado = 1080;
        this.tiempoRecargaDisparo = 45;
        this.tiempoUltimoDisparo = -100;
        this.puedeDisparar = true;
        
        try {
            this.imagen = Herramientas.cargarImagen("plantaHielo.png");
        } catch (Exception ex) {
            System.err.println("ERROR: No se pudo cargar plantaHielo.png");
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
    
    // Métodos específicos de PlantaDeHielo
    public void actualizar(int tickActual) {
        if (!puedeDisparar && tickActual - tiempoUltimoDisparo > tiempoRecargaDisparo) {
            puedeDisparar = true;
        }
        
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
        BolaEscarcha nuevoDisparoHielo = disparar(tickActual);
        if (nuevoDisparoHielo != null) {
            juego.agregarDisparoHielo(nuevoDisparoHielo);
        }
    }
    
    public BolaEscarcha disparar(int tickActual) {
        if (puedeDisparar && plantada) {
            puedeDisparar = false;
            tiempoUltimoDisparo = tickActual;
            return new BolaEscarcha(x + 40, y, e);
        }
        return null;
    }
}