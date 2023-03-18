package com.example.nasaiotd;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class ImagesAdapter extends BaseAdapter implements ImageDataContainer {

    private final LayoutInflater layoutInflater;
    private final int layoutResourceId;
    private final List<ImageData> images = new ArrayList<ImageData>();

    public ImagesAdapter(LayoutInflater layoutInflater, int layoutResourceId) {
        this.layoutInflater = layoutInflater;
        this.layoutResourceId = layoutResourceId;
    }


    public void add(ImageData image) {
        images.add(image);
    }


    public void addRange(List<ImageData> list) {
        for (ImageData image : list) {
            add(image);
        }
    }

    public void insertAt(int index, ImageData image) {
        images.add(index, image);
    }

    /**
     * Removes the ImageData at index position.
     * @param position The index in the array of the item.
     */
    public void removeAt(int position) {
        images.remove(position);
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object getItem(int position) {
        return images.get(position);
    }

    @Override
    public long getItemId(int position) {
        ImageData image = images.get(position);

        if (image != null){
            return image.getId();
        }

        return (long)position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageData imageData = images.get(position);

        View view = layoutInflater.inflate(layoutResourceId, parent, false);

        if (imageData.getImage() != null) {
            ImageView imageImage = view.findViewById(R.id.ImageListItemImage);
            imageImage.setImageBitmap(imageData.getImage());
        }

        TextView imageDate = view.findViewById(R.id.ImageDate);
        if (imageDate != null) {
            imageDate.setText(imageData.getDate());
        }

        TextView imageTitle = view.findViewById(R.id.ImageTitle);
        if (imageTitle != null) {
            imageTitle.setText(imageData.getTitle());
        }

        return view;
    }
}
