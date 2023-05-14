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

import vn.edu.ecomapp.R;
import vn.edu.ecomapp.dto.Product;
import vn.edu.ecomapp.util.CurrencyFormat;
import vn.edu.ecomapp.util.constants.UrlConstants;
import vn.edu.ecomapp.view.adapter.listener.OnItemClickListener;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private final String TAG = ProductAdapter.class.getName();
   Context context;
   List<Product> products;

   private OnItemClickListener listener;
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public ProductAdapter(Context context, List<Product> products) {
        this.context = context;
        this.products = products;
    }

    @NonNull
    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(this.context).inflate(R.layout.list_product_item, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ViewHolder holder, int position) {
        final Product product = this.products.get(position);
        if (product == null) return;
        holder.setPrice(product);
        holder.setName(product);
        holder.setId(product);
        holder.setDesc(product);
        holder.setImage(product);
        holder.setOnClickListener(position, holder.itemView);
    }

    @Override
    public int getItemCount() {
        if(products != null)
            return this.products.size();
        return  0;
    }

    public  class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName, tvProductId, tvCost;
        ImageView imageViewPreview;
        TextView textViewDescription;
        TextView viewDetailButton;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductId = itemView.findViewById(R.id.tvProductId);
            textViewName = itemView.findViewById(R.id.text_view_title);
            imageViewPreview = itemView.findViewById(R.id.imageView);
            textViewDescription = itemView.findViewById(R.id.text_view_description);
            viewDetailButton = itemView.findViewById(R.id.button_view_detail);
            tvCost = itemView.findViewById(R.id.price);
        }

        public void setPrice(Product product) {
            if (product == null) return;
            String cost = CurrencyFormat.VietnameseCurrency(product.getNewPrice());
            tvCost.setText(cost);
        }

        public void setId(Product product) {
            if (product == null) return;
            tvProductId.setText(product.getId());
        }

        public void setName(Product product) {
            if(product == null)  return;
            textViewName.setText(product.getName());
        }
        public void setImage(Product product) {
            if(product == null)  return;
            if(product.getMainImage() == null || product.getMainImage().equals("")) return;
            String mainImageUrl = product.getMainImage();
            if(mainImageUrl.contains(UrlConstants.BASE_URL_LOCAL))
                mainImageUrl = mainImageUrl
                        .replace(UrlConstants.BASE_URL_LOCAL, UrlConstants.BASE_URL)
                        .replace(" ", "%20");
            Log.d(TAG, mainImageUrl);
            Glide.with(context) .load(mainImageUrl).into(imageViewPreview);
        }
        public void setDesc(Product product) {
            if(product == null)  return;
            String desc = "";
            if(product.getDetail().length() > 50) {
               desc = product.getDetail().substring(0, 50);
            } else {
                desc = product.getDetail();
            }
            textViewDescription.setText(desc + "....");
        }
        public void setOnClickListener(int position, View itemView) {
            itemView.setOnClickListener(view -> listener.onItemClick(position, view) );
        }
    }
}
