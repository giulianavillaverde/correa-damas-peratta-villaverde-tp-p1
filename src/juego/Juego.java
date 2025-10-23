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
    BolaEscarcha[] disparosHielo;
    int contadorPlantas;
    int zombiesEliminados;
    int zombiesTotales;
    boolean juegoGanado;
    boolean juegoPerdido;
    
    // Plantas en el banner para controlar recarga
    planta wallnutBanner; // CAMBIADO: Girasol por WallNut
    planta hieloBanner; 
    planta roseBanner;
    
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
        this.disparosHielo = new BolaEscarcha[50];
        this.zombiesEliminados = 0;
        this.zombiesTotales = 50;
        this.juegoGanado = false;
        this.juegoPerdido = false;
        
        // Crear plantas del banner
        crearPlantasBanner();
        
        this.entorno.iniciar();
    }
    
    // NUEVO: Método para crear plantas del banner
    private void crearPlantasBanner() {
        this.wallnutBanner = new WallNut(50, 40, entorno);
        this.hieloBanner = new PlantaDeHielo(150, 40, entorno);
        this.roseBanner = new RoseBlade(250, 40, entorno);
        
        // Agregar plantas al array
        plantas[contadorPlantas++] = wallnutBanner;
        plantas[contadorPlantas++] = hieloBanner;
        plantas[contadorPlantas++] = roseBanner;
    }
    
    // CAMBIADO: crearWallNutEnBanner en lugar de crearGirasolEnBanner
    private void crearWallNutEnBanner() {
        if (contadorPlantas < plantas.length) {
            WallNut nuevaWallnut = new WallNut(50, 40, entorno);
            plantas[contadorPlantas] = nuevaWallnut;
            wallnutBanner = nuevaWallnut;
            contadorPlantas++;
        }
    }
    
    private void crearPlantaDeHieloEnBanner() {
        if (contadorPlantas < plantas.length) {
            PlantaDeHielo nuevaHielo = new PlantaDeHielo(150, 40, entorno);
            plantas[contadorPlantas] = nuevaHielo;
            hieloBanner = nuevaHielo;
            contadorPlantas++;
        }
    }
    
    private void crearRoseBladeEnBanner() {
        if (contadorPlantas < plantas.length) {
            RoseBlade nuevaRose = new RoseBlade(250, 40, entorno);
            plantas[contadorPlantas] = nuevaRose;
            roseBanner = nuevaRose;
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
                // WallNut no dispara, solo sirve de barrera
            }
        }
        
        generarZombie();
        actualizarZombies();
        actualizarDisparos();
        actualizarDisparosHielo();
        verificarColisiones();
        verificarColisionesHielo();
        verificarAtaquesZombies(); // NUEVO: Verificar ataques de zombies a plantas
        manejarSeleccionYPlantado();
        manejarMovimientoTeclado();
        verificarFinJuego();
        dibujarUI();
        dibujarBarrasRecarga();
    }
    
    // NUEVO: Método para manejar movimiento con teclado
    private void manejarMovimientoTeclado() {
        // Buscar la planta seleccionada
        planta plantaSeleccionada = null;
        for (planta p : plantas) {
            if (p != null && p.seleccionada) {
                plantaSeleccionada = p;
                break;
            }
        }
        
        if (plantaSeleccionada == null || plantaSeleccionada.plantada) {
            return; // No hay planta seleccionada o ya está plantada
        }
        
        // Mover con teclas WASD
        double velocidad = 3;
        if (entorno.estaPresionada('w') || entorno.estaPresionada(entorno.TECLA_ARRIBA)) {
            plantaSeleccionada.y -= velocidad;
        }
        if (entorno.estaPresionada('s') || entorno.estaPresionada(entorno.TECLA_ABAJO)) {
            plantaSeleccionada.y += velocidad;
        }
        if (entorno.estaPresionada('a') || entorno.estaPresionada(entorno.TECLA_IZQUIERDA)) {
            plantaSeleccionada.x -= velocidad;
        }
        if (entorno.estaPresionada('d') || entorno.estaPresionada(entorno.TECLA_DERECHA)) {
            plantaSeleccionada.x += velocidad;
        }
        
        // Limitar movimiento dentro de los bordes de la pantalla
        plantaSeleccionada.x = Math.max(30, Math.min(plantaSeleccionada.x, 1000));
        plantaSeleccionada.y = Math.max(30, Math.min(plantaSeleccionada.y, 550));
    }
    
    // NUEVO: Verificar cuando los zombies atacan plantas
    private void verificarAtaquesZombies() {
        for (int i = 0; i < zombies.length; i++) {
            if (zombies[i] != null && zombies[i].vivo) {
                for (int j = 0; j < plantas.length; j++) {
                    if (plantas[j] != null && plantas[j].plantada && 
                        zombies[i].colisionaConPlanta(plantas[j])) {
                        
                        // El zombie ataca la planta
                        if (plantas[j] instanceof WallNut) {
                            WallNut wallnut = (WallNut) plantas[j];
                            wallnut.recibirAtaque();
                            // Si la WallNut muere, eliminarla
                            if (wallnut.resistencia <= 0) {
                                // Liberar la casilla en la cuadrícula
                                int indiceX = cuadricula.cercanoL(plantas[j].x, plantas[j].y).x;
                                int indiceY = cuadricula.cercanoL(plantas[j].x, plantas[j].y).y;
                                cuadricula.ocupado[indiceX][indiceY] = false;
                                plantas[j] = null;
                            }
                        } else {
                            // Otras plantas mueren instantáneamente
                            int indiceX = cuadricula.cercanoL(plantas[j].x, plantas[j].y).x;
                            int indiceY = cuadricula.cercanoL(plantas[j].x, plantas[j].y).y;
                            cuadricula.ocupado[indiceX][indiceY] = false;
                            plantas[j] = null;
                        }
                        break; // El zombie solo ataca una planta a la vez
                    }
                }
            }
        }
    }
    
    // NUEVO: Dibujar barras de recarga para plantas del banner
    private void dibujarBarrasRecarga() {
        int tickActual = entorno.numeroDeTick();
        
        // Barra para PlantaDeHielo (30 segundos)
        if (hieloBanner instanceof PlantaDeHielo) {
            PlantaDeHielo hielo = (PlantaDeHielo) hieloBanner;
            double porcentaje = hielo.porcentajeRecarga(tickActual);
            dibujarBarraRecargaConTiempo(150, 70, porcentaje, Color.CYAN, "Hielo", hielo, tickActual);
        }
        
        // Barra para RoseBlade (15 segundos)
        if (roseBanner instanceof RoseBlade) {
            RoseBlade rose = (RoseBlade) roseBanner;
            double porcentaje = rose.porcentajeRecarga(tickActual);
            dibujarBarraRecargaConTiempo(250, 70, porcentaje, Color.ORANGE, "Fuego", rose, tickActual);
        }
        
        // WallNut siempre disponible (sin recarga)
        Color marron = new Color(139, 69, 19); // Color marrón
        dibujarBarraRecarga(50, 70, 1.0, marron, "WallNut");
    }
    
    // NUEVO: Método para dibujar una barra de recarga con tiempo
    private void dibujarBarraRecargaConTiempo(double x, double y, double porcentaje, Color color, String texto, planta planta, int tickActual) {
        double anchoBarra = 80;
        double altoBarra = 8;
        
        // Fondo de la barra
        entorno.dibujarRectangulo(x, y, anchoBarra, altoBarra, 0, Color.GRAY);
        
        // Barra de progreso
        double anchoProgreso = anchoBarra * porcentaje;
        entorno.dibujarRectangulo(x - (anchoBarra - anchoProgreso) / 2, y, anchoProgreso, altoBarra, 0, color);
        
        // Texto del nombre
        entorno.cambiarFont("Arial", 12, Color.WHITE);
        entorno.escribirTexto(texto, x - 25, y - 5);
        
        // Mostrar tiempo restante si está en recarga
        if (porcentaje < 1.0) {
            int segundosRestantes = calcularSegundosRestantes(planta, tickActual);
            entorno.cambiarFont("Arial", 10, Color.WHITE);
            entorno.escribirTexto(segundosRestantes + "s", x - 8, y + 15);
        }
    }
    
    // NUEVO: Método para calcular segundos restantes
    private int calcularSegundosRestantes(planta planta, int tickActual) {
        if (planta instanceof PlantaDeHielo) {
            PlantaDeHielo hielo = (PlantaDeHielo) planta;
            int tiempoTranscurrido = tickActual - hielo.tiempoUltimoDisparo;
            int tiempoRestante = Math.max(0, hielo.tiempoRecarga - tiempoTranscurrido);
            return (int) Math.ceil(tiempoRestante / 6.0); // Convertir ticks a segundos (aprox 6 ticks por segundo)
        } else if (planta instanceof RoseBlade) {
            RoseBlade rose = (RoseBlade) planta;
            int tiempoTranscurrido = tickActual - rose.tiempoUltimoDisparo;
            int tiempoRestante = Math.max(0, rose.tiempoRecarga - tiempoTranscurrido);
            return (int) Math.ceil(tiempoRestante / 6.0); // Convertir ticks a segundos (aprox 6 ticks por segundo)
        }
        return 0;
    }
    
    // Método original para dibujar barra simple (para WallNut)
    private void dibujarBarraRecarga(double x, double y, double porcentaje, Color color, String texto) {
        double anchoBarra = 80;
        double altoBarra = 8;
        
        // Fondo de la barra
        entorno.dibujarRectangulo(x, y, anchoBarra, altoBarra, 0, Color.GRAY);
        
        // Barra de progreso
        double anchoProgreso = anchoBarra * porcentaje;
        entorno.dibujarRectangulo(x - (anchoBarra - anchoProgreso) / 2, y, anchoProgreso, altoBarra, 0, color);
        
        // Texto
        entorno.cambiarFont("Arial", 12, Color.WHITE);
        entorno.escribirTexto(texto, x - 25, y - 5);
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
        if (Math.random() < 0.01 && zombiesEliminados < zombiesTotales) {
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
            
            // Verificar si se hizo click en alguna planta del banner
            if (wallnutBanner != null && wallnutBanner.encima(entorno.mouseX(), entorno.mouseY())) {
                wallnutBanner.seleccionada = true;
            } else if (hieloBanner != null && hieloBanner.encima(entorno.mouseX(), entorno.mouseY())) {
                PlantaDeHielo hielo = (PlantaDeHielo) hieloBanner;
                if (!hielo.estaEnRecarga(entorno.numeroDeTick())) {
                    hieloBanner.seleccionada = true;
                }
            } else if (roseBanner != null && roseBanner.encima(entorno.mouseX(), entorno.mouseY())) {
                RoseBlade rose = (RoseBlade) roseBanner;
                if (!rose.estaEnRecarga(entorno.numeroDeTick())) {
                    roseBanner.seleccionada = true;
                }
            }
        }
        
        if (entorno.estaPresionado(entorno.BOTON_IZQUIERDO)) {
            // Mover la planta seleccionada del banner
            if (wallnutBanner != null && wallnutBanner.seleccionada) {
                wallnutBanner.arrastrar(entorno.mouseX(), entorno.mouseY());
            } else if (hieloBanner != null && hieloBanner.seleccionada) {
                hieloBanner.arrastrar(entorno.mouseX(), entorno.mouseY());
            } else if (roseBanner != null && roseBanner.seleccionada) {
                roseBanner.arrastrar(entorno.mouseX(), entorno.mouseY());
            }
        }
        
        if (entorno.seLevantoBoton(entorno.BOTON_IZQUIERDO)) {
            // Procesar plantado para WallNut
            if (wallnutBanner != null && wallnutBanner.seleccionada) {
                if (entorno.mouseY() < 70) {
                    // Si se suelta en el banner, regresar a su posición original
                    wallnutBanner.x = wallnutBanner.xInicial;
                    wallnutBanner.y = wallnutBanner.yInicial;
                    wallnutBanner.plantada = false;
                } else {
                    // Intentar plantar en la cuadrícula
                    int indiceX = cuadricula.cercanoL(entorno.mouseX(), entorno.mouseY()).x;
                    int indiceY = cuadricula.cercanoL(entorno.mouseX(), entorno.mouseY()).y;
                    
                    if (!cuadricula.ocupado[indiceX][indiceY]) {
                        cuadricula.centrarPlanta(wallnutBanner, indiceX, indiceY);
                        cuadricula.ocupado[indiceX][indiceY] = true;
                        wallnutBanner.plantada = true;
                        
                        // Crear nueva WallNut en el banner
                        crearWallNutEnBanner();
                    } else {
                        // Si la casilla está ocupada, regresar al banner
                        wallnutBanner.x = wallnutBanner.xInicial;
                        wallnutBanner.y = wallnutBanner.yInicial;
                        wallnutBanner.plantada = false;
                    }
                }
                wallnutBanner.seleccionada = false;
            }
            
            // Procesar plantado para PlantaDeHielo
            else if (hieloBanner != null && hieloBanner.seleccionada) {
                if (entorno.mouseY() < 70) {
                    hieloBanner.x = hieloBanner.xInicial;
                    hieloBanner.y = hieloBanner.yInicial;
                    hieloBanner.plantada = false;
                } else {
                    int indiceX = cuadricula.cercanoL(entorno.mouseX(), entorno.mouseY()).x;
                    int indiceY = cuadricula.cercanoL(entorno.mouseX(), entorno.mouseY()).y;
                    
                    if (!cuadricula.ocupado[indiceX][indiceY]) {
                        cuadricula.centrarPlanta(hieloBanner, indiceX, indiceY);
                        cuadricula.ocupado[indiceX][indiceY] = true;
                        hieloBanner.plantada = true;
                        
                        // Crear nueva PlantaDeHielo en el banner
                        crearPlantaDeHieloEnBanner();
                    } else {
                        hieloBanner.x = hieloBanner.xInicial;
                        hieloBanner.y = hieloBanner.yInicial;
                        hieloBanner.plantada = false;
                    }
                }
                hieloBanner.seleccionada = false;
            }
            
            // Procesar plantado para RoseBlade
            else if (roseBanner != null && roseBanner.seleccionada) {
                if (entorno.mouseY() < 70) {
                    roseBanner.x = roseBanner.xInicial;
                    roseBanner.y = roseBanner.yInicial;
                    roseBanner.plantada = false;
                } else {
                    int indiceX = cuadricula.cercanoL(entorno.mouseX(), entorno.mouseY()).x;
                    int indiceY = cuadricula.cercanoL(entorno.mouseX(), entorno.mouseY()).y;
                    
                    if (!cuadricula.ocupado[indiceX][indiceY]) {
                        cuadricula.centrarPlanta(roseBanner, indiceX, indiceY);
                        cuadricula.ocupado[indiceX][indiceY] = true;
                        roseBanner.plantada = true;
                        
                        // Crear nueva RoseBlade en el banner
                        crearRoseBladeEnBanner();
                    } else {
                        roseBanner.x = roseBanner.xInicial;
                        roseBanner.y = roseBanner.yInicial;
                        roseBanner.plantada = false;
                    }
                }
                roseBanner.seleccionada = false;
            }
        }
    }
    
    private void dibujarUI() {
        entorno.cambiarFont("Arial", 20, Color.WHITE);
        entorno.escribirTexto("Zombies: " + zombiesEliminados + "/" + zombiesTotales, 800, 30);
        entorno.escribirTexto("Tiempo: " + entorno.tiempo()/1000 + "s", 800, 60);
        
        // Mostrar plantas activas
        int plantasActivas = 0;
        for (planta p : plantas) {
            if (p != null && p.plantada) {
                plantasActivas++;
            }
        }
        entorno.escribirTexto("Plantas: " + plantasActivas, 800, 90);
        
        // Mostrar zombies en pantalla
        int zombiesEnPantalla = 0;
        for (Zombie z : zombies) {
            if (z != null && z.vivo) {
                zombiesEnPantalla++;
            }
        }
        entorno.escribirTexto("En pantalla: " + zombiesEnPantalla, 800, 120);
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
            entorno.cambiarFont("Arial", 24, Color.WHITE);
            entorno.escribirTexto("Zombies eliminados: " + zombiesEliminados, 400, 350);
            entorno.escribirTexto("Tiempo: " + entorno.tiempo()/1000 + " segundos", 400, 380);
        } else if (juegoPerdido) {
            entorno.cambiarFont("Arial", 40, Color.RED);
            entorno.escribirTexto("¡DERROTA!", 400, 300);
            entorno.cambiarFont("Arial", 24, Color.WHITE);
            entorno.escribirTexto("Zombies eliminados: " + zombiesEliminados, 400, 350);
        }
    }

    public static void main(String[] args) {
        new Juego();
    }
}