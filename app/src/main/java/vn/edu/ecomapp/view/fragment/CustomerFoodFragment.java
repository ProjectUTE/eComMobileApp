package vn.edu.ecomapp.view.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import vn.edu.ecomapp.R;
import vn.edu.ecomapp.model.Category;
import vn.edu.ecomapp.view.adapter.CategoryAdapter;

public class CustomerFoodFragment  extends Fragment {

    List<Category> categories;

    RecyclerView recyclerViewCategories;
  @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeData();
       loadRecyclerViewCategory(view);
    }

    private void loadRecyclerViewCategory(View view) {
       recyclerViewCategories = view.findViewById(R.id.recycler_view_category);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewCategories.setLayoutManager(linearLayoutManager);
        recyclerViewCategories.setHasFixedSize(true);
        CategoryAdapter categoryAdapter = new CategoryAdapter(getContext(), categories);
        recyclerViewCategories.setAdapter(categoryAdapter);
    }

    private void initializeData() {
      categories = new ArrayList<>();
        categories.add(new Category("1", "Coffee"));
        categories.add(new Category("2", "Drinks"));
        categories.add(new Category("3", "Cakes"));
        categories.add(new Category("4", "Beer"));
        categories.add(new Category("5", "Others"));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.fragment_customer_food, null) ;
       requireActivity().setTitle("Food");
       return  view;
    }
}


