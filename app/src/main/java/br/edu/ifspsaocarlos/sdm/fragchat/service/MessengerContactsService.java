package br.edu.ifspsaocarlos.sdm.fragchat.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
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
import br.edu.ifspsaocarlos.sdm.fragchat.models.UserModel;
import br.edu.ifspsaocarlos.sdm.fragchat.notifications.NovoContatoActivity;
import br.edu.ifspsaocarlos.sdm.fragchat.utils.DBHelper;

/**
 * Created by LeonardoAlmeida on 21/07/16.
 */

public class MessengerContactsService extends Service implements Runnable {

    public static final String LAST_CONTACT_ID = "lastContactNumber";
    public static final String TOKEN = "token";
    public static final String TOKENID = "tokenid";

    private String token;
    private long tokenid;

    private boolean appOpen;
    private boolean firstSearch;
    private static long lastContactNumber;
    private static long newContactNumber;

    private List<UserModel> contactList;
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
        firstSearch = true;
        lastContactNumber = 0;
        localdb = new DBHelper(this);
        Toast.makeText(MessengerContactsService.this, "Atualizando Contatos", Toast.LENGTH_LONG).show();
        new Thread(this).start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void run() {
        while (appOpen) {
            try {
                if (lastContactNumber>0 && firstSearch){
                    localdb.toggleAppStatus(0, DBHelper.APPSTATUS_DONE);
                    firstSearch = false;
                }
                Thread.sleep(getResources().getInteger(R.integer.service_round_time));
                searchNewContacts();
                if ( newContactNumber > lastContactNumber) {
                    localdb.toggleAppStatus(0, DBHelper.APPSTATUS_LOADING);
                    lastContactNumber += localdb.insertContacts(contactList);

                    NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    Intent intent = new Intent(this, NovoContatoActivity.class);
                    intent.putExtra("mensagem_da_notificacao",
                            getString(R.string.contatos_atualizados));
                    PendingIntent p = PendingIntent.getActivity(this, 0, intent, 0);
                    Notification.Builder builder = new Notification.Builder(this);
                    builder.setSmallIcon(R.drawable.ic_contato);
                    builder.setTicker(getString(R.string.novo_contato_adicionado));
                    builder.setContentTitle(getString(R.string.novo_contato));
                    builder.setContentText(getString(R.string.clique_aqui));
                    builder.setWhen(System.currentTimeMillis());
                    builder.setContentIntent(p);
                    builder.setLargeIcon(BitmapFactory.decodeResource(getResources(),
                            R.drawable.ic_mensageiro));
                    Notification notification = builder.build();
                    notification.vibrate = new long[] {100, 250};
                    nm.notify(R.mipmap.ic_launcher, notification);
                }
                // lastContactNumber = newContactNumber;
            }
            catch (InterruptedException ie) {
                Log.e("SDM", "Erro na thread de recuperação de contato");
                Log.e("SDM", ie.getStackTrace().toString());
            }
        }
    }

    private void searchNewContacts() {
        RequestQueue queue = Volley.newRequestQueue(MessengerContactsService.this);
        final String url = getString(R.string.url_base) + "/contato";

        try {

            JsonUTF8Request jsonObjectRequest = new JsonUTF8Request( Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject s) {
                            contactList = new ArrayList<>();
                            UserModel contact;
                            JSONArray jsonArray;
                            try {
                                jsonArray = s.getJSONArray("contatos");
                                newContactNumber = jsonArray.length();
                                Log.w("server", "JSON: " + jsonArray.toString());

                                if (firstSearch || (newContactNumber > lastContactNumber)) {
                                    for (int indice = 0; indice < jsonArray.length(); indice++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(indice);

                                        int img_avatar = getBaseContext().getResources().getIdentifier("profile_demo_"+(indice%4), "drawable", getPackageName());
                                        int img_header = getBaseContext().getResources().getIdentifier("profile_header_"+(indice%9), "drawable", getPackageName());
                                        contact = new UserModel(jsonObject, img_avatar, img_header);

                                        contactList.add(contact);
                                    }
                                }
                            } catch (JSONException je) {
                                Toast.makeText(MessengerContactsService.this, "Erro na conversão de objeto JSON!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(MessengerContactsService.this, "Erro na recuperação do número de contatos!", Toast.LENGTH_SHORT).show();
                }
            });
            queue.add(jsonObjectRequest);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        appOpen = false;
        stopSelf();
    }
}
