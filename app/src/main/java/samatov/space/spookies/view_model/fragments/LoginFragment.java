package samatov.space.spookies.view_model.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import retrofit2.HttpException;
import samatov.space.spookies.R;
import samatov.space.spookies.model.api.beans.ApiError;
import samatov.space.spookies.model.utils.Formatter;
import samatov.space.spookies.model.utils.InputValidator;
import samatov.space.spookies.view_model.activities.AuthActivity;

public class LoginFragment extends Fragment {

    public static LoginFragment newInstance(FragmentActivity activity) {
        LoginFragment fragment = new LoginFragment();
        fragment.setEnterTransition(TransitionInflater.from(activity)
                .inflateTransition(android.R.transition.slide_left));
        fragment.setExitTransition(TransitionInflater.from(activity)
                .inflateTransition(android.R.transition.slide_left));
        return fragment;
    }


    AuthActivity mActivity;


    @BindView(R.id.loginSignupLinkTextView) TextView mSignupTextView;
    @BindView(R.id.loginUsernameInput) EditText mUsernameEditText;
    @BindView(R.id.loginPassInput) EditText mPasswordEditText;
    @BindView(R.id.loginUsernameInputLayout) TextInputLayout mUsernameInputLayout;
    @BindView(R.id.loginPassInputLayout) TextInputLayout mPasswordInputLayout;



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, view);
        this.mActivity = (AuthActivity) getActivity();
        styleSignupLink();

        mUsernameEditText.requestFocus();

       return view;
    }


    private void styleSignupLink() {
        Spannable span = new SpannableString("Not a user yet? Sign up");
        span.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorLink)),
                16, span.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        span.setSpan(new StyleSpan(Typeface.BOLD),
                16, span.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        mSignupTextView.setText(span);
    }


    @OnClick(R.id.loginButton)
    public void onLoginButtonClicked() {
        if (InputValidator.inputNotEmpty(mUsernameEditText) && InputValidator.inputNotEmpty(mPasswordEditText))
            requestLogin();
        else {
            mUsernameEditText.requestFocus();
            displayErrorMessage("Username and password must be at least 3 characters");
        }
    }


    private void requestLogin() {
        String username = mUsernameEditText.getText() + "";
        String password = mPasswordEditText.getText() + "";
        mActivity.startLogin(username, password, e -> handleLoginError(e));
    }


    private void handleLoginError(Throwable e) {
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


    private void displayErrorMessage(String message) {
        mUsernameInputLayout.setError(message);
        mPasswordInputLayout.setError(" ");
    }


    @OnTextChanged(R.id.loginUsernameInput)
    public void onUsernameInputChanged() {
        removeErrorMessage();
    }


    @OnTextChanged(R.id.loginPassInput)
    public void onPassInputChanged() {
        removeErrorMessage();
    }


    private void removeErrorMessage() {
        mUsernameInputLayout.setError(null);
        mPasswordInputLayout.setError(null);
    }


    @OnClick(R.id.loginSignupLinkTextView)
    public void onSignupLinkClicked() {
        mActivity.startFragment(SignupFragment.newInstance(getActivity()));
    }
}
