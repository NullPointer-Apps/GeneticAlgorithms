package com.nullpointerapps.gaexam;
//Yo Dawg!
//We heard you like objects so
//we put an object in your objects

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Oggetto
{
    Vettore2D pos;
    private BufferedImage img;

    public Oggetto() {
        pos=new Vettore2D(Math.random()* Config.LarghezzaFinestra-45,
                Math.random()* Config.AltezzaFinestra-45);
    }

    public void paint(Graphics2D g2d) {
        try {
            img = ImageIO.read((getClass().getClassLoader().getResource("images/oggetto.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        g2d.drawImage(img,(int)pos.x,(int)pos.y,15,15,null);
    }

    public Vettore2D getPos () {
        return pos;
    }
}
