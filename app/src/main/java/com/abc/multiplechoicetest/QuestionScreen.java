package com.abc.multiplechoicetest;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class QuestionScreen extends AppCompatActivity {
    private TextView textViewQuestion,
            textViewScore,
            textViewQuestionCount,
            textViewCountDown,
            textviewDifficulty;
    private static final String KEY_SCORE="keyscore";
    private static final String KEY_QUETIONS_COUNT="keyQuestioncount";
    private static final String KEY_MILLS_LEFT="keyMillsLeft";
    private static final String KEY_ANSWERED="keyAnswered";
    private static final String KEY_QUESTION_LIST="keyQuestionList";

    private RadioGroup rbgroup;
    private RadioButton rb1, rb2, rb3, rb4;
    private Button buttonConfirm;
    private ColorStateList textcolor;
    private ColorStateList textColorDefaultcd;
    private CountDownTimer countDownTimer;
    private long timeLeftInMillis;
    private TextView textViewCategory;
    private int questioncounter;
    private int questioncounttotal;
    private Question currentques;
    private int score;
    private boolean answered;
    private long backpressed;

    public static final String EXTRA_SCORE = "extrascore";
    private static final long COUNTDOWN_IN_MILLIS = 30000;

    private ArrayList<Question> questions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_screen);
        textViewQuestion = findViewById(R.id.questoins);
        textViewScore = findViewById(R.id.Que_score);
        textViewQuestionCount = findViewById(R.id.Que_count);
        textViewCategory=findViewById(R.id.category);
        textViewCountDown = findViewById(R.id.Que_time);
        textviewDifficulty=findViewById(R.id.difficulty_count);
        rbgroup = findViewById(R.id.radiogroup);
        rb1 = findViewById(R.id.bt1);
        rb2 = findViewById(R.id.bt2);
        rb3 = findViewById(R.id.bt3);
        rb4 = findViewById(R.id.bt4);

        buttonConfirm = findViewById(R.id.confirm_quetion);
        textcolor = rb1.getTextColors();
        textColorDefaultcd = textViewCountDown.getTextColors();
        Intent intent=getIntent();
        int categoryID=intent.getIntExtra(MainActivity.EXTRA_CATEGORY_ID,0);
        String categoryName=intent.getStringExtra(MainActivity.EXTRA_CATEGORY_NAME);

        String difficulty=intent.getStringExtra(MainActivity.EXTRA_DIFFICULTY);
        textViewCategory.setText("Category: "+ categoryName);


        textviewDifficulty.setText("Difficulty : "+difficulty);

        if(savedInstanceState == null){
        QuizDBHelper dbHelper = QuizDBHelper.getInstance(this);
        questions =dbHelper.getQuestion(categoryID,difficulty);
        questioncounttotal = questions.size();
        Collections.shuffle(questions);
        showNextQuestion();}else{
            questions=savedInstanceState.getParcelableArrayList(KEY_QUESTION_LIST);
            if(questions ==null){
                finish();
            }//part8 finish
            questioncounttotal=questions.size();
            questioncounter=savedInstanceState.getInt(KEY_QUETIONS_COUNT);
            currentques=questions.get(questioncounter - 1);
            score=savedInstanceState.getInt(KEY_SCORE);
            timeLeftInMillis=savedInstanceState.getLong(KEY_MILLS_LEFT);
            answered=savedInstanceState.getBoolean(KEY_ANSWERED);
            if(!answered){
                startCountDown();

            }else{
                updateCountDownText();
                showSolution();
            }
        }

        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!answered) {
                    if (rb1.isChecked() || rb2.isChecked() || rb3.isChecked() || rb4.isChecked()) {
                        checkAnswer();
                    } else {
                        Toast.makeText(QuestionScreen.this, "Chiroote select to kar", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    showNextQuestion();
                }
            }
        });


    }

    private void showNextQuestion() {

        rb1.setTextColor(textcolor);
        rb2.setTextColor(textcolor);
        rb3.setTextColor(textcolor);
        rb4.setTextColor(textcolor);
        rbgroup.clearCheck();

        if (questioncounter < questioncounttotal) {
            currentques = questions.get(questioncounter);
            textViewQuestion.setText(currentques.getQuestion());
            rb1.setText(currentques.getOption1());
            rb2.setText(currentques.getOption2());
            rb3.setText(currentques.getOption3());
            rb4.setText(currentques.getOption4());
            questioncounter++;
            textViewQuestionCount.setText("Question: " + questioncounter + "/" + questioncounttotal);
            answered = false;
            buttonConfirm.setText("Confirm");
            timeLeftInMillis = COUNTDOWN_IN_MILLIS;
            startCountDown();


        } else {
            finishQuiz();
        }


    }

    private void startCountDown() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                timeLeftInMillis = 0;
                updateCountDownText();
                checkAnswer();
            }
        }.start();
    }

    private void updateCountDownText() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int second = (int) (timeLeftInMillis / 1000) % 60;
        String timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, second);
        textViewCountDown.setText(timeFormatted);
        if (timeLeftInMillis < 10000) {
            textViewCountDown.setTextColor(Color.RED);
        } else {
            textViewCountDown.setTextColor(textColorDefaultcd);
        }
    }

    private void checkAnswer() {

        answered = true;
        countDownTimer.cancel();

        RadioButton rbselected = findViewById(rbgroup.getCheckedRadioButtonId());
        int answerNr = rbgroup.indexOfChild(rbselected) + 1;

        if (answerNr == currentques.getAnswer()) {
            score++;
            textViewScore.setText("Score : " + score);
        }
        showSolution();


    }

    private void showSolution() {
        rb1.setTextColor(Color.RED);
        rb2.setTextColor(Color.RED);
        rb3.setTextColor(Color.RED);
        rb4.setTextColor(Color.RED);

        switch (currentques.getAnswer()) {
            case 1:
                rb1.setTextColor(Color.GREEN);
                textViewQuestion.setText("Answer 1 is corret");
                break;
            case 2:
                rb2.setTextColor(Color.GREEN);
                textViewQuestion.setText("Answer 2 is corret");
                break;
            case 3:
                rb3.setTextColor(Color.GREEN);
                textViewQuestion.setText("Answer 3 is corret");
                break;
            case 4:
                rb4.setTextColor(Color.GREEN);
                textViewQuestion.setText("Answer 4 is corret");
                break;
        }

        if (questioncounter < questioncounttotal) {

            buttonConfirm.setText("Next Quetion");
        } else {
            buttonConfirm.setText("Finish Quize");
        }
    }

    private void finishQuiz() {
        Intent intentresult = new Intent();
        intentresult.putExtra(EXTRA_SCORE, score);
        setResult(RESULT_OK, intentresult);
        finish();
    }

    @Override
    public void onBackPressed() {
        if (backpressed + 2000 > System.currentTimeMillis()) {
            finishQuiz();
        } else {
            Toast.makeText(this, "Press back again to Exit", Toast.LENGTH_SHORT).show();
        }

        backpressed = System.currentTimeMillis();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_SCORE,score);
        outState.putInt(KEY_QUETIONS_COUNT,questioncounter);
        outState.putLong(KEY_MILLS_LEFT,timeLeftInMillis);
        outState.putBoolean(KEY_ANSWERED,answered);
        outState.putParcelableArrayList(KEY_QUESTION_LIST,questions);
    }
}
