package com.example.sgppbigmedia;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.example.sgppbigmedia.Campanha.ListarCampanha;
import com.example.sgppbigmedia.Painel.EncontrarPainel;
import com.example.sgppbigmedia.Painel.PesquisaPainel;
import com.example.sgppbigmedia.Tarefas.DetalhesTarefa;
import com.example.sgppbigmedia.Tarefas.VisualizarTarefas;
import java.util.Timer;
import java.util.TimerTask;

public class MenuPrincipal extends AppCompatActivity {
    TextView menu, pesquisarPainel,encontrarPainel,verTarefas,actualizarCampanha;
    ImageView tvperfil;
    RequestQueue request;
    Bitmap bitmap;
    Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);
        inicializarVariaveis();
        final  String ip = getString(R.string.ip);
        long TEMPO = (1000 * 2); // chama o método a cada 2 segundos

        if (timer == null) {
            timer = new Timer();
            TimerTask tarefa = new TimerTask() {

                public void run() {
                    try {
                        final String foto_url=getIntent().getStringExtra("FOTO_URL");
                      //  gerarNotificacao();
                        String urlfoto=ip+"/"+ foto_url;
                        carregarWEBServiceImg(urlfoto);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            timer.scheduleAtFixedRate(tarefa, TEMPO, TEMPO);
        }


        final String email=getIntent().getStringExtra("EMAIL");
        final String senha=getIntent().getStringExtra("SENHA");
        final String nome=getIntent().getStringExtra("NOME");
        final String morada=getIntent().getStringExtra("MORADA");
        final String funcao=getIntent().getStringExtra("FUNCAO");
        final String idConta=getIntent().getStringExtra("IDCONTA");
        final String perfil=getIntent().getStringExtra("PERFIL");
        final String bi=getIntent().getStringExtra("BI");
        final String foto_url=getIntent().getStringExtra("FOTO_URL");
        final String nomeUsuario=getIntent().getStringExtra("NOMEUSUARIO");

        String urlfoto=ip+"/"+ foto_url;
        carregarWEBServiceImg(urlfoto);


        // emaildigitado = getIntent().getStringExtra("EMAIL");


        tvperfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuPrincipal.this, Perfil.class);
                intent.putExtra("EMAIL",email);
                intent.putExtra("SENHA",senha);
                intent.putExtra("NOME",nome);
                intent.putExtra("MORADA",morada);
                intent.putExtra("FUNCAO",funcao);
                intent.putExtra("IDCONTA",idConta);
                intent.putExtra("PERFIL",perfil);
                intent.putExtra("NOMEUSUARIO",nomeUsuario);
                intent.putExtra("BI",bi);
                intent.putExtra("FOTO_URL",foto_url);
                startActivity(intent);
            }
        });

        pesquisarPainel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuPrincipal.this, PesquisaPainel.class);
                startActivity(intent);
            }
        });

        encontrarPainel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuPrincipal.this, EncontrarPainel.class);
                startActivity(intent);
            }
        });
        verTarefas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuPrincipal.this, VisualizarTarefas.class);
                startActivity(intent);
            }
        });
        actualizarCampanha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuPrincipal.this, ListarCampanha.class);
                startActivity(intent);
            }
        });
    }

    private void gerarNotificacao() {
        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        PendingIntent p = PendingIntent.getActivity(MenuPrincipal.this, 0, new Intent(MenuPrincipal.this, DetalhesTarefa.class), 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(MenuPrincipal.this);
        builder.setTicker("Ticker Texto");
        builder.setContentTitle("Título");
        //builder.setContentText("Descrição");
        builder.setSmallIcon(R.drawable.logo_login);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.logo_login));
        builder.setContentIntent(p);

        NotificationCompat.InboxStyle style = new NotificationCompat.InboxStyle();
        String [] descs = new String[]{"Descrição 1", "Descrição 2", "Descrição 3", "Descrição 4"};
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
            Ringtone toque = RingtoneManager.getRingtone(MenuPrincipal.this, som);
            toque.play();
        }
        catch(Exception e){}
    }

    public void logout(View view) {
        Intent intent = new Intent(this, TelaLogin.class);
        startActivity(intent);
        finish();
    }

    private void inicializarVariaveis() {
        pesquisarPainel = (TextView) findViewById(R.id.pesquisarpainel);
        encontrarPainel = (TextView) findViewById(R.id.encontrarpainel);
        verTarefas = (TextView) findViewById(R.id.vizualizartearefas);
        actualizarCampanha = (TextView) findViewById(R.id.ActualizarCampanha);
        menu = (TextView) findViewById(R.id.menu);
        tvperfil = (ImageView) findViewById(R.id.perfil);

        request = Volley.newRequestQueue(getBaseContext());
    }

    public void onBackPressed(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(MenuPrincipal.this);
        builder.setMessage("Deseja Realmente Sair da Aplicação ?");
        builder.setCancelable(true);
        builder.setNegativeButton("Nao", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.cancel();
            }
        });
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                finish();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    } //Pergunta se quer realmente fechar o programa

    private void carregarWEBServiceImg(String urlfoto) {
        urlfoto = urlfoto.replace(" ", "%20");
        ImageRequest imageRequest = new ImageRequest(urlfoto, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                bitmap=response;
                tvperfil.setImageBitmap(redimensionarImagem(bitmap,120,120)); // colocando a imagem na imagemView do menu e redimensionando para 120,120
            }
        }, 0, 0, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(getApplicationContext(), "Erro ao Carregar Imagem"+error, Toast.LENGTH_SHORT).show();
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