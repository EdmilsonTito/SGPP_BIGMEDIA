package com.example.sgppbigmedia.Tarefas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.sgppbigmedia.DataBase.DatabaseHelper;
import com.example.sgppbigmedia.MenuPrincipal;
import com.example.sgppbigmedia.Painel.DescricaoPainel;
import com.example.sgppbigmedia.Painel.Painel;
import com.example.sgppbigmedia.Painel.PesquisaPainel;
import com.example.sgppbigmedia.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class VisualizarTarefas extends AppCompatActivity {

    ListView listViewTarefas;
    private List<Tarefas> listtarefas = new ArrayList<Tarefas>();
    private ArrayAdapter adapter;
    Timer timer;
    ProgressDialog progresso;
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;
    StringRequest stringRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_tarefas);
        inicializarVariaveis();
        ConsultarTarefas();

        long TEMPO = (1000 * 5); // chama o método a cada 5 segundos

        if (timer == null) {
            timer = new Timer();
            TimerTask tarefa = new TimerTask() {

                public void run() {
                    try {
                        listtarefas.clear();
                        ConsultarTarefas();//funcao Consultar as Tarefas com BD Online - Mysql
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            timer.scheduleAtFixedRate(tarefa, TEMPO, TEMPO);
        }

        listViewTarefas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
                Tarefas tarefaEscolhida = (Tarefas) adapter.getItemAtPosition(position);
                Intent i = new Intent(VisualizarTarefas.this, DetalhesTarefa.class);
                i.putExtra("tarefa-escolhida", tarefaEscolhida);
                startActivity(i);
            }
        });

    }

    public void ConsultarTarefas() {
        String ip = getString(R.string.ip);
        String url = ip+"/apiAndroid/Visualizar_Tarefas/consultarListaTarefas.php";
        url = url.replace(" ", "%20");

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Tarefas tarefa = null;
                JSONArray json = response.optJSONArray("listatarefas");

                try {
                    for (int i = 0; i < json.length(); i++) {
                        tarefa = new Tarefas();
                        JSONObject jsonObject = null;
                        jsonObject = json.getJSONObject(i);
                        tarefa.setIdTarefa(jsonObject.optInt("idTarefa"));
                        tarefa.setData(jsonObject.optString("data"));
                        tarefa.setDescricao(jsonObject.optString("descricao"));
                        tarefa.setEstadotarefa(jsonObject.optString("estado"));
                        tarefa.setTitulo(jsonObject.optString("titulo"));
                        tarefa.setPrioridade(jsonObject.optString("prioridade"));
                        tarefa.setIdFunc(jsonObject.optInt("idFunc"));

                        if(jsonObject.optString("estado").equals("Nova")||jsonObject.optString("estado").equals("nova")){
                            EnviarNotificao(jsonObject.optString("idTarefa"),jsonObject.optString("titulo"));
                        }
                        listtarefas.add(tarefa);
                    }
                    adapter = new ArrayAdapter<Tarefas>(VisualizarTarefas.this, android.R.layout.simple_list_item_1, listtarefas);
                    listViewTarefas.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getBaseContext(), "Nao foi possivel conectar o Servidor" + error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        request.add(jsonObjectRequest);

    }
    private void EnviarNotificao(String idtarefas,String Titulo) {
        //Toast.makeText(getBaseContext(), "Nova Notificacao. IdTarefa= "+idtarefas+"Titulo= "+Titulo, Toast.LENGTH_LONG).show();
        gerarNotificacao(idtarefas,Titulo);
        ActualizarEstadoWEBService(idtarefas,"Visualizado");

    }
    public void ActualizarEstadoWEBService(final String idTarefa, final String estado) {
        String ip = getString(R.string.ip);
        String url = ip+"/apiAndroid/Visualizar_Tarefas/alterarEstadoTarefa.php";

        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
               // Toast.makeText(VisualizarTarefas.this, "Alterada com Sucesso", Toast.LENGTH_SHORT).show();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(VisualizarTarefas.this, "Erro ao Actualizar"+ error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> paramentros = new HashMap<>();

                paramentros.put("idTarefa", idTarefa);
                paramentros.put("estadoTarefa", estado);

                return paramentros;
            }

        };
        request.add(stringRequest);
    }

    private void gerarNotificacao(String idtarefa, String Titulo) {
        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        PendingIntent p = PendingIntent.getActivity(VisualizarTarefas.this, 0, new Intent(VisualizarTarefas.this, MenuPrincipal.class), 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(VisualizarTarefas.this);
        builder.setTicker("Ticker Texto");
        builder.setContentTitle("Título");
        //builder.setContentText("Descrição");
        builder.setSmallIcon(R.drawable.logo_login);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.logo_login));
        builder.setContentIntent(p);

        NotificationCompat.InboxStyle style = new NotificationCompat.InboxStyle();
        String [] descs = new String[]{idtarefa,Titulo};
        for(int i = 0; i < descs.length; i++){
            style.addLine(descs[i]);
        }
        builder.setStyle(style);

        Notification n = builder.build();
        n.vibrate = new long[]{150, 300, 150, 600};
        n.flags = Notification.FLAG_AUTO_CANCEL;
        nm.notify(R.drawable.logo_login, n);

        try{
            Uri som = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone toque = RingtoneManager.getRingtone(VisualizarTarefas.this, som);
            toque.play();
        }
        catch(Exception e){}
    }

    private void inicializarVariaveis() {
        listViewTarefas= (ListView)findViewById(R.id. tarefaslistview);
        request = Volley.newRequestQueue(getBaseContext());
    }

}
