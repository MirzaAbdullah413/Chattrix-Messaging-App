import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;

public class BasicGUI {
    public JFrame frame;
    public static JTextArea chatBox;        // Your chat area where messages will appear
    public JTextField inputField;    // TextField to input message
    public JButton sendButton;       // Button to send message
    public ArrayList<String> selectedUser = new ArrayList<>();
    public OnlineUsersAsClient userListPanel;

    public void buildWindow() {
        frame = new JFrame("Chattrix");
        frame.setSize(700, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ImageIcon icon = new ImageIcon("logo.png");
        frame.setIconImage((icon.getImage()));

        frame.setLocationRelativeTo(null);
        JPanel backgroundPanel = new JPanel(new BorderLayout());

        frame.setContentPane(backgroundPanel);

         chatBox = new JTextArea();
        chatBox.setEditable(false);
        chatBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        chatBox.setBackground(new Color(245, 245, 220));

        JScrollPane scrollPanel = new JScrollPane(chatBox);
        scrollPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        backgroundPanel.add(scrollPanel, BorderLayout.CENTER);

        inputField = new JTextField();
        sendButton = new JButton("Send");
        sendButton.setBackground(new Color(50, 205, 50));

        // Input panel with BorderLayout to put text field and button
        JPanel inputPanel = new JPanel(new BorderLayout(5, 5));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        inputPanel.setBackground(new Color(230, 247, 255));

        inputField = new JTextField("Type the message here");
        inputField.setForeground(new Color(0, 0, 0)); // Light gray placeholder color
        inputField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (inputField.getText().equals("Type the message here")) {
                    inputField.setText("");
                    inputField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (inputField.getText().isEmpty()) {
                    inputField.setText("Type the message here");
                    inputField.setForeground(new Color(0, 0, 0));
                }
            }
        });



        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        backgroundPanel.add(inputPanel, BorderLayout.SOUTH);

        userListPanel = new OnlineUsersAsClient(chatBox, selectedUser);
        userListPanel.setPreferredSize(new Dimension(200, 0));  // Fixed width for user list

        userListPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        backgroundPanel.add(userListPanel, BorderLayout.WEST);

        frame.setVisible(true);

        System.out.println("GUI initialized and visible");
    }

    public static void addUserToPanel(String username) {
        System.out.println("addnewuser is calle for "+ username);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                OnlineUsersAsClient.addNewUser(username);
                System.out.println("Added user via GUI method: " + username);
            }
        });
    }
}
    
