package com.nemogz.mantracounter.homeworkScreen;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.FragmentManager;

import com.nemogz.mantracounter.R;

public class ReminderPrompt extends AppCompatDialogFragment {

    String promptMsg;

    Boolean isConfirm;

    public ReminderPrompt(String promptMsg, Boolean isConfirm) {
        this.promptMsg = promptMsg;
        this.isConfirm = isConfirm;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(promptMsg);
        builder.setNegativeButton(getString(R.string.cancel) , new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getContext(), getString(R.string.Cancelled), Toast.LENGTH_SHORT).show();
                isConfirm = false;
            }
        });

        builder.setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                isConfirm = true;
            }
        });
        return builder.create();
    }

    public void show(FragmentManager supportFragmentManager) {
    }
}
