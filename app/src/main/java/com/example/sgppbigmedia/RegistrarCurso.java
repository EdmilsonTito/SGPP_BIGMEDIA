package com.example.sgppbigmedia;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class RegistrarCurso extends AppCompatActivity {
    public EditText editcodigo, editnome, editcategoria, editprofessor;
    Button btnregistar, btnCarregarFoto;
    ImageView imgCarregada;
    ListView listView;
    ProgressDialog progresso;
    private List<Curso> listaCursos = new ArrayList<Curso>();//listitem
    private ArrayAdapter adapter;

    private static final int COD_SELECIONA = 10;
    private static final int COD_FOTO = 20;
    private static final int COD_PERMISSAO = 100;

    private static final String PASTA_PRINCIPAL = "minhasImagensApp/";  //dir principal
    private static final String PASTA_IMAGEM = "imagens";  //PASTA ONDE FICARAM AS FOTOS
    private static final String DIRETORIO_IMAGEM = PASTA_PRINCIPAL + PASTA_IMAGEM;
    StringRequest stringRequest;
    private String path;
    File fileImagem;
    Bitmap bitmap;

    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_curso);
        inicializarVariaveis();

       // carregarWEBServiceListarCurso();

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

        btnregistar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listaCursos.clear();
                carregarWEBService();
              //  carregarWEBServiceListarCurso();
            }
        });

    }

    private void CarregarDialog(){

        final CharSequence[] opcoes = {"Tirar Foto", "Selecionar da Galeria", "Cancelar"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
                Uri imageUri= FileProvider.getUriForFile(this,authorities,fileImagem);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            }else
            {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(fileImagem));
            }
            startActivityForResult(intent,COD_FOTO);
        }
    }




 /*   private void carregarWEBServiceListarCurso() {
        String url = "http://192.168.1.102/api/consultarLista.php";
        url = url.replace(" ", "%20");

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        request.add(jsonObjectRequest);

    }*/

    private void carregarWEBService() {

        progresso = new ProgressDialog(this);
        progresso.setMessage("Carregando...");
        progresso.show();

        String url = "http://192.168.1.100/api/registroImg.php?";

        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                progresso.hide();

                if (response.equals("registra")) {
                    editcodigo.setText("");
                    editnome.setText("");
                    editcategoria.setText("");
                    editprofessor.setText("");
                    Toast.makeText(RegistrarCurso.this, "Registrado com sucesso", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RegistrarCurso.this, " Nao Registrado"+ response, Toast.LENGTH_SHORT).show();
                    Log.i("RESPOSTA: ", "" + response);
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progresso.hide();
                Toast.makeText(RegistrarCurso.this, "Erro ao Registrar", Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                String codigo = editcodigo.getText().toString();
                String nome = editnome.getText().toString();
                String categoria = editcategoria.getText().toString();
                String professor = editprofessor.getText().toString();
                String imagem = converterImgString(bitmap);

                Map<String,String> paramentros = new HashMap<>();

                paramentros.put("codigo",codigo);
                paramentros.put("nome",nome);
                paramentros.put("categoria",categoria);
                paramentros.put("professor",professor);
                paramentros.put("imagem",imagem);

                return paramentros;
            }

        };
        request.add(stringRequest);

    }

    //Converter Imagem em String Para ser enviada no Banco de Dados
    private String converterImgString(Bitmap bitmap){
        ByteArrayOutputStream array = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100,array);
        byte[] imagemByte = array.toByteArray();
        String imagemString = Base64.encodeToString(imagemByte,Base64.DEFAULT);

        return imagemString;
    }


    private void inicializarVariaveis() {
        editcodigo = (EditText) findViewById(R.id.editcodigo);
        editnome = (EditText) findViewById(R.id.editNome);
        editcategoria = (EditText) findViewById(R.id.editCategoria);
        editprofessor = (EditText) findViewById(R.id.editProfessor);
        btnregistar = (Button) findViewById(R.id.btnregistar);
        btnCarregarFoto = (Button) findViewById(R.id.btncarregarNovaCampanha);
        imgCarregada = (ImageView) findViewById(R.id.imagemCarregada) ;
        listView = (ListView) findViewById(R.id.cursilistview);

        request = Volley.newRequestQueue(getBaseContext());
    }

 /*   @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getBaseContext(), "Nao foi possivel conectar o Servidor" + error.toString(), Toast.LENGTH_SHORT).show();
    }*/

  /*  @Override
    public void onResponse(JSONObject response) {
        Toast.makeText(getBaseContext(), "Registrado com Sucesso", Toast.LENGTH_SHORT).show();
        editcodigo.setText("");
        editnome.setText("");
        editcategoria.setText("");
        editprofessor.setText("");


        Curso curso = null;
        JSONArray json = response.optJSONArray("curso");

        try {
            for (int i = 0; i < json.length(); i++) {
                curso = new Curso();
                JSONObject jsonObject = null;
                jsonObject = json.getJSONObject(i);

                curso.setCodigo(jsonObject.optInt("codigo"));
                curso.setNome(jsonObject.optString("nome"));
                curso.setCategoria(jsonObject.optString("categoria"));
                curso.setProfessor(jsonObject.optString("professor"));
                listaCursos.add(curso);
            }
            adapter = new ArrayAdapter<Curso>(this, android.R.layout.simple_list_item_1, listaCursos);
            listView.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }*/


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
                Toast.makeText(this,"Permissões Aceitas",Toast.LENGTH_SHORT);
                btnCarregarFoto.setEnabled(true);
            }
        }else{
            solicitarPermissoesManual();
        }
    }

    // Solicitaçoes de Permissoes
    private void solicitarPermissoesManual() {
        final CharSequence[] opciones={"sim","não"};
        final AlertDialog.Builder alertOpciones=new AlertDialog.Builder(this);
        alertOpciones.setTitle("Deseja configurar as permissões manualmente?");
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("sim")){
                    Intent intent=new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri=Uri.fromParts("package", RegistrarCurso.this.getPackageName(),null);
                    intent.setData(uri);
                    startActivity(intent);
                }else{
                    Toast.makeText(RegistrarCurso.this,"Permissões Aceitas",Toast.LENGTH_SHORT).show();
                    dialogInterface.dismiss();
                }
            }
        });
        alertOpciones.show();
    }

    private void carregarDialogoRecomendacao() {
        AlertDialog.Builder dialogo=new AlertDialog.Builder(this);
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

}