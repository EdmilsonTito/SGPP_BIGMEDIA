package com.example.sgppbigmedia.Painel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.sgppbigmedia.DataBase.DatabaseHelper;
import com.example.sgppbigmedia.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DescricaoPainel extends AppCompatActivity {
    TextView nomeCliente ,tamanhoPainel,dataPublicacao,tempoDuracao,tipoPainel,cb,codPainel,codFace,Cliente,estadoUtilizacao,descricaoLoc,latitude,longitude;
    ImageView imageView;
    DatabaseHelper db;
    Painel painelEscolhido;
    RequestQueue request;
    JsonObjectRequest jsonObjectReq;
    JSONObject jsonObject;
    Bitmap bitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_descricao_painel);
        inicializarVariaveis();

        CarregarListView();
    }

    private void CarregarListView() {

        Intent intent = getIntent();
        painelEscolhido = (Painel) intent.getSerializableExtra("painel-escolhido") ;
        if(painelEscolhido != null && painelEscolhido.getEstadoUtilizacao().equals("Utilizado")) {
                carregarInfoCampanha(painelEscolhido.getIdfaces());
        } else{
            nomeCliente.setText("");
            Cliente.setText("");
            dataPublicacao.setText("");
            tempoDuracao.setText("");
            tamanhoPainel.setText(painelEscolhido.getAltura() + " x " + painelEscolhido.getLargura());
            tipoPainel.setText(painelEscolhido.getTipo_Painel());
            cb.setText(painelEscolhido.getCB());
            estadoUtilizacao.setText(painelEscolhido.getEstadoUtilizacao());
            codPainel.setText(painelEscolhido.getCodigoPainel());
            codFace.setText(painelEscolhido.getCodFace());
            descricaoLoc.setText(painelEscolhido.getDescricaoLoc());
            latitude.setText(painelEscolhido.getLatitude().toString());
            longitude.setText(painelEscolhido.getLongitude().toString());
            final String foto_url = painelEscolhido.getImagLoc_url();

            if(foto_url.equals("") || foto_url.equals("null")){
                imageView.setImageResource(R.drawable.sem_foto);
            }else{
                String ip = getString(R.string.ip);
                String urlfoto=ip+"/"+ foto_url;
                carregarWEBServiceImg(urlfoto);
            }

            imageView.setImageResource(R.drawable.sem_foto);
        }
    }

    private void carregarInfoCampanha(Integer id_Faces) {
           final  String ip = getString(R.string.ip);
            String url = ip+"/apiAndroid/Consultar_Painel/consultarDescricaoPainel.php?id_Faces=" + id_Faces ;
            url = url.replace(" ", "%20");

            jsonObjectReq = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    JSONArray json = response.optJSONArray("descricaoPainel");


                    try {
                        jsonObject = json.getJSONObject(0);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                        nomeCliente.setText(jsonObject.optString("nomeCliente"));
                        tamanhoPainel.setText(jsonObject.optString("altura") + " x " +jsonObject.optString("largura"));
                        dataPublicacao.setText(jsonObject.optString("dataPublic"));
                        tempoDuracao.setText(jsonObject.optString("duracao") + " Meses");
                        tipoPainel.setText(jsonObject.optString("tipoPainel"));
                        estadoUtilizacao.setText(jsonObject.optString("estadoUtilizacao"));
                        cb.setText(jsonObject.optString("cb"));
                        codPainel.setText(jsonObject.optString("codigoPainel"));
                        codFace.setText(jsonObject.optString("codFace"));
                        descricaoLoc.setText(jsonObject.optString("descricaoLoc"));
                        latitude.setText(jsonObject.optString("latitude"));
                        longitude.setText(jsonObject.optString("longitude"));
                        final String foto_url = jsonObject.optString("imagCamp_url");

                        if(foto_url.equals("") || foto_url.equals("null")){
                            imageView.setImageResource(R.drawable.sem_foto);
                        }else{
                            String urlfoto=ip+"/"+ foto_url;
                            carregarWEBServiceImg(urlfoto);
                        }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "Erro ao consultar Dados no Banco de dados"+error.toString(), Toast.LENGTH_SHORT).show();
                }
            });
            request.add(jsonObjectReq);

    }

    private void inicializarVariaveis() {
        nomeCliente = (TextView) findViewById(R.id.txtnomecliente);
        tamanhoPainel = (TextView) findViewById(R.id.txttamanhoPainel);
        dataPublicacao = (TextView) findViewById(R.id.txtdatapublicacao);
        tempoDuracao = (TextView) findViewById(R.id.txttempoDuracao);
        tipoPainel = (TextView) findViewById(R.id.txttipoPainel);
        cb = (TextView) findViewById(R.id.txtCB);
        codPainel = (TextView) findViewById(R.id.txtcodPainel);
        codFace = (TextView) findViewById(R.id.txtfacePainel);
        Cliente = (TextView) findViewById(R.id.Cliente);
        estadoUtilizacao = (TextView) findViewById(R.id.txtestadoUtilizacao);
        latitude =(TextView)findViewById(R.id.txtLatitude);
        longitude =(TextView)findViewById(R.id.txtlongitude);
        descricaoLoc = (TextView)findViewById(R.id.txtdescricaoLoc);
        painelEscolhido = new Painel();
        imageView= (ImageView)findViewById(R.id.imgCampSelecionada);
        request = Volley.newRequestQueue(getBaseContext());

        db = new DatabaseHelper(this);
    }


    private void carregarWEBServiceImg(String urlfoto) {
        urlfoto = urlfoto.replace(" ", "%20");
        ImageRequest imageRequest = new ImageRequest(urlfoto, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                bitmap=response;
                imageView.setImageBitmap(redimensionarImagem(bitmap,400,400)); // colocando a imagem na imagemView do menu e redimensionando para 120,120
            }
        }, 0, 0, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Erro ao Carregar Imagem"+error, Toast.LENGTH_SHORT).show();
            }
        });
        request.add(imageRequest);
    }


    private Bitmap redimensionarImagem(Bitmap bitmap, float larguraNova, float alturaNova) {

        int largura=bitmap.getWidth();
        int altura=bitmap.getHeight();

        if(largura>larguraNova || altura>alturaNova){
            float escalaLargura=larguraNova/largura;
            float escalaAltura= alturaNova/altura;

            Matrix matrix=new Matrix();
            matrix.postScale(escalaLargura,escalaAltura);

            return Bitmap.createBitmap(bitmap,0,0,largura,altura,matrix,false);

        }else{
            return bitmap;
        }
    }
}