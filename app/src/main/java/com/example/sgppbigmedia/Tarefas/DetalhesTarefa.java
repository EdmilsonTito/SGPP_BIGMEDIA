package com.example.sgppbigmedia.Tarefas;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.sgppbigmedia.Campanha.ActualizarCampanha;
import com.example.sgppbigmedia.DataBase.DatabaseHelper;
import com.example.sgppbigmedia.Painel.Painel;
import com.example.sgppbigmedia.R;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DetalhesTarefa extends AppCompatActivity {
    TextView Titulo ,data,Prioridade,EstadoTarefa,DescricaoTarefa;
    DatabaseHelper db;
    Tarefas tarefaEscolhida,tarefaporActulizar;
    Button btnExecucao,btnParado, btnConcluido;
    ProgressDialog progresso;
    RequestQueue request;
    StringRequest stringRequest;
    JsonObjectRequest jsonObjectReq;
    JSONObject jsonObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_tarefa);
        inicializarVariaveis();
        CarregarListView();

      btnExecucao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               final String EstadoTarefa="Em Execucao";
                ActualizarEstadoWEBService(tarefaEscolhida.getIdTarefa().toString(),EstadoTarefa);
            }
        });
        btnParado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String EstadoTarefa="Parado";
                ActualizarEstadoWEBService(tarefaEscolhida.getIdTarefa().toString(),EstadoTarefa);
            }
        });
        btnConcluido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String EstadoTarefa="Concluido";
                ActualizarEstadoWEBService(tarefaEscolhida.getIdTarefa().toString(),EstadoTarefa);
            }
        });
    }

    private void CarregarListView() {
        Intent intent = getIntent();
        tarefaEscolhida = (Tarefas) intent.getSerializableExtra("tarefa-escolhida") ;
            Titulo.setText(tarefaEscolhida.getTitulo());
            data.setText(tarefaEscolhida.getData());
            Prioridade.setText(tarefaEscolhida.getPrioridade());
            EstadoTarefa.setText(tarefaEscolhida.getEstadotarefa());
            DescricaoTarefa.setText(tarefaEscolhida.getDescricao());
    }


    private void ActualizarEstadoWEBService(final String idTarefa, final String estado) {
        progresso = new ProgressDialog(this);
        progresso.setMessage("Alterando Estado...");
        progresso.show();
        String ip = getString(R.string.ip);
        String url = ip+"/apiAndroid/Visualizar_Tarefas/alterarEstadoTarefa.php";

        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                progresso.hide();
                Toast.makeText(DetalhesTarefa.this, "Alterada com Sucesso", Toast.LENGTH_SHORT).show();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DetalhesTarefa.this, "Erro ao Actualizar"+ error.toString(), Toast.LENGTH_SHORT).show();
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

    private void inicializarVariaveis() {
        Titulo = (TextView) findViewById(R.id.txttituloTarefa);
        data = (TextView) findViewById(R.id.txtData);
        Prioridade = (TextView) findViewById(R.id.txtPrioridade);
        EstadoTarefa = (TextView) findViewById(R.id.txtestadotarefa);
        DescricaoTarefa = (TextView) findViewById(R.id.txtDescricaoTarefa);
        btnExecucao=(Button)findViewById(R.id.btnexecucao);
        btnParado=(Button)findViewById(R.id.btnparado);
        btnConcluido=(Button)findViewById(R.id.btnConcluido);
        request = Volley.newRequestQueue(getBaseContext());
    }
}