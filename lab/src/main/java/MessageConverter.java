import java.util.ArrayList;

public class MessageConverter {
    private final int characterLength = 16;
    private final String message;

    public MessageConverter(String message) {
        this.message = message;
    }

    public ArrayList<byte[]> getBlocks() {
        ArrayList<byte[]> messageBitBlocks = new ArrayList<byte[]>();

        byte charactersInBlock = 0;
        byte[] blockBits = new byte[64];
        for (int i = 0; i < message.length(); i++) {
            charactersInBlock++;

            byte[] characterBits = getSymbolBits(message.charAt(i));
            for (int j = 0; j < characterBits.length; j++) {
                int blockIndex = (charactersInBlock - 1) * characterLength + j;
                blockBits[blockIndex] = characterBits[j];
            }

            if (charactersInBlock == 4) {
                messageBitBlocks.add(blockBits);
                charactersInBlock = 0;
                blockBits = new byte[64];
            }
        }

        if (charactersInBlock > 0) {
            for (int i = charactersInBlock * characterLength; i < blockBits.length; i++) {
                blockBits[i] = 0;
            }
            messageBitBlocks.add(blockBits);
        }

        return messageBitBlocks;
    }

    private byte[] getSymbolBits(char character) {
        byte[] bits = new byte[16];

        StringBuilder stringBuilder = new StringBuilder(Integer.toBinaryString(character));
        while (stringBuilder.length() < characterLength) {
            stringBuilder.insert(0, '0');
        }

        String normalizedBinaryString = stringBuilder.toString();
        for (int i = 0; i < normalizedBinaryString.length(); i++) {
            bits[i] = Byte.parseByte("" + normalizedBinaryString.charAt(i));
        }

        return bits;
    }
}
