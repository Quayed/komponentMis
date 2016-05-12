/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package security;

import com.sun.javafx.scene.control.skin.VirtualFlow;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;

/**
 *
 * @author hippomormor
 */
public class TokenHandlerServer {

    private BigInteger randomToken;
    private BigInteger publicToken;
    private int clientCount = 0;
    private final ArrayList<BigInteger> keyList = new ArrayList<>();

    // Hash credentials, reduce and generate random prime and token
    public TokenHandlerServer(String user, String pass) {
        generateRandom();
        BigInteger creds = new BigInteger(Integer.toString((user.hashCode() + pass.hashCode())/10000000-8));
        if (creds.signum() < 1)
            generateToken(creds.negate());
        else
            generateToken(creds);
    }

    // Generate random 7-bit prime number
    private BigInteger generateRandom() {
        randomToken = BigInteger.probablePrime(7, new SecureRandom());
        System.out.println(randomToken.toString());
        System.out.println("Random prime generated");
        return randomToken;
    }

    // Generate token from random prime and hashed credentials: hashedCredentials^randomPrime (power)
    private BigInteger generateToken(BigInteger credentials) {
        publicToken = credentials.pow(Integer.parseInt(randomToken.toString()));
        System.out.println("Token generated");
        return publicToken;
    }

    // Generate key from recieved token and random prime: recievedToken^randomPrime (power)
    public BigInteger generateKey(BigInteger token, int ID) {
        BigInteger keyToken;
        keyToken = token.pow(Integer.parseInt(randomToken.toString()));
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
