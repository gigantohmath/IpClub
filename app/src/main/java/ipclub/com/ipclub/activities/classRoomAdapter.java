package ipclub.com.ipclub.activities;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ipclub.com.ipclub.R;
import ipclub.com.ipclub.contents.ClassRoomItem;

/**
 * Created by sench on 8/22/2016.
 */
public class classRoomAdapter extends RecyclerView.Adapter<classRoomAdapter.ViewHolder> {
    private List<ClassRoomItem> classRoomItems;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView classRoomTitle,lessonTitle;

        public  ViewHolder(View view) {
            super(view);
            classRoomTitle = (TextView) view.findViewById(R.id.classRoomTitle);
            lessonTitle = (TextView) view.findViewById(R.id.lessonTitle);
        }


    }

    public classRoomAdapter(List<ClassRoomItem> classRoomItems) {
        this.classRoomItems = classRoomItems;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.classroom_single_row, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.classRoomTitle.setText(classRoomItems.get(position).title);
        holder.lessonTitle.setText(classRoomItems.get(position).lessonTitle);
    }

    @Override
    public int getItemCount() {
        return classRoomItems.size();
    }




}
