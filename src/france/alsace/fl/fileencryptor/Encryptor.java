/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package france.alsace.fl.fileencryptor;

import java.security.AlgorithmParameters;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author Florent
 */
public class Encryptor {
    
    private final int passwordIterations;
    private final int keySize;
    
    public Encryptor(int passwordIterations, int keySize) {
        this.passwordIterations = passwordIterations;
        this.keySize = keySize;
    }
    
    public MyCipherFile cipher(MyFile myFile, String password) {
        try {
            //generate salt
            byte[] salt = generateSalt();
            
            //derive the key
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            PBEKeySpec spec = new PBEKeySpec(
                    password.toCharArray(),
                    salt,
                    passwordIterations,
                    keySize
            );
            
            SecretKey secretKey = factory.generateSecret(spec);
            SecretKeySpec secret = new SecretKeySpec(secretKey.getEncoded(), "AES");
            
            //encrypt the message
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secret);
            
            AlgorithmParameters params = cipher.getParameters();
            byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();
            
            return new MyCipherFile(cipher.doFinal(myFile.getData()), myFile.getName(), salt, iv);
            
        } catch (NoSuchAlgorithmException 
                | InvalidKeySpecException 
                | NoSuchPaddingException 
                | InvalidKeyException 
                | InvalidParameterSpecException 
                | IllegalBlockSizeException 
                | BadPaddingException ex) {
            Logger.getLogger(Encryptor.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    
    private byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[20];
        random.nextBytes(bytes);
        return bytes;
    }
}
