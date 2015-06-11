package com.nullpointerapps.gaexam;

import java.util.ArrayList;

public class Individuo {
    private ReteNeurale cervello;
    private Vector2D pos;
    private Vector2D direzione;
    private double rotazione;
    private double velocita;
    private double ruotaDx;
    private double ruotaSx;
    private double fitness;
    private double scalaIndividuo;
    private int posObbiettivo;

    public Individuo(){
        rotazione=Math.random()*Config.DuePi;
        ruotaDx=0.16;
        ruotaSx=0.16;
        fitness=0;
        scalaIndividuo=Config.ScalaIndivuo;
        posObbiettivo=0;

        pos=new Vector2D(Math.random()* Config.LarghezzaFinestra,
                Math.random()* Config.AltezzaFinestra);
    }

    public boolean Aggiorna(ArrayList<Vector2D> oggetti){
        return false;
    }

    public void Transla(ArrayList<Punto> individuo){

    }

    public Vector2D getObbiettivo(ArrayList<Vector2D> oggetti){
        return null;
    }

    public int controllaRaccolta(ArrayList<Vector2D> oggetti, double size){
        return 0;
    }

    public void Reset() {
        pos=new Vector2D(Math.random()* Config.LarghezzaFinestra,
                Math.random()* Config.AltezzaFinestra);
        fitness=0;
        rotazione=Math.random()*Config.DuePi;
    }

    public Vector2D getPosition() {
        return pos;
    }

    public double getFitness() {
        return fitness;
    }

    public void aumentaFitness(){
        fitness++;
    }

    public void setPesi(ArrayList<Double> pesi){
        cervello.setPesi(pesi);
    }

    public int getNPesi(){
        return cervello.getNPesi();
    }
}
