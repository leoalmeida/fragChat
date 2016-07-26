package br.edu.ifspsaocarlos.sdm.fragchat.view.otherActivities;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import java.util.ArrayList;

/**
 * Created by LeonardoAlmeida on 11/07/16.
 */
public class ActivityMostrarMensagens extends ListActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ArrayList<String> corpoMensagens = getIntent().getStringArrayListExtra("Mensagens");
        ListAdapter listAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                corpoMensagens);
        setListAdapter(listAdapter);
    }
}
