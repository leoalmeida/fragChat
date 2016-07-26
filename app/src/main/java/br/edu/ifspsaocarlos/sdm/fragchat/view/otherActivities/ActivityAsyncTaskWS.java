package br.edu.ifspsaocarlos.sdm.fragchat.view.otherActivities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import br.edu.ifspsaocarlos.sdm.fragchat.R;


/**
 * Created by LeonardoAlmeida on 17/06/16.
 */
public class ActivityAsyncTaskWS extends Activity implements View.OnClickListener{

    private FloatingActionButton _fabAcessarWs;
    private ProgressBar _mProgress;
    private ImageView _ivProgressOK;
    private EditText _urlText;
    private EditText _urlData;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asynctask);
        _fabAcessarWs = (FloatingActionButton) findViewById(R.id.fab_acessar_ws);
        _fabAcessarWs.setOnClickListener(this);
        _mProgress = (ProgressBar) findViewById(R.id.pb_carregando);
        _ivProgressOK = (ImageView) findViewById(R.id.pb_ok);
        _ivProgressOK.setAlpha(0.0f);
    }

    @Override
    public void onClick(View v) {
        if (v == _fabAcessarWs) {
            _urlData = (EditText) findViewById(R.id.input_data_url);
            _urlText = (EditText) findViewById(R.id.input_text_url);

            if (validateURL()) {
                if (_urlText.getText().length()>0) buscarTexto(_urlText.getText().toString());
                //"http://www.nobile.pro.br/sdm/texto.php"
                if (_urlData.getText().length()>0) buscarData(_urlData.getText().toString());
                //"http://www.nobile.pro.br/sdm/data.php"
            }
        }
    }

    private void buscarData(String url){
        AsyncTask<String, Void, JSONObject> tarefa = new AsyncTask<String, Void, JSONObject>() {
            protected void onPreExecute() {
                _mProgress.setVisibility(View.VISIBLE);
                super.onPreExecute();
            }
            protected JSONObject doInBackground(String... params) {
                JSONObject jsonObject = null;
                StringBuilder sb = new StringBuilder();
                try{
                    HttpURLConnection conexao =
                            (HttpURLConnection) (new URL(params[0])).openConnection();
                    if (conexao.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        InputStream is = conexao.getInputStream();
                        BufferedReader br = new BufferedReader(new InputStreamReader(is));
                        String temp;
                        while ((temp = br.readLine()) != null) {
                            sb.append(temp);
                        } }

                    jsonObject = new JSONObject(sb.toString());
                }
                catch (IOException ioe) {
                    Log.e("SDM", "Erro na recuperação de objeto");
                }
                catch (JSONException jsone) {

                    Log.e("SDM", "Erro no processamento do objeto JSON");
                }
                return jsonObject;
            }
            protected void onPostExecute(JSONObject s) {
                String data = null, hora = null, ds = null;
                super.onPostExecute(s);
                try {
                    data = s.getInt("mday") + "/" + s.getInt("mon") + "/" + s.getInt("year");
                    hora = s.getInt("hours") + ":" + s.getInt("minutes") + ":" + s.getInt("seconds");
                    ds   = s.getString("weekday");
                }catch (JSONException jsone) {
                    Log.e("SDM", "Erro no processamento do objeto JSON");

                }
                ((TextView) findViewById(R.id.tv_rawdata)).setText(s.toString());
                ((TextView) findViewById(R.id.tv_data)).setText(data + "\n" + hora + "\n" + ds);
                //imageViewAnimatedChange(getBaseContext(), _ivProgressOK);
                _ivProgressOK.animate().setDuration(700)
                        .translationY(0)
                        .alpha(1.0f)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                _mProgress.setVisibility(View.GONE);
                                _ivProgressOK.animate().setDuration(500)
                                        .translationY(0)
                                        .alpha(0.0f);
                            }
                        });
            }
        };
        tarefa.execute(url);
    }

    private void buscarTexto(String url) {
        AsyncTask<String, Void, String> tarefa = new AsyncTask<String, Void, String>() {
            protected void onPreExecute() {
                super.onPreExecute();
            }
            protected String doInBackground(String... params) {
                StringBuilder sb = new StringBuilder();
                try{
                    HttpURLConnection conexao =
                            (HttpURLConnection) (new URL(params[0])).openConnection();
                    if (conexao.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        InputStream is = conexao.getInputStream();
                        BufferedReader br = new BufferedReader(new InputStreamReader(is));
                        String temp;
                        while ((temp = br.readLine()) != null) {
                            sb.append(temp);
                        } }
                }
                catch (IOException ioe) {
                    Log.e("SDM", "Erro na recuperação de texto");
                }

                return sb.toString();
            }
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                TextView tvTexto = ((TextView) findViewById(R.id.tv_texto));
                tvTexto.setText(s);
                _ivProgressOK.animate().setDuration(700)
                        .translationY(0)
                        .alpha(1.0f)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                _mProgress.setVisibility(View.GONE);
                                _ivProgressOK.animate().setDuration(500)
                                        .translationY(0)
                                        .alpha(0.0f);
                            }
                        });
            }
        };
        tarefa.execute(url);
    }

    public boolean validateURL() {
        boolean valid = true;

        String data = _urlData.getText().toString();
        String text = _urlText.getText().toString();


        if (!data.isEmpty() && !Patterns.WEB_URL.matcher(data).matches() ) {
            _urlData.setError("O padrão da URL não é válido!!");
            valid = false;
        } else {
            _urlData.setError(null);
        }

        if (!text.isEmpty() && !Patterns.WEB_URL.matcher(text).matches() ) {
            _urlText.setError("O padrão da URL não é válido!!");
            valid = false;
        } else {
            _urlText.setError(null);
        }

        if (data.isEmpty() && text.isEmpty()){
            _urlData.setError("Pelo menos um campo deve ser preenchido!!");
            _urlText.setError("Pelo menos um campo deve ser preenchido!!");
            valid = false;
        }

        return valid;
    }

    public boolean validateText(String dados) {
        boolean valid = true;

        if (dados.isEmpty() || !Patterns.DOMAIN_NAME.matcher(dados).matches() ) {
            ((TextView) findViewById(R.id.tv_texto)).setError("Erro na requisição do texto");
            valid = false;
        } else {
            ((TextView) findViewById(R.id.tv_texto)).setError(null);
        }

        return valid;
    }

    public boolean validateJson(JSONObject json) {
        boolean valid = true;

        if (json == JSONObject.NULL) {
            ((TextView) findViewById(R.id.tv_rawdata)).setError("Erro na requisição dos dados");
            valid = false;
        } else {
            ((TextView) findViewById(R.id.tv_rawdata)).setError(null);
        }

        return valid;
    }


}
