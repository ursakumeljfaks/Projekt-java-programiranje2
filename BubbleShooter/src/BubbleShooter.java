import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class BubbleShooter extends JFrame{
	
	public static int[][] matrix = new int[24][12];
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	}
	
	public BubbleShooter(){
		super();
		
		setTitle("Bubble Shooter");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    getRootPane().putClientProperty("apple.awt.brushMetalLook", true);
	    setPreferredSize(new Dimension(800, 600));
	    setMinimumSize(new Dimension(600, 450));
	    setLayout(new BorderLayout());
	    
	    JPanel console = new JPanel();
	    add(console, BorderLayout.NORTH);

	    JButton restart = new JButton("Restart");
	    restart.addActionListener(new ActionListener() {

	    	@Override
	    	public void actionPerformed(ActionEvent e) {
	    		//zacetna postavitev
	    		repaint();
	    	}});
	    console.add(restart);

		
	}

}
