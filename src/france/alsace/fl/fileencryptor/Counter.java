/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package france.alsace.fl.fileencryptor;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 *
 * @author Florent
 */
public class Counter {
    private IntegerProperty cnt = new SimpleIntegerProperty();
    
    public Counter() {
        this.cnt.set(0);
    }
    
    public Counter(int init) {
        this.cnt.set(init);
    }
    
    public synchronized void increment() {
        cnt.set(cnt.getValue()+1);
    }
    
    public IntegerProperty getValue() {
        return cnt;
    }
}
