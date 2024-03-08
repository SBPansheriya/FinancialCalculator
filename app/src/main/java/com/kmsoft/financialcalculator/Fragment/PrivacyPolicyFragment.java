package com.kmsoft.financialcalculator.Fragment;

import static com.kmsoft.financialcalculator.MainActivity.isStep;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.kmsoft.financialcalculator.MainActivity;
import com.kmsoft.financialcalculator.R;

public class PrivacyPolicyFragment extends Fragment {
    ImageView back;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_privacy_policy, container, false);

        back = view.findViewById(R.id.back);

        back.setOnClickListener(v -> {
            isStep = true;
            Intent intent = new Intent(PrivacyPolicyFragment.this.getContext(), MainActivity.class);
            startActivity(intent);
        });

        return view;
    }
}