import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

//Arkady Kokush
//Date: Oct 24, 2024

public class SnakeGUI extends JPanel implements KeyListener, MouseListener
{
    public static final int PANEL_WIDTH = 800;
    public static final int PANEL_HEIGHT = 600;
    private static final int GAME_OVER = 1;
    private static final int GAME_MENU = 2;
    private static final int GAME_RUN = 3;
    private static Color color1 = Color.BLACK;
    private static Color color2 = Color.WHITE;
    private int screen;
    private Snake snake1;
    private Snake snake2;
    private Snack snack;
    private String highScore = "";
    private int score1, score2;
    private boolean winner1, winner2;
    private boolean player2 = false;

    public SnakeGUI()
    {

        this.setFocusable(true); // Lets KeyListener Detect the panel
        this.addKeyListener(this); // Adds the KeyListener
        this.addMouseListener(this);
        snake1 = new Snake(400, 300, 25, 25, KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D); // Creates
                                                                                                          // Snake
        snake2 = new Snake(400, 250, 25, 25, KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT);

        snack = new Snack((int) (Math.random() * 32) * 25, (int) (Math.random() * 24) * 25, 25, 25);
        screen = GAME_RUN;
        ; // Creates Snack

        Timer timer = new Timer(100, new ActionListener() // Creates Timer in order to keep code running
        {

            @Override
            public void actionPerformed(ActionEvent e)
            {
                switch (screen)
                {

                case (GAME_RUN):

                    // Snake 1 on/ate snack
                    if (snake1.onSnack(snack) || snake1.ateSnack(snack))
                        snack = new Snack((int) (Math.random() * 32) * 25, (int) (Math.random() * 24) * 25, 25, 25);

                    // Snake 2 on/ate snack
                    if (player2)
                        if (snake2.onSnack(snack) || snake2.ateSnack(snack))
                            snack = new Snack((int) (Math.random() * 32) * 25, (int) (Math.random() * 24) * 25, 25, 25);

                    // Moves the snakes
                    snake1.moveSnake();
                    if (player2)
                        snake2.moveSnake();

                    // Crosses Borders and Self
                    if (player2)
                    {
                        if (snake1.checkCrossBorder(PANEL_WIDTH, PANEL_HEIGHT) || snake1.checkSelf()
                                || snake1.hitOther(snake2))
                        {
                            screen = GAME_OVER;
                            checkScore();
                            winner2 = true;
                        }
                        if (snake2.checkCrossBorder(PANEL_WIDTH, PANEL_HEIGHT) || snake2.checkSelf()
                                || snake2.hitOther(snake1))
                        {
                            screen = GAME_OVER;
                            checkScore();
                            winner1 = true;
                        }
                        score2 = (snake2.size() - 3) * 10;
                        score1 = (snake1.size() - 3) * 10;

                    } else
                    {
                        if (snake1.checkCrossBorder(PANEL_WIDTH, PANEL_HEIGHT) || snake1.checkSelf())
                        {
                            screen = GAME_OVER;
                            checkScore();
                        }

                        score1 = (snake1.size() - 3) * 10;

                    }
                }

                repaint();
            }

        });
        timer.start(); // Starts Timer
    }

