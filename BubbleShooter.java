import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
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
import javax.swing.JLabel;
import javax.swing.JPanel;



public class BubbleShooter extends JFrame {

    private static final long serialVersionUID = 1L;

    public static void main(String[] args) {
    	
            BubbleShooter bubbleShooter = new BubbleShooter();
            
            //bubbleShooter.setVisible(true);
            
            
      
    }
    static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    static int FRAME_HEIGHT = (int)(0.8*screenSize.getHeight());
    static int FRAME_WIDTH = FRAME_HEIGHT/2;
    static int PANEL_HEIGHT;
    static int PANEL_WIDTH;
    static int BUBBLE_SIZE;// = PANEL_WIDTH/13;
    static int BOARD_ROWS = 24;
    static int BOARD_COLUMNS = 12;
    static Color[] bubbleColors = { new Color(80, 171, 199), new Color(245, 66, 102),
    		new Color(91, 176, 72), new Color(230, 230, 76), new Color(165, 22, 222)};
    
    GameBoard gameBoard;// = new GameBoard();
    
    
    static int START_X;// = PANEL_WIDTH/2 - BUBBLE_SIZE/2;
    static int START_Y;// = PANEL_HEIGHT - 3*BUBBLE_SIZE;
    
    private Point ball;// = new Point(START_X, START_Y);
    
    private Point2D.Double direction;
    
    private int score = 0;
    
    private String nic;
    
    private Color ballColor;
    
    boolean konec = false;
    
    private JLabel scoreLabel; 
    
