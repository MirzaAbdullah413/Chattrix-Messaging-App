//import javax.swing.*;
import java.awt.event.*;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ChatSenderFromClient {
    public static boolean IsfirstMessage = true;
    public static String myusername;

    public void setupSend(BasicGUI gui, PrintWriter printWriter) {

        gui.sendButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sendMessage(gui, printWriter);
            }
        });

        gui.inputField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sendMessage(gui, printWriter);
            }
        });
    }

    public static void sendMessage(BasicGUI gui, PrintWriter printWriter) {
        String message = gui.inputField.getText().trim();

        if (message.isEmpty()) {
            return;
        }

        if (IsfirstMessage) {
            ChatSenderFromClient.myusername="#USERNAME#@" + message;
            System.out.println(myusername+" has been sent ");
            printWriter.println( ChatSenderFromClient.myusername);
            BasicGUI.chatBox.append("👤 Username sent to server: " + message + "\n");
            IsfirstMessage = false;
        } else {
            String selectedUser = null;
            if (!OnlineUsersAsClient.selectedUserRef.isEmpty()) {
                selectedUser = OnlineUsersAsClient.selectedUserRef.get(0);
            }

            if (selectedUser == null) {
                BasicGUI.chatBox.append("⚠️ No user selected. Please click on a username first.\n");
            } else {
                LocalDateTime now = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String timestamp = now.format(formatter);
                String encryptedMessage = EncriptionInUser.encrypt(message);
                // + "  -->  " + timestamp

                printWriter.println("TO#USERNAME#@" +  selectedUser + ": " + encryptedMessage);
                System.out.println("sent by user:TO#USERNAME#@" +  selectedUser + ": " + encryptedMessage);

                int userIndex = OnlineUsersAsClient.UserNames.indexOf(selectedUser);
                if (userIndex != -1) {
                    OnlineUsersAsClient.ChatHistories.get(userIndex).append("me: " + message + "  --> "+timestamp+"\n\n");
                }

                BasicGUI.chatBox.append("To : " + selectedUser + ": " + message + "  --> "+timestamp+ "\n");

                System.out.println("Sent to " + selectedUser + ": " + message);
                System.out.println("Encrypted Message: " + selectedUser + ": " + encryptedMessage);
            }
        }

        gui.inputField.setText("");
    }
}
