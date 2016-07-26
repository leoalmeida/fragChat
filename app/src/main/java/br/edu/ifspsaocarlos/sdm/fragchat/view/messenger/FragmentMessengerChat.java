package br.edu.ifspsaocarlos.sdm.fragchat.view.messenger;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageView;
import android.text.InputType;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import br.edu.ifspsaocarlos.sdm.fragchat.R;
import br.edu.ifspsaocarlos.sdm.fragchat.models.GroupMessagesModel;
import br.edu.ifspsaocarlos.sdm.fragchat.models.UserModel;
import br.edu.ifspsaocarlos.sdm.fragchat.models.UserTokenModel;
import br.edu.ifspsaocarlos.sdm.fragchat.utils.DBHelper;
import br.edu.ifspsaocarlos.sdm.fragchat.utils.TouchEffect;

/**
 * Created by LeonardoAlmeida on 15/07/16.
 */
public class FragmentMessengerChat extends Fragment implements OnClickListener{
    private static final String TAG = "MessengerChatMainFragment";

    public static final String SENDER = "senderid";
    public static final String RECEIVER = "receiverid";
    public static final String LASTMSG = "lastmsgdate";


    public static final TouchEffect TOUCH = new TouchEffect();

    private ChatAdapter adp;

    // private static Handler handler; /** The handler. */

    ListView _messageList;
    EditText _subjectMsg;
    EditText _userMessageText;
    Button  _btnSendMessage;
    LinearLayout _layoutUserList;

    private boolean isRunning; /** Flag to hold if the activity is running or not. */


    private UserModel _loggedUserProfile;
    private UserTokenModel _loggedUserToken;
    private UserModel _selectedContact;
    private List<GroupMessagesModel> _chatMsgList;
    private static long lastMessageID = 0;

    // private GroupModel _groupDescriptions;
    // private List<UserModel> _groupUsers;

    private DBHelper _localdb;
    private static ArrayList<FragmentMessengerChat> _fragmentList;
    private static Context _context;

    public static FragmentMessengerChat newInstance(Context ctx) {
        FragmentMessengerChat fragment = new FragmentMessengerChat();

        if (_fragmentList == null) _fragmentList = new ArrayList<>();

        _fragmentList.add(fragment);
        _context = ctx;

        return fragment;
    }

    public static List<FragmentMessengerChat> getInstanceList(){
        return _fragmentList;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment_msgchat_page, null);

        ActivityMain activity = ((ActivityMain) getActivity());
        _localdb = activity.getLocaldb();
        _loggedUserProfile = activity.getLoggedUserProfile();
        _loggedUserToken = activity.getLoggedUserToken();

        _chatMsgList = new ArrayList<>();
        adp = new ChatAdapter();

        _messageList = (ListView) view.findViewById(R.id.messageList);
        _messageList.setAdapter(adp);
        _messageList.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        _messageList.setStackFromBottom(true);


        _subjectMsg = (EditText) view.findViewById(R.id.subjectMsg);
        _subjectMsg.setInputType(InputType.TYPE_CLASS_TEXT);

        _userMessageText = (EditText) view.findViewById(R.id.userMessage);
        _userMessageText.setInputType(InputType.TYPE_CLASS_TEXT);

        _btnSendMessage = (Button) view.findViewById(R.id.btnSend);
        _btnSendMessage.setOnClickListener(this);
        if (_btnSendMessage != null) _btnSendMessage.setOnTouchListener(TOUCH);

        _layoutUserList = (LinearLayout) view.findViewById(R.id.actions_list);

        _localdb = activity.getLocaldb();

        long selectedContactID = ((ActivityMain) getActivity()).getSelectedContact();

