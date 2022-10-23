public class Main {

    public static void main(String[] args) {
        DES des = new DES();
        byte[][] subKeys = des.keyToSubKeys("IEOFIT#1");
    }
}
