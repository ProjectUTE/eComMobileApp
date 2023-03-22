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

    public void setData(List<Category> categories) {
          this.categories = categories;
          notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(this.context).inflate(R.layout.list_category_item_horizontal, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final  Category category = this.categories.get(position);
        if(category == null) return;
        holder.itemView.setOnClickListener(view -> listener.onItemClick(position, view));
        holder.textViewTitle.setText(category.getName());
        holder.imageViewBackground.setImageResource(R.drawable.coffee);
    }

    @Override
    public int getItemCount() {
       if(categories != null)
          return this.categories.size();
       return 0;
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
