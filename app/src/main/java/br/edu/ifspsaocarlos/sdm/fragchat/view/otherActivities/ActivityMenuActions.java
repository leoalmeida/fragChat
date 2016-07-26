package br.edu.ifspsaocarlos.sdm.fragchat.view.otherActivities;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by LeonardoAlmeida on 02/06/16.
 */
public class ActivityMenuActions extends ListActivity {

    private final String[] activities = new String[]{
            "**AsyncTaskWS",
            "Mensagem",
            "Runnable",
            "UIThread",
            "Volley",
            "Sair"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ArrayAdapter<String> adaptador =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, activities);
        this.setListAdapter(adaptador);

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        Intent intent = null;
        switch (getListAdapter().getItem(position).toString()) {
            case "**AsyncTaskWS":
                intent = new Intent(this, ActivityAsyncTaskWS.class);
                startActivity(intent);
                break;
            case "Mensagem":
                intent = new Intent(this, ActivityMensagemHandler.class);
                startActivity(intent);
                break;
            case "Runnable":
                intent = new Intent(this, ActivityRunnableHandler.class);
                startActivity(intent);
                break;
            case "UIThread":
                intent = new Intent(this, ActivityRunOnUIThread.class);
                startActivity(intent);
                break;
            case "Volley":
                intent = new Intent(this, ActivityVolleyMessageHandler.class);
                startActivity(intent);
                break;
            case "Sair":
            default:
                finish();
                break;
        }
    }
}
