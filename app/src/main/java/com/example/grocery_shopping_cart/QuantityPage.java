package com.example.grocery_shopping_cart;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class QuantityPage extends AppCompatActivity {

    TextView ItemPrice,TotalPrice,quantity,Name;
    FloatingActionButton button;
    ImageButton Increment,Decrement;
    ImageView AddtoCartImage;
    Button AddtoCart;
    int count =0;
    private FirebaseFirestore firebaseFirestore;
    FirebaseStorage firebaseStorage;
    CollectionReference collectionReference;
    private Uri uri;
    ProgressDialog progressDialog;
    GroceryModel model = new GroceryModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quantity_page);

        button = findViewById(R.id.cart);
        AddtoCartImage = (ImageView) findViewById(R.id.AddCartImage);
        ItemPrice = (TextView) findViewById(R.id.ItemPrice);
        TotalPrice = (TextView) findViewById(R.id.TotalPrice);
        quantity = (TextView) findViewById(R.id.quantity);
        Name = (TextView) findViewById(R.id.name);
        Increment = (ImageButton) findViewById(R.id.increment);
        Decrement = (ImageButton) findViewById(R.id.decrement);
        AddtoCart = (Button) findViewById(R.id.addToCart);

        progressDialog = new ProgressDialog(this);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        collectionReference =firebaseFirestore.collection("MyCart");

        Intent intent = getIntent();
        Picasso.get().load(intent.getStringExtra("Image")).into(AddtoCartImage);
        String Price = intent.getExtras().getString("Price");
        String name = intent.getExtras().getString("Name");
        ItemPrice.setText(Price);
        TotalPrice.setText(Price);
        Name.setText(name);


       button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(),CartDescription.class);
                startActivity(i);
            }
        });
        AddtoCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UploadData();
            }
        });


    }

    private void UploadData() {
        progressDialog.setTitle("Adding item to cart...");
        progressDialog.show();

        String Image = AddtoCartImage.toString();
        String Price = ItemPrice.getText().toString();
        String totalPrice = TotalPrice.getText().toString();
        String Amount = quantity.getText().toString();
        String ItemName = Name.getText().toString();

        String id = UUID.randomUUID().toString();

        Map<String,Object> doc = new HashMap<>();
        doc.put("Image",Image);
        doc.put("Price",Price);
        doc.put("TotalPrice",totalPrice);
        doc.put("Amount",Amount);
        doc.put("Name",ItemName);

        firebaseFirestore.collection("MyCart").document(id).set(doc)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressDialog.dismiss();
                        Toast.makeText(QuantityPage.this,"Added to Cart",Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(QuantityPage.this,"Error"+e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
    }


    public void increment(View view){
        count++;
        quantity.setText(""+count);
        double Price = Double.parseDouble(ItemPrice.getText().toString());
        Price = Price *count;
        TotalPrice.setText(String.valueOf(Price));
    }

    public void decrement(View view){
        if(count <= 0) count=0;
        else count--;
        quantity.setText(""+count);
        double Price = Double.parseDouble(ItemPrice.getText().toString());
        Price = Price *count;
        TotalPrice.setText(String.valueOf(Price));
    }
}