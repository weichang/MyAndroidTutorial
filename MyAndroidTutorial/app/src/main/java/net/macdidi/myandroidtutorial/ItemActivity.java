package net.macdidi.myandroidtutorial;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.Date;

public class ItemActivity extends AppCompatActivity {

    private EditText title_text, content_text, cost_text;
    private Spinner spinner;
    private ArrayAdapter<String> lunchList;
    private Context mContext;
    private String[] lunch = {"服飾", "家具", "3C產品", "食物", "文具"};

    // 啟動功能用的請求代碼
    private static final int START_CAMERA = 0;
    private static final int START_RECORD = 1;
    private static final int START_LOCATION = 2;
    private static final int START_ALARM = 3;
    private static final int START_COLOR = 4;

    // 記事物件
    private Item item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        processViews();

        // type  spinner
        mContext = this.getApplicationContext();
        spinner = (Spinner)findViewById(R.id.spinner_type);

        lunchList = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lunch);
        spinner.setAdapter(lunchList);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
//                Toast.makeText(mContext, "你選的是"+lunch[position], Toast.LENGTH_SHORT).show();

            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });


        // 取得Intent物件
        Intent intent = getIntent();
        // 讀取Action名稱
        String action = intent.getAction();

        // 如果是修改記事
        if (action.equals("net.macdidi.myandroidtutorial.EDIT_ITEM")) {
            // 接收記事物件與設定標題、內容
            item = (Item) intent.getExtras().getSerializable(
                    "net.macdidi.myandroidtutorial.Item");
            title_text.setText(item.getTitle());
            content_text.setText(item.getContent());
            cost_text.setText(String.valueOf(item.getCost()));
            spinner.setSelection((int) item.getType());
        }
        // 新增記事
        else {
            item = new Item();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case START_CAMERA:
                    break;
                case START_RECORD:
                    break;
                case START_LOCATION:
                    break;
                case START_ALARM:
                    break;
                // 設定顏色
                case START_COLOR:
                    int colorId = data.getIntExtra(
                            "colorId", Colors.LIGHTGREY.parseColor());
                    item.setColor(getColors(colorId));
                    break;
            }
        }
    }

    public static Colors getColors(int color) {
        Colors result = Colors.LIGHTGREY;

        if (color == Colors.BLUE.parseColor()) {
            result = Colors.BLUE;
        }
        else if (color == Colors.PURPLE.parseColor()) {
            result = Colors.PURPLE;
        }
        else if (color == Colors.GREEN.parseColor()) {
            result = Colors.GREEN;
        }
        else if (color == Colors.ORANGE.parseColor()) {
            result = Colors.ORANGE;
        }
        else if (color == Colors.RED.parseColor()) {
            result = Colors.RED;
        }

        return result;
    }

    private void processViews() {
        title_text = (EditText) findViewById(R.id.title_text);
        content_text = (EditText) findViewById(R.id.content_text);
        cost_text = (EditText)findViewById(R.id.cost_text);
    }

    // 點擊確定與取消按鈕都會呼叫這個方法
    public void onSubmit(View view) {
        // 確定按鈕
        if (view.getId() == R.id.ok_teim) {
            String titleText = title_text.getText().toString();
            String contentText = content_text.getText().toString();
            String costText = cost_text.getText().toString();
            item.setTitle(titleText);
            item.setContent(contentText);
            item.setCost(Long.parseLong(costText));
            item.setType(spinner.getSelectedItemPosition());

            if (getIntent().getAction().equals(
                    "net.macdidi.myandroidtutorial.EDIT_ITEM")) {
                item.setLastModify(new Date().getTime());
            }
            // 新增記事
            else {
                item.setDatetime(new Date().getTime());
                // 建立SharedPreferences物件
                SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(this);
                // 讀取設定的預設顏色
                int color = sharedPreferences.getInt("DEFAULT_COLOR", -1);
                item.setColor(getColors(color));
            }

            Intent result = getIntent();
            result.putExtra("net.macdidi.myandroidtutorial.Item", item);
            setResult(Activity.RESULT_OK, result);
        }

        // 結束
        finish();
    }

    // 以後需要擴充的功能
    public void clickFunction(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.take_picture:
                break;
            case R.id.record_sound:
                break;
            case R.id.set_location:
                break;
            case R.id.set_alarm:
                break;
            case R.id.select_color:
                // 啟動設定顏色的Activity元件
                startActivityForResult(
                        new Intent(this, ColorActivity.class), START_COLOR);
                break;
        }

    }

}
