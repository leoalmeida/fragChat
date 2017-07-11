package br.edu.ifspsaocarlos.sdm.fragchat.view.messenger;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.edu.ifspsaocarlos.sdm.fragchat.R;
import br.edu.ifspsaocarlos.sdm.fragchat.models.UserModel;
import br.edu.ifspsaocarlos.sdm.fragchat.models.UserTokenModel;
import br.edu.ifspsaocarlos.sdm.fragchat.utils.DBHelper;
import br.edu.ifspsaocarlos.sdm.fragchat.utils.DialogService;

/**
 * Created by LeonardoAlmeida on 07/06/16.
 */
public class FragmentLoginAuth extends Fragment {
    private static final String TAG = "PrincipalActivity";
    private static final int REQUEST_SIGNUP = 0;
    int from_Where_I_Am_Coming = 0;
    private DBHelper localdb ;

    UserModel _loggedUserProfile;
    UserTokenModel _loggedUserToken;
    EditText _emailText;
    EditText _passwordText;
    Button _loginButton;
    TextView _signupLink;
    FloatingActionButton _fabMoreButton;

    private static FragmentLoginAuth _fragment;
    private static Context _context;

    public static FragmentLoginAuth newInstance(Context ctx){

        _fragment = new FragmentLoginAuth();
        _context = ctx;
        return _fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.main_fragment_auth, null);
        ActivityMain activity = ((ActivityMain) getActivity());
        activity.mToolbarSubTitle.setText(R.string.autenticacao);

        _emailText = (EditText) view.findViewById(R.id.input_email);
        _passwordText = (EditText) view.findViewById(R.id.input_password);
        _signupLink = (TextView) view.findViewById(R.id.link_signup);
        _loginButton = (Button) view.findViewById(R.id.btn_login);


        _emailText.setText("teste@teste.com");
        _passwordText.setText("");

        localdb = new DBHelper(_context);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            int Value = bundle.getInt("id");

            if (Value > 0) {
                //means this is the view part not the add contact part.
                _loggedUserProfile = localdb.getContactProfile(Value);
                if (_loggedUserProfile == null) return view;

                _loggedUserToken = localdb.getUserToken(_loggedUserProfile.getId());

                if (validateToken(_loggedUserToken)) activity.setLoggedUser(_loggedUserProfile, _loggedUserToken);

                return view;

            }
        }

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Fragment fragment =  FragmentSignup.newInstance(_context);
                Bundle dataBundle = new Bundle();
                dataBundle.putInt("id", -1);
                fragment.setArguments(dataBundle);

                getFragmentManager().beginTransaction()
                        .add(R.id.content_frame, fragment, getString(R.string.register)).addToBackStack(null).commit();
                //Intent intent = new Intent(activity.getApplicationContext(), SignupFragment.class);
                //startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((ActivityMain) getActivity()).mToolbarSubTitle.setText(R.string.autenticacao);
    }

    private boolean validateToken(UserTokenModel token){
        if (token == null) return false;
        return localdb.validateToken(token);
    };

    public boolean login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed("Erro de validação");
            return false;
        }

        /*if (localdb.getAppStatus(0) == DBHelper.APPSTATUS_LOADING) {
            Toast.makeText(this.getContext(), "Atualizando contatos, Espere um momento...", Toast.LENGTH_LONG).show();
        }else {*/

            ((ActivityMain) getActivity()).showProgressBar();
            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            // On complete call either onLoginSuccess or onLoginFailed

                            String email = _emailText.getText().toString();

                            _loggedUserProfile = localdb.validateContact(email);

                            if (_loggedUserProfile == null) {
                                onLoginFailed("Usuário não cadastrado...");
                                ((ActivityMain) getActivity()).dismissProgressBar();
                                return;
                            }

                            if (!validateToken(_loggedUserToken))
                                _loggedUserToken = localdb.getUserToken(_loggedUserProfile.getId());

                            if (_loggedUserToken == null) {
                                onLoginFailed("Cadastro desatualizado...");
                            }else {
                                ((ActivityMain) getActivity()).dismissProgressBar();
                                onLoginSuccess();
                            }

                        }
                    }, 0);
        //}
        return true;
    }

    public void onLoginSuccess() {
        _loginButton.setEnabled(false);
        ActivityMain activity = ((ActivityMain) getActivity());
        activity.setLoggedUser(_loggedUserProfile, _loggedUserToken);

        Toast.makeText(activity.getApplicationContext(), "Acesso liberado", Toast.LENGTH_SHORT).show();

        Bundle dataBundle = new Bundle();
        dataBundle.putLong("id", _loggedUserProfile.getId());
        dataBundle.putLong("token", _loggedUserToken.getId());

        //Fragment fragmento = FragmentProfile.newInstance(_context, FragmentProfile.PROFILE_MAINUSER);

        Fragment fragmento = FragmentMessenger.newInstance(_context);

        fragmento.setArguments(dataBundle);

        getFragmentManager().beginTransaction()
                .replace(R.id.content_frame, fragmento, getString(R.string.profile)).addToBackStack(null).commit();
    }

    public void onLoginFailed(String msg) {
        Toast.makeText(getActivity().getBaseContext(), msg, Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }

}
