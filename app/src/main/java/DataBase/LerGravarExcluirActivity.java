package DataBase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.example.firebasecurso2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import Util.Util;
import Util.DialogProgress;
import Util.DialogAlerta;

public class LerGravarExcluirActivity extends AppCompatActivity  implements View.OnClickListener{

    private EditText Pasta,Nome,Idade;
    private Button Remover,Alterar,Salvar;
    private FirebaseDatabase database;
    private DialogProgress dialogProgress;
    private DialogAlerta dialogAlerta;



    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ler_gravar_excluir);

        Pasta = findViewById(R.id.editText_Database_GravarAlterarRemover_NomePasta);
       Nome = findViewById(R.id.editText_Database_GravarAlterarRemover_Nome);
       Idade = findViewById(R.id.editText_Database_GravarAlterarRemover_Idade);
        Remover = findViewById(R.id.button_Database_GravarAlterarRemover_Remover);
        Alterar = findViewById(R.id.button_Database_GravarAlterarRemover_Alterar);
        Salvar = findViewById(R.id.button_Database_GravarAlterarRemover_Salvar);

        Salvar.setOnClickListener(this);
        Alterar.setOnClickListener(this);
        Remover.setOnClickListener(this);

        database = FirebaseDatabase.getInstance();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case  R.id.button_Database_GravarAlterarRemover_Salvar:
                ButtonSalvar();

                break;
            case  R.id.button_Database_GravarAlterarRemover_Remover:
                ButtonExcluir();
                break;
            case  R.id.button_Database_GravarAlterarRemover_Alterar:
                ButtonAlterar();
                break;
        }
    }

    //___________________________________________________________________________SALVAR_______________________________________________________________

    private void ButtonSalvar(){

            if(Util.verificar_campos(getBaseContext(),Nome.getText().toString(),Idade.getText().toString())){

                SalvarDados();
            }else{
                dialogAlerta = new DialogAlerta("Error","Preencha os campos ");
                dialogAlerta.show(getSupportFragmentManager(),"1");

            }




    }

    private  void SalvarDados(){

        dialogProgress = new DialogProgress();
        dialogProgress.show(getSupportFragmentManager(),"1");

        DatabaseReference reference = database.getReference().child("BD").child("Gerentes");
        Gerente gerente = new Gerente(Nome.getText().toString(),Integer.parseInt(Idade.getText().toString()),false);

        reference.push().setValue(gerente).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getBaseContext(),"Salvo Com sucesso",Toast.LENGTH_LONG).show();
                    dialogProgress.dismiss();
                }else{

                    Toast.makeText(getBaseContext(),"Erro ao gravar dados",Toast.LENGTH_LONG).show();
                    dialogProgress.dismiss();
                }
            }
        });


    }
//_____________________________________________________________________________________________________ALTERAR__________________________________________________________
    private void ButtonAlterar(){
        if(Util.statusInternet_MoWi(getBaseContext())){
            if(!Pasta.getText().toString().isEmpty()){

                AlterarDados();
            }else{
                dialogAlerta = new DialogAlerta("Error","Preencha o campo pasta ");
                dialogAlerta.show(getSupportFragmentManager(),"1");

            }
        }



    }

    private void AlterarDados(){
        dialogProgress = new DialogProgress();
        dialogProgress.show(getSupportFragmentManager(),"1");


            DatabaseReference reference = database.getReference().child("BD").child("Gerentes");
            Gerente gerente = new Gerente(Nome.getText().toString(),Integer.parseInt(Idade.getText().toString()),false);


            Map<String,Object> atualizar = new HashMap<>();

            atualizar.put(Pasta.getText().toString(),gerente);

            reference.updateChildren(atualizar).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(getBaseContext(),"Alterado Com sucesso",Toast.LENGTH_LONG).show();
                        dialogProgress.dismiss();
                    }else{
                        Toast.makeText(getBaseContext(),"Erro ao Alterar os dados",Toast.LENGTH_LONG).show();
                        dialogProgress.dismiss();
                    }
                }
            });






    }
//_______________________________________________________________________________________________________REMOVER DADOS____________________________________________________


    private void ButtonExcluir(){
        if(Util.statusInternet_MoWi(getBaseContext())){
            if(!Pasta.getText().toString().isEmpty())
            {
                RemoverDados();
            }else {
                dialogAlerta = new DialogAlerta("Error","Preencha o campo pasta ");
                dialogAlerta.show(getSupportFragmentManager(),"1");
            }
        }




    }
    private void RemoverDados(){

    if(!Pasta.getText().toString().isEmpty()){

        dialogProgress = new DialogProgress();
        dialogProgress.show(getSupportFragmentManager(),"1");

        DatabaseReference reference = database.getReference().child("BD").child("Gerentes");

        reference.child(Pasta.getText().toString()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getBaseContext(),"Excluido Com sucesso",Toast.LENGTH_LONG).show();
                    dialogProgress.dismiss();
                }else{
                    Toast.makeText(getBaseContext(),"Erro ao Excluir os dados",Toast.LENGTH_LONG).show();
                    dialogProgress.dismiss();
                }
            }
        });
    }else {
        Toast.makeText(getBaseContext(),"Preencha o campo pasta",Toast.LENGTH_LONG).show();
    }







    }
}
