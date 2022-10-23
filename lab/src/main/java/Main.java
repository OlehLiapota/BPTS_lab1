import java.util.ArrayList;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        KeyGeneration keyGeneration = new KeyGeneration();
        MessageConverter messageConverter = new MessageConverter("42424242");

        ArrayList<byte[]> messageBlocks = messageConverter.getBlocks();
        displayMessageBlocks(messageBlocks);

        byte[][] subKeys = keyGeneration.keyToSubKeys("IEOFIT#1");
    }

    public static void displayMessageBlocks(ArrayList<byte[]> messageBlocks) {
        for (byte[] messageBlock : messageBlocks) {
            System.out.println(Arrays.toString(messageBlock));
        }
    }
}
