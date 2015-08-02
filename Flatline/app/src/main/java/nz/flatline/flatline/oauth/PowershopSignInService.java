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

import nz.flatline.flatline.api.model.FlatlineRestClient;
import nz.flatline.flatline.api.model.Okay;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class PowershopSignInService implements OAuthSignInService {

    private int flatID;

    FlatlineRestClient flatLineRestAPI = new FlatlineRestClient("http://104.131.91.223:8000/api");

    private OAuthSignInUI oAuthSignInUI;

    private OAuthService service;

    // OAuth Request Token. This needs to be saved during the sign in flow as multiple points.
    private Token requestToken = null;

    private Token accessToken = null;

    public PowershopSignInService(int flatID, OAuthSignInUI oAuthSignInUI) {
        this.flatID = flatID;
        this.oAuthSignInUI = oAuthSignInUI;

        service = new ServiceBuilder()
                .provider(PowershopAPI.class).apiKey("742ea45f6dd448ba5098c4839b13be0c")
                .apiSecret("PR2tdqgNHG7zt7H2tWlK4h31t48knQum")
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
                        saveAccessTokens();
                    }
                });
    }

    private void saveAccessTokens() {
        Observable<Okay> observable =
                flatLineRestAPI.getFlatLineAPI().postAccessTokens(flatID, accessToken.getToken(), accessToken.getSecret());

        Log.d("Flatline", "Sent access tokens to server");

        observable.subscribeOn(Schedulers.io()).subscribe(new Action1<Okay>() {
            @Override
            public void call(Okay okay) {
                Log.d("Flatline", "Received okay from server");
            }
        });
    }

}
