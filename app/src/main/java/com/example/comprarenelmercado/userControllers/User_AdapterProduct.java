package com.example.comprarenelmercado.userControllers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comprarenelmercado.R;
import com.example.comprarenelmercado.models.Order;
import com.example.comprarenelmercado.models.OrderLine;
import com.example.comprarenelmercado.models.Product;
import com.example.comprarenelmercado.models.Store;
import com.example.comprarenelmercado.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class User_AdapterProduct extends RecyclerView.Adapter<User_AdapterProduct.User_AdapterProductViewHolder> {
    private ArrayList<Product> products;
    private User user;
    private Store store;
    private  String idStore;
    private Context context;
    private FirebaseAuth mAuth;
    private String userID;
    private DatabaseReference dbReference;
    private ValueEventListener eventListener;

    //AdapterStore's constructor
    public User_AdapterProduct(ArrayList<Product> products, Store store) {
        this.products = products;
        this.store =store;
        this.idStore=store.getIdStore();
        //UserID extracted
        mAuth= FirebaseAuth.getInstance();
        userID= mAuth.getCurrentUser().getUid();

        //The database is initialized and the eventListener is assigned
        dbReference = FirebaseDatabase.getInstance().getReference().child("User").child(userID);
        setEventListener();
        dbReference.addValueEventListener(eventListener);
    }

    @NonNull
    @Override
    public User_AdapterProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //The view is inflated
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_item, parent, false);

        //The view holder is created
        User_AdapterProductViewHolder avh = new User_AdapterProductViewHolder((itemView));
        return avh;
    }

    @Override
    public void onBindViewHolder(@NonNull User_AdapterProductViewHolder holder, int position) {
        Product productItem = products.get(position);

        String idProduct= productItem.getIdProduct();
        String nameProduct= productItem.getProductName();
        String description = productItem.getDescription();
        Float price = productItem.getPrice();
        Float stock = productItem.getStock();

        //The store data are put into the layout
        holder.tv_nameProduct.setText(nameProduct);
        holder.tv_priceProduct.setText(price.toString() + "€");
        holder.tv_descriptionProduct.setText(description);
        holder.tv_stock.setText(stock.toString());

        //Each item will have an OnClickListener
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //When an item is pressed an option menu will be showed
               showDialog(v, productItem);
            }
        });

    }

    @Override
    public int getItemCount() {
        return products.size();
    }


    public class User_AdapterProductViewHolder extends RecyclerView.ViewHolder {
        //Layout items
        private TextView tv_nameProduct, tv_priceProduct,tv_descriptionProduct, tv_stock;

        //ViewHolder constructor
        public User_AdapterProductViewHolder(View itemView) {
            super(itemView);

            //The context is given the correct value
            context = itemView.getContext();

            //Layout items initialization
            tv_nameProduct = (TextView) itemView.findViewById(R.id.tv_nameProduct);
            tv_priceProduct = (TextView) itemView.findViewById(R.id.tv_priceProduct);
            tv_descriptionProduct = (TextView) itemView.findViewById(R.id.tv_descriptionProduct);
            tv_stock = (TextView) itemView.findViewById(R.id.tv_stock);
        }
    }

    //OptionDialog creation method
    private void showDialog(View view, Product productItem) {
        //Initialization
        AlertDialog.Builder optionDialog = new AlertDialog.Builder(context);
        optionDialog.setTitle(productItem.getProductName());

        //Options creation
        //"view.getResources().getText(R.string.edit)"
        CharSequence opciones[] = {view.getResources().getText(R.string.add_to_order)};
        //OnClickMethod for each option
        optionDialog.setItems(opciones, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                switch (item) {
                    case 0:
                       selectAmount(view,productItem);
                        break;
                }
            }
        });
        //Dialog creation
        AlertDialog alertDialog = optionDialog.create();
        alertDialog.show();
    }

    private void addProductToCart(float cantidad, Product productItem){
        OrderLine orderLine= new OrderLine(productItem,cantidad, idStore);
        Order cart;
        try {
            cart = user.getCart();
            user.addProductToCart(orderLine);
        }catch ( NullPointerException e){
            cart= new Order(new Date());
            cart.addLine(orderLine);
        }
        user.setCart(cart);
        FirebaseDatabase.getInstance().getReference().child("User").child(user.getUid()).setValue(user);
    }

    private void selectAmount(View view, Product productItem) {
        //Initialization
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle(productItem.getProductName());
        alertDialog.setMessage(view.getResources().getText(R.string.amount));

        //Layout
        final EditText input = new EditText(context);
        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertDialog.setView(input);

        //Accept
        alertDialog.setPositiveButton(view.getResources().getText(R.string.accept),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        try{
                            addProductToCart(Float.parseFloat(input.getText().toString()),productItem);
                            Toast.makeText(context,view.getResources().getText(R.string.added) + input.getText().toString() +" " + productItem.getProductName(),Toast.LENGTH_SHORT).show();
                        }catch (Exception e){};
                    }
                });

        //Cancel
        alertDialog.setNegativeButton(view.getResources().getText(R.string.cancel),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alertDialog.show();
    }

    //Database listener
    public void setEventListener(){
        eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    //The current user is extracted
                    user = dataSnapshot.getValue(User.class);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("onDataChange", "Error!", databaseError.toException());
            }
        };
    }


}
