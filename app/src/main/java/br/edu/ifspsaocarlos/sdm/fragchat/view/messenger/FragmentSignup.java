package br.edu.ifspsaocarlos.sdm.fragchat.view.messenger;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import br.edu.ifspsaocarlos.sdm.fragchat.R;
import br.edu.ifspsaocarlos.sdm.fragchat.models.UserModel;
import br.edu.ifspsaocarlos.sdm.fragchat.models.UserSecurityModel;
import br.edu.ifspsaocarlos.sdm.fragchat.models.UserTokenModel;
import br.edu.ifspsaocarlos.sdm.fragchat.utils.DBHelper;

/**
 * Created by LeonardoAlmeida on 07/06/16.
 */
public class FragmentSignup extends Fragment {
    private static final String TAG = "SignupFragment";

    long userid = 0;
    private DBHelper localdb ;

    UserModel userProfile;
    UserTokenModel userToken;
    UserSecurityModel userSec;

    TextView _userid;
    EditText _nameText;
    EditText _emailText;
    EditText _apelidoText;
    RadioGroup _avatarRadio;
    RadioButton _avatarRadioButton;
    RadioGroup _headerImgRadio;
    RadioButton _headerRadioButton;
    EditText _bioText;
    EditText _detalhesText;
    EditText _oldpasswordText;
    EditText _passwordText;
    EditText _passwordText2;
    FloatingActionButton _fabSignup;
    FloatingActionButton _fabUpdate;
    FloatingActionButton _fabCancel;
    FrameLayout _viewUserID;


    private static FragmentSignup _fragment;
    private static Context context;

    public static FragmentSignup newInstance(Context ctx){
        if (_fragment == null) {
            _fragment = new FragmentSignup();
        }
        context = ctx;
        return _fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment_signup, null);

        ActivityMain activity = (ActivityMain) getActivity();

        activity.mToolbarSubTitle.setText(R.string.register);

        _userid = (TextView) view.findViewById(R.id.user_id);
        _viewUserID = (FrameLayout) view.findViewById(R.id.viewUserID);
        _nameText = (EditText) view.findViewById(R.id.input_name);
        _emailText = (EditText) view.findViewById(R.id.input_email);
        _apelidoText = (EditText) view.findViewById(R.id.input_apelido);
        _avatarRadio = (RadioGroup) view.findViewById(R.id.avatar_list);
        _headerImgRadio = (RadioGroup) view.findViewById(R.id.header_list);
        _bioText = (EditText) view.findViewById(R.id.input_bio);
        _detalhesText = (EditText) view.findViewById(R.id.input_detalhes);
        _oldpasswordText = (EditText) view.findViewById(R.id.input_oldpassword);
        _passwordText = (EditText) view.findViewById(R.id.input_password);
        _passwordText2 = (EditText) view.findViewById(R.id.input_password2);
        _fabSignup = (FloatingActionButton) view.findViewById(R.id.fab_signup);
        _fabUpdate = (FloatingActionButton) view.findViewById(R.id.fab_update);
        _fabCancel = (FloatingActionButton) view.findViewById(R.id.fab_cancel);

        localdb = new DBHelper(context);

