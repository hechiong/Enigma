package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;

import static org.junit.Assert.*;

import static enigma.TestUtils.*;

/** The suite of all JUnit tests for the Permutation class.
 *  @author Henry Chiong
 */
public class PermutationTest {

    /** Testing time limit. */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    /* ***** TESTS ***** */

    @Test
    public void testSize() {
        Permutation p0 = new Permutation("", new Alphabet(""));
        assertEquals(0, p0.size());

        Permutation p1 = new Permutation("", new Alphabet());
        assertEquals(26, p1.size());

        Permutation p2 = new Permutation(" (STAR)", new Alphabet("ARGST"));
        assertEquals(5, p2.size());
    }

    @Test
    public void testPermuteInt() {
        Permutation p0 = new Permutation("", new Alphabet("T"));
        assertEquals(0, p0.permute(0));

        Permutation p1 = new Permutation("(T) ", new Alphabet("T"));
        assertEquals(0, p1.permute(0));

        Permutation p2 = new Permutation("(AC) (B)", new Alphabet("ABCD"));
        assertEquals(2, p2.permute(0));
        assertEquals(1, p2.permute(1));
        assertEquals(0, p2.permute(2));
        assertEquals(3, p2.permute(7));
    }

    @Test
    public void testInvertInt() {
        Permutation p0 = new Permutation("", new Alphabet("T"));
        assertEquals(0, p0.invert(0));

        Permutation p1 = new Permutation("(T)", new Alphabet("T"));
        assertEquals(0, p1.invert(0));

        Permutation p2 = new Permutation("(C)(DE)", new Alphabet("BDEC"));
        assertEquals(0, p2.invert(0));
        assertEquals(2, p2.invert(5));
        assertEquals(1, p2.invert(2));
        assertEquals(3, p2.invert(3));
    }

    @Test
    public void testPermuteChar() {
        Permutation p0 = new Permutation("", new Alphabet("T"));
        assertEquals('T', p0.permute('T'));

        Permutation p1 = new Permutation(" (Y) ", new Alphabet("Y"));
        assertEquals('Y', p1.permute('Y'));

        Permutation p2 = new Permutation("(BACD)", new Alphabet("ABCD"));
        assertEquals('C', p2.permute('A'));
        assertEquals('A', p2.permute('B'));
        assertEquals('D', p2.permute('C'));
        assertEquals('B', p2.permute('D'));

        Permutation p3 = new Permutation("(WY) (Z)", new Alphabet("WXYZ"));
        assertEquals('Y', p3.permute('W'));
        assertEquals('W', p3.permute('Y'));
        assertEquals('X', p3.permute('X'));
        assertEquals('Z', p3.permute('Z'));

        Permutation p4 = new Permutation("(K) (M)", new Alphabet("KM"));
        assertEquals('K', p4.permute('K'));
        assertEquals('M', p4.permute('M'));
    }

    @Test
    public void testInvertChar() {
        Permutation p0 = new Permutation("", new Alphabet("T"));
        assertEquals('T', p0.invert('T'));

        Permutation p1 = new Permutation("(P)", new Alphabet("P"));
        assertEquals('P', p1.invert('P'));

        Permutation p2 = new Permutation("(BACD)", new Alphabet("ABCD"));
        assertEquals('D', p2.invert('B'));
        assertEquals('B', p2.invert('A'));
        assertEquals('A', p2.invert('C'));
        assertEquals('C', p2.invert('D'));

        Permutation p3 = new Permutation("(VW) (Z)", new Alphabet("VWXZ"));
        assertEquals('V', p3.invert('W'));
        assertEquals('W', p3.invert('V'));
        assertEquals('X', p3.invert('X'));
        assertEquals('Z', p3.invert('Z'));

        Permutation p4 = new Permutation("", new Alphabet("AKM"));
        assertEquals('A', p4.invert('A'));
        assertEquals('K', p4.invert('K'));
        assertEquals('M', p4.invert('M'));

        Permutation p5 = new Permutation("(IOU)", new Alphabet("IOU"));
        assertEquals('U', p5.invert('I'));
        assertEquals('I', p5.invert('O'));
        assertEquals('O', p5.invert('U'));
    }

