package com.nullpointerapps.gaexam;

import java.util.ArrayList;

/**
 * Created by Simone on 08/06/2015.
 */
public class Neurone {
    int nInput;
    ArrayList<Double> pesi;

    public Neurone (int nInput) {
        this.nInput = nInput;
        for (int i=0;i<=nInput;i++) {
            pesi.add(Math.random());
        }
    }

}
