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

import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

/**
 *
 * @author Federico Vera {@literal <fedevera at unc.edu.ar>}
 */
public class BundleDecorator {
    private final ResourceBundle bundle;
    private final Set<String> notFound = new HashSet<>(10);
    public BundleDecorator(String name) {
        bundle = ResourceBundle.getBundle(name);
    }
    
    public String __(String key) {
        try {
            return bundle.getString(key);
        } catch (Exception e) {
            synchronized(notFound) {
                notFound.add(key);
            }
            return key;
        }
    }
    
    public String __(String key, Object... args) {
        return String.format(__(key), args);
    }
    
    public Set<String> getNotFound() {
        return new HashSet<>(notFound);
    }

}
