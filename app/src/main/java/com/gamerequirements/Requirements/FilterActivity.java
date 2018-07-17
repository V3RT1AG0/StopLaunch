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
    String selectedOrder, selectedSort;
    RadioButton title, release, rand, asc, dsc;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_filter);
        selectedOrder = getIntent().getStringExtra("Order");
        selectedSort = getIntent().getStringExtra("Sort");
        title = findViewById(R.id.title);
        release = findViewById(R.id.release_date);
        rand = findViewById(R.id.random);
        asc = findViewById(R.id.ascending);
        dsc = findViewById(R.id.descending);

        final RadioGroup radioGroup = findViewById(R.id.orderBy);
        final RadioGroup radioGroup2 = findViewById(R.id.sortBy);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                // find which radio button is selected
                if (checkedId == R.id.random)
                {
                    selectedSort = null;
                    asc.setEnabled(false);
                    dsc.setEnabled(false);
                    asc.setChecked(false);
                    dsc.setChecked(false);
                } else
                {
                    asc.setEnabled(true);
                    dsc.setEnabled(true);
                    asc.setChecked(true);
                }

                if (checkedId == R.id.random)
                {
                    selectedOrder = "rand";

                } else if (checkedId == R.id.title)
                {
                    selectedOrder = "title";
                } else
                {
                    selectedOrder = "release_date";
                }
            }

        });

        switch (selectedOrder)
        {
            case "title":
                title.setChecked(true);
                break;
            case "rand":
                rand.setChecked(true);
                break;
            case "release_date":
                release.setChecked(true);
                break;
        }
        if (selectedSort != null)
            switch (selectedSort)
            {
                case "asc":
                    asc.setChecked(true);
                    break;
                case "dsc":
                    dsc.setChecked(true);
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


        radioGroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                // find which radio button is selected
                if (checkedId == R.id.ascending)
                {
                    selectedSort = "asc";
                } else
                {
                    selectedSort = "dsc";
                }
            }

        });
    }
}
