package com.example.comprarenelmercado;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.comprarenelmercado.models.Order;
import com.example.comprarenelmercado.models.OrderLine;
import com.example.comprarenelmercado.models.Product;
import com.example.comprarenelmercado.models.User;
import com.example.comprarenelmercado.userControllers.AdapterOrder;
import com.example.comprarenelmercado.userViews.UserOrManagerActivity;
import com.example.comprarenelmercado.userViews.User_OrderManagement;
import com.example.comprarenelmercado.userViews.User_StoreSelection;
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
        setContentView(R.layout.activity_view_balance);

        //UserID extracted
        mAuth= FirebaseAuth.getInstance();
        userID= mAuth.getCurrentUser().getUid();

        //The database is initialized and the eventListener is assigned
        dbReference = FirebaseDatabase.getInstance().getReference().child("User").child(userID);
        setEventListener();
        dbReference.addValueEventListener(eventListener);

        tv_balance = findViewById(R.id.tv_Balance);
        try {
            tv_balance.setText(Float.toString(user.getBalance())+"€");
        }catch (NullPointerException e){
            tv_balance.setText(String.valueOf(0)+"€");
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

                    tv_balance.setText(Float.toString(user.getBalance())+"€");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("onDataChange", "Error!", databaseError.toException());
            }
        };
    }

}