/**
 * Project Mayhem: Jacob Hohisel, Loralyn Solomon, Brian Plocki, Brandon Soto
 */
package edu.uw.ProjectMayhem;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A screen that allows teh user to reset his/her password.
 */
public class ResetActivity extends ActionBarActivity {

    /**
     * Displays the user's security question.
     */
    private TextView mQuestion;

    /**
     * Where the user types the new password.
     */
    private EditText mNewPassword;

    /**
     * Where the user types a ccnfirmed password.
     */
    private EditText mConfirmPassword;

    /**
     * Where the user types in email address.
     */
    private EditText mEmail;

    /**
     * Where user enters the answer for his/her security question.
     */
    private EditText mAnswer;

    /**
     * Initiates the reset password process.
     */
    private Button mResetButton;

    /**
     * Stores a saved answer to the security question
     */
    private String savedAnswer;

    /**
     * Stores a saved user email.
     */
    private String currentEmail;

    /**
     * contains the shared preferences.
     */
    private SharedPreferences prefs;

    /**
     * The savedInstance of the user.
     */
    private Bundle savedInstance;

    /**
     * onCreate method creates the Reset Activity.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        savedInstance = savedInstanceState;
        savedInstance = savedInstanceState;
        setContentView(R.layout.activity_reset);

        mQuestion = (TextView) findViewById(R.id.sec_question);
        mNewPassword = (EditText) findViewById(R.id.new_password);
        mConfirmPassword = (EditText) findViewById(R.id.confirm_password);
        mEmail = (EditText) findViewById(R.id.email);
        mAnswer = (EditText) findViewById(R.id.security_answer);
        mResetButton = (Button) findViewById(R.id.reset_password);

        Intent loginIntent = getIntent();
        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        String securityQuestion = prefs.getString("security", "No saved question!");

        savedAnswer = prefs.getString("answer", "No answer");
        currentEmail = prefs.getString("email", "No email");

        mQuestion.setText(securityQuestion);

        mResetButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                reset(v);
            }
        });

    }

    /**
     * Attempts to reset the user's password.
     *
     * @param view the view context of widget calling this method.
     */
    private void reset(View view) {
        Intent loginIntent = new Intent(this, LoginActivity.class);

        if (mEmail.getText().toString().equals(currentEmail)
                && mAnswer.getText().toString().equals(savedAnswer)
                && mNewPassword.getText().toString().equals(mConfirmPassword.getText().toString())) {

            final SharedPreferences.Editor editor = prefs.edit();
            editor.putString("password", mNewPassword.getText().toString());
            editor.apply();

            Toast.makeText(this, "Password reset successfully!", Toast.LENGTH_SHORT).show();

            finish();
            startActivity(loginIntent);
        } else {
            Toast.makeText(this, "Invalid email and/or security answer.", Toast.LENGTH_SHORT).show();
            // caused app to crash
            // onCreate(savedInstance);
        }

    }

}