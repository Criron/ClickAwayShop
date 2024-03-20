package com.example.polit.clickawayshop;

import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;

public class DatabasePopulator {
    private FirebaseFirestore db;

    public DatabasePopulator(FirebaseFirestore db) {
        this.db = db;
    }

    public DatabasePopulator addUsers() {
        List<User> users = new ArrayList<>();
        users.add(new User("Thanos", "politis@gmail.com"));
        users.add(new User("Marios", "papaggelis@gmail.com"));
        users.add(new User("Giorgos", "martinos@gmail.com"));

        Iterator<User> iterator = users.iterator();

        while(iterator.hasNext()) {
            User user = iterator.next();
            this.db.collection("users").add(user)
            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Log.d("TGM", "User " + documentReference.getId()
                            + " was successfully added.");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("TGM", "Something went wrong: " + e);
                }
            });
        }

        return this;
    }

    public DatabasePopulator addBooks() {
        List<Book> books = new ArrayList<>();
        books.add(new Book("A song of ice and fire", 19.99));
        books.add(new Book("Call of Cthulhu", 14.99));
        books.add(new Book("Happy tree friends", 12.00));
        books.add(new Book("Necronomicon", 25.50));
        books.add(new Book("Fifty Shades of Grey", 5.00));
        books.add(new Book("At the Witch house", 9.99));

        Iterator<Book> iterator = books.iterator();

        while(iterator.hasNext()) {
            Book book = iterator.next();
            this.db.collection("books").add(book)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d("TGM", "Book " + documentReference.getId()
                            + " was successfully added.");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("TGM", "Something went wrong: " + e);
                }
            });
        }

        return this;
    }

    public DatabasePopulator addStocks() {
        CollectionReference booksRef = this.db.collection("books");
        CollectionReference storesRef = this.db.collection("stores");
        CollectionReference stocksRef = this.db.collection("stocks");

        storesRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            for(QueryDocumentSnapshot document : task.getResult()) {
                                if(document.exists()) {
                                    Store store = document.toObject(Store.class);
                                    String storeName = store.getName();

                                    booksRef.get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if(task.isSuccessful()) {
                                                Map<String, Integer> stockMap = new HashMap<>();
                                                for(QueryDocumentSnapshot document : task.getResult()) {
                                                    if(document.exists()) {
                                                        Book book = document.toObject(Book.class);
                                                        stockMap.put(book.getTitle(), 5);
                                                    }
                                                }

                                                Stock stock = new Stock(stockMap, storeName);
                                                stocksRef.add(stock);
                                            } else {
                                                Log.d("TGM", "Something went wrong: "
                                                        + task.getException());
                                            }
                                        }
                                    });
                                }
                            }
                        } else {
                            Log.d("TGM", "Something went wrong: " + task.getException());
                        }
                    }
                });

        return this;
    }

    public DatabasePopulator createCartForUsers() {
        CollectionReference usersRef = db.collection("users");
        CollectionReference cartRef = db.collection("carts");

        usersRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for(QueryDocumentSnapshot userDoc : task.getResult()) {
                        if(userDoc.exists()) {
                            User user = userDoc.toObject(User.class);
                            List<Book> booksInCart = new ArrayList<>();
                            Cart cart = new Cart(booksInCart, user.getEmail());
                            cartRef.add(cart);
                        } else {
                            Log.d("TGM", "Document does not exist!");
                        }
                    }
                } else {
                    Log.d("TGM", "Something went wrong: " + task.getException());
                }
            }
        });

        return this;
    }
}
