package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;

import static org.junit.Assert.*;

import java.util.HashMap;

import static enigma.TestUtils.*;

/** The suite of all JUnit tests for the Reflector class.
 *  @author Henry Chiong
 */
public class ReflectorTest {

    /** Testing time limit. */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    /* ***** TESTING UTILITIES ***** */

    private Rotor rotor;

    /** Set the rotor to the one with given NAME and permutation as
     *  specified by the NAME entry in ROTORS, with given NOTCHES. */
    private void setRotor(String name, HashMap<String, String> rotors) {
        rotor = new Reflector(name, new Permutation(rotors.get(name), UPPER));
    }

    /* ***** TESTS ***** */

    @Test
    public void checkReflecting() {
        setRotor("B", NAVALA);
        assertTrue(rotor.reflecting());
    }

    @Test(expected = EnigmaException.class)
    public void checkInvalidReflector() {
        Permutation p = new Permutation("", UPPER);
        Reflector r = new Reflector("No Derangement", p);
    }
}
