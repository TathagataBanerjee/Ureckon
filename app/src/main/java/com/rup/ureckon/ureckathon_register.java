package com.rup.ureckon;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ureckathon_register extends AppCompatActivity {

    private Button reg;
    private ImageView locima;
    private FirebaseAuth mAuth;
    private String Uid;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private StorageReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ureckathon_register);

        reg = (Button) findViewById(R.id.event_reg);
        locima = (ImageView) findViewById(R.id.venue_image);
        locima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://bit.ly/2tQ9NSh");
                startActivity(new Intent(Intent.ACTION_VIEW, uri));
            }
        });
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mAuth=FirebaseAuth.getInstance();
                FirebaseUser user=mAuth.getCurrentUser();
                // Uid=user.getUid();
                if(user==null)
                {
                    Toast.makeText(ureckathon_register.this, "First register for the app..", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Intent intent=new Intent(ureckathon_register.this, TeamRegistrations.class);

                      intent.putExtra("event","ureckathon");
                    intent.putExtra("url","https://docs.google.com/forms/d/e/1FAIpQLSdyt_VCrQY4G5KhklrxD3AUQ1d84YYrr6jEUaYsQHkEVon-Hw/viewform");
                    startActivity(intent);
                }
            }
        });
        Button reg2=findViewById(R.id.event_reg2);

        reg2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                download();
            }
        });
    }

    private void download()
    {
        Toast.makeText(ureckathon_register.this, "Downloading..", Toast.LENGTH_LONG).show();
        storageReference=firebaseStorage.getInstance().getReference();

        ref=storageReference.child("ureckathon.pdf");
        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                String url=uri.toString();

                downloadFile(ureckathon_register.this,"Ureckathon",".pdf", Environment.DIRECTORY_DOWNLOADS,url);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }

    private void downloadFile(Context context, String fileName, String fileExtension, String destinationDirectory, String url)
    {
        DownloadManager downloadManager=(DownloadManager)context.getSystemService(Context.DOWNLOAD_SERVICE);

        Uri uri=Uri.parse(url);
        DownloadManager.Request request=new DownloadManager.Request(uri);

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context,destinationDirectory,fileName+fileExtension);

        downloadManager.enqueue(request);

    }
}