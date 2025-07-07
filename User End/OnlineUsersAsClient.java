import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class OnlineUsersAsClient extends JPanel {
    public static ArrayList<String> UserNames = new ArrayList<>();
    public static ArrayList<JButton> UserButtons = new ArrayList<>();
    public static ArrayList<StringBuilder> ChatHistories = new ArrayList<>();
    public static JTextArea chatBoxRef;
    public static ArrayList<String> selectedUserRef = new ArrayList<>();

    public JPanel panel;

    public OnlineUsersAsClient(JTextArea chatBox, ArrayList<String> selectedUser) {
        chatBoxRef = chatBox;
        selectedUserRef = selectedUser;
        setupPanel();
        UserListHolder.panel = this;

        System.out.println("OnlineUsersAsClient initialized");
    }

    private void setupPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(230, 247, 255));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setPreferredSize(new Dimension(200, 0));

        JLabel titleLabel = new JLabel("Online Users");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(new Color(50, 50, 50));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 15, 0));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(titleLabel);

        JSeparator separator = new JSeparator();
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        separator.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(separator);

        add(Box.createRigidArea(new Dimension(0, 10)));
    }

    public static void addNewUser(String username) {
        System.out.println("Creating button for user: " + username);

        UserNames.add(username);
        ChatHistories.add(new StringBuilder(""));

        JButton userButton = new JButton(username);
        styleButton(userButton);
        userButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        UserButtons.add(userButton);

        userButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedUser = userButton.getText();

                selectedUserRef.clear();
                selectedUserRef.add(selectedUser);

                int index = UserNames.indexOf(selectedUser);
                if (index != -1 && index < ChatHistories.size()) {
                    String chat = ChatHistories.get(index).toString();
                    chatBoxRef.setText("Now You are chatting with " + selectedUser + " :\n\n" + chat);
                }

                for (JButton b : UserButtons) {
                    b.setOpaque(true);
                    b.setBorderPainted(false);
                    b.setFocusPainted(false);
                    b.setBackground(new Color(50,205,50));
                }
                userButton.setOpaque(true);
                userButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, userButton.getPreferredSize().height));
                userButton.setAlignmentX(Component.LEFT_ALIGNMENT);
                userButton.setBackground(new Color(144, 238, 144));

            }
        });

        SwingUtilities.invokeLater(() -> {
            if (UserListHolder.panel != null) {
                UserListHolder.panel.add(userButton);
                UserListHolder.panel.add(Box.createRigidArea(new Dimension(0, 5)));
                UserListHolder.panel.revalidate();
                UserListHolder.panel.repaint();
                System.out.println("Button added to panel for: " + username);
            } else {
                System.out.println("ERROR: Panel reference is null when trying to add " + username);
            }
        });
    }

    public static void styleButton(JButton button) {
        button.setFont(button.getFont().deriveFont(Font.BOLD));
        button.setOpaque(true);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, button.getPreferredSize().height));
        button.setAlignmentX(Component.LEFT_ALIGNMENT);

        button.setBackground(new Color(50, 205, 50));
    }

    public static class UserListHolder {
        public static JPanel panel;
    }
}
