package juego;

import java.awt.Color;
import entorno.Entorno;

public class WallNut extends planta {
    
    public WallNut(double x, double y, Entorno e) {
        super(x, y, e, "wallnut.png", "wallnut.png", 0.11);
        
        this.setResistencia(10);
        this.setResistenciaMaxima(10);
        
        this.tiempoRecargaPlantado = 360;
    }
    
    @Override
    public void dibujar() {
        super.dibujar();
        
        if (isBajoAtaque() && isPlantada()) {
            getEntorno().dibujarCirculo(getX(), getY(), 45, new Color(255, 0, 0, 100));
        }
    }
    
    @Override
    public void actualizar(int tickActual) {
        if (isBajoAtaque() && tickActual >= getTiempoFinAtaque()) {
            setBajoAtaque(false);
        }
    }
    
    @Override
    public void usar(int tickActual) {
        this.disponibleParaPlantar = false;
        this.tiempoUltimoPlantado = tickActual;
    }
    
    @Override
    public void ejecutarComportamientoEspecifico(int tickActual, Juego juego) {
        // WallNut no tiene comportamiento especial, solo defiende
    }
    
    // Método específico para WallNut
    public int getTiempoUltimoUso() {
        return this.tiempoUltimoPlantado;
    }
}