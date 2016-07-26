package br.edu.ifspsaocarlos.sdm.fragchat.view.messenger;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import br.edu.ifspsaocarlos.sdm.fragchat.R;
import br.edu.ifspsaocarlos.sdm.fragchat.models.UserModel;
import br.edu.ifspsaocarlos.sdm.fragchat.utils.DBHelper;
import br.edu.ifspsaocarlos.sdm.fragchat.utils.DialogService;

/**
 * Created by LeonardoAlmeida on 02/06/16.
 */
public class FragmentProfile extends Fragment{

    private static final String TAG = "ProfileFragment";
    private DBHelper _localdb;

    public static final short PROFILE_MAINUSER = 0;
    public static final short PROFILE_USERCONTACT = 1;

    UserModel _loggedUserProfile;

    TextView _userProfileName;
    TextView _userProfileEmail;
    TextView _userProfileID;
    TextView _userProfileApelido;
    TextView _userProfileBio;
    TextView _userProfileDetalhes;

    ImageView _headerImageView;
    ImageView _avatarImageView;

    private static Context context;
    private static FragmentProfile fragmentMainUser;
    private static FragmentProfile fragmentContacts;

    public static FragmentProfile newInstance(Context ctx, int instanceType) {

        context = ctx;

        if (instanceType == PROFILE_MAINUSER){
            if (fragmentMainUser == null) {
                fragmentMainUser = new FragmentProfile();
            }
            return fragmentMainUser;
        }else{
            fragmentContacts = new FragmentProfile();
            return fragmentContacts;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment_profile, null);

        _userProfileName = (TextView) view.findViewById(R.id.user_profile_name);
        _userProfileEmail = (TextView) view.findViewById(R.id.user_profile_email);
        _headerImageView = (ImageView) view.findViewById(R.id.header_cover_image);
        _avatarImageView = (ImageView) view.findViewById(R.id.user_profile_photo);
        _userProfileID = (TextView) view.findViewById(R.id.input_id);
        _userProfileApelido = (TextView) view.findViewById(R.id.input_apelido);
        _userProfileBio = (TextView) view.findViewById(R.id.input_short_bio);
        _userProfileDetalhes = (TextView) view.findViewById(R.id.input_detalhes);

        ActivityMain activity = ((ActivityMain) getActivity());
        activity.mToolbarSubTitle.setText(R.string.profile);

        _localdb = new DBHelper(getActivity().getApplicationContext());

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            long Value = bundle.getLong("id");
            if (Value > 0) {
                _loggedUserProfile = _localdb.getContactProfile(Value);

                String printedText = "";

                _userProfileName.setText(_loggedUserProfile.getNome());
                _userProfileEmail.setText(_loggedUserProfile.getEmail());

                printedText = "ID: " + _loggedUserProfile.getId();
                _userProfileID.setText(printedText);
                printedText = "Apelido: " + _loggedUserProfile.getApelido();
                _userProfileApelido.setText(printedText);
                printedText = "Short Bio: " + _loggedUserProfile.getBio();
                _userProfileBio.setText(printedText);
                printedText = "Detalhes: " + _loggedUserProfile.getDetalhes();
                _userProfileDetalhes.setText(printedText);

                _headerImageView.setImageResource(_loggedUserProfile.getImgHeader());
                _avatarImageView.setImageResource(_loggedUserProfile.getImgAvatar());
            }
        }

        return view;
    }

}
