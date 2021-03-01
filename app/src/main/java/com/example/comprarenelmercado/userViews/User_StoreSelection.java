package com.example.comprarenelmercado.userViews;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.comprarenelmercado.MainActivity;
import com.example.comprarenelmercado.R;
import com.example.comprarenelmercado.models.Store;
import com.example.comprarenelmercado.models.User;
import com.example.comprarenelmercado.userControllers.User_AdapterStore;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class User_StoreSelection extends AppCompatActivity {
    ArrayList<Store> stores;
    DatabaseReference dbRefenrece = FirebaseDatabase.getInstance().getReference();
    RecyclerView recView;
    private User user;
    private FirebaseAuth mAuth;

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {// Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.user_menu, menu);
        return true;

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()) {
            case R.id.menitem_store_selection:
                finish();
                startActivity(new Intent(this, User_StoreSelection.class));
                break;
            case R.id.menitem_view_orders:
                finish();
                startActivity(new Intent(this, User_OrderManagement.class));
                break;
            case R.id.menitem_logout:
                mAuth.signOut();
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("EXIT", true);
                startActivity(i);
                break;
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_user__store_selection);

        mAuth= FirebaseAuth.getInstance();

        //FloatingActionButton Listener
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_Cart);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ViewCart.class));
            }
        });

        user = (User) getIntent().getSerializableExtra("user");

        //RecyclerView initialization
        recView = (RecyclerView) findViewById(R.id.rv_userStore);

        //Assignment of the Layout to the Recycler View
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recView.setLayoutManager(layoutManager);

        stores = new ArrayList<Store>();

        listenStoreDatabase();

        //Assignment of the Recycler View adapter with the user list
        User_AdapterStore adapter = new User_AdapterStore(stores,user);
        recView.setAdapter(adapter);
    }

    public void listenStoreDatabase (){
        dbRefenrece.child("stores").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    stores.clear();
                    Iterable<DataSnapshot> datos = snapshot.getChildren();
                    for(DataSnapshot snap: datos){
                        stores.add(snap.getValue(Store.class));
                    }
                    User_AdapterStore adapter = new User_AdapterStore(stores, user);
                    recView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}