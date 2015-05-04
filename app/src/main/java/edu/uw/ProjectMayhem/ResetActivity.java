/**
 * Project Mayhem: Jacob Hohisel, Loralyn Solomon, Brian Plocki, Brandon Soto
 */
package edu.uw.ProjectMayhem;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
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

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;


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

    /**
     * Running the loading of the JSON in a separate thread.
     * Code adapted from http://www.vogella.com/tutorials/AndroidBackgroundProcessing/article.html
     */
    /*
    private class DownloadWebPageTask extends AsyncTask<Void, Void, String> {


        private final String url = "450.atwebpages.com/reset.php";

        @Override
        protected void onPreExecute() {
            //super.onPreExecute();
            //mProgressDialog = ProgressDialog.show(CourseListActivity.this, "Wait", "Downloading...");
        }

        @Override
        protected String doInBackground(Void... params) {
            String response = "";
                DefaultHttpClient client = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet();
                httpGet.setParams();
                try {
                    HttpResponse execute = client.execute(httpGet);
                    InputStream content = execute.getEntity().getContent();

                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                    String s = "";
                    while ((s = buffer.readLine()) != null) {
                        response += s;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            //mProgressDialog.dismiss();
            mCourses = result;
            if (mCourses != null) {
                try {
                    JSONArray arr = new JSONArray(mCourses);

                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject obj = arr.getJSONObject(i);
                        Course course = new Course(obj.getString(Course.ID), obj.getString(Course.SHORT_DESC)
                                , obj.getString(Course.LONG_DESC), obj.getString(Course.PRE_REQS));
                        mCourseList.add(course);
                    }
                } catch (JSONException e) {
                    System.out.println("JSON Exception");
                }

            }

            if (!mCourseList.isEmpty()) {
                mCoursesListView.setAdapter(mAdapter);
            }
        }
    }
    */

}