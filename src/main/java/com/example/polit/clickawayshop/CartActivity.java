package com.example.polit.clickawayshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CartActivity extends AppCompatActivity {
    RecyclerView cartRecyclerView;
    Button orderButton;

    FirebaseFirestore mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        cartRecyclerView = findViewById(R.id.cart_recycler_view);
        orderButton = findViewById(R.id.order_button);

        mDb = FirebaseFirestore.getInstance();

        setListeners();
        setRecyclerAdapter();
    }

    public void setListeners() {
        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timeFragment = new TimePickerFragment();
                timeFragment.show(getSupportFragmentManager(), "timePicker");
                DialogFragment dateFragment = new DatePickerFragment();
                dateFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });
    }

    public void setRecyclerAdapter() {
        List<Book> cartBooks = new ArrayList<>();
        CollectionReference cartsRef = mDb.collection("carts");
        cartsRef.whereEqualTo("userId", "politis@gmail.com").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for(QueryDocumentSnapshot cartDoc : task.getResult()) {
                        if(cartDoc.exists()) {
                            Cart cart = cartDoc.toObject(Cart.class);
                            Iterator it = cart.getBooks().iterator();
                            while(it.hasNext()) {
                                Book book = (Book) it.next();
                                cartBooks.add(book);
                            }
                        }
                    }
                }
                cartRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                cartRecyclerView.setItemAnimator(new DefaultItemAnimator());
                cartRecyclerView.setAdapter(new CartAdapter(cartBooks) {
                    @Override
                    void doOnCartClick(int position) {
                        cartBooks.remove(position);
                        cartsRef.whereEqualTo("userId", "politis@gmail.com").get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if(task.isSuccessful()) {
                                            for(QueryDocumentSnapshot document : task.getResult()) {
                                                if(document.exists()) {
                                                    cartsRef.document(document.getId())
                                                            .update("books", cartBooks);
                                                    Toast.makeText(getApplicationContext(), "Book was removed from the list",
                                                            Toast.LENGTH_SHORT).show();
                                                    updateUI();
                                                }
                                            }
                                        }
                                    }
                                });
                    }
                });
            }
        });
    }

    private void updateUI() {
        Intent updateIntent = new Intent(this, CartActivity.class);
        startActivity(updateIntent);
        this.onDestroy();
    }
}