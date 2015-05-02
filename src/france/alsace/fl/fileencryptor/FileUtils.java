/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package france.alsace.fl.fileencryptor;

import java.util.StringTokenizer;

/**
 *
 * @author Florent
 */
public class FileUtils {

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
