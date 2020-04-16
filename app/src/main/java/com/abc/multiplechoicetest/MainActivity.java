package com.abc.multiplechoicetest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    Button startQuiz;
    private static final int REQUEST_CODE_QUIZ=1;
    public static final String EXTRA_DIFFICULTY="extraDifficulty";

    public static final String EXTRA_CATEGORY_ID="extracateID";
    public static final String EXTRA_CATEGORY_NAME="extracateName";


    private static final String SHARED_PRES="sharedpres";
    private static final String KEY_HIGH_SCORE="keyhighscore";

    private TextView textViewHighscore;
    private int highscore;
    private Spinner spinnerDifficulty;
    private  Spinner spinnerCate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startQuiz=findViewById(R.id.startquize);
        textViewHighscore=findViewById(R.id.score);
        spinnerDifficulty=findViewById(R.id.spinner);
        spinnerCate=findViewById(R.id.spinner_category);

//        String[] difficultyLevels=Question.getAllDifficultyLevels();
//
//        ArrayAdapter<String> adapterdifficulty=new ArrayAdapter<>(this,
//                android.R.layout.simple_spinner_item,difficultyLevels);
//        adapterdifficulty.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinnerDifficulty.setAdapter(adapterdifficulty);

            loadCategories();
            loadDifficultyLevels();
                loadHighScore();


        startQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StartQuize();
            }
        });
    }
    private void StartQuize(){
        Category selectedCategory=(Category) spinnerCate.getSelectedItem();
        int categoryID=selectedCategory.getId();
        String categoryName=selectedCategory.getName();
        String difficulty=spinnerDifficulty.getSelectedItem().toString();

        Intent intent=new Intent(MainActivity.this,QuestionScreen.class);
       intent.putExtra(EXTRA_CATEGORY_ID,categoryID);
       intent.putExtra(EXTRA_CATEGORY_NAME,categoryName);
        intent.putExtra(EXTRA_DIFFICULTY,difficulty);
        startActivityForResult(intent,REQUEST_CODE_QUIZ);
        //startActivityForResult(intent,REQUEST_CODE_QUIZ);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_CODE_QUIZ){
            if(resultCode==RESULT_OK){
                int score=data.getIntExtra(QuestionScreen.EXTRA_SCORE,0);
                if(score > highscore){
                    updateHighScore(score);
                }
            }
        }
    }

    private  void loadCategories(){
        QuizDBHelper dbHelper=QuizDBHelper.getInstance(this);
        List<Category> categoryList=dbHelper.getAllCategories();

    ArrayAdapter<Category> categoryArrayAdapter =new ArrayAdapter<>(this,
            android.R.layout.simple_spinner_item,categoryList);
    categoryArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    spinnerCate.setAdapter(categoryArrayAdapter);
    }

    private void loadDifficultyLevels(){
        String[] difficultyLevels=Question.getAllDifficultyLevels();

        ArrayAdapter<String> adapterdifficulty=new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,difficultyLevels);
        adapterdifficulty.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDifficulty.setAdapter(adapterdifficulty);


    }

    private void loadHighScore(){
        SharedPreferences prefs =getSharedPreferences(SHARED_PRES,MODE_PRIVATE);
            highscore=prefs.getInt(KEY_HIGH_SCORE,0);
            textViewHighscore.setText("HighScore " + highscore);

    }
    private  void updateHighScore(int highscorenew){
        highscore=highscorenew;
        textViewHighscore.setText("HighScore :"+ highscore);
        SharedPreferences prefs =getSharedPreferences(SHARED_PRES,MODE_PRIVATE);
        SharedPreferences.Editor editor=prefs.edit();
        editor.putInt(KEY_HIGH_SCORE,highscore);
        editor.apply();
    }
}