    private int steviloStrelov = 0;
    

    
    public BubbleShooter() {
        super();
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getRootPane().putClientProperty("apple.awt.brushMetalLook", true);
        setSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
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
                
                
                //g.setColor(Color.BLACK);
                //g.drawString("Score: " + score, getWidth() - 80, getHeight() - 20);
                
                if (konec){
                	g.setColor(Color.BLACK);
                    int x2 = (PANEL_WIDTH / 2 - 70); 
                    int y2 = (PANEL_HEIGHT / 2);
                    Font font = new Font("Arial", Font.BOLD, 20); 
                    g.setFont(font);
                    g.drawString("GAME OVER!", x2, y2);
                }

              
            }
            
        };
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (ball.getX() == START_X && ball.getY() == START_Y) {
                    double dx = e.getX() - ball.getX();
                    double dy = e.getY() - ball.getY();
                    double distance = Math.sqrt(dx * dx + dy * dy);
                    double speed = 10;
                    double directionX = speed * dx / distance;
                    double directionY = speed * dy / distance;
                    direction = new Point2D.Double(directionX, directionY);                    //direction = new Point((int) Math.round(speed * Math.cos(angle)), (int) Math.round(speed * Math.sin(angle)));
                }
            }
        });
        
        JPanel scorePanel = new JPanel();
        scoreLabel = new JLabel("Score: " + score);
        scorePanel.setPreferredSize(new Dimension(PANEL_WIDTH, 40));
        scorePanel.setBackground(new Color(201, 199, 191));
        scorePanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 18));
        scorePanel.add(scoreLabel);
        
        
        JFrame frame = new JFrame("Bubble Shooter");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        frame.setResizable(false);
        frame.setLayout(new BorderLayout());
        frame.add(panel, BorderLayout.CENTER);
        frame.setVisible(true);
        frame.add(scorePanel, BorderLayout.SOUTH);
        
        PANEL_HEIGHT = panel.getHeight();
        PANEL_WIDTH = panel.getWidth();
        BUBBLE_SIZE = (int)(PANEL_WIDTH/12.5);
        START_X = PANEL_WIDTH/2 - BUBBLE_SIZE/2;
        START_Y = PANEL_HEIGHT - 3*BUBBLE_SIZE;
        ball = new Point(START_X, START_Y);
        gameBoard = new GameBoard();
        
        
        Random random = new Random();
        ballColor = bubbleColors[random.nextInt(bubbleColors.length)];
        
        
        while (!konec) {
        	nic = "0";   
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
        				if (bubbles[row][0].isIndent()) {
        					if (Math.abs((col+0.5)*BUBBLE_SIZE - ball.getX()) > (Math.abs((col+1.5)*BUBBLE_SIZE - ball.getX()))){
        						col += 1;
        					}
        				}
        				if (!bubbles[row][0].isIndent()) {
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
        					scoreLabel.setText("Score: " + score);


        					for (int i = 0; i < bubbles.length; i++) {
        						for (int j = 0; j < bubbles[j].length; j++) {
        							Bubble bubble = bubbles[i][j];
        							if (!bubble.isEmpty()) {
        								Set<Bubble> pregledani = new HashSet<Bubble>();
        								for (Bubble ball: lebdi(bubbles, bubble, pregledani)) {
        									ball.setEmpty();
        								}
        							}
        						}
        					}}

        				if (jeKonec(bubbles)) {
        					konec = true;
        					panel.repaint();
        					break;
        				}

        				steviloStrelov += 1;

        				direction = null;
        				ball.setLocation(START_X, START_Y);
        				ballColor = bubbleColors[random.nextInt(bubbleColors.length)];

        				if (steviloStrelov % 5 == 0) {

        					for (int row1 = bubbles.length-2; row1 >= 0; row1--) {
        						for (int col1 = 0; col1 < bubbles[row1].length; col1++) {
        							Bubble trenutni = bubbles[row1][col1];
        							trenutni.setCol(col1);
        							trenutni.setRow(row1+1);
        							bubbles[row1+1][col1] = trenutni; 
        						}
        					}
        					boolean indent = bubbles[1][0].isIndent();
        					for (int col2 = 0; col2 < bubbles[0].length; col2++) {
        						Color randomColor = bubbleColors[random.nextInt(bubbleColors.length)];
        						Bubble nov_bubble = new Bubble(0, col2, randomColor, !indent);
        						bubbles[0][col2] = nov_bubble;
        					}

        					if (jeKonec(bubbles)) {
        						konec = true;
        						panel.repaint();
        						break;
        					}




        				}
        				panel.repaint();




        				break;
        			}



        			if (ball.getX() <= 0) {
        				ball.setLocation(0, ball.getY());
        				direction.setLocation(-direction.getX(), direction.getY());
        			} else if (ball.getX() >= PANEL_WIDTH - BUBBLE_SIZE) {
        				ball.setLocation(PANEL_WIDTH - BUBBLE_SIZE, ball.getY());
        				direction.setLocation(-direction.getX(), direction.getY());
        			}
        			if (ball.getY() <= BUBBLE_SIZE) {
        				ball.setLocation(ball.getX(), BUBBLE_SIZE);
        				direction.setLocation(direction.getX(), -direction.getY());
        			} else if (ball.getY() >= PANEL_HEIGHT - BUBBLE_SIZE) {
        				ball.setLocation(ball.getX(), PANEL_HEIGHT - BUBBLE_SIZE);
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
    public boolean jeKonec(Bubble[][] bubbles) {
    	for (Bubble ball:bubbles[bubbles.length-1]) {
    		if (!ball.isEmpty()) {
    			return true;
    		}
    	}
    	return false;
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
    	Set<Bubble> sosedi = vsi_sosedi(bubbles,ball);
    	for (Bubble sosed:sosedi) {
    		if (sosed.getColor() == ball.getColor() && !set.contains(sosed)) {
        		set.add(sosed);
        		set.addAll(izbrise(bubbles, sosed, set));
        	}
    	}
    	
		return set;
    }
    public Set<Bubble> lebdi(Bubble[][] bubbles, Bubble ball, Set<Bubble> pregledani) {
    	pregledani.add(ball);        
        Set<Bubble> vsi = new HashSet<>();
        vsi.add(ball);
                
        if (ball.getRow() == 0) {
            vsi.clear();
        	return vsi;
        }
        
        Set<Bubble> sosedi = vsi_sosedi(bubbles, ball);
        for (Bubble sosed : sosedi) {
            if (pregledani.contains(sosed)) {
                continue;
            }
            Set<Bubble> novi = lebdi(bubbles, sosed, pregledani);
            if (novi.isEmpty()) {
                vsi.clear();
            	return vsi;
            } else {
                pregledani.addAll(novi);
                vsi.addAll(novi);
            }
        }
        
        return vsi;
    }
    
    
    public Set<Bubble> vsi_sosedi(Bubble[][] bubbles, Bubble ball){
    	Set<Bubble> sosedi = new HashSet<Bubble>();
    	if (ball.getCol()+1 < BOARD_COLUMNS) {
    		sosedi.add(bubbles[ball.getRow()][ball.getCol()+1]);
    	}
    	if (ball.getCol()-1 >= 0) {
    		sosedi.add(bubbles[ball.getRow()][ball.getCol()-1]);
    	}
    	
    	if (ball.isIndent()) {
    		if (ball.getRow()+1 < BOARD_ROWS) {
        		sosedi.add(bubbles[ball.getRow()+1][ball.getCol()]);
        		if (ball.getCol()+1 < BOARD_COLUMNS) {
            		sosedi.add(bubbles[ball.getRow()+1][ball.getCol()+1]);
        		}
    		}
    		if (ball.getRow()-1 >= 0) {
        		sosedi.add(bubbles[ball.getRow()-1][ball.getCol()]);
        		if (ball.getCol()+1 < BOARD_COLUMNS) {
            		sosedi.add(bubbles[ball.getRow()-1][ball.getCol()+1]);
        		}
    		}
    	}
    	if (!ball.isIndent()) {
    		if (ball.getRow()+1 < BOARD_ROWS) {
        		sosedi.add(bubbles[ball.getRow()+1][ball.getCol()]);
        		if (ball.getCol()-1 >= 0) {
            		sosedi.add(bubbles[ball.getRow()+1][ball.getCol()-1]);
        		}
    		}
    		if (ball.getRow()-1 >= 0) {
        		sosedi.add(bubbles[ball.getRow()-1][ball.getCol()]);
        		if (ball.getCol()-1 >= 0) {
            		sosedi.add(bubbles[ball.getRow()-1][ball.getCol()-1]);
        		}
    		}
    	}
    	Set<Bubble> pravi_sosedi = new HashSet<Bubble>();
    	for (Bubble sosed:sosedi) {
    		if (!sosed.isEmpty()) {
    			pravi_sosedi.add(sosed);
    		}
    	}
    	return pravi_sosedi;
    }

 class GameBoard {
        private Bubble[][] bubbles;

        public GameBoard() {

            bubbles = new Bubble[BOARD_ROWS][BOARD_COLUMNS];
            Random random = new Random();

            for (int row = 0; row < bubbles.length/3; row++) {
                for (int col = 0; col < bubbles[row].length; col++) {
                    Color randomColor = bubbleColors[random.nextInt(bubbleColors.length)];
                    boolean indent = row%2==1? true:false;
                    Bubble bubble = new Bubble(row, col, randomColor, indent);
                    bubbles[row][col] = bubble;
                }
            }
            for (int row = bubbles.length/3; row < bubbles.length; row++) {
                for (int col = 0; col < bubbles[row].length; col++) {
                	boolean indent = row%2==1? true:false;
                	Bubble bubble = new Bubble(row, col, true,indent);
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
        private boolean indent;

        public Bubble(int row, int col, Color color, boolean indent) {
            this.row = row;
            this.col = col;
            this.color = color;
            this.indent = indent;
            x = col * BUBBLE_SIZE;
            y = row * BUBBLE_SIZE;
            if (indent) 	
            	x = x + 0.5* BUBBLE_SIZE;
        }
        
        public Bubble(int row, int col, boolean empty, boolean indent) {
        	this(row, col, Color.WHITE, indent);
        	this.empty = empty;        	
        }

        public int getRow() {
            return row;
        }
        public void setRow(int row) {
        	this.row = row;
        	x = col * BUBBLE_SIZE;
            y = row * BUBBLE_SIZE;
            if (indent) 	
            	x = x + 0.5* BUBBLE_SIZE;
        }

        public int getCol() {
            return col;
        }
        public void setCol(int col) {
        	this.col = col;
        	x = col * BUBBLE_SIZE;
            y = row * BUBBLE_SIZE;
            if (indent) 	
            	x = x + 0.5* BUBBLE_SIZE;
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
        public boolean isIndent() {
        	return indent;
        }
        public void setIndent(boolean vr) {
        	indent = vr;
        }
    }
} 