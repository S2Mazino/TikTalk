package edu.uw.tcss450.team3.tiktalk.model;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;


public class UserInfoViewModel extends ViewModel {


    private final String mEmail;
    private final String mJwt;
    private final String mNickname;
    private final String mFirstname;
    private final String mLastname;

    private UserInfoViewModel(String theJwt, String email, String firstname, String lastname, String nickname) {
        mJwt = theJwt;
        mEmail = email;
        mNickname = nickname;
        mFirstname = firstname;
        mLastname = lastname;
    }

    public String getEmail() {
        return mEmail;
    }

    public String getmJwt() {
        return mJwt;
    }

    public String getNickname() {
        return mNickname;
    }

    public String getFirstname() {
        return mFirstname;
    }

    public String getLastname() {
        return mLastname;
    }

    public static class UserInfoViewModelFactory implements ViewModelProvider.Factory {

        private final String mEmail;
        private final String mJwt;
        private final String mNickname;
        private final String mFirstname;
        private final String mLastname;

        public UserInfoViewModelFactory(String mEmail, String mJwt, String mNickname, String mFirstname, String mLastname) {
            this.mEmail = mEmail;
            this.mJwt = mJwt;
            this.mNickname = mNickname;
            this.mFirstname = mFirstname;
            this.mLastname = mLastname;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass == UserInfoViewModel.class) {
                return (T) new UserInfoViewModel(mEmail, mJwt, mNickname, mFirstname, mLastname);
            }
            throw new IllegalArgumentException(
                    "Argument must be: " + UserInfoViewModel.class);
        }
    }


}
