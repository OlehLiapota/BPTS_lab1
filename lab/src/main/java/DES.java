public class DES {
    public byte[] xor(byte[] leftOperand, byte[] rightOperand) throws Exception {
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
