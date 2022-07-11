import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import java.awt.event.MouseListener;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;

public class ButtonPanel extends JPanel  // Nơi chứa button
{
	public static final long serialVersionUID = 1L;
	private static final int BUTTON_WIDTH = 200, BUTTON_HEIGHT = 100; // kích thước của button
	private JLabel[] buttons;
	private SortButtonListener listener;
	public int number = 8;
	
	public JLabel[] getButtons() {
		return buttons;
	}


	public void setButtons(JLabel[] buttons) {
		this.buttons = buttons;
	}


	public ButtonPanel(SortButtonListener listener)
	{
		super();

		this.listener = listener; // Listen action

		// Khai bao so luong nut bam
		buttons = new JLabel[number]; 
		for (int i = 0; i < buttons.length; i++)
			buttons[i] = new JLabel();

		
		initButtons(buttons[0], "bubble_button", 0);
		initButtons(buttons[1], "selection_button", 1);
		initButtons(buttons[2], "insertion_button", 2);
		initButtons(buttons[3], "quick_button", 3);
		initButtons(buttons[4], "merge_button", 4);
		initButtons(buttons[5], "random_button", 5);
		initButtons(buttons[6], "create_button", 6);
		initButtons(buttons[7], "run_button", 7);
		

		// add button to the panel
		setLayout(null);
		for (int i = 0; i < 5; i++)
		{
			buttons[i].setBounds(80 + (BUTTON_HEIGHT + 200) * i, -10, BUTTON_WIDTH, BUTTON_HEIGHT);   // căn chỉnh button theo chiều dọc
			add(buttons[i]);
		};
	
	
	for (int i = 5; i < 8; i++)
	{
		buttons[i].setBounds(220 + (BUTTON_HEIGHT + 360) * (i-5), 90, BUTTON_WIDTH, BUTTON_HEIGHT);   // căn chỉnh button theo chiều dọc
		add(buttons[i]);
	};
	}
	public void initButtons(JLabel button, String name, int id) // phương thức tạo 1 button 
	{
		button.setIcon(new ImageIcon(String.format("buttons/%s.png", name))); 
		button.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) {}

			public void mouseEntered(MouseEvent e) {
				button.setIcon(new ImageIcon(String.format("buttons/%s_entered.png", name))); // chạm vào thì đổi sang ảnh entered
			}

			public void mouseExited(MouseEvent e) {
				button.setIcon(new ImageIcon(String.format("buttons/%s.png", name))); // không chạm thì đổi sang ảnh bình thường
			}

			public void mousePressed(MouseEvent e) {
				button.setIcon(new ImageIcon(String.format("buttons/%s_pressed.png", name))); // khi ấn thì đổi sang ảnh Pressed
			}

			public void mouseReleased(MouseEvent e) {				
				listener.sortButtonClicked(id);												  // thả ra thì gọi tới phương thức SortButtonClicked
				button.setIcon(new ImageIcon(String.format("buttons/%s_entered.png", name))); // khi thả ra thì đổi sang ảnh Entered
			}
		});
	}


	public interface SortButtonListener
	{
		void sortButtonClicked(int id); 
	}


}
