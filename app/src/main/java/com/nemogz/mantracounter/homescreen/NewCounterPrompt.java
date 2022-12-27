package com.nemogz.mantracounter.homescreen;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Layout;
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
import com.nemogz.mantracounter.counterStuff.LittleHouse;
import com.nemogz.mantracounter.counterStuff.MasterCounter;
import com.nemogz.mantracounter.dataStorage.MasterCounterDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class NewCounterPrompt extends AppCompatDialogFragment {

    private EditText mantraName;
    private EditText mantraCount;
    public MasterCounterDatabase db;
    private List<Counter> counters;
    private CounterMainRecViewAdapter adapter;

    public NewCounterPrompt(CounterMainRecViewAdapter adapter) {
        this.adapter = adapter;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        createDataBase(getContext());
        counters = new ArrayList<>();

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.new_counter_prompt, null);

        builder.setView(view).setTitle(getString(R.string.NewMantra)).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getContext(), getString(R.string.createNewCounterCancelled), Toast.LENGTH_SHORT).show();
            }
        }).setPositiveButton(getString(R.string.Change), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Editable notConvertedName = mantraName.getEditableText();
                Editable unParsedInt = mantraCount.getEditableText();
                String name = notConvertedName.toString().equals("")? getString(R.string.blank): notConvertedName.toString();
                int initialCount = unParsedInt.toString().equals("")? 0: Integer.parseInt(unParsedInt.toString());
                counters = db.masterCounterDAO().getAllCounters();
                Counter newCounter = new Counter(name, initialCount, requireContext());
                db.masterCounterDAO().insertCounter(newCounter);
                //resets the grid view to include new counter
                counters.add(newCounter);
                adapter.setCounters(counters);
                adapter.notifyItemInserted(counters.size()-1);
            }
        });

        mantraName = view.findViewById(R.id.newMantraName);
        mantraCount = view.findViewById(R.id.newMantraCount);
        return builder.create();
    }

    private void createDataBase(Context context) {
        db = MasterCounterDatabase.getINSTANCE(context);
    }

}
