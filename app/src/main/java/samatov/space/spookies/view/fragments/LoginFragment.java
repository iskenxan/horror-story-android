package samatov.space.spookies.view.fragments;

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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ConsoleMessage;
import android.widget.EditText;
import android.widget.TextView;

import java.io.Console;
import java.util.logging.ConsoleHandler;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import samatov.space.spookies.AuthActivity;
import samatov.space.spookies.R;
import samatov.space.spookies.model.utils.InputValidator;
import samatov.space.spookies.model.utils.api.beans.Auth;

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
        if (InputValidator.inputNotEmpty(mUsernameEditText) && InputValidator.inputNotEmpty(mPasswordEditText)) {
            String username = mUsernameEditText.getText() + "";
            String password = mPasswordEditText.getText() + "";
            Auth.login(username, password)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Auth>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(Auth auth) {
                            Log.d("TEST", auth.getToken());
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        } else {
            mUsernameEditText.requestFocus();
            displayErrorMessage("Username and password must be at least 3 characters");
        }
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
