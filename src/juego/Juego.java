package juego;

import java.awt.Color;
import entorno.Entorno;
import entorno.InterfaceJuego;

public class Juego extends InterfaceJuego {

   private Entorno entorno;
   private Banner banner;
   private cuadricula cuadricula;
   private regalos[] regalo;
   
   // Arrays de cada tipo de planta
   private WallNut[] plantasWallnut;
   private PlantaDeHielo[] plantasHielo;
   private RoseBlade[] plantasRose;
   private CerezaExplosiva[] plantasCereza;
   
   private ZombieGrinch[] zombiesGrinch;
   private ZombieRapido[] zombiesRapidos;
   private ZombieColosal[] zombiesColosales;
   private BolaFuego[] disparos;
   private BolaEscarcha[] disparosHielo;
   private BolaNieve[] bolasNieve;
   public int contadorPlantas;
   private int zombiesEliminados;
   private int zombiesTotales;
   private boolean juegoGanado;
   private boolean juegoPerdido;
   private int ticksParaProximoRapido;
   private int ticksParaProximoColosal;
   private int rapidosAparecidos;
   private int colosalesAparecidos;
   
   // Plantas en el banner - tipos específicos
   private WallNut wallnutBanner;
   private PlantaDeHielo hieloBanner;
   private RoseBlade roseBanner;
   private CerezaExplosiva cerezaBanner;
   
   // Zombies en el banner
   private ZombieGrinch zombieGrinchBanner;
   private ZombieRapido zombieRapidoBanner;
   private ZombieColosal zombieColosalBanner;
   
   // Contadores para optimización
   public int cantidadWallnutActivas;
   public int cantidadHieloActivas;
   public int cantidadRoseActivas;
   public int cantidadCerezaActivas;
   public int cantidadDisparosActivos;
   public int cantidadDisparosHieloActivos;
   public int cantidadBolasNieveActivas;
   public int cantidadZombiesGrinchActivos;
   public int cantidadZombiesRapidosActivos;
   public int cantidadZombiesColosalesActivos;
   
   // CONSTRUCTOR PRINCIPAL
   // Inicializa todos los componentes del juego: entorno, cuadrícula, plantas, zombies, etc.
   public Juego(){
       this.entorno = new Entorno(this, "Zombies Grinch", 1034, 585);
       this.cuadricula = new cuadricula(50, 150, entorno);
       this.banner = new Banner(entorno);
      
       // Inicializa los regalos (objetivos que defiende el jugador)
       this.regalo = new regalos[5];
       for (int i = 0; i < regalo.length; i++) {
           double x = 50;
           double y = 140 + (i * 100);
           this.regalo[i] = new regalos(x, y, entorno);
       }
      
       // Inicializar arrays separados para cada tipo de planta
       this.plantasWallnut = new WallNut[10];
       this.plantasHielo = new PlantaDeHielo[10];
       this.plantasRose = new RoseBlade[10];
       this.plantasCereza = new CerezaExplosiva[10];
       
       this.contadorPlantas = 0;
       this.cantidadWallnutActivas = 0;
       this.cantidadHieloActivas = 0;
       this.cantidadRoseActivas = 0;
       this.cantidadCerezaActivas = 0;
       
       // Inicializar arrays de zombies con límites específicos
       this.zombiesGrinch = new ZombieGrinch[5];
       this.zombiesRapidos = new ZombieRapido[2] ;
       this.zombiesColosales = new ZombieColosal[1];
       
       // Inicializar arrays de proyectiles
       this.disparos = new BolaFuego[50];
       this.disparosHielo = new BolaEscarcha[50];
       this.bolasNieve = new BolaNieve[30];
       
       // Variables de estado del juego
       this.zombiesEliminados = 0;
       this.zombiesTotales = 80;
       this.juegoGanado = false;
       this.juegoPerdido = false;
       
       // Temporizadores para aparición de zombies especiales
       this.ticksParaProximoRapido = 270;
       this.ticksParaProximoColosal = 1080;
       this.rapidosAparecidos = 0;
       this.colosalesAparecidos = 0;
      
       // Inicializar contadores de elementos activos
       this.cantidadDisparosActivos = 0;
       this.cantidadDisparosHieloActivos = 0;
       this.cantidadBolasNieveActivas = 0;
       this.cantidadZombiesGrinchActivos = 0;
       this.cantidadZombiesRapidosActivos = 0;
       this.cantidadZombiesColosalesActivos = 0;
      
       crearPlantasBanner();
       crearZombiesBanner();
       this.entorno.iniciar();
   }  
    
   // MÉTODO: Crea las plantas que aparecen en el banner de selección
   // Propósito: Inicializar las plantas que el jugador puede arrastrar al campo de juego
   private void crearPlantasBanner() {
       this.wallnutBanner = new WallNut(50, 40, entorno);
       this.hieloBanner = new PlantaDeHielo(150, 40, entorno);
       this.roseBanner = new RoseBlade(250, 40, entorno);
       this.cerezaBanner = new CerezaExplosiva(350, 40, entorno);
      
       // Agregar las plantas banner a sus respectivos arrays
       agregarWallnut(wallnutBanner);
       agregarPlantaHielo(hieloBanner);
       agregarRoseBlade(roseBanner);
       agregarCerezaExplosiva(cerezaBanner);
   }
   
   // MÉTODO: Crea los zombies que aparecen en el banner informativo
   // Propósito: Mostrar los tipos de zombies al jugador en la interfaz
   private void crearZombiesBanner() {
       this.zombieGrinchBanner = new ZombieGrinch(0, entorno);
       this.zombieRapidoBanner = new ZombieRapido(0, entorno);
       this.zombieColosalBanner = new ZombieColosal(entorno);
       
       // Posicionar zombies banner en coordenadas específicas
       this.zombieGrinchBanner.x = 800;
       this.zombieGrinchBanner.y = 20;
       this.zombieGrinchBanner.vivo = true;
       
       this.zombieRapidoBanner.x = 900;
       this.zombieRapidoBanner.y = 20;
       this.zombieRapidoBanner.vivo = true;
       
       this.zombieColosalBanner.x = 1000;
       this.zombieColosalBanner.y = 20;
       this.zombieColosalBanner.vivo = true;
   }
   
   
   // MÉTODO: Agrega una nueva WallNut al array correspondiente
   // Propósito: Gestionar la adición de plantas WallNut al juego buscando posición libre
   private boolean agregarWallnut(WallNut nuevaPlanta) {
       for (int i = 0; i < plantasWallnut.length; i++) {
           if (plantasWallnut[i] == null) {
               plantasWallnut[i] = nuevaPlanta;
               cantidadWallnutActivas++;
               return true;
           }
       }
       return false;
   }
   
   // MÉTODO: Agrega una nueva PlantaDeHielo al array correspondiente
   // Propósito: Gestionar la adición de plantas de hielo al juego
   private boolean agregarPlantaHielo(PlantaDeHielo nuevaPlanta) {
       for (int i = 0; i < plantasHielo.length; i++) {
           if (plantasHielo[i] == null) {
               plantasHielo[i] = nuevaPlanta;
               cantidadHieloActivas++;
               return true;
           }
       }
       return false;
   }
   
   // MÉTODO: Agrega una nueva RoseBlade al array correspondiente
   // Propósito: Gestionar la adición de plantas RoseBlade al juego
   private boolean agregarRoseBlade(RoseBlade nuevaPlanta) {
       for (int i = 0; i < plantasRose.length; i++) {
           if (plantasRose[i] == null) {
               plantasRose[i] = nuevaPlanta;
               cantidadRoseActivas++;
               return true;
           }
       }
       return false;
   }
   
   // MÉTODO: Agrega una nueva CerezaExplosiva al array correspondiente
   // Propósito: Gestionar la adición de plantas cereza explosiva al juego
   private boolean agregarCerezaExplosiva(CerezaExplosiva nuevaPlanta) {
       for (int i = 0; i < plantasCereza.length; i++) {
           if (plantasCereza[i] == null) {
               plantasCereza[i] = nuevaPlanta;
               cantidadCerezaActivas++;
               return true;
           }
       }
       return false;
   }
   
   // ========== MÉTODOS PARA ELIMINAR PLANTAS ==========
   
   // MÉTODO: Elimina una WallNut del array por índice
   // Propósito: Remover plantas muertas o destruidas del juego
   private void eliminarWallnut(int indice) {
       if (plantasWallnut[indice] != null) {
           plantasWallnut[indice] = null;
           cantidadWallnutActivas--;
       }
   }
   
   // MÉTODO: Elimina una PlantaDeHielo del array por índice
   // Propósito: Remover plantas de hielo muertas del juego
   private void eliminarPlantaHielo(int indice) {
       if (plantasHielo[indice] != null) {
           plantasHielo[indice] = null;
           cantidadHieloActivas--;
       }
   }
   
   // MÉTODO: Elimina una RoseBlade del array por índice
   // Propósito: Remover plantas RoseBlade muertas del juego
   private void eliminarRoseBlade(int indice) {
       if (plantasRose[indice] != null) {
           plantasRose[indice] = null;
           cantidadRoseActivas--;
       }
   }
   
   // MÉTODO: Elimina una CerezaExplosiva del array por índice
   // Propósito: Remover plantas cereza muertas o explotadas del juego
   private void eliminarCerezaExplosiva(int indice) {
       if (plantasCereza[indice] != null) {
           plantasCereza[indice] = null;
           cantidadCerezaActivas--;
       }
   }
   
   // ========== MÉTODOS PARA AGREGAR ZOMBIES ==========
   
   // MÉTODO: Agrega un nuevo ZombieGrinch al array correspondiente
   // Propósito: Gestionar la aparición de zombies Grinch en el juego
   private boolean agregarZombieGrinch(ZombieGrinch nuevoZombie) {
       for (int i = 0; i < zombiesGrinch.length; i++) {
           if (zombiesGrinch[i] == null) {
               zombiesGrinch[i] = nuevoZombie;
               cantidadZombiesGrinchActivos++;
               return true;
           }
       }
       return false;
   }

   // MÉTODO: Agrega un nuevo ZombieRapido al array correspondiente
   // Propósito: Gestionar la aparición de zombies rápidos en el juego
   private boolean agregarZombieRapido(ZombieRapido nuevoZombie) {
       for (int i = 0; i < zombiesRapidos.length; i++) {
           if (zombiesRapidos[i] == null) {
               zombiesRapidos[i] = nuevoZombie;
               cantidadZombiesRapidosActivos++;
               return true;
           }
       }
       return false;
   }

   // MÉTODO: Agrega un nuevo ZombieColosal al array correspondiente
   // Propósito: Gestionar la aparición de zombies colosales en el juego
   private boolean agregarZombieColosal(ZombieColosal nuevoZombie) {
       for (int i = 0; i < zombiesColosales.length; i++) {
           if (zombiesColosales[i] == null) {
               zombiesColosales[i] = nuevoZombie;
               cantidadZombiesColosalesActivos++;
               return true;
           }
       }
       return false;
   }
   
   // ========== MÉTODOS PARA ELIMINAR ZOMBIES ==========
   
   // MÉTODO: Elimina un ZombieGrinch del array por índice
   // Propósito: Remover zombies Grinch muertos del juego
   private void eliminarZombieGrinch(int indice) {
       if (zombiesGrinch[indice] != null) {
           zombiesGrinch[indice] = null;
           cantidadZombiesGrinchActivos--;
       }
   }

   // MÉTODO: Elimina un ZombieRapido del array por índice
   // Propósito: Remover zombies rápidos muertos del juego
   private void eliminarZombieRapido(int indice) {
       if (zombiesRapidos[indice] != null) {
           zombiesRapidos[indice] = null;
           cantidadZombiesRapidosActivos--;
       }
   }

   // MÉTODO: Elimina un ZombieColosal del array por índice
   // Propósito: Remover zombies colosales muertos del juego
   private void eliminarZombieColosal(int indice) {
       if (zombiesColosales[indice] != null) {
           zombiesColosales[indice] = null;
           cantidadZombiesColosalesActivos--;
       }
   }
   
   // ========== MÉTODO PRINCIPAL DEL JUEGO (GAME LOOP) ==========
   
