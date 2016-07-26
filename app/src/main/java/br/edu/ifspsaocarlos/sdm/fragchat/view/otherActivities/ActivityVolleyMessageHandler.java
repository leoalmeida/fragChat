package br.edu.ifspsaocarlos.sdm.fragchat.view.otherActivities;

import android.app.ListActivity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import br.edu.ifspsaocarlos.sdm.fragchat.service.BuscaNovoContatoService;

/**
 * Created by LeonardoAlmeida on 11/07/16.
 */
public class ActivityVolleyMessageHandler extends ListActivity{
    private Intent serviceIntent;
    private String []opcoes = new String[] {"EnviarActivity mensagem", "LerActivity mensagens", "Sair"};

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, opcoes);
        setListAdapter(arrayAdapter);

        Context context = this.getApplicationContext();
        serviceIntent = new Intent(context, BuscaNovoContatoService.class);
        context.startService(serviceIntent);

        //serviceIntent = new Intent("BUSCAR_NOVO_CONTATO_SERVICE");
        //startService(serviceIntent);

    }

    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        switch (getListAdapter().getItem(position).toString()) {
            case "EnviarActivity mensagem":
                startActivity(new Intent(this, ActivityEnviar.class));
                break;
            case "LerActivity mensagens":
                startActivity(new Intent(this, ActivityLer.class));
                break;
            case "Sair":
            default:
                finish();
        } }
    protected void onDestroy() {
        stopService(serviceIntent);
        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE); nm.cancelAll();
        super.onDestroy();
    }

}
