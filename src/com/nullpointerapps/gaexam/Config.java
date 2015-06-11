package com.nullpointerapps.gaexam;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
     public static double Pi = 3.14159265358979;
     public static double MezzoPi = Pi / 2;
     public static double DuePi = Pi * 2;
     public static int LarghezzaFinestra = 400;
     public static int AltezzaFinestra = 400;
     public static int FPS = 0;
     public static int nInput = 0;
     public static int nNascosti = 0;
     public static int NPlN = 0;
     public static int nOutput = 0;
     public static double Risposta = 0;
     public static double BIAS = 0;
     public static double MassimoSterzo = 0;
     public static double VelMassima = 0;
     public static int ScalaIndivuo = 0;
     public static int nIndividui = 0;
     public static int nOgetti = 0;
     public static int nTicks = 0;
     public static double ScalaOggetto = 0;
     public static double probCrossover = 0;
     public static double probMutazione = 0;
     public static double DisturboMassimo = 0;
     public static int nElite = 0;
     public static int nCopieElite = 0;

    public Config() throws IOException {

        Properties prop = new Properties();
        String propFileName = "config.properties";

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);

        if (inputStream != null) {
            prop.load(inputStream);
        } else {
            throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
        }

      FPS = Integer.parseInt(prop.getProperty("FPS"));
      nInput = Integer.parseInt(prop.getProperty("nInput"));
      nNascosti = Integer.parseInt(prop.getProperty("nNascosti"));
      NPlN = Integer.parseInt(prop.getProperty("NPlN"));
      nOutput = Integer.parseInt(prop.getProperty("nOutput"));
      Risposta = Double.parseDouble(prop.getProperty("Risposta"));
      BIAS = Double.parseDouble(prop.getProperty("BIAS"));
      MassimoSterzo = Double.parseDouble(prop.getProperty("MassimoSterzo"));
      VelMassima = Double.parseDouble(prop.getProperty("VelMassima"));
      ScalaIndivuo = Integer.parseInt(prop.getProperty("ScalaIndividuo"));
      nIndividui = Integer.parseInt(prop.getProperty("nIndividui"));
      nOgetti= Integer.parseInt(prop.getProperty("nOggetti"));
      nTicks = Integer.parseInt(prop.getProperty("nTicks"));
      ScalaOggetto= Double.parseDouble(prop.getProperty("ScalaOggetto"));
      probCrossover = Double.parseDouble(prop.getProperty("probCrossover"));
      probMutazione = Double.parseDouble(prop.getProperty("probMutazione"));
      DisturboMassimo = Double.parseDouble(prop.getProperty("DisturboMassimo"));
      nElite = Integer.parseInt(prop.getProperty("nElite"));
      nCopieElite = Integer.parseInt(prop.getProperty("nCopieElite"));
    }
}
