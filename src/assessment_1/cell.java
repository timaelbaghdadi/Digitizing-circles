package assessment_1;

import java.awt.Dimension;
import java.awt.Toolkit;

public class cell{
	// Definition of class "cell" that will which will inherit the parent class
	private int height;
	private int width;
	private double position_x;
	private double position_y;
	private boolean touched;

	// Obtain size of the screen
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	// the screen height
	double height_display = screenSize.getHeight();
	// the screen width
	double width_display = screenSize.getWidth();
	// Features of the cell
	public cell() {
		height = 10;
		width = 10;
		position_x = 0;
		position_y = 0;
		touched = false;
	}
	
	// Methods
	public double get_position_x() { // Obtain position x of the upper right corner
		return position_x;
	}
	public double get_position_y() { // Obtain position y of the upper right corner
		return position_y;
	}
	public int get_height() { // Obtain the height of the cell
		return height;
	}
	public int get_width() { // Obtain the width of the cell
		return width;
	}
	public boolean get_state_touched() { // function to show if the cell touches the circle
		return touched;
	}
	public void set_position_x(double position_x) { // Change the position x of the upper right corner
		this.position_x = position_x;
	}
	public void set_position_y(double position_y) { // Change the position y of the upper right corner
		this.position_y = position_y;
	}
	public void set_state_touched(boolean touched) { // Change the state of the cell when the circle touches it
		this.touched = touched;
	}
}