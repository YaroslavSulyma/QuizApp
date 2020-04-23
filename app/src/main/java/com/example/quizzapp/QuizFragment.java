package com.example.quizzapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextClock;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class QuizFragment extends Fragment implements View.OnClickListener {

    //Declare
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private String quizId;

    //UI
    private TextView quizTitle;
    private TextView questionNumber;
    private TextView questionText;
    private Button optionOneButton;
    private Button optionTwoButton;
    private Button optionThreeButton;
    private TextView questionFeedback;
    private Button nextBtn;
    private TextView questionTimer;
    private ProgressBar questionProgress;
    private String TAG = "QUIZ FRAGMENT LOG";

    private Boolean canAnswer = false;
    private int currentQuestion = 0;

    //Firebase Data
    private List<QuestionsModel> allQuestions = new ArrayList<>();
    private long totalQuestionToAnswer = 10;
    private List<QuestionsModel> questionsToAnswer = new ArrayList<>();
    private CountDownTimer countDownTimer;

    public QuizFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_quiz, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        quizTitle = view.findViewById(R.id.fragment_quiz_title);
        questionNumber = view.findViewById(R.id.fragment_quiz_question_number);
        questionText = view.findViewById(R.id.fragment_quiz_question);
        optionOneButton = view.findViewById(R.id.fragment_quiz_option_one);
        optionTwoButton = view.findViewById(R.id.fragment_quiz_option_two);
        optionThreeButton = view.findViewById(R.id.fragment_quiz_option_three);
        questionFeedback = view.findViewById(R.id.fragment_quiz_option_feedback);
        nextBtn = view.findViewById(R.id.fragment_quiz_next_button);
        questionTimer = view.findViewById(R.id.fragment_quiz_question_timer);
        questionProgress = view.findViewById(R.id.fragment_quiz_question_progress);
        //Initialize
        firebaseFirestore = FirebaseFirestore.getInstance();

        //Get quizId
        quizId = QuizFragmentArgs.fromBundle(getArguments()).getQuizId();
        totalQuestionToAnswer = QuizFragmentArgs.fromBundle(getArguments()).getTotalQuestions();

        //Get Firestore data
        firebaseFirestore.collection("QuizList").document(quizId).
                collection("Questions").get().
                addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            allQuestions = task.getResult().toObjects(QuestionsModel.class);
                            Log.d(TAG, "Question list: " + allQuestions.get(0).getQuestion());
                            pickQuestions();
                            loadUi();
                        } else {
                            //Error getting Questions
                            quizTitle.setText(R.string.error_loading_data);
                        }
                    }
                });

        //Set button click listener
        optionOneButton.setOnClickListener(this);
        optionTwoButton.setOnClickListener(this);
        optionThreeButton.setOnClickListener(this);
    }

    private void loadUi() {
        //Quiz data loaded, load Ui
        quizTitle.setText("Quiz data loaded");
        questionText.setText("Load first Question");

        //Enable Options
        enableOptions();

        //Load firs Question
        loadQuestion(1);
    }

    private void loadQuestion(int questionNumb) {
        //Set Question Number
        questionNumber.setText(questionNumb + "");
        //Load Question Text
        questionText.setText(questionsToAnswer.get(questionNumb).getQuestion());

        //Load options
        optionOneButton.setText(questionsToAnswer.get(questionNumb).getOption_a());
        optionTwoButton.setText(questionsToAnswer.get(questionNumb).getOption_b());
        optionThreeButton.setText(questionsToAnswer.get(questionNumb).getOption_c());
        Log.d(TAG, "Options:" + questionsToAnswer.get(questionNumb).getOption_a());

        //Question loaded. Set can answer
        canAnswer = true;
        currentQuestion = questionNumb;
        //Start Quiz timer
        startTimer(questionNumb);
    }

    private void startTimer(int questionNumber) {
        //Set Timer Text
        final Long timerToAnswer = questionsToAnswer.get(questionNumber).getTimer();
        questionTimer.setText(timerToAnswer.toString());

        //Start Timer ProgressBar
        questionProgress.setVisibility(View.VISIBLE);


        //Start CountDown
        countDownTimer = new CountDownTimer(timerToAnswer * 1000, 10) {

            @Override
            public void onTick(long millisUntilFinished) {
                //Update Time
                questionTimer.setText(millisUntilFinished / 1000 + "");

                //Progress in percent
                Long percent = millisUntilFinished / (timerToAnswer * 10);
                questionProgress.setProgress(percent.intValue());
            }

            @Override
            public void onFinish() {
                //time up. Can`t answer question anymore
                canAnswer = false;
            }
        };

        countDownTimer.start();
    }

    private void enableOptions() {
        optionOneButton.setVisibility(View.VISIBLE);
        optionTwoButton.setVisibility(View.VISIBLE);
        optionThreeButton.setVisibility(View.VISIBLE);

        //Enable option buttons
        optionOneButton.setEnabled(true);
        optionTwoButton.setEnabled(true);
        optionThreeButton.setEnabled(true);

        //Hide feedback an next Button
        questionFeedback.setVisibility(View.INVISIBLE);
        nextBtn.setVisibility(View.INVISIBLE);
        nextBtn.setEnabled(false);
    }

    private void pickQuestions() {
        for (int i = 0; i < totalQuestionToAnswer; i++) {
            int randomNumber = getRandomInteger(allQuestions.size(), 0);
            questionsToAnswer.add(allQuestions.get(randomNumber));
        }
    }

    public static int getRandomInteger(int maximum, int minimum) {
        return ((int) (Math.random() * (maximum - minimum)) + minimum);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_quiz_option_one:
                answerSelected(optionOneButton.getText());
                break;
            case R.id.fragment_quiz_option_two:
                break;
            case R.id.fragment_quiz_option_three:
                break;
        }
    }

    private void answerSelected(CharSequence selectedAnswer) {
        //Check answer
        if(canAnswer){
            if(questionsToAnswer.get(currentQuestion).getAnswer().equals(selectedAnswer)){
                //answer is correct
                Log.d(TAG,"CORRECT ANSWER");
            }else{
                //answer is incorrect
                Log.d(TAG,"WRONG ANSWER");
            }
            //set can answer to false
            canAnswer = false;
        }
    }
}
