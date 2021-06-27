package com.pranjal.vac_slots_india.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.pranjal.vac_slots_india.models.Centers;
import com.pranjal.vac_slots_india.models.Sessions;
import com.pranjal.vac_slots_india.models.VaccineFees;

import java.util.ArrayList;

public class DialogFragmentViewSlotDetails extends DialogFragment {
    public static String TAG = "slotDetailView";
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        assert bundle != null;
        ArrayList<Sessions> sessionsList = bundle.getParcelableArrayList("sessions");
        ArrayList<VaccineFees> vaccineFeesList = bundle.getParcelableArrayList("vaccine");
        Centers center = bundle.getParcelable("center");
        String text="";
        text += center.toStringDialog();
        for(VaccineFees vaccineFees : vaccineFeesList)
            text += vaccineFees.toStringDialog();
        text+="\n";
        for(Sessions session : sessionsList)
            text += session.toStringDialog();
        return new AlertDialog.Builder(requireContext())
                .setMessage(text)
                .setPositiveButton("OKAY", (dialog, which) -> {})
                .create();
    }
}
