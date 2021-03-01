package com.example.comprarenelmercado.userViews;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.example.comprarenelmercado.R;
import com.example.comprarenelmercado.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserOrManagerActivity extends AppCompatActivity {

    private static User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_or_manager);

        user = getUser();

    }

    public void userPart(View view) {
        Intent inte = new Intent(this, User_StoreSelection.class);
        System.out.println(user.getEmail());
        inte.putExtra("user",user);
        startActivity(inte);
    }

    static User us;
    public static User getUser (){
        String h = FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseDatabase.getInstance().getReference().child("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    us = snapshot.getValue(User.class);

                    if (us == null){
                        System.out.println("nulo");
                    } else{
                        System.out.println("YES");
                        System.out.println(user.getUid());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return us;
    }
}