package com.example.a213506699.foodordering;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class CartRecyclerViewAdapter extends RecyclerView.Adapter<CartRecyclerViewAdapter.ViewHolder> {

    ArrayList<String> cartItemDescriptions;
    ArrayList<Double> cartPrices;
    ArrayList<Integer> cartQuantities;
    ArrayList<Integer> menuItemIDs;
    Context ctx;


    public CartRecyclerViewAdapter(Context ctx, ArrayList<Integer> menuItemIDs, ArrayList<String> cartItemDescriptions, ArrayList<Double> cartPrices, ArrayList<Integer> cartQuantities) {
        this.cartItemDescriptions = cartItemDescriptions;
        this.cartPrices = cartPrices;
        this.cartQuantities = cartQuantities;
        this.ctx = ctx;
        this.menuItemIDs = menuItemIDs;
    }


    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_cartlist, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        Log.d(TAG, "onBindViewHolder: called");
        viewHolder.description.setText(cartItemDescriptions.get(i));
        viewHolder.price.setText("R " + Double.toString(cartPrices.get(i)));
        viewHolder.txtQuantity.setText(Integer.toString(cartQuantities.get(i)));
        // Intent intent = new Intent(a,CreateOrder.class);
        final int myValue = menuItemIDs.get(i);
        viewHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = "";
                RemoveItem removeItem = new RemoveItem(ctx, myValue);
                try {
                cartItemDescriptions.remove(i);
                cartPrices.remove(i);
                cartQuantities.remove(i);
                menuItemIDs.remove(i);
                removeItem.execute("");
                notifyItemRemoved(i);
                notifyItemRangeChanged(i, getItemCount());
                if(cartPrices.size()==0){
                    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(ctx);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putInt("returning",0);
                    editor.commit();
                }

                message = cartItemDescriptions.get(i) + " deleted";
            }catch (Exception e){
                    message = " error while removing item in recycler view";
                    Log.e(TAG, "onClick: "+ e.getMessage() );
                }
                Toast.makeText(ctx,message,Toast.LENGTH_LONG);

            }
        });

    }

    @Override
    public int getItemCount() {
        return cartItemDescriptions.size();
    }

    /**
     * ViewHandler Class
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView description;
        TextView price;
        RelativeLayout parentLayout;
        Button btnDelete;
        TextView txtQuantity;

        public ViewHolder(View itemView) {
            super(itemView);
            description = itemView.findViewById(R.id.cmenuDescription);
            price = itemView.findViewById(R.id.citem_price);
            parentLayout = itemView.findViewById(R.id.cparent_layout);
            txtQuantity = itemView.findViewById(R.id.ctxtquantity);
            btnDelete = itemView.findViewById(R.id.cbtn_deleteItem);
        }
    }
}