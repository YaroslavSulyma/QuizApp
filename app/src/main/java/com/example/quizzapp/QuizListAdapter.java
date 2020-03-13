package com.example.quizzapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class QuizListAdapter extends RecyclerView.Adapter<QuizListAdapter.QuizViewHolder> {

    private List<QuizListModel> quizListModels;

    public void setQuizListModels(List<QuizListModel> quizListModels) {
        this.quizListModels = quizListModels;
    }

    @NonNull
    @Override
    public QuizViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_list_item, parent, false);
        return new QuizViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuizViewHolder holder, int position) {
        holder.listTitle.setText(quizListModels.get(position).getName());
    }

    @Override
    public int getItemCount() {
        if (quizListModels == null) {
            return 0;
        } else {
            return quizListModels.size();
        }
    }

    public class QuizViewHolder extends RecyclerView.ViewHolder {

        private ImageView listImage;
        private TextView listTitle;
        private TextView lestDescription;
        private TextView listLevel;
        private Button listButton;

        public QuizViewHolder(@NonNull View itemView) {
            super(itemView);

            listImage = itemView.findViewById(R.id.fragment_list_image);
            listTitle = itemView.findViewById(R.id.fragment_list_title);
            lestDescription = itemView.findViewById(R.id.fragment_list_description);
            listLevel = itemView.findViewById(R.id.fragment_list_difficulty);
            listButton = itemView.findViewById(R.id.fragment_list_button);
            listImage = itemView.findViewById(R.id.fragment_list_image);
        }
    }
}
