package com.example.scheactim;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.scheactim.adaptadores.AdaptadorActividades;
import com.example.scheactim.data.ActividadesRepo;
import com.example.scheactim.helpers.eventos.ItemTapListener;
import com.example.scheactim.modelos.ModeloActividades;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity implements ItemTapListener {

    private static final String TAG = MainActivity.class.getName();
    public static final String FULLNAME_KEY = "FULLNAME";
    public static final String EMAIL_KEY = "EMAIL";

    private ActividadesRepo mPointsRepository;
    private List<ModeloActividades> mModelList;
    private AdaptadorActividades mPointsAdapter;

    private ViewGroup rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Log.e(TAG, "Entro Main Activity");
        setup();

    }
    private void setup() {
        Log.e(TAG, "Entro en setup");
        mPointsRepository = new ActividadesRepo(getBaseContext());
        mModelList = new ArrayList<>();

        Intent startIntent = getIntent();
        if(startIntent == null) {
            Toast.makeText(
                    this,
                    "Algo salió mal al obtener los datos :(",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        String fullname = startIntent.getStringExtra(FULLNAME_KEY);
        if(TextUtils.isEmpty(fullname)) {
            fullname = "Usuario";
        }
        if(getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getString(R.string.welcome_user_title, fullname));
        }

        String email = startIntent.getStringExtra(EMAIL_KEY);
        if(TextUtils.isEmpty(email)) {
            Log.e(TAG, "Aqui es antes de on resume ");
            Toast.makeText(
                    this,
                    R.string.cannot_get_email,
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        rootView = findViewById(R.id.ly_root);
        setupPointListView();
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "Entro a la ON resume");
        loadData();
    }

    private void setupPointListView() {

        Log.e(TAG, "EntrosetupPointListView para llenaer");
        RecyclerView rvPoints = findViewById(R.id.rv_points);
        mPointsAdapter = new AdaptadorActividades(mModelList, this);
        rvPoints.setAdapter(mPointsAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getBaseContext());
        rvPoints.setLayoutManager(layoutManager);
        rvPoints.setHasFixedSize(true);
    }

    private void loadData() {
        Log.e(TAG, "Entro a LoadData");
        if(!mModelList.isEmpty()) {
            Log.d(TAG, "Ya existen valores en la lista");
            return;
        }
        if(mPointsRepository == null) {
            Log.e(TAG, "mPointsRepository no debería ser null");
            return;
        }
        mModelList = mPointsRepository.getAll();
        mPointsAdapter.updateList(mModelList);
    }

    @Override
    public void onItemTap(View view, int position) {
        showMessageWithSelectedItem(position);
    }

    private void showMessageWithSelectedItem(int position) {
        if(mModelList == null) {
            Log.e(TAG, "invalid mModelList");
            return;
        }
        if(position > mModelList.size()) {
            Log.e(TAG, "invalid position");
            return;
        }

        ModeloActividades selectedItemModel = mModelList.get(position);
        Snackbar.make(rootView,
                String.format(Locale.getDefault(),
                        "Has seleccionado %s", selectedItemModel.getNombre()
                ),
                Snackbar.LENGTH_LONG
        ).show();
    }

}


