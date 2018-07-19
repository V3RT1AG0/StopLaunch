package com.gamerequirements.Requirements;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.gamerequirements.R;

public class FilterActivity extends Activity
{
    String selectedOrder, selectedSort, label;
    RadioButton oldest, newest, rand, titlasc, titledsc;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_filter);
        selectedOrder = getIntent().getStringExtra("Order");
        selectedSort = getIntent().getStringExtra("Sort");
        titlasc = findViewById(R.id.titleAsc);
        titledsc = findViewById(R.id.titleDsc);
        newest = findViewById(R.id.newest);
        oldest = findViewById(R.id.oldest);
        rand = findViewById(R.id.random);

        final RadioGroup radioGroup = findViewById(R.id.orderBy);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                RadioButton selectedRadio = findViewById(checkedId);
                label = selectedRadio.getText().toString();
                if (checkedId == R.id.random)
                {
                    selectedOrder = "rand";
                    selectedSort = null;
                } else if (checkedId == R.id.titleAsc)
                {
                    selectedOrder = "title";
                    selectedSort = "asc";
                } else if (checkedId == R.id.titleDsc)
                {
                    selectedOrder = "title";
                    selectedSort = "desc";
                } else if (checkedId == R.id.newest)
                {
                    selectedOrder = "date";
                    selectedSort = "asc";
                } else
                {
                    selectedOrder = "date";
                    selectedSort = "desc";
                }
            }

        });

        switch (selectedOrder)
        {
            case "title":
                if (selectedSort.equals("asc"))
                    titlasc.setChecked(true);
                else
                    titledsc.setChecked(true);
                break;
            case "rand":
                rand.setChecked(true);
                break;
            case "date":
                if (selectedSort.equals("asc"))
                    oldest.setChecked(true);
                else
                    newest.setChecked(true);
                break;
        }

        findViewById(R.id.Done).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("order", selectedOrder);
                returnIntent.putExtra("sort", selectedSort);
                returnIntent.putExtra("label", label);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });

        findViewById(R.id.Cancel).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_CANCELED, returnIntent);
                finish();
            }
        });
    }
}
