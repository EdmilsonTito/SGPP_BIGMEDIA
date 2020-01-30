package com.example.sgppbigmedia;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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
import com.example.sgppbigmedia.Campanha.ListarCampanha;
import com.example.sgppbigmedia.DataBase.DatabaseHelper;
import com.example.sgppbigmedia.Painel.Painel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AlterarSenha extends AppCompatActivity {
    EditText senhaNova,senhaAntiga;
    Button btnAsenha;
    DatabaseHelper db;
    ProgressBar progressBar;
    RequestQueue request;
    JsonObjectRequest jsonObjectReq;
    StringRequest stringRequest;
    ProgressDialog progresso;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar_senha);
        inicializarVariaveis();
        db=new DatabaseHelper(this);
        final String nomeUsuario = (getIntent().getStringExtra("NOMEUSUARIO"));
        final String senha = (getIntent().getStringExtra("SENHA"));
        final String idConta = (getIntent().getStringExtra("IDCONTA"));
        final String perfil = (getIntent().getStringExtra("PERFIL"));

       btnAsenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String senhaN = senhaNova.getText().toString();
                String senhaA = senhaAntiga.getText().toString();

                if (senhaN.equals("") || senhaA.equals("")) {
                    Toast.makeText(getApplicationContext(), "Os Campos Senha está Vazio!", Toast.LENGTH_SHORT).show();
                } else if(senhaA.equals(senha)) {
                    ActualizarSenha(nomeUsuario,senhaN,idConta,perfil);
                }else{
                    Toast.makeText(getApplicationContext(), "Erro ao alterar a Senha, Senha Antiga Invalida!", Toast.LENGTH_SHORT).show();
                    senhaAntiga.setText("");
                }
            }
        });
    }

    private void ActualizarSenha(final String nomeUsuario, final String senhaN, final String idConta, final String perfil) {

            progresso = new ProgressDialog(this);
            progresso.setMessage("Actualizando a Senha...");
            progresso.show();
            String ip = getString(R.string.ip);
            String url = ip+"/apiAndroid/Perfil_Login/Alterar_Senha.php";

            stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    progresso.hide();
                    Toast.makeText(AlterarSenha.this, "Alterada com Sucesso", Toast.LENGTH_SHORT).show();
                    LimparCampos();
              /*  if (response.trim().equalsIgnoreCase("registra")) {
                    Toast.makeText(ActualizarCampanha.this, "Actualizado com sucesso", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ActualizarCampanha.this, " Nao Actualizado"+ response, Toast.LENGTH_SHORT).show();
                    Log.i("RESPOSTA: ", "" + response);
                }*/
                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progresso.hide();
                    Toast.makeText(AlterarSenha.this, "Erro ao Actualizar"+ error.toString(), Toast.LENGTH_SHORT).show();

                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String, String> paramentros = new HashMap<>();

                    paramentros.put("idConta", idConta);
                    paramentros.put("nomeUsuario", nomeUsuario);
                    paramentros.put("novaSenha", senhaN);
                    paramentros.put("perfil", perfil);

                    return paramentros;
                }

            };
            request.add(stringRequest);
        }


    private void inicializarVariaveis() {
        senhaNova = (EditText)findViewById(R.id.EdtpassNova);
        senhaAntiga = (EditText)findViewById(R.id.EdtpassAntiga);
        btnAsenha = (Button)findViewById(R.id.btnAlterarSenha);
        progressBar = (ProgressBar)findViewById(R.id.pbalSenha);
        progressBar.setVisibility(View.INVISIBLE);
        request = Volley.newRequestQueue(getBaseContext());
    }

    private void LimparCampos() {
        senhaNova.setText("");
        senhaAntiga.setText("");
        progressBar.setVisibility(View.INVISIBLE);
    }

}
