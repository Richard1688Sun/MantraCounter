package com.nemogz.mantracounter.homescreen;

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
import com.nemogz.mantracounter.dataStorage.MasterCounterDatabase;

import java.util.List;

public class ChangeLittleHousePrompt extends AppCompatDialogFragment {
    private EditText mantraName;
    private EditText mantraCount;
    public MasterCounterDatabase db;
    private LittleHouse littleHouse;
    private HomeScreenActivity homeScreenActivity;

    public ChangeLittleHousePrompt(HomeScreenActivity homeScreenActivity) {
        this.homeScreenActivity = homeScreenActivity;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        createDataBase(getContext());
        littleHouse = db.masterCounterDAO().getLittleHouse();

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.new_counter_prompt, null);

        mantraName = view.findViewById(R.id.newMantraName);
        mantraCount = view.findViewById(R.id.newMantraCount);
        mantraName.setHint("New Name(Blank for no change)");
        mantraCount.setHint("New Count(Blank for no change)");

        builder.setView(view).setTitle("Changing: " + littleHouse.getLittleHouseDisplayName()).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getContext(), "Change " + littleHouse.getLittleHouseDisplayName() + " cancelled", Toast.LENGTH_SHORT).show();
            }
        }).setPositiveButton("change", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Editable notConvertedName = mantraName.getEditableText();
                Editable unParsedInt = mantraCount.getEditableText();

                String name = notConvertedName.toString().equals("")? littleHouse.getLittleHouseDisplayName(): notConvertedName.toString();
                int newCount = unParsedInt.toString().equals("")? littleHouse.getLittleHouseCount(): Integer.parseInt(unParsedInt.toString());

                littleHouse.setLittleHouseDisplayName(name);
                littleHouse.setLittleCount(newCount);

                if (!notConvertedName.toString().equals("") || !unParsedInt.toString().equals("")) {
                    db.masterCounterDAO().insertLittleHouse(littleHouse);
                    homeScreenActivity.setLittleHouse(littleHouse);
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
