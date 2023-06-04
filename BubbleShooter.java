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
    
    static int BUBBLE_SIZE = 35;//Math.min(getWidth() / 8, getHeight() / 8);
    
    static int BOARD_ROWS = 24;
    
    static int BOARD_COLUMNS = 12;
    
    GameBoard gameBoard = new GameBoard();
    
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

                
                Bubble[][] bubbles = gameBoard.getBubbles();
                
                for (int row = 0; row < bubbles.length; row++) {
                    for (int col = 0; col < bubbles[row].length; col++) {
                        Bubble bubble = bubbles[row][col];
                        if (bubble.isEmpty()) {
                        	continue;
                        }
                        int x = col * BUBBLE_SIZE;
                        int y = row * BUBBLE_SIZE;
                        if (row % 2 == 1) {
                        	x = x + (int)(0.5* BUBBLE_SIZE);
                        }

                        g.setColor(bubble.getColor());
                        g.fillOval(x, y, BUBBLE_SIZE, BUBBLE_SIZE);
                    }
                }
            }
        };

        add(panel, BorderLayout.CENTER);

        JPanel console = new JPanel();
        add(console, BorderLayout.NORTH);
        
    }


    
    static Color[] bubbleColors = { Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.PINK};

    class GameBoard {
        private Bubble[][] bubbles;

        public GameBoard() {

            bubbles = new Bubble[BOARD_ROWS][BOARD_COLUMNS];
            Random random = new Random();

            for (int row = 0; row < bubbles.length/3; row++) {
                for (int col = 0; col < bubbles[row].length; col++) {
                    Color randomColor = bubbleColors[random.nextInt(bubbleColors.length)];                    
                    Bubble bubble = new Bubble(row, col, randomColor);
                    bubbles[row][col] = bubble;
                }
            }
            for (int row = bubbles.length/3; row < bubbles.length; row++) {
                for (int col = 0; col < bubbles[row].length; col++) {
                	Bubble bubble = new Bubble(row, col, true);
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
        private boolean empty;

        public Bubble(int row, int col, Color color) {
            this.row = row;
            this.col = col;
            this.color = color;
        }
        
        public Bubble(int row, int col, boolean empty) {
        	this(row, col, Color.WHITE);
        	this.empty = empty;        	
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
        
        public boolean isEmpty() {
            return empty;
        }
        
        public void setEmpty(boolean vr) {
            empty = vr;
        }
    }
} 