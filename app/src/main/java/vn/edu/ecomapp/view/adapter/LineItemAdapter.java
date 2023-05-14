package vn.edu.ecomapp.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import vn.edu.ecomapp.R;
import vn.edu.ecomapp.api.ProductApi;
import vn.edu.ecomapp.dto.lineitem.LineItemResponse;
import vn.edu.ecomapp.retrofit.RetrofitClient;
import vn.edu.ecomapp.util.CurrencyFormat;
import vn.edu.ecomapp.util.constants.PrefsConstants;
import vn.edu.ecomapp.util.prefs.TokenManager;

public class LineItemAdapter extends RecyclerView.Adapter<LineItemAdapter.ViewHolder>{

    Context context;
    List<LineItemResponse> lineItems;

    TokenManager tokenManager;

    ProductApi productApi;

    public LineItemAdapter(Context context, List<LineItemResponse> lineItems) {
        this.context = context;
        this.lineItems = lineItems;
        tokenManager = TokenManager
                .getInstance(context.getSharedPreferences(PrefsConstants.DATA_ACCESS_TOKEN, Context.MODE_PRIVATE));
        productApi = RetrofitClient.createApiWithAuth(ProductApi.class, tokenManager);
    }

    @NonNull
    @Override
    public LineItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         View view = LayoutInflater.from(context).inflate(R.layout.list_line_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LineItemAdapter.ViewHolder holder, int position) {
        final LineItemResponse lineItem = lineItems.get(position);
        if(lineItem == null) return;
        holder.setId(lineItem);
        holder.setProductName(lineItem);
        holder.setQuantityProduct(lineItem);
        holder.setTotalPrice(lineItem);
    }

    @Override
    public int getItemCount() {
        if(lineItems == null) return 0;
        return lineItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvId, tvProductName, tvQuantityProduct,tvTotal;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvId = itemView.findViewById(R.id.lineItemIdValue);
            tvProductName = itemView.findViewById(R.id.productNameValue);
            tvQuantityProduct = itemView.findViewById(R.id.quantityValue);
            tvTotal = itemView.findViewById(R.id.totalValue);
        }

        public void setId(LineItemResponse lineItem) {
            if(lineItem == null) return;
            tvId.setText(lineItem.getId());
        }
        public void setProductName(LineItemResponse lineItem) {
            Log.d("TAG ProductName", "HERE");
            if(lineItem == null) return;
            tvProductName.setText(lineItem.getProductName());
        }
        @SuppressLint("SetTextI18n")
        public void setQuantityProduct(LineItemResponse lineItem) {
            if(lineItem == null) return;
            tvQuantityProduct.setText(Integer.toString(lineItem.getQuantity()));
        }
        public void setTotalPrice(LineItemResponse lineItem) {
            tvTotal.setText(CurrencyFormat.VietnameseCurrency(lineItem.getAmount()));
        }
    }


}
