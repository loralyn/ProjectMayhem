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

    private User mUser;
    private static String uid = "1";
    private static final String PREFS_NAME = "preferences";

    private EditText mEmailText;
    private EditText mPasswordText;
    private EditText mAnswerText;
    private Spinner mSecuritySpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mEmailText = (EditText) findViewById(R.id.email);
        mPasswordText = (EditText) findViewById(R.id.password);
        mAnswerText = (EditText) findViewById(R.id.answer_field);

        Intent fromLogin = getIntent();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String n = prefs.getString("email", mEmailText.getText().toString());
        mEmailText.setText(n.toString());

        // Fill spinner items
        mSecuritySpinner = (Spinner) findViewById(R.id.spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.security_questions, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);

        mSecuritySpinner.setAdapter(adapter);

        Button mRegisterButton = (Button) findViewById(R.id.email_register_button);
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mUser = new User(uid, mEmailText.getText().toString(), mPasswordText.getText().toString(), mAnswerText.getText().toString());
                login(v);
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login(view);
            }
        });

    }

    private void login(View view) {
        Intent loginIntent = new Intent(this, LoginActivity.class);
        finish();
        startActivity(loginIntent);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putSerializable("user", mUser);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mUser = (User) savedInstanceState.getSerializable("user");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_registration, menu);
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
    protected void onDestroy() {
        super.onDestroy();
        Log.d("registrationActivity", "ON DESTROY CALLED!");
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString("email", mEmailText.getText().toString());
        editor.putString("password", mPasswordText.getText().toString());
        editor.putString("answer", mAnswerText.getText().toString());
        editor.putString("security", mSecuritySpinner.getSelectedItem().toString());

        editor.commit();
    }
}
