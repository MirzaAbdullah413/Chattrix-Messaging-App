public class DecriptionInuser {
    public static String decrypt(String encryptedMessage) {
    StringBuilder decrypted = new StringBuilder();

    for (char ch : encryptedMessage.toCharArray()) {
        char originalChar = (char) (ch - 3);  
        decrypted.append(originalChar);
    }

    return decrypted.toString();
}

    
}
