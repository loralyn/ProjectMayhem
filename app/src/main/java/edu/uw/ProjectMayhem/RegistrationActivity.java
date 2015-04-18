package edu.uw.ProjectMayhem;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


public class RegistrationActivity extends ActionBarActivity {

    /** Used to generate unique user ID. */
    private static String uid = "1";

    /** The newly registered user. */
    private User mUser;

    /** Where user types in email. */
    private EditText mEmailText;

    /** Where user types in password. */
    private EditText mPasswordText;

    /** Stores all possible security questions. */
    private Spinner mSecuritySpinner;

    /** Where user types in answer to security question. */
    private EditText mAnswerText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mEmailText = (EditText) findViewById(R.id.email);
        mPasswordText = (EditText) findViewById(R.id.password);
        mAnswerText = (EditText) findViewById(R.id.answer_field);

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        final String theEmail = prefs.getString("email", mEmailText.getText().toString());

        mEmailText.setText(theEmail.toString());

        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.security_questions, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);

        mSecuritySpinner = (Spinner) findViewById(R.id.spinner);
        mSecuritySpinner.setAdapter(adapter);

        final Button mRegisterButton = (Button) findViewById(R.id.email_register_button);
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Attempts to register the user and then switch to login screen.
             */
            @Override
            public void onClick(View v) {
                mUser = new User(uid, mEmailText.getText().toString(), mPasswordText.getText().toString(),
                        mAnswerText.getText().toString(), mAnswerText.getText().toString());

                login(v);
            }
        });

        final Button mSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mSignInButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Switches to login screen.
             */
            @Override
            public void onClick(View view) {
                login(view);
            }
        });

    }

    /**
     * Attempts a user login.
     */
    private void login(View view) {
        Intent loginIntent = new Intent(this, LoginActivity.class);
        finish();
        startActivity(loginIntent);
    }

    /** {@inheritDoc} */
    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putSerializable("user", mUser);
    }

    /** {@inheritDoc} */
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mUser = (User) savedInstanceState.getSerializable("user");
    }

    /** {@inheritDoc} */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_registration, menu);
        return true;
    }

    /** {@inheritDoc} */
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

    /**
     * Destorys this activity and saves user data.
     */
    @Override
    protected void onDestroy() {
        Log.d("registrationActivity", "ON DESTROY CALLED!");
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        final SharedPreferences.Editor editor = prefs.edit();
        editor.putString("email", mEmailText.getText().toString());
        editor.putString("password", mPasswordText.getText().toString());
        editor.putString("answer", mAnswerText.getText().toString());
        editor.putString("security", mSecuritySpinner.getSelectedItem().toString());
        editor.commit();

        super.onDestroy();
    }
}
