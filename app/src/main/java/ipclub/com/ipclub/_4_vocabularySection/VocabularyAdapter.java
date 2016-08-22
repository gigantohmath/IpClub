package ipclub.com.ipclub._4_vocabularySection;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import ipclub.com.ipclub.R;

public class VocabularyAdapter extends RecyclerView.Adapter<VocabularyAdapter.MyViewHolder> {

    private ArrayList<VocabularyItem> dataSet;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName;
        TextView textViewTrans;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.textViewName = (TextView) itemView.findViewById(R.id.textViewName);
            this.textViewTrans = (TextView) itemView.findViewById(R.id.textViewTrans);
        }
    }

    public VocabularyAdapter(ArrayList<VocabularyItem> data) {
        this.dataSet = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cards_layout, parent, false);

        //view.setOnClickListener(LoginActivity.myOnClickListener);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        TextView textViewName = holder.textViewName;
        TextView textViewTrans = holder.textViewTrans;

        textViewName.setText(dataSet.get(listPosition).title);
        textViewTrans.setText(dataSet.get(listPosition).translation);
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
