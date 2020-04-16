package com.abc.multiplechoicetest;

import android.provider.BaseColumns;

public class QuizContract {

    private QuizContract(){
        //to avoid creating object of this class
    }

    public static class CategoriesTable implements  BaseColumns{
        public static final String TABLE_NAME="QUIZE_CATEGORIES";

        public static final String COLUMN_NAME="name";
    }
    public static class QuestionTable implements BaseColumns {

        public static final String TABLE_NAME="QuestionQuize";
        public static final String COLUMN_QUESTION="question";
        public static final String COLUMN_OPTION1="option1";
        public static final String COLUMN_OPTION2="option2";
        public static final String COLUMN_OPTION3="option3";
        public static final String COLUMN_OPTION4="option4";
        public static final String COLUMN_ANSWER_NR="answer_nr";
        public static final String COLUMN_DEFFICULTY="difficulty";
        public static final String COLUMN_CATAGORIES_ID="category_id";

    }
}
