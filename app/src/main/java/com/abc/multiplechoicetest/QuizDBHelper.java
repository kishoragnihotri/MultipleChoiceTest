package com.abc.multiplechoicetest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.abc.multiplechoicetest.QuizContract.*;

import java.util.ArrayList;
import java.util.List;
//import com.abc.multiplechoicetest.Question.*;

public class QuizDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME="MyQuiz.db";
    private static final int DATABSE_VERSION=1;

private static QuizDBHelper instance;

    private SQLiteDatabase db;
//private  QuizDBHelper(Context context){
//    super(context,DATABASE_NAME,null,DATABSE_VERSION);
//}

    private QuizDBHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABSE_VERSION);
    }
    public  static synchronized  QuizDBHelper getInstance(Context context){
        if(instance==null){
           instance=new QuizDBHelper(context.getApplicationContext());
        }
return instance;

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
                    this.db=db;
                    final String SQL_CREATE_CATEGORIES_TABLE="CREATE TABLE " +
                            CategoriesTable.TABLE_NAME + "( " +
                            CategoriesTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            CategoriesTable.COLUMN_NAME + " TEXT " + ")";


                    final String SQL_CREATE_QUESTIONS_TABLE =" CREATE TABLE " +
                            QuizContract.QuestionTable.TABLE_NAME + " ( "
                            + QuestionTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            QuestionTable.COLUMN_QUESTION + " TEXT, " +
                            QuestionTable.COLUMN_OPTION1 + " TEXT, " +
                            QuestionTable.COLUMN_OPTION2 + " TEXT, " +
                            QuestionTable.COLUMN_OPTION3 + " TEXT, " +
                            QuestionTable.COLUMN_OPTION4 + " TEXT, " +
                            QuestionTable.COLUMN_ANSWER_NR + " INTEGER, " +
                            QuestionTable.COLUMN_DEFFICULTY + " TEXT, " +
                            QuestionTable.COLUMN_CATAGORIES_ID + " INTEGER, " +
                            "FOREIGN KEY (" + QuestionTable.COLUMN_CATAGORIES_ID
                            + ") REFERENCES "
                            + CategoriesTable.TABLE_NAME + "(" + CategoriesTable._ID +
                            ")" + "ON DELETE CASCADE" +
                            ")" ;
                    db.execSQL(SQL_CREATE_CATEGORIES_TABLE);
                    db.execSQL(SQL_CREATE_QUESTIONS_TABLE);

                    fillCategoriesTable();
            fillQuestionTable();

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CategoriesTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + QuestionTable.TABLE_NAME);

        onCreate(db);
       }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }
    private void fillCategoriesTable(){
        Category c1=new Category("Programming");
        insertCategory(c1);
        Category c2=new Category("DescriteMath");
        insertCategory(c2);
        Category c3=new Category("CurrentAffair");
        insertCategory(c3);
    }
