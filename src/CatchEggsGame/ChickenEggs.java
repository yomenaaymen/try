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
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalTime;

import java.util.BitSet;
import java.util.Scanner;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.opengl.glu.GLU;

/**
 *
 * @author 1 way
 */
public class ChickenEggs extends JFrame {

    String Username = "P1";

    /**
     * @param args the command line arguments
     */
//    public static void main(String[] args) {
//        // TODO code application logic here
//        new ChickenEggs();
//    }
    public ChickenEggs() {
        GLCanvas glcanvas;
        Animator animator;

        AnimListener listener = new ChickenEggsGLEventListener();
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

    class ChickenEggsGLEventListener extends AnimListener {

        LocalTime startTime;
        int Goal = 35;

        TextRenderer renderer = new TextRenderer(new Font("SanasSerif", Font.BOLD, 36));
        TextRenderer renderer1 = new TextRenderer(new Font("SanasSerif", Font.BOLD, 60));
        TextRenderer renderer2 = new TextRenderer(new Font("SanasSerif", Font.BOLD, 25));
        //static String Username="Player1 ";
        int timer = 1;
        int pausedTime = 0;
        boolean pause = false;
        boolean started = false;
        double speed = 2;
        double speedx = 3;
        LocalTime T2;
        int A = 0;
        int maxWidth = 100;
        int maxHeight = 100;
        double x = maxWidth / 2, y = maxHeight / 2;
        String textureNames[] = {"unnamed.png", "chicken3.png", "egg.png", "brokenegg.png", "forest.png", "farmchicken.png", "happy.png", "sad.png"};
        TextureReader.Texture texture[] = new TextureReader.Texture[textureNames.length];
        int textures[] = new int[textureNames.length];
        int highscore = 0;
        int xegg[] = {1, 30, 60, 88};
        double yegg = 75;
        int xbegg[] = {1, 30, 60, 88};
        int ybegg = 0;
        int score;
        public int lives = 5;
        int index = (int) (Math.random() * 4);
        int index1 = (int) (Math.random() * 5);
        int index2 = (int) (Math.random() * 6);
        int lvl = 1;

        public ChickenEggsGLEventListener() {
        }

        void levels(GLAutoDrawable gld) {

            GL gl = gld.getGL();
            if (lvl == 1) {

                DrawChicken(gl, 1, 85);
                DrawChicken(gl, 30, 85);
                DrawChicken(gl, 60, 85);
                DrawChicken(gl, 88, 85);
                if (score >= 15) {
                    lvl = 2;
                    lives += 5;
                }
            }
            if (lvl == 2) {

                xegg = new int[]{1, 23, 45, 67, 89};
                xbegg = new int[]{1, 23, 45, 67, 89};
                DrawBackground(gl);
                DrawPlayPage(gl, gld);
                DrawChicken(gl, 1, 85);
                DrawChicken(gl, 23, 85);
                DrawChicken(gl, 45, 85);
                DrawChicken(gl, 67, 85);
                DrawChicken(gl, 89, 85);
                DrawBasket(gl, x, y, 0, 1);
                inBasket();
                outBasket(gl);
                speed = 1.6;
                speedx = 4.5;
                if (score >= 25) {
                    lives += 5;
                    lvl = 3;
                }
            }
            if (lvl == 3) {
                xegg = new int[]{1, 19, 37, 55, 73, 91};
                xbegg = new int[]{1, 19, 37, 55, 73, 91};
                DrawBackground(gl);
                DrawPlayPage(gl, gld);
                DrawChicken(gl, 1, 85);
                DrawChicken(gl, 19, 85);
                DrawChicken(gl, 37, 85);
                DrawChicken(gl, 55, 85);
                DrawChicken(gl, 73, 85);
                DrawChicken(gl, 89, 85);
                DrawBasket(gl, x, y, 0, 1);
                inBasket();
                outBasket(gl);
                speed = 1.9;
                speedx = 5;
            }
        }

        @Override
        public void init(GLAutoDrawable gld) {

//            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            GL gl = gld.getGL();
            gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);    //This Will Clear The Background Color To Black

            gl.glEnable(GL.GL_TEXTURE_2D);  // Enable Texture Mapping
            gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
            gl.glGenTextures(textureNames.length, textures, 0);

            for (int i = 0; i < textureNames.length; i++) {
                try {
                    texture[i] = TextureReader.readTexture(assetsFolderName + "//" + textureNames[i], true);
                    gl.glBindTexture(GL.GL_TEXTURE_2D, textures[i]);

//                mipmapsFromPNG(gl, new GLU(), texture[i]);
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

            if (lives == 0 || timer == 0) {
                speed = 0;
                pause = true;
            } else if (pause) {
                switch (lvl) {
                    case 1:
                        DrawEgg(gl, xegg[index], yegg);
                        break;
                    case 2:
                        DrawEgg(gl, xegg[index1], yegg);
                        break;
                    case 3:
                        DrawEgg(gl, xegg[index2], yegg);
                        break;

                }
            } else if (!pause) {
                switch (lvl) {
                    case 1:
                        DrawEgg(gl, xegg[index], yegg -= speed);
                        break;
                    case 2:
                        DrawEgg(gl, xegg[index1], yegg -= speed);
                        break;
                    case 3:
                        DrawEgg(gl, xegg[index2], yegg -= speed);
                        break;

                }
            }
            renderer.beginRendering(gld.getWidth(), gld.getHeight());

            renderer.setColor(Color.WHITE);
            if (score < Goal) {
                renderer.draw("The Goal: " + Goal, gld.getWidth() - 230, 60);
            }
            renderer.draw("Time: " + timer, gld.getWidth() - 150, 20);
            renderer.draw("Score: " + score, 15, 60);
            renderer.draw("Lives:   " + lives, 15, 20);
            renderer.setColor(Color.WHITE);
            renderer.endRendering();
            if ((score == Goal || score == Goal + 1 || score == Goal + 2) && lives != 0 && timer != 0) {

                renderer1.beginRendering(gld.getWidth(), gld.getHeight());
                renderer1.setColor(Color.WHITE);
                renderer1.draw("Great! Now try to get the highscore ", gld.getWidth() / 2 - 460, gld.getHeight() / 2);
                renderer1.endRendering();
                renderer1.setColor(Color.WHITE);
                DrawhappyChicken(gl, 48, 64);
            }

            if ((score == 15 || score == 16 || score == 25) && lives != 0 && timer != 0) {
                renderer1.beginRendering(gld.getWidth(), gld.getHeight());
                renderer1.setColor(Color.WHITE);
                renderer1.draw(" LEVEL UP!     +5Lives ", 250, 340);
                renderer1.endRendering();
                renderer1.setColor(Color.WHITE);
            }
            Timer(60);
            outBasket(gl);
            inBasket();
            GameOver(gld);

        }

        void GameOver(GLAutoDrawable gld) {
            GL gl = gld.getGL();
            if (Duration.between(startTime, LocalTime.now()).getSeconds() < 3) {
                renderer1.beginRendering(gld.getWidth(), gld.getHeight());
                renderer1.setColor(Color.magenta);
                renderer1.draw("Try to make your score " + Goal, gld.getWidth() / 2 - 360, gld.getHeight() / 2);
                renderer1.endRendering();
                renderer1.setColor(Color.WHITE);
            }
            if (lives <= 0 || timer <= 0) {

                renderer1.setColor(Color.RED);
                renderer1.beginRendering(gld.getWidth(), gld.getHeight());
                if (score < Goal) {
                    renderer1.draw("Try again to reach the goal!", 250, 490);
                    renderer1.draw(Username + " Your score is: " + score, 250, 340);
                    renderer1.draw("Press B to go to the menu", 250, 200);

                }
                if (score < highscore && score >= Goal) {
                    renderer1.draw(Username + " Your score is: " + score, 250, 480);
                    renderer1.draw(" The highscore is: " + highscore, 250, 340);
                    renderer1.draw("Press B to go to the menu", 250, 200);
                }
                if (score >= highscore && score > Goal) {
                    renderer1.draw(Username + " You got The highscore! ", 250, 480);
                    renderer1.draw(" Your score is: " + score, 250, 340);
                    renderer1.draw("Press B to go to the menu", 250, 200);
                }

                renderer1.endRendering();
                renderer1.setColor(Color.WHITE);
                DrawSadChicken(gl, 6, 60);

                try {
                    highscore();
                } catch (IOException ex) {
                    Logger.getLogger(ChickenEggs.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        boolean i = true;

        void highscore() throws FileNotFoundException, IOException {
            if (i) {
                String thisscore = "";
                File filee = new File("Assets////scores.txt");
                Scanner scan = new Scanner(filee);
                while (scan.hasNextLine()) {
                    String S = scan.nextLine();
                    int a = S.lastIndexOf("---");
                    int b = Integer.valueOf(S.substring(a + 3, S.length()));
                    if (b > score) {
                        highscore = b;
                    }

                    thisscore = thisscore.concat(S + "\n");
                }
                FileWriter writer = new FileWriter("Assets////scores.txt");
                writer.write(thisscore + Username + "---" + score + "\n");
                writer.close();
                i = false;
            }
            TreeSet<Integer> numbers = new TreeSet<>();
            FileReader fr = new FileReader("Assets////scores.txt");
            BufferedReader br = new BufferedReader(fr);
            String line;

            while ((line = br.readLine()) != null) {
                int a = line.lastIndexOf("---");
                numbers.add(Integer.parseInt(line.substring(a + 3, line.length())));
            }
            highscore = numbers.last();
        }

        @Override
        public void display(GLAutoDrawable gld) {

//            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            GL gl = gld.getGL();
            gl.glClear(GL.GL_COLOR_BUFFER_BIT);       //Clear The Screen And The Depth Buffer

            gl.glLoadIdentity();

            handleKeyPress();
            DrawBasket(gl, x, y, 0, 1);
            DrawBackground(gl);
            DrawPlayPage(gl, gld);
            DrawBasket(gl, x, y, 0, 1);
            inBasket();
            outBasket(gl);
            levels(gld);

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
                if (timer != 0 && lives != 0) {
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
                    if (pause){MainInterface.M=false;}
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

        @Override
        public void mouseDragged(MouseEvent e) {
//            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void mouseMoved(MouseEvent e) {
//            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void mouseClicked(MouseEvent e) {
//            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void mousePressed(MouseEvent e) {
//            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void mouseReleased(MouseEvent e) {
//            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void mouseEntered(MouseEvent e) {
//            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void mouseExited(MouseEvent e) {
//            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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

        public void DrawChicken(GL gl, int x, int y) {
            gl.glEnable(GL.GL_BLEND);
            gl.glBindTexture(GL.GL_TEXTURE_2D, textures[1]);	// Turn Blending On
            gl.glPushMatrix();
            gl.glTranslated(x / (maxWidth / 2.0) - 0.9, y / (maxHeight / 2.0) - 0.9, 0);
            gl.glScaled(0.1 * 1, 0.1 * 2, 1);
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

        public void DrawBasket(GL gl, double x, double y, int index, float scale) {
            gl.glEnable(GL.GL_BLEND);
            gl.glBindTexture(GL.GL_TEXTURE_2D, textures[index]);	// Turn Blending On

            gl.glPushMatrix();
            gl.glTranslated(x / (maxWidth / 2.0) - 0.9, y / (maxHeight / 0.6) - 0.9, 0);
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

        public void DrawEgg(GL gl, int x, double y) {
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

            switch (lvl) {
                case 1:
                    if (yegg > 15 && yegg < 20 && x + 6 > xegg[index] && x - 6 < xegg[index]) {
                        yegg = 75;
                        index = (int) (Math.random() * 4);
                        score++;
                    }
                    break;
                case 2:
                    if (yegg > 15 && yegg < 20 && x + 6 > xegg[index1] && x - 6 < xegg[index1]) {
                        yegg = 75;
                        index1 = (int) (Math.random() * 5);
                        score++;
                    }
                    break;
                case 3:
                    if (yegg > 15 && yegg < 20 && x + 6 > xegg[index2] && x - 6 < xegg[index2]) {
                        yegg = 75;
                        index2 = (int) (Math.random() * 6);
                        score++;
                    }
            }
        }

        public void outBasket(GL gl) {
            if (yegg <= -1) {
                yegg = 75;
                switch (lvl) {
                    case 1:
                        DrawBrokenEgg(gl, xbegg[index], ybegg);
                        index = (int) (Math.random() * 4);
                        lives--;
                        break;
                    case 2:
                        DrawBrokenEgg(gl, xbegg[index1], ybegg);
                        index1 = (int) (Math.random() * 5);
                        lives--;
                        break;
                    case 3:
                        DrawBrokenEgg(gl, xbegg[index2], ybegg);
                        index2 = (int) (Math.random() * 6);
                        lives--;
                        break;
                }

            }
        }
        public BitSet keyBits = new BitSet(256);

        public boolean isKeyPressed(final int keyCode) {
            return keyBits.get(keyCode);
        }

        public void handleKeyPress() {
            if (!pause) {
                if (isKeyPressed(KeyEvent.VK_LEFT)) {
                    if (x > 0) {
                        x -= speedx;
                    }
                }
                if (isKeyPressed(KeyEvent.VK_RIGHT)) {
                    if (x < maxWidth - 10) {
                        x += speedx;
                    }
                }
            }
        }
    }
}
