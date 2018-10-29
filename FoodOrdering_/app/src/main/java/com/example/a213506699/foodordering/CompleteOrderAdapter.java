package com.example.a213506699.foodordering;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class CompleteOrderAdapter extends RecyclerView.Adapter<CompleteOrderAdapter.ViewHolder> {
    private static final String TAG = "CompleteOrderAdapter";
    ArrayList<String> cartItemDescriptions;
    ArrayList<Double> cartPrices;
    ArrayList<Integer> cartQuantities;
    ArrayList<Integer> menuItemIDs;
    Context ctx;

    public CompleteOrderAdapter(Context ctx,ArrayList<String> cartItemDescriptions, ArrayList<Double> cartPrices, ArrayList<Integer> cartQuantities, ArrayList<Integer> menuItemIDs) {
        this.cartItemDescriptions = cartItemDescriptions;
        this.cartPrices = cartPrices;
        this.cartQuantities = cartQuantities;
        this.menuItemIDs = menuItemIDs;
        this.ctx = ctx;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.complete_orderl_list, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.description.setText(cartItemDescriptions.get(i));
        viewHolder.price.setText("R " + cartPrices.get(i));
        viewHolder.txtQuantity.setText(Integer.toString((cartQuantities.get(i))));
    }

    @Override
    public int getItemCount() {
        return cartItemDescriptions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView description;
        TextView price;
        RelativeLayout parentLayout;
        Button btnCompleteOrder,btnCancel;
        TextView txtQuantity;

        public ViewHolder(View itemView) {
            super(itemView);
            description = itemView.findViewById(R.id.fmenuDescription);
            price = itemView.findViewById(R.id.fitem_price);
            parentLayout = itemView.findViewById(R.id.completeRecycler);
            txtQuantity = itemView.findViewById(R.id.ftxtCompleteCount);
            btnCompleteOrder = itemView.findViewById(R.id.btn_CompleteOrder);
            btnCancel = itemView.findViewById(R.id.btn_Cancel);
        }
    }
}
