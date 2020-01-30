package com.example.sgppbigmedia;


import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.sgppbigmedia.DataBase.DatabaseHelper;
import com.example.sgppbigmedia.Tarefas.VisualizarTarefas;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TelaLogin extends AppCompatActivity {
    public EditText edtemail, edtsenha;
    Button btnautenticar;
    DatabaseHelper db;
    TextView textView;
    private ProgressBar progressBar;
    RequestQueue request;
    JsonObjectRequest jsonObjectReq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_login);
        inicializarVariaveis();

        btnautenticar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtemail.getText().toString().equals("") && edtsenha.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "Os Campos estão Vazios! ", Toast.LENGTH_LONG).show();
                }else if(edtsenha.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), " Campo Senha está Vazio!", Toast.LENGTH_LONG).show();
                }else if(edtemail.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), " Campo Email está Vazio!", Toast.LENGTH_LONG).show();
                }else{
                    btnautenticar.setVisibility(View.GONE);
                    carregarWEBValidarLogin();//funcao validar login com BD Online - Mysql
                }

            }
        });
    }

    private void carregarWEBValidarLogin() {
        progressBar.setVisibility(View.VISIBLE);
        String ip = getString(R.string.ip);
        String email = edtemail.getText().toString();
        String senha = edtsenha.getText().toString();

        String url = ip+"/apiAndroid/Perfil_Login/Autenticar_Login.php?email=" + email + "&senha=" + senha ;
        url = url.replace(" ", "%20");

        jsonObjectReq = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressBar.setVisibility(View.GONE);
                JSONArray json = response.optJSONArray("Usuario");
                JSONObject jsonObject = null;

                try {
                    jsonObject = json.getJSONObject(0);
                    String nomeUsuario=jsonObject.optString("nomeUsuario");
                    String email=jsonObject.optString("email");
                    String senha=jsonObject.optString("senha");
                    String nome=jsonObject.optString("nome");
                    String morada=jsonObject.optString("morada");
                    String funcao=jsonObject.optString("funcao");
                    String idConta =jsonObject.optString("idConta");
                    String perfil=jsonObject.optString("perfil");
                    String bi=jsonObject.optString("bi");
                    String foto_url=jsonObject.optString("foto_url");

                    if(nomeUsuario.equals(edtemail.getText().toString()) && senha.equals(edtsenha.getText().toString())){
                        MenuPrincipal(email,senha,nome,morada,funcao,idConta,perfil,bi,foto_url,nomeUsuario);
                        finish();
                    }else{
                        Toast.makeText(getApplicationContext(), "Email ou Senha Errada!", Toast.LENGTH_SHORT).show();
                        btnautenticar.setVisibility(View.VISIBLE);
                        LimparCampos();
                    }

                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Erro ao Validar no Banco de dados"+error.toString(), Toast.LENGTH_SHORT).show();
                btnautenticar.setVisibility(View.VISIBLE);
                LimparCampos();
            }
        });
        request.add(jsonObjectReq);
    }


    private void MenuPrincipal(String email,String senha,String nome,String morada,String funcao,String idConta,String perfil,String bi, String foto_url,String nomeUsuario) {
        Intent intent = new Intent(this, MenuPrincipal.class);
        //passando os valor  para a activity MenuPrincipal
        intent.putExtra("EMAIL",email);
        intent.putExtra("SENHA",senha);
        intent.putExtra("NOME",nome);
        intent.putExtra("MORADA",morada);
        intent.putExtra("FUNCAO",funcao);
        intent.putExtra("IDCONTA",idConta);
        intent.putExtra("PERFIL",perfil);
        intent.putExtra("BI",bi);
        intent.putExtra("NOMEUSUARIO",nomeUsuario);
        intent.putExtra("FOTO_URL",foto_url);
        startActivity(intent);
    }

    private void inicializarVariaveis() {
        edtemail = (EditText)findViewById(R.id.EdtEmail);
        edtsenha = (EditText)findViewById(R.id.EdtSenha);
        btnautenticar = (Button)findViewById(R.id.btnAutenticar);
        progressBar = (ProgressBar) findViewById(R.id.pblogin);
        request = Volley.newRequestQueue(getBaseContext());
    }

    private void LimparCampos() {
        edtsenha.setText("");
        progressBar.setVisibility(View.GONE);
    }

    public void registar(View view) {

        Intent intent = new Intent(this, RegistrarCurso.class);
        startActivity(intent);

    }
}