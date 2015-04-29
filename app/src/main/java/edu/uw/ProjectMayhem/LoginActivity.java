package edu.uw.ProjectMayhem;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends ActionBarActivity implements LoaderCallbacks<Cursor> {

    /**
     * A dummy authentication store containing known user names and passwords.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    /** Where the user types in the email address. */
    private AutoCompleteTextView mEmail;

    /** Where the user types in the password. */
    private EditText mPassword;

    /** Shows progress for signing in. */
    private View mProgressView;

    /** Initiates signing a user in. */
    private Button mLoginButton;

    /** Switches to the registration screen. */
    private Button mRegisterButton;

    /** Switches to the "forgot password" screen. */
    private Button mForgotButton;

    /** Saved email from preferences. */
    private String savedEmail;

    /** Saved password from preferences. */
    private String savedPassword;

    /** Saved security answer from preferences. */
    private String savedAnswer;

    /** Saved security question from preferences. */
    private String savedQuestion;

    private View mLoginFormView;

    private Bundle instanceState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instanceState = savedInstanceState;
        setContentView(R.layout.activity_login);

        // Get the saved data
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        savedEmail = prefs.getString("email", "no default");
        savedPassword = prefs.getString("password", "no default");
        savedAnswer = prefs.getString("answer", "no default");
        savedQuestion = prefs.getString("security", "no default");

        // Set up the login form.
        mEmail = (AutoCompleteTextView) findViewById(R.id.email);
        populateAutoComplete();

        mPassword = (EditText) findViewById(R.id.password);
        mEmail = (AutoCompleteTextView) findViewById(R.id.email);

        mPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        mLoginButton = (Button) findViewById(R.id.email_sign_in_button);
        mLoginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mRegisterButton = (Button) findViewById(R.id.register_button);
        mRegisterButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                register(view);
            }
        });

        mForgotButton = (Button) findViewById(R.id.forgot_button);
        mForgotButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                reset(view);
            }
        });


        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    /** Starts the "reset password" screen. */
    private void reset(View view) {
        Intent resetIntent = new Intent(this, ResetActivity.class);
        startActivity(resetIntent);
    }

    /** Starts the register screen. */
    private void register(View view) {
        Intent registerIntent = new Intent(this, RegistrationActivity.class);
        startActivity(registerIntent);
    }

    /** {@inheritDoc} */
    private void populateAutoComplete() {
        getLoaderManager().initLoader(0, null, this);
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void attemptLogin() {

        Log.d("login", "ATTEMPING LOGIN");
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmail.setError(null);
        mPassword.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmail.getText().toString();
        String password = mPassword.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPassword.setError(getString(R.string.error_invalid_password));
            focusView = mPassword;
            cancel = true;
        }


        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmail.setError(getString(R.string.error_field_required));
            focusView = mEmail;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmail.setError(getString(R.string.error_invalid_email));
            focusView = mEmail;
            cancel = true;
        }

        if (cancel) {

            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();

        } else {

            // Kick off a background task to
            // perform the user login attempt.
            UserLoginTask task = new UserLoginTask(mEmail.getText().toString(), mPassword.getText().toString());
            task.execute();

        }
    }

    private void doLogin() {
        // If login is successful, switch to MyAccount
        showProgress(false);

        Toast.makeText(this, mEmail.getText().toString()
                + " has signed in!", Toast.LENGTH_SHORT).show();

        Intent accountIntent = new Intent(this, MyAccount.class);
        startActivity(accountIntent);
    }

    /** {@inheritDoc} */
    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    /** {@inheritDoc} */
    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /** {@inheritDoc} */
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    /** {@inheritDoc} */
    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<String>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    /** {@inheritDoc} */
    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    /** {@inheritDoc} */
    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /** {@inheritDoc} */
    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmail.setAdapter(adapter);
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, String> {

        /** The user's email. */
        private final String mUserEmail;

        /** The user's password. */
        private final String mUserPassword;

        /** URL address of login PHP page. */
        private final String webURL = "http://cssgate.insttech.washington.edu/~_450team3/login.php";

        UserLoginTask(String email, String password) {
            mUserEmail = email;
            mUserPassword = password;
        }

        /** {@inheritDoc} */
        @Override
        protected void onPreExecute() {
            showProgress(true);
        }

        /** {@inheritDoc} */
        @Override
        protected String doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            String result = "";

            HttpURLConnection connection;
            OutputStreamWriter request = null;

            URL url = null;
            String response = null;
            String parameters = "email=" + mUserEmail;

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

                InputStreamReader isr = new InputStreamReader(connection.getInputStream());
                BufferedReader reader = new BufferedReader(isr);
                String savedPass = reader.readLine();
                String savedSalt = reader.readLine();

                // Response from server after login process will be stored in response variable.
                System.out.println("Saved password: " + savedPass);
                System.out.println("Saved salt: " + savedSalt);

                if (savedPass.equals("") && savedSalt.equals("")) {

                    System.out.println("Unknown email!");
                    result = "bad_email";

                } else {

                    // Hash the entered password using the salt and compare to saved password.
                    String enteredPass = mUserPassword;

                    if (enteredPass.equals(savedPass)) {

                        System.out.println("Hashes are equal!");
                        result = "success";

                    } else {

                        System.out.println("Passwords don't match!");
                        result = "bad_password";

                    }

                }

                // You can perform UI operations here
                isr.close();
                reader.close();

            }
            catch(IOException e)
            {
                System.err.println("Something bad happened");
            }

            // TODO: register the new account here.
            return result;
        }

        /** {@inheritDoc} */
        @Override
        protected void onPostExecute(final String success) {
            mAuthTask = null;
            showProgress(false);

            if (success.equals("success")) {

                doLogin();
                finish();

            } else if (success.equals("bad_email")) {

                mEmail.setError(getString(R.string.unknown_email));
                mEmail.requestFocus();

            } else {

                mPassword.setError(getString(R.string.error_incorrect_password));
                mPassword.requestFocus();

            }
        }

        /** {@inheritDoc} */
        @Override
        protected void onCancelled() {

            mAuthTask = null;
            showProgress(false);

        }
    }
}



