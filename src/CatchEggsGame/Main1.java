/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CatchEggsGame;
/**
 *
 * @author yomna
 */

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JOptionPane;


public class Main1   {
 
    
    public static AudioInputStream audioStream;
    public static String selected;
    public static Clip clip;
    public static void main(String[] args){
        MainInterface I = new MainInterface();
        I.setVisible(true);
        AudioInputStream audios;
    
    File musicpath = new File("src\\\\sound\\\\chicken dance song.wav");
    
    try {
           
            audios = AudioSystem.getAudioInputStream(musicpath);
          clip = AudioSystem.getClip();
          clip.open(audios);
          clip.start();
          clip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getLocalizedMessage());
        }

} }
        

