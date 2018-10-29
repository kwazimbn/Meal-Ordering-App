package com.example.a213506699.foodordering;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private static final String TAG = "CartRecyclerAdapter";
    private ArrayList<String> descriptions = new ArrayList<>();
    private ArrayList<String> prices = new ArrayList<>();
    private ArrayList<String> idList = new ArrayList<>();
    private Context ctx;
    private int count;
    int shopID;
    int qSize;


    public RecyclerViewAdapter(Context ctx,int q ,ArrayList<String> ids,int shopID, ArrayList<String> descriptions, ArrayList<String> prices) {
        this.descriptions = descriptions;
        this.prices = prices;
        this.ctx = ctx;
        this.idList = ids;
        this.shopID = shopID;
        count = 0;
        this.qSize = q;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_listitem, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        count = 0;
        Log.d(TAG, "onBindViewHolder: called");
        viewHolder.description.setText(descriptions.get(i));
        viewHolder.price.setText("R " + prices.get(i) + "0");
        // Intent intent = new Intent(a,CreateOrder.class);

        viewHolder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Clicked on " + descriptions.get(i));
               // Toast.makeText(ctx, descriptions.get(i), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ctx, CreateOrder.class);
                intent.putExtra("item", descriptions.get(i));
                intent.putExtra("itemPrice", prices.get(i));
                intent.putExtra("itemID", idList.get(i));
                intent.putExtra("shopID",shopID);
                intent.putExtra("qSize",qSize);
                ctx.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return descriptions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView description;
        TextView price;
        RelativeLayout parentLayout;
        Button btnDecrease, btnIncrease;
        TextView txtCounter;

        public ViewHolder(View itemView) {
            super(itemView);
            description = itemView.findViewById(R.id.menuDescription);
            price = itemView.findViewById(R.id.item_price);
            parentLayout = itemView.findViewById(R.id.parent_layout);
            txtCounter = itemView.findViewById(R.id.txtcount);
        }
    }

}
