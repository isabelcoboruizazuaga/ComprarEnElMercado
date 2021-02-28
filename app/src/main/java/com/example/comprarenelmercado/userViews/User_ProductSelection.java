package com.example.comprarenelmercado.userViews;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.comprarenelmercado.R;
import com.example.comprarenelmercado.models.Product;
import com.example.comprarenelmercado.models.Store;
import com.example.comprarenelmercado.models.User;
import com.example.comprarenelmercado.userControllers.User_AdapterProduct;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class User_ProductSelection extends AppCompatActivity {
    private ValueEventListener eventListener;
    private RecyclerView recView;
    //Object variables
    private User user;
    private Store selectedStore;
    String idStore;
    private Intent intent;
    private ArrayList<Product> products;
    //Database and Firebase variables
    private FirebaseDatabase database;
    private DatabaseReference dbReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user__product_selection);

        //The storeId is recovered
        intent = getIntent();
        idStore= intent.getStringExtra("idStore");
        user = (User) intent.getSerializableExtra("user");

        /*
        //FloatingActionButton Listener
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_addProduct);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getBaseContext(), AddProductActivity.class);
                intent.putExtra("store",selectedStore);
                startActivity(intent);
            }
        });*/


        //RecyclerView and list initialization
        recView = (RecyclerView) findViewById(R.id.rv_userProduct);
        products=new ArrayList<>();

        //Assignment of the Layout to the Recycler View
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recView.setLayoutManager(layoutManager);

        //Database initialization
        database = FirebaseDatabase.getInstance();
        dbReference = database.getReference();
        //EventListeners asignation
        setEventListener();
        dbReference.child("stores").child(idStore).addValueEventListener(eventListener);
    }

    //Database listener
    public void setEventListener(){

        eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    //The current store is extracted
                    selectedStore = dataSnapshot.getValue(Store.class);
                    //Products list is refilled
                    products= (ArrayList<Product>) selectedStore.getProducts();

                    //Assignment of the Recycler View adapter with the product list
                    User_AdapterProduct adapter = new User_AdapterProduct(products,selectedStore,user);
                    recView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("onDataChange", "Error!", databaseError.toException());
            }
        };
    }


}