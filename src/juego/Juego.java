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
    Zombie[] zombies;
    BolaFuego[] disparos;
    BolaEscarcha[] disparosHielo; // DESCOMENTADO - sistema de escarcha
    int contadorPlantas;
    int zombiesEliminados;
    int zombiesTotales;
    boolean juegoGanado;
    boolean juegoPerdido;
    
    public Juego(){
        this.entorno = new Entorno(this, "Zombies Grinch", 1034, 585);
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
        
        this.zombies = new Zombie[15];
        this.disparos = new BolaFuego[50];
        this.disparosHielo = new BolaEscarcha[50]; // DESCOMENTADO
        this.zombiesEliminados = 0;
        this.zombiesTotales = 10;
        this.juegoGanado = false;
        this.juegoPerdido = false;
        
        crearGirasolEnBanner(50, 40);
        crearPlantaDeHieloEnBanner(150, 40);
        crearRoseBladeEnBanner(250, 40);
        
        this.entorno.iniciar();
    }
    
    private void crearGirasolEnBanner(double x, double y) {
        if (contadorPlantas < plantas.length) {
            plantas[contadorPlantas] = new planta(x, y, entorno, "planta1.jpg", "planta1.jpg", 0.10);
            contadorPlantas++;
        }
    }
    
    private void crearPlantaDeHieloEnBanner(double x, double y) {
        if (contadorPlantas < plantas.length) {
            plantas[contadorPlantas] = new PlantaDeHielo(x, y, entorno);
            contadorPlantas++;
        }
    }
    
    private void crearRoseBladeEnBanner(double x, double y) {
        if (contadorPlantas < plantas.length) {
            plantas[contadorPlantas] = new RoseBlade(x, y, entorno);
            contadorPlantas++;
        }
    }

    public void tick(){
        if (juegoGanado || juegoPerdido) {
            dibujarPantallaFin();
            return;
        }
        
        entorno.colorFondo(Color.GREEN);
        this.banner.dibujar();
        this.cuadricula.dibujar();
        
        for (regalos r: this.regalo) {
            r.dibujar();
        }
        
        // ACTUALIZAR Y DIBUJAR PLANTAS
        for(planta p: this.plantas) {
            if(p != null) {
                p.dibujar();
                
                if (p instanceof RoseBlade && p.plantada) {
                    RoseBlade rose = (RoseBlade) p;
                    rose.actualizar(entorno.numeroDeTick());
                    BolaFuego nuevoDisparo = rose.disparar(entorno.numeroDeTick());
                    if (nuevoDisparo != null) {
                        agregarDisparo(nuevoDisparo);
                    }
                }
                // Disparos de PlantaDeHielo (con BolaEscarcha real)
                else if (p instanceof PlantaDeHielo && p.plantada) {
                    PlantaDeHielo plantaHielo = (PlantaDeHielo) p;
                    plantaHielo.actualizar(entorno.numeroDeTick());
                    BolaEscarcha nuevoDisparoHielo = plantaHielo.disparar(entorno.numeroDeTick());
                    if (nuevoDisparoHielo != null) {
                        agregarDisparoHielo(nuevoDisparoHielo);
                    }
                }
            }
        }
        
        generarZombie();
        actualizarZombies();
        actualizarDisparos();
        actualizarDisparosHielo(); // DESCOMENTADO
        verificarColisiones();
        verificarColisionesHielo(); // DESCOMENTADO
        manejarSeleccionYPlantado();
        manejarMovimientoTeclado();
        verificarFinJuego();
        dibujarUI();
    }
    
    private void agregarDisparo(BolaFuego disparo) {
        for (int i = 0; i < disparos.length; i++) {
            if (disparos[i] == null) {
                disparos[i] = disparo;
                break;
            }
        }
    }
    
    private void agregarDisparoHielo(BolaEscarcha disparo) {
        for (int i = 0; i < disparosHielo.length; i++) {
            if (disparosHielo[i] == null) {
                disparosHielo[i] = disparo;
                break;
            }
        }
    }
    
    private void generarZombie() {
        if (Math.random() < 0.005 && zombiesEliminados < zombiesTotales) {
            for (int i = 0; i < zombies.length; i++) {
                if (zombies[i] == null) {
                    int fila = (int)(Math.random() * 5);
                    zombies[i] = new Zombie(fila, entorno);
                    break;
                }
            }
        }
    }
    
    private void actualizarZombies() {
        for (int i = 0; i < zombies.length; i++) {
            if (zombies[i] != null) {
                zombies[i].mover();
                zombies[i].dibujar();
                
                if (zombies[i].llegoARegalos()) {
                    juegoPerdido = true;
                }
                
                if (!zombies[i].vivo) {
                    zombies[i] = null;
                    zombiesEliminados++;
                }
            }
        }
    }
    
    private void actualizarDisparos() {
        for (int i = 0; i < disparos.length; i++) {
            if (disparos[i] != null) {
                disparos[i].mover();
                disparos[i].dibujar();
                if (!disparos[i].activa) {
                    disparos[i] = null;
                }
            }
        }
    }
    
    private void actualizarDisparosHielo() {
        for (int i = 0; i < disparosHielo.length; i++) {
            if (disparosHielo[i] != null) {
                disparosHielo[i].mover();
                disparosHielo[i].dibujar();
                if (!disparosHielo[i].activa) {
                    disparosHielo[i] = null;
                }
            }
        }
    }
    
    private void verificarColisiones() {
        for (int i = 0; i < disparos.length; i++) {
            if (disparos[i] != null && disparos[i].activa) {
                for (int j = 0; j < zombies.length; j++) {
                    if (zombies[j] != null && zombies[j].vivo && 
                        disparos[i].colisionaCon(zombies[j])) {
                        zombies[j].recibirDanio();
                        disparos[i].activa = false;
                        disparos[i] = null;
                        break;
                    }
                }
            }
        }
    }
    
    private void verificarColisionesHielo() {
        for (int i = 0; i < disparosHielo.length; i++) {
            if (disparosHielo[i] != null && disparosHielo[i].activa) {
                for (int j = 0; j < zombies.length; j++) {
                    if (zombies[j] != null && zombies[j].vivo && 
                        disparosHielo[i].colisionaCon(zombies[j])) {
                        zombies[j].ralentizar(disparosHielo[i].duracionRalentizacion);
                        disparosHielo[i].activa = false;
                        disparosHielo[i] = null;
                        break;
                    }
                }
            }
        }
    }
    
    private void manejarSeleccionYPlantado() {
        if (entorno.sePresionoBoton(entorno.BOTON_IZQUIERDO)) {
            for (int i = 0; i < plantas.length; i++) {
                if (plantas[i] != null) {
                    plantas[i].seleccionada = false;
                }
            }
            
            for (int i = 0; i < plantas.length; i++) {
                if (plantas[i] != null && plantas[i].encima(entorno.mouseX(), entorno.mouseY())) {
                    plantas[i].seleccionada = true;
                    if (plantas[i].plantada) {
                        int indiceX = cuadricula.cercanoL(plantas[i].x, plantas[i].y).x;
                        int indiceY = cuadricula.cercanoL(plantas[i].x, plantas[i].y).y;
                        cuadricula.ocupado[indiceX][indiceY] = false;
                    }
                    break;
                }
            }
        }
        
        if (entorno.estaPresionado(entorno.BOTON_IZQUIERDO)) {
            for (planta p : plantas) {
                if (p != null && p.seleccionada) {
                    p.arrastrar(entorno.mouseX(), entorno.mouseY());
                }
            }
        }
        
        if (entorno.seLevantoBoton(entorno.BOTON_IZQUIERDO)) {
            for (int i = 0; i < plantas.length; i++) {
                if (plantas[i] != null && plantas[i].seleccionada) {
                    if (entorno.mouseY() < 70) {
                        plantas[i].x = plantas[i].xInicial;
                        plantas[i].y = plantas[i].yInicial;
                        plantas[i].plantada = false;
                    } else {
                        int indiceX = cuadricula.cercanoL(entorno.mouseX(), entorno.mouseY()).x;
                        int indiceY = cuadricula.cercanoL(entorno.mouseX(), entorno.mouseY()).y;
                        
                        if (!cuadricula.ocupado[indiceX][indiceY]) {
                            cuadricula.centrarPlanta(plantas[i], indiceX, indiceY);
                            cuadricula.ocupado[indiceX][indiceY] = true;
                            plantas[i].plantada = true;
                            
                            if (plantas[i] instanceof PlantaDeHielo) {
                                crearPlantaDeHieloEnBanner(150, 40);
                            } else if (plantas[i] instanceof RoseBlade) {
                                crearRoseBladeEnBanner(250, 40);
                            } else {
                                crearGirasolEnBanner(50, 40);
                            }
                        } else {
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
        for (int i = 0; i < plantas.length; i++) {
            if (plantas[i] != null && plantas[i].seleccionada && plantas[i].plantada) {
                moverPlantaConTeclado(plantas[i]);
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
        planta.x = cuadricula.coorX[xNuevo];
        planta.y = cuadricula.coorY[yNuevo];
        cuadricula.ocupado[xNuevo][yNuevo] = true;
    }
    
    private void dibujarUI() {
        entorno.cambiarFont("Arial", 20, Color.WHITE);
        entorno.escribirTexto("Zombies: " + zombiesEliminados + "/" + zombiesTotales, 800, 30);
    }
    
    private void verificarFinJuego() {
        if (zombiesEliminados >= zombiesTotales) {
            juegoGanado = true;
        }
    }
    
    private void dibujarPantallaFin() {
        entorno.colorFondo(Color.BLACK);
        if (juegoGanado) {
            entorno.cambiarFont("Arial", 40, Color.GREEN);
            entorno.escribirTexto("¡VICTORIA!", 400, 300);
        } else if (juegoPerdido) {
            entorno.cambiarFont("Arial", 40, Color.RED);
            entorno.escribirTexto("¡DERROTA!", 400, 300);
        }
    }

    public static void main(String[] args) {
        new Juego();
    }
}