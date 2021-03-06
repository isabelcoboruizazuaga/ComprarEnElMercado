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

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_or_manager);

        getUser();

        Intent inte = new Intent(this, User_StoreSelection.class);
        inte.putExtra("user",user);
        startActivity(inte);
    }

    public void userPart(View view) {
        Intent inte = new Intent(this, User_StoreSelection.class);
        inte.putExtra("user",user);
        startActivity(inte);
    }


    public void getUser (){
        String h = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase.getInstance().getReference().child("User").child(h).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    user = snapshot.getValue(User.class);

                    if (user == null){
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
    }
}