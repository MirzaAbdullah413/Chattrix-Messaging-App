
import java.net.*;
import java.util.*;
import java.io.*;

public class UserManagerInServer {
    public static List<Socket> UserSockets = Collections.synchronizedList(new ArrayList<>());
    public static List<String> UserNames = Collections.synchronizedList(new ArrayList<>());

    public static void addClient(Socket usersocket, String username) {
        UserSockets.add(usersocket);
        UserNames.add(username);
        System.out.println("[DEBUG] Client added: " + username + " | Current users: " + UserNames);
        UserManagerInServer.ToInformAllOtherUser(usersocket, username);
    }

    public void removeClient(Socket usersocket) {
        int index = UserSockets.indexOf(usersocket);
        if (index != -1) {
            System.out.println("[DEBUG] Removing client: " + UserNames.get(index));
            UserSockets.remove(index);
            UserNames.remove(index);
        } else {
            System.out.println("[DEBUG] Client socket not found in list.");
        }
    }

    public static void ToInformAllOtherUser(Socket newsocketsenderuser, String message) {
        System.out.println("[DEBUG] Broadcasting message to all users except sender: " + message);
        synchronized (UserSockets) {
            for (Socket CurrentUser : UserSockets) {
                if (!CurrentUser.isClosed()) {
                    try {
                        PrintWriter printwriterforoutput = new PrintWriter(CurrentUser.getOutputStream(), true);
                        if (CurrentUser != newsocketsenderuser) {
                            printwriterforoutput.println(message);
                            System.out.println("[DEBUG] Sent message to: " + CurrentUser);
                        }
                    } catch (IOException e) {
                        System.out.println("[DEBUG] Failed to send message to: " + CurrentUser);
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void SendMessageToSpecificUser(Socket senderSocket, String recipientUsername, String message) {
        System.out.println("[DEBUG] Attempting to send message to " + recipientUsername + " from socket: " + senderSocket);
        int senderIndex = UserSockets.indexOf(senderSocket);
        String senderUsername = UserNames.get(senderIndex);
        int indexofrecepient = UserNames.indexOf(recipientUsername);
        int colonindexforremovalofusername=message.indexOf(":");
        String processedmessage=message.substring(colonindexforremovalofusername).trim(); 
        if (indexofrecepient == -1) {
            System.out.println("[DEBUG] Recipient " + recipientUsername + " not found in list.");
            return;
        }

        Socket recipientSocket = UserSockets.get(indexofrecepient);

        try {
            if (recipientSocket != null && !recipientSocket.isClosed()) {
                PrintWriter printwriterforoutput = new PrintWriter(recipientSocket.getOutputStream(), true);
                printwriterforoutput.println("TO"+senderUsername + processedmessage);
                System.out.println("this the sent message:  :TO"+senderUsername + processedmessage);
                System.out.println("[DEBUG] Message from " + senderUsername + " sent to " + recipientUsername);
            } else {
                System.out.println("[DEBUG] Recipient socket is null or closed.");
            }
        } catch (IOException e) {
            System.out.println("[DEBUG] IOException while sending message to " + recipientUsername);
            e.printStackTrace();
        }
    }

    public void sendUserList(Socket clientSocket) {
        System.out.println("[DEBUG] Sending user list to new client: " + clientSocket);
        try {
            PrintWriter printwriterforoutput = new PrintWriter(clientSocket.getOutputStream(), true);
            printwriterforoutput.println("Connected users:");
            synchronized (UserNames) {
                for (String username : UserNames) {
                    printwriterforoutput.println(username);
                    System.out.println("[DEBUG] Sent username to client: " + username);
                }
            }
        } catch (Exception e) {
            System.out.println("[DEBUG] Failed to send user list to client.");
            e.printStackTrace();
        }
    }

    public String getUsername(Socket clientSocket) {
        int index = UserSockets.indexOf(clientSocket);
        if (index != -1) {
            String foundUsername = UserNames.get(index);
            System.out.println("[DEBUG] Found username for socket: " + foundUsername);
            return foundUsername;
        }
        System.out.println("[DEBUG] Username not found for socket: " + clientSocket);
        return "Unknown";
    }
}
