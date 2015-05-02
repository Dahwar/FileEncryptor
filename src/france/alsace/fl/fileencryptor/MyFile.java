/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package france.alsace.fl.fileencryptor;

/**
 *
 * @author Florent
 */
public class MyFile {
    
    private final byte[] data;
    private final String name;
    
    public MyFile(byte[] data, String name) {
        this.data = data;
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
    public byte[] getData() {
        return data;
    }
}