   // MÉTODO: tick - Método principal que se ejecuta en cada frame del juego
   // Propósito: Actualizar y dibujar todos los elementos del juego en cada ciclo
   public void tick(){
       // Verificar si el juego terminó
       if (juegoGanado || juegoPerdido) {
           dibujarPantallaFin();
           return;
       }
      
       // Dibujar elementos básicos del juego
       entorno.colorFondo(Color.GREEN);
       this.banner.dibujar();
       this.cuadricula.dibujar();
      
       // Dibujar regalos (objetivos)
       for (regalos r: this.regalo) {
           r.dibujar();
       }
      
       // Actualizar y dibujar todos los elementos del juego
       actualizarPlantas();
       dibujarPlantasBanner();
       dibujarZombiesBanner();
      
       // Generar y actualizar zombies
       generarZombieGrinch();
       generarZombieRapido();
       generarZombieColosal();
       actualizarZombies();
       
       // Actualizar proyectiles
       actualizarDisparos();
       actualizarDisparosHielo();
       actualizarBolasNieve();
       
       // Verificar colisiones entre elementos
       verificarColisiones();
       verificarColisionesHielo();
       verificarColisionesBolasNieve();
       
       // Manejar combate y ataques
       verificarAtaquesZombies();
       generarDisparosZombies();
       
       // Manejar interacción del jugador
       manejarSeleccionYPlantado();
       manejarMovimientoTeclado();
       
       // Verificar condiciones de fin de juego
       verificarFinJuego();
       dibujarUI();
       dibujarBarrasRecarga();
   }

   // MÉTODO: Actualiza el estado y dibuja todas las plantas en el campo
   // Propósito: Procesar el comportamiento de cada tipo de planta en cada frame
   private void actualizarPlantas() {
       int tickActual = entorno.numeroDeTick();
       
       // Actualizar WallNuts
       for(int i = 0; i < plantasWallnut.length; i++) {
           if(plantasWallnut[i] != null && plantasWallnut[i].plantada) {
               plantasWallnut[i].dibujar();
               plantasWallnut[i].actualizar(tickActual);
               plantasWallnut[i].ejecutarComportamientoEspecifico(tickActual, this);
           }
       }
       
       // Actualizar Plantas de Hielo
       for(int i = 0; i < plantasHielo.length; i++) {
           if(plantasHielo[i] != null && plantasHielo[i].plantada) {
               plantasHielo[i].dibujar();
               plantasHielo[i].actualizar(tickActual);
               plantasHielo[i].ejecutarComportamientoEspecifico(tickActual, this);
           }
       }
       
       // Actualizar RoseBlades
       for(int i = 0; i < plantasRose.length; i++) {
           if(plantasRose[i] != null && plantasRose[i].plantada) {
               plantasRose[i].dibujar();
               plantasRose[i].actualizar(tickActual);
               plantasRose[i].ejecutarComportamientoEspecifico(tickActual, this);
           }
       }
       
       // Actualizar Cerezas Explosivas
       for(int i = 0; i < plantasCereza.length; i++) {
           if(plantasCereza[i] != null && plantasCereza[i].plantada) {
               plantasCereza[i].dibujar();
               plantasCereza[i].actualizar(tickActual);
               plantasCereza[i].ejecutarComportamientoEspecifico(tickActual, this);
           }
       }
   }

   // MÉTODO: Dibuja los zombies en el banner informativo
   // Propósito: Mostrar los tipos de zombies disponibles en la interfaz de usuario
   private void dibujarZombiesBanner() {
        if (zombieGrinchBanner != null && zombieGrinchBanner.imagen != null) {
            zombieGrinchBanner.x = 770;
            zombieGrinchBanner.y = 40;
            entorno.dibujarImagen(zombieGrinchBanner.imagen, 
                                zombieGrinchBanner.x, zombieGrinchBanner.y, 0, 0.1);
            entorno.cambiarFont("Arial", 12, Color.WHITE);
            entorno.escribirTexto("Grinch", zombieGrinchBanner.x - 15, zombieGrinchBanner.y + 45);
        }
        
        if (zombieRapidoBanner != null && zombieRapidoBanner.imagen != null) {
            zombieRapidoBanner.x = 870;
            zombieRapidoBanner.y = 40;
            entorno.dibujarImagen(zombieRapidoBanner.imagen, 
                                zombieRapidoBanner.x, zombieRapidoBanner.y, 0, 0.1);
            entorno.cambiarFont("Arial", 12, Color.WHITE);
            entorno.escribirTexto("Rápido", zombieRapidoBanner.x - 15, zombieGrinchBanner.y + 45);
        }
        
        if (zombieColosalBanner != null && zombieColosalBanner.imagen != null) {
            zombieColosalBanner.x = 970;
            zombieColosalBanner.y = 40;
            entorno.dibujarImagen(zombieColosalBanner.imagen, 
                                zombieColosalBanner.x, zombieColosalBanner.y, 0, 0.1);
            entorno.cambiarFont("Arial", 12, Color.WHITE);
            entorno.escribirTexto("Colosal", zombieColosalBanner.x - 15, zombieGrinchBanner.y + 45);
        }
    }

   // MÉTODO: Genera disparos de bolas de nieve por parte de los zombies
   // Propósito: Permitir a los zombies Grinch atacar a distancia a las plantas
   private void generarDisparosZombies() {
        int tickActual = entorno.numeroDeTick();
        
        // Disparos de zombies Grinch
        for (int i = 0; i < zombiesGrinch.length; i++) {
            if (zombiesGrinch[i] != null && zombiesGrinch[i].vivo && 
                zombiesGrinch[i].puedeDisparar(tickActual) && !zombiesGrinch[i].bloqueadoPorPlanta) {
                
                BolaNieve nuevaBola = zombiesGrinch[i].disparar(tickActual);
                if (nuevaBola != null) {
                    agregarBolaNieve(nuevaBola);
                }
            }
        }
    }

   // MÉTODO: Agrega una nueva bola de nieve al array de proyectiles zombies
   // Propósito: Gestionar la creación de proyectiles de zombies en el juego
   private void agregarBolaNieve(BolaNieve bola) {
       for (int i = 0; i < bolasNieve.length; i++) {
           if (bolasNieve[i] == null) {
               bolasNieve[i] = bola;
               cantidadBolasNieveActivas++;
               break;
           }
       }
   }

   // MÉTODO: Actualiza el estado y movimiento de todas las bolas de nieve
   // Propósito: Mover y eliminar bolas de nieve que salen de pantalla
   private void actualizarBolasNieve() {
       for (int i = 0; i < bolasNieve.length; i++) {
           if (bolasNieve[i] != null) {
               bolasNieve[i].mover();
               bolasNieve[i].dibujar();
               if (!bolasNieve[i].activa) {
                   bolasNieve[i] = null;
                   cantidadBolasNieveActivas--;
               }
           }
       }
   }

   // MÉTODO: Verifica colisiones entre bolas de nieve y todas las plantas
   // Propósito: Detectar cuando los proyectiles zombies impactan con plantas
   private void verificarColisionesBolasNieve() {
        for (int i = 0; i < bolasNieve.length; i++) {
            if (bolasNieve[i] != null && bolasNieve[i].activa) {
                verificarColisionBolaNieveConWallnuts(bolasNieve[i], i);
                verificarColisionBolaNieveConPlantasHielo(bolasNieve[i], i);
                verificarColisionBolaNieveConRoseBlades(bolasNieve[i], i);
                verificarColisionBolaNieveConCerezas(bolasNieve[i], i);
            }
        }
    }

    // MÉTODO: Verifica colisión entre bola de nieve y WallNuts
    // Propósito: Aplicar daño a WallNuts cuando son impactados por bolas de nieve
    private void verificarColisionBolaNieveConWallnuts(BolaNieve bola, int indiceBola) {
        for (int j = 0; j < plantasWallnut.length; j++) {
            if (plantasWallnut[j] != null && plantasWallnut[j].plantada &&
                bola.colisionaConWallnut(plantasWallnut[j])) {
                
                plantasWallnut[j].recibirAtaque(entorno.numeroDeTick());
                
                if (!plantasWallnut[j].plantada) {
                    int indiceX = cuadricula.cercanoL(plantasWallnut[j].x, plantasWallnut[j].y).x;
                    int indiceY = cuadricula.cercanoL(plantasWallnut[j].x, plantasWallnut[j].y).y;
                    cuadricula.Ocupado(indiceX, indiceY, false);
                    eliminarWallnut(j);
                }
                
                bola.activa = false;
                cantidadBolasNieveActivas--;
                break;
            }
        }
    }

    // MÉTODO: Verifica colisión entre bola de nieve y Plantas de Hielo
    // Propósito: Aplicar daño a plantas de hielo cuando son impactadas
    private void verificarColisionBolaNieveConPlantasHielo(BolaNieve bola, int indiceBola) {
        for (int j = 0; j < plantasHielo.length; j++) {
            if (plantasHielo[j] != null && plantasHielo[j].plantada &&
                bola.colisionaConPlantaHielo(plantasHielo[j])) {
                
                plantasHielo[j].recibirAtaque(entorno.numeroDeTick());
                
                if (!plantasHielo[j].plantada) {
                    int indiceX = cuadricula.cercanoL(plantasHielo[j].x, plantasHielo[j].y).x;
                    int indiceY = cuadricula.cercanoL(plantasHielo[j].x, plantasHielo[j].y).y;
                    cuadricula.Ocupado(indiceX, indiceY, false);
                    eliminarPlantaHielo(j);
                }
                
                bola.activa = false;
                cantidadBolasNieveActivas--;
                break;
            }
        }
    }

    // MÉTODO: Verifica colisión entre bola de nieve y RoseBlades
    // Propósito: Aplicar daño a RoseBlades cuando son impactadas
    private void verificarColisionBolaNieveConRoseBlades(BolaNieve bola, int indiceBola) {
        for (int j = 0; j < plantasRose.length; j++) {
            if (plantasRose[j] != null && plantasRose[j].plantada &&
                bola.colisionaConRoseBlade(plantasRose[j])) {
                
                plantasRose[j].recibirAtaque(entorno.numeroDeTick());
                
                if (!plantasRose[j].plantada) {
                    int indiceX = cuadricula.cercanoL(plantasRose[j].x, plantasRose[j].y).x;
                    int indiceY = cuadricula.cercanoL(plantasRose[j].x, plantasRose[j].y).y;
                    cuadricula.Ocupado(indiceX, indiceY, false);
                    eliminarRoseBlade(j);
                }
                
                bola.activa = false;
                cantidadBolasNieveActivas--;
                break;
            }
        }
    }

    // MÉTODO: Verifica colisión entre bola de nieve y Cerezas
    // Propósito: Aplicar daño a cerezas explosivas cuando son impactadas
    private void verificarColisionBolaNieveConCerezas(BolaNieve bola, int indiceBola) {
        for (int j = 0; j < plantasCereza.length; j++) {
            if (plantasCereza[j] != null && plantasCereza[j].plantada &&
                bola.colisionaConCereza(plantasCereza[j])) {
                
                plantasCereza[j].recibirAtaque(entorno.numeroDeTick());
                
                if (!plantasCereza[j].plantada) {
                    int indiceX = cuadricula.cercanoL(plantasCereza[j].x, plantasCereza[j].y).x;
                    int indiceY = cuadricula.cercanoL(plantasCereza[j].x, plantasCereza[j].y).y;
                    cuadricula.Ocupado(indiceX, indiceY, false);
                    eliminarCerezaExplosiva(j);
                }
                
                bola.activa = false;
                cantidadBolasNieveActivas--;
                break;
            }
        }
    }

   // MÉTODO: Genera zombies rápidos de manera controlada en el tiempo
   // Propósito: Controlar la aparición periódica de zombies rápidos para mantener desafío
   private void generarZombieRapido() {
       if (ticksParaProximoRapido > 0) {
           ticksParaProximoRapido--;
           
           if (ticksParaProximoRapido <= 0 && cantidadZombiesRapidosActivos < zombiesRapidos.length) {
               int fila = (int)(Math.random() * 5);
               ZombieRapido nuevoZombie = new ZombieRapido(fila, entorno);
               if (agregarZombieRapido(nuevoZombie)) {
                   rapidosAparecidos++;
               }
           }
       }
       
       if (ticksParaProximoRapido <= 0) {
           ticksParaProximoRapido = 270;
       }
   }

   // MÉTODO: Genera zombies colosales de manera controlada en el tiempo
   // Propósito: Controlar la aparición periódica de zombies colosales (enemigos de élite)
   private void generarZombieColosal() {
       if (ticksParaProximoColosal > 0) {
           ticksParaProximoColosal--;
          
           if (ticksParaProximoColosal <= 0 && cantidadZombiesColosalesActivos < zombiesColosales.length) {
               ZombieColosal nuevoZombie = new ZombieColosal(entorno);
               if (agregarZombieColosal(nuevoZombie)) {
                   colosalesAparecidos++;
               }
           }
       }
      
       if (ticksParaProximoColosal <= 0) {
           ticksParaProximoColosal = 1080;
       }
   }
  
