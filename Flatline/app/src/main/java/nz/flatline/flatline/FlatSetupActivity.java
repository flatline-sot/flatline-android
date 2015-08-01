package nz.flatline.flatline;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Mayur on 1/08/2015.
 */
public class FlatSetupActivity extends ActionBarActivity {

    Button submitButton, backButton;
    TextView actionBarText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flat_setup);
        initializeViews();
        actionBarText.setText(R.string.flat_setup_title);
    }

    private void initializeViews() {
        submitButton = (Button) findViewById(R.id.submit_button);
        backButton = (Button) findViewById(R.id.back_button);
        actionBarText = (TextView) findViewById(R.id.action_bar_title);
    }
}
