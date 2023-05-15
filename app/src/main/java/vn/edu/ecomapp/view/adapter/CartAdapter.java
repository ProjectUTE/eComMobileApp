package vn.edu.ecomapp.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.edu.ecomapp.R;
import vn.edu.ecomapp.api.ProductApi;
import vn.edu.ecomapp.dto.Product;
import vn.edu.ecomapp.retrofit.RetrofitClient;
import vn.edu.ecomapp.room.entities.CartItem;
import vn.edu.ecomapp.util.CurrencyFormat;
import vn.edu.ecomapp.util.constants.PrefsConstants;
import vn.edu.ecomapp.util.constants.UrlConstants;
import vn.edu.ecomapp.util.prefs.TokenManager;
import vn.edu.ecomapp.view.adapter.listener.OnItemClickListener;

public class CartAdapter  extends RecyclerView.Adapter<CartAdapter.ViewHolder>{
    private final String TAG = CartAdapter.class.getName();
    private Context context;
    private List<CartItem> lineItems;
    private OnItemClickListener listener;
    private ProductApi productApi;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public CartAdapter(Context context, List<CartItem> lineItems) {
        this.context = context;
        this.lineItems = lineItems;

        TokenManager tokenManager = TokenManager
                .getInstance(context.getSharedPreferences(PrefsConstants.DATA_ACCESS_TOKEN, Context.MODE_PRIVATE));
        productApi = RetrofitClient.createApiWithAuthAndContext(ProductApi.class, tokenManager, context);
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public List<CartItem> getLineItems() {
        return lineItems;
    }

    public void setLineItems(List<CartItem> lineItems) {
        this.lineItems = lineItems;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         View view = LayoutInflater.from(this.context).inflate(R.layout.list_cart_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.ViewHolder holder, int position) {
        final CartItem lineItem = lineItems.get(position);
        if(lineItem == null) return;
        holder.setId(lineItem);
        holder.setTitle(lineItem);
        holder.setPrice(lineItem);
        holder.setQuantity(lineItem);
        holder.setImageView(lineItem);
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
            tvLineItemId = itemView.findViewById(R.id.productId);
            textViewTitle = itemView.findViewById(R.id.text_view_title);
            tvPrice = itemView.findViewById(R.id.price);
            textViewQuantity = itemView.findViewById(R.id.text_view_quantity);
            plusButton = itemView.findViewById(R.id.button_plus);
            minusButton = itemView.findViewById(R.id.button_minus);
            imageView = itemView.findViewById(R.id.imageView);
        }

        public void setId(CartItem item) {
           if(item == null)  return;
           tvLineItemId.setText(item.getProductId());
        }

        public void setTitle(CartItem lineItem) {
            if(lineItem == null) return;
            textViewTitle.setText(lineItem.getName());
        }

        public void setQuantity(CartItem lineItem) {
            if(lineItem == null) return;
            @SuppressLint("DefaultLocale") String quantityStr = String.format("%d", lineItem.getQuantity());
            textViewQuantity.setText(quantityStr);
        }

        public void setPrice(CartItem lineItem) {
            if(lineItem == null) return;
            String priceStr = CurrencyFormat.VietnameseCurrency(lineItem.getPrice());
            tvPrice.setText(priceStr);
        }

        public void setImageView(CartItem lineItem) {
            if(lineItem == null) return;
            if(lineItem.getProductId().isEmpty() || lineItem.getProductId() == null) return;
            Log.d(TAG, lineItem.getProductId());
            productApi.getProductById(lineItem.getProductId()).enqueue(new Callback<Product>() {
                @Override
                public void onResponse(Call<Product> call, Response<Product> response) {
                    Log.d(TAG, "Success");
                    if(response.body() == null) return;
                    if (response.body().getMainImage() == null) return;
                    String mainImageUrl = response.body().getMainImage();
                    if(mainImageUrl.contains(UrlConstants.BASE_URL_LOCAL))
                        mainImageUrl = mainImageUrl
                                .replace(UrlConstants.BASE_URL_LOCAL, UrlConstants.BASE_URL)
                                .replace(" ", "%20");
                    Log.d(TAG, mainImageUrl);
                    Glide.with(context) .load(mainImageUrl).into(imageView);
                }

                @Override
                public void onFailure(Call<Product> call, Throwable t) {
                    Log.d(TAG, t.getMessage());
                }
            });
        }

        public void setOnClickListener(int position, View itemView) {
            itemView.setOnClickListener(view -> listener.onItemClick(position, view) );
        }
    }
}
