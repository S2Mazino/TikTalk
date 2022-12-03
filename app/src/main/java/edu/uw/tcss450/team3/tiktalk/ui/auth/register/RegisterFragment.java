package edu.uw.tcss450.team3.tiktalk.ui.auth.register;

import static edu.uw.tcss450.team3.tiktalk.utils.PasswordValidator.*;
import static edu.uw.tcss450.team3.tiktalk.utils.PasswordValidator.checkClientPredicate;

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

import edu.uw.tcss450.team3.tiktalk.databinding.FragmentRegisterBinding;
import edu.uw.tcss450.team3.tiktalk.utils.PasswordValidator;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment {

    private FragmentRegisterBinding binding;

    private RegisterViewModel mRegisterModel;

    final private PasswordValidator mNameValidator = checkPwdLength(2)
            .and(checkExcludeWhiteSpace())
            .and(checkNoDigits())
            .and(checkNoSpecialChar());

    final private PasswordValidator mNicknameValidator = checkPwdLength(2)
            .and(checkExcludeWhiteSpace())
            .and(checkNoSpecialChar());

    final private PasswordValidator mEmailValidator = checkPwdLength(2)
            .and(checkExcludeWhiteSpace())
            .and(checkPwdSpecialChar("@"))
            .and(checkPwdSpecialChar("."));

    final private PasswordValidator mPassWordValidator =
            checkClientPredicate(pwd -> pwd.equals(binding.editPassword2.getText().toString()))
                    .and(checkPwdLength(4))
                    .and(checkPwdSpecialChar())
                    .and(checkExcludeWhiteSpace())
                    .and(checkPwdDigit())
                    .and(checkPwdLowerCase())
                    .and(checkPwdUpperCase());

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRegisterModel = new ViewModelProvider(getActivity())
                .get(RegisterViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonToSignIn.setOnClickListener(button ->
                Navigation.findNavController(getView()).navigate(
                        RegisterFragmentDirections.actionRegisterFragmentToSignInFragment()
                ));
        binding.buttonRegister.setOnClickListener(this::attemptRegister);
        mRegisterModel.addResponseObserver(getViewLifecycleOwner(),
                this::observeResponse);
    }

    private void attemptRegister(final View button) {
        validateFirst();
    }

    private void validateFirst() {
        mNameValidator.processResult(
                mNameValidator.apply(binding.editFirst.getText().toString().trim()),
                this::validateLast,
                this::fNameError);
    }

    private void validateLast() {
        mNameValidator.processResult(
                mNameValidator.apply(binding.editLast.getText().toString().trim()),
                this::validateEmail,
                this::lNameError);
    }

    private void validateEmail() {
        mEmailValidator.processResult(
                mEmailValidator.apply(binding.editEmail.getText().toString().trim()),
                this::validateNickname,
                this::emailError);
    }

    private void validateNickname() {
        mNicknameValidator.processResult(
                mNicknameValidator.apply(binding.editNickname.getText().toString().trim()),
                this::validatePasswordsMatch,
                this::nicknameError);
    }

    private void validatePasswordsMatch() {
        PasswordValidator matchValidator =
                checkClientPredicate(
                        pwd -> pwd.equals(binding.editPassword2.getText().toString().trim()));

        mEmailValidator.processResult(
                matchValidator.apply(binding.editPassword1.getText().toString().trim()),
                this::validatePassword,
                result -> binding.editPassword1.setError("Passwords must match."));
    }

    private void validatePassword() {
        mPassWordValidator.processResult(
                mPassWordValidator.apply(binding.editPassword1.getText().toString()),
                this::verifyAuthWithServer,
                this::passwordError);
                //result -> binding.editPassword1.setError("Please enter a valid Password."));
    }



    private void verifyAuthWithServer() {
        mRegisterModel.connect(
                binding.editFirst.getText().toString(),
                binding.editLast.getText().toString(),
                binding.editEmail.getText().toString(),
                binding.editNickname.getText().toString(),
                binding.editPassword1.getText().toString());
        //This is an Asynchronous call. No statements after should rely on the
        //result of connect().
    }

    private void navigateToLogin() {
        RegisterFragmentDirections.ActionRegisterFragmentToSignInFragment directions =
                RegisterFragmentDirections.actionRegisterFragmentToSignInFragment();

        directions.setEmail(binding.editEmail.getText().toString());
        directions.setPassword(binding.editPassword1.getText().toString());

        Navigation.findNavController(getView()).navigate(directions);

    }

    /**
     * An observer on the HTTP Response from the web server. This observer should be
     * attached to SignInViewModel.
     *
     * @param response the Response from the server
     */
    private void observeResponse(final JSONObject response) {
        if (response.length() > 0) {
            if (response.has("code")) {
                try {
                    if(response.getJSONObject("data").getString("message").equals("Email exists")){
                        binding.editEmail.setError("Email already exists.");
                    }else {
                        binding.editEmail.setError(
                                "Error Authenticating: " +
                                        response.getJSONObject("data").getString("message"));

                    }
                } catch (JSONException e) {
                    Log.e("JSON Parse Error", e.getMessage());
                }
            } else {
                navigateToLogin();
            }
        } else {
            Log.d("JSON Response", "No Response");
        }
    }


    private void fNameError(ValidationResult result) {

        String error = result.toString();
        Log.d("JSON Response - fName", error);

        switch (error) {
            case "PWD_INVALID_LENGTH":
                binding.editFirst.setError("Name must be at least 3 characters long.");
                break;
            case "PWD_INCLUDES_EXCLUDED":
                binding.editFirst.setError("Name cannot contain numbers or special characters.");
                break;
            case "PWD_INCLUDES_WHITESPACE":
                binding.editFirst.setError("Name cannot contain spaces.");
                break;
            default:
                binding.editFirst.setError("Contact admin error.");
                Log.d("lName Error", "Special edge case missing");
                break;
        }
    }

    private void lNameError(ValidationResult result) {

        String error = result.toString();
        Log.d("JSON Response - lName", error);

        switch (error) {
            case "PWD_INVALID_LENGTH":
                binding.editLast.setError("Name must be at least 3 characters long.");
                break;
            case "PWD_INCLUDES_EXCLUDED":
                binding.editLast.setError("Name cannot contain numbers or special characters.");
                break;
            case "PWD_INCLUDES_WHITESPACE":
                binding.editLast.setError("Name cannot contain spaces.");
                break;
            default:
                binding.editLast.setError("Contact admin error.");
                Log.d("lName Error", "Special edge case missing");
                break;
        }
    }

    private void nicknameError(ValidationResult result) {

        String error = result.toString();
        Log.d("JSON Response - nickname", error);

        switch (error) {
            case "PWD_INVALID_LENGTH":
                binding.editNickname.setError("Nickname must be at least 3 characters long.");
                break;
            case "PWD_INCLUDES_EXCLUDED":
                binding.editNickname.setError("Nickname cannot contain numbers or special characters.");
                break;
            case "PWD_INCLUDES_WHITESPACE":
                binding.editNickname.setError("Nickname cannot contain spaces.");
                break;
            default:
                binding.editNickname.setError("Contact admin error.");
                Log.d("Nickname Error", "Special edge case missing");
                break;
        }
    }


    private void emailError(ValidationResult result) {

        String error = result.toString();
        Log.d("JSON Response - email", error);

        switch (error) {
            case "PWD_INVALID_LENGTH":
                binding.editEmail.setError("Email must be at least 5 characters long.");
                break;
            case "PWD_MISSING_SPECIAL":
                binding.editEmail.setError("Email must contain '@' and '.' character.");
                break;
            case "PWD_INCLUDES_WHITESPACE":
                binding.editEmail.setError("Email cannot contain spaces.");
                break;
            default:
                binding.editEmail.setError("Contact admin error.");
                Log.d("Email Error", "Special edge case missing");
                break;
        }
    }

    private void passwordError(ValidationResult result) {

        String error = result.toString();
        Log.d("JSON Response - password", error);

        switch (error) {
            case "PWD_INVALID_LENGTH":
                binding.editPassword1.setError("Password must be at least 5 characters long.");
                break;
            case "PWD_MISSING_DIGIT":
                binding.editPassword1.setError("Password must contain a digit.");
                break;
            case "PWD_MISSING_UPPER":
                binding.editPassword1.setError("Password must contain a uppercase letter.");
                break;
            case "PWD_MISSING_LOWER":
                binding.editPassword1.setError("Password must contain a lowercase letter.");
                break;
            case "PWD_MISSING_SPECIAL":
                binding.editPassword1.setError("Password must contain a special character.");
                break;
            case "PWD_INCLUDES_WHITESPACE":
                binding.editPassword1.setError("Password cannot contain spaces.");
                break;
            default:
                binding.editPassword1.setError("Contact admin error.");
                Log.d("Password Error", "Special edge case missing");
                break;
        }
    }
}


