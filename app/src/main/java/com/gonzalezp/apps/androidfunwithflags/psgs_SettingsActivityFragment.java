package com.gonzalezp.apps.androidfunwithflags;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;

/**
 * A placeholder fragment containing a simple view.
 */
public class psgs_SettingsActivityFragment extends PreferenceFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.psgs_preferences);
    }
}
