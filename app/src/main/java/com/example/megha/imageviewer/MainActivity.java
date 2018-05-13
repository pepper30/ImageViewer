package com.example.megha.imageviewer;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.imageList)
    RecyclerView imageList;

    List<Details> detailsList;
    private FirebaseFirestore db;
    private ListenerRegistration listenerRegistration;
    private RecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        detailsList=new ArrayList<>();
        db=FirebaseFirestore.getInstance();
        ButterKnife.bind(this);

        loadImages();
        listenerRegistration=db.collection("images")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                        final List<Details> list=new ArrayList<>();
                        for(DocumentSnapshot doc:queryDocumentSnapshots){
                            Details details=doc.toObject(Details.class);
                            list.add(details);
                        }

                        adapter=new RecyclerViewAdapter(list, db, getApplicationContext(), new ClickHandler() {
                            @Override
                            public void onMyButtonClicked(int position) {
//                                Log.d("Position", list.get(position).toString());
                                DocumentReference ref = db.collection("images").document(Integer.toString(position + 1));
                                Log.d("Reference", ref.getPath());
                                ref.update("likes",list.get(position).getLikes() + 1)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(MainActivity.this,"Liked",Toast.LENGTH_LONG).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(MainActivity.this,"Failed",Toast.LENGTH_LONG).show();
                                            }
                                        });
                                 }
                        });
                        imageList.setAdapter(adapter);
                    }
                });


    }


    private void loadImages(){
        db.collection("images")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){

                            final List<Details> detailsList = new ArrayList<>();
                            for(DocumentSnapshot doc:task.getResult()){
                                Details details=doc.toObject(Details.class);

                                detailsList.add(details);
//                                Log.d("TAG2", details.toString());
                            }

                            adapter = new RecyclerViewAdapter(detailsList,db,getApplicationContext(),new ClickHandler() {
                                @Override
                                public void onMyButtonClicked(int position) {
                                    DocumentReference ref = db.collection("images").document(Integer.toString(position + 1));
                                    Log.d("Reference", ref.getPath());
                                    ref.update("likes",detailsList.get(position).getLikes() + 1)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(MainActivity.this,"Liked",Toast.LENGTH_LONG).show();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(MainActivity.this,"Failed",Toast.LENGTH_LONG).show();
                                                }
                                            });

                                }
                            });
//                            adapter=new RecyclerViewAdapter(detailsList,db,getApplicationContext());
                            RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
                            imageList.setLayoutManager(layoutManager);
                            imageList.setItemAnimator(new DefaultItemAnimator());
                            adapter.notifyDataSetChanged();
                            imageList.setAdapter(adapter);

                        }
                        else {
                            Log.d("TAG","",task.getException());
                        }
                    }
                });
    }
}