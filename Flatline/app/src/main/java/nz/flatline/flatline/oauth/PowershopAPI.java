package nz.flatline.flatline.oauth;

import org.scribe.builder.api.DefaultApi10a;
import org.scribe.model.Token;

/**
 * Information about the Powershop API used by the Scribe OAuth Library
 */
public class PowershopAPI extends DefaultApi10a {
    private static final String BASE_URL = "https://wip5.test.powershop.co.nz/external_api/oauth/";

    private static final String REQUEST_TOKEN_URL = BASE_URL + "request_token";
    private static final String AUTHORIZE_URL = BASE_URL + "authorize?oauth_token=%s";
    private static final String ACCESS_TOKEN_URL = BASE_URL + "access_token";
    private Token requestToken;

    @Override
    public String getRequestTokenEndpoint() {
        return REQUEST_TOKEN_URL;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return ACCESS_TOKEN_URL;
    }

    @Override
    public String getAuthorizationUrl(Token requestToken) {
        this.requestToken = requestToken;
        return String.format(AUTHORIZE_URL, requestToken.getToken());
    }
}