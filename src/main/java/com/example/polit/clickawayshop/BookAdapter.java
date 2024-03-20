package com.example.polit.clickawayshop;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.RecyclerView;

public abstract class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {
    private List<Book> mBookList;

    public BookAdapter(List<Book> bookList) {
        mBookList = bookList;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.book_item, parent, false);

        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = mBookList.get(position);
        String title = book.getTitle();
        String price = String.valueOf(book.getPrice());

        holder.bookTitle.setText(title);
        holder.bookPrice.setText(price);

        holder.bookRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doOnCartClick(book);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mBookList.size();
    }

    public class BookViewHolder extends RecyclerView.ViewHolder {
        private View bookRow;
        private TextView bookTitle;
        private TextView bookPrice;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);

            bookRow = itemView.findViewById(R.id.book_row);
            bookTitle = itemView.findViewById(R.id.book_title);
            bookPrice = itemView.findViewById(R.id.book_price);
        }
    }

    abstract void doOnCartClick(Book book);
}
