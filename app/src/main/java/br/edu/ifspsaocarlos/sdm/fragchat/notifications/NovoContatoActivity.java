package br.edu.ifspsaocarlos.sdm.fragchat.notifications;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by LeonardoAlmeida on 11/07/16.
 */
public class NovoContatoActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String mensagemDaNotificacao = getIntent().getStringExtra("mensagem_da_notificacao");
        if (mensagemDaNotificacao != null) {
            Toast.makeText(this, mensagemDaNotificacao, Toast.LENGTH_SHORT).show(); }
        TextView tvNovoContato = new TextView(this);
        tvNovoContato.setText("Informações do novo contato");
        setContentView(tvNovoContato);
    }
}
