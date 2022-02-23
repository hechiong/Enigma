package enigma;

/** Class that represents a rotor that has no ratchet and does not advance.
 *  @author Henry Chiong
 */
class FixedRotor extends Rotor {

    /** A non-moving rotor named NAME whose permutation at the 0 settings
     * is given by PERM. */
    FixedRotor(String name, Permutation perm) {
        super(name, perm);
    }

    @Override
    public String toString() {
        return "Fixed " + super.toString();
    }
}
