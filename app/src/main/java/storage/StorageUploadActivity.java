package storage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.firebasecurso2.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import Util.DialogAlerta;
import Util.DialogProgress;
import Util.Permissao;
import Util.Util;

public class StorageUploadActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView imagem_upload;
    private Button Button_upload;
    private Uri uri_imagem;
    private FirebaseStorage storage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage_upload);

        imagem_upload = findViewById(R.id.Imagem_Upload);
        Button_upload = findViewById(R.id.Butto_Upload);

        Button_upload.setOnClickListener(this);

            storage = FirebaseStorage.getInstance();
        permissao();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.Butto_Upload:
             button_Upload();
                break;
        }

    }


    private void button_Upload(){


        if(Util.statusInternet_MoWi(getBaseContext())){

            if(imagem_upload.getDrawable() != null){

                // upload_Imagem_1();

                upload_Imagem_2();

            }else{
                Toast.makeText(getBaseContext(),"Não existe imagem ainda para realizar o upload",Toast.LENGTH_LONG).show();


            }


        }else{

            Toast.makeText(getBaseContext(),"Erro de Conexão - verifique se o seu Wiffi ou 3G está funcionando",Toast.LENGTH_LONG).show();
        }




    }


    // ___________________________________________________________________ MENU________________________________________________________________

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
      getMenuInflater().inflate(R.menu.menu_storage_upload,menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_galeria:
                obterImagem_Galeria();
                break;
            case R.id.item_camera:
               item_camera();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void item_camera(){


        if(ContextCompat.checkSelfPermission(getBaseContext(),Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){

            DialogAlerta dialogAlerta = new DialogAlerta("Permissão Necessária","Acesse as configurações do aplicativo" +
                    "para poder utilizar a camera no nosso aplicativo.");

            dialogAlerta.show(getSupportFragmentManager(),"1");


        }else{

            obterImagem_Camera();
        }



    }



    //---------------------------------------- OBTER IMAGENS ----------------------------------------------------------------


    private void obterImagem_Galeria(){


        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);


        startActivityForResult(Intent.createChooser(intent,"Escolha uma Imagem"),0);

    }



    private void obterImagem_Camera(){



        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.Images.Media.MIME_TYPE,"image/jpeg");
            ContentResolver contentResolver = getContentResolver();

            uri_imagem = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues);

            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION|Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            intent.putExtra(MediaStore.EXTRA_OUTPUT,uri_imagem);

            startActivityForResult(intent,1);

        }else{

            File diretorio = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

            String nomeImagem = diretorio.getPath()+"/"+"CursoImagem"+System.currentTimeMillis()+".jpg";

            File file = new File(nomeImagem);


            String autorizacao = "com.example.firebasecurso2.fileprovider";

            uri_imagem = FileProvider.getUriForFile(getBaseContext(),autorizacao,file);


            intent.putExtra(MediaStore.EXTRA_OUTPUT,uri_imagem);

            startActivityForResult(intent,1);


        }




    }


    //----------------------------------------UPLOAD DE IMAGENS----------------------------------------------------------------


    private void upload_Imagem_1(){


        final DialogProgress dialogProgress = new DialogProgress();

        dialogProgress.show(getSupportFragmentManager(),"");

        StorageReference reference = storage.getReference().child("upload").child("imagens");

        StorageReference nome_imagem = reference.child("CursoFirebaseUpload"+System.currentTimeMillis()+".jpg");



        BitmapDrawable drawable = (BitmapDrawable) imagem_upload.getDrawable();

        Bitmap bitmap = drawable.getBitmap();

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.JPEG,100, bytes);



        UploadTask uploadTask = nome_imagem.putBytes(bytes.toByteArray());


        uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {


            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {


                if (task.isSuccessful()){

                    dialogProgress.dismiss();
                    Toast.makeText(getBaseContext(),"Sucesso ao realizar upload",Toast.LENGTH_LONG).show();
                }else{

                    dialogProgress.dismiss();
                    Toast.makeText(getBaseContext(),"Erro ao realizar upload",Toast.LENGTH_LONG).show();

                }


            }
        });

    }


    private void upload_Imagem_2(){


        final DialogProgress dialogProgress = new DialogProgress();

        dialogProgress.show(getSupportFragmentManager(),"");

        StorageReference reference = storage.getReference().child("upload").child("imagens");

        final StorageReference nome_imagem = reference.child("CursoFirebaseUpload"+System.currentTimeMillis()+".jpg");





        Glide.with(getBaseContext()).asBitmap().load(uri_imagem).apply(new RequestOptions().override(1024,768))
                .listener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {

                        Toast.makeText(getBaseContext(),"Erro ao transformar imagem",Toast.LENGTH_LONG).show();

                        return false;

                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {



                        ByteArrayOutputStream bytes = new ByteArrayOutputStream();

                        resource.compress(Bitmap.CompressFormat.JPEG,70, bytes);

                        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes.toByteArray());


                        try {
                            bytes.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                        UploadTask uploadTask = nome_imagem.putStream(inputStream);



                        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>(){

                            @Override
                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {


                                return nome_imagem.getDownloadUrl();
                            }


                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {


                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {


                                if(task.isSuccessful()){

                                    dialogProgress.dismiss();

                                    Uri uri = task.getResult();

                                    String url_imagem = uri.toString();

                                    DialogAlerta alerta = new DialogAlerta("URL IMAGEM",url_imagem);
                                    alerta.show(getSupportFragmentManager(),"3");


                                    Toast.makeText(getBaseContext(),"Sucesso ao Realizar Upload",Toast.LENGTH_LONG).show();


                                }else{
                                    dialogProgress.dismiss();

                                    Toast.makeText(getBaseContext(),"Erro ao realizar Upload",Toast.LENGTH_LONG).show();
                                }

                            }
                        });



                        return false;
                    }
                }).submit();

    }





    //---------------------------------------- REPOSTAS DE COMUNICACAO ----------------------------------------------------------------




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);



        if(resultCode == RESULT_OK){


            if(requestCode == 0){ // RESPOSTA DA GALERIA

                if (data != null){ // CONTEUDO DA ESCOLHA DA IMAGEM DA GALERIA

                    uri_imagem = data.getData();

                    Glide.with(getBaseContext()).asBitmap().load(uri_imagem).listener(new RequestListener<Bitmap>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {

                            Toast.makeText(getBaseContext(),"Erro ao carregar imagem",Toast.LENGTH_LONG).show();
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    }).into(imagem_upload);

                }else{

                    Toast.makeText(getBaseContext(),"Falha ao selecionar imagem",Toast.LENGTH_LONG).show();

                }
            }

            else if (requestCode == 1){ //RESPOSTA DA CAMERA


                if(uri_imagem != null){// VERIFICAR RESPOSTA DA CAMERA

                    Glide.with(getBaseContext()).asBitmap().load(uri_imagem).listener(new RequestListener<Bitmap>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {

                            Toast.makeText(getBaseContext(),"Erro ao carregar imagem",Toast.LENGTH_LONG).show();
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    }).into(imagem_upload);


                }else{

                    Toast.makeText(getBaseContext(),"Falha ao Tirar Foto",Toast.LENGTH_LONG).show();

                }
            }






        }





    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        for(int result: grantResults){

            if(result == PackageManager.PERMISSION_DENIED){

                Toast.makeText(this,"Aceite as permissões para o aplicativo acessar sua câmera",Toast.LENGTH_LONG).show();
                finish();

                break;
            }

        }


    }


    private void permissao(){
        String  permissoes[]= new String[]{
            Manifest.permission.CAMERA
        };


        Permissao.permissao(this,0,permissoes);

    }
}