   // MÉTODO: Ejecuta la explosión de una cereza explosiva
   // Propósito: Aplicar daño masivo a todos los zombies dentro del radio de explosión
   private void verificarExplosionCereza(CerezaExplosiva cereza, int tickActual) {
       if (cereza.debeExplotar(tickActual)) {
           // Zombies Grinch
           for (int i = 0; i < zombiesGrinch.length; i++) {
               if (zombiesGrinch[i] != null && zombiesGrinch[i].vivo) {
                   double distancia = Math.sqrt(Math.pow(zombiesGrinch[i].x - cereza.x, 2) +
                                             Math.pow(zombiesGrinch[i].y - cereza.y, 2));
                   if (distancia < cereza.radioExplosion) {
                       zombiesGrinch[i].morir();
                       zombiesEliminados++;
                   }
               }
           }
          
           // Zombies Rápidos
           for (int i = 0; i < zombiesRapidos.length; i++) {
               if (zombiesRapidos[i] != null && zombiesRapidos[i].vivo) {
                   double distancia = Math.sqrt(Math.pow(zombiesRapidos[i].x - cereza.x, 2) +
                                             Math.pow(zombiesRapidos[i].y - cereza.y, 2));
                   if (distancia < cereza.radioExplosion) {
                       zombiesRapidos[i].morir();
                       zombiesEliminados += 2;
                   }
               }
           }
          
           // Zombies Colosales
           for (int i = 0; i < zombiesColosales.length; i++) {
               if (zombiesColosales[i] != null && zombiesColosales[i].vivo) {
                   double distancia = Math.sqrt(Math.pow(zombiesColosales[i].x - cereza.x, 2) +
                                             Math.pow(zombiesColosales[i].y - cereza.y, 2));
                   if (distancia < cereza.radioExplosion) {
                       zombiesColosales[i].morir();
                       zombiesEliminados += 5;
                   }
               }
           }
          
           cereza.plantada = false;
           int indiceX = cuadricula.cercanoL(cereza.x, cereza.y).x;
           int indiceY = cuadricula.cercanoL(cereza.x, cereza.y).y;
           cuadricula.Ocupado(indiceX, indiceY, false);
           eliminarCerezaExplosivaDeArray(cereza);
       }
   }

   // MÉTODO: Elimina una cereza explosiva del array después de explotar
   // Propósito: Limpiar el array de plantas después de que una cereza explote
   private void eliminarCerezaExplosivaDeArray(CerezaExplosiva cereza) {
        for (int i = 0; i < plantasCereza.length; i++) {
            if (plantasCereza[i] == cereza) {
                eliminarCerezaExplosiva(i);
                break;
            }
        }
    }
  
   // MÉTODO: Dibuja las plantas en el banner de selección
   // Propósito: Mostrar las plantas disponibles para que el jugador las seleccione
   private void dibujarPlantasBanner() {
       int tickActual = entorno.numeroDeTick();
      
       if (wallnutBanner != null && !wallnutBanner.estaEnRecarga(tickActual)) {
           wallnutBanner.dibujar();
       }
      
       if (hieloBanner != null && !hieloBanner.estaEnRecarga(tickActual)) {
           hieloBanner.dibujar();
       }
      
       if (roseBanner != null && !roseBanner.estaEnRecarga(tickActual)) {
           roseBanner.dibujar();
       }
      
       if (cerezaBanner != null && !cerezaBanner.estaEnRecarga(tickActual)) {
           cerezaBanner.dibujar();
       }
   }

   // ========== MÉTODOS DE MOVIMIENTO CON TECLADO ==========
   
   private boolean puedeMover = true;
   private int ticksBloqueoMovimiento = 0;

   // MÉTODO: Maneja el movimiento de plantas con teclado
   // Propósito: Permitir al jugador mover plantas ya plantadas usando las teclas WASD o flechas
   private void manejarMovimientoTeclado() {
	   
	   if (!puedeMover) {
		    ticksBloqueoMovimiento--;
		    if (ticksBloqueoMovimiento <= 0) {
		        puedeMover = true;
		    }
		    return; 
	   }
	   
	    // Buscar planta seleccionada en todos los arrays
	    WallNut wallnutSeleccionada = null;
	    PlantaDeHielo hieloSeleccionada = null;
	    RoseBlade roseSeleccionada = null;
	    CerezaExplosiva cerezaSeleccionada = null;
	    
	    for (WallNut p : plantasWallnut) {
	        if (p != null && p.seleccionada) {
	            wallnutSeleccionada = p;
	            break;
	        }
	    }
	    
	    if (wallnutSeleccionada == null) {
	        for (PlantaDeHielo p : plantasHielo) {
	            if (p != null && p.seleccionada) {
	                hieloSeleccionada = p;
	                break;
	            }
	        }
	    }
	    
	    if (wallnutSeleccionada == null && hieloSeleccionada == null) {
	        for (RoseBlade p : plantasRose) {
	            if (p != null && p.seleccionada) {
	                roseSeleccionada = p;
	                break;
	            }
	        }
	    }
	    
	    if (wallnutSeleccionada == null && hieloSeleccionada == null && roseSeleccionada == null) {
	        for (CerezaExplosiva p : plantasCereza) {
	            if (p != null && p.seleccionada) {
	                cerezaSeleccionada = p;
	                break;
	            }
	        }
	    }
	    
	    // Mover la planta seleccionada por cuadrícula
	    if (wallnutSeleccionada != null) {
	        moverWallnutCuadricula(wallnutSeleccionada);
	    } else if (hieloSeleccionada != null) {
	        moverPlantaHieloCuadricula(hieloSeleccionada);
	    } else if (roseSeleccionada != null) {
	        moverRoseBladeCuadricula(roseSeleccionada);
	    } else if (cerezaSeleccionada != null) {
	        moverCerezaCuadricula(cerezaSeleccionada);
	    }
	}

   // MÉTODO: Mueve una WallNut por la cuadrícula usando teclado
   // Propósito: Permitir reposicionamiento táctico de plantas defensivas
   private void moverWallnutCuadricula(WallNut planta) {
	    int indiceXActual = cuadricula.cercanoL(planta.x, planta.y).x;
	    int indiceYActual = cuadricula.cercanoL(planta.x, planta.y).y;

	    int nuevoIndiceX = indiceXActual;
	    int nuevoIndiceY = indiceYActual;

	    if (entorno.sePresiono(entorno.TECLA_ARRIBA) || entorno.sePresiono('w')) {
	        nuevoIndiceY = indiceYActual - 1;
	    } else if (entorno.sePresiono(entorno.TECLA_ABAJO) || entorno.sePresiono('s')) {
	        nuevoIndiceY = indiceYActual + 1;
	    } else if (entorno.sePresiono(entorno.TECLA_IZQUIERDA) || entorno.sePresiono('a')) {
	        nuevoIndiceX = indiceXActual - 1;
	    } else if (entorno.sePresiono(entorno.TECLA_DERECHA) || entorno.sePresiono('d')) {
	        nuevoIndiceX = indiceXActual + 1;
	    }

	    if ((nuevoIndiceX != indiceXActual || nuevoIndiceY != indiceYActual) &&
	        !cuadricula.estaOcupado(nuevoIndiceX, nuevoIndiceY)) {
	        
	        cuadricula.Ocupado(indiceXActual, indiceYActual, false);
	        planta.x = cuadricula.coorX[nuevoIndiceX];
	        planta.y = cuadricula.coorY[nuevoIndiceY];
	        planta.xInicial = planta.x;
	        planta.yInicial = planta.y;
	        cuadricula.Ocupado(nuevoIndiceX, nuevoIndiceY, true);

	        System.out.println("Movido de [" + indiceXActual + "," + indiceYActual + "] a [" + nuevoIndiceX + "," + nuevoIndiceY + "]");

	        puedeMover = false;
	        ticksBloqueoMovimiento = 10;
	    }
	}

   // MÉTODO: Mueve una PlantaDeHielo por la cuadrícula usando teclado
   // Propósito: Permitir reposicionamiento táctico de plantas de hielo
   private void moverPlantaHieloCuadricula(PlantaDeHielo planta) {
	    int indiceXActual = cuadricula.cercanoL(planta.x, planta.y).x;
	    int indiceYActual = cuadricula.cercanoL(planta.x, planta.y).y;
	    
	    int nuevoIndiceX = indiceXActual;
	    int nuevoIndiceY = indiceYActual;
	    
	    if (entorno.sePresiono(entorno.TECLA_ARRIBA) || entorno.sePresiono('w')) {
	        nuevoIndiceY = indiceYActual - 1;
	    }
	    else if (entorno.sePresiono(entorno.TECLA_ABAJO) || entorno.sePresiono('s')) {
	        nuevoIndiceY = indiceYActual + 1;  
	    }
	    else if (entorno.sePresiono(entorno.TECLA_IZQUIERDA) || entorno.sePresiono('a')) {
	        nuevoIndiceX = indiceXActual - 1;
	    }
	    else if (entorno.sePresiono(entorno.TECLA_DERECHA) || entorno.sePresiono('d')) {
	        nuevoIndiceX = indiceXActual + 1;
	    }
	    
	    if ((nuevoIndiceX != indiceXActual || nuevoIndiceY != indiceYActual) && 
	        !cuadricula.estaOcupado(nuevoIndiceX, nuevoIndiceY)) {
	        
	        cuadricula.Ocupado(indiceXActual, indiceYActual, false);
	        planta.x = cuadricula.coorX[nuevoIndiceX];
	        planta.y = cuadricula.coorY[nuevoIndiceY];
	        planta.xInicial = planta.x;
	        planta.yInicial = planta.y;
	        cuadricula.Ocupado(nuevoIndiceX, nuevoIndiceY, true);
	        puedeMover = false;
	        ticksBloqueoMovimiento = 10;
	    }
	}

   // MÉTODO: Mueve una RoseBlade por la cuadrícula usando teclado
   // Propósito: Permitir reposicionamiento táctico de plantas ofensivas
	private void moverRoseBladeCuadricula(RoseBlade planta) {
	    int indiceXActual = cuadricula.cercanoL(planta.x, planta.y).x;
	    int indiceYActual = cuadricula.cercanoL(planta.x, planta.y).y;
	    
	    int nuevoIndiceX = indiceXActual;
	    int nuevoIndiceY = indiceYActual;
	    
	    if (entorno.sePresiono(entorno.TECLA_ARRIBA) || entorno.sePresiono('w')) {
	        nuevoIndiceY = indiceYActual - 1;
	    }
	    else if (entorno.sePresiono(entorno.TECLA_ABAJO) || entorno.sePresiono('s')) {
	        nuevoIndiceY = indiceYActual + 1;  
	    }
	    else if (entorno.sePresiono(entorno.TECLA_IZQUIERDA) || entorno.sePresiono('a')) {
	        nuevoIndiceX = indiceXActual - 1;
	    }
	    else if (entorno.sePresiono(entorno.TECLA_DERECHA) || entorno.sePresiono('d')) {
	        nuevoIndiceX = indiceXActual + 1;
	    }
	    
	    if ((nuevoIndiceX != indiceXActual || nuevoIndiceY != indiceYActual) && 
	        !cuadricula.estaOcupado(nuevoIndiceX, nuevoIndiceY)) {
	        
	        cuadricula.Ocupado(indiceXActual, indiceYActual, false);
	        planta.x = cuadricula.coorX[nuevoIndiceX];
	        planta.y = cuadricula.coorY[nuevoIndiceY];
	        planta.xInicial = planta.x;
	        planta.yInicial = planta.y;
	        cuadricula.Ocupado(nuevoIndiceX, nuevoIndiceY, true);
	        
	        puedeMover = false;
	        ticksBloqueoMovimiento = 10;
	    }
	}

   // MÉTODO: Mueve una CerezaExplosiva por la cuadrícula usando teclado
   // Propósito: Permitir reposicionamiento táctico de plantas explosivas
	private void moverCerezaCuadricula(CerezaExplosiva planta) {
	    int indiceXActual = cuadricula.cercanoL(planta.x, planta.y).x;
	    int indiceYActual = cuadricula.cercanoL(planta.x, planta.y).y;
	    
	    int nuevoIndiceX = indiceXActual;
	    int nuevoIndiceY = indiceYActual;
	    
	    if (entorno.sePresiono(entorno.TECLA_ARRIBA) || entorno.sePresiono('w')) {
	        nuevoIndiceY = indiceYActual - 1;
	    }
	    else if (entorno.sePresiono(entorno.TECLA_ABAJO) || entorno.sePresiono('s')) {
	        nuevoIndiceY = indiceYActual + 1;  
	    }
	    else if (entorno.sePresiono(entorno.TECLA_IZQUIERDA) || entorno.sePresiono('a')) {
	        nuevoIndiceX = indiceXActual - 1;
	    }
	    else if (entorno.sePresiono(entorno.TECLA_DERECHA) || entorno.sePresiono('d')) {
	        nuevoIndiceX = indiceXActual + 1;
	    }
	    
	    if ((nuevoIndiceX != indiceXActual || nuevoIndiceY != indiceYActual) && 
	        !cuadricula.estaOcupado(nuevoIndiceX, nuevoIndiceY)) {
	        
	        cuadricula.Ocupado(indiceXActual, indiceYActual, false);
	        planta.x = cuadricula.coorX[nuevoIndiceX];
	        planta.y = cuadricula.coorY[nuevoIndiceY];
	        planta.xInicial = planta.x;
	        planta.yInicial = planta.y;
	        cuadricula.Ocupado(nuevoIndiceX, nuevoIndiceY, true);
	        puedeMover = false;
	        ticksBloqueoMovimiento = 10;
	    }
	}

