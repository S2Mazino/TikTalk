package edu.uw.tcss450.team3.tiktalk.ui.auth.forgotpassword;

import static edu.uw.tcss450.team3.tiktalk.utils.PasswordValidator.checkCodeLength;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;
import org.json.JSONObject;

import edu.uw.tcss450.team3.tiktalk.R;
import edu.uw.tcss450.team3.tiktalk.databinding.FragmentEnterCodeBinding;
import edu.uw.tcss450.team3.tiktalk.ui.auth.signin.SignInFragmentArgs;
import edu.uw.tcss450.team3.tiktalk.ui.auth.signin.SignInFragmentDirections;
import edu.uw.tcss450.team3.tiktalk.utils.PasswordValidator;

/**
 * A simple {@link Fragment} subclass.
 */
public class EnterCodeFragment extends Fragment {

    private FragmentEnterCodeBinding binding;
    private EnterCodeViewModel mEnterCodeModel;

    private PasswordValidator mVerifyCode = checkCodeLength(6);


    public EnterCodeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mEnterCodeModel = new ViewModelProvider(getActivity())
                .get(EnterCodeViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEnterCodeBinding.inflate(inflater);
        binding.editEmail.setVisibility(View.GONE);
        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonToChangePassword.setOnClickListener(this::attemptCodeVerify);
        mEnterCodeModel.addResponseObserver(getViewLifecycleOwner(),
                this::observeVerifyResponse);

        EnterCodeFragmentArgs args = EnterCodeFragmentArgs.fromBundle(getArguments());
        binding.editEmail.setText(args.getEmail().equals("default") ? "bronnyo@uw.edu" : args.getEmail());
    }

    private void attemptCodeVerify(final View button) {
        validateVerificationCode();
    }

    private void validateVerificationCode() {
        mVerifyCode.processResult(
                mVerifyCode.apply(binding.editCode.getText().toString().trim()),
                this::verifyCode,
                validationResult -> binding.editCode.setError("Ensure you have entered the correct code"));
    }

    private void verifyCode() {
        mEnterCodeModel.connectVerify(binding.editEmail.getText().toString(),
                binding.editCode.getText().toString());
    }

    private void navigateToSuccess(int memberid) {
        EnterCodeFragmentDirections.ActionEnterCodeFragmentToChangePasswordFragment directions =
                EnterCodeFragmentDirections.actionEnterCodeFragmentToChangePasswordFragment();

        directions.setMemberID(memberid);

        Navigation.findNavController(getView()).navigate(directions);
    }

    private void observeVerifyResponse(final JSONObject response) {
        if (response.length() > 0) {
            if (response.has("code")) {
                try {
                    binding.editCode.setError("Error Authenticating: " + response.getJSONObject("data").getString("message"));
                } catch (JSONException e) {
                    Log.e("JSON Parse Error", e.getMessage());
                }
            } else {
                try {
                    navigateToSuccess(response.getInt("memberid"));
                    Log.d("MemberID", response.getString("memberid"));
                    Log.d("Verify", "Verification successful");
                } catch (JSONException e) {
                    Log.e("JSON Parse Error", e.getMessage());
                }

            }
        }   else {
            Log.d("JSON Response", "No Response");
        }
    }
}