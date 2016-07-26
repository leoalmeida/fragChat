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

import br.edu.ifspsaocarlos.sdm.fragchat.R;
import br.edu.ifspsaocarlos.sdm.fragchat.notifications.NovoContatoActivity;

/**
 * Created by LeonardoAlmeida on 11/07/16.
 */
public class BuscaNovoContatoService extends Service implements Runnable {
    private boolean appAberta;
    private boolean primeiraBusca;
    private static int ultimoNumeroContatos;
    private static int novoNumeroContatos;


    public BuscaNovoContatoService() {}

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        appAberta = true;
        primeiraBusca = true;
        ultimoNumeroContatos = 0;
        new Thread(this).start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void run() {
        while (appAberta) {
            try {
                Thread.sleep(getResources().getInteger(R.integer.tempo_inatividade_servico));
                buscaNumeroContatos();
                if (!primeiraBusca && novoNumeroContatos != ultimoNumeroContatos) {
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
                ultimoNumeroContatos = novoNumeroContatos;
                primeiraBusca = false;
            }
            catch (InterruptedException ie) {
                Log.e("SDM", "Erro na thread de recuperação de contato");
                Log.e("SDM", ie.getStackTrace().toString());
            }
        }
    }

    private void buscaNumeroContatos() {
        RequestQueue queue = Volley.newRequestQueue(BuscaNovoContatoService.this);
        final String url = getString(R.string.url_base) + "/contato/";

        try {
            JsonUTF8Request jsonObjectRequest = new JsonUTF8Request( Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject s) {
                        JSONArray jsonArray;
                        try {
                            jsonArray = s.getJSONArray("contatos");
                            novoNumeroContatos = jsonArray.length();
                        } catch (JSONException je) {
                            Toast.makeText(BuscaNovoContatoService.this, "Erro na conversão de objeto JSON!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(BuscaNovoContatoService.this, "Erro na recuperação do número de contatos!", Toast.LENGTH_SHORT).show();
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
        appAberta = false;
        stopSelf();
    }
}
