package vn.edu.ecomapp.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import vn.edu.ecomapp.R;
import vn.edu.ecomapp.model.Product;

public class CartAdapter  extends RecyclerView.Adapter<CartAdapter.ViewHolder>{

    Context context;

    List<Product> products;

    public CartAdapter(Context context, List<Product> products) {
        this.context = context;
        this.products = products;
    }

    @NonNull
    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         View view = LayoutInflater.from(this.context).inflate(R.layout.list_cart_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return this.products.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle, textViewFeeEachItem, textViewTotalEachItem, textViewQuantity;
        ImageView imageView;
        Button plusButton, minusButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_view_title);
            textViewFeeEachItem = itemView.findViewById(R.id.text_view_fee_each_item);
            textViewTotalEachItem = itemView.findViewById(R.id.text_view_total_each_item);
            textViewQuantity = itemView.findViewById(R.id.text_view_quantity);
            plusButton = itemView.findViewById(R.id.button_plus);
            minusButton = itemView.findViewById(R.id.button_minus);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