    @Test
    public void testAlphabet() {
        Alphabet a = new Alphabet("TES");
        Permutation p = new Permutation("(SET)", a);
        assertEquals(a, p.alphabet());
    }

    @Test
    public void testDerangement() {
        Permutation p0 = new Permutation("", new Alphabet(""));
        assertTrue(p0.derangement());

        Permutation p1 = new Permutation("", new Alphabet());
        assertFalse(p1.derangement());

        Permutation p3 = new Permutation("(PE) (K)", new Alphabet("PEK"));
        assertFalse(p3.derangement());

        Permutation p4 = new Permutation("(JPE)", new Alphabet("JERP"));
        assertFalse(p4.derangement());

        Permutation p5 = new Permutation("(" + UPPER_STRING + ")",
                new Alphabet());
        assertTrue(p5.derangement());
    }

    @Test
    public void testGetCycle() {
        Permutation p = new Permutation("(A!) (B?)", new Alphabet("AB!?5"));
        assertTrue("A!".equals(p.getCycle(0)));
        assertTrue("B?".equals(p.getCycle(1)));

        assertTrue("A!".equals(p.getCycle('!')));
        assertTrue("B?".equals(p.getCycle('?')));

        assertTrue("".equals(p.getCycle('5')));
    }

    @Test(expected = EnigmaException.class)
    public void testNegativeIndexGetCycle() {
        Permutation p = new Permutation("(Z)", new Alphabet());
        p.getCycle(-1);
    }

    @Test(expected = EnigmaException.class)
    public void testBadIndexGetCycle() {
        Permutation p = new Permutation("(A)", new Alphabet());
        p.getCycle(26);
    }

    @Test(expected = EnigmaException.class)
    public void testBadCharGetCycle() {
        Permutation p = new Permutation("", new Alphabet());
        p.getCycle('a');
    }

    @Test(expected = EnigmaException.class)
    public void testEmptyCycle() {
        Permutation p = new Permutation("()", new Alphabet());
    }

    @Test(expected = EnigmaException.class)
    public void testOneOpenParenCycle() {
        Permutation p = new Permutation("(", new Alphabet());
    }

    @Test(expected = EnigmaException.class)
    public void testOneCloseParenCycle() {
        Permutation p = new Permutation(")", new Alphabet());
    }

    @Test(expected = EnigmaException.class)
    public void testDoubleOpenParenCycle() {
        Permutation p = new Permutation("(OP) (E(N)", new Alphabet());
    }

    @Test(expected = EnigmaException.class)
    public void testDoubleCloseParenCycle() {
        Permutation p = new Permutation("(OP)) (EN)", new Alphabet());
    }

    @Test(expected = EnigmaException.class)
    public void testAsteriskCycle() {
        Permutation p = new Permutation("(STAR*)", new Alphabet());
    }

    @Test(expected = EnigmaException.class)
    public void testWhitespaceCycle() {
        Permutation p = new Permutation("(WHITE SPAC)", new Alphabet());
    }

    @Test(expected = EnigmaException.class)
    public void testCycleCharNotInAlphabet() {
        Permutation p = new Permutation("(h)", new Alphabet());
    }

    @Test(expected = EnigmaException.class)
    public void testNoParenCycle() {
        Permutation p = new Permutation("NOPAREN", new Alphabet());
    }

    @Test(expected = EnigmaException.class)
    public void testCharOutsideParenCycle1() {
        Permutation p = new Permutation("I(N)", new Alphabet());
    }

    @Test(expected = EnigmaException.class)
    public void testCharOutsideParenCycle2() {
        Permutation p = new Permutation("(OU)T", new Alphabet());
    }

    @Test(expected = EnigmaException.class)
    public void testRepeatedCharsCycle1() {
        Permutation p = new Permutation("(BAB)", new Alphabet());
    }

    @Test(expected = EnigmaException.class)
    public void testRepeatedCharsCycle2() {
        Permutation p = new Permutation("(?^) (?{})", new Alphabet());
    }

    @Test(expected = EnigmaException.class)
    public void testUnevenParenCycle() {
        Permutation p = new Permutation("(Z)(", new Alphabet());
    }
}
