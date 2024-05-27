import java.net.*;

import javax.swing.BorderFactory;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.Font;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.BorderLayout;
import javax.swing.SwingConstants;

import java.io.*;

public class Client extends JFrame {

    Socket socket;

    BufferedReader br;
    PrintWriter out;

    private JLabel heading=new JLabel("Client Area");
    private JTextArea messageArea=new JTextArea();
    private JTextField messageInput=new JTextField();
    private Font font=new Font("Roboto",Font.PLAIN,20);




    public Client()
    {
        try {
               System.out.println("Sending request to server");
               socket=new Socket("127.0.0.1",7777);
               System.out.println("Connection done...");

               br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
               out=new PrintWriter(socket.getOutputStream());
              createGUI();
              handleEvents();
              startReading();
             //startWriting();



        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private void handleEvents() {
        messageInput.addKeyListener(new KeyListener() {
            public void keyTyped(KeyEvent e) {
    
            }
    
            public void keyPressed(KeyEvent e) {
    
            }
    
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == 10) {
                    System.out.println("You have pressed Enter key");
                    String contentToSend = messageInput.getText();
                    messageArea.append("Me: " + contentToSend + "\n");
                    out.println(contentToSend);
                    out.flush();
                    messageInput.setText("");
                    messageInput.requestFocus();
                }
            }
        });
    }
    
    
    

    private void createGUI(){
        this.setTitle("Client Messanger[END]");
        this.setSize(600,600);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        heading.setFont(font);
        messageArea.setFont(font);
        messageInput.setFont(font);
        
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        messageArea.setEditable(false);

        this.setLayout(new BorderLayout());

        this.add(heading,BorderLayout.NORTH);
        JScrollPane JScrollPane=new JScrollPane(messageArea);
        this.add(JScrollPane,BorderLayout.CENTER);
        this.add(messageInput,BorderLayout.SOUTH);
        this.setVisible(true);

    }

    public void startReading()
    {
        Runnable r1 = () ->{
            System.out.println("Reader Started...");

           try {
            while(true){

                String msg=br.readLine();
                if(msg.equals("exit")){
                    System.out.println("Server terminated the chat");
                    JOptionPane.showMessageDialog(this,"Server terminated the chat");
                    messageInput.setEnabled(false);
                    socket.close();
                    break;
                }
                //System.out.println("Server :"+msg);
                messageArea.append("Server"+msg+"\n");
            }

            
           } catch (Exception e) {
            e.printStackTrace();
           }
        };
        new Thread(r1).start(); 

    }

    public void startWriting()
    {
        Runnable r2 = () ->{
            System.out.println("Writter Started");

            try {
                while( !socket.isClosed()){
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                    String content =br1.readLine();
                    out.println(content);
                    out.flush();

                    if(content.equals("exit"))
                    {
                        socket.close();
                        break;
                    }
                    
                }
                System.out.println("Connection is Closed");
            } catch (Exception e) {
                e.printStackTrace();
            }

        };
        new Thread(r2).start();

    }
    public static void main(String args[]){
        System.out.println("This is client....");
        new Client();
    }
}
