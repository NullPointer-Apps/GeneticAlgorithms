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

    public synchronized void incrementaDi(Vettore2D a){
        x+=a.x;
        y+=a.y;
    }

    public synchronized void diminuisciDi(Vettore2D a){
        x-=a.x;
        y-=a.y;
    }

    public synchronized Vettore2D diminuisci(Vettore2D a){
        return new Vettore2D(x-a.x,y-a.y);
    }

    public void moltiplicaPer(double a){
        x*=a;
        y*=a;
    }

    public synchronized Vettore2D moltiplica(double a){
        return new Vettore2D(x*a,y*a);
    }

    public synchronized void dividiPer (double a){
        x/=a;
        y/=a;
    }

    public synchronized static double getLunghezza(Vettore2D v){
        return Math.sqrt(v.x*v.x + v.y*v.y);
    }

    public synchronized void normalizza(){
        double lung = getLunghezza(this);
        x/= lung;
        y /= lung;
    }

    public synchronized double prodottoPunto(Vettore2D v1, Vettore2D v2){
        return v1.x*v2.x + v1.y*v2.y;

    }

    public int segno(Vettore2D v1, Vettore2D v2){
        if (v1.y*v2.x > v1.x*v2.y) return 1;
        else return -1;
    }
}
