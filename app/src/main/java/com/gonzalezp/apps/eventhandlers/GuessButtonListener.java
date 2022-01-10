package com.gonzalezp.apps.eventhandlers;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.gonzalezp.apps.androidfunwithflags.psgs_MainActivityFragment;
import com.gonzalezp.apps.androidfunwithflags.R;
import com.gonzalezp.apps.androidfunwithflags.psgs_ResultsDialogFragment;
import com.gonzalezp.apps.lifecyclehelpers.psgs_QuizViewModel;

public class GuessButtonListener implements OnClickListener {
    private psgs_MainActivityFragment psgsMainActivityFragment;
    private Handler handler;

    public GuessButtonListener(psgs_MainActivityFragment psgsMainActivityFragment) {
        this.psgsMainActivityFragment = psgsMainActivityFragment;
        this.handler = new Handler();
    }

    @Override
    public void onClick(View v) {
        Button guessButton = ((Button) v);
        String guess = guessButton.getText().toString();
        String answer = this.psgsMainActivityFragment.getQuizViewModel().getCorrectCountryName();
        this.psgsMainActivityFragment.getQuizViewModel().setTotalGuesses(1);

        if (guess.equals(answer)) {
            this.psgsMainActivityFragment.getQuizViewModel().setCorrectAnswers(1);
            this.psgsMainActivityFragment.getAnswerTextView().setText(answer + "!");
            this.psgsMainActivityFragment.getAnswerTextView().setTextColor(
                    this.psgsMainActivityFragment.getResources().getColor(R.color.correct_answer));

            this.psgsMainActivityFragment.disableButtons();

            if (this.psgsMainActivityFragment.getQuizViewModel().getCorrectAnswers()
                    == psgs_QuizViewModel.getFlagsInQuiz()) {
                psgs_ResultsDialogFragment quizResults = new psgs_ResultsDialogFragment();
                quizResults.setCancelable(false);
                try {
                    quizResults.show(this.psgsMainActivityFragment.getChildFragmentManager(), "Quiz Results");
                } catch (NullPointerException e) {
                    Log.e(psgs_QuizViewModel.getTag(),
                            "GuessButtonListener: this.psgsMainActivityFragment.getFragmentManager() " +
                                    "returned null",
                            e);
                }
            } else {
                this.handler.postDelayed(
                        new Runnable() {
                            @Override
                            public void run() {
                                psgsMainActivityFragment.animate(true);
                            }
                        }, 2000);
            }
        } else {
            this.psgsMainActivityFragment.incorrectAnswerAnimation();
            guessButton.setEnabled(false);
        }
    }
}
