package com.example.comprarenelmercado.userControllers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comprarenelmercado.R;
import com.example.comprarenelmercado.models.Order;
import com.example.comprarenelmercado.models.OrderLine;
import com.example.comprarenelmercado.models.Product;
import com.example.comprarenelmercado.models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdapterOrderLine extends RecyclerView.Adapter<AdapterOrderLine.AdapterOrderLineViewHolder> {
    private ArrayList<OrderLine> lines;
    private Order order;
    private Context context;
    private User user;
    //Database variables
    private FirebaseDatabase database;
    private DatabaseReference dbReference;
    private ValueEventListener eventListener;

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

        //Each item will have an OnClickListener
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Database initialization
                database = FirebaseDatabase.getInstance();
                dbReference = database.getReference().child("User").child(order.getUserID());
                setEventListener();
                dbReference.addValueEventListener(eventListener);

                //When an item is pressed an option menu will be showed
                if(order.getStatus() == 0){
                    showDialog(v,lineItem);
                }
            }
        });
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
    private void showDialog(View view,OrderLine lineItem) {
        //Initialization
        AlertDialog.Builder optionDialog = new AlertDialog.Builder(context);
        optionDialog.setTitle(String.valueOf(lineItem.getProduct().getProductName()));

        //Options creation
        CharSequence opciones[] = {context.getString(R.string.manage),context.getString(R.string.delete)};
        //OnClickMethod for each option
        optionDialog.setItems(opciones, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                switch (item) {
                    case 1:
                        deleteConfirmation(view,lineItem);
                        break;
                }
            }
        });
        //Dialog creation
        AlertDialog alertDialog = optionDialog.create();
        alertDialog.show();
    }

    private void deleteConfirmation(View view, OrderLine lineItem){
        //Initialization
        AlertDialog.Builder alertDialogBu = new AlertDialog.Builder(context);
        alertDialogBu.setTitle(view.getResources().getText(R.string.delete));
        alertDialogBu.setMessage(view.getResources().getText(R.string.are_you_sure) + String.valueOf(lineItem.getProduct().getProductName()) + view.getResources().getText(R.string.cant_undo));

        //Positive option
        alertDialogBu.setPositiveButton( view.getResources().getText(R.string.accept), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                delete(lineItem);
                Toast.makeText(context, String.valueOf(lineItem.getProduct().getProductName()) + view.getResources().getText(R.string.is_deleted), Toast.LENGTH_SHORT).show();
            }
        });
        //Negative option
        alertDialogBu.setNegativeButton(view.getResources().getText(R.string.cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(context, view.getResources().getText(R.string.cancelled), Toast.LENGTH_SHORT).show();
            }
        });
        //Dialog creation
        AlertDialog alertDialog = alertDialogBu.create();
        alertDialog.show();
    }

    private void delete(OrderLine orderLine){
        OrderLine myLine;
        List<OrderLine> orderLinesDeleted= new ArrayList<>();
        //Iterate in orderLines, the line deleted is erased
        for(int i= order.getOrderLines().size();i>0;i--){
            myLine=order.getOrderLines().get(i-1);
            if(myLine.getOrderLineId()!=orderLine.getOrderLineId()){
                orderLinesDeleted.add(myLine);
            }
        }
        order.setOrderLines((ArrayList<OrderLine>) orderLinesDeleted);


        Order myOrder;
        List<Order> ordersDeleted= new ArrayList<>();
        //Same with the order of those lines, it will be replaced with an order without that line
        for(int i= user.getOrders().size(); i>0;  i--){
            myOrder=user.getOrders().get(i-1);
            if(myOrder.getOrderId()!=order.getOrderId()){
                ordersDeleted.add(myOrder);
            }else{
                //if the order is the order of the line the old lines are replaced only if there are lines left
                if(orderLinesDeleted.size()>0) {
                    myOrder.setOrderLines((ArrayList<OrderLine>) orderLinesDeleted);
                    ordersDeleted.add(myOrder);
                } //If there aren't lines left the order is deleted
            }
        }
        user.setOrders(ordersDeleted);
        dbReference.setValue(user);
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

