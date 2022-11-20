package edu.uw.tcss450.team3.tiktalk.ui.connection;

public class Contact {

    private String mFName;
    private String mLName;
    private String mNickname;
    private String mEmail;
    private int mMemberID;

    public Contact (String FName, String LName, String Nickname, String Email, int MemberID) {
        this.mFName = FName;
        this.mLName = LName;
        this.mNickname = Nickname;
        this.mEmail = Email;
        this.mMemberID = MemberID;
    }

    public String getFName() {
        return mFName;
    }

    public String getLName() {
        return mLName;
    }


    public String getNickname() {
        return mNickname;
    }

    public String getEmail() {
        return mEmail;
    }

    public int getMemberID() {
        return mMemberID;
    }
}
