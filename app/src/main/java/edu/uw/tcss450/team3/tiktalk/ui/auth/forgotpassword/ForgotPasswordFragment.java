package edu.uw.tcss450.team3.tiktalk.ui.auth.forgotpassword;

import static edu.uw.tcss450.team3.tiktalk.utils.PasswordValidator.checkCodeLength;
import static edu.uw.tcss450.team3.tiktalk.utils.PasswordValidator.checkExcludeWhiteSpace;
import static edu.uw.tcss450.team3.tiktalk.utils.PasswordValidator.checkPwdLength;
import static edu.uw.tcss450.team3.tiktalk.utils.PasswordValidator.checkPwdSpecialChar;

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

import edu.uw.tcss450.team3.tiktalk.databinding.FragmentForgotPasswordBinding;
import edu.uw.tcss450.team3.tiktalk.utils.PasswordValidator;

/**
 * A simple {@link Fragment} subclass.
 */
public class ForgotPasswordFragment extends Fragment {

    private FragmentForgotPasswordBinding binding;
    private ForgotPasswordViewModel mForgotModel;
    

    private PasswordValidator mEmailValidator = checkPwdLength(2)
            .and(checkExcludeWhiteSpace())
            .and(checkPwdSpecialChar("@"));


    public ForgotPasswordFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mForgotModel = new ViewModelProvider(getActivity())
                .get(ForgotPasswordViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentForgotPasswordBinding.inflate(inflater);
        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonBackToSignin.setOnClickListener(button ->
                Navigation.findNavController(getView()).navigate(
                        ForgotPasswordFragmentDirections.actionForgotPasswordToSignInFragment()
                ));
        binding.buttonForgotPassword.setOnClickListener(this::attemptEmail);
        mForgotModel.addResponseObserver(getViewLifecycleOwner(),
                this::observeResponse);
    }

    private void navigateToSuccess() {
        
        ForgotPasswordFragmentDirections.ActionForgotPasswordToEnterCodeFragment directions =
                ForgotPasswordFragmentDirections.actionForgotPasswordToEnterCodeFragment();

        directions.setEmail(binding.editEmail.getText().toString());

        Navigation.findNavController(getView()).navigate(directions);
    }

    private void attemptEmail(final View button) {
        validateEmail();
    }

    private void validateEmail() {
        mEmailValidator.processResult(
                mEmailValidator.apply(binding.editEmail.getText().toString().trim()),
                this::sendResetCode,
                validationResult -> binding.editEmail.setError("Please enter a valid email"));
    }

    private void sendResetCode() {
        mForgotModel.connect(
                binding.editEmail.getText().toString());

        //This is an Asynchronous call. No statements after should rely on the
        //result of connect().
    }



    private void observeResponse(final JSONObject response) {
        if (response.length() > 0) {
            if (response.has("code")) {
                try {
                    binding.editEmail.setError("Error Authenticating: " + response.getJSONObject("data").getString("message"));
                } catch (JSONException e) {
                    Log.e("JSON Parse Error", e.getMessage());
                }
            } else {
                navigateToSuccess();
            }
        }   else {
            Log.d("JSON Response", "No Response");
        }
    }


}