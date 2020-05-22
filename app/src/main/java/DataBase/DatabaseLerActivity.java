package DataBase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.firebasecurso2.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import Util.DialogAlerta;

public class DatabaseLerActivity extends AppCompatActivity {

    private TextView textView_Nome;
    private TextView textView_Idade;
    private TextView textView_Fumante;

    private TextView textView_Nome_2;
    private TextView textView_Idade_2;
    private TextView textView_Fumante_2;

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private ValueEventListener valueEventListener;
    private ChildEventListener childEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_ler);

        textView_Nome = (TextView)findViewById(R.id.Database_Nome);
        textView_Idade = (TextView)findViewById(R.id.Database_idade);
        textView_Fumante = (TextView)findViewById(R.id.Database_Fumante);


        textView_Nome_2 =(TextView)findViewById(R.id.Database_Nome2);
        textView_Idade_2 = (TextView)findViewById(R.id.Database_idade2);
        textView_Fumante_2 = (TextView)findViewById(R.id.Database_Fumante2);


        database = FirebaseDatabase.getInstance();



    }



    private void ouvinte_1() {


        DatabaseReference reference = database.getReference().child("BD").child("Gerentes");


        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                List<Gerente> gerentes = new ArrayList<>();

                for(DataSnapshot data: dataSnapshot.getChildren()){


                    Gerente gerente = data.getValue(Gerente.class);

                    gerentes.add(gerente);

                }

                textView_Nome.setText(gerentes.get(0).getNome());
                textView_Idade.setText(gerentes.get(0).getIdade()+"");
                textView_Fumante.setText(gerentes.get(0).isFumante()+"");

                textView_Nome_2.setText(gerentes.get(1).getNome());
                textView_Idade_2.setText(gerentes.get(1).getIdade()+"");
                textView_Fumante_2.setText(gerentes.get(1).isFumante()+"");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void ouvinte_2() {


        databaseReference = database.getReference().child("BD").child("Gerentes");

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                List<Gerente> gerentes = new ArrayList<>();

                for(DataSnapshot data: dataSnapshot.getChildren()){


                    Gerente gerente = data.getValue(Gerente.class);

                    gerentes.add(gerente);

                }

                textView_Nome.setText(gerentes.get(0).getNome());
                textView_Idade.setText(gerentes.get(0).getIdade()+"");
                textView_Fumante.setText(gerentes.get(0).isFumante()+"");

                textView_Nome_2.setText(gerentes.get(1).getNome());
                textView_Idade_2.setText(gerentes.get(1).getIdade()+"");
                textView_Fumante_2.setText(gerentes.get(1).isFumante()+"");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        databaseReference.addValueEventListener(valueEventListener);


    }

    private void ouvinte_3(){

        databaseReference = database.getReference().child("BD").child("Gerentes");


        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {


                String chave = dataSnapshot.getKey();

                Gerente gerente = dataSnapshot.getValue(Gerente.class);

                Log.i("testouviente_3_Add",gerente.getNome()+"--"+chave);


            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {


                String chave = dataSnapshot.getKey();

                Log.i("testouviente_3_Change","--"+chave);

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                String chave = dataSnapshot.getKey();

                Log.i("testouviente_3_Remove","--"+chave);

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };



        databaseReference.addChildEventListener(childEventListener);



    }

    @Override
    protected void onStart() {
        super.onStart();
        ouvinte_2();

    }

    @Override
    protected void onStop() {
        super.onStop();

        if(valueEventListener != null){
            databaseReference.removeEventListener(valueEventListener);
        }

        if(childEventListener != null){
            databaseReference.removeEventListener(valueEventListener);

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(valueEventListener != null){
            databaseReference.removeEventListener(valueEventListener);
        }

        if(childEventListener != null){
            databaseReference.removeEventListener(valueEventListener);

        }
    }
}