        _nameText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
            if(!hasFocus) {
                if (!FragmentSignup.this._nameText.equals("") && FragmentSignup.this._emailText.equals("")){
                    FragmentSignup.this.userProfile =
                            localdb.findContactByName(FragmentSignup.this._nameText.getText().toString());

                    if (FragmentSignup.this.userProfile != null) {
                        userid = userProfile.getId();
                        _nameText.setText(userProfile.getNome());
                        _emailText.setText(userProfile.getEmail());
                        _apelidoText.setText(userProfile.getApelido());
                        _fabSignup.setVisibility(View.GONE);
                        _fabSignup.setClickable(false);
                        _fabUpdate.setVisibility(View.VISIBLE);
                        _fabUpdate.setClickable(true);
                        _userid.setVisibility(View.VISIBLE);
                        _userid.setText(String.valueOf(userid));
                    }else{
                        _fabSignup.setVisibility(View.VISIBLE);
                        _fabSignup.setClickable(true);
                        _fabUpdate.setVisibility(View.GONE);
                        _fabUpdate.setClickable(false);
                        _userid.setVisibility(View.GONE);
                        _userid.setText("");
                    }
                }
            }
            }
        });

        _emailText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
            if(!hasFocus) {
                if (!FragmentSignup.this._emailText.equals("")){
                    FragmentSignup.this.userProfile =
                            localdb.findContactByEmail(FragmentSignup.this._emailText.getText().toString());

                    if (FragmentSignup.this.userProfile != null) {
                        userid = userProfile.getId();
                        _nameText.setText(userProfile.getNome());
                        _emailText.setText(userProfile.getEmail());
                        _apelidoText.setText(userProfile.getApelido());
                        _detalhesText.setText(userProfile.getDetalhes());
                        _bioText.setText(userProfile.getBio());
                        _fabSignup.setVisibility(View.GONE);
                        _fabSignup.setClickable(false);
                        _fabUpdate.setVisibility(View.VISIBLE);
                        _fabUpdate.setClickable(true);
                        _userid.setVisibility(View.VISIBLE);
                        _userid.setText(String.valueOf(userid));
                    }else{
                        _fabSignup.setVisibility(View.VISIBLE);
                        _fabSignup.setClickable(true);
                        _fabUpdate.setVisibility(View.GONE);
                        _fabUpdate.setClickable(false);
                        _userid.setVisibility(View.GONE);
                        _userid.setText("");
                    }
                }
            }
            }
        });


        _fabUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update();
            }
        });

        _fabSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        _fabCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                getFragmentManager().popBackStack();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        _userid.setText("");
        _nameText.setText("");
        _emailText.setText("");
        _apelidoText.setText("");
        _avatarRadio.clearCheck();
        _headerImgRadio.clearCheck();
        _bioText.setText("");
        _detalhesText.setText("");
        _oldpasswordText.setText("");
        _passwordText.setText("");
        _passwordText2.setText("");

        userProfile = ((ActivityMain)getActivity()).getLoggedUserProfile();
        if (userProfile != null) {
            //update contact only.
            _fabSignup.setVisibility(View.GONE);
            _fabSignup.setClickable(false);
            _fabUpdate.setVisibility(View.VISIBLE);
            _fabUpdate.setClickable(true);


            _userid.setVisibility(View.VISIBLE);
            _userid.setText(String.valueOf(userProfile.getId()));

            _nameText.setText((CharSequence) userProfile.getNome());
            _nameText.setFocusable(false);
            _nameText.setClickable(false);

            _emailText.setText((CharSequence) userProfile.getEmail());
            _emailText.setFocusable(false);
            _emailText.setClickable(false);

            _apelidoText.setText((CharSequence) userProfile.getApelido());
            _apelidoText.setFocusable(true);
            _apelidoText.setClickable(true);

            _bioText.setText((CharSequence) userProfile.getBio());
            _bioText.setFocusable(true);
            _bioText.setClickable(true);

            _detalhesText.setText((CharSequence) userProfile.getDetalhes());
            _detalhesText.setFocusable(true);
            _detalhesText.setClickable(true);

            _oldpasswordText.setVisibility(View.VISIBLE);

        }else {//register a new contact.
            _fabSignup.setVisibility(View.VISIBLE);
            _fabSignup.setClickable(true);
            _fabUpdate.setVisibility(View.GONE);
            _fabUpdate.setClickable(false);

            _viewUserID.setVisibility(View.GONE);
            _userid.setVisibility(View.GONE);

            _nameText.setEnabled(true);
            _nameText.setFocusable(true);
            _nameText.setClickable(true);

            _emailText.setEnabled(true);
            _emailText.setFocusable(true);
            _emailText.setClickable(true);

            _apelidoText.setEnabled(true);
            _apelidoText.setFocusable(true);
            _apelidoText.setClickable(true);

            _bioText.setEnabled(true);
            _bioText.setFocusable(true);
            _bioText.setClickable(true);

            _bioText.setEnabled(true);
            _detalhesText.setFocusable(true);
            _detalhesText.setClickable(true);

            _oldpasswordText.setFocusable(false);
            _oldpasswordText.setVisibility(View.GONE);

            _passwordText.setEnabled(true);
            _passwordText.setFocusableInTouchMode(true);
            _passwordText.setClickable(true);

            _passwordText2.setEnabled(true);
            _passwordText2.setFocusableInTouchMode(true);
            _passwordText2.setClickable(true);
        }
    }

    public void update() {
        Log.d(TAG, "Update");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        _fabUpdate.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(context,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Criando conta...");
        progressDialog.show();

        userProfile = new UserModel();

        userProfile.setId(userid);
        userProfile.setNome(_nameText.getText().toString());
        userProfile.setEmail(_emailText.getText().toString());
        userProfile.setApelido(_apelidoText.getText().toString());
        userProfile.setDetalhes(_detalhesText.getText().toString());
        userProfile.setBio(_bioText.getText().toString());
        userProfile.setStatus(UserModel.STATUS_ONLINE);

        int selectedId = 0;

        selectedId = _headerImgRadio.getCheckedRadioButtonId();// get selected radio button from radioGroup
        _headerRadioButton = (RadioButton) getView().findViewById(selectedId);// find the radiobutton by returned id
        userProfile.setImgHeader(context.getResources().getIdentifier("profile_header"+_headerRadioButton.getText(), "drawable", context.getPackageName()));

        selectedId = _avatarRadio.getCheckedRadioButtonId();
        _avatarRadioButton = (RadioButton) getView().findViewById(selectedId);
        userProfile.setImgAvatar(context.getResources().getIdentifier("profile_demo_"+_avatarRadioButton.getText(), "drawable", context.getPackageName()));

        if (_passwordText.getText().toString() != ""){
            userSec = new UserSecurityModel(userProfile.getId(),
                    userProfile.getEmail(), _passwordText.getText().toString());
        }

        userToken = localdb.updateContact(userProfile, userSec);


        if(localdb.updateUserProfile(userProfile)){
            Toast.makeText(getActivity().getApplicationContext(), "Conta Alterada", Toast.LENGTH_SHORT).show();
        }

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        onSignupSuccess();
                        // onSignupFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        _fabSignup.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(context,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Criando conta...");
        progressDialog.show();

        userProfile = new UserModel();

        userProfile.setNome(_nameText.getText().toString());
        userProfile.setEmail(_emailText.getText().toString());
        userProfile.setApelido(_apelidoText.getText().toString());
        userProfile.setDetalhes(_detalhesText.getText().toString());
        userProfile.setBio(_bioText.getText().toString());

        int selectedId = 0;
        selectedId = _headerImgRadio.getCheckedRadioButtonId();// get selected radio button from radioGroup
        _headerRadioButton = (RadioButton) getView().findViewById(selectedId);// find the radiobutton by returned id
        userProfile.setImgHeader(context.getResources().getIdentifier("profile_header"+_headerRadioButton.getText(), "drawable", context.getPackageName()));

        selectedId = _avatarRadio.getCheckedRadioButtonId();
        _avatarRadioButton = (RadioButton) getView().findViewById(selectedId);
        userProfile.setImgAvatar(context.getResources().getIdentifier("profile_demo_"+_avatarRadioButton.getText(), "drawable", context.getPackageName()));

        userSec= new UserSecurityModel(userProfile.getId(),
                userProfile.getEmail(),
                _passwordText.getText().toString());

        if(userProfile.getId()>-1){
            Toast.makeText(getActivity().getApplicationContext(), "Usuário criado: " + Long.toString(userProfile.getId()), Toast.LENGTH_SHORT).show();
        }

        new Thread(){
            public void run(){
                RequestQueue queue = Volley.newRequestQueue(getActivity());
                String url = getString(R.string.url_base) + "/contato";
                String string = "{\"nome_completo\":\"" + userProfile.getNome() + "\"," +
                        "\"apelido\":\"" + userProfile.getApelido() + "\"}";

                Log.w("fragChat", "URL: " + url);
                Log.w("fragChat", "ATTRs: " + string);
                try {

                    final JSONObject jsonBody = new JSONObject(string);
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                            Request.Method.POST,
                            url,
                            jsonBody,
                            new Response.Listener<JSONObject>() {
                                public void onResponse(JSONObject sret) {
                                    try{
                                        Log.w("fragChat", "JSON: " + sret.toString());
                                        userProfile.setId(sret.getLong("id"));
                                    }catch (JSONException je){
                                        Toast.makeText(getActivity(), "Erro ao Receber id do usuário!",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                    getActivity().runOnUiThread(new Runnable() {
                                        public void run() {
                                            Log.w("fragChat", "Novo usuário: " + userProfile.getNome());
                                            userToken = localdb.updateContact(userProfile, userSec);
                                            Log.w("fragChat", "JSON: " + userToken.toString());
                                        }
                                    });
                                    Toast.makeText(getActivity(), "Usuário Cadastrado!",
                                            Toast.LENGTH_SHORT).show();
                                }
                            },
                            new Response.ErrorListener() {
                                public void onErrorResponse(VolleyError volleyError) {
                                    Toast.makeText(getActivity(), "Erro no envio da mensagem!", Toast.LENGTH_SHORT).show();
                                }
                            }
                    );
                    queue.add(jsonObjectRequest);
                }catch (Exception e ) {
                    Log.e("SDM", "Erro no envio da mensagem");
                }
            }
        }.start();

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        onSignupSuccess();
                        // onSignupFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }

    public void onSignupSuccess() {
        _fabSignup.setEnabled(false);
        this.getActivity().setResult(Activity.RESULT_OK, null);
        getFragmentManager().popBackStack();
    }

    public void onSignupFailed() {
        Toast.makeText(getActivity().getBaseContext(), "Falha ao registrar usuário", Toast.LENGTH_LONG).show();

        _fabSignup.setEnabled(true);
    }
    public boolean validateUpdate() {
        boolean valid = true;

        String oldpassword = _oldpasswordText.getText().toString();
        String password = _passwordText.getText().toString();
        String password2 = _passwordText2.getText().toString();

        if (password.isEmpty() || password.length() < 4 || password.length() > 10 || !password.equals(password2) || password.equals(oldpassword) ) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }
    public boolean validate() {
        boolean valid = true;

        String name = _nameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();
        String password2 = _passwordText2.getText().toString();

        if (name.isEmpty() || name.toCharArray().length < 3) {
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
