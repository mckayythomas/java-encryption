import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import java.io.*;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class FileEncrypterDecrypter {
    private SecretKey secretKey;
    private Cipher cipher;

    public FileEncrypterDecrypter(SecretKey secretKey, String transformation) throws NoSuchAlgorithmException, NoSuchPaddingException {
        this.secretKey = secretKey;
        this.cipher = Cipher.getInstance(transformation);
    }

    public void encrypt(String content, String fileName) throws IOException, InvalidKeyException {
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        // Generate and store IV
        byte[] iv = cipher.getIV();

        try (FileOutputStream fileOut = new FileOutputStream(fileName);
             CipherOutputStream cipherOut = new CipherOutputStream(fileOut, cipher)) {
            fileOut.write(iv);
            cipherOut.write(content.getBytes());
        }
    }

    public String decrypt(String fileName) throws IOException, InvalidKeyException, InvalidAlgorithmParameterException {
        String content;
    
        try (FileInputStream fileIn = new FileInputStream(fileName)) {
            // Read IV from the file
            byte[] fileIv = new byte[16];
            fileIn.read(fileIv);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(fileIv));
    
            try (CipherInputStream cipherIn = new CipherInputStream(fileIn, cipher);
                 Scanner scanner = new Scanner(cipherIn)) {
    
                StringBuilder sb = new StringBuilder();
                while (scanner.hasNextLine()) {
                    sb.append(scanner.nextLine());
                    if (scanner.hasNextLine()) {
                        sb.append(System.lineSeparator());  // Add newline character after each line except the last one
                    }
                }
                content = sb.toString();
            }
        }
    
        // Write the decrypted content back to the file
        try (FileOutputStream fileOut = new FileOutputStream(fileName)) {
            fileOut.write(content.getBytes());
        }
    
        return content;
    }    
}
