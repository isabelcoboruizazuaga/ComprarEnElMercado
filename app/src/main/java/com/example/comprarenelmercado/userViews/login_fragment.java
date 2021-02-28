package com.example.comprarenelmercado.userViews;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.comprarenelmercado.R;
import com.example.comprarenelmercado.models.User;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class login_fragment extends Fragment {

    private static final int RC_GOOGLE_API = 1;
    private DatabaseReference dbReference;
    private FirebaseDatabase database;

    //variables
    Button bt_login, bt_registrar;
    SignInButton bt_googleSingIn;
    EditText et_correo, et_contrasena;
    FirebaseAuth mAuth;
    GoogleSignInClient client_google;

    public login_fragment() {
        // Required empty public constructor
    }

    public static login_fragment newInstance(String param1, String param2) {
        login_fragment fragment = new login_fragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bt_login = getView().findViewById(R.id.bt_login);
        bt_registrar = getView().findViewById(R.id.bt_singUp);
        bt_googleSingIn = getView().findViewById(R.id.botonGoogle);

        et_correo = getView().findViewById(R.id.et_loginCorreo);
        et_contrasena = getView().findViewById(R.id.et_loginPassword);

        database= FirebaseDatabase.getInstance();
        dbReference=database.getReference();

        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String correo = et_correo.getText().toString();
                String contrasena = et_contrasena.getText().toString();
                if(correo.length()<5 || contrasena.length()<6){
                    Toast.makeText(getContext(), R.string.short_fields, Toast.LENGTH_SHORT).show();
                } else {
                    if(iniciaSesion(correo,contrasena)){
                        Toast.makeText(getContext(), R.string.sesion_ok, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getContext(), UserOrManagerActivity.class));
                    }
                }
            }
        });

        bt_registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.nav_loginToRegister);
            }
        });


        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        client_google = GoogleSignIn.getClient(getContext(), gso);

        mAuth = FirebaseAuth.getInstance();

        bt_googleSingIn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent singIntent = client_google.getSignInIntent();
                startActivityForResult(singIntent, RC_GOOGLE_API);
            }
        });


    }


    Boolean exito = false;
    public boolean iniciaSesion(String correo, String contrasena){
        FirebaseAuth.getInstance().signInWithEmailAndPassword(correo,contrasena).addOnCompleteListener(
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        exito = task.isSuccessful();
                    }
                }
        );
        return exito;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_GOOGLE_API){
            Task<GoogleSignInAccount> gTask = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = gTask.getResult(ApiException.class);
                if(account != null){
                    mAuth.signInWithCredential(GoogleAuthProvider.getCredential(account.getIdToken(), null));

                    if(mAuth.getCurrentUser() != null){
                        FirebaseUser user = mAuth.getCurrentUser();

                        String uid = user.getUid();

                        dbReference.child("User").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()){
                                    startActivity(new Intent(getContext(),UserOrManagerActivity.class));
                                    Toast.makeText(getContext(),R.string.sesion_ok, Toast.LENGTH_LONG).show();
                                } else {
                                    User userObject= new User(user.getEmail(),user.getDisplayName(),uid);
                                    dbReference.child("User").child(uid).setValue(userObject);
                                    startActivity(new Intent(getContext(),UserOrManagerActivity.class));
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }

                } else{
                    Toast.makeText(getContext(),R.string.error_register, Toast.LENGTH_LONG).show();
                }
            } catch (ApiException e) {
                Toast.makeText(getContext(),e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}