package ly.generalassemb.drewmahrt.shoppinglistwithdetailview;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {

    private ShoppingSQLiteOpenHelper helper;

    private TextView itemName;
    private TextView itemDescription;
    private TextView itemPrice;
    private TextView itemType;
    private TextView itemNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        itemName = (TextView) findViewById(R.id.name);
        itemDescription = (TextView) findViewById(R.id.description);
        itemPrice = (TextView) findViewById(R.id.price);
        itemType = (TextView) findViewById(R.id.type);
        itemNum = (TextView) findViewById(R.id.num);

        int id = getIntent().getIntExtra(ShoppingSQLiteOpenHelper.COL_ID, -2);
        helper = ShoppingSQLiteOpenHelper.getInstance(this);
        Cursor cursor = helper.getShoppingListItemById(id);

        if(cursor.getCount() != 1){
            Log.e("Cursor Shenanigans!", "onCreate: Cursor does not have only 1 row!");
        }

        if(cursor.moveToFirst()){
            itemName.setText(cursor.getString(cursor.getColumnIndex(helper.COL_ITEM_NAME)));
            itemDescription.setText(cursor.getString(cursor.getColumnIndex(helper.COL_ITEM_DESCRIPTION)));
            itemPrice.setText(cursor.getString(cursor.getColumnIndex(helper.COL_ITEM_PRICE)));
            itemType.setText(cursor.getString(cursor.getColumnIndex(helper.COL_ITEM_TYPE)));
            itemNum.setText(cursor.getString(cursor.getColumnIndex(helper.COL_ITEM_NUM)));
        }

        cursor.close();
    }
}
