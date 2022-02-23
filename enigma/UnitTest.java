package enigma;

import ucb.junit.textui;

/** The suite of all JUnit tests for the enigma package.
 *  @author Henry Chiong
 */
public class UnitTest {

    /** Run the JUnit tests in this package. Add xxxTest.class entries to
     *  the arguments of runClasses to run other JUnit tests. */
    public static void main(String[] ignored) {
        System.exit(textui.runClasses(AlphabetTest.class, FixedRotorTest.class,
                MachineTest.class, MainTest.class, MovingRotorTest.class,
                PermutationTest.class, ReflectorTest.class, RotorTest.class));
    }
}


