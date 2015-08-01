package nz.flatline.flatline.oauth;


/**
 * Interface for OAuthSignIn UI's
 */
public interface OAuthSignInUI {

    /**
     * Called when an authorization URL is available.
     *
     * @param authorizationURL authorization url
     */
    void onReceiveAuthorizationURL(String authorizationURL);

}
