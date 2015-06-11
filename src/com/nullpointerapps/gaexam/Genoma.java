package com.nullpointerapps.gaexam;


import java.util.ArrayList;

public class Genoma implements Comparable{
    ArrayList<Double> pesi;
    double fitness;

    public Genoma(){
        pesi=new ArrayList<>();
        fitness=0;
    }

    public Genoma(ArrayList<Double> pesi,Double fitness){
        this.pesi=new ArrayList<>(pesi);
        this.fitness=fitness;
    }

    @Override
    public int compareTo(Object g) {
        return (int) (this.fitness-((Genoma)g).fitness);
    }
}
