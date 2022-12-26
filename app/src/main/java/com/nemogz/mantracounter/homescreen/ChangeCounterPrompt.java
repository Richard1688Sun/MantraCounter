package com.nemogz.mantracounter.homescreen;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.nemogz.mantracounter.HomeScreenActivity;
import com.nemogz.mantracounter.R;
import com.nemogz.mantracounter.counterStuff.Counter;
import com.nemogz.mantracounter.dataStorage.MasterCounterDatabase;

import java.util.ArrayList;
import java.util.List;

public class ChangeCounterPrompt extends AppCompatDialogFragment {

    private EditText mantraName;
    private EditText mantraCount;
    public MasterCounterDatabase db;
    private List<Counter> counters;
    private int position;
    private CounterMainRecViewAdapter adapter;

    public ChangeCounterPrompt(CounterMainRecViewAdapter adapter, int position) {
        this.position = position;
        this.adapter = adapter;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        createDataBase(getContext());
        counters = db.masterCounterDAO().getAllCounters();

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.new_counter_prompt, null);

        mantraName = view.findViewById(R.id.newMantraName);
        mantraCount = view.findViewById(R.id.newMantraCount);
        mantraName.setHint("New Name(Blank for no change)");
        mantraCount.setHint("New Count(Blank for 0)");

        builder.setView(view).setTitle("Changing Counter: " + counters.get(position).getDisplayName()).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getContext(), "Change " + counters.get(position).getDisplayName() + " cancelled", Toast.LENGTH_SHORT).show();
            }
        }).setPositiveButton("change", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Editable notConvertedName = mantraName.getEditableText();
                Editable unParsedInt = mantraCount.getEditableText();

                String name = notConvertedName.toString().equals("")? counters.get(position).getDisplayName(): notConvertedName.toString();
                int newCount = unParsedInt.toString().equals("")? 0: Integer.parseInt(unParsedInt.toString());

                counters.get(position).setCount(newCount);
                counters.get(position).setDisplayName(name);
                db.masterCounterDAO().insertAllCounters(counters);
                //resets the grid view to include new counter
                adapter.setCounters(counters);
                adapter.notifyItemChanged(position);
            }
        });

        return builder.create();
    }

    private void createDataBase(Context context) {
        db = MasterCounterDatabase.getINSTANCE(context);
    }

}