package com.gonzalezp.apps.androidfunwithflags;

import java.io.IOException;
import java.io.InputStream;
import java.security.SecureRandom;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.arch.lifecycle.ViewModelProviders;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import android.support.constraint.ConstraintLayout;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.gonzalezp.apps.eventhandlers.GuessButtonListener;
import com.gonzalezp.apps.lifecyclehelpers.psgs_QuizViewModel;

public class psgs_MainActivityFragment extends Fragment {

    private SecureRandom random;
    private Animation shakeAnimation;
    private ConstraintLayout quizConstraintLayout;
    private TextView questionNumberTextView;
    private ImageView flagImageView;
    private TableRow[] guessTableRows;
    private TextView answerTextView;
    private psgs_QuizViewModel psgsQuizViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.psgsQuizViewModel = ViewModelProviders.of(getActivity()).get(psgs_QuizViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.psgs_fragment_main, container, false);
        OnClickListener guessButtonListener = new GuessButtonListener(this);
        TableLayout answersTableLayout = view.findViewById(R.id.answersTableLayout);

        this.random = new SecureRandom();
        this.shakeAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.psgs_incorrect_shake);
        this.shakeAnimation.setRepeatCount(3);
        this.quizConstraintLayout = view.findViewById(R.id.quizConstraintLayout);
        this.questionNumberTextView = view.findViewById(R.id.questionNumberTextView);
        this.flagImageView = view.findViewById(R.id.flagImageView);

        this.guessTableRows = new TableRow[4];
        this.answerTextView = view.findViewById(R.id.answerTextView);



        for (int i = 0; i < answersTableLayout.getChildCount(); i++) {
            try {
                if (answersTableLayout.getChildAt(i) instanceof TableRow) {
                    this.guessTableRows[i] = (TableRow) answersTableLayout.getChildAt(i);
                }
            } catch (ArrayStoreException e) {
                Log.e(psgs_QuizViewModel.getTag(),
                        "Error getting button rows on loop #" + String.valueOf(i), e);
            }
        }

        for (TableRow row : this.guessTableRows) {
            for (int column = 0; column < row.getChildCount(); column++) {
                (row.getChildAt(column)).setOnClickListener(guessButtonListener);
            }
        }

        this.questionNumberTextView.setText(
                getString(R.string.question, 1, psgs_QuizViewModel.getFlagsInQuiz()));
        return view;
    }

    public void updateGuessRows() {

        int numberOfGuessRows = this.psgsQuizViewModel.getGuessRows();
        for (TableRow row : this.guessTableRows) {
            row.setVisibility(View.GONE);
        }
        for (int rowNumber = 0; rowNumber < numberOfGuessRows; rowNumber++) {
            guessTableRows[rowNumber].setVisibility(View.VISIBLE);
        }
    }

    public void resetQuiz() {
        this.psgsQuizViewModel.clearFileNameList();
        this.psgsQuizViewModel.setFileNameList(getActivity().getAssets());
        this.psgsQuizViewModel.resetTotalGuesses();
        this.psgsQuizViewModel.resetCorrectAnswers();
        this.psgsQuizViewModel.clearQuizCountriesList();

        int flagCounter = 1;
        int numberOfFlags = this.psgsQuizViewModel.getFileNameList().size();
        while (flagCounter <= psgs_QuizViewModel.getFlagsInQuiz()) {
            int randomIndex = this.random.nextInt(numberOfFlags);

            String filename = this.psgsQuizViewModel.getFileNameList().get(randomIndex);

            if (!this.psgsQuizViewModel.getQuizCountriesList().contains(filename)) {
                this.psgsQuizViewModel.getQuizCountriesList().add(filename);
                ++flagCounter;
            }
        }

        this.updateGuessRows();
        this.loadNextFlag();
    }

    private void loadNextFlag() {
        AssetManager assets = getActivity().getAssets();
        String nextImage = this.psgsQuizViewModel.getNextCountryFlag();
        String region = nextImage.substring(0, nextImage.indexOf('-'));

        this.psgsQuizViewModel.setCorrectAnswer(nextImage);
        answerTextView.setText("");

        questionNumberTextView.setText(getString(R.string.question,
                (psgsQuizViewModel.getCorrectAnswers() + 1), psgs_QuizViewModel.getFlagsInQuiz()));

        try (InputStream stream = assets.open(region + "/" + nextImage + ".png")) {
            Drawable flag = Drawable.createFromStream(stream, nextImage);
            flagImageView.setImageDrawable(flag);
            animate(false);
        } catch (IOException e) {
            Log.e(psgs_QuizViewModel.getTag(), "Error Loading " + nextImage, e);
        }

        this.psgsQuizViewModel.shuffleFilenameList();

        for (int rowNumber = 0; rowNumber < this.psgsQuizViewModel.getGuessRows(); rowNumber++) {
            for (int column = 0; column < guessTableRows[rowNumber].getChildCount(); column++) {
                Button guessButton = (Button) guessTableRows[rowNumber].getVirtualChildAt(column);
                guessButton.setEnabled(true);
                String filename = this.psgsQuizViewModel.getFileNameList()
                        .get((rowNumber * 2) + column)
                        .substring(this.psgsQuizViewModel.getFileNameList()
                                .get((rowNumber * 2) + column).indexOf('-') + 1)
                        .replace('_', ' ');
                guessButton.setText(filename);
            }
        }

        int row = this.random.nextInt(this.psgsQuizViewModel.getGuessRows());
        int column = this.random.nextInt(2);
        TableRow randomRow = guessTableRows[row];
        ((Button) randomRow.getChildAt(column)).setText(this.psgsQuizViewModel.getCorrectCountryName());
    }

    public void animate(boolean animateOut) {
        if (this.psgsQuizViewModel.getCorrectAnswers() == 0) {
            return;
        }
        int centreX = (quizConstraintLayout.getLeft() + quizConstraintLayout.getRight()) / 2;
        int centreY = (quizConstraintLayout.getTop() + quizConstraintLayout.getBottom()) / 2;
        int radius = Math.max(quizConstraintLayout.getWidth(), quizConstraintLayout.getHeight());
        Animator animator;
        if (animateOut) {
            animator = ViewAnimationUtils.createCircularReveal(
                    quizConstraintLayout, centreX, centreY, radius, 0);
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    loadNextFlag();
                }
            });
        } else {
            animator = ViewAnimationUtils.createCircularReveal(
                    quizConstraintLayout, centreX, centreY, 0, radius);
        }

        animator.setDuration(500);
        animator.start();
    }

    public void incorrectAnswerAnimation(){
        flagImageView.startAnimation(shakeAnimation);

        answerTextView.setText(R.string.incorrect_answer);
        answerTextView.setTextColor(getResources().getColor(R.color.wrong_answer));
    }

    public void disableButtons() {
        for (TableRow row : this.guessTableRows) {
            for (int column = 0; column < row.getChildCount(); column++) {
                (row.getChildAt(column)).setEnabled(false);
            }
        }
    }

    public TextView getAnswerTextView() {
        return answerTextView;
    }

    public psgs_QuizViewModel getQuizViewModel() {
        return psgsQuizViewModel;
    }
}

