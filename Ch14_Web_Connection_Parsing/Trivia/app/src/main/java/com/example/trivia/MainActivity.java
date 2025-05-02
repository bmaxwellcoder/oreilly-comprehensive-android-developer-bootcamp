package com.example.trivia;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.trivia.data.AnswerListAsyncResponse;
import com.example.trivia.data.QuestionBank;
import com.example.trivia.model.Question;
import com.example.trivia.model.Score;
import com.example.trivia.util.Prefs;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView questionTextView;
    private TextView scoreTextView;
    private TextView highestScoreTextView;
    private TextView questionCounterTextView;
    private Button trueButton;
    private Button falseButton;
    private ImageButton nextButton;
    private ImageButton prevButton;
    private  int currentQuestionIndex = 0;
    private  int scoreCounter = 0;
    private Score score;
    private Prefs prefs;
    private static List<Question> questionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        score = new Score();

        prefs = new Prefs(this);

        scoreTextView = findViewById(R.id.score_textView);
        highestScoreTextView = findViewById(R.id.highest_score_textView);
        nextButton = findViewById(R.id.next_button);
        prevButton = findViewById(R.id.prev_button);
        trueButton = findViewById(R.id.true_button);
        falseButton = findViewById(R.id.false_button);
        questionCounterTextView = findViewById(R.id.counter_text);
        questionTextView = findViewById(R.id.question_textView);

        nextButton.setOnClickListener(this);
        prevButton.setOnClickListener(this);
        trueButton.setOnClickListener(this);
        falseButton.setOnClickListener(this);

        scoreTextView.setText(MessageFormat.format("Current Score: {0}", String.valueOf(score.getScore())));
        highestScoreTextView.setText(MessageFormat.format("Highest Score: {0}", String.valueOf(prefs.getHighScore())));

        // get previous state
        currentQuestionIndex = prefs.getState();

//        // Get data back from shared preferences
//        SharedPreferences getShareData = getSharedPreferences(MESSAGE_ID, MODE_PRIVATE);
//        int value = getShareData.getInt("message", 0);

        questionList = new QuestionBank().getQuestions(questionArrayList -> {
            questionTextView.setText(questionArrayList.get(currentQuestionIndex).getAnswer());
            questionCounterTextView.setText(MessageFormat.format("{0} / {1}", currentQuestionIndex + 1, questionArrayList.size()));
            Log.d("Inside", "processFinished: " + questionArrayList);
        });

//        Log.d("Main", "onCreate: " + questionList);
//        new QuestionBank().getQuestions();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.score_textView), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.prev_button) {
            if (currentQuestionIndex > 0) {
                currentQuestionIndex = (currentQuestionIndex - 1 + questionList.size()) % questionList.size();
                updateQuestion();
            }
        } else if (id == R.id.next_button) {
            goNext();
        } else if (id == R.id.true_button) {
            checkAnswer(true);
            updateQuestion(); // update the UI
        } else if (id == R.id.false_button) {
            checkAnswer(false);
            updateQuestion(); // update the UI
        }
    }

    private void checkAnswer(boolean userChooseCorrect) {
        boolean answerIsTrue = questionList.get(currentQuestionIndex).isAnswerTrue();
        int toastMessageResId =  R.string.correct_answer;

        // TODO: should probably only update score if question
        //  not already answered, higherst score should be updated immediately
        if (userChooseCorrect != answerIsTrue) {
            deductPoints();
            shakeAnimation();
            toastMessageResId = R.string.wrong_answer;
        } else {
           addPoints();
            fadeView();
        }

        Toast.makeText(this, toastMessageResId, Toast.LENGTH_SHORT).show();
    }

    private void updateQuestion() { // helps update the UI
        Log.d("Current", "onClick: " + currentQuestionIndex);
        String question = questionList.get(currentQuestionIndex).getAnswer();
        questionTextView.setText(question);
        questionCounterTextView.setText(MessageFormat.format("{0} / {1}", currentQuestionIndex + 1, questionList.size()));
    }


    private void addPoints() {
        scoreCounter += 100;
        score.setScore(scoreCounter);
        scoreTextView.setText(MessageFormat.format("Current Score: {0}", String.valueOf(score.getScore())));
        Log.d("Score", "addPoints: " + score.getScore());
    }

    private void deductPoints() {
        scoreCounter -= 100;
        if (scoreCounter > 0) {
            score.setScore(scoreCounter);
            scoreTextView.setText(MessageFormat.format("Current Score: {0}", String.valueOf(score.getScore())));
        } else {
            scoreCounter = 0;
            score.setScore(scoreCounter);
            scoreTextView.setText(MessageFormat.format("Current Score: {0}", String.valueOf(score.getScore())));
        }

        Log.d("Score", "deductPoints: " + score.getScore());
    }

    private void shakeAnimation() {
        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake_animation);
        CardView cardView = findViewById(R.id.cardView);
        cardView.setAnimation(shake);

        shake.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cardView.setCardBackgroundColor(Color.RED);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cardView.setCardBackgroundColor(Color.WHITE);
               goNext();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void fadeView() {
        CardView cardView = findViewById(R.id.cardView);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
        alphaAnimation.setDuration(350);
        alphaAnimation.setRepeatCount(1);
        alphaAnimation.setRepeatMode(Animation.REVERSE);

        cardView.setAnimation(alphaAnimation);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cardView.setCardBackgroundColor(Color.GREEN);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cardView.setCardBackgroundColor(Color.WHITE);
              goNext();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void goNext() {
        currentQuestionIndex = (currentQuestionIndex + 1) % questionList.size();
        updateQuestion();
    }

    @Override
    protected void onPause() {
        prefs.saveHighScore(score.getScore());
        prefs.setState(currentQuestionIndex);
        super.onPause();
    }
}