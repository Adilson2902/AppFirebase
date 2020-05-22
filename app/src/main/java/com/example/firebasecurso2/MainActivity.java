package com.example.firebasecurso2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import DataBase.DatabaseLerActivity;
import DataBase.LerGravarExcluirActivity;
import Util.Permissao;
import storage.StorageDownloadActivity;
import storage.StorageUploadActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private CardView CardView_Download,Cardview_upload,CardView_DatabaseLerdados,CardView_DatabaseGravar,CardViewEmpresas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CardView_Download = findViewById(R.id.Cardview_StorageDownload);
        Cardview_upload = findViewById(R.id.Cardview_StorageUpload);
        CardView_DatabaseLerdados = findViewById(R.id.Cardview_DatabaseLerdados);
        CardView_DatabaseGravar = findViewById(R.id.Cardview_GravarAlterarExcluir);
        CardViewEmpresas = findViewById(R.id.Cardview_Empresas);


        CardView_Download.setOnClickListener(this);
        Cardview_upload.setOnClickListener(this);
        CardView_DatabaseLerdados.setOnClickListener(this);
        CardView_DatabaseGravar.setOnClickListener(this);
        CardViewEmpresas.setOnClickListener(this);

        permissao();


    }
//______________________________________ TRATAMENTO DE CLICKS___________________
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case  R.id.Cardview_StorageDownload:
                startActivity(new Intent(getBaseContext(),StorageDownloadActivity.class));
                break;
            case  R.id.Cardview_StorageUpload:
                startActivity(new Intent(getBaseContext(), StorageUploadActivity.class));
                break;
            case  R.id.Cardview_DatabaseLerdados:
                startActivity(new Intent(getBaseContext(), DatabaseLerActivity.class));
                break;
            case  R.id.Cardview_GravarAlterarExcluir:
                startActivity(new Intent(getBaseContext(), LerGravarExcluirActivity.class));
                break;
            case  R.id.Cardview_Empresas:

                break;

        }

    }


    // _________________________________________________Permissao do usuario______________________________________________

    private void permissao(){

        String permissoes[] = new String []{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
        };

        Permissao.permissao(this,0,permissoes);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int result : grantResults){

            if(result == PackageManager.PERMISSION_DENIED){
                Toast.makeText(getBaseContext(),"Aceite as permissoes para o app",Toast.LENGTH_LONG).show();
                finish();
            }

        }
    }
}
