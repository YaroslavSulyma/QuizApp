package com.example.quizzapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class QuizFragment extends Fragment {

    //Declare
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private String quizId;

    //UI
    private TextView quizTitle;
    private String TAG = "QUIZ FRAGMENT LOG";

    //Firebase Data
    private List<QuestionsModel> allQuestions = new ArrayList<>();
    private long totalQuestionToAnswer = 10;
    private List<QuestionsModel> questionsToAnswer = new ArrayList<>();

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
        //Initialize
        firebaseFirestore = FirebaseFirestore.getInstance();

        //Get quizId
        quizId = QuizFragmentArgs.fromBundle(getArguments()).getQuizId();
        totalQuestionToAnswer = QuizFragmentArgs.fromBundle(getArguments()).getTotalQuestions();

        //Get all questions
        firebaseFirestore.collection("QuizList").document(quizId).
                collection("Questions").get().
                addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            allQuestions = task.getResult().toObjects(QuestionsModel.class);
                            Log.d(TAG, "Question list: " + allQuestions.get(0).getQuestion());

                            //pick questions
                            pickQuestions();
                        } else {
                            //Error getting Questions
                            quizTitle.setText(R.string.error_loading_data);
                        }
                    }
                });
    }

    private void pickQuestions() {
        for (int i = 0; i < totalQuestionToAnswer; i++) {
            int randomNumber = getRandomInteger(allQuestions.size(), 0);
            questionsToAnswer.add(allQuestions.get(randomNumber));
            allQuestions.remove(randomNumber);

            Log.d(TAG, "Questions: "+ i + ":" + questionsToAnswer.get(i).getQuestion());
        }
    }

    public static int getRandomInteger(int maximum, int minimum) {
        return ((int) (Math.random() * (maximum - minimum)) + minimum);
    }
}
