import javax.swing.*;
import java.io.*;
import java.net.*;

public class ConnectionSetupInClient {

    public static void connect(BasicGUI gui) {
        try {
            Socket socket = new Socket("172.30.17.61", 201);

            BufferedReader bufferedReaderforinput = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);

            ChatSenderFromClient sender = new ChatSenderFromClient();
            sender.setupSend(gui, printWriter);

            ChatReceiverFromClient receiver = new ChatReceiverFromClient(gui, bufferedReaderforinput);
            Thread thread = new Thread(receiver);
            thread.start();

            gui.chatBox.append(" Connected to server!\n\n");
           

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, " Could not connect to server.", "Connection Error", JOptionPane.ERROR_MESSAGE);
        }
     }
}
