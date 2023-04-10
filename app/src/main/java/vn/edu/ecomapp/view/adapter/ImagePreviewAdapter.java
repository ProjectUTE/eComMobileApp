package vn.edu.ecomapp.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import vn.edu.ecomapp.R;
import vn.edu.ecomapp.model.ImagePreview;
import vn.edu.ecomapp.util.Constants;

public class ImagePreviewAdapter extends RecyclerView.Adapter<ImagePreviewAdapter.ViewHolder> {
    List<ImagePreview> imagePreviews;

    Context context;

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<ImagePreview> imagePreviews) {
        this.imagePreviews = imagePreviews;
        notifyDataSetChanged();
    }

    public ImagePreviewAdapter(List<ImagePreview> imagePreviews, Context context) {
        this.imagePreviews = imagePreviews;
        this.context = context;
    }

    @NonNull
    @Override
    public ImagePreviewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(this.context).inflate(R.layout.list_image_preview_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImagePreviewAdapter.ViewHolder holder, int position) {
        final ImagePreview imagePreview = imagePreviews.get(position);
        if(imagePreview == null) return;
        holder.setImage(imagePreview);
    }

    @Override
    public int getItemCount() {
        if(imagePreviews != null) return  imagePreviews.size();
        return 0;
    }

    public class  ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.imagePreview);
        }

        public void setImage(ImagePreview ins) {
            if (ins == null) return;
            String filePathReplaced = ins.getFilePath().replace(Constants.BASE_URL_LOCAL, Constants.BASE_URL);
                Glide.with(context).load(filePathReplaced).into(image);
        }
    }
}
