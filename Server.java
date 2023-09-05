import javax.swing.BorderFactory;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.net.*;

public class Server extends JFrame {
	
	ServerSocket server;
	Socket socket;
	BufferedReader br;//read
	PrintWriter out;//write
	
	public JLabel heading=new JLabel("SERVER.");
	public JTextArea msgarea=new JTextArea();
	public JTextField msgfield=new JTextField();
	public Server()  
	{
		try {
		server=new ServerSocket(7777);
		System.out.println("server is ready to accept connection.");
		System.out.println("waiting");
		socket=server.accept();
		InputStreamReader r=new InputStreamReader(socket.getInputStream());//reading data comes from other
   		br=new BufferedReader(r);  
   		out=new PrintWriter(socket.getOutputStream());;//writing and sending data to others
		createGUI();
		handleEvents();
		startread();
		startwrite();
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private void handleEvents()
	{
		msgfield.addKeyListener(new KeyListener(){
			@Override
			public void keyTyped(KeyEvent e) {				
			}
			@Override
			public void keyPressed(KeyEvent e) {				
			}
			@Override
			public void keyReleased(KeyEvent e) {
				System.out.print("key relesed "+e.getKeyCode());
				if(e.getKeyCode()==10) {
					String contenttosend=msgfield.getText();
					out.println(contenttosend);
					out.flush();
					msgfield.setText("");
					msgfield.requestFocus();
				}
			}
		});
	}
	
	private void createGUI() 
	{
		this.setTitle("SERVER");
		this.setSize(350,600);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		this.setLayout(new BorderLayout());
		heading.setHorizontalAlignment(SwingConstants.CENTER);
		this.add(heading,BorderLayout.NORTH);
		this.add(msgarea,BorderLayout.CENTER);
		this.add(msgfield,BorderLayout.SOUTH);
		heading.setBorder(BorderFactory.createEmptyBorder(20,20,12,20));
	}
	
	public void startread() 
	{
		Runnable r1=()->
		{
			System.out.println("reader started");
			while(true) 
			{
				try 
				{
					String msg=br.readLine();
					if(msg.equals("exit")) {
					System.out.println("exit");
					JOptionPane.showMessageDialog(this,"Server Terminated the chat");
					break;
					}
					msgarea.append("server : "+msg +"\n");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}; 
		new Thread(r1).start();
	}
	
	
	private void startwrite() 
	{		
		Runnable r2=()->
		{
			while(true) 
			{
				try 
				{
					BufferedReader br1=new BufferedReader(new InputStreamReader(System.in));
					String containt =br1.readLine();
					out.println(containt);
					out.flush();
				} catch (Exception e) 
				{
					e.printStackTrace();
				}
			}
		};
		new Thread(r2).start();
	}
	public static void main(String[] args) throws Exception {
		System.out.println("THIS IS Server..going to start server");
		new Server();
	}
}
