/*
 * MIT License
 *
 * Copyright (c) 2016 Federico Vera <https://github.com/dktcoding>
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.dkt.mrft.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Locale;

/**
 *  
 * @author Federico Vera {@literal <fedevera at unc.edu.ar>}
 */
public class Config extends CommentedProperties {
    private static final Config INSTANCE = new Config();
    
    private static final String INTERNAL_CONF_PATH    = "/res/i18n/conf/conf_%s.properties";
    private static final String INTERNAL_CONF_PATH_EN = "/res/i18n/conf/conf.properties";
    
    private static final String PATH = System.getProperty("user.home") 
                                     + File.separator + ".dkt"
                                     + File.separator + "mrft"
                                     + File.separator;
    
    private Config () {
    }
    static {
        loadDefault();
        loadCurrent();
    }

    private static void loadDefault() {
        Locale locale = Locale.getDefault();
        String path = null;
        
        // Yeah we now have localized configs... 
        String fooPath = String.format(INTERNAL_CONF_PATH, locale.getLanguage());
        URL url = Config.class.getResource(fooPath);
        if (url != null) {
            path = fooPath;
        }
        
        if (path == null) path = INTERNAL_CONF_PATH_EN;
        
        try (InputStream is = Config.class.getResourceAsStream(path)) {
            INSTANCE.load(is);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void loadCurrent() {
        File file = new File(PATH);
        
        if (!file.exists()) {
            file.mkdirs();
            return;
        }
        
        //Someone could have deleted the file, it's mostly to avoid the exception
        file = new File(PATH + "conf.properties");
        if (!file.exists()) return;
        
        //@TODO Test what really happens when the config properties differ... (i.e. new property)
        try (InputStream is = new FileInputStream(file)) {
            INSTANCE.load(is);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public static Config get() {
        return INSTANCE;
    }
}
