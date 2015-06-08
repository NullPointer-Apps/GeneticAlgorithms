package com.nullpointerapps.gaexam;

import java.util.ArrayList;

/**
 * Created by Simone on 08/06/2015.
 */
public class LayerNeuroni {
    int nNeuroni;
    ArrayList<Neurone> layer;

    public LayerNeuroni (int nNeuroni, int nIpN) {
        this.nNeuroni = nNeuroni;
        for (int i=0;i<nNeuroni;i++) {
            layer.add(new Neurone(nIpN));
        }
    }
}
