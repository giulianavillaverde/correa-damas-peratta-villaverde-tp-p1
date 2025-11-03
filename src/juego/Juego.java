package juego;

import java.awt.Color;
import entorno.Entorno;
import entorno.InterfaceJuego;

public class Juego extends InterfaceJuego {

   private Entorno entorno;
   private Banner banner;
   private cuadricula cuadricula;
   private regalos[] regalo;
   
   // Arrays separados para cada tipo de planta
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
      
       // Inicializar arrays separados
       this.plantasWallnut = new WallNut[10];
       this.plantasHielo = new PlantaDeHielo[10];
       this.plantasRose = new RoseBlade[10];
       this.plantasCereza = new CerezaExplosiva[10];
       
       this.contadorPlantas = 0;
       this.cantidadWallnutActivas = 0;
       this.cantidadHieloActivas = 0;
       this.cantidadRoseActivas = 0;
       this.cantidadCerezaActivas = 0;
       this.zombiesGrinch = new ZombieGrinch[5];
       this.zombiesRapidos = new ZombieRapido[2] ;
       this.zombiesColosales = new ZombieColosal[1];
       this.disparos = new BolaFuego[50];
       this.disparosHielo = new BolaEscarcha[50];
       this.bolasNieve = new BolaNieve[30];
       this.zombiesEliminados = 0;
       this.zombiesTotales = 80;
       this.juegoGanado = false;
       this.juegoPerdido = false;
       this.ticksParaProximoRapido = 270;
       this.ticksParaProximoColosal = 1080;
       this.rapidosAparecidos = 0;
       this.colosalesAparecidos = 0;
      
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
    
   private void crearPlantasBanner() {
       this.wallnutBanner = new WallNut(50, 40, entorno);
       this.hieloBanner = new PlantaDeHielo(150, 40, entorno);
       this.roseBanner = new RoseBlade(250, 40, entorno);
       this.cerezaBanner = new CerezaExplosiva(350, 40, entorno);
      
       agregarWallnut(wallnutBanner);
       agregarPlantaHielo(hieloBanner);
       agregarRoseBlade(roseBanner);
       agregarCerezaExplosiva(cerezaBanner);
   }
   
   private void crearZombiesBanner() {
       this.zombieGrinchBanner = new ZombieGrinch(0, entorno);
       this.zombieRapidoBanner = new ZombieRapido(0, entorno);
       this.zombieColosalBanner = new ZombieColosal(entorno);
       
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
   
   // Métodos para agregar plantas a sus arrays correspondientes
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
   
   // Métodos para eliminar plantas
   private void eliminarWallnut(int indice) {
       if (plantasWallnut[indice] != null) {
           plantasWallnut[indice] = null;
           cantidadWallnutActivas--;
       }
   }
   
   private void eliminarPlantaHielo(int indice) {
       if (plantasHielo[indice] != null) {
           plantasHielo[indice] = null;
           cantidadHieloActivas--;
       }
   }
   
   private void eliminarRoseBlade(int indice) {
       if (plantasRose[indice] != null) {
           plantasRose[indice] = null;
           cantidadRoseActivas--;
       }
   }
   
   private void eliminarCerezaExplosiva(int indice) {
       if (plantasCereza[indice] != null) {
           plantasCereza[indice] = null;
           cantidadCerezaActivas--;
       }
   }
   
   // Métodos para agregar zombies
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
   
   // Métodos para eliminar zombies
   private void eliminarZombieGrinch(int indice) {
       if (zombiesGrinch[indice] != null) {
           zombiesGrinch[indice] = null;
           cantidadZombiesGrinchActivos--;
       }
   }

   private void eliminarZombieRapido(int indice) {
       if (zombiesRapidos[indice] != null) {
           zombiesRapidos[indice] = null;
           cantidadZombiesRapidosActivos--;
       }
   }

   private void eliminarZombieColosal(int indice) {
       if (zombiesColosales[indice] != null) {
           zombiesColosales[indice] = null;
           cantidadZombiesColosalesActivos--;
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
      
       actualizarPlantas();
       dibujarPlantasBanner();
       dibujarZombiesBanner();
      
       generarZombieGrinch();
       generarZombieRapido();
       generarZombieColosal();
       actualizarZombies();
       actualizarDisparos();
       actualizarDisparosHielo();
       actualizarBolasNieve();
       verificarColisiones();
       verificarColisionesHielo();
       verificarColisionesBolasNieve();
       verificarAtaquesZombies();
       generarDisparosZombies();
       manejarSeleccionYPlantado();
       manejarMovimientoTeclado();
       verificarFinJuego();
       dibujarUI();
       dibujarBarrasRecarga();
   }

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
            entorno.escribirTexto("Rápido", zombieRapidoBanner.x - 15, zombieRapidoBanner.y + 45);
        }
        
        if (zombieColosalBanner != null && zombieColosalBanner.imagen != null) {
            zombieColosalBanner.x = 970;
            zombieColosalBanner.y = 40;
            entorno.dibujarImagen(zombieColosalBanner.imagen, 
                                zombieColosalBanner.x, zombieColosalBanner.y, 0, 0.1);
            entorno.cambiarFont("Arial", 12, Color.WHITE);
            entorno.escribirTexto("Colosal", zombieColosalBanner.x - 15, zombieColosalBanner.y + 45);
        }
    }

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

