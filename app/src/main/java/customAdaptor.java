import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.deepika.travelguide.R;
import com.squareup.picasso.Picasso;


class customAdaptor extends  ArrayAdapter<String> {
    public customAdaptor(@NonNull Context context, String[] resource) {
        super(context, R.layout.list_row, resource);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater infla = LayoutInflater.from(getContext());
        View Custom = infla.inflate(R.layout.list_row,parent,false);
        String f = getItem(position);
        TextView txt = (TextView)Custom.findViewById(R.id.artist);
        ImageView img = (ImageView)Custom.findViewById(R.id.list_image);
        txt.setText(f);
        Picasso.with(getContext()).load("http://i.imgur.com/DvpvklR.png").into(img);

        //img.setImageResource(R.drawable.ic_action_name);
        Custom.setMinimumHeight(500);
        return Custom;

    }
}
