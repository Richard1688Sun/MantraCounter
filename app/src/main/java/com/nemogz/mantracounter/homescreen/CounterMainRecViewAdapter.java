package com.nemogz.mantracounter.homescreen;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.nemogz.mantracounter.HomeScreenActivity;
import com.nemogz.mantracounter.MainActivity;
import com.nemogz.mantracounter.R;
import com.nemogz.mantracounter.counterStuff.Counter;

import java.util.ArrayList;
import java.util.List;

public class CounterMainRecViewAdapter extends RecyclerView.Adapter<CounterMainRecViewAdapter.ViewHolder>{

    private List<Counter> counters = new ArrayList<>();
    private Context context;

    public CounterMainRecViewAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //attachToRoot is redundant if we pass true
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.counter_list_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        //TODo set the other stuff
        holder.mantraName.setText(counters.get(position).getName());
        holder.mantraCount.setText(counters.get(position).getCount().toString());

        holder.mantraView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, counters.get(position).getName() + " Selected", Toast.LENGTH_SHORT).show();
                Intent counterScreenIntent = new Intent(context, MainActivity.class);
                context.startActivity(counterScreenIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return counters.size();
    }

    //generates the view objects(counterListItem)
    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView mantraName;
        private TextView mantraCount;
        private CardView mantraView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mantraName = itemView.findViewById(R.id.mantraNameItem);
            mantraCount = itemView.findViewById(R.id.mantraCount);
            mantraView = itemView.findViewById(R.id.mantraItem);
        }
    }

    public void setCounters(List<Counter> counters) {
        this.counters = counters;
        //tells adapter that the data has been changed
        notifyDataSetChanged();
    }
}
