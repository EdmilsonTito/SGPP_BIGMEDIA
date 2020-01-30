package com.example.sgppbigmedia.Campanha;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.sgppbigmedia.DataBase.DatabaseHelper;
import com.example.sgppbigmedia.Painel.DescricaoPainel;
import com.example.sgppbigmedia.Painel.Painel;
import com.example.sgppbigmedia.Painel.PesquisaPainel;
import com.example.sgppbigmedia.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ListarCampanha extends AppCompatActivity {
    DatabaseHelper db;
    EditText edtpesquisarcliente;
    ListView listViewcliente;
    private List<Campanha> listcliente = new ArrayList<Campanha>();
    private ArrayAdapter adapter;
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;
    Timer timer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_campanha);
        inicializarVariaveis();
      //  ConsultarListaClientes();
        ConsultarWEBListarClientes();
        long TEMPO = (1000 * 2); // chama o método a cada 3 segundos

        if (timer == null) {
            timer = new Timer();
            TimerTask tarefa = new TimerTask() {

                public void run() {
                    try {
                        listcliente.clear();
                        ConsultarWEBListarClientes();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            timer.scheduleAtFixedRate(tarefa, TEMPO, TEMPO);
        }


        listViewcliente.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
                Campanha clienteEscolhido = (Campanha) adapter.getItemAtPosition(position);
                Intent i = new Intent(ListarCampanha.this, ClienteCampanha.class);
                i.putExtra("cliente-escolhido", clienteEscolhido);
                startActivity(i);
            }
        });


        edtpesquisarcliente.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                (ListarCampanha.this).adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    private void inicializarVariaveis() {
        edtpesquisarcliente = (EditText) findViewById(R.id.editPesquisacliente);
        listViewcliente = (ListView)findViewById(R.id.clientelistview);
        db = new DatabaseHelper(this);
        request = Volley.newRequestQueue(getBaseContext());
    }


    public void ConsultarWEBListarClientes(){

            String ip = getString(R.string.ip);
            String url = ip+"/apiAndroid/actualizar_Campanha/consultarListaCliente.php";
            url = url.replace(" ", "%20");

            jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Campanha campanha = null;
                    JSONArray json = response.optJSONArray("listaClientes");

                    try {
                        for (int i = 0; i < json.length(); i++) {
                            campanha = new Campanha();
                            JSONObject jsonObject = null;
                            jsonObject = json.getJSONObject(i);
                            campanha.setNomeCliente(jsonObject.optString("nomeCliente"));

                            listcliente.add(campanha);
                        }
                        adapter = new ArrayAdapter<Campanha>(ListarCampanha.this, android.R.layout.simple_list_item_1, listcliente);
                        listViewcliente.setAdapter(adapter);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    adapter = new ArrayAdapter<Campanha>(ListarCampanha.this, android.R.layout.simple_list_item_1, listcliente);
                    listViewcliente.setAdapter(adapter);
                   // Toast.makeText(getBaseContext(), "Nao foi possivel conectar o Servidor \n" + error.toString(), Toast.LENGTH_SHORT).show();
                }
            });
            request.add(jsonObjectRequest);
    }

}