public void addcategory(Category category){
        db=getWritableDatabase();
        insertCategory(category);
}
public void addCategories(List<Category>categories){
        db=getWritableDatabase();
        for(Category category : categories){
            insertCategory(category);
        }
}
    private void insertCategory(Category category){
        ContentValues contentValues=new ContentValues();
        contentValues.put(CategoriesTable.COLUMN_NAME,category.getName());
        db.insert(CategoriesTable.TABLE_NAME,null,contentValues);
    }

    private void fillQuestionTable(){
        Question q1=new Question("Programming Easy:  A is Correct",
                "A","B","C",
                "D",1,Question.DIFFICULTY_EASY,Category.PROGRAMMING);
        insertQuestion(q1);
           Question q2=new Question("programming,B is Correct",
                   "A","B","C",
                   "D",2,Question.DIFFICULTY_EASY,Category.PROGRAMMING);
           insertQuestion(q2);
           Question q3=new Question("Math C is Correct",
                   "A","B","C",
                   "D",3,Question.DIFFICULTY_MEDIUM,Category.DESCRITEMATH);
           insertQuestion(q3);
           Question q4=new Question("Math D is Correct",
                   "A","B","C",
                   "D",4,Question.DIFFICULTY_MEDIUM,Category.DESCRITEMATH);
           insertQuestion(q4);
           Question q5=new Question("CurrentAffair, B is Correct",
                   "A","B","C",
                   "D",2,Question.DIFFICULTY_HARD,Category.CURRENTFFAIR);
           insertQuestion(q5);

           }

           public void addQuestion(Question question){
        db=getWritableDatabase();
        insertQuestion(question);


           }

           public void addQuestions(List<Question> questions){
        db=getWritableDatabase();
        for(Question question : questions){
            insertQuestion(question);
        }
           }
    private void insertQuestion(Question question){
        ContentValues contentValues=new ContentValues();
        contentValues.put(QuestionTable.COLUMN_QUESTION,question.getQuestion());
        contentValues.put(QuestionTable.COLUMN_OPTION1,question.getOption1());
        contentValues.put(QuestionTable.COLUMN_OPTION2,question.getOption2());
        contentValues.put(QuestionTable.COLUMN_OPTION3,question.getOption3());
        contentValues.put(QuestionTable.COLUMN_OPTION4,question.getOption4());
        contentValues.put(QuestionTable.COLUMN_ANSWER_NR,question.getAnswer());
        contentValues.put(QuestionTable.COLUMN_DEFFICULTY,question.getDifficulty());
        contentValues.put(QuestionTable.COLUMN_CATAGORIES_ID,question.getCategoryID());
        db.insert(QuestionTable.TABLE_NAME,null,contentValues);



    }
    public List<Category>getAllCategories(){
        List<Category> categoryList=new ArrayList<>();
        db=getReadableDatabase();
        Cursor c=db.rawQuery("SELECT * FROM "+ CategoriesTable.TABLE_NAME,null);
        if(c.moveToFirst()){
            do{
                Category category=new Category();
                category.setId(c.getInt(c.getColumnIndex(CategoriesTable._ID)));
                category.setName(c.getString(c.getColumnIndex(CategoriesTable.COLUMN_NAME)));
                categoryList.add(category);
            }while(c.moveToNext());
        }
        c.close();
        return  categoryList;

    }
    public ArrayList<Question> getAllQuestion(){

        ArrayList<Question> questions=new ArrayList<>();
        db=getReadableDatabase();
        Cursor c=db.rawQuery(" SELECT *FROM " + QuestionTable.TABLE_NAME, null);

        if(c.moveToFirst()){
            do{
                Question question=new Question();
                question.setId(c.getColumnIndex(QuestionTable._ID));
                question.setQuestion(c.getString(c.getColumnIndex(QuestionTable.COLUMN_QUESTION)));
                question.setOption1(c.getString(c.getColumnIndex(QuestionTable.COLUMN_OPTION1)));
                question.setOption2(c.getString(c.getColumnIndex(QuestionTable.COLUMN_OPTION2)));
                question.setOption3(c.getString(c.getColumnIndex(QuestionTable.COLUMN_OPTION3)));
                question.setOption4(c.getString(c.getColumnIndex(QuestionTable.COLUMN_OPTION4)));
                question.setAnswer(c.getInt(c.getColumnIndex(QuestionTable.COLUMN_ANSWER_NR)));
                question.setDifficulty(c.getString(c.getColumnIndex(QuestionTable.COLUMN_DEFFICULTY)));
                question.setCategoryID(c.getInt(c.getColumnIndex(QuestionTable.COLUMN_CATAGORIES_ID)));
                questions.add(question);
            }while (c.moveToNext());
        }
c.close();
        return questions;
    }

    public ArrayList<Question> getQuestion(int categoryID ,String difficulty){

        ArrayList<Question> questions=new ArrayList<>();
        db=getReadableDatabase();
        //String []selectionArgs=new String[]{difficulty};
        String selection=QuestionTable.COLUMN_CATAGORIES_ID + " = ? " +
                " AND " + QuestionTable.COLUMN_DEFFICULTY + " = ? ";
        String [] selectionargs=new String[]{String.valueOf(categoryID),difficulty};
         Cursor c=db.query(
                 QuestionTable.TABLE_NAME,
                 null,
                 selection,
                 selectionargs,
                 null,
                 null,
                 null
         );

//        Cursor c=db.rawQuery(" SELECT * FROM " + QuestionTable.TABLE_NAME +
//                " WHERE " + QuestionTable.COLUMN_DEFFICULTY + " = ? ", selectionArgs);

        if(c.moveToFirst()){
            do{
                Question question=new Question();
                question.setId(c.getColumnIndex(QuestionTable._ID));
                question.setQuestion(c.getString(c.getColumnIndex(QuestionTable.COLUMN_QUESTION)));
                question.setOption1(c.getString(c.getColumnIndex(QuestionTable.COLUMN_OPTION1)));
                question.setOption2(c.getString(c.getColumnIndex(QuestionTable.COLUMN_OPTION2)));
                question.setOption3(c.getString(c.getColumnIndex(QuestionTable.COLUMN_OPTION3)));
                question.setOption4(c.getString(c.getColumnIndex(QuestionTable.COLUMN_OPTION4)));
                question.setAnswer(c.getInt(c.getColumnIndex(QuestionTable.COLUMN_ANSWER_NR)));
                question.setDifficulty(c.getString(c.getColumnIndex(QuestionTable.COLUMN_DEFFICULTY)));
                question.setCategoryID(c.getInt(c.getColumnIndex(QuestionTable.COLUMN_CATAGORIES_ID)));
                questions.add(question);
            }while (c.moveToNext());
        }
        c.close();
        return questions;
    }



}
