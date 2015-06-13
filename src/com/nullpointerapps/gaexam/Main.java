package com.nullpointerapps.gaexam;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class Main extends JPanel {

    public static ArrayList<Oggetto> oggetti = new ArrayList<>();
    public static int nOggettiReali;
    public static int nPesiRN;
    public static BufferedImage imgInd;
    public static BufferedImage imgOgg;
    public static BufferedImage imgMInd;
    public static BufferedImage icon;
    private static ArrayList<Genoma> popolazione = new ArrayList<>();
    private static ArrayList<Individuo> individui = new ArrayList<>();
    private static AlgoritmoGenetico ag;
    private static int contoGenerazione = 1;
    private ArrayList<Double> listaFitnessMedie = new ArrayList<>();
    private ArrayList<Double> listaFitnessMig = new ArrayList<>();
    private boolean isModVeloce;
    private int cicliGenerazione;

    public static void main(String[] args) {
        try {
            new Config();
            imgInd = ImageIO.read((Main.class.getClassLoader().getResource("images/individuo.png")));
            imgMInd = ImageIO.read((Main.class.getClassLoader().getResource("images/individuo-elite.png")));
            imgOgg = ImageIO.read((Main.class.getClassLoader().getResource("images/oggetto.png")));
            icon = ImageIO.read((Main.class.getClassLoader().getResource("images/icon.png")));
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
        frame.setIconImage(icon);
        frame.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_F) {
                    main.isModVeloce = !main.isModVeloce;
                }

            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

        if (individui.size() == 0) {
            for (int i = 0; i < Config.nIndividui; i++) individui.add(new Individuo());
        }

        nPesiRN = individui.get(0).getNPesi();

        ag = new AlgoritmoGenetico(Config.nIndividui, Config.probMutazione, Config.probCrossover, nPesiRN);

        popolazione = ag.getCromosomi();

        for (int i = 0; i < Config.nIndividui; i++) {
            individui.get(i).setPesi(popolazione.get(i).pesi);
        }

        for (nOggettiReali = 0; nOggettiReali < Config.nOggetti; nOggettiReali++) oggetti.add(new Oggetto());

        long passato;
        long inizio;
        long aspetta;
        long velSimulazione;
        boolean inCorso = true;

        while (inCorso) {
            if (main.isModVeloce) {
                velSimulazione = 1000 / Config.FPSfastMode;
            } else {
                velSimulazione = 1000 / Config.FPS;
            }

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

    private synchronized void disegnaGrafico(Graphics2D g) {
        //sxA - sxB
        g.drawLine(25,Config.AltezzaFinestra-45-480,25,Config.AltezzaFinestra-45);
        //dxA - dxB
        g.drawLine(Config.LarghezzaFinestra-20,Config.AltezzaFinestra-45-480,Config.LarghezzaFinestra-20,Config.AltezzaFinestra-45);
        //sxA - dxA
        g.drawLine(25,Config.AltezzaFinestra-45-480,Config.LarghezzaFinestra-20,Config.AltezzaFinestra-45-480);
        //sxB - dxB
        g.drawLine(25,Config.AltezzaFinestra-45,Config.LarghezzaFinestra-20,Config.AltezzaFinestra-45);
        for (int i = 0; i < contoGenerazione - 2; i++) {
            g.setColor(Color.BLUE);
            g.drawLine(i * (Config.LarghezzaFinestra-50)/(contoGenerazione-2)+25, Config.AltezzaFinestra-(listaFitnessMedie.get(i).intValue() * 8)-50, (i + 1) * (Config.LarghezzaFinestra-50)/(contoGenerazione-2)+25, Config.AltezzaFinestra-(listaFitnessMedie.get(i+1).intValue() * 8)-50);
            g.setColor(Color.RED);
            g.drawLine(i * (Config.LarghezzaFinestra-50)/(contoGenerazione-2)+25, Config.AltezzaFinestra-(listaFitnessMig.get(i).intValue() * 8)-50, (i + 1) * (Config.LarghezzaFinestra-50)/(contoGenerazione-2)+25, Config.AltezzaFinestra-(listaFitnessMig.get(i+1).intValue() * 8)-50);
        }
    }

    private synchronized void move() {
        if (cicliGenerazione++ < Config.nCicli) {
            for (int i = 0; i < Config.nIndividui; i++) individui.get(i).move();
        } else {
            for (int i = 0; i < Config.nIndividui; i++) {
                popolazione.get(i).setFitness(individui.get(i).getFitness());
            }
            popolazione = ag.Epoca(popolazione);
            listaFitnessMedie.add(ag.getFitnessMedia());
            listaFitnessMig.add(ag.getMigFitness());
            System.out.println("Generazione " + contoGenerazione + " Media Fitness: "+listaFitnessMedie.get(contoGenerazione-1)+" Migliore Fitness: " + listaFitnessMig.get(contoGenerazione-1));
            contoGenerazione++;
            cicliGenerazione = 0;

            for (int i = 0; i < Config.nIndividui; i++) {
                individui.get(i).setPesi(popolazione.get(i).pesi);
                individui.get(i).Reset();

                if(i<Config.nElite) {
                    individui.get(i).setElite(true);
                }
            }
            oggetti.clear();
            nOggettiReali = 0;
        }
    }

    @Override
    public synchronized void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        for (; nOggettiReali < Config.nOggetti; nOggettiReali++) oggetti.add(new Oggetto());
        if (individui.size() == 0) {
            for (int i = 0; i < Config.nIndividui; i++) individui.add(new Individuo());
        }
        if (isModVeloce) {
            disegnaGrafico(g2d);
            g2d.setFont(new Font("Arial",Font.BOLD,20));
            if (listaFitnessMedie.size()!=0&&listaFitnessMig.size()!=0) {
                g2d.setColor(Color.RED);
                g2d.drawString("Fitness migliore: " + listaFitnessMig.get(contoGenerazione-2), 10, 60);
                g2d.setColor(Color.BLUE);
                g2d.drawString("Fitness medio: " + listaFitnessMedie.get(contoGenerazione-2), 10, 90);
            }
        } else {
            for (int i = 0; i < nOggettiReali; i++) oggetti.get(i).paint(g2d);
            for (int i = 0; i < Config.nIndividui; i++) individui.get(i).paint(g2d);
        }

        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.BOLD, 20));
        g2d.drawString("Generazione: " + contoGenerazione, 10, 30);

    }
}
