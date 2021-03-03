package com.example.comprarenelmercado.userViews;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comprarenelmercado.MainActivity;
import com.example.comprarenelmercado.R;
import com.example.comprarenelmercado.ViewBalance;
import com.example.comprarenelmercado.models.Order;
import com.example.comprarenelmercado.models.OrderLine;
import com.example.comprarenelmercado.models.Product;
import com.example.comprarenelmercado.models.User;
import com.example.comprarenelmercado.userControllers.AdapterOrder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class User_OrderManagement extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private RecyclerView recView;
    private String userID;
    private ValueEventListener eventListener;
    private User selectedUser;
    private ArrayList<Order> orders;
    private DatabaseReference dbReference;
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
            case R.id.menitem_view_balance:
                finish();
                startActivity(new Intent(this, ViewBalance.class));
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
        setContentView(R.layout.activity_user__order_management);

        //FloatingActionButton Listener
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_Cart);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ViewCart.class));
            }
        });

        //The Recycler View is initialized
        recView = (RecyclerView) findViewById(R.id.rv_myUserOrders);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recView.setLayoutManager(layoutManager);

        //UserID extracted
        mAuth= FirebaseAuth.getInstance();
        userID= mAuth.getCurrentUser().getUid();

        orders= new ArrayList<Order>();

        //The database is initialized and the eventListener is assigned
        dbReference = FirebaseDatabase.getInstance().getReference().child("User").child(userID);
        setEventListener();
        dbReference.addValueEventListener(eventListener);
    }

    //Database listener
    public void setEventListener(){
        eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    orders.clear();
                    //The current user is extracted
                    selectedUser = dataSnapshot.getValue(User.class);
                    //Orders list is refilled
                    orders = (ArrayList<Order>) selectedUser.getOrders();

                    //Assignment of the Recycler View adapter with the product list
                    AdapterOrder adapter = new AdapterOrder(orders);
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