import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;
import java.util.Set;
import java.util.HashSet;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class BubbleShooter extends JFrame {

    private static final long serialVersionUID = 1L;

    public static void main(String[] args) {
    	
            BubbleShooter bubbleShooter = new BubbleShooter();
            //bubbleShooter.setVisible(true);
      
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
        setSize(new Dimension(455, 900));
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
                    int x = (int) (ball.getX());
                    int y = (int) (ball.getY());
                    g.fillOval(x, y, BUBBLE_SIZE, BUBBLE_SIZE);
                }
                else {
                	g.fillOval(202, 820 , BUBBLE_SIZE, BUBBLE_SIZE);
                }

            }
        };
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (ball == null) {
                    int klikX = e.getX();
                    int klikY = e.getY();
                    ball = new Point(202, 820);
                    double angle = Math.atan2(klikY - (ball.getY()+0.5*BUBBLE_SIZE), klikX - (ball.getX()+0.5*BUBBLE_SIZE));
                    double speed = 5;
                    direction = new Point((int) (speed * Math.cos(angle)), (int) (speed * Math.sin(angle)));
                }
            }
        });
 
        JFrame frame = new JFrame("Bubble Shooter");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(455, 900);
        frame.setResizable(false);
        frame.setLayout(new BorderLayout());
        frame.add(panel, BorderLayout.CENTER);
        frame.setVisible(true);
        
        //direction = new Point(15, 6); //nevem zakaj to rabva
       
        Random random = new Random();
        
        while (true) {
        	ballColor = bubbleColors[random.nextInt(bubbleColors.length)];
        	if (ball != null) {
        		
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
        				}

        				ball = null;
        				panel.repaint();
        				
        				break;
        			}

        			

        			if (ball.getX() <= 0) {
        				ball.setLocation(0, ball.getY());
        				direction.setLocation(-direction.getX(), direction.getY());
        			} else if (ball.getX() >= panel.getWidth() - 35) {
        				ball.setLocation(panel.getWidth() - 35, ball.getY());
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

        			try {
        				Thread.sleep(10);
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
        private int x;
        private int y;

        public Bubble(int row, int col, Color color) {
            this.row = row;
            this.col = col;
            this.color = color;
            x = col * BUBBLE_SIZE;
            y = row * BUBBLE_SIZE;
            if (row % 2 == 1) 	
            	x = x + (int)(0.5* BUBBLE_SIZE);
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
        public int getX() {
        	return x;
        }
        public int getY() {
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