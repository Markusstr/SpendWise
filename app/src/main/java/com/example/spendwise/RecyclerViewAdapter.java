package com.example.spendwise;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private ArrayList<DataClass> mDataList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onCloseClick(int position);
    }

    void setOnItemClickListener(OnItemClickListener listener) { mListener = listener; }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mProductImage;
        ImageButton mClose;
        TextView mProductName;
        TextView mProductPrice;


        public ViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            mProductImage = itemView.findViewById(R.id.card_product_image);
            mClose = itemView.findViewById(R.id.card_close);
            mProductName = itemView.findViewById(R.id.card_product_name);
            mProductPrice = itemView.findViewById(R.id.card_product_price);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });

            mClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onCloseClick(position);
                        }
                    }
                }
            });
        }
    }

    RecyclerViewAdapter(ArrayList<DataClass> mDataList) { this.mDataList = mDataList; }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_card, parent, false);
        return new ViewHolder(v, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DataClass currentItem = mDataList.get(position);

        holder.mProductImage.setImageBitmap(currentItem.getProductImage());
        holder.mProductName.setText(currentItem.getProductName());
        holder.mProductPrice.setText(currentItem.getProductPrice());
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }
}
