package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;

import static org.junit.Assert.*;

/** The suite of all JUnit tests for the Alphabet class.
 *  @author Henry Chiong
 */
public class AlphabetTest {

    /** Testing time limit. */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    /* Test valid alphabets */
    Alphabet emptyAlphabet = new Alphabet("");
    Alphabet upperCaseAlphabet = new Alphabet();
    Alphabet a1 = new Alphabet("!");
    Alphabet a2 = new Alphabet("3BbcD/");

    /* ***** TESTS ***** */

    @Test
    public void testSize() {
        assertEquals(0, emptyAlphabet.size());
        assertEquals(26, upperCaseAlphabet.size());
        assertEquals(1, a1.size());
        assertEquals(6, a2.size());
    }

    @Test
    public void testContains() {
        assertTrue(upperCaseAlphabet.contains('K'));
        assertTrue(a1.contains('!'));
        assertTrue(a2.contains('3'));

        assertFalse(upperCaseAlphabet.contains('a'));
        assertFalse(a1.contains('?'));
        assertFalse(a2.contains('\\'));
    }

    @Test
    public void testToChar() {
        assertEquals('Z', upperCaseAlphabet.toChar(25));
        assertEquals('!', a1.toChar(0));
        assertEquals('b', a2.toChar(2));
    }

    @Test
    public void testToInt() {
        assertEquals(6, upperCaseAlphabet.toInt('G'));
        assertEquals(0, a1.toInt('!'));
        assertEquals(3, a2.toInt('c'));
    }

    @Test(expected = EnigmaException.class)
    public void testInvalidIndex1() {
        a1.toChar(-1);
    }

    @Test(expected = EnigmaException.class)
    public void testInvalidIndex2() {
        a1.toChar(a1.size());
    }

    @Test(expected = EnigmaException.class)
    public void testInvalidChar1() {
        upperCaseAlphabet.toInt('a');
    }

    @Test(expected = EnigmaException.class)
    public void testInvalidChar2() {
        a2.toInt('C');
    }

    @Test(expected = EnigmaException.class)
    public void testWhitespaceAlphabet() {
        new Alphabet("Black space");
    }

    @Test(expected = EnigmaException.class)
    public void testAsteriskAlphabet() {
        new Alphabet("3*5=15");
    }

    @Test(expected = EnigmaException.class)
    public void testOpenParenAlphabet() {
        new Alphabet("(_|ovo]");
    }

    @Test(expected = EnigmaException.class)
    public void testClosedParenAlphabet() {
        new Alphabet("_|ovo])");
    }

    @Test(expected = EnigmaException.class)
    public void testRepeatedCharsAlphabet() {
        new Alphabet("QqQ");
    }
}