        if (selectedContactID > 0) {

            _selectedContact = _localdb.getContactProfile(selectedContactID, _loggedUserToken);
            activity.mToolbarSubTitle.setText(_selectedContact.getNome());

        } else {
            Toast.makeText(getActivity(), "Contato não escolhido!!", Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((ActivityDefault) getActivity()).stopService(ActivityDefault.RECEIVER_SERVICE);
        _layoutUserList.removeAllViewsInLayout();
        _layoutUserList.invalidate();
        _fragmentList.remove(this);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        isRunning = true;
        loadConversationList(GroupMessagesModel.ALL_MESSAGES);
    }

    @Override
    public void onStop() {
        super.onStop();
        isRunning = false;
        _layoutUserList.removeAllViewsInLayout();
        _layoutUserList.invalidate();
        ((ActivityDefault) getActivity()).stopService(ActivityDefault.RECEIVER_SERVICE);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnSend) {
            sendMessage();
        }
    }

    private void sendMessage() {

        if (_userMessageText.length() == 0)
            return;

        InputMethodManager inputMethod = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethod.hideSoftInputFromWindow(_userMessageText.getWindowToken(), 0);

        String subject = _subjectMsg.getText().toString();
        String message = _userMessageText.getText().toString();
        final GroupMessagesModel messagesModel =
                new GroupMessagesModel(_selectedContact.getId(), subject, message, new Date(), _loggedUserProfile, _selectedContact.getId());
        messagesModel.setStatus(GroupMessagesModel.STATUS_SAVED);
        // _chatMsgList.add(messagesModel);
        adp.notifyDataSetChanged();
        _userMessageText.setText(null);
        _subjectMsg.setText(null);

        // _localdb.saveMessage(messagesModel, _loggedUserToken);

        new Thread(){
            public void run(){
                RequestQueue queue = Volley.newRequestQueue(getActivity());
                String url = getString(R.string.url_base) + "/mensagem";
                String string = "{\"origem_id\":\"" + messagesModel.getSenderID() + "\"," +
                        "\"destino_id\":\""+ messagesModel.getReceiverID() + "\"," +
                        "\"assunto\":\"" + messagesModel.getSubject() + "\"," +
                        "\"corpo\":\"" + messagesModel.getMsg() + "\"}";

                Log.w("fragChat", "URL: " + string);
                Log.w("fragChat", "Attrs" + string);

                try {
                    messagesModel.setStatus(messagesModel.STATUS_SENDING);
                    _localdb.updateMessageStatus(messagesModel, _loggedUserToken);

                    final JSONObject jsonBody = new JSONObject(string);
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                            Request.Method.POST,
                            url,
                            jsonBody,
                            new Response.Listener<JSONObject>() {
                                public void onResponse(JSONObject s) {
                                    Log.w("fragChat", "Return" + s.toString());
                                    _localdb.saveMessage(new GroupMessagesModel(s, messagesModel.STATUS_SENT),_loggedUserToken);

                                    Toast.makeText(getActivity(), "Mensagem enviada!",
                                            Toast.LENGTH_SHORT).show();
                                }
                            },
                            new Response.ErrorListener() {
                                public void onErrorResponse(VolleyError volleyError) {
                                    messagesModel.setStatus(messagesModel.STATUS_FAILED);
                                    _localdb.updateMessageStatus(messagesModel, _loggedUserToken);
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

    }

    private void loadConversationList(short requestType) {

        ((ActivityDefault) getActivity()).showProgressBar();

        if (requestType != GroupMessagesModel.STATUS_RECEIVED){
            _chatMsgList = _localdb.requestMessages(_selectedContact.getId(), _loggedUserProfile.getId(), _loggedUserToken);
            _chatMsgList.addAll(_localdb.requestMessages(_loggedUserProfile.getId(), _selectedContact.getId(), _loggedUserToken));
            Collections.sort(_chatMsgList);
        }

        _layoutUserList.addView(addOnUserList(_loggedUserProfile));
        _layoutUserList.addView(addOnUserList(_selectedContact));

        ((ActivityDefault) getActivity()).dismissProgressBar();

    }

    private ImageView addOnUserList(UserModel userContact) {
        int margin = 5;
        MarginLayoutParams marginParams;
        LayoutParams layoutParams;
        ImageView imageView;

        imageView = new AppCompatImageView(_context);
        imageView.setImageResource(userContact.getImgAvatar());
        imageView.setId((int) userContact.getId());
        imageView.setPadding(5, 0, 5, 5);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            imageView.setElevation(20);
        }
        imageView.setBackgroundResource(R.drawable.draw_circular_border_imageview);
        layoutParams = new LayoutParams(50,50);
        imageView.setLayoutParams(layoutParams);
        marginParams = new MarginLayoutParams(imageView.getLayoutParams());
        marginParams.setMargins(margin, 0, margin, margin);

        return imageView;
    }


    /**
     * The Class ChatAdapter is the adapter class for Chat ListView. This
     * adapter shows the Sent or Received Chat message in each list item.
     */
    private class ChatAdapter extends BaseAdapter
    {

        /* (non-Javadoc)
         * @see android.widget.RecycleViewAdapter#getCount()
         */
        @Override
        public int getCount()
        {
            return _chatMsgList.size();
        }

        /* (non-Javadoc)
         * @see android.widget.RecycleViewAdapter#getItem(int)
         */
        @Override
        public GroupMessagesModel getItem(int arg0)
        {
            return _chatMsgList.get(arg0);
        }

        /* (non-Javadoc)
         * @see android.widget.RecycleViewAdapter#getItemId(int)
         */
        @Override
        public long getItemId(int arg0)
        {
            return arg0;
        }

        /* (non-Javadoc)
         * @see android.widget.RecycleViewAdapter#getView(int, android.view.View, android.view.ViewGroup)
         */
        @Override
        public View getView(int pos, View v, ViewGroup arg2)
        {
            int avatar = 0;
            GroupMessagesModel msg = getItem(pos);
            if (msg.isSent(_loggedUserProfile.getId())) {
                v = getLayoutInflater(getArguments()).inflate(R.layout.component_msgchat_item_sent, null);
                avatar = _loggedUserProfile.getImgAvatar();
            }
            else {
                v = getLayoutInflater(getArguments()).inflate(R.layout.component_msgchat_item_received, null);
                avatar = _selectedContact.getImgAvatar();
            }

            ImageView imv = (ImageView) v.findViewById(R.id.user_profile_photo);
            imv.setBackgroundResource(R.drawable.draw_circular_border_imageview);
            imv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imv.setImageResource(avatar);

            TextView lbl = (TextView) v.findViewById(R.id.label1);

            lbl.setText(DateUtils.getRelativeDateTimeString(_context,
                    msg.getRequestedDate().getTime(), DateUtils.SECOND_IN_MILLIS,
                            DateUtils.DAY_IN_MILLIS, 0));
            lbl = (TextView) v.findViewById(R.id.label2);
            lbl.setText(msg.getMsg());

            lbl = (TextView) v.findViewById(R.id.label3);
            // if (msg.isSent(_loggedUserProfile.getId()))
            // {
                if (msg.getStatus() == GroupMessagesModel.STATUS_SENT)
                    lbl.setText(R.string.msg_delivered);
                else if (msg.getStatus() == GroupMessagesModel.STATUS_SENDING)
                    lbl.setText(R.string.msg_sending);
                else if (msg.getStatus() == GroupMessagesModel.STATUS_SAVED)
                    lbl.setText(R.string.msg_saved);
                else if (msg.getStatus() == GroupMessagesModel.STATUS_RECEIVED)
                    lbl.setText(R.string.msg_received);
                else if (msg.getStatus() == GroupMessagesModel.STATUS_READ)
                    lbl.setText(R.string.msg_read);
                else if (msg.getStatus() == GroupMessagesModel.STATUS_FAILED)
                    lbl.setText(R.string.msg_failed);
                else
                    lbl.setText("");

            return v;
        }

    }
}
