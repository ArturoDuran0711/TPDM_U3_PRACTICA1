package mx.edu.ittepic.tpdm_u3_practica1_restaurante;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Main2Activity extends AppCompatActivity {

    EditText nombreP,precioP;
    Button agregar,cancelar;
    ListView lista;
    int id;

    List<Map> platilloslista;
    DatabaseReference basedatos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        cancelar = findViewById(R.id.cancelar);
        nombreP = findViewById(R.id.nombrePlatillo);
        precioP = findViewById(R.id.costoPlatillo);
        agregar = findViewById(R.id.agregar);
        lista = findViewById(R.id.lista);

        basedatos = FirebaseDatabase.getInstance().getReference();

        agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String,Object> data =new HashMap<>();

                data.put("nombre",nombreP.getText().toString());
                data.put("precio",precioP.getText().toString());

                Random aleatorio = new Random();
                id= aleatorio.nextInt(99999999);
                basedatos.child("platillo").child(""+(id+1)).setValue(data);
                Toast.makeText(Main2Activity.this,"Se inserto correctamente",Toast.LENGTH_LONG).show();

                nombreP.setText("");
                precioP.setText("");
            }
        });

        basedatos.child("platillo").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount()<=0){
                    Toast.makeText(Main2Activity.this, "NO HAY DATOS A MOSTRAR", Toast.LENGTH_LONG).show();
                    return;
                }
                platilloslista=new ArrayList<>();
                for(final DataSnapshot otro:dataSnapshot.getChildren()){
                    basedatos.child("platillo").child(otro.getKey()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Platillo plat = dataSnapshot.getValue(Platillo.class);
                            if(plat!=null){
                                Map<String,Object> xx = new HashMap<>();
                                xx.put("idP",otro.getKey());
                                xx.put("nombre",plat.getNombre());
                                xx.put("precio",plat.getPrecio());
                                platilloslista.add(xx);
                                cargarlista();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(platilloslista.size()<=0){
                    return;
                }
                AlertDialog.Builder alerta = new AlertDialog.Builder(Main2Activity.this);
                final Map<String,Object> datos = platilloslista.get(i);
                final String id=(datos.get("idP")).toString();
                final String nombre=(datos.get("nombre")).toString();
                final String precio=(datos.get("precio")).toString();
                final String producto = "platillo";
                //prueba
                //Toast.makeText(Main2Activity.this,producto,Toast.LENGTH_LONG).show();
                alerta.setTitle("ATENCION").setMessage("¿Quieres editar/borrar a "+datos.get("nombre"))
                        .setPositiveButton("EDITAR", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent mp = new Intent(Main2Activity.this, Main7Activity.class);
                                mp.putExtra("id",id);
                                mp.putExtra("nombre",nombre);
                                mp.putExtra("precio",precio);
                                mp.putExtra("producto",producto);
                                startActivity(mp);
                            }
                        })
                        .setNegativeButton("BORRAR", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                basedatos.child("platillo").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for(final DataSnapshot clave:dataSnapshot.getChildren()){
                                            basedatos.child("platillo").child(id).removeValue();
                                            Toast.makeText(Main2Activity.this,"Platillo eliminado",Toast.LENGTH_LONG).show();
                                        }
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }
                        })
                        .show();
            }
        });

        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void cargarlista(){
        String[] vector = new String[platilloslista.size()];

        for(int i=0; i<vector.length; i++){
            Map<String,Object> ww = new HashMap<>();

            ww = platilloslista.get(i);
            vector[i] = ww.get("nombre").toString()+" $"+ww.get("precio").toString();
        }

        ArrayAdapter<String> arr = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,vector);
        lista.setAdapter(arr);
    }
}
