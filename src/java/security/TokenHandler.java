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

    // Hash credentials, reduce and generate random prime and token
    public TokenHandler(String user, String pass) {
        generateRandom();
        generateToken(new BigInteger(Integer.toString((user.hashCode() + pass.hashCode())/10000000-1)));
    }

    // Generate random 7-bit prime number
    private BigInteger generateRandom() {
        randomToken = BigInteger.probablePrime(7, new SecureRandom());
        System.out.println("Random prime generated.." + randomToken);
        return randomToken;
    }

    // Generate token from random prime and hashed credentials: hashedCredentials^randomPrime (power)
    private BigInteger generateToken(BigInteger credentials) {
        publicToken = credentials.pow(Integer.parseInt(randomToken.toString()));
        System.out.println("Token generated.." + publicToken);
        return publicToken;
    }

    // Generate key from recieved token and random prime: recievedToken^randomPrime (power)
    public BigInteger generateKey(BigInteger token) {
        keyToken = token.pow(Integer.parseInt(randomToken.toString()));
        System.out.println("Key generated..");
        return keyToken;
    }
    
    // Check key with own key (should be equal)
    public boolean checkKey(BigInteger key) {
        if (key.toString().equals(keyToken.toString())) {
            return true;
        }
        System.out.println("Key  invalid");
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
    public BigInteger getKeyToken() {
        return keyToken;
    }   
}
