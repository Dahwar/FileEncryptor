/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package france.alsace.fl.fileencryptor;

import java.io.Serializable;

/**
 *
 * @author Florent
 */
public class MyCipherFile implements Serializable {
    
    public static final String CIPHER_FILE_EXTENSION = "ffe";
    
    private final byte[] data;
    private final String name;
    private final byte[] salt;
    private final byte[] iv;
    
    public MyCipherFile(byte[] data, String name, byte[] salt, byte[] iv) {
        this.data = data;
        this.name = name;
        this.salt = salt;
        this.iv = iv;
    }
    
    public byte[] getSalt() {
        return salt;
    }
    
    public String getName() {
        return name;
    }
    
    public byte[] getIv() {
        return iv;
    }
    
    public byte[] getData() {
        return data;
    }
}
