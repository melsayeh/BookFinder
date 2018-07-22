package com.example.android.booksfinder;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

public class BookAdapter<E extends Object> extends ArrayAdapter<BookInfo> {
    private Bitmap coverThumbnail;

    public BookAdapter(@NonNull Context context, @NonNull List<BookInfo> objects) {
        super(context, 0, objects);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // (Recycling) Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView==null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);
        }
        // Get the {@link Word} object located at this position in the list
        final BookInfo pointer = getItem(position);

        /*Declare and assign values to ImageView and TextViews BookTitle, Authors,
        Publisher, PublishedDate, and Description
         */
        Log.d("Pointer Value","---------->"+position);

        TextView bookTitle =  listItemView.findViewById(R.id.book_title);
        bookTitle.setText(pointer.getBookTitle());

        TextView authorsNames = listItemView.findViewById(R.id.authors);
        authorsNames.setText(fetchAuthorsFromArray(pointer.getAuthors()));

        TextView publisherAndDate = listItemView.findViewById(R.id.publisher_date);
        String collector;
        if(pointer.getPublisher().equals(" ")) {
            collector = pointer.getPublishedDate();
        }else{
            collector = pointer.getPublisher()+" - "+pointer.getPublishedDate();
        }
        publisherAndDate.setText(collector);

        TextView price = listItemView.findViewById(R.id.price);
        if(pointer.getCurrencyCode().equals(" "))
        {
            price.setText(pointer.getRetailPrice());
        }else{
            String retailPrice = pointer.getCurrencyCode()+" "+pointer.getRetailPrice();
            price.setText(retailPrice);
        }

        TextView bookDescription = listItemView.findViewById(R.id.description);
        bookDescription.setText(pointer.getBookDescription());

        ImageView bookCoverImage = listItemView.findViewById(R.id.book_cover);
        bookCoverImage.setImageBitmap(pointer.getCoverImage());

        LinearLayout itemLayout = listItemView.findViewById(R.id.parent_item);
        itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri previewLink = Uri.parse(pointer.getPreviewLinkUrl());
                Intent openPreviewLink = new Intent(Intent.ACTION_VIEW, previewLink);
                getContext().startActivity(openPreviewLink);
            }
        });
        return listItemView;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public String fetchAuthorsFromArray(ArrayList<String> authors){
        Log.v("Authors", Arrays.toString(authors.toArray()));
        int i;
        StringJoiner groupedAuthors = new StringJoiner(" & ");
        for(i=0 ; i<authors.size() ; i++ ){
            if(authors.size()==1) {
                return authors.get(0);
            }
            else{
//                if(authors[i]!=null) {
                    groupedAuthors.add(authors.get(i));
//                }

            }
        }
        return groupedAuthors.toString();
    }
}
