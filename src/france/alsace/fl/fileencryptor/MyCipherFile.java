package france.alsace.fl.fileencryptor;

import java.io.Serializable;

/**
 * The cipher file to serialize : contains all the data of the ciphered file
 * @author Florent
 */
public class MyCipherFile implements Serializable {
    
    public static final String CIPHER_FILE_EXTENSION = "ffe";
    
    private final byte[] data;
    private final String name;
    private final byte[] salt;
    private final byte[] iv;
    
    /**
     * Constructor
     * @param data the ciphered data
     * @param name the name of the file
     * @param salt the salt
     * @param iv the initialization vector
     */
    public MyCipherFile(byte[] data, String name, byte[] salt, byte[] iv) {
        this.data = data;
        this.name = name;
        this.salt = salt;
        this.iv = iv;
    }
    
    /**
     * Get the salt
     * @return the salt
     */
    public byte[] getSalt() {
        return salt;
    }
    
    /**
     * Get the file name
     * @return the file name
     */
    public String getName() {
        return name;
    }
    
    /**
     * get the initialization vector
     * @return the IV
     */
    public byte[] getIv() {
        return iv;
    }
    
    /**
     * Get the data file
     * @return the data file
     */
    public byte[] getData() {
        return data;
    }
}
