package com.nullpointerapps.gaexam;

import java.util.ArrayList;
import java.util.Collections;

public class AlgoritmoGenetico {
    private ArrayList<Genoma> popolazione = new ArrayList<>();
    private int dimPop;
    private int lungCromo;
    private double totFitness;
    private double migFitness;
    private double mediaFitness;
    private double pegFitness;
    private int migGenoma;
    //tra 0.05 e 0.03
    private double probMutazione;
    //≈0.7
    private double probCrossover;
    private int contoGenerazioni;


    public AlgoritmoGenetico(int dimPop, double probMutazione, double probCrossover, int nPesi) {
        //inizializza la popolazione con cromosomi con pesi a caso
        // e tutti i valori di fitness a zero

        this.dimPop = dimPop;
        this.probMutazione = probMutazione;
        this.probCrossover = probCrossover;
        lungCromo = nPesi;
        totFitness = 0;
        contoGenerazioni = 0;
        migGenoma = 0;
        migFitness = 0;
        pegFitness = 9999999;
        mediaFitness = 0;

        for (int i = 0; i < dimPop; ++i) {
            popolazione.add(new Genoma());
            for (int j = 0; j < lungCromo; ++j) {
                popolazione.get(i).pesi.add(Math.random() - Math.random());
            }
        }
    }

    //Muta il cromosoma modificando i suoi pesi di un valore
    //minore del paramentro PerturbazioneMax

    //Dati due genitori e due contenitori per la prole questo metodo
    //esegue il crossing over secono la probabilità di Crossing Over dell'AG
    private ArrayList<ArrayList<Double>> Crossover(ArrayList<Double> mamma,
                                                   ArrayList<Double> papa,
                                                   ArrayList<Double> figlio1,
                                                   ArrayList<Double> figlio2) {
        ArrayList<ArrayList<Double>> figli = new ArrayList<>();
        //ritorna i genitori come figli se non c'è crossover
        //o se i genitori sono uguali
        if ((Math.random() > probCrossover) || (mamma.equals(papa))) {
            figlio1 = mamma;
            figlio2 = papa;
        } else {
            //determino a random un punto di crossover
            int cp = (int) (Math.random() * (lungCromo - 1));

            //creo la prole
            for (int i = 0; i < cp; ++i) {
                figlio1.add(mamma.get(i));
                figlio2.add(papa.get(i));
            }

            for (int i = cp; i < mamma.size(); ++i) {
                figlio1.add(papa.get(i));
                figlio2.add(mamma.get(i));
            }
        }
        figli.add(figlio1);
        figli.add(figlio2);
        return figli;
    }

    private ArrayList<Double> Mutazione(ArrayList<Double> cromosoma){
        //ogni peso del cromosoma viene mutato, o anche no,
        //in base alla probabilità di mutazione
        for (int i=0;i<cromosoma.size();i++) {
            Double peso=cromosoma.get(i);
            if (Math.random() < probMutazione) {
                //la mutazione avviene aggiungendo, o togliendo, un piccolo
                //valore random (-1<n<1 • PerturbazioneMax)
                peso += ((Math.random() - Math.random()) * Config.DisturboMassimo);
            }
            cromosoma.set(i,peso);
        }
        return cromosoma;
    }

    private Genoma getRouletteCromosoma(){

        //un numero a random tra 0 e la fitness totale
        double n = ((Math.random() - Math.random()) * totFitness);

        //genoma da trovare
        Genoma trovato=null;

        //per aumentare la casualità scelgo il genoma che
        //fa superare alla fitnessT il numero n;
        double fitnessT = 0;

        for (Genoma g : popolazione) {
            fitnessT += g.fitness;

            if (fitnessT >= n) {
                trovato = g;
                break;
            }

        }
        return trovato;
    }

    private ArrayList<Genoma> scegliNMigliori (int nMig, int nCopie, ArrayList<Genoma> pop){
        //Aggiungo la quantità necessaria di copie
        //dei nMax migliori alla popolazione in entrata
        for (;nMig>0;nMig--) {
            for (int j=0; j<nCopie; j++) {
                pop.add(popolazione.get(dimPop-1-nMig));
            }
        }
        return pop;
    }

    private void CalcolaMigPegMediaTot(){

        totFitness = 0;
        double max = 0;
        double min  = 9999999;

        for (int i=0; i<dimPop; ++i) {
            double f = popolazione.get(i).fitness;
            if (f > max) {
                max = f;
                migGenoma = i;
                migFitness = max;
            }

            if (f < min){
                min = f;
                pegFitness = min;
            }

            totFitness	+= f;
        }

        mediaFitness = totFitness / dimPop;
    }

    private void Reset(){
        totFitness=0;
        migFitness=0;
        pegFitness=9999999;
        mediaFitness=0;
    }

    //Prende la vecchia popolazione, fa un ciclo
    //e restituisce una nuova popolazione
    public ArrayList<Genoma> Epoca(ArrayList<Genoma> vecchiaPop){
        popolazione=vecchiaPop;
        Reset();
        Collections.sort(popolazione);
        CalcolaMigPegMediaTot();
        ArrayList<Genoma> nuovaPop = new ArrayList<>();

        int nCopieElite = Config.nCopieElite;
        int nElite = Config.nElite;

        //copio i migliri nElite genomi per nCopieElite volte
        //Aggiungo i genomi copiati alla popolazione
        //i genomi aggiunti devono essere pari per non far crashare la roulette
        if(nCopieElite*nElite%2==0){
            nuovaPop = scegliNMigliori(nElite,nCopieElite,nuovaPop);
        }

        //AG loop
        while (nuovaPop.size() < dimPop) {
            //prendo due cromosomi a caso
            Genoma mamma = getRouletteCromosoma();
            Genoma papa = getRouletteCromosoma();

            //creo gli array di pesi per la prole tramite crossover
            ArrayList<Double> figlio1 = new ArrayList<>();
            ArrayList<Double> figlio2 = new ArrayList<>();
            ArrayList<ArrayList<Double>> figli = Crossover(mamma.pesi, papa.pesi, figlio1, figlio2);
            figlio1 = figli.get(0);
            figlio2 = figli.get(1);

            //muto la prole
            figlio1=Mutazione(figlio1);
            figlio2=Mutazione(figlio2);

            //creo l'effettiva prole e l'aggiungo alla nouova popolazione
            nuovaPop.add(new Genoma(figlio1, 0.0));
            nuovaPop.add(new Genoma(figlio2, 0.0));
        }

        //la popolazione diventa quella nuova
        popolazione = nuovaPop;

        return popolazione;
    }

    public ArrayList<Genoma> getCromosomi(){
        return popolazione;
    }

    public double getFitnessMedia(){
        return totFitness/dimPop;
    }

    public double getMigFitness(){
        return migFitness;
    }
}
