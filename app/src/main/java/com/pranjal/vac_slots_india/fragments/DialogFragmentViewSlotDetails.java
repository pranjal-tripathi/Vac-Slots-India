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
        StringBuilder text= new StringBuilder();
        text.append(center.toStringDialog());
        for(VaccineFees vaccineFees : vaccineFeesList)
            text.append(vaccineFees.toStringDialog());
        text.append("\n");
        for(Sessions session : sessionsList)
            text.append(session.toStringDialog());
        return new AlertDialog.Builder(requireContext())
                .setMessage(text.toString())
                .setPositiveButton("OKAY", (dialog, which) -> {})
                .create();
    }
}
