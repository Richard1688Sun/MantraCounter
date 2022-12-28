package com.nemogz.mantracounter.homeworkScreen;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.nemogz.mantracounter.counterStuff.LittleHouse;
import com.nemogz.mantracounter.counterStuff.MasterCounter;
import com.nemogz.mantracounter.dataStorage.MasterCounterDatabase;

import java.util.List;

public class ChangeHomeworkPrompt extends AppCompatDialogFragment {
    private EditText mantraName;
    private EditText mantraCount;
    public MasterCounterDatabase db;
    private MasterCounter masterCounter;
    private HomeScreenActivity homeScreenActivity;

    public ChangeHomeworkPrompt(HomeScreenActivity homeScreenActivity) {
        this.homeScreenActivity = homeScreenActivity;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        createDataBase(getContext());
        masterCounter = db.masterCounterDAO().getMasterCounter();
        masterCounter.setLittleHouse(db.masterCounterDAO().getLittleHouse());
        masterCounter.setCounters(db.masterCounterDAO().getAllCounters());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.new_counter_prompt, null);

        mantraName = view.findViewById(R.id.newMantraName);
        mantraCount = view.findViewById(R.id.newMantraCount);
        mantraName.setHint(getString(R.string.NewName));
        mantraCount.setHint(getString(R.string.NewCount));

        builder.setView(view).setTitle( getString(R.string.Changing) + ": " + masterCounter.getHomeworkDisplayName()).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getContext(), getString(R.string.Change) + " " + masterCounter.getHomeworkDisplayName() + " " + getString(R.string.Cancelled), Toast.LENGTH_SHORT).show();
            }
        }).setPositiveButton(getString(R.string.Change), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Editable notConvertedName = mantraName.getEditableText();
                Editable unParsedInt = mantraCount.getEditableText();

                String name = notConvertedName.toString().equals("")? masterCounter.getHomeworkDisplayName(): notConvertedName.toString();
                int newCount = unParsedInt.toString().equals("")? masterCounter.getHomeworkCount(): Integer.parseInt(unParsedInt.toString());

                masterCounter.setHomeworkCount(newCount);
                masterCounter.setHomeworkDisplayName(name);

                if (!notConvertedName.toString().equals("") || !unParsedInt.toString().equals("")) {
                    db.masterCounterDAO().insertCounterPosition(masterCounter);
                    homeScreenActivity.setMasterCounter(masterCounter);
                    homeScreenActivity.setBasicCounterView();
                }
            }
        });

        return builder.create();
    }

    private void createDataBase(Context context) {
        db = MasterCounterDatabase.getINSTANCE(context);
    }

}
