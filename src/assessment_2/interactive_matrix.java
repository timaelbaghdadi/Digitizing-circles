package assessment_2;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;
import java.util.Collections;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import assessment_1.cell;

public class interactive_matrix extends JPanel implements MouseListener, ActionListener{
	// Definition of variables that will be useful to toggle points on the grid 
	private static final long serialVersionUID = 1L;
	private int n_cell = 20, space = 10;
	cell[][] matrix1 = new cell[n_cell][n_cell];
	private boolean select_cell = false, generate_circle = false;
	private Point initial_point;
	int radii = 0, max_x, max_y, min_x, min_y, x_average = 0, y_average = 0, center = 0, new_centery = 0, new_centerx = 0, width_screen, height_screen;
	double error = 40, error_radius = 100, distance_x = 0, distance_y = 0, distance_x_1 = 0, distance_y_1 = 0, opt_x, opt_y, opt_r;
	Vector<Integer> vector_pos_x = new Vector();
	Vector<Integer> vector_pos_y = new Vector();
	Vector<Integer> pointsx = new Vector();
	Vector<Integer> pointsy = new Vector();
	
	public static void main(String[] args) {
		Frame_centrated frame1 = new Frame_centrated();
	}
	// Main function of the class interactive_matrix
	
	public interactive_matrix(int width_screen, int height_screen) {
		// Definition of variable
		this.width_screen = width_screen;
		this.height_screen = height_screen;
		
		// Creation of the matrix which inherits the cell class for each cell
		for (int i = 0; i < matrix1.length; i++) {
			for (int j = 0; j < matrix1.length; j++) {
				
				//create cell
				matrix1[i][j] =  new cell(); //create cell
				if (i == 0 & j ==  0) { // Give the initial position of the first cell
					distance_x_1 = (width_screen - (n_cell)*space - (12+n_cell)*matrix1[i][j].get_width() )/1.2;
					distance_x = distance_x_1;
					distance_y_1 = (height_screen - (n_cell)*space - (5+n_cell)*matrix1[i][j].get_height() )/2;
					distance_y = distance_y_1;
				}
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
				if(select_cell & matrix1[i][j].get_state_touched() == true) {
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
		// Check if we have already clicked the button and create the circle 
		if (generate_circle) {
			g.setColor(Color.blue);
			g.drawArc((int)(opt_x - opt_r), (int)(opt_y - opt_r), (int)(2*opt_r) , (int)(2*opt_r), 0, 360);
			
		}
		// Text in the frame
		Font font = new Font("Verdana", Font.BOLD, 14);
	    g.setFont(font);
	    g.setColor(Color.black);
	    g.drawString("Please toggle points on the grid to create a circle", width_screen/5, 2*space);
	}
	public void mouseClicked(MouseEvent e) {}
	
	public void mousePressed(MouseEvent e) { 
		// If the mouse is pressed then obtain its position and also set select_cell to true to draw the circle
		e = SwingUtilities.convertMouseEvent(e.getComponent(), e, this);
		select_cell = true;
		initial_point = e.getPoint();
		// Call function to determine which are the cells that are touched by the mouse 
		cell_closer(initial_point);
	}
	
	public void mouseReleased(MouseEvent e) {repaint();}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	
	public void cell_closer(Point initial_point) {
		int x_pos, y_pos, upper_rangex, lower_rangex, upper_rangey, lower_rangey;
		for (int i = 0; i < matrix1.length; i++) {
			for (int j = 0; j < matrix1.length; j++) {
				
				// Obtain position of the mouse
				x_pos = (int) initial_point.getX();
				y_pos = (int) initial_point.getY();
				
				// Obtain the 4 corners of every cell to check if the circle touches the cell or not
				upper_rangex = (int) matrix1[i][j].get_position_x() + matrix1[i][j].get_width(); 
				lower_rangex = (int) matrix1[i][j].get_position_x();
				upper_rangey = (int) matrix1[i][j].get_position_y() + matrix1[i][j].get_height();
				lower_rangey = (int) matrix1[i][j].get_position_y();
				vector_pos_y.add(upper_rangey);
				vector_pos_x.add(upper_rangex);
				vector_pos_y.add(lower_rangey);
				vector_pos_x.add(lower_rangex);
				
				// Check if the selection is inside the matrix taking into account that all the positions of the circle
				// must be inside the matrix
				 if(x_pos >= lower_rangex & x_pos <= upper_rangex & y_pos >=lower_rangey & y_pos <= upper_rangey) {
					 if (matrix1[i][j].get_state_touched() == false) {
						 
						 // if the cell is not touched, it changes state to touched and save the position in the vector
						 matrix1[i][j].set_state_touched(true);
						 pointsx.add((int)(lower_rangex + matrix1[i][j].get_width()/2));
						 pointsy.add((int)(lower_rangey + matrix1[i][j].get_height()/2));
						 
					 }else { // Otherwise, change the state to untouched and remove the positions from the vector
						 matrix1[i][j].set_state_touched(false);
						 pointsx.remove(Integer.valueOf(lower_rangex + matrix1[i][j].get_width()/2));
						 pointsy.remove(Integer.valueOf(lower_rangey + matrix1[i][j].get_height()/2));
					 }
				 }
			}
		}
	}
	
	public void fit_center() {
		// Generate a grid around the selected points and obtain the radius between each position of the grid
		// to the selected points and choose the center whose radius is similar to the different points generated
		
		int number_attempsx = 5, number_attempsy = 5; //
		for (int x = (int) (Collections.min(pointsx)); x < (Collections.max(pointsx)) ;  x+= number_attempsx ) {
			for (int y = (int) (Collections.min(pointsy)); y < (Collections.max(pointsy)) ;  y+= number_attempsy ) {
				
				// Call function calc_radii to calculate the radius between the center (point of the grid) and the generated points
				radii = calc_radii(pointsy, pointsx, x, y);
				
				// If the difference between the different radius to each point generated is smaller than a threshold, then update 
				// the positions and the radii
				if (error_radius < error) {
					error = error_radius;
					opt_x = x;
					opt_y = y;
					opt_r = radii;	
				}
			}
		}
		// If during the grid generated, the difference between the different radius is really big because 
		// the chosen points do not respect the contour of any circle (because the points have rarely 
		// been chosen), then the program calculates the center as the average of the coordinates of the points
		if (opt_x == 0 && opt_y == 0) {
			int prov_x = 0, prov_y = 0;
			 for (int i = 0; i < pointsx.size(); i++) {
				 prov_x += pointsx.get(i);
				 prov_y += pointsy.get(i);
			}
			opt_x = (float) prov_x/ pointsx.size();
			opt_y = (float) prov_y/ pointsx.size();	
			opt_r = calc_radii(pointsy,pointsx, (int)opt_x, (int)opt_y);
		} 		
	}
	
	// To find the optimum center and the radius, the following function calculates every radius between
	// every point of the mesh generated and the initial points and the difference between them
	// finally, it return the maximum radius to ensure that the points are inside the circle
	public int calc_radii(Vector<Integer> pointsy, Vector<Integer> pointsx, int x, int y) {
		Vector<Integer> radii_vec = new Vector();
		for(int i = 0; i <= pointsy.size()-1; i++) {	
			int relat_dist_x = Math.abs(pointsx.get(i) - x);
			int relat_dist_y = Math.abs(pointsy.get(i) - y);			
			int radii_v = (int)(Math.hypot(relat_dist_x,relat_dist_y));
			radii_vec.add(radii_v);			
		}
		error_radius = Collections.max(radii_vec) - Collections.min(radii_vec);
		radii = Collections.max(radii_vec);
		radii_vec.clear();
		return radii;
	}
	
	public void actionPerformed(ActionEvent e) {
		// When the button generate is clicked call the function to find the optimized center
		// In addition, set generate_circle to draw the center
		fit_center();
		generate_circle = true;
		repaint();
	}
}

class Frame_centrated extends JPanel { // Create a frame with respect to the size of the screen 
	int height_screen, width_screen;
	JFrame frame = new JFrame("Software Assessment 2 of Neocis");
	
	public Frame_centrated(){
		Toolkit myscreen = Toolkit.getDefaultToolkit();	
		Dimension sizescreen = myscreen.getScreenSize();	
		height_screen = (int)(sizescreen.height/1.7);
		width_screen = (int)(sizescreen.width/2.3);
			
		JPanel panelForButtons = new JPanel();
	    panelForButtons.setBackground(Color.GRAY);
		interactive_matrix matrix_printed = new interactive_matrix(width_screen, height_screen);
		add(matrix_printed);
		matrix_printed.addMouseListener(matrix_printed);	
		frame.add(matrix_printed, BorderLayout.CENTER);
	
		JPanel panelForGenerate = new JPanel();
		panelForGenerate.setBackground(Color.GRAY);
		
		JButton ButtonGenerate = new JButton("Generate");
		panelForGenerate.add(ButtonGenerate); 
		ButtonGenerate.addActionListener(matrix_printed);;	
		
	    frame.add(panelForGenerate, BorderLayout.PAGE_END);
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.setVisible(true);	    
	    frame.setSize(width_screen, height_screen);
	    frame.setLocationRelativeTo(null);
		frame.setLocation(width_screen/4, height_screen/4);       
	}
}
