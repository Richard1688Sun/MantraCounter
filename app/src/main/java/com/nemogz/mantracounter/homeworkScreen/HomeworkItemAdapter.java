package com.nemogz.mantracounter.homeworkScreen;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.nemogz.mantracounter.R;
import com.nemogz.mantracounter.counterStuff.Counter;
import com.nemogz.mantracounter.counterStuff.MasterCounter;
import com.nemogz.mantracounter.dataStorage.MasterCounterDatabase;
import com.nemogz.mantracounter.dataStorage.OnDataChangedListener;
import com.nemogz.mantracounter.settings.SettingsDataClass;
import com.nemogz.mantracounter.settings.SettingsOptionsRecViewAdapter;

import java.util.List;

public class HomeworkItemAdapter extends RecyclerView.Adapter<HomeworkItemAdapter.ViewHolder> {

    private MasterCounter masterCounter;
    private Context context;
    public MasterCounterDatabase db;
    private OnDataChangedListener onDataChangedListener;

    public HomeworkItemAdapter(Context context, OnDataChangedListener onDataChangedListener) {
        this.context = context;
        this.db = MasterCounterDatabase.getINSTANCE(context);
        this.masterCounter = new MasterCounter(context);
        this.onDataChangedListener = onDataChangedListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.homework_counter_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.homeworkText.setText(masterCounter.getCounters().get(position).getDisplayName());
        holder.homeworkCount.setText(masterCounter.getCounters().get(position).getHomeworkAmount().toString());

        int missingHW = masterCounter.getCounters().get(position).homeworkAmountMissing();
        if (missingHW != 0) {
            holder.homeworkMissingText.setVisibility(View.VISIBLE);
            String str = context.getString(R.string.missing) + " " + missingHW;
            holder.homeworkMissingText.setText(str);
        }
        else {
            holder.homeworkMissingText.setVisibility(View.GONE);
        }

        holder.homeworkCount.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                String unconverted = holder.homeworkCount.getText().toString();
                if (unconverted.equals("")) {
                    masterCounter.getCounters().get(position).setHomeworkAmount(0);
                    db.masterCounterDAO().updateCounter(masterCounter.getCounters().get(position));
                    onDataChangedListener.onDataChanged();
                    notifyItemChanged(position);
                }
                else {
                    masterCounter.getCounters().get(position).setHomeworkAmount(Integer.parseInt(unconverted));
                    db.masterCounterDAO().updateCounter(masterCounter.getCounters().get(position));
                    onDataChangedListener.onDataChanged();
                    notifyItemChanged(position);
                }
                return false;
            }
        });
    }


    @Override
    public int getItemCount() {
        return masterCounter.getCounters().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView homeworkText;
        EditText homeworkCount;
        TextView homeworkMissingText;
        CardView homeworkItemCardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            homeworkText = itemView.findViewById(R.id.homeworkCounterNameItem);
            homeworkCount = itemView.findViewById(R.id.homeworkCounterAmountItem);
            homeworkMissingText = itemView.findViewById(R.id.homeworkWarning);
            homeworkItemCardView = itemView.findViewById(R.id.homeworkItemCardView);
        }
    }

    public MasterCounter getMasterCounter() {
        return masterCounter;
    }

    public void setMasterCounter(MasterCounter masterCounter) {
        this.masterCounter = masterCounter;
    }
}
