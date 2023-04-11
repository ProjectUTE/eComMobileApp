package vn.edu.ecomapp.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import vn.edu.ecomapp.R;
import vn.edu.ecomapp.dto.Slide;
import vn.edu.ecomapp.util.constants.UrlConstants;

public class SliderAdapter extends  RecyclerView.Adapter<SliderAdapter.ViewHolder>{
    Context context;
    List<Slide> slides;
    ViewPager2 sliderContainer;

    private final Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            slides.addAll(slides);
            notifyDataSetChanged();
        }
    };

    public SliderAdapter(Context context, List<Slide> slides, ViewPager2 sliderContainer) {
        this.context = context;
        this.sliderContainer = sliderContainer;
        this.slides = slides;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(context).inflate(R.layout.list_slide_item, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Slide slide = slides.get(position);
        if(slide == null) return;
        holder.setSlideItem(slide);
        if(position == slides.size() - 2) {
            sliderContainer.post(sliderRunnable);
        }
    }

    @Override
    public int getItemCount() {
        if(slides == null)
            return 0;
        return  slides.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView slideItem;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            slideItem = itemView.findViewById(R.id.slideItem);
        }

        void  setSlideItem(Slide slide) {
            if(slide == null) return;
            String link = slide.getLink().replace(UrlConstants.BASE_URL_LOCAL, UrlConstants.BASE_URL);
            Glide.with(context).load(link)
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(10)))
                    .into(slideItem);
        }


    }

}
