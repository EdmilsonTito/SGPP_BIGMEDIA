package com.example.sgppbigmedia;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.sgppbigmedia.Campanha.ListarCampanha;
import com.example.sgppbigmedia.DataBase.DatabaseHelper;

public class Perfil extends AppCompatActivity {
    TextView nome ,emailf,bi,funcao,morada;
    ImageView imageView;
    Button btnalterar;
    DatabaseHelper db;
    RequestQueue request;
    JsonObjectRequest jsonObjectReq;
    Bitmap bitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        inicializarVariaveis();
        String ip = getString(R.string.ip);
        final String foto_url=getIntent().getStringExtra("FOTO_URL");
        final String perfil=getIntent().getStringExtra("PERFIL");
        final String idConta=getIntent().getStringExtra("IDCONTA");
        final String senha=getIntent().getStringExtra("SENHA");
        final String nomeUsuario=getIntent().getStringExtra("NOMEUSUARIO");
        final String email=getIntent().getStringExtra("EMAIL");
        nome.setText(getIntent().getStringExtra("NOME"));
        morada.setText(getIntent().getStringExtra("MORADA"));
        funcao.setText(getIntent().getStringExtra("FUNCAO"));
        bi.setText(getIntent().getStringExtra("BI"));
        emailf.setText(email);

        String urlfoto=ip+"/"+ foto_url;
        carregarWEBServiceImg(urlfoto);


        btnalterar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Chamar a Activity Alterar a Senha
                Intent intent = new Intent(Perfil.this, AlterarSenha.class);//passando o valor do email para a activity AlterarSenha
                intent.putExtra("EMAIL",email);
                intent.putExtra("SENHA",senha);
                intent.putExtra("IDCONTA",idConta);
                intent.putExtra("PERFIL",perfil);
                intent.putExtra("NOMEUSUARIO",nomeUsuario);
                startActivity(intent);
            }
        });

    }

    private void inicializarVariaveis() {
        nome = (TextView) findViewById(R.id.txtnome);
        emailf = (TextView) findViewById(R.id.txtEmail);
        bi = (TextView) findViewById(R.id.txtBI);
        morada = (TextView) findViewById(R.id.txtmorada);
        funcao = (TextView) findViewById(R.id.txtfuncao);
        btnalterar = (Button)findViewById(R.id.btnalterar);
        imageView = (ImageView) findViewById(R.id.imageView);
        request = Volley.newRequestQueue(getBaseContext());

    }


    private void carregarWEBServiceImg(String urlfoto) {
        urlfoto = urlfoto.replace(" ", "%20");
        ImageRequest imageRequest = new ImageRequest(urlfoto, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                bitmap=response;
                imageView.setImageBitmap(redimensionarImagem(bitmap,160,160));
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
            matrix.mapRadius(100);

            return Bitmap.createBitmap(bitmap,0,0,largura,altura,matrix,false);

        }else{
            return bitmap;
        }
    }

}