   // ========== MÉTODOS DE COMBATE Y ATAQUES ==========

   // MÉTODO: Verifica y procesa los ataques de todos los zombies contra plantas
   // Propósito: Gestionar el sistema de combate cuerpo a cuerpo entre zombies y plantas
   private void verificarAtaquesZombies() {
     int tickActual = entorno.numeroDeTick();
     
     // Ataques de zombies Grinch
     for (int i = 0; i < zombiesGrinch.length; i++) {
    	    if (zombiesGrinch[i] != null && zombiesGrinch[i].vivo) {
    	        zombiesGrinch[i].verificarPlantaBloqueadora(plantasWallnut, plantasHielo, plantasRose, plantasCereza);

    	        if (zombiesGrinch[i].bloqueadoPorPlanta) {
    	            Object plantaBloqueadora = zombiesGrinch[i].plantaBloqueadora;
                 // Verificar WallNuts
                 for (int j = 0; j < plantasWallnut.length; j++) {
                     if (plantasWallnut[j] == plantaBloqueadora && WallnutPlantada(plantasWallnut[j]) && 
                         zombiesGrinch[i].puedeAtacar(tickActual)) {
                         
                         aplicarAtaqueWallnut(plantasWallnut[j], tickActual);
                         
                         if (!WallnutPlantada(plantasWallnut[j])) {
                             liberarYLimpiarWallnut(plantasWallnut[j]);
                             zombiesGrinch[i].liberar();
                         }
                         zombiesGrinch[i].registrarAtaque(tickActual);
                         break;
                     }
                 }
                 
                 // Verificar Plantas de Hielo
                 for (int j = 0; j < plantasHielo.length; j++) {
                     if (plantasHielo[j] == plantaBloqueadora && PlantaHieloPlantada(plantasHielo[j]) && 
                         zombiesGrinch[i].puedeAtacar(tickActual)) {
                         
                         aplicarAtaquePlantaHielo(plantasHielo[j], tickActual);
                         
                         if (!PlantaHieloPlantada(plantasHielo[j])) {
                             liberarYLimpiarPlantaHielo(plantasHielo[j]);
                             zombiesGrinch[i].liberar();
                         }
                         zombiesGrinch[i].registrarAtaque(tickActual);
                         break;
                     }
                 }
                 
                 // Verificar RoseBlades
                 for (int j = 0; j < plantasRose.length; j++) {
                     if (plantasRose[j] == plantaBloqueadora && estaRoseBladePlantada(plantasRose[j]) && 
                         zombiesGrinch[i].puedeAtacar(tickActual)) {
                         
                         aplicarAtaqueRoseBlade(plantasRose[j], tickActual);
                         
                         if (!estaRoseBladePlantada(plantasRose[j])) {
                             liberarYLimpiarRoseBlade(plantasRose[j]);
                             zombiesGrinch[i].liberar();
                         }
                         zombiesGrinch[i].registrarAtaque(tickActual);
                         break;
                     }
                 }
                 
                 // Verificar Cerezas
                 for (int j = 0; j < plantasCereza.length; j++) {
                     if (plantasCereza[j] == plantaBloqueadora && estaCerezaPlantada(plantasCereza[j]) && 
                         zombiesGrinch[i].puedeAtacar(tickActual)) {
                         
                         aplicarAtaqueCereza(plantasCereza[j], tickActual);
                         
                         if (!estaCerezaPlantada(plantasCereza[j])) {
                             liberarYLimpiarCereza(plantasCereza[j]);
                             zombiesGrinch[i].liberar();
                         }
                         zombiesGrinch[i].registrarAtaque(tickActual);
                         break;
                     }
                 }
             } else {
                 buscarYBloquearPlantaGrinch(zombiesGrinch[i], tickActual);
             }
         }
     }
     
     // Ataques de zombies Rápidos
     for (int i = 0; i < zombiesRapidos.length; i++) {
 	    if (zombiesRapidos[i] != null && zombiesRapidos[i].vivo) {
 	        zombiesRapidos[i].verificarPlantaBloqueadora(plantasWallnut, plantasHielo, plantasRose, plantasCereza);

 	        if (zombiesRapidos[i].bloqueadoPorPlanta) {
 	            Object plantaBloqueadora = zombiesRapidos[i].plantaBloqueadora;
                 // Verificar WallNuts
                 for (int j = 0; j < plantasWallnut.length; j++) {
                     if (plantasWallnut[j] == plantaBloqueadora && WallnutPlantada(plantasWallnut[j]) && 
                         zombiesRapidos[i].puedeAtacar(tickActual)) {
                         
                         aplicarAtaqueWallnut(plantasWallnut[j], tickActual);
                         
                         if (!WallnutPlantada(plantasWallnut[j])) {
                             liberarYLimpiarWallnut(plantasWallnut[j]);
                             zombiesRapidos[i].liberar();
                         }
                         zombiesRapidos[i].registrarAtaque(tickActual);
                         break;
                     }
                 }
                 
                 // Verificar Plantas de Hielo
                 for (int j = 0; j < plantasHielo.length; j++) {
                     if (plantasHielo[j] == plantaBloqueadora && PlantaHieloPlantada(plantasHielo[j]) && 
                         zombiesRapidos[i].puedeAtacar(tickActual)) {
                         
                         aplicarAtaquePlantaHielo(plantasHielo[j], tickActual);
                         
                         if (!PlantaHieloPlantada(plantasHielo[j])) {
                             liberarYLimpiarPlantaHielo(plantasHielo[j]);
                             zombiesRapidos[i].liberar();
                         }
                         zombiesRapidos[i].registrarAtaque(tickActual);
                         break;
                     }
                 }
                 
                 // Verificar RoseBlades
                 for (int j = 0; j < plantasRose.length; j++) {
                     if (plantasRose[j] == plantaBloqueadora && estaRoseBladePlantada(plantasRose[j]) && 
                         zombiesRapidos[i].puedeAtacar(tickActual)) {
                         
                         aplicarAtaqueRoseBlade(plantasRose[j], tickActual);
                         
                         if (!estaRoseBladePlantada(plantasRose[j])) {
                             liberarYLimpiarRoseBlade(plantasRose[j]);
                             zombiesRapidos[i].liberar();
                         }
                         zombiesRapidos[i].registrarAtaque(tickActual);
                         break;
                     }
                 }
                 
                 // Verificar Cerezas
                 for (int j = 0; j < plantasCereza.length; j++) {
                     if (plantasCereza[j] == plantaBloqueadora && estaCerezaPlantada(plantasCereza[j]) && 
                         zombiesRapidos[i].puedeAtacar(tickActual)) {
                         
                         aplicarAtaqueCereza(plantasCereza[j], tickActual);
                         
                         if (!estaCerezaPlantada(plantasCereza[j])) {
                             liberarYLimpiarCereza(plantasCereza[j]);
                             zombiesRapidos[i].liberar();
                         }
                         zombiesRapidos[i].registrarAtaque(tickActual);
                         break;
                     }
                 }
             } else {
                 buscarYBloquearPlantaRapido(zombiesRapidos[i]);
             }
         }
     }
     
     // Ataques de zombies Colosales
     for (int i = 0; i < zombiesColosales.length; i++) {
         if (zombiesColosales[i] != null && zombiesColosales[i].vivo) {
             buscarYAtacarPlantaColosal(zombiesColosales[i], tickActual);
         }
     }
 }

 
   // MÉTODO: Verifica si una WallNut está plantada
   // Propósito: Validar el estado de una planta antes de procesar ataques
   private boolean WallnutPlantada(WallNut planta) {
	    return planta.plantada;
	}

   // MÉTODO: Verifica si una PlantaDeHielo está plantada
   // Propósito: Validar el estado de una planta antes de procesar ataques
	private boolean PlantaHieloPlantada(PlantaDeHielo planta) {
	    return planta.plantada;
	}

   // MÉTODO: Verifica si una RoseBlade está plantada
   // Propósito: Validar el estado de una planta antes de procesar ataques
	private boolean estaRoseBladePlantada(RoseBlade planta) {
	    return planta.plantada;
	}

   // MÉTODO: Verifica si una CerezaExplosiva está plantada
   // Propósito: Validar el estado de una planta antes de procesar ataques
	private boolean estaCerezaPlantada(CerezaExplosiva planta) {
	    return planta.plantada;
	}

   // MÉTODO: Aplica daño a una WallNut
   // Propósito: Procesar el efecto de un ataque zombie sobre una WallNut
	private void aplicarAtaqueWallnut(WallNut planta, int tickActual) {
	    planta.recibirAtaque(tickActual);
	}

   // MÉTODO: Aplica daño a una PlantaDeHielo
   // Propósito: Procesar el efecto de un ataque zombie sobre una planta de hielo
	private void aplicarAtaquePlantaHielo(PlantaDeHielo planta, int tickActual) {
	    planta.recibirAtaque(tickActual);
	}

   // MÉTODO: Aplica daño a una RoseBlade
   // Propósito: Procesar el efecto de un ataque zombie sobre una RoseBlade
	private void aplicarAtaqueRoseBlade(RoseBlade planta, int tickActual) {
	    planta.recibirAtaque(tickActual);
	}

   // MÉTODO: Aplica daño a una CerezaExplosiva
   // Propósito: Procesar el efecto de un ataque zombie sobre una cereza
	private void aplicarAtaqueCereza(CerezaExplosiva planta, int tickActual) {
	    planta.recibirAtaque(tickActual);
	}

   // MÉTODO: Libera y elimina una WallNut del juego
   // Propósito: Limpiar una WallNut destruida y liberar su casilla
	private void liberarYLimpiarWallnut(WallNut planta) {
	    int indiceX = cuadricula.cercanoL(planta.x, planta.y).x;
	    int indiceY = cuadricula.cercanoL(planta.x, planta.y).y;
	    cuadricula.Ocupado(indiceX, indiceY, false);
	    eliminarWallnutDeArray(planta);
	}

   // MÉTODO: Libera y elimina una PlantaDeHielo del juego
   // Propósito: Limpiar una planta de hielo destruida y liberar su casilla
	private void liberarYLimpiarPlantaHielo(PlantaDeHielo planta) {
	    int indiceX = cuadricula.cercanoL(planta.x, planta.y).x;
	    int indiceY = cuadricula.cercanoL(planta.x, planta.y).y;
	    cuadricula.Ocupado(indiceX, indiceY, false);
	    eliminarPlantaHieloDeArray(planta);
	}

   // MÉTODO: Libera y elimina una RoseBlade del juego
   // Propósito: Limpiar una RoseBlade destruida y liberar su casilla
	private void liberarYLimpiarRoseBlade(RoseBlade planta) {
	    int indiceX = cuadricula.cercanoL(planta.x, planta.y).x;
	    int indiceY = cuadricula.cercanoL(planta.x, planta.y).y;
	    cuadricula.Ocupado(indiceX, indiceY, false);
	    eliminarRoseBladeDeArray(planta);
	}

   // MÉTODO: Libera y elimina una CerezaExplosiva del juego
   // Propósito: Limpiar una cereza destruida y liberar su casilla
	private void liberarYLimpiarCereza(CerezaExplosiva planta) {
	    int indiceX = cuadricula.cercanoL(planta.x, planta.y).x;
	    int indiceY = cuadricula.cercanoL(planta.x, planta.y).y;
	    cuadricula.Ocupado(indiceX, indiceY, false);
	    eliminarCerezaExplosivaDeArray(planta);
	}

   // MÉTODO: Elimina una WallNut del array por referencia
   // Propósito: Encontrar y remover una WallNut específica del array
   private void eliminarWallnutDeArray(WallNut wallnut) {
     for (int i = 0; i < plantasWallnut.length; i++) {
         if (plantasWallnut[i] == wallnut) {
             eliminarWallnut(i);
             break;
         }
     }
   }

   // MÉTODO: Elimina una PlantaDeHielo del array por referencia
   // Propósito: Encontrar y remover una planta de hielo específica del array
   private void eliminarPlantaHieloDeArray(PlantaDeHielo planta) {
     for (int i = 0; i < plantasHielo.length; i++) {
         if (plantasHielo[i] == planta) {
             eliminarPlantaHielo(i);
             break;
         }
     }
   }

