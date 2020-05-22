package storage;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.firebasecurso2.R;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import Util.DialogAlerta;
import Util.Util;

public class StorageDownloadActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView Image_download;
    private ProgressBar Progress_download;
    private Button Button_download, Button_remover;
    private FirebaseStorage storage;
    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage_download);


        Image_download = findViewById(R.id.ImageView_Download);
        Progress_download = findViewById(R.id.Progress_Download);
        Button_download = findViewById(R.id.Button_Download);
        Button_remover = findViewById(R.id.Button_Remover);


        Button_download.setOnClickListener(this);
        Button_remover.setOnClickListener(this);

        Progress_download.setVisibility(View.GONE);


        storage = FirebaseStorage.getInstance();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case  R.id.Button_Download:
               button_Download();
                break;
            case  R.id.Button_Remover:
              button_Remover();
                break;
        }
    }
//_________________________________________________________________________________________DOWNLOAD IMAGENS________________________________________________________________________________________________________________________________________________
    private  void dowload_image1(){
        Progress_download.setVisibility(View.VISIBLE);


        String url = "https://firebasestorage.googleapis.com/v0/b/fir-project2-6abb1.appspot.com/o/Imagem%2Fjflex1.jpeg?alt=media&token=e3363fae-a126-49f1-887b-7d5111d98ea8";

/*
        Picasso.
                with(getBaseContext()).
                load(url).
                into(imageView, new com.squareup.picasso.Callback() {
            @Override
            public void onSuccess() {

                progressBar.setVisibility(View.GONE);


            }

            @Override
            public void onError() {
                progressBar.setVisibility(View.GONE);


            }
        });

       */


        Glide.
                with(getBaseContext()).
                asBitmap().
                load(url).
                listener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {

                       Progress_download.setVisibility(View.GONE);

                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {

                        Progress_download.setVisibility(View.GONE);

                        return false;
                    }
                }).into(Image_download);
    }

    private void download_imagem_2(){

        Progress_download.setVisibility(View.VISIBLE);


        StorageReference reference = storage.getReference().child("jflex2.jpeg");


        reference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {


            @Override
            public void onComplete(@NonNull Task<Uri> task) {

                if (task.isSuccessful()){

                    String url = task.getResult().toString();

                    Picasso.with(getBaseContext()).load(url).into(Image_download, new Callback() {
                        @Override
                        public void onSuccess() {
                            Toast.makeText(getBaseContext(),"DOWNLOAD REALIZADO COM SUCESSO",Toast.LENGTH_LONG);
                            Progress_download.setVisibility(View.GONE);

                        }

                        @Override
                        public void onError() {
                                Toast.makeText(getBaseContext(),"ERROR",Toast.LENGTH_LONG);
                            Progress_download.setVisibility(View.GONE);

                        }
                    });


                }


            }
        });

    }
//___________________________________________________________________________________________ EXCLUIR IMAGEM____________________________________________________________________________________

    private void remover_imagem_1(){


        StorageReference reference = FirebaseStorage.getInstance().getReferenceFromUrl("https://firebasestorage.googleapis.com/v0/b/fir-project2-6abb1.appspot.com/o/Imagem%2Fjflex1.jpeg?alt=media&token=e3363fae-a126-49f1-887b-7d5111d98ea8");




        reference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {


            @Override
            public void onComplete(@NonNull Task<Void> task) {


                if(task.isSuccessful()){

                    Image_download.setImageDrawable(null);

                    Toast.makeText(getBaseContext(),"Sucesso ao Remover Imagem",Toast.LENGTH_LONG).show();
                }else{


                    Toast.makeText(getBaseContext(),"Erro ao Remover Imagem",Toast.LENGTH_LONG).show();

                }

            }
        });


    }

    private void remover_imagem_2(){

        String nome = "jafapps.png";


        StorageReference reference = storage.getReference().child("imagem").child(nome);


        reference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){

                    Image_download.setImageDrawable(null);

                    Toast.makeText(getBaseContext(),"Sucesso ao Remover Imagem",Toast.LENGTH_LONG).show();
                }else{


                    Toast.makeText(getBaseContext(),"Erro ao Remover Imagem",Toast.LENGTH_LONG).show();

                }

            }
        });

    }

