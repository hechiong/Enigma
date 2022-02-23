package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import static enigma.EnigmaException.*;
import static org.junit.Assert.*;

/** The suite of all JUnit tests for the Main class.
 *  @author Henry Chiong
 */
public class MainTest {

    /** Testing time limit. */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    /* ***** TESTING UTILITIES ***** */
    private String defaultConfPath = "testing/correct/default.conf";
    private String testConfPath = "testing/correct/test.conf";
    private String riptideEncInPath = "testing/correct/riptideEnc.in";
    private String riptideEncOutPath = "testing/correct/riptideEnc.out";
    private String riptideDecInPath = "testing/correct/riptideDec.in";
    private String riptideDecOutPath = "testing/correct/riptideDec.out";
    private String trivial1InPath = "testing/correct/trivial1.in";
    private String trivial1OutPath = "testing/correct/trivial1.out";
    private String settingOnlyInPath = "testing/correct/settingOnly.in";
    private String settingOnlyOutPath = "testing/correct/settingOnly.out";
    private String testInPath = "testing/correct/test.in";
    private String testOutPath = "testing/correct/test.out";
    private String[] riptideEncoding = {"HGJNB OKDWA LBFKU CMUTJ ZUIO",
                                        "XTYQF BDZRG BYFZC ASYRU ",
                                        "UAAFW OAGFK OCJGM UMOPC HTAVR SA",
                                        "HXHFR UXOFC BLRYS DXFCZ XGVFA NA",
                                        "CNBZH SNQMC MNIRW MTTTQ BRNKR XDRPN ",
                                        "AJIRV IFOVC TKGNU CKUMB ITFEN V"};
    private String[] riptideDecoding = {"IWASS CARED OFCOD INGIN JAVA",
                                        "IWASS CARED OFUSI NGGIT ",
                                        "ANDST ARTIN GALLT HESEP ROJEC TS",
                                        "COMPI LERKE EPSGE TTING MADAT ME",
                                        "NOWMY PROJE CTONL YRUNS INMYD REAMS ",
                                        "OHOHA LLTHE SEMER GECON FLICT S"};
    private String[] trivial1 = {"IHBDQ QMTQZ ",
                                 "HELLO WORLD "};
    private String[] test = {"0P!8A 8M,V5 4CHA7 ", "", "",
                             "CLANG UAGE4 LIFE! "};

    /* ***** TESTS ***** */

    @Test
    public void checkRiptideEnc() {
        String[] riptideArgs = {defaultConfPath, riptideEncInPath,
                                riptideEncOutPath};
        Main.main(riptideArgs);

        try {
            Scanner riptideScanner = new Scanner(new File(riptideEncOutPath));
            int index = 0;
            while (riptideScanner.hasNextLine()) {
                String outputLine = riptideScanner.nextLine();
                assertTrue(outputLine.equals(riptideEncoding[index]));
                index += 1;
            }
        } catch (IOException excp) {
            throw error("could not open %s", riptideEncOutPath);
        }
    }

    @Test
    public void checkRiptideDec() {
        String[] riptideArgs = {defaultConfPath, riptideDecInPath,
                                riptideDecOutPath};
        Main.main(riptideArgs);

        try {
            Scanner riptideScanner = new Scanner(new File(riptideDecOutPath));
            int index = 0;
            while (riptideScanner.hasNextLine()) {
                String outputLine = riptideScanner.nextLine();
                assertTrue(outputLine.equals(riptideDecoding[index]));
                index += 1;
            }
        } catch (IOException excp) {
            throw error("could not open %s", riptideDecOutPath);
        }
    }

    @Test
    public void checkTrivial1() {
        String[] trivial1Args = {defaultConfPath, trivial1InPath,
                                 trivial1OutPath};
        Main.main(trivial1Args);

        try {
            Scanner trivial1Scanner = new Scanner(new File(trivial1OutPath));
            int index = 0;
            while (trivial1Scanner.hasNextLine()) {
                String outputLine = trivial1Scanner.nextLine();
                assertTrue(outputLine.equals(trivial1[index]));
                index += 1;
            }
        } catch (IOException excp) {
            throw error("could not open %s", trivial1OutPath);
        }
    }

    @Test
    public void checkSettingOnly() {
        String[] settingOnlyArgs = {defaultConfPath, settingOnlyInPath,
                                    settingOnlyOutPath};
        Main.main(settingOnlyArgs);

        try {
            Scanner settingOnlyScanner = new Scanner(
                    new File(settingOnlyOutPath));
            assertFalse(settingOnlyScanner.hasNextLine());
        } catch (IOException excp) {
            throw error("could not open %s", settingOnlyOutPath);
        }
    }

    @Test
    public void checkTest() {
        String[] testArgs = {testConfPath, testInPath,
                             testOutPath};
        Main.main(testArgs);

        try {
            Scanner testScanner = new Scanner(new File(testOutPath));
            int index = 0;
            while (testScanner.hasNextLine()) {
                String outputLine = testScanner.nextLine();
                assertTrue(outputLine.equals(test[index]));
                index += 1;
            }
        } catch (IOException excp) {
            throw error("could not open %s", testOutPath);
        }
    }
}
