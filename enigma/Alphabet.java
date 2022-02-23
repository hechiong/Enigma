package enigma;

import static enigma.EnigmaException.*;
import static enigma.TestUtils.*;
import static java.lang.Character.*;

/** An alphabet of encodable characters.  Provides a mapping from characters
 *  to and from indices into the alphabet.
 *  @author Henry Chiong
 */
class Alphabet {

    /** A new alphabet containing CHARS.  Character number #k has index
     *  K (numbering from 0). No character may be duplicated. */
    Alphabet(String chars) {
        checkAlphabet(chars);
        alpha = chars;
    }

    /** A default alphabet of all upper-case characters. */
    Alphabet() {
        this(UPPER_STRING);
    }

    /** Returns the size of the alphabet. */
    int size() {
        return alpha.length();
    }

    /** Returns true if CH is in this alphabet. */
    boolean contains(char ch) {
        return alpha.indexOf(ch) >= 0;
    }

    /** Returns character number INDEX in the alphabet, where
     *  0 <= INDEX < size(). */
    char toChar(int index) {
        if (index < 0 || index >= size()) {
            throw error(index
                    + "is an invalid index to access in the alphabet.");
        }
        return alpha.charAt(index);
    }

    /** Returns the index of character CH which must be in
     *  the alphabet. This is the inverse of toChar(). */
    int toInt(char ch) {
        if (!contains(ch)) {
            throw error(ch + " must be in the alphabet.");
        }
        return alpha.indexOf(ch);
    }

    /** Checks if CHARS is a valid alphabet, which is achieved by containing
     *  valid characters and not containing repeated characters. If CHARS is an
     *  invalid alphabet, throw an EnigmaException. */
    private static void checkAlphabet(String chars) {
        for (int i = 0; i < chars.length(); i += 1) {
            char c1 = chars.charAt(i);
            checkInvalidChar(c1);
            for (int j = i + 1; j < chars.length(); j += 1) {
                char c2 = chars.charAt(j);
                checkEqualChars(c1, c2);
            }
        }
    }

    /** Throws an EnigmaException if C is either
     *  a whitespace character, '*', '(', or ')'. */
    private static void checkInvalidChar(char c) {
        if (isWhitespace(c) || c == '*' || c == '(' || c  == ')') {
            throw error(c + " is an invalid character in the alphabet.");
        }
    }

    /** Throws an EnigmaException if C1 is the same character as C2. */
    private static void checkEqualChars(char c1, char c2) {
        if (c1 == c2) {
            throw error(c1 + "is repeated in the alphabet.");
        }
    }

    /** Characters that represent this Alphabet. */
    private String alpha;
}
