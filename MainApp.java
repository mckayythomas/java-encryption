import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class MainApp {

    public static void main(String[] args) {
        // Hardcoded file path for simplicity
        String filePath = "example.txt";

        try {
            // Create or load your secret key
            // For simplicity, we'll generate a new key here
            FileEncrypterDecrypter fileEncrypterDecrypter = new FileEncrypterDecrypter(KeyGeneratorUtils.generateAESKey(), "AES/CBC/PKCS5Padding");

            // Read content from file
            String originalContent = new String(Files.readAllBytes(Paths.get(filePath)));

            try (// Create a command-line menu
            Scanner scanner = new Scanner(System.in)) {
                int choice;
                do {
                    System.out.println("Choose an option:");
                    System.out.println("1. Encrypt");
                    System.out.println("2. Decrypt");
                    System.out.println("0. Exit");
                    System.out.print("Enter your choice: ");
                    
                    choice = scanner.nextInt();
                    
                    switch (choice) {
                        case 1:
                            // Encrypt and save to the same file
                            fileEncrypterDecrypter.encrypt(originalContent, filePath);
                            System.out.println("Content encrypted and saved to '" + filePath + "'");
                            break;
                        case 2:
                            // Decrypt and overwrite the same file
                            fileEncrypterDecrypter.decrypt(filePath);
                            System.out.println("Content decrypted and saved to '" + filePath + "'");
                            break;
                        case 0:
                            System.out.println("Exiting...");
                            break;
                        default:
                            System.out.println("Invalid choice. Please enter a valid option.");
                    }
                } while (choice != 0);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
