package com.nullpointerapps.gaexam;

public class Vector2D {
    double x;
    double y;

    public Vector2D(){
        x=0;
        y=0;
    }

    public Vector2D(double x, double y){
        this.x=x;
        this.y=y;
    }

    public Vector2D somma(Vector2D a){
        x+=a.x;
        y+=a.y;
        return this;
    }

    public Vector2D togli(Vector2D a){
        x-=a.x;
        y-=a.y;
        return this;
    }
    public Vector2D moltiplica(double a){
        x*=a;
        y*=a;
        return this;
    }

    public Vector2D dividi(double a){
        x/=a;
        y/=a;
        return this;
    }

    public double getLunghezza(Vector2D v){
        return Math.sqrt(v.x*v.x + v.y*v.y);
    }

    public Vector2D normalizza(Vector2D v){
        double lung = getLunghezza(v);

        v.x /= lung;
        v.y /= lung;
        return v;
    }

    public double prodottoPunto(Vector2D v1, Vector2D v2){
        return v1.x*v2.x + v1.y*v2.y;

    }

    public int segno(Vector2D v1, Vector2D v2){
        if (v1.y*v2.x > v1.x*v2.y) return 1;
        else return -1;
    }
}
