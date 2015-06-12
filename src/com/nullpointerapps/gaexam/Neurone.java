package com.nullpointerapps.gaexam;

import java.util.ArrayList;

public class Neurone {
    int nInput;
    ArrayList<Double> pesi;

    public Neurone (int nInput) {
        this.nInput = nInput;
        pesi = new ArrayList<>();
        for (int i=0;i<=nInput;i++) {
            pesi.add(Math.random() - Math.random());
        }
    }

}
