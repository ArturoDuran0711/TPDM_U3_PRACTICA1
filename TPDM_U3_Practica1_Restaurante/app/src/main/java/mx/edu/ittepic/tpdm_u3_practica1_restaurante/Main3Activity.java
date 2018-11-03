package mx.edu.ittepic.tpdm_u3_practica1_restaurante;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Main3Activity extends AppCompatActivity {

    EditText nombreB,precioB;
    Button agregar,cancelar;
    ListView listaB;
    int id;

    List<Map> bebidaslista;
    DatabaseReference basedatos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        cancelar = findViewById(R.id.cancelar);
        nombreB = findViewById(R.id.nombreBebida);
        precioB = findViewById(R.id.costoBebida);
        agregar = findViewById(R.id.agregar);
        listaB = findViewById(R.id.lista);

        basedatos = FirebaseDatabase.getInstance().getReference();

        agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String,Object> data =new HashMap<>();

                data.put("nombre",nombreB.getText().toString());
                data.put("precio",precioB.getText().toString());

                Random aleatorio = new Random();
                id= aleatorio.nextInt(99999999);
                basedatos.child("bebida").child(""+(id+1)).setValue(data);

                Toast.makeText(Main3Activity.this,"Se inserto correctamente",Toast.LENGTH_LONG).show();

                nombreB.setText("");
                precioB.setText("");
            }
        });

        basedatos.child("bebida").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount()<=0){
                    Toast.makeText(Main3Activity.this,"NO HAY DATOS A MOSTRAR",Toast.LENGTH_LONG).show();
                    return;
                }
                bebidaslista = new ArrayList<>();
                for(final DataSnapshot otrob:dataSnapshot.getChildren()){
                    basedatos.child("bebida").child(otrob.getKey()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Bebida beb = dataSnapshot.getValue(Bebida.class);
                            if(beb!=null){
                                Map<String,Object> bb = new HashMap<>();
                                bb.put("idB",otrob.getKey());
                                bb.put("nombre",beb.getNombre());
                                bb.put("precio",beb.getPrecio());
                                bebidaslista.add(bb);
                                cargarlistabebida();
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

        listaB.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(bebidaslista.size()<=0){
                    return;
                }
                AlertDialog.Builder alerta = new AlertDialog.Builder(Main3Activity.this);
                final Map<String,Object> datos = bebidaslista.get(i);
                final String id=(datos.get("idB")).toString();
                final String nombre=(datos.get("nombre")).toString();
                final String precio=(datos.get("precio")).toString();
                final String producto = "bebida";

                alerta.setTitle("ATENCION").setMessage("¿Quieres editar/borrar a "+datos.get("nombre"))
                        .setPositiveButton("EDITAR", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent mp = new Intent(Main3Activity.this, Main8Activity.class);
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
                                basedatos.child("bebida").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for(final DataSnapshot clave:dataSnapshot.getChildren()){
                                            basedatos.child("bebida").child(id).removeValue();
                                            Toast.makeText(Main3Activity.this,"Bebida eliminada",Toast.LENGTH_LONG).show();
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

    private void cargarlistabebida(){
        String[] vectorbebida = new String[bebidaslista.size()];

        for(int i=0; i<vectorbebida.length; i++){
            Map<String,Object> ww = new HashMap<>();

            ww = bebidaslista.get(i);
            vectorbebida[i] = ww.get("nombre").toString()+" $"+ww.get("precio").toString();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,vectorbebida);
        listaB.setAdapter(adapter);
    }
}
