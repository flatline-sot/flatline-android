package nz.flatline.flatline.oauth;


/**
 * A service which manages the oauth sign in process for some API.
 */
public interface OAuthSignInService {

    /**
     * Request the authorization URL from the server.
     **/
    void requestAuthorizationURL();

    /**
     * Called when the request token has been verified.
     *
     * @param verifier an oauth verifier
     */
    void onTokenVerification(String verifier);
}
