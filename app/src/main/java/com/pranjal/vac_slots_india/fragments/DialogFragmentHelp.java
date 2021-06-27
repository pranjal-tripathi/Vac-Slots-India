package com.pranjal.vac_slots_india.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class DialogFragmentHelp extends DialogFragment {
    public static String TAG = "helpview";
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        String text="How to use? \n\n" +
                "1. Select any one of the options from the radio buttons to find slots by District name or by Pincode. \n" +
                "2. Select your state and district from the dropdown menu or enter the pincode. \n" +
                "3. Select the type of slots that you want to see by choosing the appropriate checkboxes. \n" +
                "4. Click on Get Slots button to view the slots. \n" +
                "5. If you want to get notified about the slots available, then turn on the switch at upper right corner. \n";
        return new AlertDialog.Builder(requireContext())
                .setMessage(text)
                .setPositiveButton("OKAY", (dialog, which) -> {})
                .create();
    }
}
