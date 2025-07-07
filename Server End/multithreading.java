
import java.io.*;
import java.net.*;

public class multithreading implements Runnable {
    private Socket socket;
    private UserManagerInServer usermanagerinserver;

    public multithreading(Socket socket, UserManagerInServer usermanagerinserver) {
        this.socket = socket;
        this.usermanagerinserver = usermanagerinserver;
    }

    @Override
    public void run() {
        try (
            BufferedReader bufferreaderforinput = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter printwriterforoutput = new PrintWriter(socket.getOutputStream(), true)
        ) {
            // First, read the username
            printwriterforoutput.println("Enter your username:");
            System.out.println("[DEBUG] Prompted client for username.");

            String username = bufferreaderforinput.readLine();
            System.out.println("[DEBUG] Username received from client: " + username);

            if ( username.startsWith("#USERNAME#@")) {
                System.out.println("[DEBUG] Valid username format. Adding client.");

                // Add client to server and notify others
                usermanagerinserver.addClient(socket, username);
                System.out.println(username + " connected with socket: " + socket);

                // Notify all other users

                // Send the list of users to the new client
                usermanagerinserver.sendUserList(socket);
                System.out.println("[DEBUG] Sent current user list to: " + username);
            }

            String message;
            while ((message = bufferreaderforinput.readLine()) != null) {
                if (message.trim().isEmpty()) {
                    System.out.println("[DEBUG] Empty message received. Skipping.");
                    continue; // Ignore empty messages
                }

                System.out.println(username + ": " + message);

                if (message.startsWith("#USERNAME#@")) {
                    System.out.println("[DEBUG] Received user update message: " + message);

                    // int colonindexfordestination=message.indexOf(":");
                    // String tofinddestinationuser=message.substring(3, colonindexfordestination).trim();
                    // System.out.println("destisntion user : "+tofinddestinationuser);
                    usermanagerinserver.addClient(socket, username);
                    // usermanagerinserver.ToInformAllOtherUser(socket, message ); // Forward message to other users
                } else {
                    int colonindexfordestination = message.indexOf(":");
                    String recepientusername = message.substring(2, colonindexfordestination).trim();
                    System.out.println("[DEBUG] Forwarding message to: " + recepientusername);

                    usermanagerinserver.SendMessageToSpecificUser(socket, recepientusername, message); 
                }
            }

        } catch (IOException e) {
            // Handle error when client disconnects or any other IO error
            try {
                String username = usermanagerinserver.getUsername(socket);
                System.out.println("Client disconnected: " + username + " " + socket);
                System.out.println("[DEBUG] IOException occurred: " + e.getMessage());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } finally {
            // Ensure client is removed and the socket is closed properly
            try {
                String username = usermanagerinserver.getUsername(socket);
                usermanagerinserver.removeClient(socket);
                usermanagerinserver.ToInformAllOtherUser(socket, username + " has left");
                socket.close();
                System.out.println("[DEBUG] Cleaned up resources for: " + username);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
