package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;

import static org.junit.Assert.*;

import java.util.HashMap;

import static enigma.TestUtils.*;

/** The suite of all JUnit tests for the MovingRotor class.
 *  @author Henry Chiong
 */
public class FixedRotorTest {

    /** Testing time limit. */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    /* ***** TESTING UTILITIES ***** */

    private Rotor rotor;
    private String alpha = UPPER_STRING;

    /** Check that rotor has an alphabet whose size is that of
     *  FROMALPHA and TOALPHA and that maps each character of
     *  FROMALPHA to the corresponding character of FROMALPHA, and
     *  vice-versa. TESTID is used in error messages. */
    private void checkRotor(String testId,
                            String fromAlpha, String toAlpha) {
        int N = fromAlpha.length();
        assertEquals(testId + " (wrong length)", N, rotor.size());
        for (int i = 0; i < N; i += 1) {
            char c = fromAlpha.charAt(i), e = toAlpha.charAt(i);
            int ci = alpha.indexOf(c), ei = alpha.indexOf(e);
            assertEquals(msg(testId, "wrong translation of %d (%c)", ci, c),
                    ei, rotor.convertForward(ci));
            assertEquals(msg(testId, "wrong inverse of %d (%c)", ei, e),
                    ci, rotor.convertBackward(ei));
        }
    }

    /** Set the rotor to the one with given NAME and permutation as
     *  specified by the NAME entry in ROTORS, with given NOTCHES. */
    private void setRotor(String name, HashMap<String, String> rotors) {
        rotor = new FixedRotor(name, new Permutation(rotors.get(name), UPPER));
    }

    /* ***** TESTS ***** */

    @Test
    public void checkRotorAtB() {
        setRotor("Beta", NAVALB);
        checkRotor("Rotor Beta (B)", UPPER_STRING, NAVALB_MAP.get("Beta"));
    }

    @Test
    public void checkRotorAdvance() {
        setRotor("Gamma", NAVALA);
        rotor.advance();
        checkRotor("Rotor Gamma set", UPPER_STRING, NAVALA_MAP.get("Gamma"));
    }

    @Test
    public void checkRotorSetInt() {
        setRotor("Beta", NAVALA);
        rotor.set(0);
        checkRotor("Rotor Beta set", UPPER_STRING, NAVALA_MAP.get("Beta"));
    }

    @Test
    public void checkRotorSetChar() {
        setRotor("Beta", NAVALB);
        rotor.set('A');
        checkRotor("Rotor Beta set", UPPER_STRING, NAVALB_MAP.get("Beta"));
    }
}
