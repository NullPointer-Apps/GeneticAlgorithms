package com.nullpointerapps.gaexam;

import java.util.ArrayList;


public class LayerNeuroni {
    int nNeuroni;
    ArrayList<Neurone> layer;

    public LayerNeuroni (int nNeuroni, int nIpN) {
        this.nNeuroni = nNeuroni;
        layer=new ArrayList<>();
        for (int i=0;i<nNeuroni;i++) {
            layer.add(new Neurone(nIpN));
        }
    }
}
