package br.edu.ifspsaocarlos.sdm.fragchat.view.otherActivities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import br.edu.ifspsaocarlos.sdm.fragchat.R;

public class ActivityLer extends Activity implements View.OnClickListener {

    private Button btLer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ler);
        btLer = (Button) findViewById(R.id.bt_ler);
        btLer.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        final EditText etOrigem = (EditText) findViewById(R.id.et_origem_ler);
        final EditText etDestino = (EditText) findViewById(R.id.et_destino_ler);
        final EditText etUltimaMensagem = (EditText) findViewById(R.id.et_ultima_mensagem_ler);
        RequestQueue queue = Volley.newRequestQueue(ActivityLer.this);
        String url = getString(R.string.url_base) + "/mensagem" +
                "/" + etUltimaMensagem.getText().toString() +
                "/" + etOrigem.getText().toString() +
                "/" + etDestino.getText().toString();
        try {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>() {
                        public void onResponse(JSONObject s) {
                            etOrigem.setText("");
                            etDestino.setText("");
                            etUltimaMensagem.setText("");
                            ArrayList<String> corpoMensagens = new ArrayList<String>();
                            JSONArray jsonArray;
                            try {
                                jsonArray = s.getJSONArray("mensagens");
                                for (int indice = 0; indice < jsonArray.length(); indice++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(indice);
                                    corpoMensagens.add(jsonObject.getString("corpo"));
                                }
                            }
                            catch (JSONException je) {
                                Toast.makeText(ActivityLer.this, "Erro na conversão de objeto JSON!", Toast.LENGTH_SHORT).show();
                            }
                            Intent mostraMensagensIntent = new Intent(ActivityLer.this,
                                    ActivityMostrarMensagens.class);
                            mostraMensagensIntent.putStringArrayListExtra("mensagens", corpoMensagens);
                            startActivity(mostraMensagensIntent);
                        }
                    }, new Response.ErrorListener() {
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(ActivityLer.this, "Erro na recuperação das mensagens!", Toast.LENGTH_SHORT).show();
                } });
            queue.add(jsonObjectRequest);
        } catch (Exception e) {
            Log.e("SDM", "Erro na leitura de mensagens");
        }
    }
}
