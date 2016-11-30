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

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Properties;

/**
 * The CommentedProperties class is an extension of {@link Properties} to allow 
 * retention of comment lines and blank (whitespace only) lines in the 
 * properties file.
 * 
 * @author Honestly I'd seen this class all around the internet and never figure out who was the 
 * one that actually wrote it...
 */
public class CommentedProperties extends Properties {    
    /**
     * Use an ArrayList to keep a copy of lines that are a comment or 'blank'
     */
    public final ArrayList<String> lineData = new ArrayList<>(50);

    /**
     * Use an ArrayList to keep a copy of lines containing a key, i.e. they are
     * a property.
     */
    public final ArrayList<String> keyData = new ArrayList<>(50);

    /**
     * Load properties from the specified InputStream. Overload the load method 
     * in Properties so we can keep comment and blank lines.<br>
     * The spec says that the file must be encoded using ISO-8859-1.
     *
     * @param inStream The InputStream to read.
     * @throws IOException If an error occurs whilst reading
     */
    @Override
    public void load(InputStream inStream) throws IOException {
        final BufferedReader reader =
                new BufferedReader(
                        new InputStreamReader(inStream, StandardCharsets.ISO_8859_1));
        String line;

        while ((line = reader.readLine()) != null) {
            char c = 0;
            int pos = 0;
            // Leading whitespaces must be deleted first.
            while (pos < line.length() && 
                   Character.isWhitespace(c = line.charAt(pos))) {
                pos++;
            }

            // If empty line or begins with a comment character, save this line
            // in lineData and save a "" in keyData.
            if ((line.length() - pos) == 0
                  || line.charAt(pos) == '#'
                  || line.charAt(pos) == '!') {
                lineData.add(line);
                keyData.add("");
                continue;
            }

            // The characters up to the next Whitespace, ':', or '='
            // describe the key.  But look for escape sequences.
            // Try to short-circuit when there is no escape char.
            int start = pos;
            final boolean needsEsc = line.indexOf('\\', pos) != -1;
            final StringBuilder key = new StringBuilder(100);

            while (pos < line.length() && 
                  !Character.isWhitespace(c = line.charAt(pos++)) && 
                   c != '=' && c != ':') {
                if (needsEsc && c == '\\') {
                    if (pos == line.length()) {
                        // The line continues on the next line.  If there
                        // is no next line, just treat it as a key with an
                        // empty value.
                        line = reader.readLine();
                        if (line == null) {
                            line = "";
                        }
                        pos = 0;
                        while (pos < line.length() &&
                               Character.isWhitespace(c = line.charAt(pos))) {
                            pos++;
                        }
                    } else {
                        c = line.charAt(pos++);
                        switch (c) {
                            case 'n': key.append('\n'); break;
                            case 't': key.append('\t'); break;
                            case 'r': key.append('\r'); break;
                            case 'u':
                                if (pos + 4 <= line.length()) {
                                    char uni = (char) Integer.parseInt(line.substring(pos, pos + 4), 16);
                                    key.append(uni);
                                    pos += 4;
                                }
                                break;
                            default: key.append(c); break;
                        }
                    }
                } else if (needsEsc) {
                    key.append(c);
                }
            }

            final boolean isDelim = (c == ':' || c == '=');

            String keyString;
            if (needsEsc) {
                keyString = key.toString();
            } else if (isDelim || Character.isWhitespace(c)) {
                keyString = line.substring(start, pos - 1);
            } else {
                keyString = line.substring(start, pos);
            }

            while (pos < line.length() && 
                   Character.isWhitespace(c = line.charAt(pos))) {
                pos++;
            }

            if (!isDelim && (c == ':' || c == '=')) {
                pos++;
                while (pos < line.length() && 
                       Character.isWhitespace(line.charAt(pos))) {
                    pos++;
                }
            }

            // Short-circuit if no escape chars found.
            if (!needsEsc) {
                put(keyString, line.substring(pos));
                // Save a "" in lineData and save this
                // keyString in keyData.
                lineData.add("");
                keyData.add(keyString);
                continue;
            }

            // Escape char found so iterate through the rest of the line.
            final StringBuilder elem = new StringBuilder(line.length() - pos);
            while (pos < line.length()) {
                c = line.charAt(pos++);
                if (c == '\\') {
                    if (pos == line.length()) {
                        // The line continues on the next line.
                        line = reader.readLine();

                        // We might have seen a backslash at the end of
                        // the file.  The JDK ignores the backslash in
                        // this case, so we follow for compatibility.
                        if (line == null) {
                            break;
                        }

                        pos = 0;
                        while (pos < line.length() && 
                               Character.isWhitespace(line.charAt(pos))) {
                            pos++;
                        }
                        elem.ensureCapacity(line.length() - pos
                                + elem.length());
                    } else {
                        c = line.charAt(pos++);
                        switch (c) {
                            case 'n': elem.append('\n'); break;
                            case 't': elem.append('\t'); break;
                            case 'r': elem.append('\r'); break;
                            case 'u':
                                if (pos + 4 <= line.length()) {
                                    char uni = (char)Integer.parseInt(
                                            line.substring(pos, pos + 4), 16);
                                    elem.append(uni);
                                    pos += 4;
                                }   // else throw exception?
                                break;
                            default: elem.append(c); break;
                        }
                    }
                } else {
                    elem.append(c);
                }
            }
            put(keyString, elem.toString());
            // Save a "" in lineData and save this
            // keyString in keyData.
            lineData.add("");
            keyData.add(keyString);
        }
    }

