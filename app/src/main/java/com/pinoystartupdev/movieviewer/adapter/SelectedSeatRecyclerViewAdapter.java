package com.pinoystartupdev.movieviewer.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.pinoystartupdev.movieviewer.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectedSeatRecyclerViewAdapter extends RecyclerView.Adapter<SelectedSeatRecyclerViewAdapter.ViewHolder>{
    private List<Pair<Integer, String>> selectedSeatNumberList;
    LayoutInflater mInflater;

    public SelectedSeatRecyclerViewAdapter(Context context, List<Pair<Integer, String>> selectedSeatNumberList){
        this.selectedSeatNumberList = selectedSeatNumberList;
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.textViewSeatNumber)
        TextView textViewProductName;

        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

        public void set(int position) {
            textViewProductName.setText(selectedSeatNumberList.get(position).second);
        }
    }

    @Override
    public SelectedSeatRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.selected_seat_item, parent, false);
        return new SelectedSeatRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SelectedSeatRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.set(position);
    }

    @Override
    public int getItemCount() {
        return selectedSeatNumberList.size();
    }
}
