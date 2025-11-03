package juego;

import java.awt.Color;
import entorno.Entorno;

public class WallNut extends planta {
    
    public WallNut(double x, double y, Entorno e) {
        super(x, y, e, "wallnut.png", "wallnut.png", 0.11);
        
        this.resistencia = 10;
        this.resistenciaMaxima = 10;
        this.tiempoRecargaPlantado = 360;
    }
    
    public void dibujar() {
        super.dibujar();
        
        if (bajoAtaque && plantada) {
            e.dibujarCirculo(x, y, 45, new Color(255, 0, 0, 100));
        }
    }
    
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