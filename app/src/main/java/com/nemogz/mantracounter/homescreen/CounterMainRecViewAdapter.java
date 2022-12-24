package com.nemogz.mantracounter.homescreen;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.nemogz.mantracounter.counterStuff.LittleHouse;
import com.nemogz.mantracounter.counterStuff.MasterCounter;
import com.nemogz.mantracounter.dataStorage.MasterCounterDatabase;

import java.util.ArrayList;
import java.util.List;

public class CounterMainRecViewAdapter extends RecyclerView.Adapter<CounterMainRecViewAdapter.ViewHolder>{

    private MasterCounter masterCounter = new MasterCounter(0);
    private Context context;
    public MasterCounterDatabase db;

    public CounterMainRecViewAdapter(Context context) {
        this.context = context;
        db = MasterCounterDatabase.getINSTANCE(context);
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
        if (position != getItemCount() - 1) {
            holder.mantraName.setVisibility(View.VISIBLE);
            holder.mantraCount.setVisibility(View.VISIBLE);
            holder.addImageView.setVisibility(View.GONE);
            holder.mantraName.setText(masterCounter.getCounters().get(position).getName());
            holder.mantraCount.setText(masterCounter.getCounters().get(position).getCount().toString());

            holder.mantraView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, masterCounter.getCounters().get(position).getName() + " Selected", Toast.LENGTH_SHORT).show();
                    Intent counterScreenIntent = new Intent(context, MainActivity.class);
                    db.masterCounterDAO().insertCounterPosition(new MasterCounter(position));
                    context.startActivity(counterScreenIntent);
                }
            });
        }
        else {
            holder.mantraName.setVisibility(View.GONE);
            holder.mantraCount.setVisibility(View.GONE);
            holder.addImageView.setVisibility(View.VISIBLE);

            holder.addImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "making new counter", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return masterCounter.getCounters().size() + 1;
    }

    //generates the view objects(counterListItem)
    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView mantraName;
        private TextView mantraCount;
        private CardView mantraView;
        private ImageView addImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mantraName = itemView.findViewById(R.id.mantraNameItem);
            mantraCount = itemView.findViewById(R.id.mantraCount);
            mantraView = itemView.findViewById(R.id.mantraItem);
            addImageView = itemView.findViewById(R.id.addImageView);
        }
    }

    public void setMasterCounters(MasterCounter masterCounter) {
        this.masterCounter = masterCounter;
        //tells adapter that the data has been changed
        notifyDataSetChanged();
    }
}
