package vn.edu.ecomapp.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import vn.edu.ecomapp.R;
import vn.edu.ecomapp.model.Category;
import vn.edu.ecomapp.view.adapter.listener.OnItemClickListener;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private OnItemClickListener listener;
    Context context;
    List<Category> categories;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public  CategoryAdapter(Context context, List<Category> categories) {
        this.context = context;
        this.categories = categories;
    }

    @NonNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(this.context).inflate(R.layout.list_category_item, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final  Category category = this.categories.get(position);
        if(category == null) return;
        holder.setTitle(category);
        holder.setImage();
        holder.setOnClickListener(position, holder.itemView);
        holder.setId(category);
    }

    @Override
    public int getItemCount() {
       if(categories != null)
          return this.categories.size();
       return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle, categoryId;
        ImageView imageViewBackground;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.image_view_background);
            textViewTitle = itemView.findViewById(R.id.text_view_title);
            categoryId = itemView.findViewById(R.id.categoryId);
        }

        public void setTitle(Category category) {
            if (category == null) return;
            textViewTitle.setText(category.getName());
        }

        public void setId(Category category) {
            if(category == null) return;
            categoryId.setText(category.getId());
        }

        public void setImage() {
            String url = "https://product.hstatic.net/1000075078/product/1655348107_mochi-choco_9694871c490b426ab333810d17261c96_large.jpg";
            if(context != null)
                Glide.with(context).load(url).into(imageViewBackground);
        }

        public void setOnClickListener(int position, View itemView) {
            itemView.setOnClickListener(view -> listener.onItemClick(position, view) );
        }
    }
}
