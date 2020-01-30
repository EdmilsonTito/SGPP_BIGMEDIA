package com.example.sgppbigmedia.Campanha;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.sgppbigmedia.DataBase.DatabaseHelper;
import com.example.sgppbigmedia.Painel.Painel;
import com.example.sgppbigmedia.R;
import com.example.sgppbigmedia.RegistrarCurso;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static java.security.AccessController.getContext;


public class ActualizarCampanha extends AppCompatActivity {
    TextView nomeCliente,dataPublicacao,cb,codPainel,codFace;
    Button btnActualizarFoto, btnCarregarFoto;
    ImageView imgCarregada;
    DatabaseHelper db;
    ProgressDialog progresso;
    Painel painelporActualizar;

    private static final int COD_SELECIONA = 10;
    private static final int COD_FOTO = 20;
    private static final int COD_PERMISSAO = 100;

    private static final String PASTA_PRINCIPAL = "minhasImagensApp/";  //dir principal
    private static final String PASTA_IMAGEM = "imagens";  //PASTA ONDE FICARAM AS FOTOS
    private static final String DIRETORIO_IMAGEM = PASTA_PRINCIPAL + PASTA_IMAGEM;

    private String path;
    File fileImagem;
    Bitmap bitmap;
    RequestQueue request;
    StringRequest stringRequest;
    JsonObjectRequest jsonObjectReq;
    JSONObject jsonObject;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_campanha);
        inicializarVariaveis();
        CarregarListView();

        if(solicitarPermissoesVersoesSuperiores()){
            btnCarregarFoto.setEnabled(true);
        }else{
            btnCarregarFoto.setEnabled(false);
        } //Verificar se as Permissoes foram aceite e desabilitar o botao se nao forem aceites

        btnCarregarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CarregarDialog(); //carregar caixa de dialogo
            }
        });

        btnActualizarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActualizarCampanhaWEBService(); //Actualizar Campanha
            }
        });

    }

    private void CarregarDialog(){

        final CharSequence[] opcoes = {"Tirar Foto", "Selecionar da Galeria", "Cancelar"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(ActualizarCampanha.this);
        builder.setTitle("Escolha uma Opção");
        builder.setItems(opcoes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opcoes[i].equals("Tirar Foto")){
                    AbrirCamera();
                }else{
                    if (opcoes[i].equals("Selecionar da Galeria")){// metodo que abri a galeria
                        Intent intent=new Intent(Intent.ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/");
                        startActivityForResult(intent.createChooser(intent,"Selecione"),COD_SELECIONA);

                    }else{
                        dialogInterface.dismiss();
                    }
                }
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {// metodo que seta a imagem selecionada da galeria, na ImagemView do Formulario
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case COD_SELECIONA:
                Uri imagemCampanha=data.getData();
                imgCarregada.setImageURI(imagemCampanha);

                try {
                    bitmap=MediaStore.Images.Media.getBitmap(this.getContentResolver(),imagemCampanha);

                    if(bitmap.getWidth()>2000 || bitmap.getHeight()>2000){
                        Matrix matrix=new Matrix();
                        matrix.postRotate(90);
                        bitmap=Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,false);
                    }
                    imgCarregada.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;

            case COD_FOTO:
                MediaScannerConnection.scanFile(this, new String[]{path}, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            @Override
                            public void onScanCompleted(String path, Uri uri) {
                                Log.i("Path",""+path);
                            }
                        });

                    bitmap= BitmapFactory.decodeFile(path);
                    Matrix matrix=new Matrix();
                    matrix.postRotate(90);
                    bitmap=Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,false);
                    imgCarregada.setImageBitmap(bitmap);
                    break;
        }
    }

    private void AbrirCamera() {
        File meuFile = new File(Environment.getExternalStorageDirectory(), DIRETORIO_IMAGEM);
        boolean estaCriada = meuFile.exists();

        if(estaCriada == false){
            estaCriada = meuFile.mkdirs();
        }
        if(estaCriada == true){
            //Extração do Nome da Imagem
            Long consecultivo = System.currentTimeMillis()/1000;
            String nome = consecultivo.toString()+".jpg";

            path = Environment.getExternalStorageDirectory() + File.separator + DIRETORIO_IMAGEM + File.separator + nome;  //caminho completo da imagem
            fileImagem = new File(path);
            Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(fileImagem));

            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N)
            {
                String authorities=this.getPackageName()+".provider";
                Uri imageUri= FileProvider.getUriForFile(ActualizarCampanha.this,authorities,fileImagem);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            }else
            {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(fileImagem));
            }
            startActivityForResult(intent,COD_FOTO);
        }
    }

    private boolean solicitarPermissoesVersoesSuperiores() {

        if (Build.VERSION.SDK_INT<Build.VERSION_CODES.M){//validar se estamos em versão de android menor que 6 para solicitar permissoes
            return true;
        }

        //ver se as permissões foram aceitas
        if((this.checkSelfPermission(WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED)&& this.checkSelfPermission(CAMERA)==PackageManager.PERMISSION_GRANTED){
            return true;
        }


        if ((shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE)||(shouldShowRequestPermissionRationale(CAMERA)))){
            carregarDialogoRecomendacao();
        }else{
            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, COD_PERMISSAO);
        }

        return false;//processa o evento dependendo do que se defina aqui
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==COD_PERMISSAO){
            if(grantResults.length==2 && grantResults[0]==PackageManager.PERMISSION_GRANTED && grantResults[1]==PackageManager.PERMISSION_GRANTED){//REPRESENTA DUAS PERMISSOES
                Toast.makeText(ActualizarCampanha.this,"Permissões Aceitas",Toast.LENGTH_SHORT);
                btnCarregarFoto.setEnabled(true);
            }
        }else{
            solicitarPermissoesManual();
        }
    }

