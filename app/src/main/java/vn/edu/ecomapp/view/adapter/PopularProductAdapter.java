package vn.edu.ecomapp.view.adapter;

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
import vn.edu.ecomapp.model.Product;

public class PopularProductAdapter  extends RecyclerView.Adapter<PopularProductAdapter.ViewHolder> {

   Context context;
   List<Product> products;

    public PopularProductAdapter(Context context, List<Product> products) {
        this.context = context;
        this.products = products;
    }

    @NonNull
    @Override
    public PopularProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(this.context).inflate(R.layout.list_product_popular_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PopularProductAdapter.ViewHolder holder, int position) {
        final Product product = this.products.get(position);
        holder.textViewName.setText((CharSequence) product.getProductName());
        holder.textViewPrice.setText((CharSequence) String.format("%d", product.getNewPrice()));
    }

    @Override
    public int getItemCount() {
        return this.products.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName;
        ImageView imageViewPreview;
        TextView textViewPrice;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.text_view_name);
            imageViewPreview = itemView.findViewById(R.id.image_view_preview);
            textViewPrice = itemView.findViewById(R.id.text_view_price);
        }
    }
}
