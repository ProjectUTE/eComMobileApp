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
import vn.edu.ecomapp.model.PaymentMethod;
import vn.edu.ecomapp.view.adapter.listener.OnItemClickListener;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.ViewHolder> {

    private OnItemClickListener listener;
    Context context;
    List<PaymentMethod> paymentMethods;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public PaymentAdapter(Context context, List<PaymentMethod> paymentMethods) {
        this.context = context;
        this.paymentMethods = paymentMethods;
    }

    @NonNull
    @Override
    public PaymentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(this.context).inflate(R.layout.list_payment_item, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final PaymentMethod pm = paymentMethods.get(position);
        if(pm == null) return;
        holder.setName(pm);
        holder.setId(pm);
        holder.setOnClickListener(position, holder.itemView);
    }

    @Override
    public int getItemCount() {
       if(paymentMethods != null)
           return  paymentMethods.size();
       return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvId;
        ImageView ivImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.name);
            tvId = itemView.findViewById(R.id.tvId);
            ivImage = itemView.findViewById(R.id.image);
        }

        public void setName(PaymentMethod pm) {
           if (pm == null) return;
           tvName.setText(pm.getName());
        }

        public void setId(PaymentMethod pm) {
            if (pm == null) return;
            tvId.setText(pm.getId());
        }

        public void setImage(PaymentMethod pm) {
            if (pm == null) return;
        }

        public void setOnClickListener(int position, View itemView) {
            itemView.setOnClickListener(view -> listener.onItemClick(position, view) );
        }
    }
}
