package com.example.polit.clickawayshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecyclerView bookRecyclerView;
    Button mapButton;
    Button databaseButton;
    ImageButton userCartButton;

    FirebaseFirestore mDb;
    private FirebaseAuth mAuth;

    User dummyUser = new User("Athanasios Politis", "politis@gmail.com");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDb = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        bookRecyclerView = findViewById(R.id.book_recycler_view);
        mapButton = findViewById(R.id.map_button);
        mapButton.setVisibility(View.INVISIBLE);
        databaseButton = findViewById(R.id.database_button);
        databaseButton.setVisibility(View.INVISIBLE);
        userCartButton = findViewById(R.id.user_cart_button);

        setRecyclerAdapter();
        setListeners();
    }

    private void setListeners() {
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMap();
            }
        });

        databaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                Used only for populating the firestore with initial values
                and may not be used again.
                 */

                //new DatabasePopulator(mDb).addBooks().addStocks();
            }
        });

        userCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent userCartIntent = new Intent(getApplicationContext(), CartActivity.class);
                userCartIntent.putExtra("user", dummyUser.getEmail());
                startActivity(userCartIntent);
            }
        });
    }

    private void goToMap() {
        Intent intent = new Intent(this, MapActivity.class);
        startActivity(intent);
    }

    private void setRecyclerAdapter() {
        Log.d("TGM", "enter recycle");
        List<Book> books = new ArrayList<>();
        CollectionReference booksRef = mDb.collection("books");
        booksRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for(QueryDocumentSnapshot document : task.getResult()) {
                        if(document.exists()) {
                            Book book = document.toObject(Book.class);
                            books.add(book);
                        }
                    }
                    bookRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    bookRecyclerView.setItemAnimator(new DefaultItemAnimator());
                    bookRecyclerView.setAdapter(new BookAdapter(books) {
                        @Override
                        void doOnCartClick(Book book) {
                            String bookTitle = book.getTitle();
                            double bookPrice = book.getPrice();

                            Intent bookIntent = new Intent(getApplicationContext(), BookActivity.class);
                            bookIntent.putExtra("loggedUser", dummyUser.getName());
                            bookIntent.putExtra("bookTitle", bookTitle);
                            bookIntent.putExtra("bookPrice", bookPrice);
                            startActivity(bookIntent);
                        }
                    });
                } else {
                    Log.d("TGM", "Something went wrong: " + task.getException());
                }
            }
        });
        Log.d("TGM", "exit recycle");
    }
}