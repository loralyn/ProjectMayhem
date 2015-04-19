package edu.uw.ProjectMayhem;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class AgreementActivity extends Activity {

    /** True if the user has already agreed to the terms. Otherwise false. */
    private boolean hasAgreed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agreement);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        Log.d("has agreed?", new Boolean(prefs.getBoolean("agreed", false)).toString());
        hasAgreed = prefs.getBoolean("agreed", false);
        if (hasAgreed) {
            Intent skipAgree = new Intent(this, LoginActivity.class);
            startActivity(skipAgree);
        }
    }

    /**
     * Saves that the user has agreed to the terms and switches to the login screen.
     */
    public void switchToLogin(View view) {
        System.err.println("ACCEPTED!!!");
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("agreed", true);
        editor.commit();
        Intent loginIntent = new Intent(this, RegistrationActivity.class);
        startActivity(loginIntent);
    }

    /**
     * Exits the app.
     */
    public void decline(View view) {
        System.exit(0);
    }


    /** {@inheritDoc} */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
}
