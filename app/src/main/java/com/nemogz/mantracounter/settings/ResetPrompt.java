package com.nemogz.mantracounter.settings;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Vibrator;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.nemogz.mantracounter.R;
import com.nemogz.mantracounter.SettingsScreen;
import com.nemogz.mantracounter.counterStuff.Counter;
import com.nemogz.mantracounter.counterStuff.LittleHouse;
import com.nemogz.mantracounter.counterStuff.MasterCounter;
import com.nemogz.mantracounter.dataStorage.MasterCounterDatabase;

import java.util.List;

public class ResetPrompt extends AppCompatDialogFragment {

    private LittleHouse littleHouse;
    private List<Counter> counters;
    private MasterCounter masterCounter;
    private SettingsDataClass settingsDataClass;
    private Vibrator vibrator;
    public MasterCounterDatabase db;
    private SettingsScreen settingsScreen;

    public ResetPrompt(SettingsScreen settingsScreen) {
        this.settingsScreen = settingsScreen;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        createDataBase(getContext());
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(getString(R.string.confirmResetTitle));

        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getContext(), getString(R.string.resetCancel), Toast.LENGTH_SHORT).show();

            }
        });

        builder.setPositiveButton(getString(R.string.reset), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                vibrator = (Vibrator)getContext().getSystemService(Context.VIBRATOR_SERVICE);

                counters = db.masterCounterDAO().getAllCounters();
                littleHouse = db.masterCounterDAO().getLittleHouse();
                masterCounter = db.masterCounterDAO().getMasterCounter();
                settingsDataClass = db.masterCounterDAO().getSettingsData();

                for (Counter counter: counters) {
                    counter.setCount(0);
                }
                littleHouse.resetLittleHouse();
                masterCounter.resetHomeworkCount();

                db.masterCounterDAO().insertLittleHouse(littleHouse);
                db.masterCounterDAO().insertAllCounters(counters);
                db.masterCounterDAO().insertMasterCounter(masterCounter);
                if(vibrator.hasVibrator() && settingsDataClass.isVibrationsEffect()) vibrator.vibrate(500);
                Toast.makeText(getContext(), getString(R.string.resetConfirmed), Toast.LENGTH_SHORT).show();
                settingsScreen.setSettingsAdapter();
            }
        });
        return builder.create();
    }

    private void createDataBase(Context context) {
        db = MasterCounterDatabase.getINSTANCE(context);
    }
}
