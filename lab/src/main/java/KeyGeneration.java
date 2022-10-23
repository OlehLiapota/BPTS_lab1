public class KeyGeneration {
    private final char normalisationSymbol = '0';
    private final byte[] leftKeyPartOrder = {57,49,41,33,25,17,9,1,58,50,42,34,26,18,10,2,59,51,43,35,27,19,11,3,60,52,44,36};
    private final byte[] rightKeyPartOrder = {63,55,47,39,31,23,15,7,62,54,46,38,30,22,14,6,61,53,45,37,29,21,13,5,28,20,12,4};
    private final byte[] leftShiftNumber = {1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1};
    private final byte[] subKeyOrder = {14,17,11,24,1,5,3,28,15,6,21,10,23,19,12,4,26,8,16,7,27,
            20,13,2,41,52,31,37,47,55,30,40,51,45,33,48,44,49,39,56,34,53,46,42,50,36,29,32};
    private final String[] weakKeys = {"0101010101010101", "1f1f1f1f0e0e0e0e", "e0e0e0e0f1f1f1f1", "fefefefefefefefe",
            "01fe01fe01fe01fe", "fe01fe01fe01fe01", "1fe01fe00ef10ef1", "e01fe01ff10ef10e", "01e001e001f101f1",
            "e001e001f101f101", "1ffe1ffe0efe0efe", "fe1ffe1ffe0efe0e", "011f011f010e010e", "1f011f010e010e01",
            "e0fee0fef1fef1fe", "fee0fee0fee1fee1"};

    public byte[][] keyToSubKeys(String key) {
        byte[][] subKeys;
        if (key.length() != 8) {
            System.out.println("Ключ має складатись з 8 символів");
            subKeys = null;
        } else {
            if (isKeyContainOnlyASCII(key)) {
                String hexKey = keyToHex(key);
                if (isKeyWeak(hexKey)) {
                    System.out.println("Слабкий ключ");
                    subKeys = null;
                } else {
                    String binaryKey = hexToBinary(hexKey);
                    byte[][] rearrangedKey = rearrangeKey(binaryKey);
                    subKeys = generateSubKeys(rearrangedKey);
                }
            }
            else {
                System.out.println("Можна використовувати тільки символи базової таблиці ASCII");
                subKeys = null;
            }
        }
        return subKeys;
    }

    private boolean isKeyContainOnlyASCII(String planeKey) {
        boolean containOnlyASCII = true;
        for (int i = 0; i < planeKey.length(); i++) {
            byte maxASCIIValue = 127;
            if ((int) planeKey.charAt(i) > maxASCIIValue) {
                containOnlyASCII = false;
                i = planeKey.length();
            }
        }
        return containOnlyASCII;
    }

    private boolean isKeyWeak(String hexKey) {
        boolean isWeak = false;
        for (int i = 0; i < weakKeys.length; i++) {
            if (hexKey.equals(weakKeys[i])) {
                isWeak = true;
                i = weakKeys.length;
            }
        }
        return isWeak;
    }

    private byte[][] generateSubKeys(byte[][] rearrangedKey) {
        byte[][] subKeys = new byte[leftShiftNumber.length][subKeyOrder.length];
        for (int i = 0; i < leftShiftNumber.length; i++) {
            rearrangedKey[0] = leftShift(rearrangedKey[0], leftShiftNumber[i]);
            rearrangedKey[1] = leftShift(rearrangedKey[1], leftShiftNumber[i]);
            subKeys[i] = rearrangeSubKey(rearrangedKey[0], rearrangedKey[1]);
        }
        return subKeys;
    }

    private byte[] rearrangeSubKey(byte[] leftPartKey, byte[] rightPartKey) {
        byte[] subKey = new byte[subKeyOrder.length];
        for (int i = 0; i < subKeyOrder.length; i++) {
            if (subKeyOrder[i] > leftPartKey.length) {
                subKey[i] = rightPartKey[subKeyOrder[i] - leftPartKey.length - 1];
            } else {
                subKey[i] = leftPartKey[subKeyOrder[i] - 1];
            }
        }
        return subKey;
    }

    private byte[] leftShift(byte[] planePartKey, int numberOfShifts){
        byte[] resultPartKey = new byte[planePartKey.length];
        System.arraycopy(planePartKey, 1, resultPartKey, 0, planePartKey.length - 1);
        resultPartKey[resultPartKey.length - 1] = planePartKey[0];
        if (numberOfShifts > 1) {
            return leftShift(resultPartKey, numberOfShifts - 1);
        } else {
            return resultPartKey;
        }
    }

    private byte[][] rearrangeKey(String binaryKey) {
        byte[][] rearrangedKey = {new byte[leftKeyPartOrder.length], new byte[rightKeyPartOrder.length]};
        for (int i = 0; i < leftKeyPartOrder.length; i++) {
            rearrangedKey[0][i] = Byte.parseByte(binaryKey.substring(leftKeyPartOrder[i] - 1,leftKeyPartOrder[i]));
            rearrangedKey[1][i] = Byte.parseByte(binaryKey.substring(rightKeyPartOrder[i] - 1,rightKeyPartOrder[i]));
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
