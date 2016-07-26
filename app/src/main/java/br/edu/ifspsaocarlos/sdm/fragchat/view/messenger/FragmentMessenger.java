package br.edu.ifspsaocarlos.sdm.fragchat.view.messenger;

import android.app.NotificationManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.edu.ifspsaocarlos.sdm.fragchat.R;
import br.edu.ifspsaocarlos.sdm.fragchat.models.UserModel;
import br.edu.ifspsaocarlos.sdm.fragchat.utils.ContactsAdapter;
import br.edu.ifspsaocarlos.sdm.fragchat.utils.DBHelper;
import br.edu.ifspsaocarlos.sdm.fragchat.utils.DialogService;

/**
 * Created by LeonardoAlmeida on 15/07/16.
 */
public class FragmentMessenger extends Fragment {

    private static final String TAG = "MessengerFragment";
    //private static final String uriMainPath = "android.resource://br.edu.ifspsaocarlos.sdm.fragchat/drawable/";

    TextView _userProfileId;
    TextView _userProfileName;
    TextView _userProfileEmail;
    TextView _userProfileBio;
    TextView _userProfileDetalhes;
    ImageView _userProfileAvatar;
    ImageView _userProfileHeader;
    ListView _listView;
    EditText _inputSearch;

    private DBHelper _localdb;
    private UserModel _loggedUserProfile;

    ContactsAdapter adapter;  // Listview Adapter
    private List<UserModel> _contactList; // ArrayList for Listview
    private NotificationManager _notificationMng;

    private static FragmentMessenger _fragment;
    private static Context _context;

    public static FragmentMessenger newInstance(Context ctx) {
        if (_fragment == null) {
            _fragment = new FragmentMessenger();
        }
        _context = ctx;
        return _fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment_msgchat, container, false);

        final ActivityMain activity = ((ActivityMain) getActivity());

        _notificationMng = activity.mNotificationManager;

        _localdb = activity.getLocaldb();

        _userProfileId = (TextView) view.findViewById(R.id.user_id);
        _userProfileName = (TextView) view.findViewById(R.id.user_profile_name);
        _userProfileAvatar = (ImageView) view.findViewById(R.id.user_profile_photo);
        _userProfileHeader = (ImageView) view.findViewById(R.id.header_cover_image);
        _loggedUserProfile = activity.getLoggedUserProfile();


        _inputSearch = (EditText) view.findViewById(R.id.inputSearch);
        _inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                FragmentMessenger.this.adapter.getFilter().filter(cs);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });

        if (_loggedUserProfile.getId() > 0) {
            String textIDDisplay = getString(R.string.id) + Long.toString(_loggedUserProfile.getId());
            _userProfileId.setText(textIDDisplay);
            _userProfileName.setText(_loggedUserProfile.getNome());
            _userProfileAvatar.setImageResource(_loggedUserProfile.getImgAvatar());
            _userProfileHeader.setImageResource(_loggedUserProfile.getImgHeader());

            updateUserStatus(true);

        }
        return view;
    }

    @Override
    public void onDestroy() {
        updateUserStatus(false);
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();

        ((ActivityMain) getActivity()).mToolbarSubTitle.setText(R.string.contatos);
        loadContactList();
        loadUserList();

        FragmentMessenger.this.adapter.getFilter().filter(_inputSearch.getText().toString());
    }

    /**
     * Update User Status.
     */
    public void updateUserStatus(boolean online) {
        Log.d(TAG, "UpdateUserStatus");

        _loggedUserProfile.setStatus(UserModel.STATUS_ONLINE);

        updateUserStatusRequest();
    }

    /**
     * Load list of users.
     */
    private void loadUserList() {
        Log.d(TAG, "LoadUserList");

        _listView = (ListView) getView().findViewById(R.id.userList);

        adapter = new ContactsAdapter(_context, _contactList);
        _listView.setAdapter(adapter);
        _listView.setTextFilterEnabled(true);
        _listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0,
                                    View arg1, int pos, long arg3) {

                ((ActivityMain) getActivity()).startService(ActivityDefault.RECEIVER_SERVICE, adapter.getItem(pos).getId());

                Bundle dataBundle = new Bundle();
                dataBundle.putLong(FragmentMessengerChat.RECEIVER, adapter.getItem(pos).getId());
                Fragment fragmento = FragmentMessengerChat.newInstance(_context);
                fragmento.setArguments(dataBundle);
                getFragmentManager().beginTransaction()
                        .add(R.id.content_frame, fragmento,
                                adapter.getItem(pos).getNome())
                        .addToBackStack(null).commit();
            }
        });
    }

    private void loadContactList() {
        ((ActivityMain) getActivity()).showProgressBar();

        _contactList = _localdb.getContactlist(_loggedUserProfile.getId());

        if (_contactList != null)
        {
            if (_contactList.size() == 0)
                Toast.makeText(_context,
                        R.string.msg_no_user_found,
                        Toast.LENGTH_SHORT).show();
        }
        else
        {
            DialogService.showDialog(_context,
                    getString(R.string.err_users));
        }

        ((ActivityMain) getActivity()).dismissProgressBar();
    }

    private void updateUserStatusRequest(){

        ((ActivityMain) getActivity()).showProgressBar();

        boolean status = _localdb.updateUserStatus(_loggedUserProfile.getId(), _loggedUserProfile.isOnline());

        if (status) {
            Toast.makeText(getActivity().getApplicationContext(), "Status alterado", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getActivity().getApplicationContext(), "Falha de sincronização", Toast.LENGTH_SHORT).show();
        }

        ((ActivityMain) getActivity()).dismissProgressBar();
    };
}
