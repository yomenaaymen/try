/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CatchEggsGame;

import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.media.opengl.GLEventListener;

/**
 *
 * @author Tyba
 */

public abstract class AnimListener implements GLEventListener ,KeyListener ,MouseMotionListener ,MouseListener {
 
    protected String assetsFolderName = "Assets";
    
}
