package br.edu.ifspsaocarlos.sdm.fragchat.view.otherActivities;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import br.edu.ifspsaocarlos.sdm.fragchat.R;
import br.edu.ifspsaocarlos.sdm.fragchat.models.UserModel;
import br.edu.ifspsaocarlos.sdm.fragchat.models.UserSecurityModel;
import br.edu.ifspsaocarlos.sdm.fragchat.models.UserTokenModel;
import br.edu.ifspsaocarlos.sdm.fragchat.utils.DBHelper;

/**
 * Created by LeonardoAlmeida on 10/06/16.
 */
public class ActivityRunOnUIThread extends Activity implements OnClickListener {

    private DBHelper localdb ;

    UserModel userProfile;
    UserTokenModel userToken;

    FrameLayout _userViewID;
    TextView _userid;
    EditText _nameText;
    EditText _emailText;
    EditText _bioText;
    EditText _detalhesText;
    EditText _oldpasswordText;
    EditText _passwordText;
    EditText _passwordText2;
    FloatingActionButton _signupButton;
    FloatingActionButton _updateButton;
    ProgressBar _spinner;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geral);

        localdb = new DBHelper(this);

        _userViewID = (FrameLayout) findViewById(R.id.viewUserID);
        _userid = (TextView) findViewById(R.id.user_id);
        _nameText = (EditText) findViewById(R.id.input_name);
        _emailText = (EditText) findViewById(R.id.input_email);
        _bioText = (EditText) findViewById(R.id.input_bio);
        _detalhesText = (EditText) findViewById(R.id.input_detalhes);
        _oldpasswordText = (EditText) findViewById(R.id.input_oldpassword);
        _passwordText = (EditText) findViewById(R.id.input_password);
        _passwordText2 = (EditText) findViewById(R.id.input_password2);
        _signupButton = (FloatingActionButton) findViewById(R.id.fab_signup);
        _signupButton.setOnClickListener(this);
        _updateButton = (FloatingActionButton) findViewById(R.id.fab_update);
        _updateButton.setOnClickListener(this);
        _spinner = (ProgressBar)findViewById(R.id.pbHeaderProgress);
        _spinner.setVisibility(View.INVISIBLE);

        _userViewID.setEnabled(false);

        //register a new contact.
        _signupButton.setVisibility(View.VISIBLE);
        _signupButton.setEnabled(true);
        _signupButton.setClickable(true);

        //update a new contact.
        _updateButton.setVisibility(View.INVISIBLE);
        _updateButton.setEnabled(false);
        _updateButton.setClickable(false);

        //_userid.setVisibility(View.GONE);

        _nameText.setEnabled(true);
        _nameText.setFocusable(true);
        _nameText.setClickable(true);

        _emailText.setEnabled(true);
        _emailText.setFocusable(true);
        _emailText.setClickable(true);

        _bioText.setEnabled(true);
        _bioText.setFocusable(true);
        _bioText.setClickable(true);

        _bioText.setEnabled(true);
        _detalhesText.setFocusable(true);
        _detalhesText.setClickable(true);

        _oldpasswordText.setVisibility(View.GONE);

        _passwordText.setEnabled(true);
        _passwordText.setFocusableInTouchMode(true);
        _passwordText.setClickable(true);

        _passwordText2.setEnabled(true);
        _passwordText2.setFocusableInTouchMode(true);
        _passwordText2.setClickable(true);

    }

    public void onClick(View v) {
        if (v == _signupButton) {
            new Thread(){
                public void run() {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            signup();
                        }
                    });
                } }.start();
        }else if(v == _updateButton) {
            new Thread() {
                public void run() {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            update();
                        }
                    });
                }
            }.start();
        }
    }

    public void signup() {

        if (!validate()) {
            onSignupFailed();
            return;
        }

        // final ProgressDialog progressDialog = new ProgressDialog(getApplicationContext(),
        //R.style.AppTheme_Dark_Dialog);
        //progressDialog.setIndeterminate(true);
        //progressDialog.setMessage("Creating Account...");
        //progressDialog.show();

        userProfile = new UserModel(
                _nameText.getText().toString(),
                _emailText.getText().toString()
        );
        userProfile.setBio(_bioText.getText().toString());
        userProfile.setDetalhes(_detalhesText.getText().toString());
        userProfile.setImgHeader(R.drawable.profile_header5);
        userProfile.setImgAvatar(R.drawable.profile_demo_2);

        UserSecurityModel userSec= new UserSecurityModel(userProfile.getId(),
                userProfile.getEmail(),
                _passwordText.getText().toString());

        userToken = localdb.updateContact(userProfile, userSec);

        userProfile.setId(userToken.getUserID());

        if( userProfile.getId() > -1){
            Toast.makeText(getApplicationContext(), "Usuário criado: " + Long.toString(userProfile.getId()), Toast.LENGTH_SHORT).show();
        }

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        onSignupSuccess(userProfile);
                        _spinner.setVisibility(View.INVISIBLE);
                    }
                }, 3000);
    }

    public void onSignupSuccess(UserModel userProfile) {

        _signupButton.setEnabled(false);
        _signupButton.setFocusable(false);
        _signupButton.setClickable(false);
        _signupButton.setImageResource(android.R.drawable.ic_menu_edit);
        //_signupButton.setBackgroundTintList(contextInstance.getResources().getColorStateList(R.color.your_xml_name));

        _userViewID.setVisibility(View.VISIBLE);
        _userid.setText(Long.toString(userProfile.getId()));

        _nameText.setText((CharSequence) userProfile.getNome());
        _nameText.setEnabled(false);
        _nameText.setFocusable(false);
        _nameText.setClickable(false);

        _emailText.setText((CharSequence) userProfile.getEmail());
        _emailText.setEnabled(false);
        _emailText.setFocusable(false);
        _emailText.setClickable(false);

        _bioText.setText((CharSequence) userProfile.getBio());
        _bioText.setEnabled(true);
        _bioText.setFocusable(true);
        _bioText.setClickable(true);

        _detalhesText.setText((CharSequence) userProfile.getDetalhes());
        _detalhesText.setEnabled(true);
        _detalhesText.setFocusable(true);
        _detalhesText.setClickable(true);

        _passwordText.setEnabled(false);
        _passwordText.setFocusable(false);
        _passwordText.setClickable(false);

        _passwordText2.setVisibility(View.INVISIBLE);
        _oldpasswordText.setVisibility(View.INVISIBLE);
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Falha ao registrar usuário", Toast.LENGTH_LONG).show();
        _spinner.setVisibility(View.INVISIBLE);
    }

    public void update() {

        userProfile.setBio(_bioText.getText().toString());
        userProfile.setDetalhes(_detalhesText.getText().toString());

        if( localdb.updateUserProfile(userProfile)){
            Toast.makeText(getApplicationContext(), "Usuário alterado: " + Long.toString(userProfile.getId()), Toast.LENGTH_SHORT).show();
        }

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        onUpdateSuccess(userProfile);

                    }
                }, 3000);
    }

    public void onUpdateSuccess(UserModel userProfile) {
        _spinner.setVisibility(View.INVISIBLE);_spinner.setVisibility(View.INVISIBLE);
    }


    public boolean validate() {
        boolean valid = true;

        String name = _nameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();
        String password2 = _passwordText2.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("at least 3 characters");
            valid = false;
        } else {
            _nameText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10 || !password.equals(password2) ) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }
}
