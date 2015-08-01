package nz.flatline.flatline;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Mayur on 1/08/2015.
 */
public class FlatSetupActivity extends Activity {

    Button submitButton, backButton;
    TextView actionBarText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flat_setup);
        initializeViews();
    }

    private void initializeViews() {
        submitButton = (Button) findViewById(R.id.submit_button);
    }
}
