package DAL;

import DTO.TokenDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by mathias on 03/05/16.
 */
public class TokenDAO implements ITokenDAO {

    private final Connection CONN;
    private final String DATABASE_NAME = "Token";

    public TokenDAO(Connection conn){
        this.CONN = conn;
    }

    @Override
    public int createToken(TokenDTO token) {
        if ( token.getToken() == null ||token.getToken().equals(""))
            return -1;

        String sql = "INSERT INTO " + DATABASE_NAME + "(token, creationTime, expirationTime) VALUES(?, ?, ?)";
        long creationTime = 0;
        long expirationTime = 0;

        if(token.getCreationTime() == 0) {
            creationTime = Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTimeInMillis();
        } else{
            creationTime = token.getCreationTime();
        }

        if(token.getExpirationTime() == 0 ||token.getCreationTime() == 0){
            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

            cal.setTimeInMillis(creationTime);

            cal.add(Calendar.DAY_OF_YEAR, 1);

            expirationTime = cal.getTimeInMillis();
        } else{
            expirationTime = token.getExpirationTime();
        }



        PreparedStatement stm = null;
        try{
            stm = CONN.prepareStatement(sql);

            int param = 1;
            stm.setString(param++, token.getToken());
            stm.setLong(param++, creationTime);
            stm.setLong(param++, expirationTime);

            stm.execute();

            if(stm.getUpdateCount() == 1)
                return 1;
            else
                return -2;

        } catch(SQLException e){
            e.printStackTrace();
        } finally {
            try { stm.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return -3;
    }

    @Override
    public TokenDTO[] getTokens() {
        return new TokenDTO[0];
    }

    @Override
    public TokenDTO getToken(String token) {
        return null;
    }

    @Override
    public boolean isValidToken(String token) {
        String sql = "SELECT * FROM " + DATABASE_NAME + " WHERE token = ? AND expirationTime > ?";

        PreparedStatement stm = null;
        ResultSet result = null;
        try{
            stm = CONN.prepareStatement(sql);

            stm.setString(1, token);
            stm.setLong(2, Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTimeInMillis());
            result = stm.executeQuery();

            if(result.next()) {
                return true;
            }else
                return false;

        } catch(SQLException e){
            e.printStackTrace();
        } finally {
            if(stm != null)
            try { stm.close(); } catch (SQLException e) { e.printStackTrace(); }

            if(result != null)
            try { result.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return false;
    }

    @Override
    public int updateToken(TokenDTO token) {
        return 0;
    }

    @Override
    public int deleteToken(TokenDTO token) {
        return 0;
    }
}