//____________________________________________________ CRIAR MENU_________________________________________________________________


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_storage_download,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){

            case R.id.item_compartilhar:
                item_Compartilhar();
                return  true;

            case R.id.item_criar_pdf:
               item_GerarPDF();
                return  true;
        }


        return super.onOptionsItemSelected(item);



    }


    //-----------------------------------------TRATAMENTO DE ERROS----------------------------------------------------


    private void button_Download(){


        if (Util.statusInternet_MoWi(getBaseContext())){

            // download_imagem_1();
            download_imagem_2();
        }else{


            DialogAlerta alerta = new DialogAlerta("Erro de Conexão","Verifique se sua conexão Wiffi ou 3G está funcionando");
            alerta.show(getSupportFragmentManager(),"1");

        }
    }


    private void button_Remover(){


        if (Util.statusInternet_MoWi(getBaseContext())){

            /// remover_imagem_1();
            remover_imagem_2();
        }else{


            DialogAlerta alerta = new DialogAlerta("Erro de Conexão","Verifique se sua conexão Wiffi ou 3G está funcionando");
            alerta.show(getSupportFragmentManager(),"1");

        }
    }



    //____________________________________________________________________________Gerar PDF______________________________________________________________________________________

    private void gerarPDF() throws FileNotFoundException, DocumentException {

        File diretorio = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);


        String nome_Arquivo = diretorio.getPath()+"/"+"FirebaseCurso"+System.currentTimeMillis()+".pdf";


        File pdf = new File(nome_Arquivo);


        OutputStream outputStream =  new FileOutputStream(pdf);


        Document document = new Document();

        PdfWriter writer = PdfWriter.getInstance(document,outputStream);

        writer.setBoxSize("firebase", new Rectangle(36,54,559,788));


        document.open();

        Font font = new Font(Font.FontFamily.HELVETICA,20,Font.BOLD);

        Paragraph paragraph = new Paragraph("Curso Firebase Módulo II",font);

        paragraph.setAlignment(Element.ALIGN_CENTER);

        Paragraph paragraph_2 = new Paragraph("Teste",font);

        paragraph_2.setAlignment(Element.ALIGN_LEFT);


        ListItem item = new ListItem();

        item.add(paragraph);
        item.add(paragraph_2);



        document.add(item);



        try {
            BitmapDrawable drawable = (BitmapDrawable) Image_download.getDrawable();

            Bitmap bitmap = drawable.getBitmap();

            ByteArrayOutputStream bytes = new ByteArrayOutputStream();

            bitmap.compress(Bitmap.CompressFormat.JPEG,100, bytes);


            Image image = Image.getInstance(bytes.toByteArray());

            image.scaleAbsolute(100f,100f);

            image.setAlignment(Element.ALIGN_CENTER);

            image.setRotationDegrees(10f);
            document.add(image);


        } catch (IOException e) {
            e.printStackTrace();
        }


        document.close();

        visualizarPDF(pdf);
    }


    private void visualizarPDF(File Pdf){

        PackageManager packageManager = getPackageManager();

        Intent itent = new Intent(Intent.ACTION_VIEW);

        itent.setType("application/pdf");

        List<ResolveInfo> lista = packageManager.queryIntentActivities(itent,PackageManager.MATCH_DEFAULT_ONLY);


        if(lista.size() > 0 ){

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            Uri uri = FileProvider.getUriForFile(getBaseContext(),"com.example.mac.firebasecursods",Pdf);

            intent.setDataAndType(uri,"application/pdf");

            startActivity(intent);

        }else{


            DialogAlerta dialogAlerta = new DialogAlerta("Erro ao Abrir PDF","Não foi detectado nenhum leitor de PDF no seu Dispositivo.");
            dialogAlerta.show(getSupportFragmentManager(),"3");


        }

    }

    //-----------------------------------------TRATAMENTO DE ERROS----------------------------------------------------


    private void item_Compartilhar(){


        if( Image_download.getDrawable() != null){

            compartilhar();
        }else{


            DialogAlerta alerta = new DialogAlerta("Erro de Compartilhamento",
                    "Não existe nenhuma imagem para compartilhar");
            alerta.show(getSupportFragmentManager(),"1");

        }
    }


    private void item_GerarPDF(){


        if( Image_download.getDrawable() != null){

            try {
                gerarPDF();
            } catch (DocumentException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }else{


            DialogAlerta alerta = new DialogAlerta("Erro ao Gerar PDF",
                    "Não existe nenhuma imagem para gerar o PDF");
            alerta.show(getSupportFragmentManager(),"1");

        }



    }



    //____________________________________________________________________________ COMPARTILHAR IMAGEM______________________________________________________________________________________

    private void compartilhar(){


        if(Image_download.getDrawable() != null){
          Intent intent = new Intent(Intent.ACTION_SEND);

          intent.setType("image/jpeg");

            BitmapDrawable drawable = (BitmapDrawable) Image_download.getDrawable();
            Bitmap bitmapDrawable = drawable.getBitmap();
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();

            bitmapDrawable.compress(Bitmap.CompressFormat.JPEG,100,bytes);

            String path = MediaStore.Images.Media.insertImage(getContentResolver(),bitmapDrawable,"Curso ",null);


            Uri uri = Uri.parse(path);

            intent.putExtra(Intent.EXTRA_STREAM,uri);

            startActivity(Intent.createChooser(intent,"Compartilhar imagem curso"));


        }else {
            Toast.makeText(getBaseContext(),"Não possui Imagem pra compartilhar",Toast.LENGTH_LONG).show();
        }






    }


}
