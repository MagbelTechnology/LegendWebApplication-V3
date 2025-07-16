/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ng.com.magbel.token;

/**
 *
 * @author Kazeem
 */
public class TestTokenCall {
    public static void main(String[] args){
        TokenAuthentication token = new TokenAuthentication();
        TokenAuthenticationSoap soap = token.getTokenAuthenticationSoap();
        soap.authenticateToken("AYE123", "1282992");
    }
}
