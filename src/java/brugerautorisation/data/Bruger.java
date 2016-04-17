package brugerautorisation.data;

import java.io.*;
import java.util.HashMap;

public class Bruger implements Serializable {
    // Vigtigt: Sæt versionsnummer så objekt kan læses selvom klassen er ændret!

    private static final long serialVersionUID = 12345;

    public String brugernavn;                   // Studienummer
    public String email = "hvad@ved.jeg.dk";
    public long   sidstAktiv;
    public String campusnetId;                  // Campusnet database-ID
    public String studeretning = "ukendt";
    public String fornavn = "test";
    public String efternavn = "testesen";
    public String adgangskode;
    public HashMap<String, Object> ekstraFelter = new HashMap<>();

    public String toString() {
        return email;
    }
}
