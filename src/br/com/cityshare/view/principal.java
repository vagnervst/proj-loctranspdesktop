package br.com.cityshare.view;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JMenuBar;
import javax.swing.JButton;

public class principal extends JFrame {

	private JPanel contentPane;

	public principal() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 978, 701);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JButton btnNewButton = new JButton("HOME");
		btnNewButton.setBounds(20,20,20,20);
		menuBar.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("New button");
		menuBar.add(btnNewButton_1);
		
		JButton btnNewButton_2 = new JButton("New button");
		menuBar.add(btnNewButton_2);
		
		JButton btnNewButton_3 = new JButton("New button");
		menuBar.add(btnNewButton_3);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
	}

}
