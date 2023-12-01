import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.text.ParseException;

/**
 * Utilities for our simple implementation of JSON.
 */
public class JSON {
    // +---------------+-----------------------------------------------
    // | Static fields |
    // +---------------+

    /**
     * The current position in the input.
     */
    static int ch;

    static int pos;

    // +----------------+----------------------------------------------
    // | Static methods |
    // +----------------+

    /**
     * Parse a string into JSON.
     */
    public static JSONValue parse(String source) throws ParseException, IOException {
        return parse(new StringReader(source));
    } // parse(String)

    /**
     * Parse a file into JSON.
     */
    public static JSONValue parseFile(String filename) throws ParseException, IOException {
        FileReader reader = new FileReader(filename);
        JSONValue result = parse(reader);
        reader.close();
        return result;
    } // parseFile(String)

    /**
     * Parse JSON from a reader.
     */
    public static JSONValue parse(Reader source) throws ParseException, IOException {
        ch = 0;
        JSONValue result = parseKernel(source);
        if (-1 != skipWhitespace(source)) {
            throw new ParseException("Characters remain at end", pos);
        }
        return result;
    } // parse(Reader)

    // +---------------+-----------------------------------------------
    // | Local helpers |
    // +---------------+

    /**
     * Parse JSON from a reader, keeping track of the current position
     */
    static JSONValue parseKernel(Reader source) throws ParseException, IOException {

        String tempName = "";
        ch = skipWhitespace(source);

        if (-1 == ch) {
            throw new ParseException("Unexpected end of file", pos);
        }

        if ((char) ch == ',') {
            ch = skipWhitespace(source);
        }

        if ((char) ch == '[') {
            JSONArray newArray = new JSONArray();

            while ((char) ch != ']') {

                newArray.add(parseKernel(source));

            }
            ch = skipWhitespace(source);
            return newArray;
        }

        if ((char) ch == '"') {
            ch = skipWhitespace(source);
            while ((char) ch != '"') {
                tempName = tempName.concat(String.valueOf((char) ch));
                ch = skipWhitespace(source);
            }
            // skips one extra time to find the comma
            ch = skipWhitespace(source);
            return new JSONString(tempName);
        }

        // checks if constant
        if (Character.isAlphabetic((char) ch)) {
            while (((char) ch != '}') && ((char) ch != ']') && ((char) ch != ',')) {
                tempName = tempName.concat(String.valueOf((char) ch));
                ch = skipWhitespace(source);
            }
            if (tempName.equals("null")) {
                return JSONConstant.NULL;
            } else if (tempName.equals("true")) {
                return JSONConstant.TRUE;
            } else if (tempName.equals("false")) {
                return JSONConstant.FALSE;
            } else {
                System.err.println("Not a valid constant");
            }
        }

        // checks if is either Integer or Real number
        if (((char) ch == '-') || (Character.isDigit((char) ch))) {
            boolean isReal = false;
            while (((char) ch != '}') && ((char) ch != ']') && ((char) ch != ',')) {
                // checks if value is a Real Number
                if ((char) ch == '.' || (char) ch == 'e' || (char) ch == 'E') {
                    isReal = true;
                } // if
                tempName = tempName.concat(String.valueOf((char) ch));
                ch = skipWhitespace(source);
            } // while
            if (isReal) {
                return new JSONReal(tempName);
            } else {
                return new JSONInteger(tempName);
            } // else

        } // if

        if ((char) ch == '{') {
            JSONHash<JSONString,JSONValue> newHash = new JSONHash<>();
            JSONString jString;
            JSONValue jValue;

            while ((char) ch != '}') {
                jString = (JSONString) parseKernel(source);
                jValue = parseKernel(source);
                newHash.set(jString, jValue);
            }
            ch = skipWhitespace(source);
            return newHash;
        } // if
        return null;

    } // parseKernel

    /**
     * Get the next character from source, skipping over whitespace.
     */
    static int skipWhitespace(Reader source) throws IOException {
        int ch;
        do {
            ch = source.read();
            ++pos;
        } while (isWhitespace(ch));
        return ch;
    } // skipWhitespace(Reader)

    /**
     * Determine if a character is JSON whitespace (newline, carriage return,
     * space, or tab).
     */
    static boolean isWhitespace(int ch) {
        return (' ' == ch) || ('\n' == ch) || ('\r' == ch) || ('\t' == ch);
    } // isWhiteSpace(int)

} // class JSON