   // MÉTODO: Elimina una RoseBlade del array por referencia
   // Propósito: Encontrar y remover una RoseBlade específica del array
   private void eliminarRoseBladeDeArray(RoseBlade rose) {
     for (int i = 0; i < plantasRose.length; i++) {
         if (plantasRose[i] == rose) {
             eliminarRoseBlade(i);
             break;
         }
     }
   }

   // MÉTODO: Busca y bloquea una planta para el zombie Grinch
   // Propósito: Encontrar la primera planta con la que colisiona un zombie Grinch
   private void buscarYBloquearPlantaGrinch(ZombieGrinch zombie, int tickActual) {
     // Buscar en WallNuts
     for (int j = 0; j < plantasWallnut.length; j++) {
         if (plantasWallnut[j] != null && plantasWallnut[j].plantada && 
             zombie.colisionaConWallnut(plantasWallnut[j])) {
             zombie.bloquear(plantasWallnut[j]);
             return;
         }
     }
     
     // Buscar en Plantas de Hielo
     for (int j = 0; j < plantasHielo.length; j++) {
         if (plantasHielo[j] != null && plantasHielo[j].plantada && 
             zombie.colisionaConPlantaHielo(plantasHielo[j])) {
             zombie.bloquear(plantasHielo[j]);
             return;
         }
     }
     
     // Buscar en RoseBlades
     for (int j = 0; j < plantasRose.length; j++) {
         if (plantasRose[j] != null && plantasRose[j].plantada && 
             zombie.colisionaConRoseBlade(plantasRose[j])) {
             zombie.bloquear(plantasRose[j]);
             return;
         }
     }
     
     // Buscar en Cerezas
     for (int j = 0; j < plantasCereza.length; j++) {
         if (plantasCereza[j] != null && plantasCereza[j].plantada && 
             zombie.colisionaConCereza(plantasCereza[j])) {
             zombie.bloquear(plantasCereza[j]);
             
             // Verificar explosión de cereza
             if (!plantasCereza[j].explotando && hayZombieCercaCereza(plantasCereza[j])) {
                 verificarExplosionCereza(plantasCereza[j], tickActual);
             }
             return;
         }
     }
   }

   // MÉTODO: Verifica si hay zombies cerca de una cereza para activar explosión
   // Propósito: Detonar cerezas automáticamente cuando zombies se acercan
   private boolean hayZombieCercaCereza(CerezaExplosiva cereza) {
     for (int i = 0; i < zombiesGrinch.length; i++) {
         if (zombiesGrinch[i] != null && zombiesGrinch[i].vivo) {
             double distancia = Math.sqrt(Math.pow(zombiesGrinch[i].x - cereza.x, 2) +
                                       Math.pow(zombiesGrinch[i].y - cereza.y, 2));
             if (distancia < cereza.radioExplosion) {
                 return true;
             }
         }
     }
     return false;
   }

   // MÉTODO: Busca y bloquea una planta para el zombie rápido
   // Propósito: Encontrar la primera planta con la que colisiona un zombie rápido
   private void buscarYBloquearPlantaRapido(ZombieRapido zombie) {
     // Buscar en todas las plantas
     for (int j = 0; j < plantasWallnut.length; j++) {
         if (plantasWallnut[j] != null && plantasWallnut[j].plantada && 
             zombie.colisionaConWallnut(plantasWallnut[j])) {
             zombie.bloquear(plantasWallnut[j]);
             return;
         }
     }
     for (int j = 0; j < plantasHielo.length; j++) {
         if (plantasHielo[j] != null && plantasHielo[j].plantada && 
             zombie.colisionaConPlantaHielo(plantasHielo[j])) {
             zombie.bloquear(plantasHielo[j]);
             return;
         }
     }
     for (int j = 0; j < plantasRose.length; j++) {
         if (plantasRose[j] != null && plantasRose[j].plantada && 
             zombie.colisionaConRoseBlade(plantasRose[j])) {
             zombie.bloquear(plantasRose[j]);
             return;
         }
     }
     for (int j = 0; j < plantasCereza.length; j++) {
         if (plantasCereza[j] != null && plantasCereza[j].plantada && 
             zombie.colisionaConCereza(plantasCereza[j])) {
             zombie.bloquear(plantasCereza[j]);
             return;
         }
     }
   }

   // MÉTODO: Busca y ataca plantas para el zombie colosal
   // Propósito: Permitir al zombie colosal atacar plantas con daño doble
   private void buscarYAtacarPlantaColosal(ZombieColosal zombie, int tickActual) {
     // Buscar en WallNuts
     for (int j = 0; j < plantasWallnut.length; j++) {
         if (plantasWallnut[j] != null && plantasWallnut[j].plantada &&
             zombie.colisionaConWallnut(plantasWallnut[j])) {
             
             if (zombie.puedeAtacar(tickActual)) {
                 plantasWallnut[j].recibirAtaque(tickActual);
                 plantasWallnut[j].recibirAtaque(tickActual);
                 
                 if (!plantasWallnut[j].plantada) {
                     int indiceX = cuadricula.cercanoL(plantasWallnut[j].x, plantasWallnut[j].y).x;
                     int indiceY = cuadricula.cercanoL(plantasWallnut[j].x, plantasWallnut[j].y).y;
                     cuadricula.Ocupado(indiceX, indiceY, false);
                     eliminarWallnut(j);
                 }
                 zombie.registrarAtaque(tickActual);
             }
             return;
         }
     }
     
     // Buscar en otras plantas
     for (int j = 0; j < plantasHielo.length; j++) {
         if (plantasHielo[j] != null && plantasHielo[j].plantada &&
             zombie.colisionaConPlantaHielo(plantasHielo[j])) {
             
             if (zombie.puedeAtacar(tickActual)) {
                 plantasHielo[j].recibirAtaque(tickActual);
                 
                 if (!plantasHielo[j].plantada) {
                     int indiceX = cuadricula.cercanoL(plantasHielo[j].x, plantasHielo[j].y).x;
                     int indiceY = cuadricula.cercanoL(plantasHielo[j].x, plantasHielo[j].y).y;
                     cuadricula.Ocupado(indiceX, indiceY, false);
                     eliminarPlantaHielo(j);
                 }
                 zombie.registrarAtaque(tickActual);
             }
             return;
         }
     }
     
     for (int j = 0; j < plantasRose.length; j++) {
         if (plantasRose[j] != null && plantasRose[j].plantada &&
             zombie.colisionaConRoseBlade(plantasRose[j])) {
             
             if (zombie.puedeAtacar(tickActual)) {
                 plantasRose[j].recibirAtaque(tickActual);
                 
                 if (!plantasRose[j].plantada) {
                     int indiceX = cuadricula.cercanoL(plantasRose[j].x, plantasRose[j].y).x;
                     int indiceY = cuadricula.cercanoL(plantasRose[j].x, plantasRose[j].y).y;
                     cuadricula.Ocupado(indiceX, indiceY, false);
                     eliminarRoseBlade(j);
                 }
                 zombie.registrarAtaque(tickActual);
             }
             return;
         }
     }
     
     for (int j = 0; j < plantasCereza.length; j++) {
         if (plantasCereza[j] != null && plantasCereza[j].plantada &&
             zombie.colisionaConCereza(plantasCereza[j])) {
             
             if (zombie.puedeAtacar(tickActual)) {
                 plantasCereza[j].recibirAtaque(tickActual);
                 
                 if (!plantasCereza[j].plantada) {
                     int indiceX = cuadricula.cercanoL(plantasCereza[j].x, plantasCereza[j].y).x;
                     int indiceY = cuadricula.cercanoL(plantasCereza[j].x, plantasCereza[j].y).y;
                     cuadricula.Ocupado(indiceX, indiceY, false);
                     eliminarCerezaExplosiva(j);
                 }
                 zombie.registrarAtaque(tickActual);
             }
             return;
         }
     }
   }

  

   // MÉTODO: Dibuja las barras de recarga de las plantas en el banner
   // Propósito: Mostrar visualmente el tiempo de recarga restante para cada tipo de planta
   private void dibujarBarrasRecarga() {
     int tickActual = entorno.numeroDeTick();
     
     // WallNut
     if (wallnutBanner != null) {
         double porcentaje = wallnutBanner.porcentajeRecarga(tickActual);
         dibujarBarraRecargaWallnut(50, 75, porcentaje, tickActual);
     }
     
     // PlantaDeHielo
     if (hieloBanner != null) {
         double porcentaje = hieloBanner.porcentajeRecarga(tickActual);
         dibujarBarraRecargaPlantaHielo(150, 75, porcentaje, tickActual);
     }
     
     // RoseBlade
     if (roseBanner != null) {
         double porcentaje = roseBanner.porcentajeRecarga(tickActual);
         dibujarBarraRecargaRoseBlade(250, 75, porcentaje, tickActual);
     }
     
     // CerezaExplosiva
     if (cerezaBanner != null) {
         double porcentaje = cerezaBanner.porcentajeRecarga(tickActual);
         dibujarBarraRecargaCereza(350, 75, porcentaje, tickActual);
     }
   }

   // MÉTODO: Dibuja la barra de recarga específica para WallNut
   // Propósito: Mostrar barra de recarga con estilo visual único para WallNut
   private void dibujarBarraRecargaWallnut(double x, double y, double porcentaje, int tickActual) {
     Color colorFondo = new Color(139, 69, 19);
     Color colorBorde = new Color(160, 82, 45);
     dibujarBarraRecargaCompleta(x, y, porcentaje, colorFondo, colorBorde, "WallNut", wallnutBanner, tickActual);
   }

   // MÉTODO: Dibuja la barra de recarga específica para PlantaDeHielo
   // Propósito: Mostrar barra de recarga con estilo visual único para planta de hielo
   private void dibujarBarraRecargaPlantaHielo(double x, double y, double porcentaje, int tickActual) {
     Color colorFondo = new Color(135, 206, 250);
     Color colorBorde = new Color(173, 216, 230);
     dibujarBarraRecargaCompleta(x, y, porcentaje, colorFondo, colorBorde, "Escarchi", hieloBanner, tickActual);
   }

   // MÉTODO: Dibuja la barra de recarga específica para RoseBlade
   // Propósito: Mostrar barra de recarga con estilo visual único para RoseBlade
   private void dibujarBarraRecargaRoseBlade(double x, double y, double porcentaje, int tickActual) {
     Color colorFondo = new Color(255, 0, 0);
     Color colorBorde = new Color(255, 69, 0);
     dibujarBarraRecargaCompleta(x, y, porcentaje, colorFondo, colorBorde, "RoseBlade", roseBanner, tickActual);
   }

   // MÉTODO: Dibuja la barra de recarga específica para CerezaExplosiva
   // Propósito: Mostrar barra de recarga con estilo visual único para cereza
   private void dibujarBarraRecargaCereza(double x, double y, double porcentaje, int tickActual) {
     Color colorFondo = new Color(148, 0, 211);
     Color colorBorde = new Color(186, 85, 211);
     dibujarBarraRecargaCompleta(x, y, porcentaje, colorFondo, colorBorde, "Cereza", cerezaBanner, tickActual);
   }

