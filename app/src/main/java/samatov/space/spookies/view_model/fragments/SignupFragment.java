package samatov.space.spookies.view_model.fragments;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.HttpException;
import samatov.space.spookies.R;
import samatov.space.spookies.model.api.beans.ApiError;
import samatov.space.spookies.model.utils.Formatter;
import samatov.space.spookies.model.utils.InputValidator;
import samatov.space.spookies.view_model.activities.AuthActivity;
import samatov.space.spookies.view_model.utils.DialogFactory;

public class SignupFragment extends Fragment {


    public static SignupFragment newInstance(FragmentActivity activity) {
        SignupFragment fragment = new SignupFragment();
        fragment.setEnterTransition(TransitionInflater.from(activity)
                .inflateTransition(android.R.transition.slide_right));
        fragment.setExitTransition(TransitionInflater.from(activity)
                .inflateTransition(android.R.transition.slide_right));
        return fragment;
    }


    AuthActivity mActivity;

    @BindView(R.id.signupLoginLinkTextView) TextView mLoginLinkTextView;
    @BindView(R.id.signupUsernameInput) EditText mUsernameEditText;
    @BindView(R.id.signupPassInput) EditText mPassEditText;
    @BindView(R.id.signupPassRepeatInput) EditText mPassRepeatEditText;
    @BindView(R.id.signupUsernameInputLayout) TextInputLayout mUsernameInputLayout;
    @BindView(R.id.signupPassInputLayout) TextInputLayout mPassInputLayout;
    @BindView(R.id.signupPassRepeatInputLayout) TextInputLayout mPassRepeatInputLayout;
    @BindView(R.id.signupScrollView) ScrollView mScrollView;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_signup, container, false);
        ButterKnife.bind(this, view);
        styleLoginLink();
        mActivity = (AuthActivity) getActivity();

        return view;
    }


    private void styleLoginLink() {
        Spannable span = new SpannableString("Already a user? Log in");
        span.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorLink)),
                16, span.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        span.setSpan(new StyleSpan(Typeface.BOLD),
                16, span.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        mLoginLinkTextView.setText(span);
    }


    @OnClick(R.id.signupButton)
    public void onSignup() {
        String username = mUsernameEditText.getText() + "".trim().toLowerCase();
        String password = mPassEditText.getText() + "".trim().toLowerCase();
        String repeatPassword = mPassRepeatEditText.getText() + "".trim().toLowerCase();

        if (inputsEmpty() || !passwordsMatch() || invalidCharacters(username, password, repeatPassword))
            return;
        displayWarningDialog(username, password, repeatPassword);
    }


    private void displayWarningDialog(String username, String password, String repeatPassword) {
        String title = "Save your password";
        String text = "Please make sure to save your password as there's no way to recover it.";

        SweetAlertDialog dialog = DialogFactory.getNormalDialog(mActivity, title,
                text, true, (d) -> {
                    d.dismiss();
                    mActivity.startSignup(username, password, repeatPassword, e -> handleAuthError(e));
                });
        dialog.show();
    }



    private void handleAuthError(Throwable e) {
        String message = "There was a connection problem. Please check your connection and try again.";
        if (e instanceof HttpException) {
            try {
                message = getApiError((HttpException)e);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        displayErrorMessage(message);
    }


    private String getApiError(HttpException e) throws Exception {
        ApiError error = Formatter.extractErrorData(e);
        return error.getMessage();
    }


    private boolean inputsEmpty() {
        if (InputValidator.inputNotEmpty(mUsernameEditText)
                && InputValidator.inputNotEmpty(mPassEditText)
                && InputValidator.inputNotEmpty(mPassRepeatEditText))
            return false;
        else {
            displayErrorMessage("Username and password must be at least 3 characters");
            return true;
        }
    }


    private boolean invalidCharacters(String username, String password, String repeatPassword) {
        if (InputValidator.hasSpecialCharacters(username)
                || InputValidator.hasSpecialCharacters(password)
                || InputValidator.hasSpecialCharacters(repeatPassword)) {
            displayErrorMessage("Invalid characters. Please use only letters and numbers. No spaces");
            return true;
        }

        return false;
    }


    private void displayErrorMessage(String text) {
        mScrollView.scrollTo(0,0);
        mUsernameEditText.requestFocus();
        mUsernameInputLayout.setError(text);
        mPassInputLayout.setError(" ");
        mPassRepeatInputLayout.setError(" ");
    }


    private boolean passwordsMatch() {
        String passText = mPassEditText.getText() + "";
        String passRepeatText = mPassRepeatEditText.getText() + "";
        boolean match = passText.equals(passRepeatText);
        if (!match) {
            mPassEditText.requestFocus();
            mPassInputLayout.setError("Passwords do not match");
            mPassRepeatInputLayout.setError(" ");
        }

        return match;
    }


    @OnTextChanged(R.id.signupUsernameInput)
    public void onUsernameChanged() {
        removeError();
    }


    @OnTextChanged(R.id.signupPassInput)
    public void onPassChanged() {
        removeError();
    }


    @OnTextChanged(R.id.signupPassRepeatInput)
    public void onPassRepeatChanged() {
        removeError();
    }


    private void removeError() {
        mUsernameInputLayout.setError(null);
        mPassInputLayout.setError(null);
        mPassRepeatInputLayout.setError(null);
    }


    @OnClick(R.id.signupLoginLinkTextView)
    public void onLoginLinkClicked() {
        mActivity.startFragment(LoginFragment.newInstance(mActivity));
    }
}
