package com.gonzalezp.apps.androidfunwithflags;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v4.app.DialogFragment;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.util.Log;

import com.gonzalezp.apps.lifecyclehelpers.psgs_QuizViewModel;

public class psgs_ResultsDialogFragment extends DialogFragment{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final psgs_QuizViewModel psgsQuizViewModel = ViewModelProviders.of(getActivity()).get(psgs_QuizViewModel.class);
        int totalGuesses = psgsQuizViewModel.getTotalGuesses();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(
                getString(R.string.results, totalGuesses, (1000 / (double) totalGuesses)));

        builder.setPositiveButton(R.string.reset_quiz, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try{
                    psgs_MainActivityFragment quizFragment = (psgs_MainActivityFragment) getParentFragment();
                    try{
                        quizFragment.resetQuiz();
                    }catch (Exception e){
                        Log.e(psgsQuizViewModel.getTag(),"Unable to call resetQuiz()", e);
                    }
                }
                catch (Exception e){
                    Log.e(psgsQuizViewModel.getTag(),"Unable to get ActivityMainFragment", e);
                }
            }
        });
        return builder.create();
    }
}
