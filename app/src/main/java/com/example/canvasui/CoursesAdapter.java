package com.example.canvasui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.canvasui.api.models.Course;

public class CoursesAdapter extends RecyclerView.Adapter<CoursesAdapter.ViewHolder> {
    Course[] courses;
    public CoursesAdapter(Course[] list){
        courses = list;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.courses_card_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Course course = courses[position];
        TextView courseName = holder.itemView.findViewById(R.id.course_name);
        TextView courseDescription = holder.itemView.findViewById(R.id.course_description);
        courseName.setText(course.name);
        courseDescription.setText(R.string.lorem_ipsum);
    }


    @Override
    public int getItemCount() {
        return courses.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
