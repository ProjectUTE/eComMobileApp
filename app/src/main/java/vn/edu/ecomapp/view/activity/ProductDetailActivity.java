package vn.edu.ecomapp.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import vn.edu.ecomapp.R;
import vn.edu.ecomapp.model.ImagePreview;
import vn.edu.ecomapp.view.adapter.ImagePreviewAdapter;
import vn.edu.ecomapp.view.adapter.decorator.SpacesItemDecoration;

public class ProductDetailActivity extends AppCompatActivity {

    RecyclerView imagePreviewsRecycerView;
    List<ImagePreview> imagePreviews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        initData();
        loadImagePreviews();
    }

    private void loadImagePreviews() {
        imagePreviewsRecycerView = findViewById(R.id.recyclerViewImagesPreview);
        ImagePreviewAdapter adapter = new ImagePreviewAdapter(imagePreviews, ProductDetailActivity.this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ProductDetailActivity.this, LinearLayoutManager.HORIZONTAL, false);
        imagePreviewsRecycerView.setAdapter(adapter);
        imagePreviewsRecycerView.setLayoutManager(linearLayoutManager);
        imagePreviewsRecycerView.addItemDecoration(new SpacesItemDecoration(40));
    }

    private void initData() {
        imagePreviews = new ArrayList<>();
        imagePreviews.add(new ImagePreview("1", "url1"));
        imagePreviews.add(new ImagePreview("1", "url1"));
        imagePreviews.add(new ImagePreview("1", "url1"));
        imagePreviews.add(new ImagePreview("1", "url1"));
        imagePreviews.add(new ImagePreview("1", "url1"));
    }
}