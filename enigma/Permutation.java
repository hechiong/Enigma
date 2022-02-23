package enigma;

import static enigma.EnigmaException.*;
import static java.lang.Character.*;

/** Represents a permutation of a range of integers starting at 0 corresponding
 *  to the characters of an alphabet.
 *  @author Henry Chiong
 */
class Permutation {

    /** Set this Permutation to that specified by CYCLES, a string in the
     *  form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     *  is interpreted as a permutation in cycle notation.  Characters in the
     *  alphabet that are not included in any cycle map to themselves.
     *  Whitespace is ignored. */
    Permutation(String cycles, Alphabet alphabet) {
        checkCycles(cycles, alphabet);
        _alphabet = alphabet;
        _cycles = initCycles(cycles);
    }

    /** Return the value of P modulo SIZE. */
    final int wrap(int p, int size) {
        int r = p % size;
        if (r < 0) {
            r += size;
        }
        return r;
    }

    /** Returns the size of the alphabet I permute. */
    int size() {
        return _alphabet.size();
    }

    /** Return the result of applying this permutation to P modulo the
     *  alphabet size. */
    int permute(int p) {
        int index = wrap(p, size());
        String cycle = getCycle(index);
        int result = index;

        if (!cycle.equals("")) {
            char ch = _alphabet.toChar(index);
            int chIndex = cycle.indexOf(ch);
            int desiredIndex = wrap(chIndex + 1, cycle.length());
            char desiredChar = cycle.charAt(desiredIndex);
            result = _alphabet.toInt(desiredChar);
        }
        return result;
    }

    /** Return the result of applying the inverse of this permutation
     *  to  C modulo the alphabet size. */
    int invert(int c) {
        int index = wrap(c, size());
        String cycle = getCycle(index);
        int result = index;

        if (!cycle.equals("")) {
            char ch = _alphabet.toChar(index);
            int chIndex = cycle.indexOf(ch);
            int desiredIndex = wrap(chIndex - 1, cycle.length());
            char desiredChar = cycle.charAt(desiredIndex);
            result = _alphabet.toInt(desiredChar);
        }
        return result;
    }

    /** Return the result of applying this permutation to the index of P
     *  in ALPHABET, and converting the result to a character of ALPHABET. */
    char permute(char p) {
        String cycle = getCycle(p);
        char result = p;

        if (!cycle.equals("")) {
            int pIndex = cycle.indexOf(p);
            int desiredIndex = wrap(pIndex + 1, cycle.length());
            result = cycle.charAt(desiredIndex);
        }
        return result;
    }

    /** Return the result of applying the inverse of this permutation to C. */
    char invert(char c) {
        String cycle = getCycle(c);
        char result = c;

        if (!cycle.equals("")) {
            int cIndex = cycle.indexOf(c);
            int desiredIndex = wrap(cIndex - 1, cycle.length());
            result = cycle.charAt(desiredIndex);
        }
        return result;
    }

    /** Return the alphabet used to initialize this Permutation. */
    Alphabet alphabet() {
        return _alphabet;
    }

    /** Return true iff this permutation is a derangement (i.e., a
     *  permutation for which no value maps to itself). */
    boolean derangement() {
        boolean result = true;
        for (int i = 0; i < size(); i += 1) {
            String cycle = getCycle(i);
            if (cycle.equals("") || cycle.length() == 1) {
                result = false;
            }
        }
        return result;
    }

    /** Throws an EnigmaException if CYCLES and ALPHABET form an invalid
     *  permutation, which can be due to an empty cycle (), opening a
     *  parenthesis before closing a prior open parenthesis, using a close
     *  parenthesis without opening a parenthesis beforehand, an asterisk being
     *  in CYCLES, a whitespace character existing inside a cycle, a character
     *  specified in a cycle not being in ALPHABET, a character being outside
     *  of a cycle, or an uneven number of open and close parentheses. */
    private static void checkCycles(String cycles, Alphabet alphabet) {
        checkEmptyCycles(cycles);

        boolean open = false;
        int openParen = 0, closeParen = 0;

        for (int i = 0; i < cycles.length(); i += 1) {
            char c1 = cycles.charAt(i);
            if (!isValidChar(c1)) {
                if (c1 == '(') {
                    if (open) {
                        throw error("A parenthesis was not closed yet.");
                    }
                    open = true;
                    openParen += 1;
                } else if (c1 == ')') {
                    if (!open) {
                        throw error("A parenthesis was not opened yet.");
                    }
                    open = false;
                    closeParen += 1;
                } else if (c1 == '*') {
                    throw error("* is not allowed to be in cycles.");
                } else {
                    if (open) {
                        throw error("No whitespace inside cycles.");
                    }
                }
            } else {
                if (!alphabet.contains(c1)) {
                    throw error(c1 + " in cycles must be in the alphabet.");
                } else if (!open) {
                    throw error(c1 + " must be inside parentheses.");
                }
            }

            for (int j = i + 1; j < cycles.length(); j += 1) {
                char c2 = cycles.charAt(j);
                checkEqualChars(c1, c2);
            }
        }
        if (openParen != closeParen) {
            throw error("Number of open and close parentheses "
                    + "must match in cycles.");
        }
    }

    /** Throws an EnigmaException if CYCLES contains the substring "()". */
    private static void checkEmptyCycles(String cycles) {
        if (cycles.contains("()")) {
            throw error("() is an invalid cycle.");
        }
    }

    /** Throws an EnigmaException if C1 is the same
     *  character as C2 and C2 is a valid character. */
    private static void checkEqualChars(char c1, char c2) {
        if (c1 == c2 && isValidChar(c2)) {
            throw error(c1 + " is repeated in cycles.");
        }
    }

    /** Returns true if C is not a whitespace character,
     *  '*', '(', or ')'. Otherwise false. */
    private static boolean isValidChar(char c) {
        return !isWhitespace(c) && c != '*' && c != '(' && c != ')';
    }

    /** Returns a String array containing the cycles
     *  given by CYCLES. Each String in this array represents
     *  the characters in the corresponding cycle. */
    private static String[] initCycles(String cycles) {
        String[] result;
        int len = 0;
        for (int i = 0; i < cycles.length(); i += 1) {
            char c = cycles.charAt(i);
            if (c == '(') {
                len += 1;
            }
        }
        result = new String[len];

        int currIndex = 0;
        for (int i = 0; i < cycles.length(); i += 1) {
            char c = cycles.charAt(i);
            if (Character.isWhitespace(c)) {
                continue;
            }

            if (c == '(') {
                result[currIndex] = "";
            } else if (c == ')') {
                currIndex += 1;
            } else {
                result[currIndex] += c;
            }
        }
        return result;
    }

    /** Return the cycle that contains the character at index
     *  INDEX of the alphabet, where 0 <= INDEX < size(). */
    public String getCycle(int index) {
        if (index < 0 || index >= size()) {
            throw error(index
                    + " is an invalid index to access in the alphabet.");
        }
        char ch = _alphabet.toChar(index);
        return getCycle(ch);
    }

    /** Return the cycle that contains the character
     *  C, where C is in the permutation's alphabet. */
    public String getCycle(char c) {
        if (!_alphabet.contains(c)) {
            throw error(c + " must be in the permutation's alphabet.");
        }
        String result = "";
        for (int i = 0; i < _cycles.length; i += 1) {
            String cycle = _cycles[i];
            if (cycle.indexOf(c) >= 0) {
                result = cycle;
            }
        }
        return result;
    }

    /** Alphabet of this permutation. */
    private Alphabet _alphabet;

    /** String array of cycles of this permutation. */
    private String[] _cycles;
}
