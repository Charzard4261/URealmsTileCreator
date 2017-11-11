package charzard;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URISyntaxException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;

public class GUI {

	GUI g;
	JFrame frame;
	JPanel panel;
	JButton drc;
	JTextField dr;
	JToggleButton dbb;
	JButton create;
	JTextArea msgs;

	private boolean dropbox = false;

	public GUI()
	{
		g = this;
		EventQueue.invokeLater(new Runnable() {
			public void run()
			{
				try
				{
					initUI();
				} catch (URISyntaxException e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	public void initUI() throws URISyntaxException
	{
		frame = new JFrame();
		frame.setBounds(600, 300, 400, 300);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		panel = new JPanel();
		panel.setLayout(null);
		frame.setContentPane(panel);

		dr = new JTextField();
		dr.setText(new File(GUI.class.getProtectionDomain().getCodeSource().getLocation().toURI()
				.getPath()).getParentFile().getPath().replace('\\', '/'));
		dr.setBounds(100, 25, 200, 20);
		panel.add(dr);
		dr.setColumns(10);

		drc = new JButton("Choose Directory");
		drc.setBounds(135, 50, 130, 20);
		drc.setBackground(Color.WHITE);
		drc.setFont(new Font("Tahoma", Font.PLAIN, 12));
		panel.add(drc);
		drc.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent paramActionEvent)
			{
				chooseDirectory();
			}
		});

		dbb = new JToggleButton();
		dbb.setBounds(0, 0, 32, 32);
		dbb.setIcon(new ImageIcon(new ImageIcon(GUI.class.getResource("/charzard/dropbox.png"))
				.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH)));
		dbb.setSelectedIcon(new ImageIcon(new ImageIcon(GUI.class
				.getResource("/charzard/dropboxS.png")).getImage().getScaledInstance(32, 32,
				Image.SCALE_SMOOTH)));
		dbb.setBorderPainted(false);
		dbb.setContentAreaFilled(false);
		panel.add(dbb);
		dbb.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				dropbox = !dropbox;
			}
		});

		create = new JButton("Create");
		create.setBounds(150, 75, 100, 25);
		panel.add(create);
		create.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				drc.setEnabled(false);
				dr.setEnabled(false);
				create.setEnabled(false);

				new Thread(new Runnable() {

					@Override
					public void run()
					{
						new Startup(dr.getText(), dropbox, g);

					}
				}).start();
			}
		});

		msgs = new JTextArea();
		JScrollPane p = new JScrollPane(msgs);
		p.setBounds(50, 125, 300, 100);
		panel.add(p);

		frame.setVisible(true);
	}

	public void chooseDirectory()
	{
		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(new java.io.File("."));
		chooser.setDialogTitle("Choose a directory");
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setAcceptAllFileFilterUsed(false);
		if (chooser.showOpenDialog(drc) == JFileChooser.APPROVE_OPTION)
		{
			dr.setText("" + chooser.getSelectedFile());
		}
	}

	public void message(String s)
	{
		msgs.setText(msgs.getText() + s + "\n");
	}

	public void finish()
	{
		drc.setEnabled(true);
		dr.setEnabled(true);
		create.setEnabled(true);
	}

}
