/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package security;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 *
 * @author hippomormor
 */
public class TokenHandler {

    private BigInteger randomToken;
    private BigInteger publicToken;
    private BigInteger keyToken;
    private BigInteger credentials;

    public TokenHandler(String user, String pass) {
        credentials = new BigInteger(Integer.toString((user.hashCode() + pass.hashCode())/10000000-1));
        System.out.println("Credentials.." + credentials);
        generateRandom();
        generateToken();
    }

    private BigInteger generateRandom() {
        randomToken = BigInteger.probablePrime(7, new SecureRandom());
        System.out.println("Random prime generated.." + randomToken);
        return randomToken;
    }

    private BigInteger generateToken() {
        publicToken = credentials.pow(Integer.parseInt(randomToken.toString()));
        System.out.println("Token generated.." + publicToken);
        return publicToken;
    }

    public BigInteger generateKey(BigInteger token) {
        keyToken = token.pow(Integer.parseInt(randomToken.toString()));
        System.out.println("Key generated.." + keyToken.toString());
        return keyToken;
    }

    public boolean checkKey(BigInteger key) {
        if (key.toString().equals(keyToken.toString())) {
            System.out.println("Key from server valid");
            return true;
        }
        System.out.println("Key from server invalid");
        return false;
    }

    public BigInteger getRandomToken() {
        return randomToken;
    }

    public BigInteger getPublicToken() {
        return publicToken;
    }
    
    public BigInteger getKeyToken() {
        return keyToken;
    }   
}