   private void agregarBolaNieve(BolaNieve bola) {
       for (int i = 0; i < bolasNieve.length; i++) {
           if (bolasNieve[i] == null) {
               bolasNieve[i] = bola;
               cantidadBolasNieveActivas++;
               break;
           }
       }
   }

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
                //bolasNieve[i] = null;
                cantidadBolasNieveActivas--;
                break;
            }
        }
    }

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
               // bolasNieve[i] = null;
                cantidadBolasNieveActivas--;
                break;
            }
        }
    }

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
               // bolasNieve[i] = null;
                cantidadBolasNieveActivas--;
                break;
            }
        }
    }

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
                //bolasNieve[i] = null;
                cantidadBolasNieveActivas--;
                break;
            }
        }
    }

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

   private void eliminarCerezaExplosivaDeArray(CerezaExplosiva cereza) {
        for (int i = 0; i < plantasCereza.length; i++) {
            if (plantasCereza[i] == cereza) {
                eliminarCerezaExplosiva(i);
                break;
            }
        }
    }
  
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
   
   private void manejarMovimientoTeclado() {
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
        
        // Mover la planta seleccionada
        if (wallnutSeleccionada != null) {
            moverWallnut(wallnutSeleccionada);
        } else if (hieloSeleccionada != null) {
            moverPlantaHielo(hieloSeleccionada);
        } else if (roseSeleccionada != null) {
            moverRoseBlade(roseSeleccionada);
        } else if (cerezaSeleccionada != null) {
            moverCerezaExplosiva(cerezaSeleccionada);
        }
    }

    private void moverWallnut(WallNut planta) {
        double velocidad = 105;
        double nuevaX = planta.x;
        double nuevaY = planta.y;
        
        if (entorno.sePresiono(entorno.TECLA_ARRIBA) || entorno.sePresiono('w')) {
            nuevaY -= velocidad;
        }
        if (entorno.sePresiono(entorno.TECLA_ABAJO) || entorno.sePresiono('s')) {
            nuevaY += velocidad;
        }
        if (entorno.sePresiono(entorno.TECLA_IZQUIERDA) || entorno.sePresiono('a')) {
            nuevaX -= velocidad;
        }
        if (entorno.sePresiono(entorno.TECLA_DERECHA) || entorno.sePresiono('d')) {
            nuevaX += velocidad;
        }
        
        boolean enBanner = nuevaY < 100;
        boolean enPrimeraColumna = nuevaX < 100;
        boolean dentroDeLimites = nuevaX >= 30 && nuevaX <= 1000 && nuevaY >= 30 && nuevaY <= 550;
        
        if (!enBanner && !enPrimeraColumna && dentroDeLimites) {
            planta.x = nuevaX;
            planta.y = nuevaY;
        }
    }

    private void moverPlantaHielo(PlantaDeHielo planta) {
        double velocidad = 105;
        double nuevaX = planta.x;
        double nuevaY = planta.y;
        
        if (entorno.sePresiono(entorno.TECLA_ARRIBA) || entorno.sePresiono('w')) {
            nuevaY -= velocidad;
        }
        if (entorno.sePresiono(entorno.TECLA_ABAJO) || entorno.sePresiono('s')) {
            nuevaY += velocidad;
        }
        if (entorno.sePresiono(entorno.TECLA_IZQUIERDA) || entorno.sePresiono('a')) {
            nuevaX -= velocidad;
        }
        if (entorno.sePresiono(entorno.TECLA_DERECHA) || entorno.sePresiono('d')) {
            nuevaX += velocidad;
        }
        
        boolean enBanner = nuevaY < 100;
        boolean enPrimeraColumna = nuevaX < 100;
        boolean dentroDeLimites = nuevaX >= 30 && nuevaX <= 1000 && nuevaY >= 30 && nuevaY <= 550;
        
        if (!enBanner && !enPrimeraColumna && dentroDeLimites) {
            planta.x = nuevaX;
            planta.y = nuevaY;
        }
    }

    private void moverRoseBlade(RoseBlade planta) {
        double velocidad = 105;
        double nuevaX = planta.x;
        double nuevaY = planta.y;
        
        if (entorno.sePresiono(entorno.TECLA_ARRIBA) || entorno.sePresiono('w')) {
            nuevaY -= velocidad;
        }
        if (entorno.sePresiono(entorno.TECLA_ABAJO) || entorno.sePresiono('s')) {
            nuevaY += velocidad;
        }
        if (entorno.sePresiono(entorno.TECLA_IZQUIERDA) || entorno.sePresiono('a')) {
            nuevaX -= velocidad;
        }
        if (entorno.sePresiono(entorno.TECLA_DERECHA) || entorno.sePresiono('d')) {
            nuevaX += velocidad;
        }
        
        boolean enBanner = nuevaY < 100;
        boolean enPrimeraColumna = nuevaX < 100;
        boolean dentroDeLimites = nuevaX >= 30 && nuevaX <= 1000 && nuevaY >= 30 && nuevaY <= 550;
        
        if (!enBanner && !enPrimeraColumna && dentroDeLimites) {
            planta.x = nuevaX;
            planta.y = nuevaY;
        }
    }

    private void moverCerezaExplosiva(CerezaExplosiva planta) {
        double velocidad = 105;
        double nuevaX = planta.x;
        double nuevaY = planta.y;
        
        if (entorno.sePresiono(entorno.TECLA_ARRIBA) || entorno.sePresiono('w')) {
            nuevaY -= velocidad;
        }
        if (entorno.sePresiono(entorno.TECLA_ABAJO) || entorno.sePresiono('s')) {
            nuevaY += velocidad;
        }
        if (entorno.sePresiono(entorno.TECLA_IZQUIERDA) || entorno.sePresiono('a')) {
            nuevaX -= velocidad;
        }
        if (entorno.sePresiono(entorno.TECLA_DERECHA) || entorno.sePresiono('d')) {
            nuevaX += velocidad;
        }
        
        boolean enBanner = nuevaY < 100;
        boolean enPrimeraColumna = nuevaX < 100;
        boolean dentroDeLimites = nuevaX >= 30 && nuevaX <= 1000 && nuevaY >= 30 && nuevaY <= 550;
        
        if (!enBanner && !enPrimeraColumna && dentroDeLimites) {
            planta.x = nuevaX;
            planta.y = nuevaY;
        }
    }

    private void verificarAtaquesZombies() {
        int tickActual = entorno.numeroDeTick();
        
        // Ataques de zombies Grinch
        for (int i = 0; i < zombiesGrinch.length; i++) {
            if (zombiesGrinch[i] != null && zombiesGrinch[i].vivo) {
                zombiesGrinch[i].verificarPlantaBloqueadora();
                
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
                zombiesRapidos[i].verificarPlantaBloqueadora();
                
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

   private boolean WallnutPlantada(WallNut planta) {
	    return planta.plantada;
	}

	private boolean PlantaHieloPlantada(PlantaDeHielo planta) {
	    return planta.plantada;
	}

	private boolean estaRoseBladePlantada(RoseBlade planta) {
	    return planta.plantada;
	}

	private boolean estaCerezaPlantada(CerezaExplosiva planta) {
	    return planta.plantada;
	}

	private void aplicarAtaqueWallnut(WallNut planta, int tickActual) {
	    planta.recibirAtaque(tickActual);
	}

	private void aplicarAtaquePlantaHielo(PlantaDeHielo planta, int tickActual) {
	    planta.recibirAtaque(tickActual);
	}

	private void aplicarAtaqueRoseBlade(RoseBlade planta, int tickActual) {
	    planta.recibirAtaque(tickActual);
	}

	private void aplicarAtaqueCereza(CerezaExplosiva planta, int tickActual) {
	    planta.recibirAtaque(tickActual);
	}

	private void liberarYLimpiarWallnut(WallNut planta) {
	    int indiceX = cuadricula.cercanoL(planta.x, planta.y).x;
	    int indiceY = cuadricula.cercanoL(planta.x, planta.y).y;
	    cuadricula.Ocupado(indiceX, indiceY, false);
	    eliminarWallnutDeArray(planta);
	}

	private void liberarYLimpiarPlantaHielo(PlantaDeHielo planta) {
	    int indiceX = cuadricula.cercanoL(planta.x, planta.y).x;
	    int indiceY = cuadricula.cercanoL(planta.x, planta.y).y;
	    cuadricula.Ocupado(indiceX, indiceY, false);
	    eliminarPlantaHieloDeArray(planta);
	}

	private void liberarYLimpiarRoseBlade(RoseBlade planta) {
	    int indiceX = cuadricula.cercanoL(planta.x, planta.y).x;
	    int indiceY = cuadricula.cercanoL(planta.x, planta.y).y;
	    cuadricula.Ocupado(indiceX, indiceY, false);
	    eliminarRoseBladeDeArray(planta);
	}

	private void liberarYLimpiarCereza(CerezaExplosiva planta) {
	    int indiceX = cuadricula.cercanoL(planta.x, planta.y).x;
	    int indiceY = cuadricula.cercanoL(planta.x, planta.y).y;
	    cuadricula.Ocupado(indiceX, indiceY, false);
	    eliminarCerezaExplosivaDeArray(planta);
	}

    private void eliminarWallnutDeArray(WallNut wallnut) {
        for (int i = 0; i < plantasWallnut.length; i++) {
            if (plantasWallnut[i] == wallnut) {
                eliminarWallnut(i);
                break;
            }
        }
    }

    private void eliminarPlantaHieloDeArray(PlantaDeHielo planta) {
        for (int i = 0; i < plantasHielo.length; i++) {
            if (plantasHielo[i] == planta) {
                eliminarPlantaHielo(i);
                break;
            }
        }
    }

    private void eliminarRoseBladeDeArray(RoseBlade rose) {
        for (int i = 0; i < plantasRose.length; i++) {
            if (plantasRose[i] == rose) {
                eliminarRoseBlade(i);
                break;
            }
        }
    }

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

    private void dibujarBarraRecargaWallnut(double x, double y, double porcentaje, int tickActual) {
        Color colorFondo = new Color(139, 69, 19);
        Color colorBorde = new Color(160, 82, 45);
        dibujarBarraRecargaCompleta(x, y, porcentaje, colorFondo, colorBorde, "WallNut", wallnutBanner, tickActual);
    }

    private void dibujarBarraRecargaPlantaHielo(double x, double y, double porcentaje, int tickActual) {
        Color colorFondo = new Color(135, 206, 250);
        Color colorBorde = new Color(173, 216, 230);
        dibujarBarraRecargaCompleta(x, y, porcentaje, colorFondo, colorBorde, "Escarchi", hieloBanner, tickActual);
    }

    private void dibujarBarraRecargaRoseBlade(double x, double y, double porcentaje, int tickActual) {
        Color colorFondo = new Color(255, 0, 0);
        Color colorBorde = new Color(255, 69, 0);
        dibujarBarraRecargaCompleta(x, y, porcentaje, colorFondo, colorBorde, "RoseBlade", roseBanner, tickActual);
    }

    private void dibujarBarraRecargaCereza(double x, double y, double porcentaje, int tickActual) {
        Color colorFondo = new Color(148, 0, 211);
        Color colorBorde = new Color(186, 85, 211);
        dibujarBarraRecargaCompleta(x, y, porcentaje, colorFondo, colorBorde, "Cereza", cerezaBanner, tickActual);
    }
   
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
   
   private Color aclararColor(Color color, float factor) {
        int rojo = Math.min(255, (int)(color.getRed() + (255 - color.getRed()) * factor));
        int verde = Math.min(255, (int)(color.getGreen() + (255 - color.getGreen()) * factor));
        int azul = Math.min(255, (int)(color.getBlue() + (255 - color.getBlue()) * factor));
        return new Color(rojo, verde, azul);
    }

    private Color oscurecerColor(Color color, float factor) {
        int rojo = Math.max(0, (int)(color.getRed() * factor));
        int verde = Math.max(0, (int)(color.getGreen() * factor));
        int azul = Math.max(0, (int)(color.getBlue() * factor));
        return new Color(rojo, verde, azul);
    }

    private boolean esColorOscuro(Color color) {
        double luminosidad = (0.299 * color.getRed() + 0.587 * color.getGreen() + 0.114 * color.getBlue()) / 255;
        return luminosidad < 0.5;
    }

    private int calcularSegundosRestantesWallnut(WallNut planta, int tickActual) {
        int tiempoTranscurrido = tickActual - planta.tiempoUltimoPlantado;
        int tiempoRestante = Math.max(0, planta.tiempoRecargaPlantado - tiempoTranscurrido);
        return (int) Math.ceil(tiempoRestante / 50.0);
    }

    private int calcularSegundosRestantesPlantaHielo(PlantaDeHielo planta, int tickActual) {
        int tiempoTranscurrido = tickActual - planta.tiempoUltimoPlantado;
        int tiempoRestante = Math.max(0, planta.tiempoRecargaPlantado - tiempoTranscurrido);
        return (int) Math.ceil(tiempoRestante / 50.0);
    }

    private int calcularSegundosRestantesRoseBlade(RoseBlade planta, int tickActual) {
        int tiempoTranscurrido = tickActual - planta.tiempoUltimoPlantado;
        int tiempoRestante = Math.max(0, planta.tiempoRecargaPlantado - tiempoTranscurrido);
        return (int) Math.ceil(tiempoRestante / 50.0);
    }

    private int calcularSegundosRestantesCereza(CerezaExplosiva planta, int tickActual) {
        int tiempoTranscurrido = tickActual - planta.tiempoUltimoPlantado;
        int tiempoRestante = Math.max(0, planta.tiempoRecargaPlantado - tiempoTranscurrido);
        return (int) Math.ceil(tiempoRestante / 50.0);
    }

   
   public void agregarDisparo(BolaFuego disparo) {
       for (int i = 0; i < disparos.length; i++) {
           if (disparos[i] == null) {
               disparos[i] = disparo;
               cantidadDisparosActivos++;
               break;
           }
       }
   }
  
   public void agregarDisparoHielo(BolaEscarcha disparo) {
       for (int i = 0; i < disparosHielo.length; i++) {
           if (disparosHielo[i] == null) {
               disparosHielo[i] = disparo;
               cantidadDisparosHieloActivos++;
               break;
           }
       }
   }
  
   public void ejecutarExplosionCereza(CerezaExplosiva cereza, int tickActual) {
       verificarExplosionCereza(cereza, tickActual);
   }
  
   private void generarZombieGrinch() {
       if (Math.random() < 0.01 && zombiesEliminados < zombiesTotales && 
           cantidadZombiesGrinchActivos < zombiesGrinch.length) {
           int fila = (int)(Math.random() * 5);
           ZombieGrinch nuevoZombie = new ZombieGrinch(fila, entorno);
           agregarZombieGrinch(nuevoZombie);
       }
   }
  
   private void actualizarZombies() {
       actualizarZombiesGrinch();
       actualizarZombiesRapidos();
       actualizarZombiesColosales();
   }
  
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
  
   private void verificarColisiones() {
       verificarColisionesConGrinch();
       verificarColisionesConRapidos();
       verificarColisionesConColosales();
   }
  
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
  
   private void verificarColisionesHielo() {
       verificarColisionesHieloConGrinch();
       verificarColisionesHieloConRapidos();
       verificarColisionesHieloConColosales();
   }

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
  
   private void manejarSeleccionYPlantado() {
        if (entorno.sePresionoBoton(entorno.BOTON_IZQUIERDO)) {
            deseleccionarTodasLasPlantas();
            
            int tickActual = entorno.numeroDeTick();
            double mouseX = entorno.mouseX();
            double mouseY = entorno.mouseY();
            
            // Verificar banners
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
            else {
                seleccionarPlantaPlantada(mouseX, mouseY);
            }
        }
        
        if (entorno.estaPresionado(entorno.BOTON_IZQUIERDO)) {
            moverPlantasSeleccionadas();
        }
        
        if (entorno.seLevantoBoton(entorno.BOTON_IZQUIERDO)) {
            manejarSueltaBoton();
        }
    }

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
   
   private void manejarSueltaWallnutBanner(int tickActual) {
        if (entorno.mouseY() < 70) {
            wallnutBanner.x = wallnutBanner.xInicial;
            wallnutBanner.y = wallnutBanner.yInicial;
        } else {
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

   
    private void manejarSueltaPlantaExistente() {
        // Buscar y manejar WallNuts seleccionadas
        for (int i = 0; i < plantasWallnut.length; i++) {
            if (plantasWallnut[i] != null && plantasWallnut[i].plantada && plantasWallnut[i].seleccionada) {
                manejarSueltaWallnutExistente(plantasWallnut[i]);
                plantasWallnut[i].seleccionada = false;
                return;
            }
        }
        
        // Buscar y manejar Plantas de Hielo seleccionadas
        for (int i = 0; i < plantasHielo.length; i++) {
            if (plantasHielo[i] != null && plantasHielo[i].plantada && plantasHielo[i].seleccionada) {
                manejarSueltaPlantaHieloExistente(plantasHielo[i]);
                plantasHielo[i].seleccionada = false;
                return;
            }
        }
        
        // Buscar y manejar RoseBlades seleccionadas
        for (int i = 0; i < plantasRose.length; i++) {
            if (plantasRose[i] != null && plantasRose[i].plantada && plantasRose[i].seleccionada) {
                manejarSueltaRoseBladeExistente(plantasRose[i]);
                plantasRose[i].seleccionada = false;
                return;
            }
        }
        
        // Buscar y manejar Cerezas seleccionadas
        for (int i = 0; i < plantasCereza.length; i++) {
            if (plantasCereza[i] != null && plantasCereza[i].plantada && plantasCereza[i].seleccionada) {
                manejarSueltaCerezaExistente(plantasCereza[i]);
                plantasCereza[i].seleccionada = false;
                return;
            }
        }
    }

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
  
   private void dibujarUI() {
        entorno.cambiarFont("Arial", 20, Color.WHITE);
        entorno.escribirTexto("Zombies: " + zombiesEliminados + "/" + zombiesTotales, 400, 40);
        entorno.escribirTexto("Tiempo: " + entorno.tiempo()/1000 + "s", 400, 80);
       
        int zombiesEnPantalla = cantidadZombiesGrinchActivos + cantidadZombiesRapidosActivos + cantidadZombiesColosalesActivos;
        entorno.escribirTexto("Zombies en pantalla: " + zombiesEnPantalla, 400, 60);
 
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
            entorno.escribirTexto("Rápidos derrotados: " + rapidosAparecidos, 400, 410);
            entorno.escribirTexto("Colosales derrotados: " + colosalesAparecidos, 400, 440);
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