/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CatchEggsGame;

import java.awt.BorderLayout;
import javax.media.opengl.GLCanvas;
import javax.swing.JFrame;
import com.sun.opengl.util.Animator;
import com.sun.opengl.util.FPSAnimator;
import com.sun.opengl.util.j2d.TextRenderer;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalTime;
import java.util.BitSet;
import javax.media.opengl.glu.GLU;

/**
 *
 * @author 1 way
 */
public class multi extends JFrame {

    boolean pc = false;

    /**
     * @param args the command line arguments
     */
//    public static void main(String[] args) {
//        // TODO code application logic here
//        new multi();
//    }

    public multi() {

        GLCanvas glcanvas;
        Animator animator;

        AnimListener listener = new MULTIGLEventListener();
        glcanvas = new GLCanvas();
        glcanvas.addGLEventListener(listener);
        glcanvas.addKeyListener(listener);
        getContentPane().add(glcanvas, BorderLayout.CENTER);
        animator = new FPSAnimator(30);
        animator.add(glcanvas);
        animator.start();

        setTitle("Chicken Eggs Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 720);
        setLocationRelativeTo(null);
        setVisible(true);
        setFocusable(true);

        glcanvas.requestFocus();
    }

    private class MULTIGLEventListener extends AnimListener {

        int maxWidth = 100;
        int maxHeight = 100;
        int y = maxHeight / 2;
        int xbasket = maxWidth / 2;
        int xbasket1 = 20;
        int xbasket2 = 70;
        boolean instructions = false;
        TextRenderer renderer = new TextRenderer(new Font("SanasSerif", Font.BOLD, 36));
        TextRenderer renderer1 = new TextRenderer(new Font("SanasSerif", Font.BOLD, 60));
        TextRenderer renderer2 = new TextRenderer(new Font("SanasSerif", Font.BOLD, 40));
        int timer = 1;
        int pausedTime = 0;
        LocalTime startTime;
        LocalTime T2;
        int A = 0;
        boolean pause = false;
        boolean started = false;
        double speed = 2.1;
        int speedx = 2;
        boolean image = false;
        boolean Draw = false;

        String textureNames[] = {"unnamed.png", "chicken3.png", "egg.png", "brokenegg.png", "forest.png", "Angry.png", "happy.png", "sad.png", "food-cartoon-baskets.png"};
        TextureReader.Texture texture[] = new TextureReader.Texture[textureNames.length];
        int textures[] = new int[textureNames.length];
        int highscore = 0;
        int xegg[] = {1, 30, 60, 88};
        int xegg1[] = {58, 73, 88};
        int yegg = 75;
        int yegg2 = 75;
        int xbegg[] = {1, 30, 60, 88};
        int xbegg1[] = {58, 73, 88};
        //int ybegg=0;
        int index = (int) (Math.random() * 4);
        int index1 = (int) (Math.random() * 3);
        int index2 = (int) (Math.random() * 3);
        int score;
        int score2;

        int lives1 = 5;

        int lives2 = 5;
        boolean playing = true;
        boolean PlayPage = false;

        public MULTIGLEventListener() {
        }

        @Override
        public void init(GLAutoDrawable gld) {

            GL gl = gld.getGL();
            gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);

            gl.glEnable(GL.GL_TEXTURE_2D);
            gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
            gl.glGenTextures(textureNames.length, textures, 0);

            for (int i = 0; i < textureNames.length; i++) {
                try {
                    texture[i] = TextureReader.readTexture(assetsFolderName + "//" + textureNames[i], true);
                    gl.glBindTexture(GL.GL_TEXTURE_2D, textures[i]);

                    new GLU().gluBuild2DMipmaps(
                            GL.GL_TEXTURE_2D,
                            GL.GL_RGBA, // Internal Texel Format,
                            texture[i].getWidth(), texture[i].getHeight(),
                            GL.GL_RGBA, // External format from image,
                            GL.GL_UNSIGNED_BYTE,
                            texture[i].getPixels() // Imagedata
                    );
                } catch (IOException e) {
                    System.out.println(e);
                    e.printStackTrace();
                }
            }
        }

        void Timer(float x) {

            if (!started) {
                startTime = LocalTime.now();
            }
            Duration timeSinceStart = Duration.between(startTime, LocalTime.now());

            if (!pause) {
                A = pausedTime;
                float i = timeSinceStart.getSeconds() - (pausedTime);
                timer = (int) (x - i);
                T2 = LocalTime.now();
            }
            if (pause) {
                Duration timesinceT2 = Duration.between(T2, LocalTime.now());
                pausedTime = (int) timesinceT2.getSeconds() + A;
            }
            started = true;
        }

        void DrawPlayPage(GL gl, GLAutoDrawable gld) {

            Timer(45);
            if (Duration.between(startTime, LocalTime.now()).getSeconds() < 5) {
                pause = true;
                renderer2.beginRendering(gld.getWidth(), gld.getHeight());
                renderer2.setColor(Color.ORANGE);
                if (!pc) {
                    renderer2.draw("Use the 'A' and 'D'", 65, 400);
                    renderer2.draw("keys to move here", 65, 300);
                }
                if (pc) {
                    renderer2.draw("The PC will play here", 65, 400);
                }
                renderer2.draw("Use the left and right", gld.getWidth() - 500, 400);
                renderer2.draw("arrows to move here", gld.getWidth() - 500, 300);
                renderer2.endRendering();
                renderer2.setColor(Color.WHITE);
                instructions = true;
            }
            if (Duration.between(startTime, LocalTime.now()).getSeconds() == 5) {
                instructions = false;
                pause = false;
            }

            if ((lives1 == 0 && lives2 == 0) || timer == 0) {

                speed = 0;
                pause = true;

            } else if (pause) {

                xegg = new int[]{1, 16, 31};
                xegg1 = new int[]{58, 73, 88};
                xbegg = new int[]{1, 16, 31};
                xbegg1 = new int[]{58, 73, 88};
                if (lives1 != 0 && !instructions) {
                    DrawEgg(gl, xegg[index2], yegg);
                }
                if (lives2 != 0 && !instructions) {
                    DrawEgg(gl, xegg1[index1], yegg2);
                }
            } else if (!pause) {

                xegg = new int[]{1, 16, 31};
                xegg1 = new int[]{58, 73, 88};
                xbegg = new int[]{1, 16, 31};
                xbegg1 = new int[]{58, 73, 88};
                if (lives1 != 0) {
                    DrawEgg(gl, xegg[index2], yegg -= speed);
                }
                if (lives2 != 0) {
                    DrawEgg(gl, xegg1[index1], yegg2 -= speed);
                }

            }

            renderer.beginRendering(gld.getWidth(), gld.getHeight());

            renderer.setColor(Color.WHITE);
            renderer.draw("Time : " + timer, gld.getWidth() / 2 - 80, gld.getHeight() - 75);
            renderer.draw("Score : " + score, 15, 60);
            renderer.draw("Lives :   " + lives1, 15, 20);

            renderer.setColor(Color.WHITE);
            renderer.endRendering();

            renderer.beginRendering(gld.getWidth(), gld.getHeight());
            renderer.setColor(Color.PINK);
            renderer.draw("Score : " + score2, gld.getWidth() - 175, 60);
            renderer.draw("Lives :   " + lives2, gld.getWidth() - 175, 20);
            renderer.setColor(Color.WHITE);
            renderer.endRendering();

            outBasket(gl);

            inBasket();

            GameOver(gld);

        }

        void GameOver(GLAutoDrawable gld) {
            GL gl = gld.getGL();
            if (lives1 == 0 && lives2 != 0 && timer != 0) {
                TextRenderer renderer10 = new TextRenderer(new Font("SanasSerif", Font.BOLD, 60));
                renderer10.setColor(Color.red);
                renderer10.beginRendering(gld.getWidth(), gld.getHeight());
                renderer10.draw("Please wait for", 75, 400);
                renderer10.draw("the other player", 75, 300);

                renderer10.endRendering();
                renderer10.setColor(Color.WHITE);

            }
            if (lives2 == 0 && lives1 != 0 && timer != 0) {
                TextRenderer renderer10 = new TextRenderer(new Font("SanasSerif", Font.BOLD, 60));
                renderer10.setColor(Color.red);
                renderer10.beginRendering(gld.getWidth(), gld.getHeight());
                renderer10.draw("Please wait for", gld.getWidth() - 500, 400);
                renderer10.draw("the other player", gld.getWidth() - 500, 300);

                renderer10.endRendering();
                renderer10.setColor(Color.WHITE);
            }

            if ((lives2 <= 0 && lives1 <= 0) || timer == 0) {
                TextRenderer renderer10 = new TextRenderer(new Font("SanasSerif", Font.BOLD, 80));
                renderer10.setColor(Color.red);
                renderer10.beginRendering(gld.getWidth(), gld.getHeight());
                if (score > score2) {
                    if (!pc) {
                        renderer10.draw("Player1 WON!", 100, 400);
                    }
                    if (pc) {
                        renderer10.draw("YOU LOST", gld.getWidth() / 2 - 180, 400);
                    }
                    renderer10.draw("Press B to go to the menu", 100, 200);
                } else if (score < score2) {
                    if (!pc) {
                        renderer10.draw("Player2 WON!", gld.getWidth() - 540, 400);
                    }
                    if (pc) {
                        renderer10.draw("YOU WON!", gld.getWidth() - 540, 400);
                    }
                    renderer10.draw("Press B to go to the menu", 100, 200);
                } else {
                    renderer10.draw("DRAW!", gld.getWidth() / 2 - 120, 400);
                    renderer10.draw("Press B to go to the menu", 100, 200);
                }
                renderer10.endRendering();
                renderer10.setColor(Color.WHITE);

                image = true;
                Draw = true;
                pause = true;
            }

        }

        @Override
        public void display(GLAutoDrawable gld) {

//            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            GL gl = gld.getGL();
            gl.glClear(GL.GL_COLOR_BUFFER_BIT);       //Clear The Screen And The Depth Buffer
            gl.glLoadIdentity();
            DrawBackground(gl);
            DrawPlayPage(gl, gld);

            DrawBasket1(gl, xbasket1, y, 0, 1);
            DrawBasket2(gl, xbasket2, y, 8, 1);
            DrawChicken(gl, 1, 85);
            DrawChicken(gl, 16, 85);
            DrawChicken(gl, 31, 85);

            DrawChicken(gl, 58, 85);
            DrawChicken(gl, 73, 85);
            DrawChicken(gl, 88, 85);

            handleKeyPress();
            inBasket();
            outBasket(gl);
            if (pc) {
                if (lives1 != 0) {
                    if (!pause) {
                        if (xegg[index2] < xbasket1) {
                            xbasket1 -= 1;
                        } else if (xbasket1 < xegg[index2]) {

                            xbasket1 += 1;
                        }

                    }
                }
            }
            if (image) {
                if (score < score2) {
                    DrawhappyChicken(gl, 75, 80);
                } else if ((score > score2)) {
                    if (!pc) {
                        DrawhappyChicken(gl, 6, 80);
                    }
                    if (pc) {
                        DrawSadChicken(gl, 75, 80);
                    }
                }
            }
            if (Draw) {
                if (score == score2) {
                    DrawAngryChicken(gl, 49, 80);
                }
            }
        }

        @Override
        public void reshape(GLAutoDrawable gld, int i, int i1, int i2, int i3) {
//            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void displayChanged(GLAutoDrawable gld, boolean bln, boolean bln1) {
//            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        boolean V = true;

        @Override
        public void keyTyped(KeyEvent e) {
            if (e.getKeyChar() == KeyEvent.VK_P || e.getKeyChar() == 'p') {
                if (timer != 0 && timer != 45 && (lives1 != 0 || lives2 != 0)) {
                    long clipTime = Main1.clip.getMicrosecondPosition();
                    if (pause == true) {
                      if(MainInterface.M){Main1.clip.setMicrosecondPosition(clipTime);
                        Main1.clip.start();}
                        pause = false;
                    } else {

                        Main1.clip.stop();
                        pause = true;
                    }
                }
            }
            if (isKeyPressed(KeyEvent.VK_B)) {
                if (V) {
                    if (pause && timer != 45){MainInterface.M=false;}
                    new MainInterface().setVisible(true);
                    setVisible(false);

                }
                V = false;
            }
             if (isKeyPressed(KeyEvent.VK_M) ) {
                long clipTime1 = Main1.clip.getMicrosecondPosition();
                if (!MainInterface.M) {

                    Main1.clip.setMicrosecondPosition(clipTime1);
                    Main1.clip.start();
                    MainInterface.M = true;
                } else if (MainInterface.M) {
                    Main1.clip.stop();
                    MainInterface.M = false;
                }
            }
        }

        @Override
        public void keyPressed(final KeyEvent event) {
            int keyCode = event.getKeyCode();
            keyBits.set(keyCode);
        }

        @Override
        public void keyReleased(final KeyEvent event) {
            int keyCode = event.getKeyCode();
            keyBits.clear(keyCode);
        }

        public void DrawBackground(GL gl) {
            gl.glEnable(GL.GL_BLEND);
            gl.glBindTexture(GL.GL_TEXTURE_2D, textures[4]);	// Turn Blending On

            gl.glPushMatrix();
            gl.glBegin(GL.GL_QUADS);
            // Front Face
            gl.glTexCoord2f(0.0f, 0.0f);
            gl.glVertex3f(-1.0f, -1.0f, -1.0f);
            gl.glTexCoord2f(1.0f, 0.0f);
            gl.glVertex3f(1.0f, -1.0f, -1.0f);
            gl.glTexCoord2f(1.0f, 1.0f);
            gl.glVertex3f(1.0f, 1.0f, -1.0f);
            gl.glTexCoord2f(0.0f, 1.0f);
            gl.glVertex3f(-1.0f, 1.0f, -1.0f);
            gl.glEnd();
            gl.glPopMatrix();

            gl.glDisable(GL.GL_BLEND);
        }

        public void DrawAngryChicken(GL gl, int x, int y) {
            gl.glEnable(GL.GL_BLEND);
            gl.glBindTexture(GL.GL_TEXTURE_2D, textures[5]);	// Turn Blending On
            gl.glPushMatrix();
            gl.glTranslated(x / (maxWidth / 2.0) - 0.9, y / (maxHeight / 2.0) - 0.9, 0);
            gl.glScaled(0.1 * 2, 0.1 * 3, 1);
            gl.glBegin(GL.GL_QUADS);
            // Front Face
            gl.glTexCoord2f(0.0f, 0.0f);
            gl.glVertex3f(-1.0f, -1.0f, -1.0f);
            gl.glTexCoord2f(1.0f, 0.0f);
            gl.glVertex3f(1.0f, -1.0f, -1.0f);
            gl.glTexCoord2f(1.0f, 1.0f);
            gl.glVertex3f(1.0f, 1.0f, -1.0f);
            gl.glTexCoord2f(0.0f, 1.0f);
            gl.glVertex3f(-1.0f, 1.0f, -1.0f);
            gl.glEnd();
            gl.glPopMatrix();

            gl.glDisable(GL.GL_BLEND);
        }

        public void DrawhappyChicken(GL gl, int x, int y) {
            gl.glEnable(GL.GL_BLEND);
            gl.glBindTexture(GL.GL_TEXTURE_2D, textures[6]);	// Turn Blending On
            gl.glPushMatrix();
            gl.glTranslated(x / (maxWidth / 2.0) - 0.9, y / (maxHeight / 2.0) - 0.9, 0);
            gl.glScaled(0.1 * 2, 0.1 * 3, 1);
            gl.glBegin(GL.GL_QUADS);
            // Front Face
            gl.glTexCoord2f(0.0f, 0.0f);
            gl.glVertex3f(-1.0f, -1.0f, -1.0f);
            gl.glTexCoord2f(1.0f, 0.0f);
            gl.glVertex3f(1.0f, -1.0f, -1.0f);
            gl.glTexCoord2f(1.0f, 1.0f);
            gl.glVertex3f(1.0f, 1.0f, -1.0f);
            gl.glTexCoord2f(0.0f, 1.0f);
            gl.glVertex3f(-1.0f, 1.0f, -1.0f);
            gl.glEnd();
            gl.glPopMatrix();

            gl.glDisable(GL.GL_BLEND);
        }

        public void DrawChicken(GL gl, int x, int y) {
            gl.glEnable(GL.GL_BLEND);
            gl.glBindTexture(GL.GL_TEXTURE_2D, textures[1]);
            gl.glPushMatrix();
            gl.glTranslated(x / (maxWidth / 2.0) - 0.9, y / (maxHeight / 2.0) - 0.9, 0);
            gl.glScaled(0.1 * 1, 0.1 * 2, 1);
            gl.glBegin(GL.GL_QUADS);
            gl.glTexCoord2f(0.0f, 0.0f);
            gl.glVertex3f(-1.0f, -1.0f, -1.0f);
            gl.glTexCoord2f(1.0f, 0.0f);
            gl.glVertex3f(1.0f, -1.0f, -1.0f);
            gl.glTexCoord2f(1.0f, 1.0f);
            gl.glVertex3f(1.0f, 1.0f, -1.0f);
            gl.glTexCoord2f(0.0f, 1.0f);
            gl.glVertex3f(-1.0f, 1.0f, -1.0f);
            gl.glEnd();
            gl.glPopMatrix();

            gl.glDisable(GL.GL_BLEND);
        }

        public void DrawSadChicken(GL gl, int x, int y) {
            gl.glEnable(GL.GL_BLEND);
            gl.glBindTexture(GL.GL_TEXTURE_2D, textures[7]);	// Turn Blending On
            gl.glPushMatrix();
            gl.glTranslated(x / (maxWidth / 2.0) - 0.9, y / (maxHeight / 2.0) - 0.9, 0);
            gl.glScaled(0.1 * 2, 0.1 * 3, 1);
            gl.glBegin(GL.GL_QUADS);
            // Front Face
            gl.glTexCoord2f(0.0f, 0.0f);
            gl.glVertex3f(-1.0f, -1.0f, -1.0f);
            gl.glTexCoord2f(1.0f, 0.0f);
            gl.glVertex3f(1.0f, -1.0f, -1.0f);
            gl.glTexCoord2f(1.0f, 1.0f);
            gl.glVertex3f(1.0f, 1.0f, -1.0f);
            gl.glTexCoord2f(0.0f, 1.0f);
            gl.glVertex3f(-1.0f, 1.0f, -1.0f);
            gl.glEnd();
            gl.glPopMatrix();

            gl.glDisable(GL.GL_BLEND);
        }

        public void DrawBasket(GL gl, int x, int y, int index, float scale) {
            gl.glEnable(GL.GL_BLEND);
            gl.glBindTexture(GL.GL_TEXTURE_2D, textures[index]);	// Turn Blending On

            gl.glPushMatrix();
            gl.glTranslated(x / (maxWidth / 2.0) - 0.9, y / (maxHeight / 0.6) - 0.9, 0);
            gl.glScaled(0.1 * scale, 0.1 * scale, 1);

            gl.glBegin(GL.GL_QUADS);

            gl.glTexCoord2f(0.0f, 0.0f);
            gl.glVertex3f(-1.0f, -1.0f, -1.0f);
            gl.glTexCoord2f(1.0f, 0.0f);
            gl.glVertex3f(1.0f, -1.0f, -1.0f);
            gl.glTexCoord2f(1.0f, 1.0f);
            gl.glVertex3f(1.0f, 1.0f, -1.0f);
            gl.glTexCoord2f(0.0f, 1.0f);
            gl.glVertex3f(-1.0f, 1.0f, -1.0f);
            gl.glEnd();
            gl.glPopMatrix();

            gl.glDisable(GL.GL_BLEND);
        }

        public void DrawEgg(GL gl, int x, int y) {
            gl.glEnable(GL.GL_BLEND);
            gl.glBindTexture(GL.GL_TEXTURE_2D, textures[2]);	// Turn Blending On
            gl.glPushMatrix();
            gl.glTranslated(x / (maxWidth / 2.0) - 0.9, y / (maxHeight / 2.0) - 0.9, 0);
            gl.glScaled(0.1 * 0.3, 0.1 * 0.5, 1);
            gl.glBegin(GL.GL_QUADS);
            // Front Face
            gl.glTexCoord2f(0.0f, 0.0f);
            gl.glVertex3f(-1.0f, -1.0f, -1.0f);
            gl.glTexCoord2f(1.0f, 0.0f);
            gl.glVertex3f(1.0f, -1.0f, -1.0f);
            gl.glTexCoord2f(1.0f, 1.0f);
            gl.glVertex3f(1.0f, 1.0f, -1.0f);
            gl.glTexCoord2f(0.0f, 1.0f);
            gl.glVertex3f(-1.0f, 1.0f, -1.0f);
            gl.glEnd();
            gl.glPopMatrix();

            gl.glDisable(GL.GL_BLEND);
        }

        public void DrawBrokenEgg(GL gl, int x, int y) {
            gl.glEnable(GL.GL_BLEND);
            gl.glBindTexture(GL.GL_TEXTURE_2D, textures[3]);	// Turn Blending On
            gl.glPushMatrix();
            gl.glTranslated(x / (maxWidth / 2.0) - 0.9, y / (maxHeight / 2.0) - 0.9, 0);
            gl.glScaled(0.1 * 0.6, 0.1 * 0.6, 1);
            gl.glBegin(GL.GL_QUADS);
            // Front Face
            gl.glTexCoord2f(0.0f, 0.0f);
            gl.glVertex3f(-1.0f, -1.0f, -1.0f);
            gl.glTexCoord2f(1.0f, 0.0f);
            gl.glVertex3f(1.0f, -1.0f, -1.0f);
            gl.glTexCoord2f(1.0f, 1.0f);
            gl.glVertex3f(1.0f, 1.0f, -1.0f);
            gl.glTexCoord2f(0.0f, 1.0f);
            gl.glVertex3f(-1.0f, 1.0f, -1.0f);
            gl.glEnd();
            gl.glPopMatrix();

            gl.glDisable(GL.GL_BLEND);
        }

        void inBasket() {

            if (yegg == 18 && xbasket1 + 6 > xegg[index2] && xbasket1 - 6 < xegg[index2]) {
                yegg = 75;
                index2 = (int) (Math.random() * 3);
                score++;

            }
            if (yegg2 == 18 && xbasket2 + 6 > xegg1[index1] && xbasket2 - 6 < xegg1[index1]) {
                yegg2 = 75;
                index1 = (int) (Math.random() * 3);
                score2++;
            }

        }

        public void outBasket(GL gl) {

            if (yegg <= -1 && (xegg[index2] == 1 || xegg[index2] == 16 || xegg[index2] == 31)) {
                DrawBrokenEgg(gl, xbegg[index2], 0);
                yegg = 75;

                index2 = (int) (Math.random() * 3);

                lives1--;

            } else if (yegg2 <= -1 && (xegg1[index1] == 58 || xegg1[index1] == 73 || xegg1[index1] == 88)) {
                DrawBrokenEgg(gl, xbegg1[index1], 0);
                yegg2 = 75;

                index1 = (int) (Math.random() * 3);

                lives2--;

            }
        }

        public BitSet keyBits = new BitSet(256);

        public boolean isKeyPressed(final int keyCode) {
            return keyBits.get(keyCode);
        }

        public void handleKeyPress() {
            if (!pause) {

                if (!pc) {
                    if (lives1 != 0) {
                        if (isKeyPressed(KeyEvent.VK_A)) {
                            if (xbasket1 > 0) {
                                xbasket1 -= speedx;
                            }
                        }
                        if (isKeyPressed(KeyEvent.VK_D)) {
                            if (xbasket1 < maxWidth - 60) {
                                xbasket1 += speedx;
                            }
                        }
                    }
                }
                if (lives2 != 0) {
                    if (isKeyPressed(KeyEvent.VK_LEFT)) {
                        if (xbasket2 > maxWidth - 50) {
                            xbasket2 -= speedx;
                        }
                    }
                    if (isKeyPressed(KeyEvent.VK_RIGHT)) {
                        if (xbasket2 < maxWidth - 10) {
                            xbasket2 += speedx;
                        }
                    }
                }
            }
        }

        public void DrawBasket1(GL gl, int x, int y, int index, float scale) {
            gl.glEnable(GL.GL_BLEND);
            gl.glBindTexture(GL.GL_TEXTURE_2D, textures[index]);	// Turn Blending On

            gl.glPushMatrix();
            gl.glTranslated(xbasket1 / (maxWidth / 2.0) - 0.9, y / (maxHeight / 0.6) - 0.9, 0);
            gl.glScaled(0.1 * scale, 0.1 * scale, 1);
            //System.out.println(x +" " + y);
            gl.glBegin(GL.GL_QUADS);
            // Front Face
            gl.glTexCoord2f(0.0f, 0.0f);
            gl.glVertex3f(-1.0f, -1.0f, -1.0f);
            gl.glTexCoord2f(1.0f, 0.0f);
            gl.glVertex3f(1.0f, -1.0f, -1.0f);
            gl.glTexCoord2f(1.0f, 1.0f);
            gl.glVertex3f(1.0f, 1.0f, -1.0f);
            gl.glTexCoord2f(0.0f, 1.0f);
            gl.glVertex3f(-1.0f, 1.0f, -1.0f);
            gl.glEnd();
            gl.glPopMatrix();

            gl.glDisable(GL.GL_BLEND);
        }

        //     int xbasket1 = 20 ; int xbasket2 = 70;

        public void DrawBasket2(GL gl, int x, int y, int index, float scale) {
            gl.glEnable(GL.GL_BLEND);
            gl.glBindTexture(GL.GL_TEXTURE_2D, textures[index]);	// Turn Blending On

            gl.glPushMatrix();
            gl.glTranslated(xbasket2 / (maxWidth / 2.0) - 0.9, y / (maxHeight / 0.6) - 0.9, 0);
            gl.glScaled(0.1 * scale, 0.1 * scale, 1);
            //System.out.println(x +" " + y);
            gl.glBegin(GL.GL_QUADS);
            // Front Face
            gl.glTexCoord2f(0.0f, 0.0f);
            gl.glVertex3f(-1.0f, -1.0f, -1.0f);
            gl.glTexCoord2f(1.0f, 0.0f);
            gl.glVertex3f(1.0f, -1.0f, -1.0f);
            gl.glTexCoord2f(1.0f, 1.0f);
            gl.glVertex3f(1.0f, 1.0f, -1.0f);
            gl.glTexCoord2f(0.0f, 1.0f);
            gl.glVertex3f(-1.0f, 1.0f, -1.0f);
            gl.glEnd();
            gl.glPopMatrix();

            gl.glDisable(GL.GL_BLEND);
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void mousePressed(MouseEvent e) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void mouseExited(MouseEvent e) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }
}
