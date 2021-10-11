package assessment_1;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;
import java.util.Collections;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class matrix extends JPanel implements MouseListener, MouseMotionListener{
	// Definition of variables that will be useful for digitizing circles
	private static final long serialVersionUID = 1L;
	private int n_cell = 20, space = 10;
	cell[][] matrix1 = new cell[n_cell][n_cell];
	private boolean drag_drawing = false;
	private static boolean error = false;
	private boolean reals_drawing = false;
	private Point initial_point, final_point;
	private int radii;
	private int s_radi = 10000;
	private int b_radi = 0;	  
	private double distance_x = 0, distance_y = 0;
	int width_screen, height_screen;

	// Main function of the class matrix
	public static void main(String[] args) {
		// Create the frame that will contain the matrix
		Frame_centrated my_frame = new Frame_centrated();
		my_frame.setVisible(true);
		my_frame.setResizable(false);
		my_frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	public matrix(int width_screen, int height_screen) {
		// Definition of variable
		this.width_screen = width_screen;
		this.height_screen = height_screen;
		double distance_x_1 = 0, distance_y_1 = 0;
		
		// Creation of the matrix which inherits the cell class for each cell
		for (int i = 0; i < matrix1.length; i++) {
			for (int j = 0; j < matrix1.length; j++) {
				
				matrix1[i][j] =  new cell(); //create cell
				if (i == 0 & j ==  0) { // Give the initial position of the first cell
					distance_x_1 = (width_screen - (n_cell)*space - n_cell*matrix1[i][j].get_width() )/2;
					distance_x = distance_x_1;
					distance_y_1 = (height_screen - (n_cell)*space - (1+n_cell)*matrix1[i][j].get_height() )/2;
					distance_y = distance_y_1;
				}
				
				// Set the positions to the cell 
				matrix1[i][j].set_position_x(distance_x);
				matrix1[i][j].set_position_y(distance_y);
				
				// Check if we have to restart the position y when we are in a new column
				if (j == (matrix1.length -1)){
					distance_y = distance_y_1;
				}
				else{
					distance_y += matrix1[i][j].get_height() + space ;
				}
			}
			
			// Check if we have to restart the position x when we are in a new row
			if ( i == (matrix1.length -1)) {
				distance_x = distance_x_1;
			}
			else {
				distance_x += matrix1[i][matrix1.length-1].get_width() + space;
			}
		}
	}
	// Method to paint the cells and the circles
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		for (int i = 0; i < matrix1.length; i++) {
			for (int j = 0; j < matrix1.length; j++) {
			
				// Go through the matrix and check if the circle touches the cell and if it does, then change the color of the cell
				if(reals_drawing & matrix1[i][j].get_state_touched() == true & !error) {
					g2.setPaint(Color.cyan);
				}else {
					g2.setPaint(Color.gray);
				}
				// Fill the cell with the previous color selected
				Rectangle2D square = new Rectangle2D.Double(matrix1[i][j].get_position_x(), matrix1[i][j].get_position_y(),
						(double)matrix1[i][j].get_width(), (double)matrix1[i][j].get_height());
				g2.fill(square); 
			}
		}
		// Check if we have already dragged the mouse and the radius is not very small and if the requirements fulfill, then create the circle 
		if (drag_drawing & !error) {
			radii = (int)(Math.hypot(initial_point.x-final_point.x, initial_point.y-final_point.y));
			g.setColor(Color.blue);
			g.drawArc((int)(initial_point.x - radii),(int)(initial_point.y - radii), radii*2, radii*2, 0, 360);
		}
		// Check if we have already released the mouse and the radius is not very small and if the requirements fulfill, then create the 2 circles 
		if (reals_drawing & !error) {
			g.setColor(Color.red);
			g.drawArc((int)(initial_point.x - s_radi),(int)(initial_point.y - s_radi), s_radi*2, s_radi*2, 0, 360);
			g.drawArc((int)(initial_point.x - b_radi),(int)(initial_point.y - b_radi), b_radi*2, b_radi*2, 0, 360);
			}
		
		// Text in the frame
		Font font = new Font("Verdana", Font.BOLD, 14);
	    g.setFont(font);
	    g.setColor(Color.black);
	    g.drawString("Please click and drag mouse to set the circle", width_screen/4+2*space, 2*space);
	    
	}
	
	public void mousePressed(MouseEvent e) { 
		// If the mouse is pressed then obtain its position and also set drag_drawing to true to draw the circle
		e = SwingUtilities.convertMouseEvent(e.getComponent(), e, this);
		drag_drawing = true;
		initial_point = e.getPoint();
	}
	public void mouseDragged(MouseEvent e) {
		e = SwingUtilities.convertMouseEvent(e.getComponent(), e, this);
		final_point = e.getPoint(); 
		// Obtain the final position of the mouse of the used when he drags it
		repaint();
	}
	
	public void mouseReleased(MouseEvent e) {
		// If the mouse is pressed then obtain its position and also set reals_drawing to true to draw the 2 circles
		e = SwingUtilities.convertMouseEvent(e.getComponent(), e, this);
		reals_drawing = true;
		
		// Call function to determine which are the cells that are touched by the circle and return 
		// the radius of the 2 circles that surrounds the cells and the variable error which represent if the circle dragged is inside the matrix
		// and the radius is not so small
		error = cell_closer( initial_point,  radii); 
		if(error) { // If the circle is outside the matrix or the radius is so small, then reset the game 
			reset();
		}
		repaint(); // call repaint to repaint the changes.
		if(reals_drawing) {
			JOptionPane.showMessageDialog(null, "Very good!");
		}
	}

	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) { repaint();}
	public void mouseMoved(MouseEvent e) {}
	public void mouseClicked(MouseEvent e) {}
		
	public boolean cell_closer(Point initial_point, int radii) {
		double x_pos, y_pos, upper_rangex, lower_rangex, upper_rangey, lower_rangey, num = 0;
		boolean error;
		int incr_k = (int)(Math.toDegrees(Math.atan((1.01*matrix1[0][0].get_width()+space)/radii)));
		System.out.println("arcotangente " + Math.toDegrees(Math.atan((1.5*matrix1[0][0].get_width()+space)/radii)));
		
		for (int i = 0; i < matrix1.length; i++) {
			for (int j = 0; j < matrix1.length; j++) {
				for (int k = (int)(incr_k/2); k < 361; k+=(incr_k)) {
					// Calculate the position x and y for all the circle generated by our mouse
					 x_pos = radii*Math.cos(Math.toRadians(k)) + initial_point.x;
					 y_pos = -radii*Math.sin(Math.toRadians(k)) + initial_point.y;
					 
					 // Obtain the 4 corners of every cell to check if the circle touches the cell or not
					 upper_rangex = matrix1[i][j].get_position_x() + matrix1[i][j].get_width() + space/2; 
					 lower_rangex = matrix1[i][j].get_position_x() - space/2;
					 upper_rangey = matrix1[i][j].get_position_y() + matrix1[i][j].get_height() + space/2;
					 lower_rangey = matrix1[i][j].get_position_y() - space/2;
					 
					 // Check if the circle is inside the matrix taking into account that all the positions of the circle
					 // must be inside the matrix
					 if( matrix1[0][0].get_position_x() <= x_pos &  x_pos <= (matrix1[n_cell-1][n_cell-1].get_position_x() +
							 matrix1[n_cell-1][n_cell-1].get_width()) & matrix1[0][0].get_position_y() <= y_pos &
							 y_pos<= (matrix1[n_cell-1][n_cell-1].get_position_y() + matrix1[n_cell-1][n_cell-1].get_height())) {
						 num++;
					 }
					 // Create arrays to add the minimum and maximum radius between the center of the circle and every cell
					 if(x_pos >= lower_rangex & x_pos <= upper_rangex & y_pos >=lower_rangey & y_pos <= upper_rangey) {
						 matrix1[i][j].set_state_touched(true);
						 Vector<Integer> vector = new Vector();
						 int radi_1 = (int)(Math.hypot(initial_point.x-upper_rangex, initial_point.y-upper_rangey));
						 int radi_2 = (int)(Math.hypot(initial_point.x-upper_rangex, initial_point.y-lower_rangey));
						 int radi_3 = (int)(Math.hypot(initial_point.x-lower_rangex, initial_point.y-upper_rangey));
						 int radi_4 = (int)(Math.hypot(initial_point.x-lower_rangex, initial_point.y-lower_rangey));
						 vector.add(radi_1);
						 vector.add(radi_2);
						 vector.add(radi_3);
						 vector.add(radi_4);
						 
						 // Select the minimum radius between the center and the cells touched 
						 if (s_radi > Collections.min(vector)) {
							 s_radi = Collections.min(vector);
						 }
						 // Select the maximum radius between the center and the cells touched 
						 if (b_radi < Collections.max(vector)) {
							 b_radi = Collections.max(vector);
						 }
					 }
				}
			}
		}
		if (num != (n_cell*n_cell*((int)((361-(incr_k/2))/incr_k)+1))) { // If the number of times of the variable num is 20x20x361,
			// it means that at all times the circle is inside the matrix, otherwise it would give a lower value.
			JOptionPane.showMessageDialog(null,"\r\n"
					+ "The circle is outside the frame, please create another circle");
			 error = true;
		}
		else if(s_radi < (int)1.5*(matrix1[n_cell-1][n_cell-1].get_width())) { 
			// of the radius is lower that the half of the width of the cell, then it is so small
			JOptionPane.showMessageDialog(null,"\r\n"
					+ "The circle is too small, please create another circle");
			 error = true;
		}
		else {
			error = false;
		}
		return error;
	}
	
	public void reset(){ // Reset the variable and create a new game
		drag_drawing = false; reals_drawing = false; error = false;
		initial_point= null; final_point = null; 
		s_radi = 10000; b_radi = 0;	 distance_x = 0; distance_y = 0; radii = 0;
		for (int i = 0; i < matrix1.length; i++) {
			for (int j = 0; j < matrix1.length; j++) {
				matrix1[i][j].set_state_touched(false);
				}
		}
	}
}

class Frame_centrated extends JFrame{ // Create a frame with respect to the size of the screen 
	int height_screen, width_screen;
	public Frame_centrated() { 
		Toolkit myscreen = Toolkit.getDefaultToolkit();	
		Dimension sizescreen = myscreen.getScreenSize();	
		height_screen = (int)(sizescreen.height/1.8);
		width_screen = (int)(sizescreen.width/1.95);
		setSize(width_screen, height_screen);
		setLocation(width_screen/4, height_screen/4);
		
		setTitle("Software Assessment 1 of Neocis"); 
		matrix matrix_printed = new matrix(width_screen, height_screen);
		
		// Add the events to the matrix to detect when the mouse is pressed, released.
		add(matrix_printed,SwingConstants.CENTER);
		addMouseMotionListener(matrix_printed);
		addMouseListener(matrix_printed);
	}
}
