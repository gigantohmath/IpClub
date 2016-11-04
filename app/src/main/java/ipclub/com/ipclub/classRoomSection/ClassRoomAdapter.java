package ipclub.com.ipclub.classRoomSection;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ipclub.com.ipclub.R;


public class ClassRoomAdapter extends RecyclerView.Adapter<ClassRoomAdapter.ClassRoomViewHolder> {
    private static List<ClassRoomItem> classRoomItems;
    private static Context context;

    public static class ClassRoomViewHolder extends RecyclerView.ViewHolder {
        TextView classRoomTitle,lessonTitle;

        public ClassRoomViewHolder(View view) {
            super(view);

            this.classRoomTitle = (TextView) view.findViewById(R.id.classRoomTitle);
            this.lessonTitle = (TextView) view.findViewById(R.id.lessonTitle);
        }


    }

    public ClassRoomAdapter(List<ClassRoomItem> classRoomItems) {
        this.classRoomItems = classRoomItems;
    }

    @Override
    public ClassRoomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.classroom_single_row, parent, false);

        ClassRoomViewHolder vh = new ClassRoomViewHolder(v);
        context = parent.getContext();
        return vh;
    }


    @Override
    public void onBindViewHolder(ClassRoomViewHolder holder, int position) {
        holder.classRoomTitle.setText(classRoomItems.get(position).title);
        holder.lessonTitle.setText(classRoomItems.get(position).lessonTitle);
    }

    @Override
    public int getItemCount() {
        return classRoomItems.size();
    }




}
