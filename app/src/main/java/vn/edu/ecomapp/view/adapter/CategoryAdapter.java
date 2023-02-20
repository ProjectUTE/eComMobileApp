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

import java.util.List;

import vn.edu.ecomapp.R;
import vn.edu.ecomapp.model.Category;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    Context context;
    List<Category> categories;

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
        holder.textViewTitle.setText((CharSequence) category.getName());
        holder.imageViewBackground.setImageResource(R.drawable.title_home);
    }

    @Override
    public int getItemCount() {
        return this.categories.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle;
        ImageView imageViewBackground;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.image_view_background);
            textViewTitle = itemView.findViewById(R.id.text_view_title);

        }
    }
}
