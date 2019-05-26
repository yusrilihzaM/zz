package com.example.yusril.z;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.yusril.z.model.Request;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private DatabaseReference database;

    private EditText etNama, etEmail, etDesk;
    private Button btn_cancel, btn_save;
    private ProgressDialog loading;

    private String Spid, Spnama,Spemail,Spdesk;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database=FirebaseDatabase.getInstance().getReference();//inisialisasi fb

        Spid=getIntent().getStringExtra("id");
        Spnama=getIntent().getStringExtra("title");
        Spemail=getIntent().getStringExtra("email");
        Spdesk=getIntent().getStringExtra("desk");
        etNama=findViewById(R.id.et_name);
        etEmail=findViewById(R.id.et_email);
        etDesk=findViewById(R.id.et_desk);
        btn_save=findViewById(R.id.btn_save);
        btn_cancel=findViewById(R.id.btn_cancel);

        //Menampilkan data
        etNama.setText(Spnama);
        etEmail.setText(Spemail);
        etDesk.setText(Spdesk);

        if(Spid.equals("")){
            btn_save.setText("Save");
            btn_cancel.setText("Cancel");
        }else {
            btn_save.setText("Edit");
            btn_cancel.setText("Delete");
        }
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Snama=etNama.getText().toString();
                String Semail=etEmail.getText().toString();
                String Sdesk=etDesk.getText().toString();
                if(btn_save.getText().equals("Save")){
                    //Perintah Save
                    if(Snama.equals("")){
                        etNama.setError("Silakan Masukan Nama");
                        etNama.requestFocus();
                    }else
                    if(Semail.equals("")){
                        etEmail.setError("Silakan Masukan Email");
                        etEmail.requestFocus();
                    }else
                    if (Sdesk.equals("")){
                        etDesk.setError("Silakan Masukan Diskripsi");
                        etDesk.requestFocus();
                    }else{
                        loading=ProgressDialog.show(MainActivity.this,
                                null,
                                "Silakan tunggu",
                                true,
                                false);

                        submitUser(new Request(
                                Snama.toLowerCase(),
                                Semail.toLowerCase(),
                                Sdesk.toLowerCase()));
                    }
                }else {
                    //Perintah Edit
                    if(Snama.equals("")){
                        etNama.setError("Silakan Masukan Nama");
                        etNama.requestFocus();
                    }else
                    if(Semail.equals("")){
                        etEmail.setError("Silakan Masukan Email");
                        etEmail.requestFocus();
                    }else
                    if (Sdesk.equals("")){
                        etDesk.setError("Silakan Masukan Diskripsi");
                        etDesk.requestFocus();
                    }else{
                        loading=ProgressDialog.show(MainActivity.this,
                                null,
                                "Silakan tunggu",
                                true,
                                false);

                        editUser(new Request(
                                Snama.toLowerCase(),
                                Semail.toLowerCase(),
                                Sdesk.toLowerCase()),Spid);
                    }
                }

            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (btn_cancel.getText().equals("Cancel")) {
                    //tutup page
                    finish();
                } else {
                    // delete
                    deleteUser(Spid);
                }

            }
        });
    }

    private void submitUser(Request request){
        database.child("Request")
//                .child("request satu")
        .push().setValue(request)
                .addOnSuccessListener(this, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        loading.dismiss();

                        //clear View setelah input data
                        etNama.setText("");
                        etEmail.setText("");
                        etDesk.setText("");

                        Toast.makeText(MainActivity.this,"Data berhasil ditambahkan",
                                Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MainActivity.this, ListActivity.class));
                    }
                });
    }

    private void editUser(Request request, String id){
        database.child("Request")
                .child(id)
                .setValue(request)
                .addOnSuccessListener(this, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        loading.dismiss();

                        etNama.setText("");
                        etEmail.setText("");
                        etDesk.setText("");

                        Toast.makeText(MainActivity.this,
                                "Data Berhasil diedit",
                                Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MainActivity.this, ListActivity.class));
                    }
                });
    }

    private void deleteUser(String id){
        database.child("Request")
                .child(id)
                .removeValue()
                .addOnSuccessListener(this, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MainActivity.this,
                                "Data Berhasil delete",
                                Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MainActivity.this, ListActivity.class));
                    }
                });
    }
}
