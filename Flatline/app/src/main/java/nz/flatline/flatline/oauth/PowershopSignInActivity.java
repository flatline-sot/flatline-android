package nz.flatline.flatline.oauth;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import nz.flatline.flatline.R;
import nz.flatline.flatline.api.model.Bill;
import nz.flatline.flatline.api.model.FlatlineRestClient;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class PowershopSignInActivity extends AppCompatActivity implements OAuthSignInUI {

    OAuthSignInService powershopSignInService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_powershop_sign_in);

        powershopSignInService = new PowershopSignInService(this);

        Button connectWithPowershop = (Button) findViewById(R.id.connect_with_powershop);

        connectWithPowershop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                powershopSignInService.requestAuthorizationURL();
            }
        });

        FlatlineRestClient client = new FlatlineRestClient("http://104.131.91.223:8000/api");

        Observable<Bill> bill = client.getFlatLineAPI().bill(1);

        bill.subscribeOn(Schedulers.io()).subscribe(new Subscriber<Bill>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                Log.e("FLATLINE", e.getMessage());
            }

            @Override
            public void onNext(Bill bill) {
                Log.d("Flatline", String.valueOf(bill.cost));
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_powershop_sign_in, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Uri uri = intent.getData();
        // check if the request was request using our oauth callback uri
        if (uri != null && uri.toString().startsWith("flatline://flatline-sot.tk/oauth_callback")) {
            String oAuthVerifier = uri.getQueryParameter("oauth_verifier");

            // pass it on to service to handle
            powershopSignInService.onTokenVerification(oAuthVerifier);
        }
    }

    @Override
    public void onReceiveAuthorizationURL(String authorizationURL) {
        // redirect user to their browser where they can log in to powershop
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(authorizationURL));
        startActivity(browserIntent);
    }

}
