package mx.edu.ittepic.tpdm_u3_practica1_restaurante;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    ListView opciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        opciones = findViewById(R.id.opciones);

        opciones.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        Intent m = new Intent(MainActivity.this, Main4Activity.class);
                        startActivity(m);
                        break;
                    case 1:
                        Intent p = new Intent(MainActivity.this, Main5Activity.class);
                        startActivity(p);
                        break;
                    case 2:
                        Toast.makeText(MainActivity.this,"Se exportaran los datos",Toast.LENGTH_LONG).show();
                        break;
                    case 3:
                        AlertDialog.Builder alerta = new AlertDialog.Builder(MainActivity.this);
                        alerta.setTitle("SALIR")
                                .setMessage("Estas seguro que deseas salir?")
                                .setPositiveButton("si", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        finish();
                                    }
                                })
                                .setNegativeButton("no",null)
                                .show();

                        break;
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.platillos) {
            Intent i = new Intent(MainActivity.this, Main2Activity.class);
            startActivity(i);
            return true;
        }
        if (id == R.id.bebidas) {
            Intent i = new Intent(MainActivity.this, Main3Activity.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
