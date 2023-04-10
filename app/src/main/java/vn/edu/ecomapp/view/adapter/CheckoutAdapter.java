package vn.edu.ecomapp.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import vn.edu.ecomapp.R;
import vn.edu.ecomapp.model.LineItem;
import vn.edu.ecomapp.model.PaymentMethod;
import vn.edu.ecomapp.util.CurrencyFormat;
import vn.edu.ecomapp.view.adapter.listener.OnItemClickListener;

public class CheckoutAdapter extends RecyclerView.Adapter<CheckoutAdapter.ViewHolder> {

    private OnItemClickListener listener;
    Context context;
    List<LineItem> lineItems;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }


    public CheckoutAdapter(Context context, List<LineItem> lineItems) {
        this.context = context;
        this.lineItems = lineItems;
    }

    @NonNull
    @Override
    public CheckoutAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(this.context).inflate(R.layout.list_checkout_item, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final LineItem lineItem = lineItems.get(position);
        if(lineItem == null) return;
        holder.setName(lineItem);
        holder.setQuantity(lineItem);
        holder.setTotal(lineItem);
        holder.setOnClickListener(position, holder.itemView);
    }

    @Override
    public int getItemCount() {
       if(lineItems != null)
           return lineItems.size();
       return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvQuantity, tvTotal;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.name);
            tvQuantity = itemView.findViewById(R.id.quantity);
            tvTotal = itemView.findViewById(R.id.total);
        }

        public void setName(LineItem item) {
           if (item == null) return;
           tvName.setText(item.getName());
        }

        public void setQuantity(LineItem item) {
            if (item == null) return;
            tvQuantity.setText(String.format("%d", item.getQuantity()));
        }

        public void setTotal(LineItem item) {
            if (item == null) return;
            tvTotal.setText(CurrencyFormat.VietnameseCurrency(item.getAmount()));
        }

        public void setOnClickListener(int position, View itemView) {
            itemView.setOnClickListener(view -> listener.onItemClick(position, view) );
        }
    }
}
