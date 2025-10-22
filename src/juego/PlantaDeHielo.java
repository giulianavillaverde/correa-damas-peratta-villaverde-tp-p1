package juego;
import entorno.Entorno;

public class PlantaDeHielo extends planta {
    
    public PlantaDeHielo(double x, double y, Entorno e) {
        // Usa el mismo tamaño que el girasol (0.10) para consistencia
        super(x, y, e, "plantaHielo.png", "plantaDeHieloSeleccionada.png", 0.08);
    }
}