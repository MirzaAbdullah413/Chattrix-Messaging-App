public class MainForClient {
    public static void main(String[] args) {
        BasicGUI gui = new BasicGUI();
        gui.buildWindow();  // Create GUI

       ConnectionSetupInClient.connect(gui);  // Setup connection
    }
}