    @Override
    public void paintComponent(Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g; // Casts into Graphics 2D

        // Color the Background
        g2.setColor(new Color(182, 234, 80));
        g2.fillRect(0, 0, this.getWidth(), this.getHeight());
        g2.setColor(new Color(137, 191, 31));
        for (int i = 0; i < this.getWidth() / 25; i += 2)
            for (int q = 0; q < this.getHeight() / 25; q += 2)
                g2.fillRect(i * 25, q * 25, 25, 25);
        for (int i = 1; i < this.getWidth() / 25; i += 2)
            for (int q = 1; q < this.getHeight() / 25; q += 2)
                g2.fillRect(i * 25, q * 25, 25, 25);
        Rectangle panel = new Rectangle(0, 0, this.getWidth(), this.getHeight());

        switch (screen)
        {
        case (GAME_OVER):
            // Game Over
            g2.setFont(new Font("BatmanForeverAlternate", Font.PLAIN, 75));
            g2.setColor(Color.BLACK);
            g2.drawString("GAME OVER", 135, 80);

            // Score
            g2.setFont(new Font("BatmanForeverAlternate", Font.PLAIN, 50));
            g2.setColor(Color.BLACK);
            g2.drawString("SCORE:" + score1, 270, 270);
            if (!player2)
            {
                // High Score
                g2.setFont(new Font("BatmanForeverAlternate", Font.PLAIN, 35));
                g2.setColor(new Color(209, 96, 15));
                g2.drawString("HIGH SCORE: " + highScore, 190, 350);
            }
            // Space Bar
            g2.setColor(Color.BLACK);
            g2.setFont(new Font("BatmanForeverAlternate", Font.PLAIN, 48));
            g2.drawString("space bar to restart", 35, 550);

            // Winner
            if (winner1 && winner2)
            {
                if (score1 > score2)
                    winner2 = false;
                if (score1 < score2)
                    winner1 = false;
                else
                    g2.drawString("Both Lost", 240, 355);

            } else if (winner1)
            {
                g2.setColor(color1);
                g2.drawString("Player 1 Won", 188, 355);

            } else if (winner2)
            {
                g2.setColor(color2);
                g2.drawString("Player 2 Won", 188, 355);
            }

            break;

        case (GAME_RUN):

            if (snake1.getDirection() == 0 && snake2.getDirection() == 0)
            {
                // Set Title
                g2.setFont(new Font("BatmanForeverAlternate", Font.PLAIN, 100));
                g2.setColor(Color.BLACK);
                FontMetrics fm = g.getFontMetrics();
                int snakeWidth = fm.stringWidth("snake");
                g2.drawString("SNAKE", panel.x + (panel.width - snakeWidth) / 2, 100);

                // FakeSnake
                if (player2)
                {
                    g2.setColor(color1);
                    g2.fillRect(362, 287, 25, 25);
                    g2.setColor(color2);
                    g2.fillRect(437, 287, 25, 25);

                } else
                {
                    g2.setColor(color1);
                    g2.fillRect(387, 287, 25, 25);
                }

                // Menu Button
                g2.setColor(Color.BLACK);
                g2.setFont(new Font("BatmanForeverAlternate", Font.PLAIN, 75));
                Stroke oldStroke = g2.getStroke();
                g2.setStroke(new BasicStroke(2));
                g2.drawRect(250, 375, 300, 100);
                g2.setStroke(oldStroke);
                Rectangle menuButton = new Rectangle(250, 375, 300, 100);
                fm = g.getFontMetrics();
                int menuWidth = fm.stringWidth("menu");
                int menuHeight = fm.getHeight();
                g2.drawString("Menu", menuButton.x + (menuButton.width - menuWidth) / 2,
                        menuButton.y + (menuButton.height - menuHeight) / 2 + fm.getAscent());
                
                //Instructions
                g2.setFont(new Font("BatmanForeverAlternate", Font.PLAIN, 25));
                g2.drawString("WASD for P1, Arrows for P2", 160, 550);
            } else
            {
                

                // Score
                g2.setFont(new Font("BatmanForeverAlternate", Font.PLAIN, 50));
                g2.setColor(Color.BLACK);
                g2.drawString("SCORE:" + score1, 25, 40);
                if (player2)
                {
                    g2.setFont(new Font("BatmanForeverAlternate", Font.PLAIN, 50));
                    g2.setColor(Color.BLACK);
                    g2.drawString("SCORE:" + score2, 500, 575);
                }
                if (!player2)
                {
                    // HighScore
                    g2.setFont(new Font("BatmanForeverAlternate", Font.PLAIN, 25));
                    g2.drawString("HIGH SCORE:" + highScore, 25, 70);
                }

                // Draw Snack
                snack.draw(g2);

                // Draw Snake

                if (player2)
                {

                    g2.setColor(color2);
                    snake2.draw(g2);
                }
                g2.setColor(color1);
                snake1.draw(g2);
            }

            break;
        case (GAME_MENU):
            // Set Title
            g2.setFont(new Font("BatmanForeverAlternate", Font.PLAIN, 100));
            g2.setColor(Color.BLACK);
            FontMetrics fm = g.getFontMetrics();
            int menuWidth = fm.stringWidth("menu");
            g2.drawString("MENU", panel.x + (panel.width - menuWidth) / 2, 100);

            // One or Two Players
            g2.setFont(new Font("BatmanForeverAlternate", Font.PLAIN, 30));

            // Boxes
            Rectangle oneP = new Rectangle(150, 200, 200, 100);
            Rectangle twoP = new Rectangle(450, 200, 200, 100);
            Rectangle back = new Rectangle(20, 20, 100, 50);

            Stroke oldStroke = g2.getStroke();
            g2.setStroke(new BasicStroke(2));
            g2.drawRect(20, 20, 100, 50);
            g2.drawRect(150, 200, 200, 100);
            g2.drawRect(450, 200, 200, 100);
            g2.setStroke(oldStroke);

            // Text
            fm = g.getFontMetrics();
            int onePWidth = fm.stringWidth("1 Player");
            int onePHeight = fm.getHeight();
            int twoPWidth = fm.stringWidth("2 Player");
            int twoPHeight = fm.getHeight();
            int backWidth = fm.stringWidth("back");
            int backHeight = fm.getHeight();
            g2.drawString("1 Player", oneP.x + (oneP.width - onePWidth) / 2,
                    oneP.y + (oneP.height - onePHeight) / 2 + fm.getAscent());
            g2.drawString("2 Player", twoP.x + (twoP.width - twoPWidth) / 2,
                    twoP.y + (twoP.height - twoPHeight) / 2 + fm.getAscent());
            g2.drawString("back", back.x + (back.width - backWidth) / 2,
                    back.y + (back.height - backHeight) / 2 + fm.getAscent());
            // Draw Color Boxes
            g2.setColor(Color.RED);
            g2.fillRect(150, 400, 50, 50);
            g2.fillRect(450, 400, 50, 50);

            g2.setColor(Color.BLUE);
            g2.fillRect(225, 400, 50, 50);
            g2.fillRect(525, 400, 50, 50);

            g2.setColor(Color.BLACK);
            g2.fillRect(300, 400, 50, 50);
            g2.fillRect(600, 400, 50, 50);

            g2.setColor(Color.GREEN);
            g2.fillRect(150, 475, 50, 50);
            g2.fillRect(450, 475, 50, 50);

            g2.setColor(Color.WHITE);
            g2.fillRect(225, 475, 50, 50);
            g2.fillRect(525, 475, 50, 50);

            g2.setColor(Color.PINK);
            g2.fillRect(300, 475, 50, 50);
            g2.fillRect(600, 475, 50, 50);

        }

        // Initialize High Score
        if (highScore.equals(""))
            highScore = this.getHighScore();

    }

