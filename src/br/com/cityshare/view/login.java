package br.com.cityshare.view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;

import java.awt.Font;
import java.awt.Color;

public class login extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JTextField textField_1;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					login frame = new login();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
				    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				        if ("Nimbus".equals(info.getName())) {
				            UIManager.setLookAndFeel(info.getClassName());
				            break;
				        }
				    }
				} catch (UnsupportedLookAndFeelException e) {
				    // handle exception
				} catch (ClassNotFoundException e) {
				    // handle exception
				} catch (InstantiationException e) {
				    // handle exception
				} catch (IllegalAccessException e) {
				    // handle exception
				}
			}
		});
	}
	

	/**
	 * Create the frame.
	 */
	public login() {
		setAlwaysOnTop(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 447, 276);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		textField = new JTextField();
		textField.setBounds(20, 80, 386, 20);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JLabel lblusuario = new JLabel("Usuario :");
		lblusuario.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblusuario.setBounds(20, 55, 117, 14);
		contentPane.add(lblusuario);
		
		JLabel lblSenha = new JLabel("Senha :");
		lblSenha.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblSenha.setBounds(20, 124, 117, 14);
		contentPane.add(lblSenha);
		
		textField_1 = new JTextField();
		textField_1.setColumns(10);
		textField_1.setBounds(20, 149, 386, 20);
		contentPane.add(textField_1);
		
		JPanel panel = new JPanel();
		panel.setForeground(Color.DARK_GRAY);
		panel.setBorder(new TitledBorder(null, "Insira os dados", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(10, 11, 411, 184);
		contentPane.add(panel);
		
		JButton btnLogar = new JButton("Logar");
		btnLogar.setBounds(168, 206, 89, 23);
		contentPane.add(btnLogar);
	}
}
