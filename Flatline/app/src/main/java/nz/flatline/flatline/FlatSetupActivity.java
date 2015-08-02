package nz.flatline.flatline;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Mayur on 1/08/2015.
 */
public class FlatSetupActivity extends Activity implements View.OnClickListener{

    Button submitButton;
    EditText name, flat_name;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flat_setup);
        initializeViews();
    }

    private void initializeViews() {
        name = (EditText) findViewById(R.id.name);
        flat_name = (EditText) findViewById(R.id.flat_name);
        submitButton = (Button) findViewById(R.id.submit_button);
        submitButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(TextUtils.isEmpty(name.getText())) {
            Toast.makeText(FlatSetupActivity.this, "Please enter a name!", Toast.LENGTH_SHORT).show();
        } else if(TextUtils.isEmpty(flat_name.getText())) {
            Toast.makeText(FlatSetupActivity.this, "Please enter a flat name!", Toast.LENGTH_SHORT).show();
        } else {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            prefs.edit().putBoolean("FLAT_EXISTS" ,true).commit();
            startActivity(new Intent(this, HomepageActivity.class));
            finish();
        }
    }
}
