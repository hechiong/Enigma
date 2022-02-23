package enigma;

import static enigma.EnigmaException.*;

/** Class that represents a reflector in the enigma.
 *  @author Henry Chiong
 */
class Reflector extends FixedRotor {

    /** A non-moving rotor named NAME whose permutation at the 0 settings
     * is PERM. */
    Reflector(String name, Permutation perm) {
        super(name, perm);
        checkPermutation(perm);
    }

    @Override
    boolean reflecting() {
        return true;
    }

    @Override
    public String toString() {
        return "Reflector " + name();
    }

    /** Throw an EnigmaException if PERM is not a derangement. */
    private static void checkPermutation(Permutation perm) {
        if (!perm.derangement()) {
            throw error("Reflector's permutation must be a derangement.");
        }
    }
}
