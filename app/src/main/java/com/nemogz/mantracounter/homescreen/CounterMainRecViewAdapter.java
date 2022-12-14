package com.nemogz.mantracounter.homescreen;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.nemogz.mantracounter.HomeScreenActivity;
import com.nemogz.mantracounter.MainActivity;
import com.nemogz.mantracounter.R;
import com.nemogz.mantracounter.counterStuff.Counter;
import com.nemogz.mantracounter.counterStuff.MasterCounter;
import com.nemogz.mantracounter.dataStorage.MasterCounterDatabase;
import com.nemogz.mantracounter.settings.SettingsDataClass;

import java.util.List;

public class CounterMainRecViewAdapter extends RecyclerView.Adapter<CounterMainRecViewAdapter.ViewHolder>{

    private MasterCounter masterCounter;
    private Context context;
    public MasterCounterDatabase db;
    private HomeScreenActivity homeScreenActivity;

    public CounterMainRecViewAdapter(Context context, HomeScreenActivity homeScreenActivity) {
        this.context = context;
        this.homeScreenActivity = homeScreenActivity;
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
        SettingsDataClass settingsDataClass = db.masterCounterDAO().getSettingsData();

        //if trash button setting is on
        if (settingsDataClass.isHomeSelectTrash() && position > 3 && position != getItemCount() -1) {
            holder.deleteCounterButton.setVisibility(View.VISIBLE);
            holder.deleteCounterButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Counter counterToRemove = masterCounter.getCounters().remove(position);
                    if (masterCounter.getPositionCounters() == position) {
                        masterCounter.setPositionCounters(position - 1);
                        db.masterCounterDAO().insertMasterCounter(masterCounter);
                    }
                    masterCounter.deleteCounter(counterToRemove.getOriginalName());
                    db.masterCounterDAO().deleteCounter(counterToRemove);
                    notifyItemRemoved(position);
                    notifyItemChanged(position);
                }


            });
        }
        else {
            holder.deleteCounterButton.setVisibility(View.GONE);
        }

        //normal counter cards
        if (position != getItemCount() - 1) {
            holder.mantraName.setVisibility(View.VISIBLE);
            holder.mantraCount.setVisibility(View.VISIBLE);
            holder.addImageView.setVisibility(View.GONE);
            holder.mantraName.setText(masterCounter.getCounters().get(position).getDisplayName());
            holder.mantraCount.setText(masterCounter.getCounters().get(position).getCount().toString());

            holder.mantraView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    masterCounter.setPositionCounters(position);
                    db.masterCounterDAO().insertMasterCounter(masterCounter);
                    Intent counterScreenIntent = new Intent(context, MainActivity.class);
                    context.startActivity(counterScreenIntent);
                }
            });

            holder.mantraView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    openChangeCounterPrompt(CounterMainRecViewAdapter.this, position, context);
                    return true;
                }
            });
        }
        else {  //the last counter card(adding new card)
            holder.mantraName.setVisibility(View.GONE);
            holder.mantraCount.setVisibility(View.GONE);
            holder.addImageView.setVisibility(View.VISIBLE);

            holder.mantraView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openNewCounterPrompt(CounterMainRecViewAdapter.this);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return masterCounter.getCounters().size() + 1;
    }



    private void openNewCounterPrompt(CounterMainRecViewAdapter adapter) {
        NewCounterPrompt newCounterPrompt = new NewCounterPrompt(adapter);
        newCounterPrompt.show(((AppCompatActivity)context).getSupportFragmentManager(), "test");
    }

    private void openChangeCounterPrompt(CounterMainRecViewAdapter adapter, int position, Context context) {
        ChangeCounterPrompt changeCounterPrompt = new ChangeCounterPrompt(adapter, position, homeScreenActivity, context);
        changeCounterPrompt.show(((AppCompatActivity)context).getSupportFragmentManager(), "test");
    }

    //generates the view objects(counterListItem)
    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView mantraName;
        private TextView mantraCount;
        private CardView mantraView;
        private ImageView addImageView;
        private FloatingActionButton deleteCounterButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mantraName = itemView.findViewById(R.id.mantraNameItem);
            mantraCount = itemView.findViewById(R.id.mantraCount);
            mantraView = itemView.findViewById(R.id.mantraItem);
            addImageView = itemView.findViewById(R.id.addImageView);
            deleteCounterButton = itemView.findViewById(R.id.removeButtonCounterHome);
        }
    }

    public void setMasterCounters(MasterCounter masterCounter) {
        this.masterCounter = masterCounter;
        //tells adapter that the data has been changed
        notifyDataSetChanged();
    }

    public void setCounters(List<Counter> counters) {
        this.masterCounter.setCounters(counters);
    }



}
