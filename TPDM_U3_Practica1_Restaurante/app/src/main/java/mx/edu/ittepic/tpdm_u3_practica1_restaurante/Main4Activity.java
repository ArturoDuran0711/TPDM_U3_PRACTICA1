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
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class Main4Activity extends AppCompatActivity {
    EditText noMesa,cantidadP,cantidadB;
    Spinner spiPlatillo,spiBebida;
    Button agregarP,agregarB,guardar,cancelar;
    ListView listaorden;


    DatabaseReference basedatos;
    List<Map> platilloslista;
    List<Map> bebidaslista;
    List<Map> comanda;



    int id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        noMesa = findViewById(R.id.noMesa);
        cantidadP = findViewById(R.id.cantidadPlatillo);
        cantidadB = findViewById(R.id.cantidadbebidas);
        spiPlatillo = findViewById(R.id.spiplatillos);
        spiBebida = findViewById(R.id.spibebidas);
        agregarP = findViewById(R.id.agregarplatillo);
        agregarB = findViewById(R.id.agregarbebidas);
        listaorden = findViewById(R.id.orden);
        cancelar = findViewById(R.id.cancelar);
        guardar = findViewById(R.id.guardar);

        basedatos = FirebaseDatabase.getInstance().getReference();

        basedatos.child("platillo").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() <= 0) {
                    Toast.makeText(Main4Activity.this, "NO HAY DATOS A MOSTRAR", Toast.LENGTH_LONG).show();
                    return;
                }
                platilloslista = new ArrayList<>();
                for (final DataSnapshot otro : dataSnapshot.getChildren()) {
                    basedatos.child("platillo").child(otro.getKey()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Platillo plat = dataSnapshot.getValue(Platillo.class);
                            if (plat != null) {
                                Map<String, Object> xx = new HashMap<>();
                                xx.put("idP", otro.getKey());
                                xx.put("nombre", plat.getNombre());
                                xx.put("precio", plat.getPrecio());
                                platilloslista.add(xx);
                                cargarplatillos();
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

        basedatos.child("bebida").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() <= 0) {
                    Toast.makeText(Main4Activity.this, "NO HAY DATOS A MOSTRAR", Toast.LENGTH_LONG).show();
                    return;
                }
                bebidaslista = new ArrayList<>();
                for (final DataSnapshot otro : dataSnapshot.getChildren()) {
                    basedatos.child("bebida").child(otro.getKey()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Bebida beb = dataSnapshot.getValue(Bebida.class);
                            if (beb != null) {
                                Map<String, Object> xx = new HashMap<>();
                                xx.put("idP", otro.getKey());
                                xx.put("nombre", beb.getNombre());
                                xx.put("precio", beb.getPrecio());
                                bebidaslista.add(xx);
                                cargarbebidas();
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

        comanda=new ArrayList<>();
        agregarP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(cantidadP.getText().toString().isEmpty()){
                    Toast.makeText(Main4Activity.this,"Ingresa cantidad de platillos",Toast.LENGTH_LONG).show();
                    return;
                }

                String seleccionP = spiPlatillo.getSelectedItem().toString();
                String cantidad = cantidadP.getText().toString();
                //Toast.makeText(Main4Activity.this,"cantiad: "+cantidad+" producto: "+seleccionP,Toast.LENGTH_LONG).show();

                Map<String,Object> xx = new HashMap<>();
                xx.put("nombre",seleccionP);
                xx.put("cantidad",cantidad);
                xx.put("tipoProducto","platillo");
                comanda.add(xx);
                cargarcomanda();
            }
        });


        agregarB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(cantidadB.getText().toString().isEmpty()){
                    Toast.makeText(Main4Activity.this,"Ingresa cantidad de bebidas",Toast.LENGTH_LONG).show();
                    return;
                }

                String seleccionB = spiBebida.getSelectedItem().toString();
                String cantidad = cantidadB.getText().toString();
                //Toast.makeText(Main4Activity.this,"cantiad: "+cantidad+" producto: "+seleccionB,Toast.LENGTH_LONG).show();

                Map<String,Object> xx = new HashMap<>();
                xx.put("nombre",seleccionB);
                xx.put("cantidad",cantidad);
                xx.put("tipoProducto","bebida");
                comanda.add(xx);
                cargarcomanda();

            }
        });


        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(noMesa.getText().toString().isEmpty()){
                    Toast.makeText(Main4Activity.this,"Ingresa numero de mesa",Toast.LENGTH_LONG).show();
                    return;
                }

                if(comanda.size()<=0){
                    Toast.makeText(Main4Activity.this,"Ingresa bebidas o platillos",Toast.LENGTH_LONG).show();
                    return;
                }

                basedatos.child("comanda").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getChildrenCount() <= 0) {
                            Toast.makeText(Main4Activity.this, "NO HAY DATOS A MOSTRAR", Toast.LENGTH_LONG).show();
                            return;
                        }
                        comanda = new ArrayList<>();
                        for (final DataSnapshot otro : dataSnapshot.getChildren()) {
                            basedatos.child("comanda").child(otro.getKey()).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    Comanda com = dataSnapshot.getValue(Comanda.class);
                                    if (com.getEtatus() =="deuda") {
                                        Toast.makeText(Main4Activity.this,"Mesa "+com.getEtatus()+" no disponible, intenta otra",Toast.LENGTH_LONG).show();
                                        return;
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






                Map<String,Object> data =new HashMap<>();
                String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

                data.put("nomesa",noMesa.getText().toString());
                data.put("estatus","deuda");
                data.put("fecha",date);

                Random aleatorio = new Random();
                id= aleatorio.nextInt(99999999);
                basedatos.child("comanda").child(""+(id+1)).setValue(data);

                String[] vector = new String[comanda.size()];


                for(int i=0; i<vector.length; i++){
                    Map<String,Object> ww = new HashMap<>();

                    ww = comanda.get(i);

                    if(ww.get("tipoProducto").toString()=="platillo"){
                        basedatos.child("comanda").child(""+(id+1)).child("platillo").child(ww.get("nombre").toString()).setValue(ww.get("cantidad").toString());
                    }

                    if(ww.get("tipoProducto").toString()=="bebida"){
                        basedatos.child("comanda").child(""+(id+1)).child("bebida").child(ww.get("nombre").toString()).setValue(ww.get("cantidad").toString());
                    }

                }

                Toast.makeText(Main4Activity.this,"Se inserto correctamente",Toast.LENGTH_LONG).show();

                noMesa.setText("");
                cantidadB.setText("");
                cantidadP.setText("");
                comanda.clear();
                cargarcomanda();
            }
        });

        listaorden.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(comanda.size()<=0){
                    return;
                }
                AlertDialog.Builder alerta = new AlertDialog.Builder(Main4Activity.this);
                final Map<String,Object> datos = comanda.get(i);
                final int pos=i;

                alerta.setTitle("ATENCION").setMessage("¿Quieres borrar a "+datos.get("nombre")+" de la comanda?")
                        .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                comanda.remove(pos);
                                cargarcomanda();
                                Toast.makeText(Main4Activity.this,"se elimino de comanda",Toast.LENGTH_LONG).show();
                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                               dialogInterface.cancel();
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

    private  void cargarplatillos(){
        String[] vector = new String[platilloslista.size()];

        for(int i=0; i<vector.length; i++){
            Map<String,Object> ww = new HashMap<>();

            ww = platilloslista.get(i);
            vector[i] = ww.get("nombre").toString();
        }

        ArrayAdapter<String> arr = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,vector);
        spiPlatillo.setAdapter(arr);
    }

    private void cargarbebidas(){
        String[] vector = new String[bebidaslista.size()];

        for(int i=0; i<vector.length; i++){
            Map<String,Object> ww = new HashMap<>();

            ww = bebidaslista.get(i);
            vector[i] = ww.get("nombre").toString();
        }

        ArrayAdapter<String> arr = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,vector);
        spiBebida.setAdapter(arr);
    }

    private void cargarcomanda(){
        String[] vector = new String[comanda.size()];

        for(int i=0; i<vector.length; i++){
            Map<String,Object> ww = new HashMap<>();

            ww = comanda.get(i);
            vector[i] = ww.get("cantidad").toString()+" "+ww.get("nombre").toString();
        }

        ArrayAdapter<String> arr = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,vector);
        listaorden.setAdapter(arr);
        arr.notifyDataSetChanged();
    }
}
