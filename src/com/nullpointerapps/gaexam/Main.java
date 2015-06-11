package com.nullpointerapps.gaexam;

import javax.swing.*;
import java.awt.*;
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
    private static int contoGenerazione = 0;
    private int altezzaFinestra;
    private int larghezzaFinestra;

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
            popolazione = ag.Epoca(popolazione);

            for(int i = 0; i<Config.nIndividui;i++) {
                individui.get(i).setPesi(popolazione.get(i).pesi);
                individui.get(i).Reset();
            }
        }
        System.out.println("Ciclo " + cicliGenerazione);
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
        try {
            new Config();
        } catch (IOException e) {
            e.printStackTrace();
        }
        JFrame frame = new JFrame("SeFunzionaSpaccoTutto");
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




        while(true) {
            main.move();
            main.repaint();
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
	// write your code here
    }
}
