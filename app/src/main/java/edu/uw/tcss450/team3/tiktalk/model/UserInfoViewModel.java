package edu.uw.tcss450.team3.tiktalk.model;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.auth0.android.jwt.JWT;


public class UserInfoViewModel extends ViewModel {


    private final String mEmail;
    private final String mJwt;
    private final String mNickname;
    private final String mFirstname;
    private final String mLastname;

    private UserInfoViewModel(String theJwt) {
        mJwt = theJwt;

        final JWT jwt = new JWT(theJwt);
        mEmail = jwt.getClaim("email").asString();
        mNickname = jwt.getClaim("nickname").asString();;
        mFirstname = jwt.getClaim("firstname").asString();;
        mLastname = jwt.getClaim("lastname").asString();;
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

        private final String mJwt;

        public UserInfoViewModelFactory(String theJwt) {
            this.mJwt = theJwt;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass == UserInfoViewModel.class) {
                return (T) new UserInfoViewModel(mJwt);
            }
            throw new IllegalArgumentException(
                    "Argument must be: " + UserInfoViewModel.class);
        }
    }


}
