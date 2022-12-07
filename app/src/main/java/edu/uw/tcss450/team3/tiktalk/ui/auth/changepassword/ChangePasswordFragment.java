package edu.uw.tcss450.team3.tiktalk.ui.auth.changepassword;

import static edu.uw.tcss450.team3.tiktalk.utils.PasswordValidator.checkClientPredicate;
import static edu.uw.tcss450.team3.tiktalk.utils.PasswordValidator.checkExcludeWhiteSpace;
import static edu.uw.tcss450.team3.tiktalk.utils.PasswordValidator.checkPwdDigit;
import static edu.uw.tcss450.team3.tiktalk.utils.PasswordValidator.checkPwdLength;
import static edu.uw.tcss450.team3.tiktalk.utils.PasswordValidator.checkPwdLowerCase;
import static edu.uw.tcss450.team3.tiktalk.utils.PasswordValidator.checkPwdSpecialChar;
import static edu.uw.tcss450.team3.tiktalk.utils.PasswordValidator.checkPwdUpperCase;

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
import edu.uw.tcss450.team3.tiktalk.databinding.FragmentChangePasswordCodeBinding;
import edu.uw.tcss450.team3.tiktalk.model.UserInfoViewModel;
import edu.uw.tcss450.team3.tiktalk.utils.PasswordValidator;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChangePasswordFragment extends Fragment {

    private FragmentChangePasswordCodeBinding binding;
    private ChangePasswordViewModel mChangeModel;
    private UserInfoViewModel mUserInfoModel;


    private PasswordValidator mPasswordValidator =
            checkClientPredicate(pwd -> pwd.equals(binding.editTextTextPassword3.getText().toString()))
                    .and(checkPwdLength(4))
                    .and(checkPwdSpecialChar())
                    .and(checkExcludeWhiteSpace())
                    .and(checkPwdDigit())
                    .and(checkPwdLowerCase())
                    .and(checkPwdUpperCase());

    private PasswordValidator mEmailValidator = checkPwdLength(2)
            .and(checkExcludeWhiteSpace())
            .and(checkPwdSpecialChar("@"))
            .and(checkPwdSpecialChar("."));

    public ChangePasswordFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mChangeModel = new ViewModelProvider(getActivity())
                .get(ChangePasswordViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentChangePasswordCodeBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.buttonReset.setOnClickListener(this::attemptPasswordChange);
        mChangeModel.addResponseObserver(getViewLifecycleOwner(),
                this::observeChangeResponse);
    }


    private void navigateBackToSignIn() {
        Navigation.findNavController(getView())
                .navigate(ChangePasswordFragmentDirections
                        .actionChangePasswordFragmentToSignInFragment());
    }

    private void attemptPasswordChange(final View button) {
        validatePasswordsMatch();
    }

    private void validatePasswordsMatch() {
        PasswordValidator matchValidator =
                checkClientPredicate(
                        pwd -> pwd.equals(binding.editTextTextPassword3.getText().toString().trim()));

        mEmailValidator.processResult(
                matchValidator.apply(binding.editTextTextPassword2.getText().toString().trim()),
                this::validatePassword,
                result -> binding.editTextTextPassword2.setError("Passwords must match."));
    }

    private void validatePassword() {
        mPasswordValidator.processResult(
                mPasswordValidator.apply(binding.editTextTextPassword2.getText().toString()),
                this::verifyPasswordChange,
                this::passwordError);
    }

    private void verifyPasswordChange() {
        ChangePasswordFragmentArgs args = ChangePasswordFragmentArgs.fromBundle(getArguments());
        Log.d("memberID", String.valueOf(args.getMemberID()));
        mChangeModel.connectPasswordChange(args.getMemberID(), binding.editTextTextPassword2.getText().toString());
    }

    private void passwordError(PasswordValidator.ValidationResult result) {

        String error = result.toString();
        Log.d("JSON Response - password", error);

        switch (error) {
            case "PWD_INVALID_LENGTH":
                binding.editTextTextPassword2.setError("Password must be at least 5 characters long.");
                break;
            case "PWD_MISSING_DIGIT":
                binding.editTextTextPassword2.setError("Password must contain a digit.");
                break;
            case "PWD_MISSING_UPPER":
                binding.editTextTextPassword2.setError("Password must contain a uppercase letter.");
                break;
            case "PWD_MISSING_LOWER":
                binding.editTextTextPassword2.setError("Password must contain a lowercase letter.");
                break;
            case "PWD_MISSING_SPECIAL":
                binding.editTextTextPassword2.setError("Password must contain a special character.");
                break;
            case "PWD_INCLUDES_WHITESPACE":
                binding.editTextTextPassword2.setError("Password cannot contain spaces.");
                break;
            default:
                binding.editTextTextPassword2.setError("Contact admin error.");
                Log.d("Password Error", "Special edge case missing");
                break;
        }
    }

    private void observeChangeResponse(final JSONObject response) {
        if (response.length() > 0) {
            if (response.has("code")) {
                try {
                    binding.editTextTextPassword2.setError("Error Authenticating: " + response.getJSONObject("data").getString("message"));
                } catch (JSONException e) {
                    Log.e("JSON Parse Error", e.getMessage());
                }
            } else {
                navigateBackToSignIn();
            }
        }   else {
            Log.d("JSON Response", "No Response");
        }
    }
}