package brugerautorisation.transport.soap;

import brugerautorisation.data.Bruger;
import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public interface Brugeradmin {

    /**
     * Henter alle en brugers data
     *
     * @param brugernavn
     * @param adgangskode
     * @return et Bruger-objekt med alle data
     */
    @WebMethod
    Bruger hentBruger(String brugernavn, String adgangskode);

}
