package com.example.polit.clickawayshop;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public abstract class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder>{
    private List<Book> mCartList;

    public CartAdapter(List<Book> cartList) {
        mCartList = cartList;
    }

    @NonNull
    @Override
    public CartAdapter.CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_item, parent, false);

        return new CartAdapter.CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.CartViewHolder holder, int position) {
        Book book = mCartList.get(position);
        String title = book.getTitle();
        String price = String.valueOf(book.getPrice());

        holder.cartTitle.setText(title);
        holder.cartPrice.setText(price);

        holder.removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doOnCartClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCartList.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {
        private TextView cartTitle;
        private TextView cartPrice;
        private Button removeButton;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);

            cartTitle = itemView.findViewById(R.id.cart_book_title);
            cartPrice = itemView.findViewById(R.id.cart_book_price);
            removeButton = itemView.findViewById(R.id.cart_remove_item);
        }
    }

    abstract void doOnCartClick(int position);
}
