package com.example.comprarenelmercado.userControllers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comprarenelmercado.R;
import com.example.comprarenelmercado.models.Order;
import com.example.comprarenelmercado.models.OrderLine;
import com.example.comprarenelmercado.models.Product;
import com.example.comprarenelmercado.models.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AdapterOrderLine extends RecyclerView.Adapter<AdapterOrderLine.AdapterOrderLineViewHolder> {
    private ArrayList<OrderLine> lines;
    private Order order;
    private Context context;
    //Database variables
    private FirebaseDatabase database;
    private DatabaseReference dbReference;

    //AdapterOrder's constructor
    public AdapterOrderLine(Order order) {
        this.lines = order.getOrderLines();
        this.order = order;
        //Database initialization
        database = FirebaseDatabase.getInstance();
        dbReference = database.getReference().child("User");
    }

    @NonNull
    @Override
    public AdapterOrderLineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //The view is inflated
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.orderline_item, parent, false);

        //The view holder is created
        AdapterOrderLineViewHolder avh=new AdapterOrderLineViewHolder((itemView));
        return avh;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterOrderLineViewHolder holder, int position) {
        OrderLine lineItem = lines.get(position);

        Product product = lineItem.getProduct();
        Float amount = lineItem.getAmount();
        String store = lineItem.getStore();

        //The order data are put into the layout
        holder.tv_orderLineStore.setText(store);
        holder.tv_orderLineProductName.setText(product.getProductName());
        holder.tv_orderLineAmount.setText(Float.toString(amount));
        holder.tv_orderLinePrice.setText(Float.toString(product.getPrice()) + " €");
        holder.tv_orderLineTotal.setText(Float.toString(product.getPrice()*amount) + " €");

        /*//Each item will have an OnClickListener
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(v) {
                //Database initialization
                database = FirebaseDatabase.getInstance();
                dbReference = database.getReference().child("User").child(orderItem.getUid());

                //When an item is pressed an option menu will be showed
                showDialog(v,orderItem);
            }
        });*/

    }

    @Override
    public int getItemCount() {
        return lines.size();
    }


    public class AdapterOrderLineViewHolder  extends RecyclerView.ViewHolder {
        //Layout items
        private TextView tv_orderLineStore, tv_orderLineProductName, tv_orderLineAmount,tv_orderLineTotal, tv_orderLinePrice;

        //ViewHolder constructor
        public AdapterOrderLineViewHolder(View itemView) {
            super(itemView);

            //The context is given the correct value
            context = itemView.getContext();

            //Layout items initialization
            tv_orderLineStore = (TextView) itemView.findViewById(R.id.tv_orderLineStore);
            tv_orderLineProductName = (TextView) itemView.findViewById(R.id.tv_orderLineProductName);
            tv_orderLineAmount = (TextView) itemView.findViewById(R.id.tv_orderLineAmount);
            tv_orderLineTotal = (TextView) itemView.findViewById(R.id.tv_orderLineTotal);
            tv_orderLinePrice = (TextView) itemView.findViewById(R.id.tv_orderLinePrice);
        }
    }

    //OptionDialog creation method
   /* private void showDialog(View view, User user) {
        //Initialization
        AlertDialog.Builder optionDialog = new AlertDialog.Builder(context);
        optionDialog.setTitle(user.getUserName());

        //Options creation
        CharSequence opciones[] = {view.getResources().getText(R.string.viewOrders),view.getResources().getText(R.string.setBalance)};
        //OnClickMethod for each option
        optionDialog.setItems(opciones, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                switch (item) {
                    case 0:
                        Intent intent = new Intent(context, ProductsActivity.class);
                        intent.putExtra("user",user);
                        context.startActivity(intent);
                        break;
                    case 1:
                        //grantBalanceDialog(view, user);
                        break;
                }
            }
        });
        //Dialog creation
        AlertDialog alertDialog = optionDialog.create();
        alertDialog.show();
    }*/
}

