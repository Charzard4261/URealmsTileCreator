package charzard;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class G extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtC;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable() {
			public void run()
			{
				try
				{
					G frame = new G();
					frame.setVisible(true);
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public G()
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 400, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnChooseDirectory = new JButton("Choose Directory");
		btnChooseDirectory.setBackground(Color.WHITE);
		btnChooseDirectory.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnChooseDirectory.setBounds(135, 50, 130, 20);
		contentPane.add(btnChooseDirectory);
		
		txtC = new JTextField();
		txtC.setText("C:\\");
		txtC.setBounds(125, 25, 150, 20);
		contentPane.add(txtC);
		txtC.setColumns(10);
	}
}
