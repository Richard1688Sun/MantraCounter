package com.nemogz.mantracounter.homeworkScreen;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.FragmentManager;

import com.nemogz.mantracounter.LittleHouseScreenActivity;
import com.nemogz.mantracounter.R;

public class HomeworkReminderPrompt extends AppCompatDialogFragment {

    LittleHouseScreenActivity activity;

    public HomeworkReminderPrompt(LittleHouseScreenActivity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(getString(R.string.confirm) + " " + getString(R.string.increment) + " " + getString(R.string.xiaofangzi) + "?");
        builder.setNegativeButton(getString(R.string.cancel) , new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getContext(), getString(R.string.Cancelled), Toast.LENGTH_SHORT).show();
            }
        });

        builder.setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                activity.getMasterCounter().incrementLittleHouse();
                if (activity.getSettingsDataClass().isSoundEffect() && activity.isLoaded()) activity.getSoundPool().play(activity.getLittleHouseID(), 1, 1, 1, 0, 0);
                if (activity.isHasVibratorFunction() && activity.getSettingsDataClass().isVibrationsEffect()) activity.getVibrator().vibrate(100);
                activity.setCounterView();
            }
        });

        return builder.create();
    }

    public void show(FragmentManager supportFragmentManager) {
    }
}
