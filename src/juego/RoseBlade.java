package juego;

import entorno.Entorno;

public class RoseBlade extends planta {
   
    public int tiempoRecargaDisparo;
    public int tiempoUltimoDisparo;
    public boolean puedeDisparar;
    
    public RoseBlade(double x, double y, Entorno e) {
        super(x, y, e, "roseblade.png", "roseblade.png", 0.10);
        
        this.resistencia = 5;
        this.resistenciaMaxima = 5;
        this.tiempoRecargaPlantado = 360;
        this.tiempoRecargaDisparo = 80;
        this.tiempoUltimoDisparo = -100;
        this.puedeDisparar = true;
    }
    
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
        BolaFuego nuevoDisparo = disparar(tickActual);
        if (nuevoDisparo != null) {
            juego.agregarDisparo(nuevoDisparo);
        }
    }
    
    public BolaFuego disparar(int tickActual) {
        if (puedeDisparar && plantada) {
            puedeDisparar = false;
            tiempoUltimoDisparo = tickActual;
            return new BolaFuego(x + 40, y, e);
        }
        return null;
    }
}