import java.util.Arrays;

public class DES {
    public byte[] run(byte[] block, byte[][] keys) throws Exception {
        byte[] IPBlock = permutation(block, Tables.IPTable);
        byte[] feistelNetworkResult = runFeistelNetwork(block, keys);

        return permutation(feistelNetworkResult, Tables.inverseIPTable);
    }

    private byte[] runFeistelNetwork(byte[] block, byte[][] keys) throws Exception {
        byte[] result = new byte[32];

        for (byte[] key: keys) {
            byte[] leftPart = Arrays.copyOfRange(block, 0, 32);
            byte[] rightPart= Arrays.copyOfRange(block, 32, 64);
            //add extension for the right part
            //call XOR
            //add S-boxes
            //call permutation

            //add entropy calculation
        }

        return result;
    }

    private byte[] permutation(byte[] block, byte[] permutationTable) throws Exception {
        if (block.length != permutationTable.length) {
            throw new Exception("Different lengths");
        }

        byte[] result = new byte[permutationTable.length];

        for (int i = 0; i < permutationTable.length; i++) {
            result[i] = block[permutationTable[i]];
        }

        return result;
    }

    private byte[] xor(byte[] leftOperand, byte[] rightOperand) throws Exception {
        if (leftOperand.length != rightOperand.length) {
            throw new Exception("Different lengths");
        }

        byte[] result = new byte[leftOperand.length];

        for (int i = 0; i < leftOperand.length; i++) {
            result[i] = (leftOperand[i] == 1 && rightOperand[i] == 0 || leftOperand[i] == 0 && rightOperand[i] == 1) ? (byte)1 : (byte)0;
        }

        return result;
    }
}
