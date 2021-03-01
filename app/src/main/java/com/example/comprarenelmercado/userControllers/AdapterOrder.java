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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AdapterOrder extends RecyclerView.Adapter<AdapterOrder.AdapterOrderViewHolder> {
    private List<Order> orders;
    private Context context;
    //Database variables
    private FirebaseDatabase database;
    private DatabaseReference dbReference;

    //AdapterOrder's constructor
    public AdapterOrder(List<Order> orders) {
        this.orders = orders;

        //Database initialization
        database = FirebaseDatabase.getInstance();
        dbReference = database.getReference().child("User");

    }

    @NonNull
    @Override
    public AdapterOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //The view is inflated
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_item, parent, false);

        //The view holder is created
        AdapterOrderViewHolder avh=new AdapterOrderViewHolder((itemView));
        return avh;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterOrderViewHolder holder, int position) {
        Order orderItem = orders.get(position);

        String orderId = orderItem.getOrderId();
        Date orderDate = orderItem.getOrderDate();
        int orderStatus = orderItem.getStatus();

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        String formattedDate = df.format(orderDate);

        String statusInString="";
        switch (orderStatus){
            case 0:
                statusInString= holder.itemView.getContext().getResources().getText(R.string.outstanding).toString();
                break;
            case 1:
                statusInString=holder.itemView.getContext().getResources().getText(R.string.collected).toString();
                break;
        }

        //The order data are put into the layout
        holder.tv_orderId.setText(orderId.toString());
        holder.tv_orderDate.setText(formattedDate);
        holder.tv_orderStatus.setText(statusInString);

        //Each item will have an OnClickListener
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Database initialization
                database = FirebaseDatabase.getInstance();
                dbReference = database.getReference().child("User").child(orderItem.getUserID());

                //When an item is pressed an option menu will be showed
                //showDialog(v,orderItem);
            }
        });

    }

    @Override
    public int getItemCount() {
        return orders.size();
    }


    public class AdapterOrderViewHolder  extends RecyclerView.ViewHolder {
        //Layout items
        private TextView tv_orderId, tv_orderDate, tv_orderStatus;

        //ViewHolder constructor
        public AdapterOrderViewHolder(View itemView) {
            super(itemView);

            //The context is given the correct value
            context = itemView.getContext();

            //Layout items initialization
            tv_orderId = (TextView) itemView.findViewById(R.id.tv_orderId);
            tv_orderDate = (TextView) itemView.findViewById(R.id.tv_orderDate);
            tv_orderStatus = (TextView) itemView.findViewById(R.id.tv_orderStatus);
        }
    }

    //OptionDialog creation method
    /*private void showDialog(View view,Order order) {
        //Initialization
        AlertDialog.Builder optionDialog = new AlertDialog.Builder(context);
        optionDialog.setTitle(order.getOrderId());

        //Options creation
        CharSequence opciones[] = {context.getString(R.string.manage),context.getString(R.string.delete)};
        //OnClickMethod for each option
        optionDialog.setItems(opciones, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                switch (item) {
                    case 0:
                        Intent intent = new Intent(context, OrderLineActivity.class);
                        intent.putExtra("order",order);
                        context.startActivity(intent);
                        break;
                    case 1:
                        //todo borrar pedido
                        break;
                }
            }
        });
        //Dialog creation
        AlertDialog alertDialog = optionDialog.create();
        alertDialog.show();
    }*/
}
