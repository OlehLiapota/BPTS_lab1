public class DES {
    private final char normalisationSymbol = '0';
    private final int[] leftKeyPartOrder = {57,49,41,33,25,17,9,1,58,50,42,34,26,18,10,2,59,51,43,35,27,19,11,3,60,52,44,36};
    private final int[] rightKeyPartOrder = {63,55,47,39,31,23,15,7,62,54,46,38,30,22,14,6,61,53,45,37,29,21,13,5,28,20,12,4};
    private final int[] leftShiftNumber = {1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1};
    private final int[] subKeyOrder = {14,17,11,24,1,5,3,28,15,6,21,10,23,19,12,4,26,8,16,7,27,
            20,13,2,41,52,31,37,47,55,30,40,51,45,33,48,44,49,39,56,34,53,46,42,50,36,29,32};

    public int[][] keyToSubKeys(String key) {
        String hexKey = keyToHex(key);
        String binaryKey = hexToBinary(hexKey);
        int[][] rearrangedKey = rearrangeKey(binaryKey);
        return generateSubKeys(rearrangedKey);
    }

    private int[][] generateSubKeys(int[][] rearrangedKey) {
        int[][] subKeys = new int[leftShiftNumber.length][subKeyOrder.length];
        for (int i = 0; i < leftShiftNumber.length; i++) {
            rearrangedKey[0] = leftShift(rearrangedKey[0], leftShiftNumber[i]);
            rearrangedKey[1] = leftShift(rearrangedKey[1], leftShiftNumber[i]);
            subKeys[i] = rearrangeSubKey(rearrangedKey[0], rearrangedKey[1]);
        }
        return subKeys;
    }

    private int[] rearrangeSubKey(int[] leftPartKey, int[] rightPartKey) {
        int[] subKey = new int[subKeyOrder.length];
        for (int i = 0; i < subKeyOrder.length; i++) {
            if (subKeyOrder[i] > leftPartKey.length) {
                subKey[i] = rightPartKey[subKeyOrder[i] - leftPartKey.length - 1];
            } else {
                subKey[i] = leftPartKey[subKeyOrder[i] - 1];
            }
        }
        return subKey;
    }

    private int[] leftShift(int[] planePartKey, int numberOfShifts){
        int[] resultPartKey = new int[planePartKey.length];
        System.arraycopy(planePartKey, 1, resultPartKey, 0, planePartKey.length - 1);
        resultPartKey[resultPartKey.length - 1] = planePartKey[0];
        if (numberOfShifts > 1) {
            return leftShift(resultPartKey, numberOfShifts - 1);
        } else {
            return resultPartKey;
        }
    }

    private int[][] rearrangeKey(String binaryKey) {
        int[][] rearrangedKey = {new int[leftKeyPartOrder.length], new int[rightKeyPartOrder.length]};
        for (int i = 0; i < leftKeyPartOrder.length; i++) {
            rearrangedKey[0][i] = Integer.parseInt(binaryKey.substring(leftKeyPartOrder[i] - 1,leftKeyPartOrder[i]));
            rearrangedKey[1][i] = Integer.parseInt(binaryKey.substring(rightKeyPartOrder[i] - 1,rightKeyPartOrder[i]));
        }
        return rearrangedKey;
    }

    private String keyToHex(String planeKey) {
        StringBuilder hexKey = new StringBuilder();
        for (int i = 0; i < planeKey.length(); i++) {
            hexKey.append(normaliseHex(Integer.toHexString(planeKey.charAt(i))));
        }
        return hexKey.toString();
    }

    private String normaliseHex(String defaultHex) {
        int normalisationHexCapacity = 2;
        StringBuilder normalisedHex = new StringBuilder(normalisationHexCapacity);
        if (defaultHex.length() < normalisationHexCapacity) {
            normalisedHex.append(normalisationSymbol).append(defaultHex);
        } else {
            normalisedHex.append(defaultHex);
        }
        return normalisedHex.toString();
    }

    private String hexToBinary(String hexKey) {
        StringBuilder binaryKey = new StringBuilder();
        for (int i = 0; i < hexKey.length(); i++) {
            int hexNumberFormat = 16;
            binaryKey.append(normaliseBinary(Integer.toBinaryString(Integer.parseInt(hexKey.substring(i, i + 1),
                    hexNumberFormat))));
        }
        return binaryKey.toString();
    }

    private String normaliseBinary(String defaultBinary) {
        int normalisationBinCapacity = 4;
        StringBuilder normalisedBinary = new StringBuilder(normalisationBinCapacity);
        if (defaultBinary.length() < normalisationBinCapacity) {
            int currentNormalDifference = normalisationBinCapacity - defaultBinary.length();
            while (currentNormalDifference > 0) {
                normalisedBinary.append(normalisationSymbol);
                currentNormalDifference--;
            }
        }
        normalisedBinary.append(defaultBinary);
        return normalisedBinary.toString();
    }
}
