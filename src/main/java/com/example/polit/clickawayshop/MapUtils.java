package com.example.polit.clickawayshop;

import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;

public class MapUtils {
    public static Marker addMarker(GoogleMap googleMap, Store store) {
        String title = store.getName();
        LatLng position = new LatLng(store.getLatitude(), store.getLongitude());

        return googleMap.addMarker(new MarkerOptions().position(position).title(title));
    }

    public static void populateMapWithStores(GoogleMap googleMap,
                                             CollectionReference collectionReference) {
        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for(QueryDocumentSnapshot document : task.getResult()) {
                        if(document.exists()) {
                            Store store = document.toObject(Store.class);
                            addMarker(googleMap, store);
                        }
                    }
                } else {
                    Log.d("TGM", "Something went wrong: " + task.getException());
                }
            }
        });
    }
}
