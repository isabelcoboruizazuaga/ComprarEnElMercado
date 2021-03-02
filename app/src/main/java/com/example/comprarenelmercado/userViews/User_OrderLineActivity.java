package com.example.comprarenelmercado.userViews;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comprarenelmercado.R;
import com.example.comprarenelmercado.models.Order;
import com.example.comprarenelmercado.models.OrderLine;
import com.example.comprarenelmercado.models.User;
import com.example.comprarenelmercado.userControllers.AdapterOrderLine;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class User_OrderLineActivity extends AppCompatActivity {

    private RecyclerView recView;
    private ValueEventListener eventListener;

    Order order;
    private DatabaseReference dbReference;
    private User selectedUser;
    private ArrayList<OrderLine> lines;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user__order_line);

        //Recycler view initialization
        recView = (RecyclerView) findViewById(R.id.rv_userOrderLines);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recView.setLayoutManager(layoutManager);

        //Order is recovered
        order = (Order) getIntent().getSerializableExtra("order");


        //Database initialization
       dbReference = FirebaseDatabase.getInstance().getReference().child("User").child(order.getUserID());
       setEventListener();
       dbReference.addValueEventListener(eventListener);
    }

    public void setEventListener(){
        eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    //The current user is extracted
                    selectedUser = dataSnapshot.getValue(User.class);
                    //Orders list is refilled
                    lines = order.getOrderLines();

                    //Assignment of the Recycler View adapter with the product list
                    AdapterOrderLine adapter = new AdapterOrderLine(order,lines);
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