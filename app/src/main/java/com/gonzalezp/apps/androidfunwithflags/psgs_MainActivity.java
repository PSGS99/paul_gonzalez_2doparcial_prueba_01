package com.gonzalezp.apps.androidfunwithflags;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.gonzalezp.apps.eventhandlers.psgs_PreferenceChangeListener;
import com.gonzalezp.apps.lifecyclehelpers.psgs_QuizViewModel;

public class psgs_MainActivity extends AppCompatActivity {
    public static final String CHOICES = "pref_numberOfChoices";
    public static final String REGIONS = "pref_regionsToInclude";
    private boolean deviceIsPhone = true;
    private boolean preferencesChanged = true;
    private psgs_MainActivityFragment quizFragment;
    private psgs_QuizViewModel psgsQuizViewModel;
    private OnSharedPreferenceChangeListener preferencesChangeListener;

    private void setSharedPreferences() {
        // set default values in the app's SharedPreferences
        PreferenceManager.setDefaultValues(this, R.xml.psgs_preferences, false);

        // Register a listener for shared preferences changes
        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(preferencesChangeListener);
    }

    private void screenSetUp() {
        if (getScreenSize() == Configuration.SCREENLAYOUT_SIZE_LARGE ||
                getScreenSize() == Configuration.SCREENLAYOUT_SIZE_XLARGE) {
            deviceIsPhone = false;
        }
        if (deviceIsPhone) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.psgsQuizViewModel = ViewModelProviders.of(this).get(psgs_QuizViewModel.class);
        this.preferencesChangeListener = new psgs_PreferenceChangeListener(this);
        setContentView(R.layout.psgs_activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.setSharedPreferences();
        this.screenSetUp();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (preferencesChanged) {
            this.quizFragment = (psgs_MainActivityFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.quizFragment);
            this.psgsQuizViewModel.setGuessRows(PreferenceManager.getDefaultSharedPreferences(this)
                    .getString(CHOICES, null));
            this.psgsQuizViewModel.setRegionsSet(PreferenceManager.getDefaultSharedPreferences(this)
                    .getStringSet(REGIONS, null));

            this.quizFragment.resetQuiz();

            preferencesChanged = false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.psgs_menu_main, menu);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent preferencesIntent = new Intent(this, psgs_SettingsActivity.class);
        startActivity(preferencesIntent);
        return super.onOptionsItemSelected(item);
    }

    public int getScreenSize() {
        return getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK;
    }

    public psgs_MainActivityFragment getQuizFragment() {
        return this.quizFragment;
    }

    public psgs_QuizViewModel getQuizViewModel() {
        return psgsQuizViewModel;
    }

    public static String getCHOICES() {
        return CHOICES;
    }

    public static String getREGIONS() {
        return REGIONS;
    }

    public void setPreferencesChanged(boolean preferencesChanged) {
        this.preferencesChanged = preferencesChanged;
    }


}
