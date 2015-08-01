package nz.flatline.flatline.oauth;

import android.util.Log;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.DefaultApi10a;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class PowershopSignInService implements OAuthSignInService {

    private OAuthSignInUI oAuthSignInUI;

    private OAuthService service;

    // OAuth Request Token. This needs to be saved during the sign in flow as multiple points.
    private Token requestToken = null;

    private Token accessToken = null;

    public PowershopSignInService(OAuthSignInUI oAuthSignInUI) {
        this.oAuthSignInUI = oAuthSignInUI;

        service = new ServiceBuilder()
                .provider(PowershopAPI.class)
                // note that callback url is defined in android manifest
                .callback("flatline://flatline-sot.tk/oauth_callback")
                .build();
    }

    @Override
    public void requestAuthorizationURL() {
        // get the authorization url and pass it on to the UI.
        authorizationUrlObservable
                .subscribeOn(Schedulers.io()).subscribeOn(Schedulers.io())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String authUrl) {
                        oAuthSignInUI.onReceiveAuthorizationURL(authUrl);
                    }
                });
    }

    // create observable that will handle getting the authorization url from powershop
    private final Observable<String> authorizationUrlObservable = Observable.create(new Observable.OnSubscribe<String>() {
        @Override
        public void call(Subscriber<? super String> subscriber) {
            requestToken = service.getRequestToken();

            // notify subscriber with authorization url
            subscriber.onNext(service.getAuthorizationUrl(requestToken));
            subscriber.onCompleted();
        }
    });

    @Override
    public void onTokenVerification(final String verifier) {
        Observable<Token> accessTokenObservable = Observable.create(new Observable.OnSubscribe<Token>() {
            @Override
            public void call(Subscriber<? super Token> subscriber) {
                Verifier v = new Verifier(verifier);
                Token accessToken = service.getAccessToken(requestToken, v);
                subscriber.onNext(accessToken);

                subscriber.onCompleted();
            }
        });

        // get the authorization url and pass it on to the UI.
        accessTokenObservable
                .subscribeOn(Schedulers.io())
                .subscribe(new Action1<Token>() {
                    @Override
                    public void call(Token accessToken) {
                        PowershopSignInService.this.accessToken = accessToken;
                        sendTestRequest();
                    }
                });
    }

    private void sendTestRequest() {
        OAuthRequest request = new
                OAuthRequest(Verb.GET, "https://stable.test.powershop.co.nz/external_api/v2/accounts.js");
        service.signRequest(accessToken, request); // the access token from step 4
        Response response = request.send();
        Log.d("Flatline", response.getBody());
    }


    /**
     * Information about the Powershop API used by the Scribe OAuth Library
     */
    private class PowershopAPI extends DefaultApi10a {
        private static final String BASE_URL = "https://stable.test.powershop.co.nz/external_api/oauth/";

        private static final String REQUEST_TOKEN_URL = BASE_URL + "request_token";
        private static final String AUTHORIZE_URL = BASE_URL + "authorize?oauth_token=%s";
        private static final String ACCESS_TOKEN_URL = BASE_URL + "access_token";

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
            return String.format(AUTHORIZE_URL, requestToken.getToken());
        }
    }
}
