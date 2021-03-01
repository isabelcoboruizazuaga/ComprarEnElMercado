package com.example.comprarenelmercado;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.comprarenelmercado.models.Order;
import com.example.comprarenelmercado.models.OrderLine;
import com.example.comprarenelmercado.models.Product;
import com.example.comprarenelmercado.models.User;
import com.example.comprarenelmercado.userControllers.AdapterOrder;
import com.example.comprarenelmercado.userViews.UserOrManagerActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewBalance extends AppCompatActivity {
    private User user;
    private FirebaseAuth mAuth;
    private String userID;
    private TextView tv_balance;
    private DatabaseReference dbReference;
    private ValueEventListener eventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_balance);

        //UserID extracted
        mAuth= FirebaseAuth.getInstance();
        userID= mAuth.getCurrentUser().getUid();

        //The database is initialized and the eventListener is assigned
        dbReference = FirebaseDatabase.getInstance().getReference().child("User").child(userID);
        setEventListener();
        dbReference.addValueEventListener(eventListener);

       // getUser();


        tv_balance = findViewById(R.id.tv_Balance);
        try {
            tv_balance.setText(Float.toString(user.getBalance()));
        }catch (NullPointerException e){
            tv_balance.setText(String.valueOf(0));
        }
    }

    //Database listener
    public void setEventListener(){
        eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    //The current user is extracted
                    user = dataSnapshot.getValue(User.class);

                    tv_balance.setText(Float.toString(user.getBalance()));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("onDataChange", "Error!", databaseError.toException());
            }
        };
    }

    /*public void getUser (){
        String h = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase.getInstance().getReference().child("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    user = snapshot.getValue(User.class);

                    if (user == null){
                        System.out.println("nulo");
                    } else{
                        System.out.println("YESBalance");
                        System.out.println(user.getUid());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }*/
}