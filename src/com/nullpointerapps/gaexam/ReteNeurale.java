package com.nullpointerapps.gaexam;


import java.io.IOException;
import java.util.ArrayList;

public class ReteNeurale {
    private int nInput = 4;
    private int nOutput = 2;
    private int nLNascosti = 1;
    private int nNpLN = 6;
    private ArrayList<LayerNeuroni> layers;

    public ReteNeurale(){
    }

    String  getProperty(String key) {
        try {
            return new GetProperties().getPropValue(key);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public void creaRete(){
        layers = new ArrayList<>();

        //layer nascosto
        layers.add(new LayerNeuroni(nNpLN,nInput));
        //layer di output
        layers.add(new LayerNeuroni(nOutput,nNpLN));
    }

    ArrayList<Double> getPesi(){
        ArrayList<Double> pesi = new ArrayList<>();
        for (LayerNeuroni ln : layers) {
            for (Neurone n : ln.layer){
                for (Double peso : n.pesi){
                    pesi.add(peso);
                }
            }
        }
        return pesi;
    }

    int getNPesi(){
        return nInput*nNpLN+nNpLN*nOutput;
    }

    void setPesi(ArrayList<Double> pesi){

    }

    ArrayList<Double> aggiorna(ArrayList<Double> inputs){
        ArrayList<Double> outputs = new ArrayList<>();
        //contatore dei pesi
        int pesoC;

        //controllo dell'input, se sbagliato ritorno un output vuoto
        if (inputs.size() != nInput){
            return outputs;
        }

        //for per ogni layer
        for (int i=0;i<=nLNascosti; i++){
            if (i>0){
                inputs=outputs;
            }
            outputs.clear();

            pesoC=0;

            LayerNeuroni ln = layers.get(i);
            //for per neuroni del layer i
            for (int j=0; j<ln.nNeuroni;j++){
                double netInput = 0;
                Neurone n = ln.layer.get(i);
                int nInput= n.nInput;

                //for per ogni peso del neurone j
                for (int k=0; k<nInput-1;k++){
                    double pesoT = n.pesi.get(k);
                    netInput+=pesoT*inputs.get(pesoC);
                    pesoC++;
                }

                //aggiungo l'errore
               netInput += n.pesi.get(nInput-1)*Double.parseDouble(getProperty("BIAS"));

               outputs.add(Sigmoide(netInput, Double.parseDouble(getProperty("Risposta"))));

               pesoC=0;
            }
        }

        return outputs;
    }

    Double Sigmoide(double attivazione, double risposta){
        return 1/(1+Math.exp(-attivazione/risposta));
    }

}
