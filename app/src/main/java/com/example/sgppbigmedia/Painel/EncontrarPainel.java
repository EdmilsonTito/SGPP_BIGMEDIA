package com.example.sgppbigmedia.Painel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.sgppbigmedia.DataBase.DatabaseHelper;
import com.example.sgppbigmedia.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class EncontrarPainel extends AppCompatActivity {

    DatabaseHelper db;
    ImageView btnpesquisa;
    EditText edtencontrarpainal;
    ListView listViewPainel;
    private List<Painel> listpainel = new ArrayList<Painel>();//listitem
    private ArrayAdapter adapter;
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encontrar_painel);
        inicializarVariaveis();
        ConsultarPainelOnline();
       // EncontrarPainel();

        listViewPainel.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
                Painel painelEscolhido = (Painel) adapter.getItemAtPosition(position);
                Intent i = new Intent(EncontrarPainel.this, MapsActivity.class);
                i.putExtra("painel-escolhido", painelEscolhido);
                startActivity(i);
            }
        });



        edtencontrarpainal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                (EncontrarPainel.this).adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void inicializarVariaveis() {
        btnpesquisa = (ImageView) findViewById(R.id.btnpesquisa);
        edtencontrarpainal = (EditText) findViewById(R.id.editEncontrarPainel);
        listViewPainel = (ListView)findViewById(R.id.painellistview);
        db = new DatabaseHelper(this);
        request = Volley.newRequestQueue(getBaseContext());

    }

    private void ConsultarPainelOnline() {
        String ip = getString(R.string.ip);

        String url = ip+"/apiAndroid/Consultar_Painel/consultarListaPainel.php";
        url = url.replace(" ", "%20");

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Painel painel = null;
                JSONArray json = response.optJSONArray("paineis");

                try {
                    for (int i = 0; i < json.length(); i++) {
                        painel = new Painel();
                        JSONObject jsonObject = null;
                        jsonObject = json.getJSONObject(i);
                        painel.setIdfaces(jsonObject.optInt("id_Faces"));
                        painel.setLatitude(jsonObject.optDouble("latitude"));
                        painel.setLongitude(jsonObject.optDouble("longitude"));
                        painel.setDescricaoLoc(jsonObject.optString("descricaoLoc"));
                        painel.setTipo_Painel(jsonObject.optString("tipoPainel"));
                        painel.setEstadoUtilizacao(jsonObject.optString("estadoUtilizacao"));
                        painel.setCodFace(jsonObject.optString("codFace"));
                        painel.setCB(jsonObject.optString("cb"));
                        painel.setAltura(jsonObject.optString("altura"));
                        painel.setLargura(jsonObject.optString("largura"));
                        painel.setCodigoPainel(jsonObject.optString("codigoPainel"));
                        painel.setImagLoc_url(jsonObject.optString("imagLoc_url"));

                        listpainel.add(painel);
                    }
                    adapter = new ArrayAdapter<Painel>(EncontrarPainel.this, android.R.layout.simple_list_item_1, listpainel);
                    listViewPainel.setAdapter(adapter);

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



  /*  public void EncontrarPainel(){
        db = new DatabaseHelper(this);
        listpainel = db.getListaPainel();
        db.close();

        if(listpainel != null){
            adapter = new ArrayAdapter<Painel>(this, android.R.layout.simple_list_item_1, listpainel);
            listViewPainel.setAdapter(adapter);
        }
    }
*/

}