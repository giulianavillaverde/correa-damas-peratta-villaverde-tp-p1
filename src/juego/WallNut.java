package juego;
import entorno.Entorno;

public class WallNut extends planta {
    public int resistencia;
    
    public WallNut(double x, double y, Entorno e) {
        super(x, y, e, "wallnut.png", "wallnut.png", 0.12);
        this.resistencia = 5; // Muy resistente - aguanta 5 ataques
    }
    
    // Wall-Nut no dispara, solo sirve de barrera
    // La resistencia se manejará cuando los zombies la ataquen
    
    public void recibirAtaque() {
        resistencia--;
        if (resistencia <= 0) {
            morir();
        }
    }
    
    private void morir() {
        // Liberar la casilla en la cuadrícula
        if (plantada) {
            int indiceX = obtenerIndiceX();
            int indiceY = obtenerIndiceY();
            if (indiceX >= 0 && indiceY >= 0) {
                // Necesitamos acceso a la cuadrícula - esto se manejará en la clase Juego
            }
        }
        // La planta se eliminará del array en la clase Juego
    }
    
    private int obtenerIndiceX() {
        // Calcular índice X basado en la posición (similar a cuadricula.cercanoL)
        for (int i = 0; i < 10; i++) {
            if (Math.abs(x - (70 + i * 100)) < 50) {
                return i;
            }
        }
        return -1;
    }
    
    private int obtenerIndiceY() {
        // Calcular índice Y basado en la posición
        for (int i = 0; i < 5; i++) {
            if (Math.abs(y - (135 + i * 100)) < 50) {
                return i;
            }
        }
        return -1;
    }
}