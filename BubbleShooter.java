import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class BubbleShooter extends JFrame {

    private static final long serialVersionUID = 1L;

    public static void main(String[] args) {
    	
            BubbleShooter bubbleShooter = new BubbleShooter();
            bubbleShooter.setVisible(true);
      
    }

    public BubbleShooter() {
        super();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getRootPane().putClientProperty("apple.awt.brushMetalLook", true);
        setPreferredSize(new Dimension(800, 600));
        setMinimumSize(new Dimension(450, 450));
        setLayout(new BorderLayout());

        JPanel panel = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);

                int bubbleSize = 35;//Math.min(getWidth() / 8, getHeight() / 8);
                GameBoard gameBoard = new GameBoard();

                for (int row = 0; row < 8; row++) {
                    for (int col = 0; col < 13; col++) {
                        Bubble bubble = gameBoard.getBubbles()[row][col];
                        if (row % 2 == 1 && (col == 0 || col == 12)) {
                            continue;
                        }
                        int x = col * bubbleSize;
                        int y = row * bubbleSize;

                        g.setColor(bubble.getColor());
                        g.fillOval(x, y, bubbleSize, bubbleSize);
                    }
                }
            }
        };

        add(panel, BorderLayout.CENTER);

        JPanel console = new JPanel();
        add(console, BorderLayout.NORTH);
    }


    
    String[] bubbleColors = { "red", "blue", "green", "yellow", "purple" };

    class GameBoard {
        private Bubble[][] bubbles;

        public GameBoard() {

            bubbles = new Bubble[8][13];
            Random random = new Random();

            for (int row = 0; row < 8; row++) {
                for (int col = 0; col < 13; col++) {
                    Color randomColor = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
                    if (row % 2 == 1 && (col == 0 || col == 12)) {
                        continue;
                    }
                    Bubble bubble = new Bubble(row, col, randomColor);
                    bubbles[row][col] = bubble;
                }
            }
        }

        public Bubble[][] getBubbles() {
            return bubbles;
        }
    }

    public class Bubble {
        private int row;
        private int col;
        private Color color;

        public Bubble(int row, int col, Color color) {
            this.row = row;
            this.col = col;
            this.color = color;
        }

        public int getRow() {
            return row;
        }

        public int getCol() {
            return col;
        }

        public Color getColor() {
            return color;
        }
    }
} 

