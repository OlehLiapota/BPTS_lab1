import java.util.Arrays;

public class DES {
    public byte[] run(byte[] block, byte[][] keys) throws Exception {
        byte[] IPBlock = permutation(block, Tables.IPTable);
        byte[] feistelNetworkResult = runFeistelNetwork(IPBlock, keys);

        return permutation(feistelNetworkResult, Tables.inverseIPTable);
    }

    private byte[] runFeistelNetwork(byte[] block, byte[][] keys) throws Exception {
        byte[] result = new byte[64];

        byte[] leftPart = Arrays.copyOfRange(block, 0, 32);
        byte[] rightPart = Arrays.copyOfRange(block, 32, 64);

        int i = 0;

        System.out.println("ENTROPY");
        for (byte[] key: keys) {
            i++;

            byte[] extendedRightPart = permutation(rightPart, Tables.extensionTable);
            byte[] extendedRightPartXOR = xor(extendedRightPart, key);
            byte[] encodedRightPart = runSBoxes(extendedRightPartXOR);
            byte[] resultedRightPart = permutation(encodedRightPart, Tables.PTable);

            byte[] t = leftPart;
            leftPart = rightPart;
            rightPart = xor(t, resultedRightPart);

            System.out.println(i + "=>" + countEntropy(leftPart, rightPart));
            // entropy calculation and display its value (format: <cycle number> => <entropy value>)
        }
        System.out.println("BLOCK");

        System.arraycopy(rightPart, 0, result, 0, rightPart.length);
        System.arraycopy(leftPart, 0, result, rightPart.length, leftPart.length);

        return result;
    }

    private double countEntropy(byte[] leftArray, byte[] rightArray) {
        double numbersOf1 = 0;
        double numbersOf0 = 0;
        double fullLength = leftArray.length + rightArray.length;

        for (byte b : leftArray) {
            if (b == 1) {
                numbersOf1++;
            } else {
                numbersOf0++;
            }
        }

        for (byte b : rightArray) {
            if (b == 1) {
                numbersOf1++;
            } else {
                numbersOf0++;
            }
        }

        double entropyFunction1 = numbersOf1 / fullLength;
        double entropyFunction0 = numbersOf0 / fullLength;

        double entropyApplication1 = entropyFunction1 * (Math.log(entropyFunction1) / Math.log(2));
        double entropyApplication0 = entropyFunction0 * (Math.log(entropyFunction0) / Math.log(2));

        return -(entropyApplication1 + entropyApplication0);
    }

    private byte[] permutation(byte[] block, byte[] permutationTable) {
        byte[] result = new byte[permutationTable.length];

        for (int i = 0; i < permutationTable.length; i++) {
            result[i] = block[(permutationTable[i] - 1)];
        }

        return result;
    }

    private byte[] runSBoxes(byte[] blockPart) {
        byte[] result = new byte[32];

        for (int i = 0; i < Tables.sTables.length; i++) {
            String jBinaryString = String.valueOf(blockPart[i * 6]) + blockPart[i * 6 + 5];
            int j = Integer.parseInt(jBinaryString, 2);

            String kBinaryString = String.valueOf(blockPart[i * 6 + 1]) + blockPart[i * 6 + 2] + blockPart[i * 6 + 3] + blockPart[i * 6 + 4];
            int k = Integer.parseInt(kBinaryString, 2);

            byte value = Tables.sTables[i][j][k];

            for (int resultIndex = i * 4 + 3; resultIndex >= i * 4; resultIndex--) {
                result[resultIndex] = (byte)(value & 1);
                value >>= 1;
            }
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
