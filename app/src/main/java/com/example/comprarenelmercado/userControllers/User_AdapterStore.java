package com.example.comprarenelmercado.userControllers;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comprarenelmercado.R;
import com.example.comprarenelmercado.models.Store;
import com.example.comprarenelmercado.models.User;
import com.example.comprarenelmercado.userViews.User_ProductSelection;

import java.util.ArrayList;

public class User_AdapterStore extends RecyclerView.Adapter<User_AdapterStore.User_AdapterStoreViewHolder> {
    private ArrayList<Store> stores;
    private User user;
    private Context context;

    //AdapterStore's constructor
    public User_AdapterStore(ArrayList<Store> stores, User user){

        this.stores = stores;

        this.user = user;
    }

    @NonNull
    @Override
    public User_AdapterStoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //The view is inflated
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.store_item,parent,false);

        //The view holder is created
        User_AdapterStoreViewHolder avh=new User_AdapterStoreViewHolder((itemView));
        return avh;
    }

    @Override
    public void onBindViewHolder(@NonNull User_AdapterStoreViewHolder holder, int position) {
        Store storeItem= stores.get(position);
        String idStore=storeItem.getIdStore();
        String nameStore=storeItem.getNameStore();
        //The store data are put into the layout
        holder.tv_nameStore.setText(nameStore);
        holder.tv_idStore.setText(idStore);

        //Each item will have an OnClickListener
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //When an item is pressed the list of its products will be loaded
                Intent intent= new Intent(context, User_ProductSelection.class);
                intent.putExtra("idStore",idStore);
                intent.putExtra("user",user);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return stores.size();
    }


    public class User_AdapterStoreViewHolder extends RecyclerView.ViewHolder{
        //Layout items
        private TextView tv_nameStore,tv_idStore;

        //ViewHolder constructor
        public  User_AdapterStoreViewHolder(View itemView){
            super(itemView);

            //The context is given the correct value
            context = itemView.getContext();

            //Layout items initialization
            tv_nameStore=(TextView) itemView.findViewById(R.id.tv_nameStore);
            tv_idStore=(TextView) itemView.findViewById(R.id.tv_idStore);
        }
    }
}