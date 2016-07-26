package br.edu.ifspsaocarlos.sdm.fragchat.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import br.edu.ifspsaocarlos.sdm.fragchat.R;
import br.edu.ifspsaocarlos.sdm.fragchat.models.GroupMessagesModel;
import br.edu.ifspsaocarlos.sdm.fragchat.notifications.NewMessagesNotification;
import br.edu.ifspsaocarlos.sdm.fragchat.utils.DBHelper;

/**
 * Created by LeonardoAlmeida on 21/07/16.
 */

public class MessengerReceiverService  extends Service implements Runnable {


    public static final String SENDER = "senderid";
    public static final String RECEIVER = "receiverid";
    public static final String LAST_SENT_MSG = "last_sent_msg";
    public static final String LAST_RECV_MSG = "last_recv_msg";
    public static final String TOKEN = "token";
    public static final String TOKENID = "tokenid";
    public static final short  SEND = 1;
    public static final short  RECEIVE = 2;

    private String token;
    private long senderID, receiverID, tokenid;
    private boolean appOpen;


    private static long oldLastReceivedID, newLastReceivedID,oldLastSentID, newLastSentID;
    private List<GroupMessagesModel> recvMsgList, sentMsgList;

    protected DBHelper localdb;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        appOpen = true;
        oldLastReceivedID = oldLastSentID = newLastReceivedID = newLastSentID = 0;

        localdb = new DBHelper(this);
        new Thread(this).start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int result = super.onStartCommand(intent, flags, startId);

        senderID =  intent.getLongExtra(SENDER, 0);
        receiverID =  intent.getLongExtra(RECEIVER, 0);
        oldLastSentID = intent.getLongExtra(LAST_SENT_MSG, oldLastSentID);
        oldLastReceivedID = intent.getLongExtra(LAST_RECV_MSG, oldLastReceivedID);
        tokenid = intent.getLongExtra(TOKENID, 0);
        token = intent.getStringExtra(TOKEN);

        return result;
    }

    @Override
    public void run() {

        while (appOpen) {
            try {
                Thread.sleep(getResources().getInteger(R.integer.service_round_time));

                receiveNewSentMessages(); // Sent
                newLastSentID = localdb.getLastMessageID(senderID, receiverID);

                receiveNewReceivedMessages(); // Received
                newLastReceivedID = localdb.getLastMessageID(receiverID, senderID);

                if (newLastReceivedID > oldLastReceivedID){
                    NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                    Intent intent = new Intent(this, NewMessagesNotification.class);
                    intent.putExtra("mensagem_da_notificacao",
                            getString(R.string.msgs_atualizadas));

                    PendingIntent p = PendingIntent.getActivity(this, 0, intent, 0);
                    Notification.Builder builder = new Notification.Builder(this);
                    builder.setSmallIcon(R.drawable.icon);
                    builder.setTicker(getString(R.string.nova_msg_recebida));
                    builder.setContentTitle(getString(R.string.nova_msg));
                    builder.setContentText(getString(R.string.clique_aqui));
                    builder.setWhen(System.currentTimeMillis());
                    builder.setContentIntent(p);
                    builder.setLargeIcon(BitmapFactory.decodeResource(getResources(),
                            android.R.drawable.sym_action_chat));
                    Notification notification = builder.build();
                    notification.vibrate = new long[]{100, 250};
                    nm.notify(R.mipmap.ic_launcher, notification);
                }

                oldLastReceivedID = newLastReceivedID;
                oldLastSentID = newLastSentID;

            }catch(InterruptedException ie){
                Log.e("SDM", "Erro na thread de recuperação de mensagens");
                Log.e("SDM", ie.getStackTrace().toString());
            }
        }

    }

    private void receiveNewSentMessages() {
        RequestQueue queue = Volley.newRequestQueue(MessengerReceiverService.this);
        String url = getString(R.string.url_base) + "/mensagem" +
                "/" + oldLastSentID +
                "/" + senderID +
                "/" + receiverID;

        try {
            JsonUTF8Request jsonObjectRequest = new JsonUTF8Request( Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject s) {
                            responseCallback(s, SEND);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Toast.makeText(MessengerReceiverService.this, "Erro na recuperação das mensagens!", Toast.LENGTH_SHORT).show();
                        }
                    }
            );
            queue.add(jsonObjectRequest);
        }catch (Exception e) {
            Log.e("SDM", "Erro na leitura de mensagens");
        }
    }

    private void receiveNewReceivedMessages() {
        RequestQueue queue = Volley.newRequestQueue(MessengerReceiverService.this);
        String url = getString(R.string.url_base) + "/mensagem" +
                "/" + oldLastReceivedID +
                "/" + receiverID +
                "/" + senderID;

        try {
            JsonUTF8Request jsonObjectRequest = new JsonUTF8Request( Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject s) {
                            responseCallback(s, RECEIVE);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Toast.makeText(MessengerReceiverService.this, "Erro na recuperação das mensagens!", Toast.LENGTH_SHORT).show();
                        }
                    }
            );
            queue.add(jsonObjectRequest);
        }catch (Exception e) {
            Log.e("SDM", "Erro na leitura de mensagens");
        }
    }

    private void responseCallback(JSONObject s, short reqType) {
        List<GroupMessagesModel> msgList = new ArrayList<>();

        GroupMessagesModel msg;
        JSONArray jsonArray;
        try {
            jsonArray = s.getJSONArray("mensagens");
            for (int indice = 0; indice < jsonArray.length(); indice++) {
                JSONObject jsonObject = jsonArray.getJSONObject(indice);

                msg = new GroupMessagesModel(jsonObject, GroupMessagesModel.STATUS_RECEIVED);

                if(msg.getStatus() == GroupMessagesModel.STATUS_FAILED){
                    throw new JSONException("Falha no parser das mensagens");
                }

                msgList.add(msg);
            }

            localdb.saveReceivedMessages(msgList, tokenid, token);

        }catch (JSONException je) {
            Toast.makeText(MessengerReceiverService.this, "Erro na conversão de objeto JSON!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        appOpen = false;
        stopSelf();
    }

}
