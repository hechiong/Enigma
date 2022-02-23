package enigma;

import java.util.ArrayList;
import java.util.Collection;

import static enigma.EnigmaException.*;

/** Class that represents a complete enigma machine.
 *  @author Henry Chiong
 */
class Machine {

    /** A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     *  and 0 <= PAWLS < NUMROTORS pawls. ALLROTORS contains all the
     *  available rotors. */
    Machine(Alphabet alpha, int numRotors, int pawls,
            Collection<Rotor> allRotors) {
        checkRotorsAndPawls(numRotors, pawls, allRotors);
        _alphabet = alpha;
        _numRotors = numRotors;
        _pawls = pawls;
        _allRotors = allRotors;
    }

    /** Return the number of rotor slots I have. */
    int numRotors() {
        return _numRotors;
    }

    /** Return the number pawls (and thus rotating rotors) I have. */
    int numPawls() {
        return _pawls;
    }

    /** Set my rotor slots to the rotors named ROTORS from my set of
     *  available rotors (ROTORS[0] names the reflector).
     *  Initially, all rotors are set at their 0 settings. */
    void insertRotors(String[] rotors) {
        checkValidRotors(rotors);
        _usedRotors = new ArrayList<>();
        for (String rotorName : rotors) {
            for (Rotor rotor : _allRotors) {
                if (rotorName.equals(rotor.name())) {
                    rotor.set(0);
                    rotor.setRing(0);
                    _usedRotors.add(rotor);
                }
            }
        }
    }

    /** Set my rotors according to SETTING, which must be a string of
     *  numRotors()-1 characters in my alphabet. The first letter refers
     *  to the leftmost rotor setting (not counting the reflector). */
    void setRotors(String setting) {
        checkSetting(setting, false);
        for (int i = 0; i < setting.length(); i += 1) {
            Rotor r = _usedRotors.get(i + 1);
            char c = setting.charAt(i);
            r.set(c);
        }
    }

    /** Set my rotors according to SETTING, which must be a string of
     *  numRotors()-1 characters in my alphabet. The first letter refers
     *  to the leftmost rotor ring setting (not counting the reflector). */
    void setRingRotors(String setting) {
        checkSetting(setting, true);
        for (int i = 0; i < setting.length(); i += 1) {
            Rotor r = _usedRotors.get(i + 1);
            char c = setting.charAt(i);
            r.setRing(c);
        }
    }

    /** Set the plugboard to PLUGBOARD. */
    void setPlugboard(Permutation plugboard) {
        _plugboard = plugboard;
    }

    /** Returns the result of converting the input character C (as an
     *  index in the range 0..alphabet size - 1), after first advancing
     *  the machine. */
    int convert(int c) {
        if (c < 0 || c >= _alphabet.size()) {
            throw error(c + " is an invalid index to access in the alphabet.");
        }
        advanceRotors();

        int result = c;
        if (_plugboard != null) {
            result = _plugboard.permute(result);
        }
        for (int i = _usedRotors.size() - 1; i >= 0; i -= 1) {
            Rotor r = _usedRotors.get(i);
            result = r.convertForward(result);
        }

        for (int i = 1; i < _usedRotors.size(); i += 1) {
            Rotor r = _usedRotors.get(i);
            result = r.convertBackward(result);
        }
        if (_plugboard != null) {
            result = _plugboard.invert(result);
        }

        return result;
    }

    /** Returns the encoding/decoding of MSG, updating the state of
     *  the rotors accordingly. */
    String convert(String msg) {
        checkString(msg);
        String result = "";
        for (int i = 0; i < msg.length(); i += 1) {
            char inputChar = msg.charAt(i);
            int inputInt = _alphabet.toInt(inputChar);
            int outputInt = convert(inputInt);
            char outputChar = _alphabet.toChar(outputInt);
            result += outputChar;
        }
        return result;
    }

