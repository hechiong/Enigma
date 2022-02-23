package enigma;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import static org.junit.Assert.*;

/** The suite of all JUnit tests for the Rotor class.
 *  @author Henry Chiong
 */
public class RotorTest {

    /** Testing time limit. */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    private String name = "Rotor Name";
    private Permutation p = new Permutation("(DASY)", new Alphabet("ASDY"));
    private Rotor r = new Rotor(name, p);

    /* ***** TESTS ***** */

    @Test
    public void testName() {
        assertTrue("Rotor Name".equals(r.name()));
    }

    @Test
    public void testAlphabet() {
        assertEquals(p.alphabet(), r.alphabet());
    }

    @Test
    public void testPermutation() {
        assertEquals(p, r.permutation());
    }

    @Test
    public void testSize() {
        assertEquals(4, r.size());
    }

    @Test
    public void testRotates() {
        assertFalse(r.rotates());
    }

    @Test
    public void testReflecting() {
        assertFalse(r.reflecting());
    }

    @Test
    public void testSetting() {
        assertEquals(0, r.setting());

        r.set(3);
        assertEquals(3, r.setting());

        r.set('A');
        assertEquals(0, r.setting());
    }

    @Test
    public void testRing() {
        assertEquals(0, r.ring());

        r.setRing(2);
        assertEquals(2, r.ring());

        r.setRing('S');
        assertEquals(1, r.ring());
    }

    @Test
    public void testConvertForward() {
        assertEquals(3, r.convertForward(1));
    }

    @Test
    public void testConvertBackward() {
        assertEquals(3, r.convertBackward(2));
    }

    @Test
    public void testAtNotch() {
        assertFalse(r.atNotch());
    }

    @Test
    public void testToString() {
        assertTrue("Rotor Rotor Name".equals(r.toString()));
    }

    @Test(expected = EnigmaException.class)
    public void testSetNegativeIntPosn() {
        r.set(-1);
    }

    @Test(expected = EnigmaException.class)
    public void testSetBadIntPosn() {
        r.set(r.size());
    }

    @Test(expected = EnigmaException.class)
    public void testSetBadCharPosn() {
        r.set('B');
    }

    @Test(expected = EnigmaException.class)
    public void testSetRingNegativeIntPosn() {
        r.setRing(-1);
    }

    @Test(expected = EnigmaException.class)
    public void testSetRingBadIntPosn() {
        r.setRing(r.size());
    }

    @Test(expected = EnigmaException.class)
    public void testSetRingBadCharPosn() {
        r.setRing('B');
    }
}
