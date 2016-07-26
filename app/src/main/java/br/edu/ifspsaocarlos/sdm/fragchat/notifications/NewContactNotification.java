package br.edu.ifspsaocarlos.sdm.fragchat.notifications;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by LeonardoAlmeida on 21/07/16.
 */

public class NewContactNotification extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String notificationMessage = getIntent().getStringExtra("mensagem_da_notificacao");
        if (notificationMessage != null) {
            Toast.makeText(this, notificationMessage, Toast.LENGTH_SHORT).show(); }
        TextView tvNewUser = new TextView(this);
        tvNewUser.setText("Informações do novo contato");
        setContentView(tvNewUser);
    }
}
