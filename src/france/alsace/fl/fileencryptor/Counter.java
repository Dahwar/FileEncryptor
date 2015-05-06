package france.alsace.fl.fileencryptor;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * A observable counter
 * @author Florent
 */
public class Counter {
    private IntegerProperty cnt = new SimpleIntegerProperty();
    
    /**
     * Default constructor
     */
    public Counter() {
        this.cnt.set(0);
    }
    
    /**
     * Constructor with parameter
     * @param init the initial value of the counter
     */
    public Counter(int init) {
        this.cnt.set(init);
    }
    
    /**
     * Increment the counter (synchronized function)
     */
    public synchronized void increment() {
        cnt.set(cnt.getValue()+1);
    }
    
    /**
     * Return the counter value (property)
     * @return the counter value (property)
     */
    public IntegerProperty getCounter() {
        return cnt;
    }
    
    /**
     * Return the counter value (int)
     * @return the counter value (int)
     */
    public int getValue() {
        return cnt.get();
    }
}
