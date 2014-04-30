/*
 * java.util.Properties: part of the Java Class Libraries project.
 * Copyright (C) 1998 Jochen Hoenicke
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Library General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307, USA.
 */

package java.util;
import java.io.*;

/**
 * An example of a properties file for the german language is given
 * here.  This extends the example given in ListResourceBundle.
 * Create a file MyResource_de.properties with the following contents
 * and put it in the CLASSPATH.  (The character
 * <code>\</code><code>u00e4</code> is the german &auml;)
 * 
 * <pre>
 * s1=3
 * s2=MeineDisk
 * s3=3. M\<code></code>u00e4rz 96
 * s4=Die Diskette ''{1}'' enth\<code></code>u00e4lt {0} in {2}.
 * s5=0
 * s6=keine Dateien
 * s7=1
 * s8=eine Datei
 * s9=2
 * s10={0,number} Dateien
 * s11=Das Formatieren schlug fehl mit folgender Exception: {0}
 * s12=FEHLER
 * s13=Ergebnis
 * s14=Dialog
 * s15=Auswahlkriterium
 * s16=1,3
 * </pre>
 *
 * Although this is a sub class of a hash table, you should never
 * insert anything other than strings to this property, or several
 * methods, that need string keys and values, will fail.  To ensure
 * this, you should use the <code>get/setProperty</code> method instead
 * of <code>get/put</code>.
 *
 * @see PropertyResourceBundle
 * @author Jochen Hoenicke */
public class Properties extends Hashtable {
    /**
     * The property list that contains default values for any keys not
     * in this property list.  
     */
    private Properties defaults;

    /**
     * Creates a new empty property list.
     */
    public Properties() {
        this.defaults = null;
    }
    
    /**
     * Create a new empty property list with the specified default values.
     * @param defaults a Properties object containing the default values.
     */
    public Properties(Properties defaults) {
        this.defaults = defaults;
    }

    /**
     * Reads a property list from an input stream.  The stream should
     * have the following format: <br>
     *
     * An empty line or a line starting with <code>#</code> or
     * <code>!</code> is ignored.  An backslash (<code>\</code>) at the
     * end of the line makes the line continueing on the next line
     * (but make sure there is no whitespace after the backslash).
     * Otherwise, each line describes a key/value pair. <br>
     *
     * The chars up to the first whitespace, = or : are the key.  You
     * can include this caracters in the key, if you precede them with
     * a backslash (<code>\</code>). The key is followed by optional
     * whitespaces, optionally one <code>=</code> or <code>:</code>,
     * and optionally some more whitespaces.  The rest of the line is
     * the resource belonging to the key. <br>
     *
     * Escape sequences <code>\t, \n, \r, \\, \", \', \!, \#, \ </code>(a
     * space), and unicode characters with the
     * <code>\</code><code>u</code>xxxx notation are detected, and 
     * converted to the corresponding single character. <br>
     *
     * <pre>
     * # This is a comment
     * key     = value
     * k\:5      \ a string starting with space and ending with newline\n
     * # This is a multiline specification; note that the value contains
     * # no white space.
     * weekdays: Sunday,Monday,Tuesday,Wednesday,\
     *           Thursday,Friday,Saturday
     * # The safest way to include a space at the end of a value:
     * label   = Name:\<code></code>u0020
     * </pre>
     *
     * @param in the input stream
     * @exception IOException if an error occured when reading
     * from the input.  */
    public void load(InputStream inStream) throws IOException {
        BufferedReader reader = 
            new BufferedReader(new InputStreamReader(inStream));
        String line;
        while ((line = reader.readLine()) != null) {
            char c = 0;
            int pos = 0;
            while (pos < line.length()
                   && Character.isWhitespace(c = line.charAt(pos)))
                pos++;

            // If line is empty or begins with a comment character,
            // skip this line.
            if (pos == line.length() || c == '#' || c == '!')
                continue;

            // The characaters up to the next Whitespace, ':', or '='
            // describe the key.  But look for escape sequences.
            StringBuffer key = new StringBuffer();
            while (pos < line.length() 
                   && !Character.isWhitespace(c = line.charAt(pos++))
                   && c != '=' && c != ':') {
                if (c == '\\') {
                    if (pos == line.length()) {
                        // The line continues on the next line.
                        line = reader.readLine();
                        pos = 0;
                        while (pos < line.length()
                               && Character.isWhitespace(c = line.charAt(pos)))
                            pos++;
                    } else {
                        c = line.charAt(pos++);
                        switch (c) {
                        case 'n':
                            key.append('\n');
                            break;
                        case 't':
                            key.append('\t');
                            break;
                        case 'r':
                            key.append('\r');
                            break;
                        case 'u':
                            if (pos+4 <= line.length()) {
                                char uni = (char) Integer.parseInt
                                    (line.substring(pos, pos+4), 16);
                                key.append(uni);
                            } // else throw exception?
                            break;
			default:
                            key.append(c);
                            break;
                        }
                    }
                } else 
                    key.append(c);
            }
            
            boolean isDelim = (c == ':' || c == '=');
            while (pos < line.length()
                   && Character.isWhitespace(c = line.charAt(pos)))
                pos++;

            if (!isDelim && (c == ':' || c == '=')) {
                pos++;
                while (pos < line.length()
                       && Character.isWhitespace(c = line.charAt(pos)))
                    pos++;
            }

            StringBuffer element = new StringBuffer(line.length()-pos);
            while (pos < line.length()) {
                c = line.charAt(pos++);
                if (c == '\\') {
                    if (pos == line.length()) {
                        // The line continues on the next line.
                        line = reader.readLine();
                        pos = 0;
                        while (pos < line.length()
                               && Character.isWhitespace(c = line.charAt(pos)))
                            pos++;
			element.ensureCapacity(line.length()-pos+element.length());
                    } else {
                        c = line.charAt(pos++);
                        switch (c) {
                        case 'n':
                            element.append('\n');
                            break;
                        case 't':
                            element.append('\t');
                            break;
                        case 'r':
                            element.append('\r');
                            break;
                        case 'u':
                            if (pos+4 <= line.length()) {
                                char uni = (char) Integer.parseInt
                                    (line.substring(pos, pos+4), 16);
                                element.append(uni);
                            } // else throw exception?
                            break;
			default:
                            element.append(c);
                            break;
                        }
                    }
                } else 
                    element.append(c);
            }
            put(key.toString(), element.toString());
        }
    }

