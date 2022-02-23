package enigma;

import static enigma.EnigmaException.*;

/** Superclass that represents a rotor in the enigma machine.
 *  @author Henry Chiong
 */
class Rotor {

    /** A rotor in default settings named NAME
     *  whose permutation is given by PERM. */
    Rotor(String name, Permutation perm) {
        _name = name;
        _permutation = perm;
        _setting = 0;
        _ring = 0;
    }

    /** Return my name. */
    String name() {
        return _name;
    }

    /** Return my alphabet. */
    Alphabet alphabet() {
        return _permutation.alphabet();
    }

    /** Return my permutation. */
    Permutation permutation() {
        return _permutation;
    }

    /** Return the size of my alphabet. */
    int size() {
        return _permutation.size();
    }

    /** Return true iff I have a ratchet and can move. */
    boolean rotates() {
        return false;
    }

    /** Return true iff I reflect. */
    boolean reflecting() {
        return false;
    }

    /** Return my current setting. */
    int setting() {
        return _setting;
    }

    /** Return my current ring setting. */
    int ring() {
        return _ring;
    }

    /** Set setting() to POSN.  */
    void set(int posn) {
        if (posn < 0 || posn >= size()) {
            throw error(posn + " is an invalid position to set for " + this);
        }
        _setting = posn;
    }

    /** Set setting() to character CPOSN. */
    void set(char cposn) {
        _setting = alphabet().toInt(cposn);
    }

    /** Set ring() to POSN.  */
    void setRing(int posn) {
        if (posn < 0 || posn >= size()) {
            throw error(posn + " is an invalid position to set for the ring of"
                    + " " + this);
        }
        _ring = posn;
    }

    /** Set ring() to character CPOSN. */
    void setRing(char cposn) {
        _ring = alphabet().toInt(cposn);
    }

    /** Return the conversion of P (an integer in the range 0..size()-1)
     *  according to my permutation. */
    int convertForward(int p) {
        Permutation perm = _permutation;
        if (p < 0 || p >= size()) {
            throw error(p + " is an invalid input to convert forward.");
        }
        int newSetting = setting() - ring();
        return perm.wrap(perm.permute(p + newSetting) - newSetting, size());
    }

    /** Return the conversion of E (an integer in the range 0..size()-1)
     *  according to the inverse of my permutation. */
    int convertBackward(int e) {
        Permutation perm = _permutation;
        if (e < 0 || e >= size()) {
            throw error(e + " is an invalid input to convert backward.");
        }
        int newSetting = setting() - ring();
        return perm.wrap(perm.invert(e + newSetting) - newSetting, size());
    }

    /** Returns true iff I am positioned to allow the rotor to my left
     *  to advance. */
    boolean atNotch() {
        return false;
    }

    /** Advance me one position, if possible. By default, does nothing. */
    void advance() {
    }

    @Override
    public String toString() {
        return "Rotor " + _name;
    }

    /** My name. */
    private final String _name;

    /** The permutation implemented by this rotor in its 0 position. */
    private Permutation _permutation;

    /** The setting of this rotor. */
    private int _setting;

    /** The ring setting of this rotor. */
    private int _ring;
}
