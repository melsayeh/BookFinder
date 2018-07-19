package com.example.android.booksfinder;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.List;
import java.util.StringJoiner;

public class BookAdapter<E extends Object> extends ArrayAdapter<BookInfo> {
    private Bitmap coverThumbnail;

    public BookAdapter(@NonNull Context context, int resource, @NonNull List<BookInfo> objects) {
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

        ImageView bookCover = listItemView.findViewById(R.id.book_cover);
        bookCover.setImageBitmap(pointer.getBookCoverImage());

        TextView bookTitle =  listItemView.findViewById(R.id.book_title);
        bookTitle.setText(pointer.getBookTitle());

        TextView authorsNames = listItemView.findViewById(R.id.authors);
        authorsNames.setText(fetchAuthorsFromArray(pointer.getAuthors()));

        TextView publisherAndDate = listItemView.findViewById(R.id.publisher_date);
        String collector = pointer.getPublisher()+R.string.dash+pointer.getPublishedDate();
        publisherAndDate.setText(collector);

        TextView bookDescription = listItemView.findViewById(R.id.description);
        bookDescription.setText(pointer.getBookDescription());
        return listItemView;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public String fetchAuthorsFromArray(String[] authors){
        int i;
        StringJoiner groupedAuthors = new StringJoiner(" & ");
        for(i=0 ; i<authors.length ; i++ ){
            if(authors.length==1) {
                return authors[0];
            }
            else{
                groupedAuthors.add(authors[i]);
            }
        }
        return groupedAuthors.toString();
    }
}
