package com.nullpointerapps.gaexam;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class Individuo{
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

    public Individuo(){
        rotazione=Math.random()*Config.DuePi;
        ruotaDx=0.16;
        ruotaSx=0.16;
        fitness=0;
        scalaIndividuo=Config.ScalaIndividuo;
        indexObbiettivo=0;

        pos=new Vettore2D(Math.random()* (Config.LarghezzaFinestra-45),
                Math.random()* (Config.AltezzaFinestra-45));
        direzione=new Vettore2D(Math.random(),
                Math.random());
        cervello = new ReteNeurale();
        cervello.creaRete();
    }

    public void paint(Graphics2D g2d) {
        try {
            img = ImageIO.read((getClass().getClassLoader().getResource("images/individuo.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        AffineTransform tx = new AffineTransform();
        tx.translate(pos.x,pos.y);
        tx.scale(0.1,0.1);
        tx.rotate(rotazione - Config.Pi / 2, img.getWidth() / 2, img.getHeight() / 2);
        g2d.drawImage(img,tx ,null);
        controllaCollisione();
    }

    public boolean move(){
        ArrayList<java.lang.Double> inputs = new ArrayList<>();
        ArrayList<java.lang.Double> outputs;
        Vettore2D obiettivo = getObbiettivo(Main.oggetti);

        //modifica il vettore rendendolo di lunghezza 1 in modo da poter fare un
        //confronto della direzione attraverso i versori
        obiettivo.normalizza();

        inputs.add(obiettivo.x);
        inputs.add(obiettivo.y);
        inputs.add(direzione.x);
        inputs.add(direzione.y);
        //vengono passati gli input alla rete neurale
        outputs = cervello.aggiorna(inputs);
        //controlla se il numero degli output � corretto
        if (outputs.size() < Config.nOutput) return false;

        ruotaDx = outputs.get(0);
        ruotaSx = outputs.get(1);
        //calcola l'intensit� dello sterzo
        double intSterzo = ruotaDx - ruotaSx;
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
        return true;
    }


    public Vettore2D getObbiettivo(ArrayList<Oggetto> oggetti){
        double distObiettivo = 9999999.0;
        Vettore2D obiettivo = new Vettore2D(0,0);
        for (int i = 0; i<oggetti.size();i++) {
            double distanza = Vettore2D.getLunghezza(oggetti.get(i).getPos().diminuisci(pos));
            if(distanza < distObiettivo) {
                distObiettivo = distanza;
                obiettivo = pos.diminuisci(oggetti.get(i).getPos());
                indexObbiettivo = i;
            }
        }
        return obiettivo;
    }

    public void controllaCollisione(){
        Rectangle a = new Rectangle((int)pos.x,(int)pos.y,25,16);
        Rectangle b;
        Vettore2D v;
        for(int i = 0;i<Main.oggetti.size();i++) {
            v = Main.oggetti.get(i).getPos();
            b = new Rectangle((int)v.x,(int)v.y,15,15);
            if(a.intersects(b)){
                Main.oggetti.remove(i);
                Main.nOggettiReali--;
                fitness++;
            }
        }
    }

    public void Reset() {
        pos=new Vettore2D(Math.random()* Config.LarghezzaFinestra,
                Math.random()* Config.AltezzaFinestra);
        fitness=0;
        rotazione=Math.random()*Config.DuePi;
    }

    public Vettore2D getPosition() {
        return pos;
    }

    public double getFitness() {
        return fitness;
    }

    public void setPesi(ArrayList<java.lang.Double> pesi){
        cervello.setPesi(pesi);
    }

    public int getNPesi(){
        return cervello.getNPesi();
    }
}
