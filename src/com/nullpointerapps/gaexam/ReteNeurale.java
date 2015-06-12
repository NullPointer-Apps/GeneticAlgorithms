package com.nullpointerapps.gaexam;


import java.util.ArrayList;

public class ReteNeurale {
    private int nInput = Config.nInput;
    private int nOutput = Config.nOutput;
    private int nLNascosti = Config.nNascosti;
    private int nNpLN = Config.NPlN;
    private ArrayList<LayerNeuroni> layers;
    private ArrayList<Neurone> neuroni;

    public ReteNeurale(){
    }

    public void setNeuroni () {
        for (int i =0; i< nNpLN;i++) {
            neuroni.add(new Neurone(nInput));
        }
    }

    public void creaRete(){
        layers = new ArrayList<>();
        neuroni = new ArrayList<>();
        if(nLNascosti>0) {
            //layer nascosto
            layers.add(new LayerNeuroni(nNpLN,nInput));
            for (int i =0;i<nLNascosti;i++) {
                layers.add(new LayerNeuroni(nNpLN,nInput));
            }
            //layer di output
            layers.add(new LayerNeuroni(nOutput,nNpLN));
        } else {
            layers.add(new LayerNeuroni(nOutput,nNpLN));
        }

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
        int contaPesi = 0 ;
        for(int i = 0;i<Config.nNascosti;i++) {
            for(int j =0;j<layers.get(i).nNeuroni;j++) {
                for(int k =0;k<layers.get(i).layer.get(j).nInput;k++) {
                    layers.get(i).layer.get(j).pesi.set(k,pesi.get(contaPesi));
                    contaPesi++;
                }
            }
        }

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
        for (int i=0;i<nLNascosti; i++){
            if (i>0){
               inputs=outputs;
            }
            outputs.clear();
            pesoC=0;

            LayerNeuroni ln = layers.get(i);
            //for per neuroni del layer i
            for (int j=0; j<ln.nNeuroni;j++){
                double netInput = 0;
                Neurone n = ln.layer.get(j);
                int nInput= n.nInput;

                //for per ogni peso del neurone j
                for (int k=0; k<nInput-1;k++){
                    double pesoT = n.pesi.get(k);
                    netInput+=pesoT*inputs.get(pesoC);
                    pesoC++;
                }

                //aggiungo l'errore
               netInput += n.pesi.get(nInput-1)* Config.BIAS;

               outputs.add(Sigmoide(netInput, Config.Risposta));

               pesoC=0;
            }
        }

        return outputs;
    }

    Double Sigmoide(double attivazione, double risposta){
        return 1/(1+Math.exp(-attivazione/risposta));
    }

}
