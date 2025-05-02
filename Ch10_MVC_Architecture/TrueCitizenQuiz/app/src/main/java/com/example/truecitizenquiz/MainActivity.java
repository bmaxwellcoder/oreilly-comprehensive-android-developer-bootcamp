package com.example.truecitizenquiz;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button falseButton;
    private Button trueButton;
    private ImageButton nextButton;
    private ImageButton prevButton;
    private TextView questionTextView;

    private int currentQuestionIdx = -1;

    private Question[] questionBank = new Question[]{
            new Question(R.string.question_declaration, true),
            new Question(R.string.question_amendments, false), //correct: 27
            new Question(R.string.question_constitution, true),
            new Question(R.string.question_independence_rights, true),
            new Question(R.string.question_religion, true),
            new Question(R.string.question_government, false),
            new Question(R.string.question_government_feds, false),
            new Question(R.string.question_government_senators, true),
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

//        Question question = new Question(R.string.question_declaration, true);


        falseButton = findViewById(R.id.false_button);
        trueButton = findViewById(R.id.true_button);
        nextButton = findViewById(R.id.next_button);
        prevButton = findViewById(R.id.prev_button);
        questionTextView = findViewById(R.id.answer_text_view);

//        questionTextView = findViewById(questionBank[0].getAnswerResId());

        // Another way to set a button function
        falseButton.setOnClickListener(this); // register buttons to listen to click events
        trueButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);
        prevButton.setOnClickListener(this);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.false_button:
//                Toast.makeText(MainActivity.this, "False",
//                        Toast.LENGTH_SHORT).show();
                checkAnswer(false);
                break;
            case R.id.true_button:
//                Toast.makeText(MainActivity.this, "True",
//                        Toast.LENGTH_SHORT).show();
                checkAnswer(true);
                break;
            case R.id.next_button:
                // go to next question
                currentQuestionIdx = (currentQuestionIdx + 1) % questionBank.length; // we are safe to never go out of bounds
                updateQuestion();
                break;
            case R.id.prev_button:
                // go to next question
                if (currentQuestionIdx > 0) {
                    currentQuestionIdx = (currentQuestionIdx - 1) % questionBank.length; // we are safe to never go out of bounds
                    updateQuestion();
                    break;
                }
        }
    }

    private void updateQuestion() {
        Log.d("Current", "onClick: " + currentQuestionIdx);
        questionTextView.setText(questionBank[currentQuestionIdx].getAnswerResId());
    }

    private void checkAnswer(boolean userChooseCorrect) {
        if (currentQuestionIdx < 0) {
            return;
        }
        boolean answerIsTrue = questionBank[currentQuestionIdx].isAnswerTrue();
        int toastMessageId = 0;

        if (userChooseCorrect == answerIsTrue) {
            toastMessageId = R.string.correct_answer;
        } else {
            toastMessageId = R.string.wrong_answer;
        }

        Toast.makeText(MainActivity.this, toastMessageId, Toast.LENGTH_SHORT).show();
    }
}