package juego;

import java.awt.Color;
import entorno.Entorno;
import entorno.InterfaceJuego;

public class Juego extends InterfaceJuego
{
    Entorno entorno;
    Banner banner;
    cuadricula cuadricula;
    regalos[] regalo;
    planta[] plantas;
    int contadorPlantas;
    
    Juego(){
        this.entorno = new Entorno(this, "Proyecto para TP", 1034, 585);
        this.cuadricula = new cuadricula(50, 150, entorno);
        this.banner = new Banner(entorno);
        
        this.regalo = new regalos[5];
        for (int i = 0; i < regalo.length; i++) {
            double x = 50;
            double y = 140 + (i * 100);
            this.regalo[i] = new regalos(x, y, entorno);
        }
        
        this.plantas = new planta[30];
        this.contadorPlantas = 0;
        
        // Crear plantas iniciales en el banner
        crearGirasolEnBanner(50, 40);
        crearPlantaDeHieloEnBanner(150, 40);
        
        this.entorno.iniciar();
    }
    
    private void crearGirasolEnBanner(double x, double y) {
        if (contadorPlantas < plantas.length) {
            plantas[contadorPlantas] = new planta(x, y, entorno, "planta1.jpg", "plantaSeleccionada.jpg", 0.10);
            contadorPlantas++;
        }
    }
    
    private void crearPlantaDeHieloEnBanner(double x, double y) {
        if (contadorPlantas < plantas.length) {
            plantas[contadorPlantas] = new PlantaDeHielo(x, y, entorno);
            contadorPlantas++;
        }
    }

    public void tick(){
        entorno.colorFondo(Color.GREEN);
        
        this.banner.dibujar();
        this.cuadricula.dibujar();
        
        for (regalos r: this.regalo) {
            r.dibujar();
        }
        
        for(planta p: this.plantas) {
            if(p != null) {
                p.dibujar();
            }
        }
        
        manejarSeleccionYPlantado();
        manejarMovimientoTeclado();
        
        // Debug
        entorno.cambiarFont("Arial", 12, Color.WHITE);
        entorno.escribirTexto("Mouse: " + entorno.mouseX() + "," + entorno.mouseY(), 10, 570);
        entorno.escribirTexto("Plantas: " + contadorPlantas, 300, 570);
    }
    
    private void manejarSeleccionYPlantado() {
        // SELECCIÓN CON MOUSE
        if (entorno.sePresionoBoton(entorno.BOTON_IZQUIERDO)) {
            // Deseleccionar todas primero
            for (int i = 0; i < plantas.length; i++) {
                if (plantas[i] != null) {
                    plantas[i].seleccionada = false;
                }
            }
            
            // Seleccionar la planta clickeada
            for (int i = 0; i < plantas.length; i++) {
                if (plantas[i] != null && plantas[i].encima(entorno.mouseX(), entorno.mouseY())) {
                    plantas[i].seleccionada = true;
                    
                    // Si está plantada, liberar su casilla temporalmente
                    if (plantas[i].plantada) {
                        int indiceX = cuadricula.cercanoL(plantas[i].x, plantas[i].y).x;
                        int indiceY = cuadricula.cercanoL(plantas[i].x, plantas[i].y).y;
                        cuadricula.ocupado[indiceX][indiceY] = false;
                    }
                    break;
                }
            }
        }
        
        // ARRASTRE
        if (entorno.estaPresionado(entorno.BOTON_IZQUIERDO)) {
            for (planta p : plantas) {
                if (p != null && p.seleccionada) {
                    p.arrastrar(entorno.mouseX(), entorno.mouseY());
                }
            }
        }
        
        // SOLTAR - PLANTAR O VOLVER AL BANNER
        if (entorno.seLevantoBoton(entorno.BOTON_IZQUIERDO)) {
            for (int i = 0; i < plantas.length; i++) {
                if (plantas[i] != null && plantas[i].seleccionada) {
                    
                    if (entorno.mouseY() < 70) {
                        // Volver al banner - posición original
                        plantas[i].x = plantas[i].xInicial;
                        plantas[i].y = plantas[i].yInicial;
                        plantas[i].plantada = false;
                        
                    } else {
                        // Intentar plantar en cuadrícula
                        int indiceX = cuadricula.cercanoL(entorno.mouseX(), entorno.mouseY()).x;
                        int indiceY = cuadricula.cercanoL(entorno.mouseX(), entorno.mouseY()).y;
                        
                        if (!cuadricula.ocupado[indiceX][indiceY]) {
                            // CENTRAR la planta en la casilla
                            cuadricula.centrarPlanta(plantas[i], indiceX, indiceY);
                            cuadricula.ocupado[indiceX][indiceY] = true;
                            plantas[i].plantada = true;
                            
                            // CREAR NUEVA PLANTA EN EL BANNER
                            if (plantas[i] instanceof PlantaDeHielo) {
                                crearPlantaDeHieloEnBanner(150, 40);
                            } else {
                                crearGirasolEnBanner(50, 40);
                            }
                        } else {
                            // Casilla ocupada, volver al banner
                            plantas[i].x = plantas[i].xInicial;
                            plantas[i].y = plantas[i].yInicial;
                            plantas[i].plantada = false;
                        }
                    }
                }
            }
        }
    }
    
    private void manejarMovimientoTeclado() {
        for (planta p : plantas) {
            if (p != null && p.seleccionada && p.plantada) {
                moverPlantaConTeclado(p);
            }
        }
    }
    
    private void moverPlantaConTeclado(planta planta) {
        int indiceX = cuadricula.cercanoL(planta.x, planta.y).x;
        int indiceY = cuadricula.cercanoL(planta.x, planta.y).y;
        
        if (entorno.sePresiono(entorno.TECLA_ARRIBA) && indiceY > 0 && !cuadricula.ocupado[indiceX][indiceY-1]) {
            moverPlanta(planta, indiceX, indiceY, indiceX, indiceY-1);
        }
        else if (entorno.sePresiono(entorno.TECLA_ABAJO) && indiceY < 4 && !cuadricula.ocupado[indiceX][indiceY+1]) {
            moverPlanta(planta, indiceX, indiceY, indiceX, indiceY+1);
        }
        else if (entorno.sePresiono(entorno.TECLA_DERECHA) && indiceX < 9 && !cuadricula.ocupado[indiceX+1][indiceY]) {
            moverPlanta(planta, indiceX, indiceY, indiceX+1, indiceY);
        }
        else if (entorno.sePresiono(entorno.TECLA_IZQUIERDA) && indiceX > 0 && !cuadricula.ocupado[indiceX-1][indiceY]) {
            moverPlanta(planta, indiceX, indiceY, indiceX-1, indiceY);
        }
    }
    
    private void moverPlanta(planta planta, int xActual, int yActual, int xNuevo, int yNuevo) {
        cuadricula.ocupado[xActual][yActual] = false;
        // Usar el método centrar para mantener la planta centrada
        cuadricula.centrarPlanta(planta, xNuevo, yNuevo);
        cuadricula.ocupado[xNuevo][yNuevo] = true;
    }

    @SuppressWarnings("unused")
    public static void main(String[] args) {
        Juego juego = new Juego();
    }
}