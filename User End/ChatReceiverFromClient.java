import java.io.BufferedReader;
import javax.swing.SwingUtilities;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ChatReceiverFromClient implements Runnable {
    private BufferedReader bufferreaderforinput;
    private BasicGUI gui;
    public static boolean IsfirstMessage=true;

    public ChatReceiverFromClient(BasicGUI gui, BufferedReader bufferedReader) {
        this.bufferreaderforinput = bufferedReader;
        this.gui = gui;
    }
    String message;

    @Override
    public void run() {
        try {
            String  incomingmessage;

            while ((incomingmessage = bufferreaderforinput.readLine()) != null) {
                message=incomingmessage;
                System.out.println("[Receiver] Raw message received: " + message);

                if (message.trim().isEmpty()) {
                    continue;
                }
                else
                {

                if (message.startsWith("#USERNAME#@")  ) {
                    String username = message.substring(11).trim();
                    System.out.println("[Receiver] New user joined: " + username);
                    SwingUtilities.invokeLater(() -> BasicGUI.addUserToPanel(username));
                    continue;
                }
                else
                {
                if(IsfirstMessage)
                {
                    BasicGUI.chatBox.append(message);
                    IsfirstMessage=false;
                }
                else
                {

                if (message.startsWith("TO#USERNAME#@")) {
                    //int atIndex = message.indexOf("@");
                    int colonIndex = message.indexOf(":");

                    if ( colonIndex != -1  ) {
                        String senderuser = message.substring(13, colonIndex).trim();
                        String content = message.substring(colonIndex + 1).trim();
                        String decryptedMessage = DecriptionInuser.decrypt(content);
                        String formattedMessage = senderuser + " : " + decryptedMessage;

                        LocalDateTime now = LocalDateTime.now();
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                        String timeStamp = now.format(formatter);
                        SwingUtilities.invokeLater(() -> {
                            int indexofuser = OnlineUsersAsClient.UserNames.indexOf(senderuser);
                            if (indexofuser != -1  ) {
                                // Append to chat history
                                OnlineUsersAsClient.ChatHistories.get(indexofuser).append(formattedMessage).append("  --> "+timeStamp).append("\n\n");
                                System.out.println("message has been saved in chathistories "+message);
                                BasicGUI.chatBox.append(formattedMessage+ " --> " + timeStamp + "\n\n");

                                if (OnlineUsersAsClient.selectedUserRef.isEmpty()==false) {
                                    BasicGUI.chatBox.setText("Now you are chatting with " + senderuser + " ::\n\n" +
                                            OnlineUsersAsClient.ChatHistories.get(indexofuser).toString());
                                }

                                System.out.println("[Receiver] Message from " + senderuser + " displayed in GUI.");
                            } else {
                                System.out.println("[Receiver] ⚠ User not found in chat list: " + senderuser);
                            }
                        });
                    } else {
                        System.out.println("[Receiver] ⚠ Malformed incoming message.");
                    }
                } else {
                    System.out.println("[Receiver] ⚠ Unknown format: " + message);
                }
            }
        }
        }
            }
        } catch (Exception e) {
            SwingUtilities.invokeLater(() -> gui.chatBox.append("❌ Connection lost.\n"));
            System.out.println("[Receiver] ❌ Error: " + e.getMessage());
        }
    }
}
