package edu.uw.ProjectMayhem;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
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
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;


public class RegistrationActivity extends ActionBarActivity {

    /** Used to generate unique user ID. */
    private static String uid = "1";

    /** The newly registered user. */
    private User mUser;

    /** Where user types in email. */
    private EditText mEmailText;

    /** Where user types in password. */
    private EditText mPasswordText;

    /** Password confirmation. */
    private EditText mConfirmPasswordText;

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
        mConfirmPasswordText = (EditText) findViewById(R.id.password_confirm);
        mAnswerText = (EditText) findViewById(R.id.answer_field);

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

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

                final View theView = v;

                // we need error checking eventually //////////////////////////////////////////////

                if (!isEmailValid(mEmailText.getText().toString())) {

                    mEmailText.setError("Not a valid email.");
                    mEmailText.requestFocus();

                } else if (!isPasswordValid(mPasswordText.getText().toString())) {

                    mPasswordText.setError("The password is too short.");
                    mPasswordText.requestFocus();

                } else if (!mPasswordText.getText().toString().equals(mConfirmPasswordText.getText().toString())) {

                    mConfirmPasswordText.setError("Passwords don't match!");
                    mConfirmPasswordText.requestFocus();

                } else if (!isAnswerValid(mAnswerText.getText().toString())) {

                    mAnswerText.setError("Please enter an answer.");
                    mAnswerText.requestFocus();

                } else {

                    mUser = new User(uid, mEmailText.getText().toString(), mPasswordText.getText().toString(),
                            mSecuritySpinner.getSelectedItem().toString(), mAnswerText.getText().toString());

                    // Display agreement, and only register to server if agreed, else exit

                    AlertDialog.Builder agreement = new AlertDialog.Builder(RegistrationActivity.this);
                    agreement
                            .setTitle("User Agreement")
                            .setMessage(R.string.agreement_terms)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton("I Agree", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface agreement, int which) {
                                    // Register to server
                                    RegisterWebTask task = new RegisterWebTask();
                                    task.execute();

                                    Toast.makeText(RegistrationActivity.this,
                                            "Registration successful!", Toast.LENGTH_SHORT).show();
                                    login(theView);
                                }
                            })
                            .setNegativeButton("Decline", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface agreement, int which) {
                                    System.exit(0);
                                }
                            })
                            .show();
                }
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
     * Goes to login page.
     */
    private void login(View view) {
        Intent loginIntent = new Intent(this, LoginActivity.class);
        startActivity(loginIntent);
        finish();
    }

    /** {@inheritDoc} */
    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /** {@inheritDoc} */
    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isAnswerValid(String answer) {
        //TODO: Replace this with your own logic
        return answer.length() > 0;
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
        editor.apply();

        super.onDestroy();
    }

    /**
     * Registers the new user to the web server.
     */
    private class RegisterWebTask extends AsyncTask<String, Void, String> {

        /** Register URL */
        private String webURL = "http://cssgate.insttech.washington.edu/~_450team3/register.php";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... urls) {

            HttpURLConnection connection;
            OutputStreamWriter request = null;

            URL url = null;
            String response = null;
            String parameters = "email=";

            /*
            String parameters = ("access=" + "66E2094E"
                                  +"&email=" + mUser.getEmail()
                                  +"&pass=" + mUser.getPwHash()
                                  +"&salt=" + mUser.getSalt()
                                  +"&q=" + mUser.getSecurityQuestion()
                                  +"&a=" + mUser.getSecurityAnswer());
                                  */
            try
            {
                url = new URL(webURL);
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                connection.setRequestMethod("POST");

                request = new OutputStreamWriter(connection.getOutputStream());
                request.write(parameters);
                request.flush();
                request.close();
                String line = "";
                InputStreamReader isr = new InputStreamReader(connection.getInputStream());
                BufferedReader reader = new BufferedReader(isr);
                StringBuilder sb = new StringBuilder();
                while ((line = reader.readLine()) != null)
                {
                    sb.append(line + "\n");
                }
                // Response from server after login process will be stored in response variable.
                response = sb.toString();
                // You can perform UI operations here
                isr.close();
                reader.close();

            }
            catch(IOException e)
            {
                System.err.println("Something bad happened");
            }

            return response;
        }

        @Override
        protected void onPostExecute(String result) {

        }
    }
}
