import java.net.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;

class Server extends JFrame { // Extending JFrame for GUI functionality
    ServerSocket server;
    Socket socket;

    BufferedReader br; // For input
    PrintWriter out;   // For output

    // GUI components
    private JLabel heading = new JLabel("Server Messenger");
    private JTextArea messageArea = new JTextArea();
    private JTextField messageInput = new JTextField();
    private Font font = new Font("Roboto", Font.PLAIN, 20);

    // Constructor
    public Server() {
        try {
            server = new ServerSocket(7777);
            System.out.println("Server is ready to accept connection...");
            System.out.println("Waiting...");

            socket = server.accept(); // Accept the connection from client
            System.out.println("Connection established with client.");

            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());

            createGUI();
            startReading();
            startWriting();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createGUI() {
        // GUI creation code
        this.setTitle("Server Messenger [END]");
        this.setSize(600, 700);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Component styling
        heading.setFont(font);
        messageArea.setFont(font);
        messageInput.setFont(font);
        heading.setIcon(new ImageIcon("clogo.png")); // Ensure the file exists
        heading.setHorizontalTextPosition(SwingConstants.CENTER);
        heading.setVerticalTextPosition(SwingConstants.BOTTOM);
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Frame layout setup
        this.setLayout(new BorderLayout());

        // Adding components to frame
        this.add(heading, BorderLayout.NORTH);
        JScrollPane jScrollPane = new JScrollPane(messageArea);
        this.add(jScrollPane, BorderLayout.CENTER);
        this.add(messageInput, BorderLayout.SOUTH);

        this.setVisible(true);
    }

    // Reading data from the client
    public void startReading() {
        Runnable r1 = () -> {
            System.out.println("Reader started...");
            try {
                while (true) {
                    String msg = br.readLine();
                    if (msg.equals("exit")) {
                        System.out.println("Client terminated the chat.");
                        socket.close();
                        break;
                    }
                    System.out.println("Client: " + msg);
                }
            } catch (Exception e) {
                System.out.println("Connection closed.");
            }
        };

        new Thread(r1).start();
    }

    // Writing data to the client
    public void startWriting() {
        Runnable r2 = () -> {
            System.out.println("Writer started...");
            try {
                while (!socket.isClosed()) {
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                    String content = br1.readLine();

                    out.println(content);
                    out.flush();

                    if (content.equals("exit")) {
                        socket.close();
                        break;
                    }
                }
            } catch (Exception e) {
                System.out.println("Connection closed.");
            }
        };

        new Thread(r2).start();
    }

    public static void main(String[] args) {
        System.out.println("This is the server...");
        new Server();
    }
}
