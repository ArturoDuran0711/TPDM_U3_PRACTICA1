package mx.edu.ittepic.tpdm_u3_practica1_restaurante;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class Main7Activity extends AppCompatActivity {

    Button actualizar,cancelar;
    EditText nombremp,costomp;
    DatabaseReference basedatos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main7);

        actualizar = findViewById(R.id.actualizar);
        cancelar = findViewById(R.id.cancelar);
        nombremp = findViewById(R.id.nombre);
        costomp = findViewById(R.id.costo);

        Bundle datos = getIntent().getExtras();
        final String id = datos.getString("id");
        final String nombre = datos.getString("nombre");
        final String precio = datos.getString("precio");
        final String producto = datos.getString("producto");

        basedatos = FirebaseDatabase.getInstance().getReference();

        nombremp.setText(nombre);
        costomp.setText(precio);

        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Map<String,Object> data =new HashMap<>();
                data.put("nombre",nombremp.getText().toString());
                data.put("precio",costomp.getText().toString());

                basedatos.child("platillo").child(id).setValue(data);

                Toast.makeText(Main7Activity.this,"Se actualizo "+producto,Toast.LENGTH_LONG).show();
                finish();
            }
        });


    }
}
