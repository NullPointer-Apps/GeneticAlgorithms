package com.nullpointerapps.gaexam;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class Main extends JPanel {

    private static ArrayList<Genoma> popolazione = new ArrayList<>();
    private static ArrayList<Individuo> individui = new ArrayList<>();
    public static ArrayList<Oggetto> oggetti = new ArrayList<>();
    private static AlgoritmoGenetico ag;
    public static int nOggettiReali;
    public static int nPesiRN;
    private ArrayList<Double> listaFitnessMedie = new ArrayList<>();
    private ArrayList<Double> listaFitnessMig = new ArrayList<>();
    private boolean modVeloce;
    private int cicliGenerazione;
    private static int contoGenerazione = 1;
    public static BufferedImage imgInd;
    public static BufferedImage imgOgg;
    public static BufferedImage imgMInd;

    private void disegnaGrafico() {

    }

    private void move() {
        if(cicliGenerazione++<Config.nCicli) {
            for (int i = 0; i < Config.nIndividui; i++) individui.get(i).move();
        } else {
            listaFitnessMedie.add(ag.getFitnessMedia());
            listaFitnessMig.add(ag.getMigFitness());
            contoGenerazione++;
            cicliGenerazione = 0;
            System.out.println("prova"+ contoGenerazione);
            popolazione = ag.Epoca(popolazione);

            System.out.println("prova"+contoGenerazione);
            for(int i = 0; i<Config.nIndividui;i++) {
                individui.get(i).setPesi(popolazione.get(i).pesi);
                individui.get(i).Reset();
            }
            System.out.println("prova"+contoGenerazione);
            oggetti.clear();
            nOggettiReali=0;
            System.out.println("Epoca " + System.currentTimeMillis()/1000);
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        for (int i = 0; i<nOggettiReali;i++) oggetti.get(i).paint(g2d);
        for(;nOggettiReali<Config.nOggetti;nOggettiReali++) oggetti.add(new Oggetto());
        if (individui.size()== 0) {
            for(int i = 0;i<Config.nIndividui;i++) individui.add(new Individuo());
        }
        for (int i = 0; i<Config.nIndividui;i++) individui.get(i).paint(g2d);

        g2d.setColor(Color.RED);
        g2d.setFont(new Font("Arial",Font.BOLD,20));
        g2d.drawString("Generazione: "+ contoGenerazione,10,30);

    }

    public static void main(String[] args) {
        System.out.println("Epoca " + System.currentTimeMillis()/1000);
        try {
            new Config();
            imgInd = ImageIO.read((Main.class.getClassLoader().getResource("images/individuo.png")));
            imgOgg = ImageIO.read((Main.class.getClassLoader().getResource("images/oggetto.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        JFrame frame = new JFrame("Evoluzione");
        Main main = new Main();
        frame.add(main);
        frame.setResizable(false);
        frame.setSize(Config.LarghezzaFinestra, Config.AltezzaFinestra);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        if (individui.size()== 0) {
            for(int i = 0;i<Config.nIndividui;i++) individui.add(new Individuo());
        }

        nPesiRN = individui.get(0).getNPesi();

        ag= new AlgoritmoGenetico(Config.nIndividui,Config.probMutazione,Config.probCrossover,nPesiRN);

        popolazione = ag.getCromosomi();

        for (int i =0; i<Config.nIndividui;i++) {
            individui.get(i).setPesi(popolazione.get(i).pesi);
        }

        for(nOggettiReali =0;nOggettiReali<Config.nOggetti;nOggettiReali++) oggetti.add(new Oggetto());

        long passato;
        long inizio;
        long aspetta;
        long velSimulazione = 1000/Config.FPS;
        boolean inCorso=true;

        while(inCorso) {

            inizio = System.nanoTime();

            main.move();
            main.repaint();

            passato = System.nanoTime() - inizio;

            aspetta = velSimulazione - passato / 1000000;
            if (aspetta < 0) {
                aspetta = 5;
            }
            try {
                Thread.sleep(aspetta);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
