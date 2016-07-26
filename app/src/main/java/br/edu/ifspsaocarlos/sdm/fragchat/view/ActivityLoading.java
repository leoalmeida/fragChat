package br.edu.ifspsaocarlos.sdm.fragchat.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import br.edu.ifspsaocarlos.sdm.fragchat.R;
import br.edu.ifspsaocarlos.sdm.fragchat.view.messenger.ActivityMain;
import br.edu.ifspsaocarlos.sdm.fragchat.view.otherActivities.ActivityMenuActions;


public class ActivityLoading extends Activity {
    private final int ABRIR_MESSENGER = 0;
    private final int ABRIR_ACTIVITY_RECORRENCIA = 1;
    private final int TEMPO_CARREGAMENTO = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carregando);
        Toast.makeText(this, R.string.mensagem_carregando, Toast.LENGTH_SHORT).show();

        Message message = new Message();
        message.what = ABRIR_MESSENGER;
        handler.sendMessageDelayed(message, TEMPO_CARREGAMENTO);
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Intent intent;
            switch (msg.what){
                case ABRIR_ACTIVITY_RECORRENCIA:
                    intent = new Intent(ActivityLoading.this, ActivityMenuActions.class);
                    startActivity(intent);
                    finish();
                    break;
                case ABRIR_MESSENGER:
                    intent = new Intent(ActivityLoading.this, ActivityMain.class);
                    startActivity(intent);
                    finish();
                    break;
                default:
                    break;
            }
        }
    };
}