   // MÉTODO: Dibuja una barra de recarga completa con todos los elementos
   // Propósito: Implementar la lógica común para dibujar cualquier barra de recarga
   private void dibujarBarraRecargaCompleta(double x, double y, double porcentaje, Color colorFondo, Color colorBorde, String texto, Object planta, int tickActual) {
     double anchoBarra = 80;
     double altoBarra = 12;
     
     Color fondoOscuro = oscurecerColor(colorFondo, 0.5f);
     entorno.dibujarRectangulo(x, y, anchoBarra, altoBarra, 0, fondoOscuro);
     
     if (porcentaje > 0) {
         double anchoLleno = anchoBarra * porcentaje;
         
         if (porcentaje < 1.0) {
             for (int i = 0; i < anchoLleno; i += 4) {
                 double onda = Math.sin((tickActual * 0.3) + (i * 0.2)) * 2;
                 double alturaOnda = altoBarra + onda;
                 double posY = y - onda/2;
                 
                 if (i + 2 <= anchoLleno) {
                     entorno.dibujarRectangulo(
                         x - anchoBarra/2 + i + 2, 
                         posY, 
                         2, 
                         alturaOnda, 
                         0, 
                         aclararColor(colorFondo, 0.1f)
                     );
                 }
             }
         }
         
         entorno.dibujarRectangulo(x - anchoBarra/2 + anchoLleno/2, y, anchoLleno, altoBarra, 0, colorFondo);
     }
     
     entorno.dibujarRectangulo(x, y, anchoBarra, altoBarra, 0, colorBorde);
     
     entorno.cambiarFont("Arial", 12, Color.WHITE);
     entorno.escribirTexto(texto, x - 25, y - 5);
     
     if (porcentaje < 1.0) {
         int segundosRestantes = 0;
         if (planta == wallnutBanner) {
             segundosRestantes = calcularSegundosRestantesWallnut((WallNut)planta, tickActual);
         } else if (planta == hieloBanner) {
             segundosRestantes = calcularSegundosRestantesPlantaHielo((PlantaDeHielo)planta, tickActual);
         } else if (planta == roseBanner) {
             segundosRestantes = calcularSegundosRestantesRoseBlade((RoseBlade)planta, tickActual);
         } else if (planta == cerezaBanner) {
             segundosRestantes = calcularSegundosRestantesCereza((CerezaExplosiva)planta, tickActual);
         }
         
         Color colorTiempo = esColorOscuro(colorFondo) ? Color.WHITE : Color.BLACK;
         entorno.cambiarFont("Arial", 10, colorTiempo);
         
         if (segundosRestantes <= 5) {
             entorno.cambiarFont("Arial", 10, Color.YELLOW);
         } else if (segundosRestantes <= 10) {
             entorno.cambiarFont("Arial", 10, Color.ORANGE);
         }
         
         entorno.escribirTexto(segundosRestantes + "s", x - 8, y + 20);
     } else {
         Color colorListo = esColorOscuro(colorFondo) ? Color.GREEN : new Color(0, 100, 0);
         entorno.cambiarFont("Arial", 10, colorListo);
         entorno.escribirTexto("Listo", x - 12, y + 20);
     }
   }

   // MÉTODO: Aclara un color para efectos visuales
   // Propósito: Crear variaciones de color para efectos de interfaz
   private Color aclararColor(Color color, float factor) {
     int rojo = Math.min(255, (int)(color.getRed() + (255 - color.getRed()) * factor));
     int verde = Math.min(255, (int)(color.getGreen() + (255 - color.getGreen()) * factor));
     int azul = Math.min(255, (int)(color.getBlue() + (255 - color.getBlue()) * factor));
     return new Color(rojo, verde, azul);
   }

   // MÉTODO: Oscurece un color para efectos visuales
   // Propósito: Crear variaciones de color para efectos de interfaz
   private Color oscurecerColor(Color color, float factor) {
     int rojo = Math.max(0, (int)(color.getRed() * factor));
     int verde = Math.max(0, (int)(color.getGreen() * factor));
     int azul = Math.max(0, (int)(color.getBlue() * factor));
     return new Color(rojo, verde, azul);
   }

   // MÉTODO: Determina si un color es oscuro
   // Propósito: Elegir colores de texto contrastantes para mejor legibilidad
   private boolean esColorOscuro(Color color) {
     double luminosidad = (0.299 * color.getRed() + 0.587 * color.getGreen() + 0.114 * color.getBlue()) / 255;
     return luminosidad < 0.5;
   }

   // MÉTODO: Calcula segundos restantes para recarga de WallNut
   // Propósito: Convertir ticks del juego a segundos entendibles para el jugador
   private int calcularSegundosRestantesWallnut(WallNut planta, int tickActual) {
     int tiempoTranscurrido = tickActual - planta.tiempoUltimoPlantado;
     int tiempoRestante = Math.max(0, planta.tiempoRecargaPlantado - tiempoTranscurrido);
     return (int) Math.ceil(tiempoRestante / 50.0);
   }

   // MÉTODO: Calcula segundos restantes para recarga de PlantaDeHielo
   // Propósito: Convertir ticks del juego a segundos entendibles para el jugador
   private int calcularSegundosRestantesPlantaHielo(PlantaDeHielo planta, int tickActual) {
     int tiempoTranscurrido = tickActual - planta.tiempoUltimoPlantado;
     int tiempoRestante = Math.max(0, planta.tiempoRecargaPlantado - tiempoTranscurrido);
     return (int) Math.ceil(tiempoRestante / 50.0);
   }

   // MÉTODO: Calcula segundos restantes para recarga de RoseBlade
   // Propósito: Convertir ticks del juego a segundos entendibles para el jugador
   private int calcularSegundosRestantesRoseBlade(RoseBlade planta, int tickActual) {
     int tiempoTranscurrido = tickActual - planta.tiempoUltimoPlantado;
     int tiempoRestante = Math.max(0, planta.tiempoRecargaPlantado - tiempoTranscurrido);
     return (int) Math.ceil(tiempoRestante / 50.0);
   }

   // MÉTODO: Calcula segundos restantes para recarga de CerezaExplosiva
   // Propósito: Convertir ticks del juego a segundos entendibles para el jugador
   private int calcularSegundosRestantesCereza(CerezaExplosiva planta, int tickActual) {
     int tiempoTranscurrido = tickActual - planta.tiempoUltimoPlantado;
     int tiempoRestante = Math.max(0, planta.tiempoRecargaPlantado - tiempoTranscurrido);
     return (int) Math.ceil(tiempoRestante / 50.0);
   }

  
   // MÉTODO: Agrega un disparo de fuego al array de proyectiles
   // Propósito: Permitir que las plantas RoseBlade agreguen sus proyectiles al juego
   public void agregarDisparo(BolaFuego disparo) {
    for (int i = 0; i < disparos.length; i++) {
        if (disparos[i] == null) {
            disparos[i] = disparo;
            cantidadDisparosActivos++;
            break;
        }
    }
   }

   // MÉTODO: Agrega un disparo de hielo al array de proyectiles
   // Propósito: Permitir que las plantas de hielo agreguen sus proyectiles al juego
   public void agregarDisparoHielo(BolaEscarcha disparo) {
    for (int i = 0; i < disparosHielo.length; i++) {
        if (disparosHielo[i] == null) {
            disparosHielo[i] = disparo;
            cantidadDisparosHieloActivos++;
            break;
        }
    }
   }

   // MÉTODO: Ejecuta la explosión de una cereza explosiva
   // Propósito: Permitir que las cerezas llamen a su explosión desde su propia clase
   public void ejecutarExplosionCereza(CerezaExplosiva cereza, int tickActual) {
    verificarExplosionCereza(cereza, tickActual);
   }

   
   // MÉTODO: Genera zombies Grinch de manera aleatoria controlada
   // Propósito: Crear zombies básicos de forma progresiva durante el juego
   private void generarZombieGrinch() {
    if (Math.random() < 0.01 && zombiesEliminados < zombiesTotales && 
        cantidadZombiesGrinchActivos < zombiesGrinch.length) {
        int fila = (int)(Math.random() * 5);
        ZombieGrinch nuevoZombie = new ZombieGrinch(fila, entorno);
        agregarZombieGrinch(nuevoZombie);
    }
   }

   // MÉTODO: Actualiza el estado de todos los zombies en el juego
   // Propósito: Coordinar la actualización de todos los tipos de zombies
   private void actualizarZombies() {
    actualizarZombiesGrinch();
    actualizarZombiesRapidos();
    actualizarZombiesColosales();
   }

   // MÉTODO: Actualiza el estado de los zombies Grinch
   // Propósito: Mover, dibujar y verificar estado de zombies Grinch
   private void actualizarZombiesGrinch() {
    for (int i = 0; i < zombiesGrinch.length; i++) {
        if (zombiesGrinch[i] != null) {
            zombiesGrinch[i].mover();
            zombiesGrinch[i].dibujar();
           
            if (zombiesGrinch[i].llegoARegalos()) {
                juegoPerdido = true;
            }
           
            if (!zombiesGrinch[i].vivo) {
                eliminarZombieGrinch(i);
                zombiesEliminados++;
            }
        }
    }
   }

   // MÉTODO: Actualiza el estado de los zombies rápidos
   // Propósito: Mover, dibujar y verificar estado de zombies rápidos
   private void actualizarZombiesRapidos() {
    for (int i = 0; i < zombiesRapidos.length; i++) {
        if (zombiesRapidos[i] != null) {
            zombiesRapidos[i].mover();
            zombiesRapidos[i].dibujar();
           
            if (zombiesRapidos[i].llegoARegalos()) {
                juegoPerdido = true;
            }
           
            if (!zombiesRapidos[i].vivo) {
                eliminarZombieRapido(i);
                zombiesEliminados += 2;
            }
        }
    }
   }

   // MÉTODO: Actualiza el estado de los zombies colosales
   // Propósito: Mover, dibujar y verificar estado de zombies colosales
   private void actualizarZombiesColosales() {
    for (int i = 0; i < zombiesColosales.length; i++) {
        if (zombiesColosales[i] != null) {
            zombiesColosales[i].mover();
            zombiesColosales[i].dibujar();
           
            if (zombiesColosales[i].llegoARegalos()) {
                juegoPerdido = true;
            }
           
            if (!zombiesColosales[i].vivo) {
                eliminarZombieColosal(i);
                zombiesEliminados += 5;
            }
        }
    }
   }

  
   // MÉTODO: Actualiza el estado de todos los disparos de fuego
   // Propósito: Mover y eliminar proyectiles de fuego que salen de pantalla
   private void actualizarDisparos() {
    for (int i = 0; i < disparos.length; i++) {
        if (disparos[i] != null) {
            disparos[i].mover();
            disparos[i].dibujar();
            if (!disparos[i].activa) {
                disparos[i] = null;
                cantidadDisparosActivos--;
            }
        }
    }
   }

   // MÉTODO: Actualiza el estado de todos los disparos de hielo
   // Propósito: Mover y eliminar proyectiles de hielo que salen de pantalla
   private void actualizarDisparosHielo() {
    for (int i = 0; i < disparosHielo.length; i++) {
        if (disparosHielo[i] != null) {
            disparosHielo[i].mover();
            disparosHielo[i].dibujar();
            if (!disparosHielo[i].activa) {
                disparosHielo[i] = null;
                cantidadDisparosHieloActivos--;
            }
        }
    }
   }

   // ========== MÉTODOS DE VERIFICACIÓN DE COLISIONES ==========

   // MÉTODO: Coordina la verificación de todas las colisiones de proyectiles de fuego
   // Propósito: Gestionar las colisiones entre bolas de fuego y todos los tipos de zombies
   private void verificarColisiones() {
    verificarColisionesConGrinch();
    verificarColisionesConRapidos();
    verificarColisionesConColosales();
   }

   // MÉTODO: Verifica colisiones entre disparos de fuego y zombies Grinch
   // Propósito: Aplicar daño a zombies Grinch cuando son impactados por bolas de fuego
   private void verificarColisionesConGrinch() {
    for (int i = 0; i < disparos.length; i++) {
        if (disparos[i] != null && disparos[i].activa) {
            for (int j = 0; j < zombiesGrinch.length; j++) {
                if (zombiesGrinch[j] != null && zombiesGrinch[j].vivo &&
                    disparos[i].colisionaConGrinch(zombiesGrinch[j])) {
                    zombiesGrinch[j].recibirDanio();
                    disparos[i].activa = false;
                    disparos[i] = null;
                    cantidadDisparosActivos--;
                    break;
                }
            }
        }
    }
   }

   // MÉTODO: Verifica colisiones entre disparos de fuego y zombies rápidos
   // Propósito: Aplicar daño a zombies rápidos cuando son impactados por bolas de fuego
   private void verificarColisionesConRapidos() {
    for (int i = 0; i < disparos.length; i++) {
        if (disparos[i] != null && disparos[i].activa) {
            for (int j = 0; j < zombiesRapidos.length; j++) {
                if (zombiesRapidos[j] != null && zombiesRapidos[j].vivo) {
                    double distancia = Math.sqrt(Math.pow(disparos[i].x - zombiesRapidos[j].x, 2) +
                                              Math.pow(disparos[i].y - zombiesRapidos[j].y, 2));
                    if (distancia < 40) {
                        zombiesRapidos[j].recibirDanio();
                        disparos[i].activa = false;
                        disparos[i] = null;
                        cantidadDisparosActivos--;
                        break;
                    }
                }
            }
        }
    }
   }

   // MÉTODO: Verifica colisiones entre disparos de fuego y zombies colosales
   // Propósito: Aplicar daño a zombies colosales cuando son impactados por bolas de fuego
   private void verificarColisionesConColosales() {
    for (int i = 0; i < disparos.length; i++) {
        if (disparos[i] != null && disparos[i].activa) {
            for (int j = 0; j < zombiesColosales.length; j++) {
                if (zombiesColosales[j] != null && zombiesColosales[j].vivo) {
                    double distancia = Math.sqrt(Math.pow(disparos[i].x - zombiesColosales[j].x, 2) +
                                              Math.pow(disparos[i].y - zombiesColosales[j].y, 2));
                    if (distancia < 70) {
                        zombiesColosales[j].recibirDanio();
                        disparos[i].activa = false;
                        disparos[i] = null;
                        cantidadDisparosActivos--;
                        break;
                    }
                }
            }
        }
    }
   }

