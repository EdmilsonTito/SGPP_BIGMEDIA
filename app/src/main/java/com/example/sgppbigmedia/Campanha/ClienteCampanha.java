package com.example.sgppbigmedia.Campanha;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.sgppbigmedia.DataBase.DatabaseHelper;
import com.example.sgppbigmedia.Painel.Painel;
import com.example.sgppbigmedia.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ClienteCampanha extends AppCompatActivity {
    DatabaseHelper db;
    TextView nomeclienteselecionado;
    ListView listViewclientePainel;
    private List<Painel> listclientePainel = new ArrayList<Painel>();
    private ArrayAdapter adapter;
    Campanha clienteEscolhido;
    Painel painel;

    RequestQueue request;
    JsonObjectRequest jsonObjectReq;
    JSONObject jsonObject;
    Bitmap bitmap;
    Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente_campanha);
        inicializarVariaveis();
        Intent intent = getIntent();
        clienteEscolhido = (Campanha) intent.getSerializableExtra("cliente-escolhido") ;
        nomeclienteselecionado.setText(clienteEscolhido.getNomeCliente());

        carregarCampanhasCliente(clienteEscolhido.getNomeCliente());//funcao Consultar Campanhas dos Clientes com BD Online - Mysql

        long TEMPO = (1000 * 2); // chama o método a cada 2 segundos

        if (timer == null) {
            timer = new Timer();
            TimerTask tarefa = new TimerTask() {

                public void run() {
                    try {
                        listclientePainel.clear();
                        carregarCampanhasCliente(clienteEscolhido.getNomeCliente());//funcao Consultar Campanhas dos Clientes com BD Online - Mysql
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            timer.scheduleAtFixedRate(tarefa, TEMPO, TEMPO);
        }


        listViewclientePainel.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
                Painel clienteEscolhido = (Painel) adapter.getItemAtPosition(position);
                Intent i = new Intent(ClienteCampanha.this, ActualizarCampanha.class);
                i.putExtra("painel-escolhido", clienteEscolhido);
                startActivity(i);
            }
        });
    }

    private void inicializarVariaveis() {
        nomeclienteselecionado = (TextView) findViewById(R.id.txtcliente);
        listViewclientePainel = (ListView)findViewById(R.id.clienteCampanhalistview);
        request = Volley.newRequestQueue(getBaseContext());
        db = new DatabaseHelper(this);
    }

    private void carregarCampanhasCliente(String Nome) {

        Intent intent = getIntent();
        clienteEscolhido = (Campanha) intent.getSerializableExtra("cliente-escolhido") ;
        String ip = getString(R.string.ip);
        String url = ip+"/apiAndroid/Actualizar_Campanha/consultarCampanhaCliente.php?nomeCliente=" + Nome ;
        url = url.replace(" ", "%20");

        jsonObjectReq = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Campanha campanha = null;

                JSONArray json = response.optJSONArray("paineisCliente");

                try {
                    for (int i = 0; i < json.length(); i++) {
                        painel = new Painel();
                        JSONObject jsonObject = null;
                        jsonObject = json.getJSONObject(i);

                        painel.setIdCampanha(jsonObject.optInt("id_Campanha"));
                        painel.setIdfaces(jsonObject.optInt("idfaces"));
                        painel.setIdCliente(jsonObject.optInt("idCliente"));
                        painel.setIdFunc(jsonObject.optInt("idFunc"));
                        painel.setNomeCliente(jsonObject.optString("nomeCliente"));
                        painel.setLatitude(jsonObject.optDouble("latitude"));
                        painel.setLongitude(jsonObject.optDouble("longitude"));
                        painel.setDescricaoLoc(jsonObject.optString("descricaoLoc"));
                        painel.setData_Pub(jsonObject.optString("dataPublic"));
                        painel.setTipo_Painel(jsonObject.optString("tipoPainel"));
                        painel.setEstadoUtilizacao(jsonObject.optString("estadoUtilizacao"));
                        painel.setTempo_Duracao(jsonObject.optString("duracao"));
                        painel.setCodFace(jsonObject.optString("codFace"));
                        painel.setCB(jsonObject.optString("cb"));
                        painel.setAltura(jsonObject.optString("altura"));
                        painel.setLargura(jsonObject.optString("largura"));
                        painel.setCodigoPainel(jsonObject.optString("codigoPainel"));
                        painel.setImagLoc_url(jsonObject.optString("imagLoc_url"));
                        painel.setImagCamp_url(jsonObject.optString("imagCamp_url"));

                        listclientePainel.add(painel);
                    }
                    adapter = new ArrayAdapter<Painel>(ClienteCampanha.this, android.R.layout.simple_list_item_1, listclientePainel);
                    listViewclientePainel.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                adapter = new ArrayAdapter<Painel>(ClienteCampanha.this, android.R.layout.simple_list_item_1, listclientePainel);
                listViewclientePainel.setAdapter(adapter);
                // Toast.makeText(getBaseContext(), "Nao foi possivel conectar o Servidor \n" + error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        request.add(jsonObjectReq);
    }

}