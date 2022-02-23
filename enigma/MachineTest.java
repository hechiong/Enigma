package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;

import static org.junit.Assert.*;

import java.util.ArrayList;

import static enigma.TestUtils.*;

/** The suite of all JUnit tests for the Machine class.
 *  @author Henry Chiong
 */
public class MachineTest {

    /** Testing time limit. */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    private Permutation plugboard = new Permutation("(YF) (ZH)", UPPER);
    private Permutation pI = new Permutation(NAVALA.get("I"), UPPER);
    private Rotor i = new MovingRotor("I", pI, "Q");
    private Permutation pIV = new Permutation(NAVALA.get("IV"), UPPER);
    private Rotor iv = new MovingRotor("IV", pIV, "J");
    private Permutation pIII = new Permutation(NAVALA.get("III"), UPPER);
    private Rotor iii = new MovingRotor("III", pIII, "V");
    private Permutation pBeta = new Permutation(NAVALA.get("Beta"), UPPER);
    private Rotor beta = new FixedRotor("Beta", pBeta);
    private Permutation pB = new Permutation(NAVALA.get("B"), UPPER);
    private Rotor b = new Reflector("B", pB);
    private Permutation pII = new Permutation(NAVALA.get("II"), UPPER);
    private Rotor ii = new MovingRotor("II", pII, "A");
    private ArrayList<Rotor> rotorsArrList = new ArrayList<>();
    private Machine m;

    /* ***** TESTING UTILITIES ***** */

    /** Initializes ROTORSARRLIST with the above rotors. */
    private void initializeRotorsArrList() {
        rotorsArrList.add(b);
        rotorsArrList.add(beta);
        rotorsArrList.add(iii);
        rotorsArrList.add(iv);
        rotorsArrList.add(i);
        rotorsArrList.add(ii);
    }

    /** Initializes M so that the methods of Machine can be tested. Also
     *  tests whether the insertRotors, setRotors, setRingRotors, and
     *  setPlugboard methods work as intended when tested. */
    private void initializeMachine() {
        initializeRotorsArrList();
        String[] rotorNames = new String[] {"B", "Beta", "III", "IV", "I"};
        m = new Machine(UPPER, 5, 3, rotorsArrList);
        m.insertRotors(rotorNames);
        m.setRotors("AXLE");
        m.setRingRotors("AAAA");
        m.setPlugboard(plugboard);
    }

    /* ***** TESTS ***** */

    @Test
    public void testNumRotors() {
        initializeMachine();
        assertEquals(5, m.numRotors());
    }

    @Test
    public void testNumPawls() {
        initializeMachine();
        assertEquals(3, m.numPawls());
    }

    @Test
    public void testConvertChar() {
        initializeMachine();
        assertEquals(UPPER.toInt('Z'), m.convert(UPPER.toInt('Y')));
    }

    @Test
    public void testConvertMsg() {
        initializeMachine();
        m.setPlugboard(new Permutation("(HQ) (EX) (IP) (TR) (BY)", UPPER));
        assertTrue("QVPQS".equals(m.convert("FROMH")));
    }

    @Test(expected = EnigmaException.class)
    public void checkInvalidNumRotors1() {
        initializeRotorsArrList();
        m = new Machine(UPPER, 1, 3, rotorsArrList);
    }

    @Test(expected = EnigmaException.class)
    public void checkInvalidNumRotors2() {
        initializeRotorsArrList();
        m = new Machine(UPPER, 7, 3, rotorsArrList);
    }

    @Test(expected = EnigmaException.class)
    public void checkNegativePawls() {
        initializeRotorsArrList();
        m = new Machine(UPPER, 5, -1, rotorsArrList);
    }

    @Test(expected = EnigmaException.class)
    public void checkInvalidPawls() {
        initializeRotorsArrList();
        m = new Machine(UPPER, 5, 5, rotorsArrList);
    }

    @Test(expected = EnigmaException.class)
    public void checkInvalidRotorArray() {
        String[] rotorNames = new String[] {"I", "III", "IV", "Beta", "B"};
        initializeMachine();
        m.insertRotors(rotorNames);
    }

    @Test(expected = EnigmaException.class)
    public void checkInvalidRotorName() {
        String[] rotorNames = new String[] {"B", "Beta", "III", "IV", "i"};
        initializeMachine();
        m.insertRotors(rotorNames);
    }

    @Test(expected = EnigmaException.class)
    public void checkRepeatedRotors() {
        String[] rotorNames = new String[] {"B", "Beta", "I", "IV", "I"};
        initializeMachine();
        m.insertRotors(rotorNames);
    }

    @Test(expected = EnigmaException.class)
    public void checkAmountOfRotors1() {
        String[] rotorNames = new String[] {"B", "Beta", "III", "IV"};
        initializeMachine();
        m.insertRotors(rotorNames);
    }

    @Test(expected = EnigmaException.class)
    public void checkAmountOfRotors2() {
        String[] rotNames = new String[] {"B", "Beta", "III", "IV", "I", "II"};
        initializeMachine();
        m.insertRotors(rotNames);
    }

    @Test(expected = EnigmaException.class)
    public void checkAmountOfPawls() {
        String[] rotorNames = new String[] {"B", "II", "III", "IV", "I"};
        initializeMachine();
        m.insertRotors(rotorNames);
    }

    @Test(expected = EnigmaException.class)
    public void testInvalidSetting1() {
        initializeMachine();
        m.setRotors("AXL");
    }

    @Test(expected = EnigmaException.class)
    public void testInvalidSetting2() {
        initializeMachine();
        m.setRotors("axle");
    }

    @Test(expected = EnigmaException.class)
    public void testInvalidRingSetting1() {
        initializeMachine();
        m.setRotors("AAA");
    }

    @Test(expected = EnigmaException.class)
    public void testInvalidRingSetting2() {
        initializeMachine();
        m.setRotors("aaaa");
    }

    @Test(expected = EnigmaException.class)
    public void testInvalidConvertChar1() {
        initializeMachine();
        m.convert(-1);
    }

    @Test(expected = EnigmaException.class)
    public void testInvalidConvertChar2() {
        initializeMachine();
        m.convert(26);
    }

    @Test(expected = EnigmaException.class)
    public void testInvalidConvertMsg() {
        initializeMachine();
        m.convert("msg");
    }
}
