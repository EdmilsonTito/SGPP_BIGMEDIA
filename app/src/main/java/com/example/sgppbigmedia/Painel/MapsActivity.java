package com.example.sgppbigmedia.Painel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sgppbigmedia.DataBase.DatabaseHelper;
import com.example.sgppbigmedia.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {
    TextView nomeCliente, tamanhoPainel, dataPublicacao, tempoDuracao, tipoPainel, cb, codPainel, codFace, Cliente, estadoUtilizacao, descricaoLoc, latitude, longitude;
    DatabaseHelper db;
    Painel painelEscolhido, painel;
    public static List<Painel> listpainel1 = new ArrayList<Painel>();
    private ArrayAdapter<Painel> adapter;
    Painel p = new Painel();

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        inicializarVariaveis();
        CarregarListView();

    }

    private void CarregarListView() {
        Intent intent = getIntent();
        painelEscolhido = (Painel) intent.getSerializableExtra("painel-escolhido");
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
        latitude = (TextView) findViewById(R.id.txtLatitude);
        longitude = (TextView) findViewById(R.id.txtlongitude);
        descricaoLoc = (TextView) findViewById(R.id.txtdescricaoLoc);
        painelEscolhido = new Painel();
        painel = new Painel();
        db = new DatabaseHelper(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Adicionando um marcador  e movendo a camera para o marcador
        LatLng localizacao = new LatLng(painelEscolhido.getLatitude(), painelEscolhido.getLongitude());
        mMap.addMarker(new MarkerOptions().position(localizacao).title("Codigo Painel: "+painelEscolhido.getCodigoPainel() + '-' + painelEscolhido.getCodFace() + "\nTamanho Painel: " + painelEscolhido.getAltura() +" x " + painelEscolhido.getLargura()
                + "\nCB: " + painelEscolhido.getCB() + "\nTipo Painel: " + painelEscolhido.getTipo_Painel()
                + "\nDescrição Localização: " + painelEscolhido.getDescricaoLoc()));


        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }
            @Override
            public View getInfoContents(Marker marker) {
                LinearLayout l =new LinearLayout(MapsActivity.this);
                l.setPadding(20,20,20,20);
                l.setBackgroundColor(Color.WHITE);
                TextView tv = new TextView(MapsActivity.this);
                tv.setText(marker.getTitle());
                tv.setTextColor(Color.BLACK);
                ImageView img = new ImageView(MapsActivity.this);
                img.setImageResource(R.drawable.logo_login);
              //img.setImageBitmap(painelEscolhido.getImgCamp());
                l.addView(tv);
                l.addView(img);
                return l;
            }
        }); //Personalização das Informaçoes do Marcador no Mapa

        mMap.moveCamera(CameraUpdateFactory.newLatLng(localizacao));
        mMap.getUiSettings().setZoomControlsEnabled(true);
        CameraUpdate localizacao1= CameraUpdateFactory.newLatLngZoom(localizacao,11F);
        mMap.animateCamera(localizacao1);
        // mMap.setMyLocationEnabled(true);
        if (ActivityCompat.checkSelfPermission(MapsActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(MapsActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            return;
        googleMap.setMyLocationEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.map_options,menu);
        return  true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
       switch (item.getItemId()){
           case R.id.normal_map:
               mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
               return true;
           case R.id.hybrid_map:
               mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
               return true;
           case R.id.satellite_map:
               mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
               return true;
           case R.id.terrain_map:
               mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
               return true;

             default:

               return super.onOptionsItemSelected(item);
       }
    }

}