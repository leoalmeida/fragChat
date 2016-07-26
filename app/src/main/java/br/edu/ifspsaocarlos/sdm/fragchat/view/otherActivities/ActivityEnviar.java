package br.edu.ifspsaocarlos.sdm.fragchat.view.otherActivities;

import android.app.Activity;
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

import org.json.JSONObject;

import br.edu.ifspsaocarlos.sdm.fragchat.R;

public class ActivityEnviar extends Activity implements View.OnClickListener {

    private Button btEnviar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enviar);
        btEnviar = (Button) findViewById(R.id.bt_enviar);
        btEnviar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        final EditText etOrigem = (EditText) findViewById(R.id.et_origem_enviar);
        final EditText etDestino = (EditText) findViewById(R.id.et_destino_enviar);
        final EditText etAssunto = (EditText) findViewById(R.id.et_assunto_enviar);
        final EditText etCorpo = (EditText) findViewById(R.id.et_corpo_enviar);

        new Thread(){
            public void run(){
                RequestQueue queue = Volley.newRequestQueue(ActivityEnviar.this);
                String url = getString(R.string.url_base) + "/mensagem";
                String string = "{\"origem_id\":\"" + etOrigem.getText().toString() + "\"," +
                        "\"destino_id\":\""+ etDestino.getText().toString() + "\"," +
                        "\"assunto\":\"" + etAssunto.getText().toString() + "\"," +
                        "\"corpo\":\"" + etCorpo.getText().toString() + "\"}";
                try {
                    final JSONObject jsonBody = new JSONObject(string);
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                            Request.Method.POST,
                            url,
                            jsonBody,
                            new Response.Listener<JSONObject>() {
                                public void onResponse(JSONObject s) {
                                    Toast.makeText(ActivityEnviar.this, "Mensagem enviada!",
                                            Toast.LENGTH_SHORT).show();

                                    runOnUiThread(new Runnable() {
                                        public void run() {
                                            etOrigem.setText("");
                                            etDestino.setText("");
                                            etAssunto.setText("");
                                            etCorpo.setText("");
                                        }
                                    });
                                }
                            },
                            new Response.ErrorListener() {
                                public void onErrorResponse(VolleyError volleyError) {
                                    Toast.makeText(ActivityEnviar.this, "Erro no envio da mensagem!", Toast.LENGTH_SHORT).show();
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
}
