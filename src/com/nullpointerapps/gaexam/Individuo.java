package com.nullpointerapps.gaexam;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Individuo{
    boolean elite = false;
    private ReteNeurale cervello;
    private Vettore2D pos;
    private Vettore2D direzione;
    private double rotazione;
    private double velocita;
    private double ruotaDx;
    private double ruotaSx;
    private double fitness;
    private double scalaIndividuo;
    private int indexObbiettivo;
    private BufferedImage img;
    Rectangle a,b;
    Vettore2D v,av,bv,obbiettivo;
    double distanza, distObbiettivo;
    double intSterzo;
    ArrayList<Double> inputs,outputs;


    public Individuo(){
        rotazione = Math.random() * Config.DuePi;
        ruotaDx=0.16;
        ruotaSx=0.16;
        fitness=0;
        scalaIndividuo=Config.ScalaIndividuo;
        indexObbiettivo=0;

        pos = new Vettore2D(Math.random() * (Config.LarghezzaFinestra - 45),
                Math.random() * (Config.AltezzaFinestra - 45));
        direzione = new Vettore2D(Math.random(),
                Math.random());
        cervello = new ReteNeurale();
        cervello.creaRete();
    }

    public synchronized void paint(Graphics2D g2d) {
        if (!elite)img=Main.imgInd;
        else img=Main.imgMInd;
        AffineTransform tx = new AffineTransform();
        tx.translate(pos.x,pos.y);
        tx.scale(0.1,0.1);
        tx.rotate(rotazione - Config.Pi / 2, img.getWidth() / 2, img.getHeight() / 2);
        g2d.drawImage(img, tx, null);
    }

    public synchronized boolean move(){
        inputs = new ArrayList<>();
        getObbiettivo(Main.oggetti);

        //modifica il vettore rendendolo di lunghezza 1 in modo da poter fare un
        //confronto della direzione attraverso i versori
        obbiettivo.normalizza();

        inputs.add(obbiettivo.x);
        inputs.add(obbiettivo.y);
        inputs.add(direzione.x);
        inputs.add(direzione.y);
        //vengono passati gli input alla rete neurale
        outputs = cervello.aggiorna(inputs);
        //controlla se il numero degli output � corretto
        if (outputs.size() < Config.nOutput) return false;

        ruotaDx = outputs.get(0);
        ruotaSx = outputs.get(1);
        //calcola l'intensit� dello sterzo
        intSterzo = ruotaDx - ruotaSx;
        //controlla che l'intensit� dello sterzo sia all'interno del
        //range fissato, se non lo � cambia i valori non consoni con
        //quelli del limite
        intSterzo= Math.max(-Config.MaxSterzo, Math.min(Config.MaxSterzo, intSterzo));

        rotazione += intSterzo;
        velocita = ruotaDx+ruotaSx;

        direzione.x = -Math.sin(rotazione);
        direzione.y = Math.cos(rotazione);
        //moltiplica il vettore direzione per la velocit� e poi
        //lo somma al vettore posizione
        pos.incrementaDi(direzione.moltiplica(velocita));
        //vari controlli per determinare se la posizione �
        //all'interno della finestra
        if (pos.x > Config.LarghezzaFinestra) pos.x=0;
        if (pos.x < -15) pos.x= Config.LarghezzaFinestra;
        if (pos.y > Config.AltezzaFinestra) pos.y=0;
        if (pos.y < -15) pos.y= Config.AltezzaFinestra;
        controllaCollisione();
        return true;
    }


    public synchronized void getObbiettivo(ArrayList<Oggetto> oggetti){
        distObbiettivo = 9999999.0;
        obbiettivo = new Vettore2D(0,0);

        for (int i = 0; i<oggetti.size();i++) {
             bv = oggetti.get(i).getPos();
             av = bv.diminuisci(pos);
            distanza = Vettore2D.getLunghezza(av);
            if(distanza < distObbiettivo) {
                distObbiettivo = distanza;
                obbiettivo = pos.diminuisci(oggetti.get(i).getPos());
                indexObbiettivo = i;
            }
        }
    }

    public synchronized void controllaCollisione(){
         a = new Rectangle((int)pos.x,(int)pos.y,25,16);
        for(int i = 0;i<Main.oggetti.size();i++) {
            v = Main.oggetti.get(i).getPos();
            b = new Rectangle((int)v.x,(int)v.y,13,18);
            if(a.intersects(b)){
                Main.oggetti.remove(i);
                Main.nOggettiReali--;
                fitness++;
            }
        }
    }

    public synchronized void Reset() {
       fitness=0;
        pos = new Vettore2D(Math.random() * (Config.LarghezzaFinestra - 45),
                Math.random() * (Config.AltezzaFinestra - 45));
        direzione = new Vettore2D(Math.random(),
                Math.random());
    }

    public synchronized Vettore2D getPosition() {
        return pos;
    }

    public synchronized double getFitness() {
        return fitness;
    }

    public synchronized void setPesi(ArrayList<java.lang.Double> pesi){
        cervello.setPesi(pesi);
    }

    public synchronized int getNPesi(){
        return cervello.getNPesi();
    }

    public synchronized void setElite(boolean elite) {
        this.elite = elite;
    }
}
