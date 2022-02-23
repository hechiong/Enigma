package enigma;

import static enigma.EnigmaException.*;

/** Class that represents a rotating rotor in the enigma machine.
 *  @author Henry Chiong
 */
class MovingRotor extends Rotor {

    /** A rotor named NAME whose permutation in its default setting is
     *  PERM, and whose notches are at the positions indicated in NOTCHES.
     *  The Rotor is initially in its 0 settings (first character of its
     *  alphabet).
     */
    MovingRotor(String name, Permutation perm, String notches) {
        super(name, perm);
        checkNotches(notches);
        _notches = notches;
    }

    @Override
    boolean rotates() {
        return true;
    }

    @Override
    boolean atNotch() {
        boolean result = false;
        for (int i = 0; i < _notches.length(); i += 1) {
            char notch = _notches.charAt(i);
            if (setting() == alphabet().toInt(notch)) {
                result = true;
            }
        }
        return result;
    }

    @Override
    void advance() {
        int nextPosn = (setting() + 1) % size();
        set(nextPosn);
    }

    /** Throws an EnigmaException if a character in
     *  NOTCHES is not in the rotor's alphabet. */
    private void checkNotches(String notches) {
        for (int i = 0; i < notches.length(); i += 1) {
            char notch = notches.charAt(i);
            if (!alphabet().contains(notch)) {
                throw error(notch + " must be in the permutation's alphabet.");
            }
        }
    }

    /** The notches of this moving rotor. */
    private String _notches;
}
