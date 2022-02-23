package enigma;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import java.util.ArrayList;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static enigma.EnigmaException.*;

/** Enigma simulator.
 *  @author Henry Chiong
 */
public final class Main {

    /** Process a sequence of encryptions and decryptions, as
     *  specified by ARGS, where 1 <= ARGS.length <= 3.
     *  ARGS[0] is the name of a configuration file.
     *  ARGS[1] is optional; when present, it names an input file
     *  containing messages.  Otherwise, input comes from the standard
     *  input.  ARGS[2] is optional; when present, it names an output
     *  file for processed messages.  Otherwise, output goes to the
     *  standard output. Exits normally if there are no errors in the input;
     *  otherwise with code 1. */
    public static void main(String... args) {
        try {
            new Main(args).process();
            return;
        } catch (EnigmaException excp) {
            System.err.printf("Error: %s%n", excp.getMessage());
        }
        System.exit(1);
    }

    /** Check ARGS and open the necessary files (see comment on main). */
    Main(String[] args) {
        if (args.length < 1 || args.length > 3) {
            throw error("Only 1, 2, or 3 command-line arguments allowed");
        }

        _config = getInput(args[0]);

        if (args.length > 1) {
            _input = getInput(args[1]);
        } else {
            _input = new Scanner(System.in);
        }

        if (args.length > 2) {
            _output = getOutput(args[2]);
        } else {
            _output = System.out;
        }
    }

    /** Return a Scanner reading from the file named NAME. */
    private Scanner getInput(String name) {
        try {
            return new Scanner(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Return a PrintStream writing to the file named NAME. */
    private PrintStream getOutput(String name) {
        try {
            return new PrintStream(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Configure an Enigma machine from the contents of configuration
     *  file _config and apply it to the messages in _input, sending the
     *  results to _output. */
    private void process() {
        Machine m = readConfig();
        String settings = "";
        while (_input.hasNextLine()) {
            while (settings.isBlank()) {
                settings = _input.nextLine();
            }
            setUp(m, settings);
            if (_input.hasNextLine()) {
                String line = _input.nextLine();
                while (line.indexOf('*') < 0) {
                    Scanner lineInput = new Scanner(line);
                    String convertedLine = "";
                    while (lineInput.hasNext()) {
                        String msg = lineInput.next();
                        String convertedMsg = m.convert(msg);
                        convertedLine += convertedMsg;
                    }
                    printMessageLine(convertedLine);
                    _output.println();
                    if (_input.hasNextLine()) {
                        line = _input.nextLine();
                    } else {
                        line = "*";
                    }
                }
                settings = line;
            }
        }
    }

    /** Return an Enigma machine configured from the contents of configuration
     *  file _config. */
    private Machine readConfig() {
        try {
            _alphabet = new Alphabet(_config.next());
            int numRotors = _config.nextInt();
            int pawls = _config.nextInt();
            Collection<Rotor> allRotors = new ArrayList<>();
            _token = _config.next();
            while (!_token.isEmpty()) {
                allRotors.add(readRotor());
            }
            return new Machine(_alphabet, numRotors, pawls, allRotors);
        } catch (NoSuchElementException excp) {
            throw error("Configuration file truncated.");
        }
    }

    /** Return a rotor, reading its description from _config. */
    private Rotor readRotor() {
        try {
            checkRotorString(_token, "name");
            String name = _token;
            String rotorDetails = _config.next();
            checkRotorString(rotorDetails, "details");
            String cycles = "";
            String cycle = _config.next();
            while (cycle.startsWith("(")) {
                cycles += cycle;
                if (_config.hasNext()) {
                    cycle = _config.next();
                } else {
                    cycle = "";
                }
            }
            _token = cycle;
            Permutation p = new Permutation(cycles, _alphabet);
            if (rotorDetails.startsWith("M")) {
                String notches = rotorDetails.substring(1);
                return new MovingRotor(name, p, notches);
            } else if (rotorDetails.startsWith("N")) {
                return new FixedRotor(name, p);
            } else if (rotorDetails.startsWith("R")) {
                return new Reflector(name, p);
            } else {
                throw error("Rotor must be described with 'M', 'N', or 'R'.");
            }
        } catch (NoSuchElementException excp) {
            throw error("Bad rotor description.");
        }
    }

    /** Set M according to the specification given on SETTINGS,
     *  which must have the format specified in the assignment. */
    private void setUp(Machine M, String settings) {
        Scanner strInput = new Scanner(settings);
        String firstCol = strInput.next();
        checkSettings(firstCol);
        String[] rotorNames = new String[M.numRotors()];
        for (int i = 0; i < M.numRotors(); i += 1) {
            rotorNames[i] = strInput.next();
        }
        M.insertRotors(rotorNames);
        String rotorSetting = strInput.next();
        checkRotorString(rotorSetting, "setting");
        M.setRotors(rotorSetting);
        String plugboardCycles = "";
        if (strInput.hasNext()) {
            _token = strInput.next();
            if (!_token.startsWith("(")) {
                M.setRingRotors(_token);
            } else {
                plugboardCycles += _token;
            }
            while (strInput.hasNext()) {
                String plugboardCycle = strInput.next();
                plugboardCycles += plugboardCycle;
            }
        }
        M.setPlugboard(new Permutation(plugboardCycles, _alphabet));
    }

    /** Print MSG in groups of five (except that the last group may
     *  have fewer letters). */
    private void printMessageLine(String msg) {
        for (int i = 0; i < msg.length(); i += 1) {
            _output.print(msg.charAt(i));
            if (i % 5 == 4) {
                _output.print(' ');
            }
        }
    }

    /** Throws an EnigmaException if S, which describes
     *  some rotor DESCRIPTOR, starts with '('. */
    private void checkRotorString(String s, String descriptor) {
        if (s.startsWith("(")) {
            throw error("Rotor " + descriptor + " must not start with '('.");
        }
    }

    /** Throws an EnigmaException if SETTINGS does not start with '*'. */
    private void checkSettings(String settings) {
        if (!settings.equals("*")) {
            throw error(settings + " must contain only an asterisk.");
        }
    }

    /** Alphabet used in this machine. */
    private Alphabet _alphabet;

    /** Source of input messages. */
    private Scanner _input;

    /** Source of machine configuration. */
    private Scanner _config;

    /** File for encoded/decoded messages. */
    private PrintStream _output;

    /** Stores token yet to be used. */
    private String _token;
}
