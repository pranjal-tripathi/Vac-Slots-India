package com.pranjal.vac_slots_india.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.pranjal.vac_slots_india.MainActivity;
import com.pranjal.vac_slots_india.R;
import com.pranjal.vac_slots_india.fragments.DialogFragmentViewSlotDetails;
import com.pranjal.vac_slots_india.models.Centers;
import com.pranjal.vac_slots_india.models.Sessions;
import com.pranjal.vac_slots_india.models.VaccineFees;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{
    private final Context context;
    private final ArrayList<Centers> centersList;

    public RecyclerViewAdapter(Context context, ArrayList<Centers> centersList) {
        this.context = context;
        this.centersList = centersList;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.slot_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerViewAdapter.ViewHolder holder, int position) {
        Centers center = centersList.get(position);
        String name = center.getCenterName();
        String address = center.getAddress();
        String fee_type = center.getFee_type();
        String pincode = center.getPincode().toString();
        String cardText = name + " ( " + fee_type + " ) | " + address  + " | " + pincode;
                holder.cardName.setText(cardText);
    }

    @Override
    public int getItemCount() {
        return centersList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView cardName;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            cardName = itemView.findViewById(R.id.cardName);
        }

        @Override
        public void onClick(View v) {
            int position = this.getAdapterPosition();
            if(centersList.size() != 0) {
                Centers center = centersList.get(position);
                ArrayList<Sessions> sessionsList = (ArrayList<Sessions>) center.getSessionsList();
                ArrayList<VaccineFees> vaccineFeesList = (ArrayList<VaccineFees>) center.getVaccineFeesList();
                openFragment(center, sessionsList, vaccineFeesList);
            }
        }

        private void openFragment(Centers center, ArrayList<Sessions> sessionsList, ArrayList<VaccineFees> vaccineFeesList) {
            Bundle bundle = new Bundle();
            DialogFragmentViewSlotDetails fragment = new DialogFragmentViewSlotDetails();
            bundle.putParcelable("center", center);
            bundle.putParcelableArrayList("sessions", sessionsList);
            bundle.putParcelableArrayList("vaccine", vaccineFeesList);
            fragment.setArguments(bundle);
            switchContent(fragment);
        }
        private void switchContent(DialogFragment fragment) {
            if (context == null)
                return;
            if (context instanceof MainActivity) {
                MainActivity mainActivity = (MainActivity) context;
                mainActivity.switchContent(fragment);
            }

        }
    }
}
