package france.alsace.fl.fileencryptor;

/**
 * The not ciphered file : contains the file in byte array format
 * @author Florent
 */
public class MyFile {
    
    private final byte[] data;
    private final String name;
    
    /**
     * Constructor
     * @param data the file
     * @param name the file name
     */
    public MyFile(byte[] data, String name) {
        this.data = data;
        this.name = name;
    }
    
    /**
     * Get the file name
     * @return the file name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Get the data file
     * @return the data file
     */
    public byte[] getData() {
        return data;
    }
}