   // MÉTODO: Coordina la verificación de todas las colisiones de proyectiles de hielo
   // Propósito: Gestionar las colisiones entre bolas de hielo y todos los tipos de zombies
   private void verificarColisionesHielo() {
    verificarColisionesHieloConGrinch();
    verificarColisionesHieloConRapidos();
    verificarColisionesHieloConColosales();
   }

   // MÉTODO: Verifica colisiones entre disparos de hielo y zombies Grinch
   // Propósito: Aplicar efecto de ralentización a zombies Grinch
   private void verificarColisionesHieloConGrinch() {
    for (int i = 0; i < disparosHielo.length; i++) {
        if (disparosHielo[i] != null && disparosHielo[i].activa) {
            for (int j = 0; j < zombiesGrinch.length; j++) {
                if (zombiesGrinch[j] != null && zombiesGrinch[j].vivo &&
                    disparosHielo[i].colisionaConGrinch(zombiesGrinch[j])) {
                    zombiesGrinch[j].ralentizar(disparosHielo[i].duracionRalentizacion);
                    disparosHielo[i].activa = false;
                    disparosHielo[i] = null;
                    cantidadDisparosHieloActivos--;
                    break;
                }
            }
        }
    }
   }

   // MÉTODO: Verifica colisiones entre disparos de hielo y zombies rápidos
   // Propósito: Aplicar efecto de ralentización a zombies rápidos
   private void verificarColisionesHieloConRapidos() {
    for (int i = 0; i < disparosHielo.length; i++) {
        if (disparosHielo[i] != null && disparosHielo[i].activa) {
            for (int j = 0; j < zombiesRapidos.length; j++) {
                if (zombiesRapidos[j] != null && zombiesRapidos[j].vivo) {
                    double distancia = Math.sqrt(Math.pow(disparosHielo[i].x - zombiesRapidos[j].x, 2) +
                                              Math.pow(disparosHielo[i].y - zombiesRapidos[j].y, 2));
                    if (distancia < 40) {
                        zombiesRapidos[j].ralentizar(disparosHielo[i].duracionRalentizacion);
                        disparosHielo[i].activa = false;
                        disparosHielo[i] = null;
                        cantidadDisparosHieloActivos--;
                        break;
                    }
                }
            }
        }
    }
   }

   // MÉTODO: Verifica colisiones entre disparos de hielo y zombies colosales
   // Propósito: Aplicar efecto de ralentización a zombies colosales
   private void verificarColisionesHieloConColosales() {
    for (int i = 0; i < disparosHielo.length; i++) {
        if (disparosHielo[i] != null && disparosHielo[i].activa) {
            for (int j = 0; j < zombiesColosales.length; j++) {
                if (zombiesColosales[j] != null && zombiesColosales[j].vivo) {
                    double distancia = Math.sqrt(Math.pow(disparosHielo[i].x - zombiesColosales[j].x, 2) +
                                              Math.pow(disparosHielo[i].y - zombiesColosales[j].y, 2));
                    if (distancia < 70) {
                        zombiesColosales[j].ralentizar(disparosHielo[i].duracionRalentizacion);
                        disparosHielo[i].activa = false;
                        disparosHielo[i] = null;
                        cantidadDisparosHieloActivos--;
                        break;
                    }
                }
            }
        }
    }
   }

   
   // MÉTODO: Maneja la selección y plantado de plantas con el mouse
   // Propósito: Gestionar todo el sistema de drag-and-drop para colocar plantas
   private void manejarSeleccionYPlantado() {
	    if (entorno.sePresionoBoton(entorno.BOTON_IZQUIERDO)) {
	        double mouseX = entorno.mouseX();
	        double mouseY = entorno.mouseY();
	        int tickActual = entorno.numeroDeTick();
	        
	        // Primero verificar si clickeamos una planta ya plantada
	        boolean clickEnPlantaPlantada = false;
	        
	        // Verificar todas las plantas plantadas
	        for (WallNut p : plantasWallnut) {
	            if (p != null && p.plantada && p.encima(mouseX, mouseY)) {
	                // Solo deseleccionar las otras, no esta
	                deseleccionarOtrasPlantas(p);
	                p.seleccionada = true;
	                clickEnPlantaPlantada = true;
	                break;
	            }
	        }
	        
	        if (!clickEnPlantaPlantada) {
	            for (PlantaDeHielo p : plantasHielo) {
	                if (p != null && p.plantada && p.encima(mouseX, mouseY)) {
	                    deseleccionarOtrasPlantas(p);
	                    p.seleccionada = true;
	                    clickEnPlantaPlantada = true;
	                    break;
	                }
	            }
	        }
	        
	        if (!clickEnPlantaPlantada) {
	            for (RoseBlade p : plantasRose) {
	                if (p != null && p.plantada && p.encima(mouseX, mouseY)) {
	                    deseleccionarOtrasPlantas(p);
	                    p.seleccionada = true;
	                    clickEnPlantaPlantada = true;
	                    break;
	                }
	            }
	        }
	        
	        if (!clickEnPlantaPlantada) {
	            for (CerezaExplosiva p : plantasCereza) {
	                if (p != null && p.plantada && p.encima(mouseX, mouseY)) {
	                    deseleccionarOtrasPlantas(p);
	                    p.seleccionada = true;
	                    clickEnPlantaPlantada = true;
	                    break;
	                }
	            }
	        }
	        
	        // Si no clickeamos una planta plantada, manejar banners normalmente
	        if (!clickEnPlantaPlantada) {
	            deseleccionarTodasLasPlantas();
	            
	            if (wallnutBanner != null && wallnutBanner.encima(mouseX, mouseY) && !wallnutBanner.estaEnRecarga(tickActual)) {
	                wallnutBanner.seleccionada = true;
	            }
	            else if (hieloBanner != null && hieloBanner.encima(mouseX, mouseY) && !hieloBanner.estaEnRecarga(tickActual)) {
	                hieloBanner.seleccionada = true;
	            }
	            else if (roseBanner != null && roseBanner.encima(mouseX, mouseY) && !roseBanner.estaEnRecarga(tickActual)) {
	                roseBanner.seleccionada = true;
	            }
	            else if (cerezaBanner != null && cerezaBanner.encima(mouseX, mouseY) && !cerezaBanner.estaEnRecarga(tickActual)) {
	                cerezaBanner.seleccionada = true;
	            }
	        }
	    }
	    
	    // Mover plantas seleccionadas mientras se mantiene el botón presionado
	    if (entorno.estaPresionado(entorno.BOTON_IZQUIERDO)) {
	        moverPlantasSeleccionadas();
	    }
	    
	    // Procesar suelta del botón (plantado o cancelado)
	    if (entorno.seLevantoBoton(entorno.BOTON_IZQUIERDO)) {
	        manejarSueltaBoton();
	    }
	    
	    manejarMovimientoTeclado();
	}

	// MÉTODO: Deselecciona otras plantas pero mantiene una seleccionada
	// Propósito: Permitir selección múltiple pero con foco en una planta específica
	private void deseleccionarOtrasPlantas(Object plantaAMantener) {
	    for (WallNut p : plantasWallnut) {
	        if (p != null && p != plantaAMantener) {
	            p.seleccionada = false;
	        }
	    }
	    for (PlantaDeHielo p : plantasHielo) {
	        if (p != null && p != plantaAMantener) {
	            p.seleccionada = false;
	        }
	    }
	    for (RoseBlade p : plantasRose) {
	        if (p != null && p != plantaAMantener) {
	            p.seleccionada = false;
	        }
	    }
	    for (CerezaExplosiva p : plantasCereza) {
	        if (p != null && p != plantaAMantener) {
	            p.seleccionada = false;
	        }
	    }
	}
	
	// MÉTODO: Deselecciona todas las plantas del juego
	// Propósito: Limpiar selecciones cuando se hace click en áreas vacías
	private void deseleccionarTodasLasPlantas() {
	    for (WallNut p : plantasWallnut) {
	        if (p != null) p.seleccionada = false;
	    }
	    for (PlantaDeHielo p : plantasHielo) {
	        if (p != null) p.seleccionada = false;
	    }
	    for (RoseBlade p : plantasRose) {
	        if (p != null) p.seleccionada = false;
	    }
	    for (CerezaExplosiva p : plantasCereza) {
	        if (p != null) p.seleccionada = false;
	    }
	}

	// MÉTODO: Selecciona una planta ya plantada con el mouse
	// Propósito: Permitir al jugador seleccionar plantas existentes para moverlas
	private void seleccionarPlantaPlantada(double mouseX, double mouseY) {
	    for (WallNut p : plantasWallnut) {
	        if (p != null && p.plantada && p.encima(mouseX, mouseY)) {
	            p.seleccionada = true;
	            return;
	        }
	    }
	    for (PlantaDeHielo p : plantasHielo) {
	        if (p != null && p.plantada && p.encima(mouseX, mouseY)) {
	            p.seleccionada = true;
	            return;
	        }
	    }
	    for (RoseBlade p : plantasRose) {
	        if (p != null && p.plantada && p.encima(mouseX, mouseY)) {
	            p.seleccionada = true;
	            return;
	        }
	    }
	    for (CerezaExplosiva p : plantasCereza) {
	        if (p != null && p.plantada && p.encima(mouseX, mouseY)) {
	            p.seleccionada = true;
	            return;
	        }
	    }
	}

	// MÉTODO: Mueve las plantas seleccionadas siguiendo el cursor del mouse
	// Propósito: Implementar el arrastre de plantas durante la selección
	private void moverPlantasSeleccionadas() {
	    for (WallNut p : plantasWallnut) {
	        if (p != null && p.seleccionada) {
	            p.x = entorno.mouseX();
	            p.y = entorno.mouseY();
	        }
	    }
	    for (PlantaDeHielo p : plantasHielo) {
	        if (p != null && p.seleccionada) {
	            p.x = entorno.mouseX();
	            p.y = entorno.mouseY();
	        }
	    }
	    for (RoseBlade p : plantasRose) {
	        if (p != null && p.seleccionada) {
	            p.x = entorno.mouseX();
	            p.y = entorno.mouseY();
	        }
	    }
	    for (CerezaExplosiva p : plantasCereza) {
	        if (p != null && p.seleccionada) {
	            p.x = entorno.mouseX();
	            p.y = entorno.mouseY();
	        }
	    }
	}

	// MÉTODO: Maneja la lógica cuando se suelta el botón del mouse
	// Propósito: Procesar el plantado o cancelación de plantas arrastradas
	private void manejarSueltaBoton() {
	    int tickActual = entorno.numeroDeTick();
	    
	    if (wallnutBanner != null && wallnutBanner.seleccionada) {
	        manejarSueltaWallnutBanner(tickActual);
	    }
	    else if (hieloBanner != null && hieloBanner.seleccionada) {
	        manejarSueltaHieloBanner(tickActual);
	    }
	    else if (roseBanner != null && roseBanner.seleccionada) {
	        manejarSueltaRoseBanner(tickActual);
	    }
	    else if (cerezaBanner != null && cerezaBanner.seleccionada) {
	        manejarSueltaCerezaBanner(tickActual);
	    }
	    else {
	        manejarSueltaPlantaExistente();
	    }
	}

	// MÉTODO: Maneja la suelta de una WallNut del banner
	// Propósito: Plantar una nueva WallNut o cancelar la operación
	private void manejarSueltaWallnutBanner(int tickActual) {
	    if (entorno.mouseY() < 70) {
	        // Cancelar: volver al banner
	        wallnutBanner.x = wallnutBanner.xInicial;
	        wallnutBanner.y = wallnutBanner.yInicial;
	    } else {
	        // Intentar plantar en la cuadrícula
	        int indiceX = cuadricula.cercanoL(entorno.mouseX(), entorno.mouseY()).x;
	        int indiceY = cuadricula.cercanoL(entorno.mouseX(), entorno.mouseY()).y;
	        
	        if (indiceX > 0 && !cuadricula.estaOcupado(indiceX, indiceY)) {
	            WallNut nuevaPlanta = new WallNut(cuadricula.coorX[indiceX], cuadricula.coorY[indiceY], entorno);
	            
	            if (agregarWallnut(nuevaPlanta)) {
	                nuevaPlanta.plantada = true;
	                cuadricula.Ocupado(indiceX, indiceY, true);
	                wallnutBanner.usar(tickActual);
	            }
	        }
	        wallnutBanner.x = wallnutBanner.xInicial;
	        wallnutBanner.y = wallnutBanner.yInicial;
	    }
	    wallnutBanner.seleccionada = false;
	}