    @Override
    public Dimension getPreferredSize()
    {
        return new Dimension(PANEL_WIDTH, PANEL_HEIGHT); // Sets Dimension
    }

    @Override
    public void keyTyped(KeyEvent e) // KeyListener Method
    {
    }

    @Override
    public void keyPressed(KeyEvent e) // KeyListener Method
    {
        int key = e.getKeyCode();
        System.out.println("Key Pressed --> " + key);
        if (key == 32)
        {
            screen = GAME_RUN;
            snake1.clear();
            snake1 = new Snake(400, 300, 25, 25, KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D);
            snake1.setDirection(0);
            if (player2)
            {
                snake2.clear();
                snake2 = new Snake(400, 250, 25, 25, KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT,
                        KeyEvent.VK_RIGHT);
                snake2.setDirection(0);
            }
            winner1 = false;
            winner2 = false;
        }

        snake1.keyPress(key);

        if (player2)
        {
            snake2.keyPress(key);
        }

    }

    @Override
    public void keyReleased(KeyEvent e) // KeyListener Method
    {
    }

    public String getHighScore()
    {
        FileReader readFile = null;
        BufferedReader reader = null;
        try
        {
            readFile = new FileReader("highscore.dat");
            reader = new BufferedReader(readFile);
            try
            {
                return reader.readLine();
            } catch (IOException e)
            {
                return 0 + "";
            }
        } catch (FileNotFoundException e)
        {
            return 0 + "";
        } finally
        {
            try
            {
                if (reader != null)
                    reader.close();

            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public void checkScore()
    {
        if (score1 > Integer.parseInt(highScore.split(" ")[1]))
        {
            String name = JOptionPane.showInputDialog("NEW HIGH SCORE,SET NAME:");
            highScore = name + " " + score1;
        }
        if (score2 > Integer.parseInt(highScore.split(" ")[1]))
        {
            String name = JOptionPane.showInputDialog("NEW HIGH SCORE,SET NAME:");
            highScore = name + " " + score2;
        }
        File scoreFile = new File("highscore.dat");
        if (!scoreFile.exists())
        {
            try
            {
                scoreFile.createNewFile();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        FileWriter writeFile = null;
        BufferedWriter writer = null;
        try
        {
            writeFile = new FileWriter(scoreFile);
            writer = new BufferedWriter(writeFile);
            writer.write(this.highScore);
        } catch (IOException e)
        {
            e.printStackTrace();
        } finally
        {
            try
            {
                if (writer != null)
                    writer.close();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void mouseClicked(MouseEvent e)
    {

    }

    @Override
    public void mousePressed(MouseEvent e)
    {
        Rectangle mouse = new Rectangle(e.getX(), e.getY(), 1, 1);
        System.out.println("Mouse Clicked at: " + e.getX() + ", " + e.getY());

        switch (screen)
        {
        case (GAME_RUN):
            if (snake1.getDirection() == 0 && snake2.getDirection() == 0)
            {
                // Menu Button Clicked
                if (250 < e.getX() && e.getX() < 550 && 375 < e.getY() && e.getY() < 475)
                {
                    System.out.println("Menu Opened");
                    screen = GAME_MENU;
                }
            }
            break;

        case (GAME_MENU):
            // Color Picker
            Rectangle oneP = new Rectangle(150, 200, 200, 100);
            Rectangle twoP = new Rectangle(450, 200, 200, 100);
            Rectangle back = new Rectangle(20, 20, 100, 50);
            Rectangle red1 = new Rectangle(150, 400, 50, 50);
            Rectangle blue1 = new Rectangle(225, 400, 50, 50);
            Rectangle black1 = new Rectangle(300, 400, 50, 50);
            Rectangle green1 = new Rectangle(150, 475, 50, 50);
            Rectangle white1 = new Rectangle(225, 475, 50, 50);
            Rectangle pink1 = new Rectangle(300, 475, 50, 50);
            Rectangle red2 = new Rectangle(450, 400, 50, 50);
            Rectangle blue2 = new Rectangle(525, 400, 50, 50);
            Rectangle black2 = new Rectangle(600, 400, 50, 50);
            Rectangle green2 = new Rectangle(450, 475, 50, 50);
            Rectangle white2 = new Rectangle(525, 475, 50, 50);
            Rectangle pink2 = new Rectangle(600, 475, 50, 50);

            System.out.println("Checking mouse intersections...");

            // One or Two Player
            if (mouse.intersects(oneP))
            {
                player2 = false;
                System.out.println("One Player mode selected");
                screen = GAME_RUN;
                System.out.println("Back to game run screen");
            }
            if (mouse.intersects(twoP))
            {
                player2 = true;
                System.out.println("Two Player mode selected");
                screen = GAME_RUN;
                System.out.println("Back to game run screen");
            }

            // Back Button
            if (mouse.intersects(back))
            {
                screen = GAME_RUN;
                System.out.println("Back to game run screen");
            }

            // Color Pickers
            if (mouse.intersects(red1))
            {
                color1 = Color.RED;
                System.out.println("Player 1 color set to RED");
            }
            if (mouse.intersects(red2))
            {
                color2 = Color.RED;
                System.out.println("Player 2 color set to RED");
            }

            if (mouse.intersects(blue1))
            {
                color1 = Color.BLUE;
                System.out.println("Player 1 color set to BLUE");
            }
            if (mouse.intersects(blue2))
            {
                color2 = Color.BLUE;
                System.out.println("Player 2 color set to BLUE");
            }

            if (mouse.intersects(black1))
            {
                color1 = Color.BLACK;
                System.out.println("Player 1 color set to BLACK");
            }
            if (mouse.intersects(black2))
            {
                color2 = Color.BLACK;
                System.out.println("Player 2 color set to BLACK");
            }

            if (mouse.intersects(white1))
            {
                color1 = Color.WHITE;
                System.out.println("Player 1 color set to WHITE");
            }
            if (mouse.intersects(white2))
            {
                color2 = Color.WHITE;
                System.out.println("Player 2 color set to WHITE");
            }

            if (mouse.intersects(green1))
            {
                color1 = Color.GREEN;
                System.out.println("Player 1 color set to GREEN");
            }
            if (mouse.intersects(green2))
            {
                color2 = Color.GREEN;
                System.out.println("Player 2 color set to GREEN");
            }

            if (mouse.intersects(pink1))
            {
                color1 = Color.PINK;
                System.out.println("Player 1 color set to PINK");
            }
            if (mouse.intersects(pink2))
            {
                color2 = Color.PINK;
                System.out.println("Player 2 color set to PINK");
            }

            // Resets Snakes
            snake1 = new Snake(400, 300, 25, 25, KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D); // Creates
            snake2 = new Snake(400, 250, 25, 25, KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT);

            System.out.println("Two Player? " + player2);
            System.out.println("Snake 1 Color: " + color1);
            System.out.println("Snake 2 Color: " + color2);

            // Repaint the screen to reflect color changes
            repaint();

            break;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e)
    {
    }

    @Override
    public void mouseEntered(MouseEvent e)
    {
    }

    @Override
    public void mouseExited(MouseEvent e)
    {
    }

    public static void runSnakeGUI()
    {
        JFrame frame = new JFrame("Snake Game");
        frame.add(new SnakeGUI()); // Adds Game
        frame.pack(); // Adds Dimension
        frame.setLocationRelativeTo(null); // Centers Frame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Exit when close
        frame.setVisible(true); // Makes it Visible
    }

    public static void main(String[] args)
    {
        SnakeGUI.runSnakeGUI();
    }

}
