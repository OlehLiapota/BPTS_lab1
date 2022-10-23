public class Main {

    public static void main(String[] args) {
        KeyGeneration keyGeneration = new KeyGeneration();
        byte[][] subKeys = keyGeneration.keyToSubKeys("IEOFIT#");
    }
}
