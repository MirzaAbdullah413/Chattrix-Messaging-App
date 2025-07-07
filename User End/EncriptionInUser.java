public class EncriptionInUser {
    
     public static String encrypt(String message) {
        StringBuilder encrypted = new StringBuilder();

        for (char ch : message.toCharArray()) {
            char shiftedChar = (char) (ch + 3); // Add 3 to ASCII
            encrypted.append(shiftedChar);
        }

        return encrypted.toString();
    }
}