    /**
     * Calls <code>store(OutputStream out, String header)</code> and
     * ignores the IOException that may be thrown.
     * @deprecated use store instead.
     * @exception ClassCastException if this property contains any key or
     * value that isn't a string.
     */
    public void save(OutputStream out, String header) {
        try {
            store(out,header);
        } catch (IOException ex) {
        }
    }
    
    /**
     * Writes the key/value pairs to the given output stream. <br>
     *
     * If header is not null, this method writes a comment containing
     * the header as first line to the stream.  The next line (or first
     * line if header is null) contains a comment with the current date.
     * Afterwards the key/value pairs are written to the stream in the
     * following format. <br>
     *
     * Each line has the form <code>key = value</code>.  Newlines,
     * Returns and tabs are written as <code>\n,\t,\r</code> resp.
     * The characters <code>\, !, #, =</code> and <code>:</code> are
     * preceeded by a backslash.  Spaces are preceded with a backslash,
     * if and only if they are at the beginning of the key.  Characters
     * that are not in the ascii range 33 to 127 are written in the
     * <code>\</code><code>u</code>xxxx Form.
     *
     * @param out the output stream
     * @param header the header written in the first line, may be null.
     * @exception ClassCastException if this property contains any key or
     * value that isn't a string.
     */
    public void store(OutputStream out, String header) throws IOException {
        PrintWriter writer = new PrintWriter(out);
        if (header != null)
            writer.println("#"+header);
        writer.println("#"+new Date().toString());
        list(writer);
    }
    
    /**
     * Adds the given key/value pair to this properties.  This calls
     * the hashtable method put.
     * @param key the key for this property
     * @param value the value for this property
     * @return The old value for the given key.
     * @since JDK1.2 */
    public Object setProperty(String key, String value) {
        return put(key,value);
    }

    /**
     * Gets the property with the specified key in this property list.
     * If the key is not found, the default property list is searched.
     * If the property is not found in default or the default of
     * default, null is returned.
     * @param key The key for this property.
     * @param defaulValue A default value
     * @return The value for the given key, or null if not found. 
     * @exception ClassCastException if this property contains any key or
     * value that isn't a string.
     */
    public String getProperty(String key) {
        return getProperty(key, null);
    }

