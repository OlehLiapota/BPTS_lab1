import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

public class Main {

    // Program flow:
    // 1. Enter message
    // 2. Enter key
    // 3. Reenter key (if weak or wrong length)
    // 4. Display keys
    // 5. Display encrypted message blocks
    // 6. Display decrypted message blocks
    // 7. Display origin message blocks
    // 8. Display decrypted message value
    public static void main(String[] args) throws Exception {
        boolean isProgramRunning = true;
        boolean isProgramComputing = false;
        boolean isProgramControlling = true;

        while (isProgramRunning) {
            Scanner consoleScanner= new Scanner(System.in);

            while (isProgramControlling) {
                System.out.print("Enter 'c' to compute/'q' to quit: ");
                String control = consoleScanner.nextLine();
                if (control.equals("c")) {
                    isProgramComputing = true;
                    isProgramControlling = false;
                }
                if (control.equals("q")) {
                    System.out.println("Program finished");
                    isProgramRunning = false;
                    isProgramControlling = false;
                }
            }

            while (isProgramComputing) {
                System.out.print("Enter message to encrypt: ");
                String message = consoleScanner.nextLine(); // message reading (console)
                int characterLength = getCharacterLength(message);

                System.out.print("Enter key (8 ASCII symbols): ");
                String key = consoleScanner.nextLine(); // key reading (console)
                KeyGeneration keyGeneration = new KeyGeneration();
                byte[][] subKeys = keyGeneration.keyToSubKeys(key);

                while (subKeys == null) {
                    key = consoleScanner.nextLine(); // key reading (console)
                    subKeys = keyGeneration.keyToSubKeys(key);
                }

                MessageConverter messageConverter = new MessageConverter(message, characterLength);
                ArrayList<byte[]> messageBlocks = messageConverter.getBlocks();

                System.out.println("\nKeys\n");
                displayKeys(subKeys);

                System.out.println("\nEncrypted message blocks\n");
                DES des = new DES();
                ArrayList<byte[]> encryptedMessageBlocks = new ArrayList<>();
                for (byte[] messageBlock : messageBlocks) {
                    encryptedMessageBlocks.add(des.run(messageBlock, subKeys));
                }
                displayMessageBlocks(encryptedMessageBlocks);

                System.out.println("\nDecrypted message blocks\n");
                Collections.reverse(Arrays.asList(subKeys));
                ArrayList<byte[]> decryptedMessageBlocks = new ArrayList<>();
                for (byte[] encryptedMessageBlock : encryptedMessageBlocks) {
                    decryptedMessageBlocks.add(des.run(encryptedMessageBlock, subKeys));
                }
                displayMessageBlocks(decryptedMessageBlocks);

                System.out.println("\nOrigin message blocks\n");
                displayMessageBlocks(messageBlocks);

                System.out.println("\nDecrypted message value\n");
                for (byte[] decryptedMessageBlock : decryptedMessageBlocks) {
                    for (int i = 0; i < decryptedMessageBlock.length / characterLength; i++) {
                        StringBuilder characterBits = new StringBuilder();
                        for (int j = 0; j < characterLength; j++) {
                            characterBits.append(decryptedMessageBlock[i * characterLength + j]);
                        }

                        int charCode = Integer.parseInt(characterBits.toString(), 2);
                        if (charCode != 0) {
                            System.out.print((char)charCode);
                        }
                    }
                }
                isProgramComputing = false;
                isProgramControlling = true;
            }
        }
    }

    private static int getCharacterLength(String message) {
        for (int i = 0; i < message.length(); i++) {
            if (message.charAt(i) > 127) {
                return 16;
            }
        }

        return 8;
    }


    private static void displayMessageBlocks(ArrayList<byte[]> messageBlocks) {
        for (byte[] messageBlock : messageBlocks) {
            System.out.println(Arrays.toString(messageBlock));
        }
    }

    private static void displayKeys(byte[][] keys) {
        for (byte[] key : keys) {
            System.out.println(Arrays.toString(key));
        }
    }
}
