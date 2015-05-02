/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package france.alsace.fl.fileencryptor;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
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
public class Decryptor {
      
    private final int passwordIterations;
    private final int keySize;
    
    public Decryptor(int passwordIterations, int keySize) {
        this.passwordIterations = passwordIterations;
        this.keySize = keySize;
    }
    
    public MyFile decipher(MyCipherFile myCipherFile, String password) {
        try {
            // derive the key
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            PBEKeySpec spec = new PBEKeySpec(
                    password.toCharArray(),
                    myCipherFile.getSalt(),
                    passwordIterations,
                    keySize
            );
            
            SecretKey secretKey = factory.generateSecret(spec);
            SecretKeySpec secret = new SecretKeySpec(secretKey.getEncoded(), "AES");
            
            //decrypt the message
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(myCipherFile.getIv()));
            
            return new MyFile(cipher.doFinal(myCipherFile.getData()), myCipherFile.getName());
            
        } catch (NoSuchAlgorithmException 
                | InvalidKeySpecException 
                | NoSuchPaddingException 
                | InvalidKeyException 
                | InvalidAlgorithmParameterException 
                | IllegalBlockSizeException 
                | BadPaddingException ex) {
            Logger.getLogger(Decryptor.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
}
