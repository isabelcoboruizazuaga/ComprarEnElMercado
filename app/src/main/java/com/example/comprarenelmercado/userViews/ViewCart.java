package com.example.comprarenelmercado.userViews;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comprarenelmercado.R;
import com.example.comprarenelmercado.models.Order;
import com.example.comprarenelmercado.models.User;
import com.example.comprarenelmercado.userControllers.AdapterChartLines;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

public class ViewCart extends AppCompatActivity {
    private RecyclerView recView;
    private ValueEventListener eventListener;
    Order cart;
    FirebaseAuth mAuth;
    private DatabaseReference dbReference;
    private  String currentUid;
    private User selectedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_cart);

        //FloatingActionButton Listener
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_Order);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cart.getOrderLines().size() > 0) {
                    //The cart is added as an order to the user
                    cart.setOrderDate(new Date());
                    cart.setUserID(currentUid);
                    selectedUser.addOrder(cart);
                    selectedUser.setCart(new Order());
                    //The user is added back to the dataBase
                    dbReference.setValue(selectedUser);

                    Toast.makeText(getApplicationContext(), getText(R.string.ordered).toString(),Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getApplicationContext(), getText(R.string.cartEmpty).toString(), Toast.LENGTH_SHORT).show();
                }
                finish();
            }
        });

        //Current user extracted
        mAuth= FirebaseAuth.getInstance();
        currentUid= mAuth.getCurrentUser().getUid();

        //Recycler view initialization
        recView = (RecyclerView) findViewById(R.id.rv_cartLines);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recView.setLayoutManager(layoutManager);

        //Database initialization and listener assignment
        dbReference = FirebaseDatabase.getInstance().getReference().child("User").child(currentUid);
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
                    //Cart extracted
                    cart=selectedUser.getCart();

                    //Assignment of the Recycler View adapter with the product list
                    AdapterChartLines adapter = new AdapterChartLines(cart);
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