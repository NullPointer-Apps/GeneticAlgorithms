package com.nullpointerapps.gaexam;

public class Vettore2D {
    double x;
    double y;

    public Vettore2D(){
        x=0;
        y=0;
    }

    public Vettore2D(double x, double y){
        this.x=x;
        this.y=y;
    }

    public void incrementaDi(Vettore2D a){
        x+=a.x;
        y+=a.y;
    }

    public void diminuisciDi(Vettore2D a){
        x-=a.x;
        y-=a.y;
    }

    public Vettore2D diminuisci(Vettore2D a){
        return new Vettore2D(x-a.x,y-a.y);
    }

    public void moltiplicaPer(double a){
        x*=a;
        y*=a;
    }

    public Vettore2D moltiplica(double a){
        return new Vettore2D(x*a,y*a);
    }

    public void dividiPer (double a){
        x/=a;
        y/=a;
    }

    public static double getLunghezza(Vettore2D v){
        return Math.sqrt(v.x*v.x + v.y*v.y);
    }

    public void normalizza(){
        double lung = getLunghezza(this);
        x/= lung;
        y /= lung;
    }

    public double prodottoPunto(Vettore2D v1, Vettore2D v2){
        return v1.x*v2.x + v1.y*v2.y;

    }

    public int segno(Vettore2D v1, Vettore2D v2){
        if (v1.y*v2.x > v1.x*v2.y) return 1;
        else return -1;
    }
}
