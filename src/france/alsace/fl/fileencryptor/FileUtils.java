package france.alsace.fl.fileencryptor;

import java.util.StringTokenizer;

/**
 * Tools for files
 * @author Florent
 */
public class FileUtils {

    /**
     * To get the file extension
     * @param fileName the name of the file
     * @return the extension of the file
     */
    public static final String getExtention(String fileName) {
        StringTokenizer st = new StringTokenizer(fileName, ".");
        String s = null;
        //get the last token, i.e. the extension, in a string
        while(st.hasMoreTokens()) {
            s = st.nextToken();
        }
        return s.toLowerCase();
    }
}