    /**
     * Gets the property with the specified key in this property list.  If
     * the key is not found, the default property list is searched.  If the
     * property is not found in default or the default of default, the 
     * specified defaultValue is returned.
     * @param key The key for this property.
     * @param defaulValue A default value
     * @return The value for the given key.
     * @exception ClassCastException if this property contains any key or
     * value that isn't a string.
     */
    public String getProperty(String key, String defaultValue) {
        Properties prop = this;
        // Eliminate tail recursion.
        do {
            String value = (String) get(key);
            if (value != null)
                return value;
            prop = defaults;
        } while (prop != null);
        return defaultValue;
    }

    /**
     * Returns an enumeration of all keys in this property list, including
     * the keys in the default property list.
     */
    public Enumeration propertyNames() {
        return (defaults == null) ? keys() 
            : new DoubleEnumeration(keys(), defaults.propertyNames());
    }



    /**
     * Formats a key/value pair for output in a properties file.
     * See store for a description of the format.
     * @param key the key.
     * @param value the value.
     * @see #store
     */
    private String formatForOutput(String key, String value) {
	// This is a simple approximation of the expected line size.
        StringBuffer result = new StringBuffer(key.length()+value.length()+16);
        boolean head = true;
        for (int i=0; i< key.length(); i++) {
            char c = key.charAt(i);
            switch (c) {
            case '\n':
                result.append("\\n");
                break;
            case '\r':
                result.append("\\r");
                break;
            case '\t':
                result.append("\\t");
                break;
            case '\\':
                result.append("\\\\");
                break;
            case '!':
                result.append("\\!");
                break;
            case '#':
                result.append("\\#");
                break;
            case '=':
                result.append("\\=");
                break;
            case ':':
                result.append("\\:");
                break;
	    case ' ':
		result.append(head ? "\\ ": " ");
		break;
            default:
                if (c < 32 || c > '~') {
                    String hex = Integer.toHexString(c);
                    result.append("\\u0000".substring(0, 6-hex.length()));
                    result.append(hex);
                } else
                    result.append(c);
            }
            if (c != 32)
                head = false;
        }
        result.append('=');
        head=true;
        for (int i=0; i< value.length(); i++) {
            char c = value.charAt(i);
            switch (c) {
            case '\n':
                result.append("\\n");
                break;
            case '\r':
                result.append("\\r");
                break;
            case '\t':
                result.append("\\t");
                break;
            case '\\':
                result.append("\\\\");
                break;
            case '!':
                result.append("\\!");
                break;
            case '#':
                result.append("\\#");
                break;
	    case ' ':
		result.append(head ? "\\ ": " ");
		break;
            default:
                if (c < 32 || c > '~') {
                    String hex = Integer.toHexString(c);
                    result.append("\\u0000".substring(0, 6-hex.length()));
                    result.append(hex);
                } else
                    result.append(c);
            }
            if (c != 32)
                head = false;
        }
        return result.toString();
    }

    /**
     * Writes the key/value pairs to the given print stream.  They are
     * written in the way, described in the method store.
     * @param out the stream, where the key/value pairs are written to.
     * @exception ClassCastException if this property contains any key or
     * value that isn't a string.
     * @see #store
     */
    public void list(PrintStream out) {
        Enumeration keys = keys();
        Enumeration elts = elements();
        while (keys.hasMoreElements()) {
            String key = (String) keys.nextElement();
            String elt = (String) elts.nextElement();
            String output = formatForOutput(key,elt);
            out.println(output);
        }
    }

    /**
     * Writes the key/value pairs to the given print writer.  They are
     * written in the way, described in the method store.
     * @param out the writer, where the key/value pairs are written to.
     * @exception ClassCastException if this property contains any key or
     * value that isn't a string.
     * @see #store
     * @see #list(java.io.PrintStream)
     * @since JDK1.1
     */
    public void list(PrintWriter out) {
        Enumeration keys = keys();
        Enumeration elts = elements();
        while (keys.hasMoreElements()) {
            String key = (String) keys.nextElement();
            String elt = (String) elts.nextElement();
            String output = formatForOutput(key,elt);
            out.println(output);
        }
    }
}
