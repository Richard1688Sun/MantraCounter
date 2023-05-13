package com.nemogz.mantracounter.homeworkScreen;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;

public class ReminderPrompt extends AppCompatDialogFragment {

    AppCompatActivity activityClass;

    String promptMsg;

    public ReminderPrompt(AppCompatActivity activityClass, String promptMsg) {
        this.activityClass = activityClass;
        this.promptMsg = promptMsg;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        return super.onCreateDialog(savedInstanceState);
    }
}
