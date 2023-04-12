package vn.edu.ecomapp.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import vn.edu.ecomapp.R;
import vn.edu.ecomapp.dto.Order;
import vn.edu.ecomapp.view.adapter.listener.OnItemClickListener;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder>{

    private OnItemClickListener listener;

    Context context;

    List<Order> orders;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
    public OrderAdapter(Context context, List<Order> orders) {
        this.context = context;
        this.orders = orders;
    }

    @NonNull
    @Override
    public OrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         View view = LayoutInflater.from(context).inflate(R.layout.list_order_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderAdapter.ViewHolder holder, int position) {
        final Order order = orders.get(position);
        if(order == null) return;
        holder.setId(order);
        holder.setDate(order);
        holder.setAddress(order);
        holder.setPaymentMethod(order);
        holder.setQuantityProduct(order);
        holder.setTotalPrice(order);
        holder.setStatus(order);
        holder.setOnClickListener(position, holder.itemView);
    }

    @Override
    public int getItemCount() {
        if(orders == null) return 0;
        return orders.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvId, tvDate, tvAddress, tvStatus, tvPaymentMethod, tvQuantityProduct,tvTotal;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvId = itemView.findViewById(R.id.orderIdValue);
            tvDate = itemView.findViewById(R.id.dateValue);
            tvAddress = itemView.findViewById(R.id.addressValue);
            tvStatus = itemView.findViewById(R.id.statusValue);
            tvPaymentMethod = itemView.findViewById(R.id.pmValue);
            tvQuantityProduct = itemView.findViewById(R.id.quantityValue);
            tvTotal = itemView.findViewById(R.id.totalValue);
        }

        private void setId(Order order) {
            if(order == null) return;
            tvId.setText(order.getId());
        }
        public void setDate(Order order) {
            if(order.getDate() == null) return;
            tvDate.setText(order.getDate());
        }
        public void setAddress(Order order) {}
        public void setStatus(Order order) {
            if(order.getStatus() == null) return;
            tvStatus.setText(order.getStatus());
        }
        public void setPaymentMethod(Order order) {
            if (order.getMethod() == null)  return;
            tvPaymentMethod.setText(order.getMethod());
        }
        public void setQuantityProduct(Order order) {
            tvQuantityProduct.setText(String.format("%d", order.getTotal()));
        }
        public void setTotalPrice(Order order) {
        }

        public void setOnClickListener(int position, View itemView) {
            itemView.setOnClickListener(view -> listener.onItemClick(position, view) );
        }
    }
}
