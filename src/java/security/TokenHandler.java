/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package security;

import java.security.SecureRandom;
import java.util.Arrays;

/**
 *
 * @author hippomormor
 */
public class TokenHandler {

    private int randomToken;
    private int publicToken;
    private final int credentials;
    
    public TokenHandler(String user, String pass) {
        credentials = user.hashCode() + pass.hashCode();   
    }

    public void generateRandom() {
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[20];
        random.nextBytes(bytes);
        randomToken = Arrays.hashCode(bytes);
    }
    
    public void generateToken(int random) {   
        publicToken =  random + credentials;
        if (checkToken(publicToken)) 
            System.out.println("Generated tokens..");
        else
            System.err.println("Failed to generate tokens..");
    }
    
    public boolean checkToken(int token) {
        return token - credentials == randomToken;
    }
    
    public int getRandomToken(){
        return randomToken;
    }
    
    public int getPublicToken(){
        return publicToken;
    }
    

    
  
}
