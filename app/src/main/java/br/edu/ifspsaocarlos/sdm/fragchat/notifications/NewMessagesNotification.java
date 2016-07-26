package br.edu.ifspsaocarlos.sdm.fragchat.notifications;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by LeonardoAlmeida on 21/07/16.
 */

public class NewMessagesNotification extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String mensagemDaNotificacao = getIntent().getStringExtra("mensagem_da_notificacao");
        if (mensagemDaNotificacao != null) {
            Toast.makeText(this, mensagemDaNotificacao, Toast.LENGTH_SHORT).show(); }
        TextView tvNewMessage = new TextView(this);
        tvNewMessage.setText("Nova Mensagem recebida");
        setContentView(tvNewMessage);
    }
}
