
import java.net.*;

public class Main {
    public static void main(String[] args) throws Exception {
        ServerSocket server = new ServerSocket(201);
        System.out.println("[DEBUG] Server socket created. Port bound: " + server.getLocalPort());

        UserManagerInServer usermanagerinserver = new UserManagerInServer();
        System.out.println("[DEBUG] UserManagerInServer instance created.");

        System.out.println("Server started on port " + server.getLocalPort() + ". Waiting for clients...");

        while (true) {
            Socket clientSocket = server.accept();
            System.out.println("[DEBUG] New client accepted: " + clientSocket);

            multithreading handler = new multithreading(clientSocket, usermanagerinserver);
            System.out.println("[DEBUG] Thread handler created for client: " + clientSocket);

            new Thread(handler).start();
            System.out.println("[DEBUG] Handler thread started.");
        }
    }
}
