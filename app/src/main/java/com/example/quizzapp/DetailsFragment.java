package com.example.quizzapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.util.List;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsFragment extends Fragment implements View.OnClickListener {
    private NavController navController;
    private QuizListViewModel quizListModel;
    private int position;

    private ImageView detailsImage;
    private TextView detailsTitle;
    private TextView detailsDesc;
    private TextView detailsDiff;
    private TextView detailsQuestions;
    private Button detailsStartButton;
    private String quizId;
    private long totalQuestions = 0;


    public DetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        position = DetailsFragmentArgs.fromBundle(getArguments()).getPosition();

        //Initialize UI Elements
        detailsImage = view.findViewById(R.id.fragment_details_image);
        detailsTitle = view.findViewById(R.id.fragment_details_title);
        detailsDesc = view.findViewById(R.id.fragment_details_description);
        detailsDiff = view.findViewById(R.id.fragment_details_difficulty_text);
        detailsQuestions = view.findViewById(R.id.fragment_details_question_text);
        detailsStartButton = view.findViewById(R.id.fragment_details_start_button);
        detailsStartButton.setOnClickListener(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        quizListModel = new ViewModelProvider(Objects.requireNonNull(getActivity())).get(QuizListViewModel.class);
        quizListModel.getQuizListModelData().observe(getViewLifecycleOwner(), new Observer<List<QuizListModel>>() {
            @Override
            public void onChanged(List<QuizListModel> quizListModels) {
                Glide.with(getContext()).load(quizListModels.get(position).getImage()).centerCrop().placeholder(R.drawable.placeholder_image).into(detailsImage);
                detailsTitle.setText(quizListModels.get(position).getName());
                detailsDesc.setText(quizListModels.get(position).getDescription());
                detailsDiff.setText(quizListModels.get(position).getLevel());
                detailsQuestions.setText(quizListModels.get(position).getQuestions() + "");

                //Assign value to quizId variable
                quizId = quizListModels.get(position).getUiz_id();
                totalQuestions = quizListModels.get(position).getQuestions();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_details_start_button:
                DetailsFragmentDirections.ActionDetailsFragmentToQuizFragment action = DetailsFragmentDirections.actionDetailsFragmentToQuizFragment();
                action.setTotalQuestions(totalQuestions);
                action.setQuizId(quizId);
                navController.navigate(action);
                break;
        }
    }
}
