package edu.uw.tcss450.team3.tiktalk.ui.auth.forgotpassword;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.uw.tcss450.team3.tiktalk.R;
import edu.uw.tcss450.team3.tiktalk.databinding.FragmentResetCodeBinding;


/**
 * A simple {@link Fragment} subclass.
 */
public class ResetCodeFragment extends Fragment {

    private FragmentResetCodeBinding binding;


    public ResetCodeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reset_code, container, false);
    }
}