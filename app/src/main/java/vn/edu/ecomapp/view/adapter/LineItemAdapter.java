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
import vn.edu.ecomapp.dto.LineItem;

public class LineItemAdapter extends RecyclerView.Adapter<LineItemAdapter.ViewHolder>{

    Context context;

    List<LineItem> lineItems;

    public LineItemAdapter(Context context, List<LineItem> lineItems) {
        this.context = context;
        this.lineItems = lineItems;
    }

    @NonNull
    @Override
    public LineItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         View view = LayoutInflater.from(context).inflate(R.layout.list_line_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LineItemAdapter.ViewHolder holder, int position) {
        final LineItem lineItem = lineItems.get(position);
        if(lineItem == null) return;
        setId(lineItem);
        setDate(lineItem);
        setAddress(lineItem);
        setPaymentMethod(lineItem);
        setQuantityProduct(lineItem);
        setTotalPrice(lineItem);
    }

    @Override
    public int getItemCount() {
        if(lineItems == null) return 0;
        return lineItems.size();
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

    private void setId(LineItem lineItem) {

    }
    private void setDate(LineItem lineItem) {

    }
    private void setAddress(LineItem lineItem) {}
    private void setStatus(LineItem lineItem) {}
    private void setPaymentMethod(LineItem lineItem) {}
    private void setQuantityProduct(LineItem lineItem) {

    }
    private void setTotalPrice(LineItem lineItem) {}
}
