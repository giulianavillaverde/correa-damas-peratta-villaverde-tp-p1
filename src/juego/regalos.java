package juego;

import java.awt.Image;

import entorno.Entorno;
import entorno.Herramientas;

public class regalos {
	double x,y,escala,alto,ancho;
	double angulo;
	Entorno e;
	Image imagen;
	

public regalos(double x, double y, Entorno e) {
	this.x = x;
	this.y = y;
	this.e = e;
	this.imagen = Herramientas.cargarImagen("regalo.png");
	this.escala = 0.15;
	this.angulo= 0;
	this.ancho = this.imagen.getWidth(null) * this.escala;
	this.alto = this.imagen.getHeight(null) * this.escala;
	
		}
public void dibujar() {
	e.dibujarImagen(imagen, x, y, this.angulo, this.escala);
	}

}
