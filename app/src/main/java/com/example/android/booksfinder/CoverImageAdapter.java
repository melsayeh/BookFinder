//package com.example.android.booksfinder;
//
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.ImageView;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class CoverImageAdapter extends ArrayAdapter<Bitmap> {
//    public CoverImageAdapter(@NonNull Context context, @NonNull List objects) {
//        super(context, 0, objects);
//    }
//
//    @NonNull
//    @Override
//    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        // (Recycling) Check if the existing view is being reused, otherwise inflate the view
//        View listCoverImage = convertView;
//        if (listCoverImage==null){
//            listCoverImage = LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);
//        }
//        // Get the {@link Word} object located at this position in the list
//        final Bitmap pointer = getItem(position);
//        /*Declare and assign values to ImageView and TextViews BookTitle, Authors,
//        Publisher, PublishedDate, and Description
//         */
//
//        ImageView bookCover = listCoverImage.findViewById(R.id.book_cover);
//        bookCover.setImageBitmap(pointer);
//
//        return listCoverImage;
//    }
//}
