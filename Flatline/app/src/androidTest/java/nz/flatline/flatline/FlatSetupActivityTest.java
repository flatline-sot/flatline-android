package nz.flatline.flatline;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Mayur on 2/08/2015.
 */
public class FlatSetupActivityTest extends ActivityInstrumentationTestCase2<FlatSetupActivity>{

    private Button submitButton;
    private EditText flat_name;
    private TextView title;
    private FlatSetupActivity main;

    public FlatSetupActivityTest(){
        super(FlatSetupActivity.class);
    }

    public FlatSetupActivityTest(Class<FlatSetupActivity> activityClass) {
        super(activityClass);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        initializeViews();
        testEmptiness();
    }

    public void testEmptiness(){
        assertNotNull("Activity is null", main);
        assertNotNull("Flat Name field cannot be found", flat_name);
        assertNotNull("Submit button cannot be found", submitButton);
        String actual = main.getString(R.string.app_name);
        String expected = title.getText().toString();
        assertEquals(actual, expected);
    }

    private void initializeViews() {
        main = getActivity();
        flat_name = (EditText) main.findViewById(R.id.flat_name);
        submitButton = (Button) main.findViewById(R.id.submit_button);
        title = (TextView) main.findViewById(R.id.flat_setup_title);
    }
}
