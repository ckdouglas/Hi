package com.example.hi.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.hi.R;
import com.example.hi.activitities.ActivityHome;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public  class Fragment_Profile_Info extends Fragment implements View.OnClickListener {
    private static final int REQUEST_CODE_PICK_IMAGE = 1;
    ImageView btn_next;
    CircleImageView profile_picture;
    FirebaseUser mUser;
    DatabaseReference mDbRef;
    EditText mUsername;
    StorageReference storageReference;
    private Uri imageUri;
    private String profileURl ="default";
    private ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_info,container,false);
        btn_next = (ImageView) view.findViewById(R.id.btn_next);
        profile_picture = (CircleImageView) view.findViewById(R.id.profile_image);
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mDbRef = FirebaseDatabase.getInstance().getReference("users").child(mUser.getUid());
        mUsername = view.findViewById(R.id.txtUsername);
        progressDialog = new ProgressDialog(getContext());
        storageReference = FirebaseStorage.getInstance().getReference("uploads");
        btn_next.setOnClickListener(this);
        profile_picture.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_next:
                saveData();
                break;
            case R.id.profile_image:
                openGallery();
                break;
        }
    }

    private void saveData() {
        progressDialog.setMessage("Saving profile");
        progressDialog.show();
        mDbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot!=null){
                    Toast.makeText(getContext(),"empty data",Toast.LENGTH_LONG).show();
                    HashMap<String, String> hashMap  = new HashMap<>();
                    hashMap.put("id",mUser.getUid());
                    hashMap.put("name",mUsername.getText().toString());
                    hashMap.put("phone",mUser.getPhoneNumber());
                    hashMap.put("status","Hey there,I am using Hi");
                    hashMap.put("profile_URL",profileURl);
                    mDbRef.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                progressDialog.dismiss();
                            Intent intent = new Intent(getContext(),ActivityHome.class);
                            startActivity(intent);
                            getActivity().finish();
                            }
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void openGallery(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_CODE_PICK_IMAGE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {

            try {
                imageUri = data.getData();
                final InputStream imageStream;
                imageStream = getContext().getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                profile_picture.setImageBitmap(selectedImage);
                uploadImage();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
    }

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadImage(){
        final ProgressDialog  progressDialog =new ProgressDialog(getContext());
        progressDialog.setMessage("setting profile image");
        progressDialog.show();
        if (imageUri !=null){
            final StorageReference fileRef= storageReference.child(System.currentTimeMillis()+"."+getFileExtension(imageUri));
            final UploadTask uploadTask = fileRef.putFile(imageUri);

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "Uploaded", Toast.LENGTH_SHORT).show();

                    uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }
                            return fileRef.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Uri downUri = task.getResult();
                                profileURl = downUri.toString();
                            }
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}
