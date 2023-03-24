package vn.edu.ecomapp.view.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

import vn.edu.ecomapp.R;
import vn.edu.ecomapp.model.Slide;

public class SliderAdapter extends  RecyclerView.Adapter<SliderAdapter.ViewHolder>{
    Context context;
    List<Slide> slides;
    ViewPager2 sliderContainer;

    private Runnable sliderRunnable = new Runnable() {
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

        private final RoundedImageView slideItem;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            slideItem = itemView.findViewById(R.id.slideItem);
        }

        void  setSlideItem(Slide slide) {
//            Glide.with(context).load(slide.getImageUrl()).into(slideItem);
            slideItem.setImageResource(R.drawable.coffee);
        }


    }

}
