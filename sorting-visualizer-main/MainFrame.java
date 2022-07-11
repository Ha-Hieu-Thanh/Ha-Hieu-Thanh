import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JFormattedTextField;
import javax.swing.JSlider;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.BorderFactory;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

import java.awt.Font;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.image.BufferStrategy;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

import java.text.NumberFormat;

public class MainFrame extends JFrame implements PropertyChangeListener,
	   ChangeListener, Visualizer.SortedListener,
	   ButtonPanel.SortButtonListener, MyCanvas.VisualizerProvider
{
	public static final long serialVersionUID = 10L;
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				
				new MainFrame().setVisible(true);
			}
		});
	}

	private static final int WIDTH = 1920, HEIGHT = 1080;
	private static final int CAPACITY = 50, FPS = 100;
	private static JPanel mainPanel;
	private JPanel inputPanel;
	private JPanel sliderPanel;
	private JPanel inforPanel;
	private ButtonPanel buttonPanel;
	private JLabel capacityLabel, fpsLabel, timeLabel, compLabel, swapLabel, fpsLabelShow;
	private JFormattedTextField capacityField;
	private JSlider fpsSlider;
	private MyCanvas canvas;
	private Visualizer visualizer;
    
	public MainFrame()
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setMaximumSize(new Dimension(WIDTH, HEIGHT));
		setMinimumSize(new Dimension(WIDTH, HEIGHT));
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setLocationRelativeTo(null);
		setResizable(true);
		setBackground(ColorManager.BACKGROUND);
		setTitle("Group 14 - OOP - Sorting Visualization");
		initialize();
	}


	// initialize components
	private void initialize()
	{	
		mainPanel = new JPanel();
		mainPanel.setLayout(null);
		mainPanel.setBackground(ColorManager.BACKGROUND);
		add(mainPanel);
		// add buttons panel
		buttonPanel = new ButtonPanel(this);
		buttonPanel.setBounds(0, 600, WIDTH , 300);
		buttonPanel.setBackground(ColorManager.BACKGROUND);
		mainPanel.add(buttonPanel);
		
		// add canvas
		canvas = new MyCanvas(this);
		int cWidth = WIDTH ;
		int cHeight = HEIGHT - 560;
		canvas.setFocusable(false);
		canvas.setMaximumSize(new Dimension(cWidth, cHeight));
		canvas.setMinimumSize(new Dimension(cWidth, cHeight));
		canvas.setPreferredSize(new Dimension(cWidth, cHeight));
		canvas.setBounds(0, 60, cWidth - 390, cHeight);
		mainPanel.add(canvas);
		pack();
		// sorting visualizer
		visualizer = new Visualizer(CAPACITY, FPS, this);


		// create an input field for capacity
		// labels
		capacityLabel = new JLabel("Columns");
		capacityLabel.setForeground(ColorManager.TEXT);
		capacityLabel.setFont(new Font(null, Font.BOLD, 15));

		// capacity input fields
		NumberFormat format = NumberFormat.getNumberInstance();
		MyFormatter formatter = new MyFormatter(format);
		formatter.setValueClass(Integer.class);
		formatter.setMinimum(0);
		formatter.setMaximum(300);
		formatter.setAllowsInvalid(false);
		/* important -> onChange */
		formatter.setCommitsOnValidEdit(true);

		capacityField = new JFormattedTextField(formatter);
		capacityField.setValue(CAPACITY);
		capacityField.setColumns(3);
		capacityField.setFont(new Font(null, Font.PLAIN, 15));
		capacityField.setForeground(ColorManager.TEXT);
		capacityField.setBackground(ColorManager.CANVAS_BACKGROUND);
		capacityField.setCaretColor(ColorManager.BAR_YELLOW);
		capacityField.setBorder(BorderFactory.createLineBorder(ColorManager.FIELD_BORDER, 1));
		capacityField.addPropertyChangeListener("value", this);

		capacityLabel.setLabelFor(capacityField);

		// input panel
		inputPanel = new JPanel(new GridLayout(1, 0));
		inputPanel.add(capacityLabel);
		inputPanel.add(capacityField);
		inputPanel.setBackground(ColorManager.BACKGROUND);
		inputPanel.setBounds(25, 20, 170, 30);
		mainPanel.add(inputPanel);


		// create slider for fps
		// label
		fpsLabel = new JLabel("FPS    ");
		fpsLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		fpsLabel.setFont(new Font(null, Font.BOLD, 15));
		fpsLabel.setForeground(ColorManager.TEXT);

		// slider
		fpsSlider = new JSlider(JSlider.HORIZONTAL, 0, 200, FPS);
		fpsSlider.setMajorTickSpacing(50);
		fpsSlider.setMinorTickSpacing(50);
		fpsSlider.setPaintTicks(false);
		fpsSlider.setPaintLabels(true);
		fpsSlider.setPaintTrack(true);
		fpsSlider.setForeground(ColorManager.TEXT);
		fpsSlider.setBackground(ColorManager.BACKGROUND);
		fpsSlider.addChangeListener(this);

		// slider panel
		sliderPanel = new JPanel();
		sliderPanel.setLayout(new BoxLayout(sliderPanel, BoxLayout.X_AXIS));
		sliderPanel.setBackground(ColorManager.BACKGROUND);
		sliderPanel.add(fpsLabel);
		sliderPanel.add(fpsSlider);

		sliderPanel.setBounds(250, 10, 200, 50);
		mainPanel.add(sliderPanel);


		// statistics
		// elapsed time
		timeLabel = new JLabel("Elapsed Time: 0 µs");
		timeLabel.setFont(new Font(null, Font.PLAIN, 15));
		timeLabel.setForeground(ColorManager.TEXT_RED);

		// comparisons
		compLabel = new JLabel("Comparisons: 0");
		compLabel.setFont(new Font(null, Font.PLAIN, 15));
		compLabel.setForeground(ColorManager.TEXT_YELLOW);

		// swapping
		swapLabel = new JLabel("Swaps: 0");
		swapLabel.setFont(new Font(null, Font.PLAIN, 15));
		swapLabel.setForeground(ColorManager.TEXT_GREEN);
		
		// FPS
		fpsLabelShow = new JLabel("FPS: 100");
		fpsLabelShow.setFont(new Font(null, Font.PLAIN, 15));
		fpsLabelShow.setForeground(Color.WHITE);
		
		// statistics panel
		inforPanel = new JPanel(new GridLayout(1, 0));
		inforPanel.setAlignmentX(JPanel.CENTER_ALIGNMENT);
		inforPanel.add(timeLabel);
		inforPanel.add(compLabel);
		inforPanel.add(swapLabel);
		inforPanel.add(fpsLabelShow);
		inforPanel.setBackground(ColorManager.BACKGROUND);
		inforPanel.setBounds(700, 20, 1000, 30);
		mainPanel.add(inforPanel);
	}


	/* IMPLEMENTED METHODS */

	// the capacity is changed
	public void propertyChange(PropertyChangeEvent e)
	{
		// update capacity
		int value = ((Number)capacityField.getValue()).intValue();
		visualizer.setCapacity(value);
	}


	// the speed (fps) is changed
	public void stateChanged(ChangeEvent e)
	{
		if (!fpsSlider.getValueIsAdjusting())
		{	int value = (int) fpsSlider.getValue();
			if (value == 0) {
				visualizer.setFPS(1);
				fpsLabelShow.setText("FPS: "+1);
			}
			else {
			visualizer.setFPS(value);
			fpsLabelShow.setText("FPS: "+value);
			}
		}
	}


	// button clicked
	public void sortButtonClicked(int id)
	{
		switch (id)
		{
			case 0:  // bubble button
				visualizer.bubbleSort();
				//visualizer.runAll();
				break;
			case 1:  // selection button
				visualizer.selectionSort();
				break;
			case 2:  // insertion button
				visualizer.insertionSort();
				break;
			case 3:  // quick button
				visualizer.quickSort();
				break;
			case 4:  // merge button
				visualizer.mergeSort();
				break;
			case 5:  // random button
				visualizer.createRandomArray(canvas.getWidth(), canvas.getHeight());
				onArraySorted(0, 0, 0);
				break;
			case 6: // create button 
				visualizer.createArray(canvas.getWidth(), canvas.getHeight());
				onArraySorted(0, 0, 0);
				capacityField.setValue(visualizer.getCapacity());
				break;
			case 7: // run all button
				visualizer.runAll();
				break;
		}
	}


	// draw the array
	public void onDrawArray()
	{
		if (visualizer != null)
			visualizer.drawArray();
	}


	// showing statistics when sorting is completed
	public void onArraySorted(long elapsedTime, int comp, int swapping)
	{
		timeLabel.setText("Elapsed Time: " + (int)(elapsedTime/1000.0) + " µs");
		compLabel.setText("Comparisons: " + comp);
		swapLabel.setText("Swaps: " + swapping);
	}


	// return the graphics for drawing
	public BufferStrategy getBufferStrategy()
	{
		BufferStrategy bs = canvas.getBufferStrategy();
		if (bs == null)
		{
			canvas.createBufferStrategy(2);
			bs = canvas.getBufferStrategy();
		}

		return bs;
	} 
	
	    
}