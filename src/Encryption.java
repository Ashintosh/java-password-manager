import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;

public class Encryption {
    private static final String METHOD = "AES/CBC/PKCS5Padding";
    private static final int IV_LENGTH_BYTE = 16;
    private static final int AES_KEY_BIT = 256;
    private static final Charset UTF_8 = StandardCharsets.UTF_8;

    private static SecretKeyFactory factory;
    private static KeySpec spec;

    public Encryption (String encryptionKey) throws NoSuchAlgorithmException {
        MessageDigest hash = MessageDigest.getInstance("SHA-256");
        char[] password = encryptionKey.toCharArray();
        byte[] salt = hash.digest(encryptionKey.getBytes(UTF_8));

        factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        spec = new PBEKeySpec(password, salt, 65536, AES_KEY_BIT);
    }

    ///// decryptString() ///// <editor-fold desc="~decryptString()~">
    public String decryptString (String cText) throws Exception {
        try {
            byte[] cTextBytes = Base64.getDecoder().decode(cText);
            ByteBuffer buffer = ByteBuffer.wrap(cTextBytes);

            byte[] iv = new byte[IV_LENGTH_BYTE];
            buffer.get(iv);

            byte[] cipherText = new byte[buffer.remaining()];
            buffer.get(cipherText);

            SecretKey tmp = factory.generateSecret(spec);
            SecretKey secretKey = new SecretKeySpec(tmp.getEncoded(), METHOD);
            Cipher cipher = Cipher.getInstance(METHOD);
            SecretKeySpec keySpec = new SecretKeySpec(secretKey.getEncoded(), "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(iv);

            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
            byte[] decryptedText = cipher.doFinal(cipherText);

            return new String(decryptedText);
        } catch (Exception e) { return null; }
    }
    ///// End decryptString() ///// </editor-fold>

    ///// encryptString() ///// <editor-fold desc="~encryptString()~">
    public String encryptString (String pText) throws Exception {
        try {
            SecureRandom secureRandom = new SecureRandom();
            byte[] iv = new byte[IV_LENGTH_BYTE];
            secureRandom.nextBytes(iv);

            SecretKey tmp = factory.generateSecret(spec);
            SecretKey secretKey = new SecretKeySpec(tmp.getEncoded(), METHOD);
            Cipher cipher = Cipher.getInstance(METHOD);
            SecretKeySpec keySpec = new SecretKeySpec(secretKey.getEncoded(), "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(iv);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);

            byte[] cipherText = cipher.doFinal(pText.getBytes(UTF_8));
            byte[] ivText = ByteBuffer.allocate(iv.length + cipherText.length)
                    .put(iv)
                    .put(cipherText)
                    .array();
            return Base64.getEncoder().encodeToString(ivText);
        } catch (Exception e) { return null; }
    }
    ///// End encryptString() ///// </editor-fold>
}