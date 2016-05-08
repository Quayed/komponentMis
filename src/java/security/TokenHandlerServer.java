
package security;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;

/**
 *
 * @author Christian Genter
 */
public class TokenHandlerServer {

    private BigInteger randomToken;
    private BigInteger publicToken;
    private int clientCount = 0;
    private final ArrayList<BigInteger> keyList = new ArrayList<>();

    // Hash credentials, reduce with modulo and generate random prime and token
    public TokenHandlerServer(String user, String pass) {
        
        generateRandom();
        
        BigInteger creds = new BigInteger(Integer.toString((user.hashCode() + pass.hashCode()) - 8));
        if (creds.signum() < 0)
            creds = creds.negate();
        
        creds = creds.mod(new BigInteger("16"));
        
        generateToken(creds);
    }

    // Generate random prime number
    private BigInteger generateRandom() {
        randomToken = BigInteger.probablePrime(512, new SecureRandom());
        randomToken = BigInteger.probablePrime(9, new SecureRandom());
        System.out.println("Random prime generated " + randomToken.toString());
        return randomToken;
    }

    // Generate token from random prime and hashed credentials: hashedCredentials^randomPrime (power)
    private BigInteger generateToken(BigInteger credentials) {
        publicToken = credentials.pow(Integer.parseInt(randomToken.toString()));
        System.out.println("Token generated");
        return publicToken;
    }

    // Generate key from recieved token and random prime: recievedToken^randomPrime (power). Reduce with modulo
    public BigInteger generateKey(BigInteger token, int ID) {
        BigInteger keyToken = token.modPow(randomToken, new BigInteger("1000000000000000000000000000000000000000000000000000"));
        keyList.set(ID, keyToken);
        System.out.println("Key generated");
        return keyList.get(ID);
    }

    // Check key with own key (should be equal)
    public boolean checkKey(BigInteger key, int ID) {
        if (key.equals(keyList.get(ID))) {
            return true;
        }
        System.out.println("Key invalid");
        return false;
    }

    // Get generated random prime
    public BigInteger getRandomToken() {
        return randomToken;
    }

    // Get generated token
    public BigInteger getPublicToken() {
        return publicToken;
    }

    // Get generated key
    public BigInteger getKeyToken(int ID) {
        return keyList.get(ID);
    }

    // Get new ID
    public int getNewID() {
        if (clientCount > 50000000)
            clientCount = 0;
        keyList.add(clientCount, BigInteger.ZERO);
        return clientCount++;
    }
}