// Solicitaçoes de Permissoes
    private void solicitarPermissoesManual() {
        final CharSequence[] opciones={"sim","não"};
        final AlertDialog.Builder alertOpciones=new AlertDialog.Builder(ActualizarCampanha.this);
        alertOpciones.setTitle("Deseja configurar as permissões manualmente?");
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("sim")){
                    Intent intent=new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri=Uri.fromParts("package", ActualizarCampanha.this.getPackageName(),null);
                    intent.setData(uri);
                    startActivity(intent);
                }else{
                    Toast.makeText(ActualizarCampanha.this,"Permissões Aceitas",Toast.LENGTH_SHORT).show();
                    dialogInterface.dismiss();
                }
            }
        });
        alertOpciones.show();
    }

    private void carregarDialogoRecomendacao() {
        AlertDialog.Builder dialogo=new AlertDialog.Builder(ActualizarCampanha.this);
        dialogo.setTitle("Permissões Desativadas");
        dialogo.setMessage("Deve aceitar as permissões para funcionamento completo do App");

        dialogo.setPositiveButton("Aceitar", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE,CAMERA},100);
            }
        });
        dialogo.show();
    }


    private void carregarWEBServiceImg(String urlfoto) {
        urlfoto = urlfoto.replace(" ", "%20");
        ImageRequest imageRequest = new ImageRequest(urlfoto, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                bitmap=response;
                imgCarregada.setImageBitmap(bitmap); // colocando a imagem na imagemView do menu e redimensionando para 120,120
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
            matrix.postRotate(45);

            return Bitmap.createBitmap(bitmap,0,0,largura,altura,matrix,false);

        }else{
            return bitmap;
        }
    }

    private void ActualizarCampanhaWEBService() {

        progresso = new ProgressDialog(this);
        progresso.setMessage("Actualizando a Campanha...");
        progresso.show();
        String ip = getString(R.string.ip);

        String url = ip+"/actualizarImagCampanhaAndroid.php";

        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                progresso.hide();

                Toast.makeText(ActualizarCampanha.this, "Actualizado com Sucesso", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(ActualizarCampanha.this, "Erro ao Actualizar"+ error.toString(), Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Intent intent = getIntent();
                painelporActualizar = (Painel) intent.getSerializableExtra("painel-escolhido");

                String idCampanha = painelporActualizar.getIdCampanha().toString();
                String dataPublic = painelporActualizar.getData_Pub();
                String duracao = painelporActualizar.getTempo_Duracao();
                String imagCamp = converterImgString(bitmap);
                String imagCamp_url = painelporActualizar.getImagCamp_url();
                String idfaces = painelporActualizar.getIdfaces().toString();
                String idCliente = painelporActualizar.getIdCliente().toString();
                String idFunc = painelporActualizar.getIdFunc().toString();
                String nomeCliente = painelporActualizar.getNomeCliente();
                String codigoPainel = painelporActualizar.getCodigoPainel();
                String codFace = painelporActualizar.getCodFace();

                Map<String, String> paramentros = new HashMap<>();

                paramentros.put("idCampanha", idCampanha);
                paramentros.put("dataPublic", dataPublic);
                paramentros.put("duracao", duracao);
                paramentros.put("imagCamp", imagCamp);
                paramentros.put("imagCamp_url", imagCamp_url);
                paramentros.put("idfaces", idfaces);
                paramentros.put("idCliente", idCliente);
                paramentros.put("idFunc", idFunc);
                paramentros.put("nomeCliente", nomeCliente);
                paramentros.put("codigoPainel", codigoPainel);
                paramentros.put("codFace", codFace);

                return paramentros;
            }

        };
        request.add(stringRequest);
    }


    private void CarregarListView() {
        String ip = getString(R.string.ip);
        Intent intent = getIntent();
        painelporActualizar = (Painel) intent.getSerializableExtra("painel-escolhido") ;
        if(painelporActualizar != null) {
            nomeCliente.setText(painelporActualizar.getNomeCliente());
            dataPublicacao.setText(painelporActualizar.getData_Pub());
            cb.setText(painelporActualizar.getCB());
            codPainel.setText(painelporActualizar.getCodigoPainel()+"-"+painelporActualizar.getCodFace());

            final String foto_url = painelporActualizar.getImagCamp_url();

            if(foto_url.equals("") || foto_url.equals("null")){
                imgCarregada.setImageResource(R.drawable.sem_foto);
            }else{
                String urlfoto=ip+"/"+ foto_url;
                carregarWEBServiceImg(urlfoto);
            }
        }
    }


    private String converterImgString(Bitmap bitmap){
        ByteArrayOutputStream array = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100,array);
        byte[] imagemByte = array.toByteArray();
        String imagemString = Base64.encodeToString(imagemByte,Base64.DEFAULT);

        return imagemString;
    }

    private void inicializarVariaveis() {
        nomeCliente = (TextView) findViewById(R.id.txtnomecliente);
        dataPublicacao = (TextView) findViewById(R.id.txtdatapublicacao);
        cb = (TextView) findViewById(R.id.txtCB);
        codPainel = (TextView) findViewById(R.id.txtcodPainel);
        codFace = (TextView) findViewById(R.id.txtfacePainel);
        imgCarregada = (ImageView) findViewById(R.id.imagemCarregada);
        btnActualizarFoto = (Button) findViewById(R.id.btnActualizar) ;
        btnCarregarFoto = (Button) findViewById(R.id.btncarregarNovaCampanha);
        painelporActualizar = new Painel();
        request = Volley.newRequestQueue(getBaseContext());
    }

}