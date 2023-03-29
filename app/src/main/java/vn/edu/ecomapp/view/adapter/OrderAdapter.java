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
import vn.edu.ecomapp.model.Order;
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
        setId(order);
        setDate(order);
        setAddress(order);
        setPaymentMethod(order);
        setQuantityProduct(order);
        setTotalPrice(order);
        holder.itemView.setOnClickListener(view -> listener.onItemClick(position, view));
    }

    @Override
    public int getItemCount() {
        if(orders == null) return 0;
        return orders.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
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
    }

    private void setId(Order order) {

    }
    private void setDate(Order order) {

    }
    private void setAddress(Order order) {}
    private void setStatus(Order order) {}
    private void setPaymentMethod(Order order) {}
    private void setQuantityProduct(Order order) {

    }
    private void setTotalPrice(Order order) {}
}
