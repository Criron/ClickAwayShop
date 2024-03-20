package com.example.polit.clickawayshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.Map;

public class BookActivity extends AppCompatActivity {
    TextView titleTextView;
    TextView priceTextView;
    ImageButton addToCartImageButton;

    FirebaseFirestore mDb;

    String mBookTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        titleTextView = findViewById(R.id.title_text_view);
        priceTextView = findViewById(R.id.price_text_view);
        addToCartImageButton = findViewById(R.id.add_to_cart_image_button);

        mDb = FirebaseFirestore.getInstance();

        Intent bookIntent = getIntent();
        mBookTitle = bookIntent.getStringExtra("bookTitle");
        double bookPrice = bookIntent.getDoubleExtra("bookPrice", 0);

        titleTextView.setText(mBookTitle);
        priceTextView.setText(String.valueOf(bookPrice));

        setListeners();
    }

    private void setListeners() {
        addToCartImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBookToCart();
            }
        });
    }

    private void addBookToCart() {
        CollectionReference stocksRef = mDb.collection("stocks");
        stocksRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    boolean isItemAvailable = false;
                    for(QueryDocumentSnapshot stockDoc : task.getResult()) {
                        if(stockDoc.exists()) {
                            Stock stock = stockDoc.toObject(Stock.class);
                            Map<String, Integer> availableItems = stock.getAvailableItems();
                            if(availableItems.get(mBookTitle) > 0 && !isItemAvailable) {
                                isItemAvailable = true;
                            }
                        }
                    }
                } else {
                    Log.d("TGM", "Something went wrong: " + task.getException());
                }
                CollectionReference cartsRef = mDb.collection("carts");
                cartsRef.whereEqualTo("userId", "politis@gmail.com").get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()) {
                                    for(QueryDocumentSnapshot cartDoc : task.getResult()) {
                                        if(cartDoc.exists()) {
                                            String cartId = cartDoc.getId();
                                            Cart cart = cartDoc.toObject(Cart.class);
                                            List<Book> booksInCart = cart.getBooks();
                                            CollectionReference booksRef = mDb.collection("books");
                                            booksRef.whereEqualTo("title", mBookTitle).get()
                                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                            if(task.isSuccessful()) {
                                                                for(QueryDocumentSnapshot bookDoc : task.getResult()) {
                                                                    if(bookDoc.exists()) {
                                                                        Book book = bookDoc.toObject(Book.class);
                                                                        booksInCart.add(book);
                                                                    }
                                                                }
                                                            }
                                                            cartsRef.document(cartId).update("books", booksInCart);
                                                            Toast.makeText(getApplicationContext(), "Book added to your cart!", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                        }
                                    }
                                } else {
                                    Log.d("TGM", "Something went wrong: " + task.getException());
                                }
                            }
                        });
            }
        });
    }
}