	// MÉTODO: Maneja la suelta de una PlantaDeHielo del banner
	// Propósito: Plantar una nueva planta de hielo o cancelar la operación
	private void manejarSueltaHieloBanner(int tickActual) {
	    if (entorno.mouseY() < 70) {
	        hieloBanner.x = hieloBanner.xInicial;
	        hieloBanner.y = hieloBanner.yInicial;
	    } else {
	        int indiceX = cuadricula.cercanoL(entorno.mouseX(), entorno.mouseY()).x;
	        int indiceY = cuadricula.cercanoL(entorno.mouseX(), entorno.mouseY()).y;
	        
	        if (indiceX > 0 && !cuadricula.estaOcupado(indiceX, indiceY)) {
	            PlantaDeHielo nuevaPlanta = new PlantaDeHielo(cuadricula.coorX[indiceX], cuadricula.coorY[indiceY], entorno);
	            
	            if (agregarPlantaHielo(nuevaPlanta)) {
	                nuevaPlanta.plantada = true;
	                cuadricula.Ocupado(indiceX, indiceY, true);
	                hieloBanner.usar(tickActual);
	            }
	        }
	        hieloBanner.x = hieloBanner.xInicial;
	        hieloBanner.y = hieloBanner.yInicial;
	    }
	    hieloBanner.seleccionada = false;
	}

	// MÉTODO: Maneja la suelta de una RoseBlade del banner
	// Propósito: Plantar una nueva RoseBlade o cancelar la operación
	private void manejarSueltaRoseBanner(int tickActual) {
	    if (entorno.mouseY() < 70) {
	        roseBanner.x = roseBanner.xInicial;
	        roseBanner.y = roseBanner.yInicial;
	    } else {
	        int indiceX = cuadricula.cercanoL(entorno.mouseX(), entorno.mouseY()).x;
	        int indiceY = cuadricula.cercanoL(entorno.mouseX(), entorno.mouseY()).y;
	        
	        if (indiceX > 0 && !cuadricula.estaOcupado(indiceX, indiceY)) {
	            RoseBlade nuevaPlanta = new RoseBlade(cuadricula.coorX[indiceX], cuadricula.coorY[indiceY], entorno);
	            
	            if (agregarRoseBlade(nuevaPlanta)) {
	                nuevaPlanta.plantada = true;
	                cuadricula.Ocupado(indiceX, indiceY, true);
	                roseBanner.usar(tickActual);
	            }
	        }
	        roseBanner.x = roseBanner.xInicial;
	        roseBanner.y = roseBanner.yInicial;
	    }
	    roseBanner.seleccionada = false;
	}

	// MÉTODO: Maneja la suelta de una CerezaExplosiva del banner
	// Propósito: Plantar una nueva cereza o cancelar la operación
	private void manejarSueltaCerezaBanner(int tickActual) {
	    if (entorno.mouseY() < 70) {
	        cerezaBanner.x = cerezaBanner.xInicial;
	        cerezaBanner.y = cerezaBanner.yInicial;
	    } else {
	        int indiceX = cuadricula.cercanoL(entorno.mouseX(), entorno.mouseY()).x;
	        int indiceY = cuadricula.cercanoL(entorno.mouseX(), entorno.mouseY()).y;
	        
	        if (indiceX > 0 && !cuadricula.estaOcupado(indiceX, indiceY)) {
	            CerezaExplosiva nuevaPlanta = new CerezaExplosiva(cuadricula.coorX[indiceX], cuadricula.coorY[indiceY], entorno);
	            
	            if (agregarCerezaExplosiva(nuevaPlanta)) {
	                nuevaPlanta.plantada = true;
	                cuadricula.Ocupado(indiceX, indiceY, true);
	                cerezaBanner.usar(tickActual);
	            }
	        }
	        cerezaBanner.x = cerezaBanner.xInicial;
	        cerezaBanner.y = cerezaBanner.yInicial;
	    }
	    cerezaBanner.seleccionada = false;
	}

	// MÉTODO: Maneja la suelta de una planta ya existente (reposicionamiento)
	// Propósito: Permitir mover plantas ya plantadas a nuevas posiciones
	private void manejarSueltaPlantaExistente() {
	    // Buscar y manejar WallNuts seleccionadas
	    for (int i = 0; i < plantasWallnut.length; i++) {
	        if (plantasWallnut[i] != null && plantasWallnut[i].plantada && plantasWallnut[i].seleccionada) {
	            manejarSueltaWallnutExistente(plantasWallnut[i]);
	            return;
	        }
	    }
	    
	    // Buscar y manejar Plantas de Hielo seleccionadas
	    for (int i = 0; i < plantasHielo.length; i++) {
	        if (plantasHielo[i] != null && plantasHielo[i].plantada && plantasHielo[i].seleccionada) {
	            manejarSueltaPlantaHieloExistente(plantasHielo[i]);
	            return;
	        }
	    }
	    
	    // Buscar y manejar RoseBlades seleccionadas
	    for (int i = 0; i < plantasRose.length; i++) {
	        if (plantasRose[i] != null && plantasRose[i].plantada && plantasRose[i].seleccionada) {
	            manejarSueltaRoseBladeExistente(plantasRose[i]);
	            return;
	        }
	    }
	    
	    // Buscar y manejar Cerezas seleccionadas
	    for (int i = 0; i < plantasCereza.length; i++) {
	        if (plantasCereza[i] != null && plantasCereza[i].plantada && plantasCereza[i].seleccionada) {
	            manejarSueltaCerezaExistente(plantasCereza[i]);
	            return;
	        }
	    }
	}

	// MÉTODO: Maneja el reposicionamiento de una WallNut existente
	// Propósito: Validar y ejecutar el movimiento de una WallNut a nueva posición
	private void manejarSueltaWallnutExistente(WallNut planta) {
	    int indiceX = cuadricula.cercanoL(planta.x, planta.y).x;
	    int indiceY = cuadricula.cercanoL(planta.x, planta.y).y;
	    
	    int indiceXAnterior = cuadricula.cercanoL(planta.xInicial, planta.yInicial).x;
	    int indiceYAnterior = cuadricula.cercanoL(planta.xInicial, planta.yInicial).y;
	    cuadricula.Ocupado(indiceXAnterior, indiceYAnterior, false);
	    
	    if (indiceX > 0 && !cuadricula.estaOcupado(indiceX, indiceY)) {
	        cuadricula.centrarWallnut(planta, indiceX, indiceY);
	        cuadricula.Ocupado(indiceX, indiceY, true);
	        planta.xInicial = planta.x;
	        planta.yInicial = planta.y;
	    } else {
	        planta.x = planta.xInicial;
	        planta.y = planta.yInicial;
	        cuadricula.Ocupado(indiceXAnterior, indiceYAnterior, true);
	    }
	}

	// MÉTODO: Maneja el reposicionamiento de una PlantaDeHielo existente
	// Propósito: Validar y ejecutar el movimiento de una planta de hielo a nueva posición
	private void manejarSueltaPlantaHieloExistente(PlantaDeHielo planta) {
	    int indiceX = cuadricula.cercanoL(planta.x, planta.y).x;
	    int indiceY = cuadricula.cercanoL(planta.x, planta.y).y;
	    
	    int indiceXAnterior = cuadricula.cercanoL(planta.xInicial, planta.yInicial).x;
	    int indiceYAnterior = cuadricula.cercanoL(planta.xInicial, planta.yInicial).y;
	    cuadricula.Ocupado(indiceXAnterior, indiceYAnterior, false);
	    
	    if (indiceX > 0 && !cuadricula.estaOcupado(indiceX, indiceY)) {
	        cuadricula.centrarPlantaHielo(planta, indiceX, indiceY);
	        cuadricula.Ocupado(indiceX, indiceY, true);
	        planta.xInicial = planta.x;
	        planta.yInicial = planta.y;
	    } else {
	        planta.x = planta.xInicial;
	        planta.y = planta.yInicial;
	        cuadricula.Ocupado(indiceXAnterior, indiceYAnterior, true);
	    }
	}

	// MÉTODO: Maneja el reposicionamiento de una RoseBlade existente
	// Propósito: Validar y ejecutar el movimiento de una RoseBlade a nueva posición
	private void manejarSueltaRoseBladeExistente(RoseBlade planta) {
	    int indiceX = cuadricula.cercanoL(planta.x, planta.y).x;
	    int indiceY = cuadricula.cercanoL(planta.x, planta.y).y;
	    
	    int indiceXAnterior = cuadricula.cercanoL(planta.xInicial, planta.yInicial).x;
	    int indiceYAnterior = cuadricula.cercanoL(planta.xInicial, planta.yInicial).y;
	    cuadricula.Ocupado(indiceXAnterior, indiceYAnterior, false);
	    
	    if (indiceX > 0 && !cuadricula.estaOcupado(indiceX, indiceY)) {
	        cuadricula.centrarRoseBlade(planta, indiceX, indiceY);
	        cuadricula.Ocupado(indiceX, indiceY, true);
	        planta.xInicial = planta.x;
	        planta.yInicial = planta.y;
	    } else {
	        planta.x = planta.xInicial;
	        planta.y = planta.yInicial;
	        cuadricula.Ocupado(indiceXAnterior, indiceYAnterior, true);
	    }
	}

	// MÉTODO: Maneja el reposicionamiento de una CerezaExplosiva existente
	// Propósito: Validar y ejecutar el movimiento de una cereza a nueva posición
	private void manejarSueltaCerezaExistente(CerezaExplosiva planta) {
	    int indiceX = cuadricula.cercanoL(planta.x, planta.y).x;
	    int indiceY = cuadricula.cercanoL(planta.x, planta.y).y;
	    
	    int indiceXAnterior = cuadricula.cercanoL(planta.xInicial, planta.yInicial).x;
	    int indiceYAnterior = cuadricula.cercanoL(planta.xInicial, planta.yInicial).y;
	    cuadricula.Ocupado(indiceXAnterior, indiceYAnterior, false);
	    
	    if (indiceX > 0 && !cuadricula.estaOcupado(indiceX, indiceY)) {
	        cuadricula.centrarCereza(planta, indiceX, indiceY);
	        cuadricula.Ocupado(indiceX, indiceY, true);
	        planta.xInicial = planta.x;
	        planta.yInicial = planta.y;
	    } else {
	        planta.x = planta.xInicial;
	        planta.y = planta.yInicial;
	        cuadricula.Ocupado(indiceXAnterior, indiceYAnterior, true);
	    }
	}

	

	// MÉTODO: Dibuja la interfaz de usuario del juego
	// Propósito: Mostrar información importante como contadores y tiempo
	private void dibujarUI() {
	    entorno.cambiarFont("Arial", 20, Color.WHITE);
	    entorno.escribirTexto("Zombies: " + zombiesEliminados + "/" + zombiesTotales, 400, 40);
	    entorno.escribirTexto("Tiempo: " + entorno.tiempo()/1000 + "s", 400, 80);
	   
	    int zombiesEnPantalla = cantidadZombiesGrinchActivos + cantidadZombiesRapidosActivos + cantidadZombiesColosalesActivos;
	    entorno.escribirTexto("Zombies en pantalla: " + zombiesEnPantalla, 400, 60);
	}

	// MÉTODO: Verifica las condiciones de fin del juego
	// Propósito: Determinar si el jugador ganó o perdió
	private void verificarFinJuego() {
	    if (zombiesEliminados >= zombiesTotales) {
	        juegoGanado = true;
	    }
	}

	// MÉTODO: Dibuja la pantalla de fin del juego
	// Propósito: Mostrar mensaje de victoria o derrota con estadísticas
	private void dibujarPantallaFin() {
	    entorno.colorFondo(Color.BLACK);
	    if (juegoGanado) {
	        entorno.cambiarFont("Arial", 40, Color.GREEN);
	        entorno.escribirTexto("¡VICTORIA!", 400, 300);
	        entorno.cambiarFont("Arial", 24, Color.WHITE);
	        entorno.escribirTexto("Zombies eliminados: " + zombiesEliminados, 400, 350);
	        entorno.escribirTexto("Tiempo: " + entorno.tiempo()/1000 + " segundos", 400, 380);
	        entorno.escribirTexto("Rápidos derrotados: " + rapidosAparecidos, 400, 410);
	        entorno.escribirTexto("Colosales derrotados: " + colosalesAparecidos, 400, 440);
	    } else if (juegoPerdido) {
	        entorno.cambiarFont("Arial", 40, Color.RED);
	        entorno.escribirTexto("¡DERROTA!", 400, 300);
	        entorno.cambiarFont("Arial", 24, Color.WHITE);
	        entorno.escribirTexto("Zombies eliminados: " + zombiesEliminados, 400, 350);
	    }
	}
   
	
	// Propósito: Iniciar la aplicación y crear la instancia del juego
	public static void main(String[] args) {
	    new Juego();
	}
}   