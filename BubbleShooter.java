import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JPanel;



public class BubbleShooter extends JFrame {

    private static final long serialVersionUID = 1L;

    public static void main(String[] args) {
    	
            BubbleShooter bubbleShooter = new BubbleShooter();
            //bubbleShooter.setVisible(true);
      
    }
    static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    static int PANEL_HEIGHT = (int)(0.8*screenSize.getHeight());
    static int PANEL_WIDTH = PANEL_HEIGHT/2;
    static int BUBBLE_SIZE = PANEL_WIDTH/13;
    static int BOARD_ROWS = 24;
    static int BOARD_COLUMNS = 12;
    static Color[] bubbleColors = { new Color(80, 171, 199), new Color(245, 66, 102),
    		new Color(91, 176, 72), new Color(230, 230, 76), new Color(165, 22, 222)};
    
    GameBoard gameBoard = new GameBoard();
    
    
    static int START_X = PANEL_WIDTH/2 - BUBBLE_SIZE/2;
    static int START_Y = PANEL_HEIGHT - 3*BUBBLE_SIZE;
    
    private Point ball = new Point(START_X, START_Y);
    
    private Point2D.Double direction;
    
    private int score = 0;
    
    private String nic;
    
    private Color ballColor;
    
    boolean konec = false;

    
    public BubbleShooter() {
        super();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getRootPane().putClientProperty("apple.awt.brushMetalLook", true);
        setSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
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
                        
                        g.setColor(bubble.getColor());
                        g.fillOval((int)(bubble.getX()), (int)(bubble.getY()), BUBBLE_SIZE, BUBBLE_SIZE);

                    }
                }
                g.setColor(ballColor);                	
                int x = (int) Math.round(ball.getX());
                int y = (int) Math.round(ball.getY());
                g.fillOval(x, y, BUBBLE_SIZE, BUBBLE_SIZE);
                
                
                g.setColor(Color.BLACK);
                g.drawString("Score: " + score, getWidth() - 80, getHeight() - 20);
                
                if (konec){
                	g.setColor(Color.BLACK);
                    int x2 = (PANEL_WIDTH / 2 - 70); 
                    int y2 = (PANEL_HEIGHT / 2);
                    Font font = new Font("Arial", Font.BOLD, 20); 
                    g.setFont(font);
                    g.drawString("GAME OVER! :(", x2, y2);
                }

                
            }
        };
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (ball.getX()==START_X && ball.getY() == START_Y) {
                    double dx = e.getX() - ball.getX();
                    double dy = e.getY() - ball.getY();
                    double distance = Math.sqrt(dx * dx + dy * dy);
                    double speed = 10;
                    double directionX = speed * dx / distance;
                    double directionY = speed * dy / distance;
                    direction = new Point2D.Double(directionX, directionY);
                    //double klikX = e.getX();
                    //double klikY = e.getY();
                    //double angle = Math.atan2(klikY - (ball.getY()+0.5*BUBBLE_SIZE), klikX - (ball.getX()+0.5*BUBBLE_SIZE));
                    //double speed = 5;
                    //direction = new Point((int) Math.round(speed * Math.cos(angle)), (int) Math.round(speed * Math.sin(angle)));
                }
            }
        });
 
        JFrame frame = new JFrame("Bubble Shooter");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(PANEL_WIDTH, PANEL_HEIGHT);
        frame.setResizable(false);
        frame.setLayout(new BorderLayout());
        frame.add(panel, BorderLayout.CENTER);
        frame.setVisible(true);
        
       
        Random random = new Random();
        ballColor = bubbleColors[random.nextInt(bubbleColors.length)];
        
        
        while (!konec) {
        	nic = "0";   //ne sprašuj, ce tle nc ni se nc ne nardi ob kliku, nima smisla ampak dela
        	if (direction != null) {
        		
        		while (true) {

        			Bubble[][] bubbles = gameBoard.getBubbles();
        			ball.setLocation(ball.getX() + direction.getX(), ball.getY() + direction.getY());
        			if (dotakne(bubbles, ball)) {                	
        				int row = (int) (ball.getY() / BUBBLE_SIZE);
        				if ((ball.getY()/BUBBLE_SIZE)%1 > 0.5) {
        					row += 1;
        				}

        				int col = (int) (ball.getX() / BUBBLE_SIZE);
        				if (row % 2 == 1) {
        					if (Math.abs((col+0.5)*BUBBLE_SIZE - ball.getX()) > (Math.abs((col+1.5)*BUBBLE_SIZE - ball.getX()))){
        						col += 1;
        					}
        				}
        				if (row % 2 == 0) {
        					if (Math.abs((col)*BUBBLE_SIZE - ball.getX()) > (Math.abs((col+1)*BUBBLE_SIZE - ball.getX()))){
        						col += 1;
        					}
        				}

        				Bubble novi = bubbles[row][col];
        				novi.setEmpty(false);
        				novi.setColor(ballColor);
        				novi.setRow(row);
        				novi.setCol(col);
        				

        				Set<Bubble> isti_sosedi = new HashSet<Bubble>();
        				isti_sosedi.add(novi);
        				isti_sosedi.addAll(izbrise(bubbles, novi, isti_sosedi));
        				if (isti_sosedi.size() >= 3) {
        					for (Bubble sosed:isti_sosedi) {
        						sosed.setEmpty();
        					}
        					score += isti_sosedi.size() * 10;
        				}
         				if (ball.getY() >= START_Y - BUBBLE_SIZE){
        						konec = true;
        						panel.repaint();
        				}
        					
        				
        				direction = null;
        				ball.setLocation(START_X, START_Y);
        				ballColor = bubbleColors[random.nextInt(bubbleColors.length)];
        				panel.repaint();
        				
        				
        				
        				break;
        			}

        			

        			if (ball.getX() <= 0) {
        				ball.setLocation(0, ball.getY());
        				direction.setLocation(-direction.getX(), direction.getY());
        			} else if (ball.getX() >= panel.getWidth() - BUBBLE_SIZE) {
        				ball.setLocation(panel.getWidth() - BUBBLE_SIZE, ball.getY());
        				direction.setLocation(-direction.getX(), direction.getY());
        			}
        			if (ball.getY() <= BUBBLE_SIZE) {
        				ball.setLocation(ball.getX(), BUBBLE_SIZE);
        				direction.setLocation(direction.getX(), -direction.getY());
        			} else if (ball.getY() >= panel.getHeight() - BUBBLE_SIZE) {
        				ball.setLocation(ball.getX(), panel.getHeight() - BUBBLE_SIZE);
        				direction.setLocation(direction.getX(), -direction.getY());
        			}
        			panel.repaint();

        			try {
        				Thread.sleep(15);
        			} catch (InterruptedException e) {
        				e.printStackTrace();
        			}
        		}
        		
        	}
        }
    }
    public boolean dotakne(Bubble[][] bubbles, Point ball) {
    	for (int i = 0; i < bubbles.length; i++) {
			for (int j = 0; j < bubbles[i].length; j++) {
				Bubble bubble = bubbles[i][j];
				if (bubble.isEmpty()) {
					continue;
				}
				double distance = Math.sqrt(Math.pow(bubble.getX() - ball.getX(),2) + Math.pow(bubble.getY() - ball.getY(),2));
				if (distance < BUBBLE_SIZE) {
					return true;
				}
			}
    	}
    	return false;
    }
    public Set<Bubble> izbrise(Bubble[][] bubbles, Bubble ball, Set<Bubble> set){
    	Set<Bubble> sosedi = new HashSet<Bubble>();
    	if (ball.getCol()+1 < BOARD_COLUMNS) {
    		sosedi.add(bubbles[ball.getRow()][ball.getCol()+1]);
    	}
    	if (ball.getCol()-1 > 0) {
    		sosedi.add(bubbles[ball.getRow()][ball.getCol()-1]);
    	}
    	
    	if (ball.getRow()%2 == 1) {
    		if (ball.getRow()+1 < BOARD_ROWS) {
        		sosedi.add(bubbles[ball.getRow()+1][ball.getCol()]);
        		if (ball.getCol()+1 < BOARD_COLUMNS) {
            		sosedi.add(bubbles[ball.getRow()+1][ball.getCol()+1]);
        		}
    		}
    		if (ball.getRow()-1 > 0) {
        		sosedi.add(bubbles[ball.getRow()-1][ball.getCol()]);
        		if (ball.getCol()+1 < BOARD_COLUMNS) {
            		sosedi.add(bubbles[ball.getRow()-1][ball.getCol()+1]);
        		}
    		}
    	}
    	if (ball.getRow()%2 == 0) {
    		if (ball.getRow()+1 < BOARD_ROWS) {
        		sosedi.add(bubbles[ball.getRow()-1][ball.getCol()]);
        		if (ball.getCol()-1 > 0) {
            		sosedi.add(bubbles[ball.getRow()+1][ball.getCol()-1]);
        		}
    		}
    		if (ball.getRow()-1 > 0) {
        		sosedi.add(bubbles[ball.getRow()-1][ball.getCol()]);
        		if (ball.getCol()-1 > 0) {
            		sosedi.add(bubbles[ball.getRow()-1][ball.getCol()-1]);
        		}
    		}
    	}
    	
    	
    	for (Bubble sosed:sosedi) {
    		if (sosed.getColor() == ball.getColor() && !set.contains(sosed)) {
        		set.add(sosed);
        		set.addAll(izbrise(bubbles, sosed, set));
        	}
    	}
    	
		return set;
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
        private double x;
        private double y;

        public Bubble(int row, int col, Color color) {
            this.row = row;
            this.col = col;
            this.color = color;
            x = col * BUBBLE_SIZE;
            y = row * BUBBLE_SIZE;
            if (row % 2 == 1) 	
            	x = x + 0.5* BUBBLE_SIZE;
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
        public double getX() {
        	return x;
        }
        public double getY() {
        	return y;
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
        
        public void setEmpty() {
            empty = true;
            color = Color.WHITE;
        }
        public void setEmpty(boolean vr) {
        	empty = vr;
        }
    }
} 