    @Override
    public synchronized Object put(Object key, Object value) {
        return super.put(key, Objects.toString(value, ""));
    }
    
    public int getInt(String key) {
        return Integer.parseInt(getProperty(key));
    }
    
    public double getDouble(String key) {
        return Double.parseDouble(getProperty(key));
    }
    
    public Color getColor(String key) {
        return new Color((int) Long.parseLong(getProperty(key), 16), true);
    }
    
    public boolean getBool(String key) {
        return Boolean.parseBoolean(getProperty(key));
    }
    
    public String get(String key) {
        return getProperty(key);
    }

    /**
     * Write the properties to the specified OutputStream.
     *
     * Overloads the store method in Properties so we can put back comment and
     * blank lines.<br>
     * The spec says that the file must be encoded using ISO-8859-1.
     *
     * @param out The OutputStream to write to.
     * @param header Ignored, here for compatability w/ Properties.
     *
     * @exception IOException
     */
    @Override
    public void store(OutputStream out, String header) throws IOException {
        final OutputStreamWriter oow = new OutputStreamWriter(out, StandardCharsets.ISO_8859_1);
        final PrintWriter writer = new PrintWriter(oow);

        String line;
        String key;
        final StringBuilder s = new StringBuilder(100);

        for (int i = 0, n = lineData.size(); i < n; i++) {
            line = lineData.get(i);
            key = keyData.get(i);
            
            if (key.length() > 0) {  // This is a 'property' line, so rebuild it
                formatForOutput(key, s, true);
                s.append('=');
                formatForOutput((String) get(key), s, false);
                writer.println(s);
            } else {  // was a blank or comment line, so just restore it
                writer.println(line);
            }
        }
        
        writer.flush();
    }

    /**
     * Need this method from Properties because original code has StringBuilder, 
     * which is an element of Java 1.5, used StringBuffer instead (because this 
     * code was written for Java 1.4)
     *
     * @param str the string to format
     * @param buffer buffer to hold the string
     * @param key true if str the key is formatted, false if the value is 
     * formatted
     */
    private void formatForOutput(String str, StringBuilder buffer, boolean key) {
        if (key) {
            buffer.setLength(0);
            buffer.ensureCapacity(str.length());
        } else {
            buffer.ensureCapacity(buffer.length() + str.length());
        }

        boolean head = true;
        final int size = str.length();
        for (int i = 0; i < size; i++) {
            final char c = str.charAt(i);
            switch (c) {
                case '\n': buffer.append("\\n"); break;
                case '\r': buffer.append("\\r"); break;
                case '\t': buffer.append("\\t"); break;
                case ' ' : buffer.append(head ? "\\ " : " "); break;
                case '\\':
                case '!':
                case '#':
                case '=':
                case ':': buffer.append('\\').append(c); break;
                default:
                    if (c < ' ' || c > '~') {
                        String hex = Integer.toHexString(c);
                        buffer.append("\\u0000".substring(0, 6 - hex.length()));
                        buffer.append(hex);
                    } else {
                        buffer.append(c);
                    }
            }
            
            if (c != ' ') {
                head = key;
            }
        }
    }

    /**
     * Add a Property to the end of the CommentedProperties.
     *
     * @param keyString	The Property key.
     * @param value	The value of this Property.
     */
    public void add(String keyString, String value) {
        put(keyString, value);
        lineData.add("");
        keyData.add(keyString);
    }

    /**
     * Add a comment or blank line or comment to the end of the CommentedProperties.
     *
     * @param line The string to add to the end, make sure this is a comment or a 'whitespace' line.
     */
    public void addLine(String line) {
        lineData.add(line);
        keyData.add("");
    }
}