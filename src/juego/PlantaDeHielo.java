package juego;
import java.awt.Image;

import entorno.Entorno;
import entorno.Herramientas;

public class PlantaDeHielo extends planta {
    double x, y;
    double escala, ancho, alto;
    Image imagen, imagenSeleccionada;
    Entorno e;
    boolean seleccionada;
    boolean plantada;
public PlantaDeHielo(double x, double y, Entorno e) {
	super(x, y, e);
    this.x = x;
    this.y = y;
    this.e = e;
    this.imagen = Herramientas.cargarImagen("plantaHielo.jpg");
    this.imagenSeleccionada = Herramientas.cargarImagen("plantaDeHieloSeleccionada.jpg");
    this.escala = 0.17;
    this.seleccionada = false;
    this.plantada = false;
    
}
public void dibujar() {
    if(seleccionada) {
        e.dibujarImagen(imagenSeleccionada, x, y,0, escala);
    }
    e.dibujarImagen(imagen, x, y, 0, escala);
}
public double distancia(double x1, double y1,double x2, double y2) {
	return Math.sqrt((x1-x2) * (x1-x2) + (y1-y2) * (y1-y2));
	
}
public boolean encima(double xM, double yM) {
	return distancia (xM, yM, this.x, this.y) < 20;
	
}
public void arrastrar(double xM, double yM) {
	this.x = xM;
	this.y = yM;
}

}





