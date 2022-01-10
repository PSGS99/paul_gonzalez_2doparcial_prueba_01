package com.gonzalezp.apps.eventhandlers;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.widget.Toast;

import com.gonzalezp.apps.androidfunwithflags.psgs_MainActivity;
import com.gonzalezp.apps.androidfunwithflags.R;

import java.util.Set;

public class psgs_PreferenceChangeListener implements OnSharedPreferenceChangeListener {
    private psgs_MainActivity psgsMainActivity;

    public psgs_PreferenceChangeListener(psgs_MainActivity psgsMainActivity) {
        this.psgsMainActivity = psgsMainActivity;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        this.psgsMainActivity.setPreferencesChanged(true);

        if (key.equals(this.psgsMainActivity.getREGIONS())) {
            this.psgsMainActivity.getQuizViewModel().setGuessRows(sharedPreferences.getString(
                    psgs_MainActivity.CHOICES, null));
            this.psgsMainActivity.getQuizFragment().resetQuiz();
        } else if (key.equals(this.psgsMainActivity.getCHOICES())) {
            Set<String> regions = sharedPreferences.getStringSet(this.psgsMainActivity.getREGIONS(),
                    null);
            if (regions != null && regions.size() > 0) {
                this.psgsMainActivity.getQuizViewModel().setRegionsSet(regions);
                this.psgsMainActivity.getQuizFragment().resetQuiz();
            } else {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                regions.add(this.psgsMainActivity.getString(R.string.default_region));
                editor.putStringSet(this.psgsMainActivity.getREGIONS(), regions);
                editor.apply();
                Toast.makeText(this.psgsMainActivity, R.string.default_region_message,
                        Toast.LENGTH_LONG).show();
            }
        }

        Toast.makeText(this.psgsMainActivity, R.string.restarting_quiz,
                Toast.LENGTH_SHORT).show();
    }
}
