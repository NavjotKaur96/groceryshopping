package com.example.grocery_shopping_cart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.icu.text.Transliterator;
import android.media.Image;
import android.nfc.cardemulation.CardEmulation;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private FirebaseFirestore firebaseFirestore;
    private RecyclerView recyclerView;
    private FirestoreRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        firebaseFirestore =FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.recyclerView);
        Query query = firebaseFirestore.collection("Products");

        FirestoreRecyclerOptions<GroceryModel> options = new FirestoreRecyclerOptions.Builder<GroceryModel>()
                .setQuery(query,GroceryModel.class)
                .build();
        adapter = new FirestoreRecyclerAdapter<GroceryModel, ItemViewHolder>(options) {
            @NonNull
            @Override
            public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_recyclerview,parent,false);
                return new ItemViewHolder(v);
            }

            @Override
            protected void onBindViewHolder(@NonNull ItemViewHolder holder, int position, @NonNull GroceryModel model) {
                Picasso.get().load(model.getImage()).into(holder.Image);
                holder.Name.setText(model.getName());
                holder.Price.setText(model.getPrice());
                holder.Quantity.setText(model.getQuantity());
                holder.AddtoCart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(view.getContext(),QuantityPage.class);
                        intent.putExtra("Image",model.getImage());
                        intent.putExtra("Price",model.getPrice());
                        intent.putExtra("Name",model.getName());
                        startActivity(intent);

                    }
                });

            }
        };

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        recyclerView.setAdapter(adapter);

    }
    private class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView Price,Quantity,Name;
        private ImageView Image;
        private Button AddtoCart,Cart;
        String id;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            Price = itemView.findViewById(R.id.Price_detail);
            Quantity = itemView.findViewById(R.id.quantity);
            Image = itemView.findViewById(R.id.Image_detail);
            AddtoCart = itemView.findViewById(R.id.Buybtn);
            Cart = itemView.findViewById(R.id.cart);
            Name = itemView.findViewById(R.id.Name);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }
}