package DAL;

import DTO.TokenDTO;

/**
 * Created by mathias on 03/05/16.
 */
public interface ITokenDAO {

    int createToken(TokenDTO token);

    TokenDTO[] getTokens();

    TokenDTO getToken(String token);

    boolean isValidToken(String token);

    int updateToken(TokenDTO token);

    int deleteToken(TokenDTO token);
}
