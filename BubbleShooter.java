import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class BubbleShooter extends JFrame {

    private static final long serialVersionUID = 1L;

    public static void main(String[] args) {
    	
            BubbleShooter bubbleShooter = new BubbleShooter();
            bubbleShooter.setVisible(true);
      
    }
    
    static int BUBBLE_SIZE = 35;
    
    static int BOARD_ROWS = 24;
    
    static int BOARD_COLUMNS = 12;
    
    static Color[] bubbleColors = { new Color(80, 171, 199), new Color(245, 66, 102),
    		new Color(91, 176, 72), new Color(230, 230, 76), new Color(165, 22, 222)};
    
    GameBoard gameBoard = new GameBoard();
    
    private Point ball = null;
    
    private Point direction;
    
    private Color ballColor;
    
    public BubbleShooter() {
        super();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getRootPane().putClientProperty("apple.awt.brushMetalLook", true);
        setSize(new Dimension(438, 600));
        setResizable(false);
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
                if (ball != null) {
                	g.setColor(ballColor);
                    int x = (int) ball.getX();
                    int y = (int) ball.getY();
                    g.fillOval(x, y, BUBBLE_SIZE, BUBBLE_SIZE);
                }

            }
        };
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (ball == null) {
                    int klikX = e.getX();
                    int klikY = e.getY();
                    ball = new Point(panel.getWidth() / 2, panel.getHeight() - BUBBLE_SIZE);
                    double angle = Math.atan2(klikY - ball.getY(), klikX - ball.getX());
                    double speed = 15;
                    direction = new Point((int) (speed * Math.cos(angle)), (int) (speed * Math.sin(angle)));
                }
            }
        });
 
        JFrame frame = new JFrame("Bubble Shooter");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(438, 600);
        frame.setResizable(false);
        frame.setLayout(new BorderLayout());
        frame.add(panel, BorderLayout.CENTER);
        frame.setVisible(true);
        
        direction = new Point(15, 6);
       
        Random random = new Random();
        ballColor = bubbleColors[random.nextInt(bubbleColors.length)];
        
        while (true) {
        	if (ball != null) {
                ball.setLocation(ball.getX() + direction.getX(), ball.getY() + direction.getY());
                
                Bubble[][] bubbles = gameBoard.getBubbles();
                int ballRow = (int) (ball.getY() / BUBBLE_SIZE);
                int ballCol = (int) (ball.getX() / BUBBLE_SIZE);
                Bubble bubble = bubbles[ballRow][ballCol];
                
                if (bubble != null && !bubble.isEmpty() && bubble.getColor().equals(ballColor) ) {
                	bubble.setColor(Color.WHITE);
                    bubble.setEmpty(true);
                    break;
                }
                
                if (ball.getX() <= 32) {
                    ball.setLocation(32, ball.getY());
                    direction.setLocation(-direction.getX(), direction.getY());
                } else if (ball.getX() >= panel.getWidth() - 32) {
                    ball.setLocation(panel.getWidth() - 32, ball.getY());
                    direction.setLocation(-direction.getX(), direction.getY());
                }
                if (ball.getY() <= 32) {
                    ball.setLocation(ball.getX(), 32);
                    direction.setLocation(direction.getX(), -direction.getY());
                } else if (ball.getY() >= panel.getHeight() - 32) {
                    ball.setLocation(ball.getX(), panel.getHeight() - 32);
                    direction.setLocation(direction.getX(), -direction.getY());
                }
                panel.repaint();
            }
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


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
        public void setRow(int row) {
        	this.row = row;
        }

        public int getCol() {
            return col;
        }
        public void setCol(int col) {
        	this.col = col ;
        }

        public Color getColor() {
            return color;
        }
        public void setColor(Color color) {
        	this.color = color ;
        }
        
        public boolean isEmpty() {
            return empty;
        }
        
        public void setEmpty(boolean vr) {
            empty = vr;
        }
    }
} 
