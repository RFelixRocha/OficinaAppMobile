package br.com.felixtec.oficinaappmobile;

import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import br.com.felixtec.oficinaappmobile.Modulos.DirectionFinder;
import br.com.felixtec.oficinaappmobile.Modulos.DirectionFinderListener;

import br.com.felixtec.oficinaappmobile.Fragments.CadastrarNvOficFragment;
import br.com.felixtec.oficinaappmobile.Fragments.ImportFragment;
import br.com.felixtec.oficinaappmobile.Modulos.Route;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback,ExitDialog.ExitListener,DirectionFinderListener,GoogleMap.InfoWindowAdapter {

    SupportMapFragment sMapFragment;
    private GoogleMap mMap;
    private View markerInfo;

    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inicializarComponentes();


        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

     /*   FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        android.support.v4.app.FragmentManager sFm = getSupportFragmentManager();
        if (!sMapFragment.isAdded()) {
            sFm.beginTransaction().replace(R.id.map, sMapFragment).commit();
        }else {
            sFm.beginTransaction().show(sMapFragment).commit();
        }


    }

    private void inicializarComponentes(){
        sMapFragment = SupportMapFragment.newInstance();
        sMapFragment.getMapAsync(this);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        android.support.v4.app.FragmentManager sFm = getSupportFragmentManager();

        int id = item.getItemId();

        if (sMapFragment.isAdded())
            sFm.beginTransaction().hide(sMapFragment).commit();

            ImportFragment fragment = new ImportFragment();
            CadastrarNvOficFragment nvOficFragment = new CadastrarNvOficFragment();
     if(id == R.id.nav_transito){

         if (!sMapFragment.isAdded()) {
             sFm.beginTransaction().replace(R.id.map, sMapFragment).commit();
         }else {
             sFm.beginTransaction().show(sMapFragment).commit();
         }
         mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
     }else

     if (id == R.id.nav_satelite) {

         if (!sMapFragment.isAdded()) {
                sFm.beginTransaction().replace(R.id.map, sMapFragment).commit();
            }else {
                sFm.beginTransaction().show(sMapFragment).commit();
            }

             mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        } else if (id == R.id.nav_oficina) {
            if (!fragment.isAdded()) {
                android.support.v4.app.FragmentTransaction fragmentTrasaction =
                        getSupportFragmentManager().beginTransaction();
                fragmentTrasaction.replace(R.id.content_frame, nvOficFragment);
                fragmentTrasaction.commit();
            } else {
                android.support.v4.app.FragmentTransaction fragmentTrasaction =
                        getSupportFragmentManager().beginTransaction();
                fragmentTrasaction.replace(R.id.content_frame, nvOficFragment);
                fragmentTrasaction.commit();
            }

        } else if (id == R.id.nav_manage) {
            if (!fragment.isAdded()) {
                android.support.v4.app.FragmentTransaction fragmentTrasaction =
                        getSupportFragmentManager().beginTransaction();
                fragmentTrasaction.replace(R.id.content_frame, fragment);
                fragmentTrasaction.commit();
            } else {
                android.support.v4.app.FragmentTransaction fragmentTrasaction =
                        getSupportFragmentManager().beginTransaction();
                fragmentTrasaction.replace(R.id.content_frame, fragment);
                fragmentTrasaction.commit();
            }

        } else if (id == R.id.nav_send) {
        }
        else if (id == R.id.nav_sair) {
            ExitDialog dialog = new ExitDialog();
            dialog.show(getFragmentManager(), "exitDialog");
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.setInfoWindowAdapter(this);
        sendRequest();



        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

        LatLng teste1 = new LatLng(-3.141232, -58.441304);
        //mMap.addMarker(new MarkerOptions().position(teste1).title("Oficina 1").snippet("Em fase de teste de marcadores no google maps"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(teste1, 15));


        Marker marcadorMinhaCasa = mMap.addMarker(new MarkerOptions().position(teste1));

        marcadorMinhaCasa.setTitle("Minha Casa");
        marcadorMinhaCasa.setSnippet("Esta é minha residência...");
        marcadorMinhaCasa.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.end_green));


        CameraPosition updateMyHome = new CameraPosition(teste1, 15, 0, 0);
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(updateMyHome), 3000, null);


        LatLng teste2 = new LatLng(-3.135044, -58.440930);
        mMap.addMarker(new MarkerOptions().position(teste2).title("Oficina 2").snippet("Em fase de teste de marcadores no google maps"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(teste2, 15));

        LatLng teste3 = new LatLng(-3.138394, -58.434432);
        mMap.addMarker(new MarkerOptions().position(teste3).title("Oficina 3").snippet("Em fase de teste de marcadores no google maps"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(teste3, 15));


        LatLng teste4 = new LatLng(-3.145401, -58.433984);
        mMap.addMarker(new MarkerOptions().position(teste4).title("Oficina 4").snippet("Em fase de teste de marcadores no google maps"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(teste4, 15));

        mMap = googleMap;
        LatLng teste5 = new LatLng(-3.129183, -58.430399);
        mMap.addMarker(new MarkerOptions().position(teste5).title("Oficina 5").snippet("Em fase de teste de marcadores no google maps"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(teste5, 15));


        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
    }

    @Override
    public void onExit() {
        finish();
    }

    private void sendRequest() {
        String origin = "Itacoatiara";
        String destination = "Manaus";
        if (origin.isEmpty()) {
            Toast.makeText(this, "Entre com endereço de origem", Toast.LENGTH_SHORT).show();
            return;
        }
        if (destination.isEmpty()) {
            Toast.makeText(this, "Entre com o endereço de destino!", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            new DirectionFinder(this, origin, destination).execute();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }




    @Override
    public void onDirectionFinderStart() {
        progressDialog = ProgressDialog.show(this, "Por favor, aguarde.",
                "Pesquisando a direção..!", true);

        if (originMarkers != null) {
            for (Marker marker : originMarkers) {
                marker.remove();
            }
        }

        if (destinationMarkers != null) {
            for (Marker marker : destinationMarkers) {
                marker.remove();
            }
        }

        if (polylinePaths != null) {
            for (Polyline polyline:polylinePaths ) {
                polyline.remove();
            }
        }
    }

    @Override
    public void onDirectionFinderSuccess(List<Route> route) {

        String dist,tempo;
        progressDialog.dismiss();
        polylinePaths = new ArrayList<>();
        originMarkers = new ArrayList<>();
        destinationMarkers = new ArrayList<>();

        for (Route routes : route) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(routes.startLocation, 16));

            dist = routes.distance.text;
            tempo = routes.duration.text;
            Toast.makeText(this, "Distância: "+dist, Toast.LENGTH_LONG).show();
            Toast.makeText(this,"Tempo: "+ tempo, Toast.LENGTH_LONG).show();
            originMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.start_blue))
                    .title(routes.startAddress)
                    .position(routes.startLocation)));
            destinationMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.end_green))
                    .title(routes.endAddress)
                    .position(routes.endLocation)));

            PolylineOptions polylineOptions = new PolylineOptions().
                    geodesic(true).
                    color(Color.BLUE).
                    width(10);

            for (int i = 0; i < routes.points.size(); i++)
                polylineOptions.add(routes.points.get(i));

            polylinePaths.add(mMap.addPolyline(polylineOptions));
        }

    }


    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        //Inflamos o nosso arquivo de layout em uma view
        markerInfo = getLayoutInflater().inflate(R.layout.infowindow_map_custom, null);

        //Configuramos o tamanho de nossa view
        markerInfo.setLayoutParams(new RelativeLayout.LayoutParams(400, RelativeLayout.LayoutParams.WRAP_CONTENT));

        //Vinculamos objetos com os componentes de tela em nossa view
        TextView titulo = (TextView) markerInfo.findViewById(R.id.infowindow_textview_titulo);
        TextView descricao = (TextView) markerInfo.findViewById(R.id.infowindow_textview_descricao);

        //Recuperamos os valores de nosso marcador e o colocamos em nossa view
        titulo.setText(marker.getTitle());
        descricao.setText(marker.getSnippet());

        //Retornamos para a tela a nossa view personalizada
        return markerInfo;
    }
}
