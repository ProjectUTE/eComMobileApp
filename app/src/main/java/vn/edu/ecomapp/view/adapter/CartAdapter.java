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
import vn.edu.ecomapp.model.LineItem;
import vn.edu.ecomapp.util.CurrencyFormat;
import vn.edu.ecomapp.view.adapter.listener.OnItemClickListener;

public class CartAdapter  extends RecyclerView.Adapter<CartAdapter.ViewHolder>{

    Context context;

    List<LineItem> lineItems;

    private OnItemClickListener listener;
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public CartAdapter(Context context, List<LineItem> lineItems) {
        this.context = context;
        this.lineItems = lineItems;
    }

    @NonNull
    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         View view = LayoutInflater.from(this.context).inflate(R.layout.list_cart_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.ViewHolder holder, int position) {
        final LineItem lineItem = lineItems.get(position);
        if(lineItem == null) return;
        holder.setId(lineItem);
        holder.setTitle(lineItem);
        holder.setPrice(lineItem);
        holder.setQuantity(lineItem);
        holder.setOnClickListener(position, holder.itemView);

    }

    @Override
    public int getItemCount() {
        if(lineItems == null) return 0;
        return  lineItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle, tvPrice, textViewQuantity, tvLineItemId;
        ImageView imageView;
        TextView plusButton, minusButton;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvLineItemId = itemView.findViewById(R.id.lineItemId);
            textViewTitle = itemView.findViewById(R.id.text_view_title);
            tvPrice = itemView.findViewById(R.id.price);
            textViewQuantity = itemView.findViewById(R.id.text_view_quantity);
            plusButton = itemView.findViewById(R.id.button_plus);
            minusButton = itemView.findViewById(R.id.button_minus);
            imageView = itemView.findViewById(R.id.imageView);
        }

        public void setId(LineItem item) {
           if(item == null)  return;
           tvLineItemId.setText(item.getId());
        }

        public void setTitle(LineItem lineItem) {
            if(lineItem == null) return;
            textViewTitle.setText(lineItem.getName());
        }

        public void setQuantity(LineItem lineItem) {
            if(lineItem == null) return;
            @SuppressLint("DefaultLocale") String quantityStr = String.format("%d", lineItem.getQuantity());
            textViewQuantity.setText(quantityStr);
        }

        public void setPrice(LineItem lineItem) {
            if(lineItem == null) return;
            String priceStr = CurrencyFormat.VietnameseCurrency(lineItem.getPrice());
            tvPrice.setText(priceStr);
        }

        public void setOnClickListener(int position, View itemView) {
            itemView.setOnClickListener(view -> listener.onItemClick(position, view) );
        }
    }
}