    /** Throws an EnigmaException if either of these conditions are not met:
     *  1 < NUMROTORS <= size of ALLROTORS and 0 <= PAWLS < NUMROTORS. */
    private static void checkRotorsAndPawls(int numRotors, int pawls,
                                            Collection<Rotor> allRotors) {
        if (numRotors <= 1 || numRotors > allRotors.size()) {
            throw error("Must use a reasonable number of rotors.");
        }
        if (pawls < 0 || pawls >= numRotors) {
            throw error("Must use a reasonable number of pawls.");
        }
    }

    /** Throws an EnigmaException if ROTORS contains a rotor name that does not
     *  exist in _ALLROTORS, ROTORS contains repeated rotor names, ROTORS does
     *  not contain _NUMROTORS rotors, or the first rotor specified in ROTORS
     *  is not a reflector. */
    private void checkValidRotors(String[] rotors) {
        if (rotors.length != _numRotors) {
            throw error("There must be " + _numRotors + " rotors to insert.");
        }
        int pawls = 0;
        for (int i = 0; i < rotors.length; i += 1) {
            String rotorName1 = rotors[i];
            boolean isValidRotor = false;
            for (Rotor rotor : _allRotors) {
                if (rotorName1.equals(rotor.name())) {
                    if (i == 0 && !rotor.reflecting()) {
                        throw error("First rotor must be a reflector.");
                    } else if (rotor.rotates()) {
                        pawls += 1;
                    }
                    isValidRotor = true;
                }
            }
            if (!isValidRotor) {
                throw error("Only valid rotors may be inserted.");
            }
            for (int j = i + 1; j < rotors.length; j += 1) {
                String rotorName2 = rotors[j];
                if (rotorName1.equals(rotorName2)) {
                    throw error("There must not be repeated rotors.");
                }
            }
        }
        if (pawls != _pawls) {
            throw error("There must be " + _pawls + " rotors to insert.");
        }
    }

    /** Throws an EnigmaException if SETTING's length is not one less than the
     *  number of rotors to be used or if a character in SETTING is not in the
     *  machine's alphabet with a descriptive message depending on ISRING. */
    private void checkSetting(String setting, boolean isRing) {
        String ringMsg = "";
        if (isRing) {
            ringMsg = "Ring ";
        }
        if (setting.length() != _numRotors - 1) {
            throw error(ringMsg + "Setting length must be one less than the"
                    + " number of rotors to be used.");
        }
        checkString(setting);
    }

    /** Throws an EnigmaException if a character
     *  in S is not in the machine's alphabet. */
    private void checkString(String s) {
        for (int i = 0; i < s.length(); i += 1) {
            char c = s.charAt(i);
            if (!_alphabet.contains(c)) {
                throw error(c + " must be in the machine's alphabet.");
            }
        }
    }

    /** Starting from the rightmost rotor in _USEDROTORS, the moving rotors are
     *  advanced properly: always advancing the rightmost rotor and advancing
     *  the current rotor and its neighboring left rotor if the current rotor
     *  is at a notch. */
    private void advanceRotors() {
        boolean wasAtNotch = false;
        for (int i = 1; i <= _pawls; i += 1) {
            Rotor curr = _usedRotors.get(_usedRotors.size() - i);
            if (i == 1) {
                if (curr.atNotch()) {
                    wasAtNotch = true;
                }
                curr.advance();
            } else if (wasAtNotch) {
                wasAtNotch = curr.atNotch();
                curr.advance();
            } else if (curr.atNotch()) {
                if (i != _pawls) {
                    wasAtNotch = true;
                    curr.advance();
                }
            }
        }
    }

    /** Common alphabet of my rotors. */
    private final Alphabet _alphabet;

    /** Number of rotors to be used in this machine. */
    private final int _numRotors;

    /** Number of pawls (or moving rotors) to be used in this machine. */
    private final int _pawls;

    /** Collection of the available rotors to use. */
    private final Collection<Rotor> _allRotors;

    /** Collection of the available rotors to use. */
    private ArrayList<Rotor> _usedRotors;

    /** Plugboard of this machine. */
    private Permutation _plugboard;
}
