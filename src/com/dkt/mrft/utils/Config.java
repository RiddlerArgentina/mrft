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
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Locale;
import java.util.Map;

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
        // @FIXME I honestly didn't check it, but if the locale is changed in
        // '~/.dkt/mrft/conf.properties' and it's different than the system
        // locale this will not use the appropiate config file, since the 
        // forced locale is set AFTER the this is read...
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
        
        file = new File(PATH + "conf.properties");
        //Someone could have deleted the file, it's mostly to avoid the exception
        if (!file.exists()) return;
        
        CommentedProperties props = new CommentedProperties();
        try (InputStream is = new FileInputStream(file)) {
            props.load(is);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        // If systems locale changes we want to update the config file while keeping 
        // the values
        for (Map.Entry<Object, Object> entry : INSTANCE.entrySet()) {
            entry.setValue(props.getOrDefault(entry.getKey(), entry.getValue()));
        }
    }
    
    /**
     * Retrieves the {@code Config} instance
     * 
     * @return {@code Config} instance
     */
    public static Config get() {
        return INSTANCE;
    }

    /**
     * Saves the current {@code Config} in {@literal '~/.dkt/mrft/conf.properties'}
     */
    public void save() {
        File file = new File(PATH);
        
        if (!file.exists()) {
            file.mkdirs();
        }
        
        file = new File(PATH + "conf.properties");
        
        try (OutputStream os = new FileOutputStream(file)) {
            INSTANCE.store(os, "");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
