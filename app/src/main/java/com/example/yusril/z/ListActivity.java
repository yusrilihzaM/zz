package com.example.yusril.z;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.example.yusril.z.adapter.RequestAdapterRecyclerView;
import com.example.yusril.z.model.Request;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {
    private DatabaseReference databaase;
    private ArrayList<Request>daftarReq;
    private RequestAdapterRecyclerView requestAdapterRecyclerView;

    private RecyclerView rc_list_request;
    private ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        databaase=FirebaseDatabase.getInstance().getReference();

        rc_list_request=findViewById(R.id.rc_list_request);

        RecyclerView.LayoutManager mLayoutManagaer=new LinearLayoutManager(getApplicationContext()); //grid vertical
        rc_list_request.setLayoutManager(mLayoutManagaer);
        rc_list_request.setItemAnimator(new DefaultItemAnimator());

        loading=ProgressDialog.show(ListActivity.this,
                null,
                "Silakan Tunggu",
                true,
                false);

        databaase.child("Request").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                /**
                 * ketika ada data baru maka akan di masukan ke array list
                 * **/
                daftarReq=new ArrayList<>();
                for(DataSnapshot noteDataSnapshot: dataSnapshot.getChildren()){
// mengambil data
                    Request request =noteDataSnapshot.getValue(Request.class);
                    request.setKey(noteDataSnapshot.getKey());

                    daftarReq.add(request);//menambahkan data ke array
                }
                /**
                 * Inisialisasi adapter dan data hotel dalam bentuk ArrayList
                 * dan mengeset Adapter ke dalam RecyclerView
                 */
                requestAdapterRecyclerView = new RequestAdapterRecyclerView(daftarReq, ListActivity.this);
                rc_list_request.setAdapter(requestAdapterRecyclerView);
                loading.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //ostosmatis jalan ketika error
                System.out.println(databaseError.getDetails()+" "+databaseError.getMessage());
                loading.dismiss();
            }
        });
        findViewById(R.id.fab_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ListActivity.this, MainActivity.class)
                        .putExtra("id", "")
                        .putExtra("title", "")
                        .putExtra("email", "")
                        .putExtra("desk", ""));
            }
        });
    }
}
