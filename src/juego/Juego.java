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
    ZombieColosal zombieColosal;
    BolaFuego[] disparos;
    BolaEscarcha[] disparosHielo;
    int contadorPlantas;
    int zombiesEliminados;
    int zombiesTotales;
    boolean juegoGanado;
    boolean juegoPerdido;
    int ticksParaProximoColosal;
    int colosalesAparecidos;
    
    // Plantas en el banner para controlar recarga
    planta wallnutBanner;
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
        this.zombieColosal = null;
        this.disparos = new BolaFuego[50];
        this.disparosHielo = new BolaEscarcha[50];
        this.zombiesEliminados = 0;
        this.zombiesTotales = 50;
        this.juegoGanado = false;
        this.juegoPerdido = false;
        this.ticksParaProximoColosal = 1080;
        this.colosalesAparecidos = 0;
        
        // Crear plantas del banner
        crearPlantasBanner();
        
        this.entorno.iniciar();
    }
    
    // Método para crear plantas del banner
    private void crearPlantasBanner() {
        this.wallnutBanner = new WallNut(50, 40, entorno);
        this.hieloBanner = new PlantaDeHielo(150, 40, entorno);
        this.roseBanner = new RoseBlade(250, 40, entorno);
        
        // Agregar plantas al array
        plantas[contadorPlantas++] = wallnutBanner;
        plantas[contadorPlantas++] = hieloBanner;
        plantas[contadorPlantas++] = roseBanner;
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
        
        // ACTUALIZAR Y DIBUJAR PLANTAS (solo las plantadas en la cuadrícula)
        for(planta p: this.plantas) {
            if(p != null && p.plantada) {  // SOLO dibujar plantas plantadas
                p.dibujar();
                
                if (p instanceof RoseBlade) {
                    RoseBlade rose = (RoseBlade) p;
                    rose.actualizar(entorno.numeroDeTick());
                    // Usar tiempoRecargaDisparo internamente, no afecta al banner
                    BolaFuego nuevoDisparo = rose.disparar(entorno.numeroDeTick());
                    if (nuevoDisparo != null) {
                        agregarDisparo(nuevoDisparo);
                    }
                }
                // Disparos de PlantaDeHielo (con BolaEscarcha real)
                else if (p instanceof PlantaDeHielo) {
                    PlantaDeHielo plantaHielo = (PlantaDeHielo) p;
                    plantaHielo.actualizar(entorno.numeroDeTick());
                    // Usar tiempoRecargaDisparo internamente, no afecta al banner
                    BolaEscarcha nuevoDisparoHielo = plantaHielo.disparar(entorno.numeroDeTick());
                    if (nuevoDisparoHielo != null) {
                        agregarDisparoHielo(nuevoDisparoHielo);
                    }
                }
                // WallNut no dispara, solo sirve de barrera
            }
        }
        
        // DIBUJAR PLANTAS DEL BANNER SOLO SI NO ESTÁN EN RECARGA
        dibujarPlantasBanner();
        
        generarZombie();
        generarZombieColosal();
        actualizarZombies();
        actualizarZombieColosal();
        actualizarDisparos();
        actualizarDisparosHielo();
        verificarColisiones();
        verificarColisionesHielo();
        verificarColisionesConColosal();
        verificarAtaquesZombies();
        verificarAtaquesZombieColosal();
        manejarSeleccionYPlantado();
        manejarMovimientoTeclado();
        verificarFinJuego();
        dibujarUI();
        dibujarBarrasRecarga();
    }
    
    // MÉTODO: Generar zombie colosal cada 3 minutos
    private void generarZombieColosal() {
        // Si no hay colosal en pantalla y es tiempo de generar uno nuevo
        if (zombieColosal == null && ticksParaProximoColosal > 0) {
            ticksParaProximoColosal--;
            
            if (ticksParaProximoColosal <= 0) {
                zombieColosal = new ZombieColosal(entorno);
                colosalesAparecidos++;
                System.out.println("¡CUIDADO! ¡Zombie Colosal #" + colosalesAparecidos + " aparece!");
            }
        }
        
        // Si el colosal fue eliminado, reiniciar el contador para el próximo
        if (zombieColosal == null && ticksParaProximoColosal <= 0) {
            ticksParaProximoColosal = 1080; // Reiniciar a 3 minutos
        }
    }
    
    // MÉTODO: Actualizar zombie colosal
    private void actualizarZombieColosal() {
        if (zombieColosal != null) {
            zombieColosal.mover();
            zombieColosal.dibujar();
            
            if (zombieColosal.llegoARegalos()) {
                juegoPerdido = true;
                System.out.println("¡JUEGO PERDIDO! El Zombie Colosal llegó a los regalos!");
            }
            
            if (!zombieColosal.vivo) {
                System.out.println("¡Zombie Colosal #" + colosalesAparecidos + " eliminado!");
                zombieColosal = null;
                zombiesEliminados += 5; // Vale por 5 zombies normales
            }
        }
    }
    
    // MÉTODO: Verificar colisiones con el zombie colosal
    private void verificarColisionesConColosal() {
        if (zombieColosal == null || !zombieColosal.vivo) return;
        
        // Colisiones con bolas de fuego
        for (int i = 0; i < disparos.length; i++) {
            if (disparos[i] != null && disparos[i].activa) {
                double distancia = Math.sqrt(Math.pow(disparos[i].x - zombieColosal.getX(), 2) + 
                                           Math.pow(disparos[i].y - zombieColosal.getY(), 2));
                if (distancia < 70) { // Radio mayor por ser más grande
                    zombieColosal.recibirDanio();
                    disparos[i].activa = false;
                    disparos[i] = null;
                    break;
                }
            }
        }
        
        // Colisiones con bolas de hielo
        for (int i = 0; i < disparosHielo.length; i++) {
            if (disparosHielo[i] != null && disparosHielo[i].activa) {
                double distancia = Math.sqrt(Math.pow(disparosHielo[i].x - zombieColosal.getX(), 2) + 
                                           Math.pow(disparosHielo[i].y - zombieColosal.getY(), 2));
                if (distancia < 70) {
                    zombieColosal.ralentizar(disparosHielo[i].duracionRalentizacion);
                    disparosHielo[i].activa = false;
                    disparosHielo[i] = null;
                    break;
                }
            }
        }
    }
    
    // MÉTODO: Verificar ataques del zombie colosal
    private void verificarAtaquesZombieColosal() {
        if (zombieColosal == null || !zombieColosal.vivo) return;
        
        int tickActual = entorno.numeroDeTick();
        
        for (int j = 0; j < plantas.length; j++) {
            if (plantas[j] != null && plantas[j].plantada && 
                zombieColosal.colisionaConPlanta(plantas[j])) {
                
                if (zombieColosal.puedeAtacar(tickActual)) {
                    // El zombie colosal ataca la planta
                    if (plantas[j] instanceof WallNut) {
                        WallNut wallnut = (WallNut) plantas[j];
                        wallnut.recibirAtaque();
                        wallnut.recibirAtaque(); // Doble daño del colosal
                        System.out.println("WallNut recibió DOBLE ataque del Colosal!");
                    } else {
                        // Otras plantas mueren instantáneamente contra el colosal
                        System.out.println("Planta destruida por Zombie Colosal!");
                        int indiceX = cuadricula.cercanoL(plantas[j].x, plantas[j].y).x;
                        int indiceY = cuadricula.cercanoL(plantas[j].x, plantas[j].y).y;
                        cuadricula.ocupado[indiceX][indiceY] = false;
                        plantas[j] = null;
                    }
                    zombieColosal.registrarAtaque(tickActual);
                }
                break;
            }
        }
    }
    
    // MÉTODO: Dibujar plantas del banner solo si no están en recarga
    private void dibujarPlantasBanner() {
        int tickActual = entorno.numeroDeTick();
        
        // Dibujar WallNut del banner si no está en recarga
        if (wallnutBanner instanceof WallNut) {
            WallNut wallnut = (WallNut) wallnutBanner;
            if (!wallnut.estaEnRecarga(tickActual)) {
                wallnutBanner.dibujar();
            }
        }
        
        // Dibujar PlantaDeHielo del banner si no está en recarga
        if (hieloBanner instanceof PlantaDeHielo) {
            PlantaDeHielo hielo = (PlantaDeHielo) hieloBanner;
            if (!hielo.estaEnRecarga(tickActual)) {
                hieloBanner.dibujar();
            }
        }
        
        // Dibujar RoseBlade del banner si no está en recarga
        if (roseBanner instanceof RoseBlade) {
            RoseBlade rose = (RoseBlade) roseBanner;
            if (!rose.estaEnRecarga(tickActual)) {
                roseBanner.dibujar();
            }
        }
    }
    
    // Método para manejar movimiento con teclado
    private void manejarMovimientoTeclado() {
        // Buscar la planta seleccionada
        planta plantaSeleccionada = null;
        for (planta p : plantas) {
            if (p != null && p.seleccionada) {
                plantaSeleccionada = p;
                break;
            }
        }
        
        if (plantaSeleccionada == null) {
            return; // No hay planta seleccionada
        }
        
        // Mover con teclas WASD (tanto plantas del banner como plantadas)
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
    
    // MODIFICADO: Verificar cuando los zombies atacan plantas (CON BLOQUEO DE WALLNUT)
    private void verificarAtaquesZombies() {
        int tickActual = entorno.numeroDeTick();
        
        for (int i = 0; i < zombies.length; i++) {
            if (zombies[i] != null && zombies[i].vivo) {
                
                // NUEVO: Primero verificar si el zombie está bloqueado y si su WallNut sigue existiendo
                zombies[i].verificarPlantaBloqueadora();
                
                // Si el zombie ya está bloqueado, solo puede atacar a su WallNut bloqueadora
                if (zombies[i].estaBloqueado()) {
                    planta plantaBloqueadora = zombies[i].plantaBloqueadora;
                    if (plantaBloqueadora != null && plantaBloqueadora.plantada && 
                        zombies[i].puedeAtacar(tickActual)) {
                        
                        if (plantaBloqueadora instanceof WallNut) {
                            WallNut wallnut = (WallNut) plantaBloqueadora;
                            wallnut.recibirAtaque();
                            System.out.println("WallNut bajo ataque! Resistencia: " + wallnut.resistencia + "/90");
                            
                            if (wallnut.resistencia <= 0) {
                                System.out.println("WallNut destruida!");
                                // Liberar la casilla en la cuadrícula
                                int indiceX = cuadricula.cercanoL(plantaBloqueadora.x, plantaBloqueadora.y).x;
                                int indiceY = cuadricula.cercanoL(plantaBloqueadora.x, plantaBloqueadora.y).y;
                                cuadricula.ocupado[indiceX][indiceY] = false;
                                plantaBloqueadora.plantada = false;
                                // El zombie será liberado automáticamente en el próximo tick por verificarPlantaBloqueadora()
                            }
                        }
                        zombies[i].registrarAtaque(tickActual);
                    }
                } 
                // Si no está bloqueado, buscar plantas para atacar/bloquear
                else {
                    boolean encontroPlanta = false;
                    for (int j = 0; j < plantas.length; j++) {
                        if (plantas[j] != null && plantas[j].plantada && 
                            zombies[i].colisionaConPlanta(plantas[j])) {
                            
                            encontroPlanta = true;
                            
                            // NUEVO: Comportamiento especial para WallNuts - BLOQUEAR
                            if (plantas[j] instanceof WallNut) {
                                zombies[i].bloquear(plantas[j]);
                            } 
                            // Para otras plantas, atacar normalmente
                            else if (zombies[i].puedeAtacar(tickActual)) {
                                System.out.println("Planta destruida por zombie!");
                                int indiceX = cuadricula.cercanoL(plantas[j].x, plantas[j].y).x;
                                int indiceY = cuadricula.cercanoL(plantas[j].x, plantas[j].y).y;
                                cuadricula.ocupado[indiceX][indiceY] = false;
                                plantas[j] = null;
                                zombies[i].registrarAtaque(tickActual);
                            }
                            break; // El zombie solo interactúa con una planta a la vez
                        }
                    }
                    
                    // NUEVO: Si no encontró ninguna planta pero detecta una WallNut específicamente, bloquearse
                    if (!encontroPlanta) {
                        for (int j = 0; j < plantas.length; j++) {
                            if (plantas[j] != null && plantas[j].plantada && 
                                plantas[j] instanceof WallNut && 
                                zombies[i].colisionaConWallNut(plantas[j])) {
                                
                                zombies[i].bloquear(plantas[j]);
                                break;
                            }
                        }
                    }
                }
            }
        }
    }
    
    // Dibujar barras de recarga para plantas del banner (SOLO TIEMPO DE PLANTADO)
    private void dibujarBarrasRecarga() {
        int tickActual = entorno.numeroDeTick();
        
        // Barra para WallNut (TIEMPO DE PLANTADO)
        if (wallnutBanner instanceof WallNut) {
            WallNut wallnut = (WallNut) wallnutBanner;
            double porcentaje = wallnut.porcentajeRecarga(tickActual);
            Color marron = new Color(139, 69, 19);
            dibujarBarraRecargaConTiempo(50, 70, porcentaje, marron, "WallNut", wallnut, tickActual);
        }
        
        // Barra para PlantaDeHielo (TIEMPO DE PLANTADO)
        if (hieloBanner instanceof PlantaDeHielo) {
            PlantaDeHielo hielo = (PlantaDeHielo) hieloBanner;
            double porcentaje = hielo.porcentajeRecarga(tickActual);
            dibujarBarraRecargaConTiempo(150, 70, porcentaje, Color.CYAN, "Hielo", hielo, tickActual);
        }
        
        // Barra para RoseBlade (TIEMPO DE PLANTADO)
        if (roseBanner instanceof RoseBlade) {
            RoseBlade rose = (RoseBlade) roseBanner;
            double porcentaje = rose.porcentajeRecarga(tickActual);
            dibujarBarraRecargaConTiempo(250, 70, porcentaje, Color.ORANGE, "Fuego", rose, tickActual);
        }
    }
    
    // Método para dibujar una barra de recarga con tiempo (SOLO PLANTADO)
    private void dibujarBarraRecargaConTiempo(double x, double y, double porcentaje, Color color, String texto, planta planta, int tickActual) {
        double anchoBarra = 80;
        double altoBarra = 8;
        
        // Fondo de la barra
        entorno.dibujarRectangulo(x, y, anchoBarra, altoBarra, 0, Color.DARK_GRAY);
        
        // Barra de progreso que se llena gradualmente
        double anchoProgreso = anchoBarra * porcentaje;
        if (anchoProgreso > 0) {
            entorno.dibujarRectangulo(x - (anchoBarra - anchoProgreso) / 2, y, anchoProgreso, altoBarra, 0, color);
        }
        
        // Borde de la barra
        entorno.dibujarRectangulo(x, y, anchoBarra, altoBarra, 0, Color.BLACK);
        
        // Texto del nombre
        entorno.cambiarFont("Arial", 12, Color.WHITE);
        entorno.escribirTexto(texto, x - 25, y - 5);
        
        // Mostrar tiempo restante si está en recarga
        if (porcentaje < 1.0) {
            int segundosRestantes = calcularSegundosRestantes(planta, tickActual);
            entorno.cambiarFont("Arial", 10, Color.WHITE);
            
            // Cambiar color del texto según el tiempo restante
            if (segundosRestantes <= 5) {
                entorno.cambiarFont("Arial", 10, Color.YELLOW);
            } else if (segundosRestantes <= 10) {
                entorno.cambiarFont("Arial", 10, Color.ORANGE);
            }
            
            entorno.escribirTexto(segundosRestantes + "s", x - 8, y + 15);
            
            // Efecto visual adicional: parpadeo cuando está por terminar
            if (segundosRestantes <= 3 && tickActual % 10 < 5) {
                entorno.dibujarRectangulo(x, y, anchoBarra, altoBarra, 0, new Color(255, 255, 255, 100));
            }
        } else {
            // Planta disponible
            entorno.cambiarFont("Arial", 10, Color.GREEN);
            entorno.escribirTexto("Listo", x - 12, y + 15);
        }
    }
    
    // Método para calcular segundos restantes (SOLO PLANTADO)
    private int calcularSegundosRestantes(planta planta, int tickActual) {
        if (planta instanceof PlantaDeHielo) {
            PlantaDeHielo hielo = (PlantaDeHielo) planta;
            int tiempoTranscurrido = tickActual - hielo.tiempoUltimoPlantado;
            int tiempoRestante = Math.max(0, hielo.tiempoRecargaPlantado - tiempoTranscurrido);
            return (int) Math.ceil(tiempoRestante / 6.0);
        } else if (planta instanceof RoseBlade) {
            RoseBlade rose = (RoseBlade) planta;
            int tiempoTranscurrido = tickActual - rose.tiempoUltimoPlantado;
            int tiempoRestante = Math.max(0, rose.tiempoRecargaPlantado - tiempoTranscurrido);
            return (int) Math.ceil(tiempoRestante / 6.0);
        } else if (planta instanceof WallNut) {
            WallNut wallnut = (WallNut) planta;
            int tiempoTranscurrido = tickActual - wallnut.tiempoUltimoUso;
            int tiempoRestante = Math.max(0, wallnut.tiempoRecargaPlantado - tiempoTranscurrido);
            return (int) Math.ceil(tiempoRestante / 6.0);
        }
        return 0;
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
            // Deseleccionar todas primero
            for (int i = 0; i < plantas.length; i++) {
                if (plantas[i] != null) {
                    plantas[i].seleccionada = false;
                }
            }
            
            int tickActual = entorno.numeroDeTick();
            
            // Verificar si se hizo click en alguna planta del banner (SOLO SI NO ESTÁ EN RECARGA DE PLANTADO)
            if (wallnutBanner != null && wallnutBanner.encima(entorno.mouseX(), entorno.mouseY())) {
                WallNut wallnut = (WallNut) wallnutBanner;
                if (!wallnut.estaEnRecarga(tickActual)) {
                    wallnutBanner.seleccionada = true;
                }
            } 
            else if (hieloBanner != null && hieloBanner.encima(entorno.mouseX(), entorno.mouseY())) {
                PlantaDeHielo hielo = (PlantaDeHielo) hieloBanner;
                if (!hielo.estaEnRecarga(tickActual)) {
                    hieloBanner.seleccionada = true;
                }
            } 
            else if (roseBanner != null && roseBanner.encima(entorno.mouseX(), entorno.mouseY())) {
                RoseBlade rose = (RoseBlade) roseBanner;
                if (!rose.estaEnRecarga(tickActual)) {
                    roseBanner.seleccionada = true;
                }
            }
            // Verificar si se hizo click en una planta ya plantada para moverla
            else {
                for (int i = 0; i < plantas.length; i++) {
                    if (plantas[i] != null && plantas[i].plantada && plantas[i].encima(entorno.mouseX(), entorno.mouseY())) {
                        plantas[i].seleccionada = true;
                        break;
                    }
                }
            }
        }
        
        if (entorno.estaPresionado(entorno.BOTON_IZQUIERDO)) {
            // Mover plantas seleccionadas (tanto del banner como plantadas)
            for (int i = 0; i < plantas.length; i++) {
                if (plantas[i] != null && plantas[i].seleccionada) {
                    plantas[i].arrastrar(entorno.mouseX(), entorno.mouseY());
                }
            }
        }
        
        if (entorno.seLevantoBoton(entorno.BOTON_IZQUIERDO)) {
            // Procesar plantado para WallNut del banner
            if (wallnutBanner != null && wallnutBanner.seleccionada) {
                WallNut wallnut = (WallNut) wallnutBanner;
                
                if (entorno.mouseY() < 70) {
                    // Si se suelta en el banner, regresar a su posición original
                    wallnutBanner.x = wallnutBanner.xInicial;
                    wallnutBanner.y = wallnutBanner.yInicial;
                } else {
                    // Intentar plantar en la cuadrícula
                    int indiceX = cuadricula.cercanoL(entorno.mouseX(), entorno.mouseY()).x;
                    int indiceY = cuadricula.cercanoL(entorno.mouseX(), entorno.mouseY()).y;
                    
                    // Verificar que no sea la primera columna (columna 0)
                    if (indiceX > 0 && !cuadricula.ocupado[indiceX][indiceY]) {
                        // Crear NUEVA planta para la cuadrícula
                        WallNut nuevaWallnut = new WallNut(cuadricula.coorX[indiceX], cuadricula.coorY[indiceY], entorno);
                        nuevaWallnut.plantada = true;
                        
                        // Buscar espacio en el array de plantas
                        for (int i = 0; i < plantas.length; i++) {
                            if (plantas[i] == null) {
                                plantas[i] = nuevaWallnut;
                                break;
                            }
                        }
                        
                        cuadricula.ocupado[indiceX][indiceY] = true;
                        
                        // Marcar como usada en el banner
                        wallnut.usar(entorno.numeroDeTick());
                    }
                    // Regresar la planta del banner a su posición original
                    wallnutBanner.x = wallnutBanner.xInicial;
                    wallnutBanner.y = wallnutBanner.yInicial;
                }
                wallnutBanner.seleccionada = false;
            }
            
            // Procesar plantado para PlantaDeHielo del banner
            else if (hieloBanner != null && hieloBanner.seleccionada) {
                PlantaDeHielo hielo = (PlantaDeHielo) hieloBanner;
                
                if (entorno.mouseY() < 70) {
                    hieloBanner.x = hieloBanner.xInicial;
                    hieloBanner.y = hieloBanner.yInicial;
                } else {
                    int indiceX = cuadricula.cercanoL(entorno.mouseX(), entorno.mouseY()).x;
                    int indiceY = cuadricula.cercanoL(entorno.mouseX(), entorno.mouseY()).y;
                    
                    // Verificar que no sea la primera columna (columna 0)
                    if (indiceX > 0 && !cuadricula.ocupado[indiceX][indiceY]) {
                        // Crear NUEVA planta para la cuadrícula
                        PlantaDeHielo nuevaHielo = new PlantaDeHielo(cuadricula.coorX[indiceX], cuadricula.coorY[indiceY], entorno);
                        nuevaHielo.plantada = true;
                        
                        // Buscar espacio en el array de plantas
                        for (int i = 0; i < plantas.length; i++) {
                            if (plantas[i] == null) {
                                plantas[i] = nuevaHielo;
                                break;
                            }
                        }
                        
                        cuadricula.ocupado[indiceX][indiceY] = true;
                        
                        // Marcar como usada en el banner
                        hielo.usar(entorno.numeroDeTick());
                    }
                    // Regresar la planta del banner a su posición original
                    hieloBanner.x = hieloBanner.xInicial;
                    hieloBanner.y = hieloBanner.yInicial;
                }
                hieloBanner.seleccionada = false;
            }
            
            // Procesar plantado para RoseBlade del banner
            else if (roseBanner != null && roseBanner.seleccionada) {
                RoseBlade rose = (RoseBlade) roseBanner;
                
                if (entorno.mouseY() < 70) {
                    roseBanner.x = roseBanner.xInicial;
                    roseBanner.y = roseBanner.yInicial;
                } else {
                    int indiceX = cuadricula.cercanoL(entorno.mouseX(), entorno.mouseY()).x;
                    int indiceY = cuadricula.cercanoL(entorno.mouseX(), entorno.mouseY()).y;
                    
                    // Verificar que no sea la primera columna (columna 0)
                    if (indiceX > 0 && !cuadricula.ocupado[indiceX][indiceY]) {
                        // Crear NUEVA planta para la cuadrícula
                        RoseBlade nuevaRose = new RoseBlade(cuadricula.coorX[indiceX], cuadricula.coorY[indiceY], entorno);
                        nuevaRose.plantada = true;
                        
                        // Buscar espacio en el array de plantas
                        for (int i = 0; i < plantas.length; i++) {
                            if (plantas[i] == null) {
                                plantas[i] = nuevaRose;
                                break;
                            }
                        }
                        
                        cuadricula.ocupado[indiceX][indiceY] = true;
                        
                        // Marcar como usada en el banner
                        rose.usar(entorno.numeroDeTick());
                    }
                    // Regresar la planta del banner a su posición original
                    roseBanner.x = roseBanner.xInicial;
                    roseBanner.y = roseBanner.yInicial;
                }
                roseBanner.seleccionada = false;
            }
            
            // Procesar movimiento de plantas ya plantadas
            for (int i = 0; i < plantas.length; i++) {
                if (plantas[i] != null && plantas[i].plantada && plantas[i].seleccionada) {
                    // Cuando se suelta una planta plantada, centrarla en la cuadrícula más cercana
                    int indiceX = cuadricula.cercanoL(plantas[i].x, plantas[i].y).x;
                    int indiceY = cuadricula.cercanoL(plantas[i].x, plantas[i].y).y;
                    
                    // Liberar la casilla anterior
                    int indiceXAnterior = cuadricula.cercanoL(plantas[i].xInicial, plantas[i].yInicial).x;
                    int indiceYAnterior = cuadricula.cercanoL(plantas[i].xInicial, plantas[i].yInicial).y;
                    cuadricula.ocupado[indiceXAnterior][indiceYAnterior] = false;
                    
                    // Ocupar la nueva casilla si está libre y no es la primera columna
                    if (indiceX > 0 && !cuadricula.ocupado[indiceX][indiceY]) {
                        cuadricula.centrarPlanta(plantas[i], indiceX, indiceY);
                        cuadricula.ocupado[indiceX][indiceY] = true;
                        plantas[i].xInicial = plantas[i].x;
                        plantas[i].yInicial = plantas[i].y;
                    } else {
                        // Si la nueva casilla está ocupada o es la primera columna, regresar a la posición original
                        plantas[i].x = plantas[i].xInicial;
                        plantas[i].y = plantas[i].yInicial;
                        cuadricula.ocupado[indiceXAnterior][indiceYAnterior] = true;
                    }
                    
                    plantas[i].seleccionada = false;
                }
            }
        }
    }
    
    private void dibujarUI() {
        entorno.cambiarFont("Arial", 20, Color.WHITE);
        entorno.escribirTexto("Zombies: " + zombiesEliminados + "/" + zombiesTotales, 400, 40);
        entorno.escribirTexto("Tiempo: " + entorno.tiempo()/1000 + "s", 400, 80);
        
        // Mostrar plantas activas
        int plantasActivas = 0;
        for (planta p : plantas) {
            if (p != null && p.plantada) {
                plantasActivas++;
            }
        }
        entorno.escribirTexto("Plantas: " + plantasActivas, 400, 20);
        
        // Mostrar zombies en pantalla
        int zombiesEnPantalla = 0;
        for (Zombie z : zombies) {
            if (z != null && z.vivo) {
                zombiesEnPantalla++;
            }
        }
        entorno.escribirTexto("Zombies en pantalla: " + zombiesEnPantalla, 400, 60);
        
        // NUEVO: Mostrar información del colosal
        if (zombieColosal != null && zombieColosal.vivo) {
            entorno.cambiarFont("Arial", 50, Color.RED);
            entorno.escribirTexto("¡ZOMBIE COLOSAL!", 212, 300);
        } else if (ticksParaProximoColosal > 0) {
            int segundosRestantes = ticksParaProximoColosal / 6;
            entorno.cambiarFont("Arial", 20, Color.red);
            entorno.escribirTexto("Próximo colosal en: " + segundosRestantes + "s", 600, 84);
        } else {
            entorno.cambiarFont("Arial", 20, Color.orange);
            entorno.escribirTexto("Colosales: " + colosalesAparecidos, 600, 84);
        }
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
            entorno.escribirTexto("Colosales derrotados: " + colosalesAparecidos, 400, 410);
        } else if (juegoPerdido) {
            entorno.cambiarFont("Arial", 40, Color.RED);
            entorno.escribirTexto("¡DERROTA!", 400, 300);
            entorno.cambiarFont("Arial", 24, Color.WHITE);
            entorno.escribirTexto("Zombies eliminados: " + zombiesEliminados, 400, 350);
            entorno.escribirTexto("Colosales derrotados: " + colosalesAparecidos, 400, 380);
        }
    }

    public static void main(String[] args) {
        new Juego();
    }
}