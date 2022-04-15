package lit.de.vkanect.HOME;

import static lit.de.vkanect.data.CONSTANTS.Firebase.TAG;
import static lit.de.vkanect.data.CONSTANTS.Firebase.institute;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.util.ArrayList;
import java.util.List;

import lit.de.vkanect.R;
import lit.de.vkanect.data.CONSTANTS.Firebase;
import lit.de.vkanect.data.Massage;
import lit.de.vkanect.student.frag.notice.NoticeBoardAdapter;

public class TimeTable extends AppCompatActivity {

    MaterialCardView materialCardView;
    RecyclerView recyclerView;
    private List<Tdata> ttList = new ArrayList<>();
    private TtAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_table);

        MaterialToolbar mToolbar= (MaterialToolbar) findViewById(R.id.toolbar);

        materialCardView = findViewById(R.id.add_tt_item_dialog);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(v -> finish());
        mToolbar.inflateMenu(R.menu.tt_bar_menu);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.add :
                       // Toast.makeText(TimeTable.this, "Add...", Toast.LENGTH_SHORT).show();


                        materialCardView.setVisibility(View.VISIBLE);

                        String[] options = {"Sem 1","Sem 2","Sem 3","Sem 4"};
                        AutoCompleteTextView autoCompleteTextView;
                        ArrayAdapter<String> adapter;
                        autoCompleteTextView = findViewById(R.id.dropdown_menu_add_row);
                        adapter = new ArrayAdapter<String>(getApplicationContext(),
                                R.layout.tt_add_option,options);
                        autoCompleteTextView.setAdapter(adapter);
                        autoCompleteTextView.setOnItemClickListener((parent, view, position, id) -> {
                            String value = parent.getItemAtPosition(position).toString();
                            Toast.makeText(TimeTable.this, value, Toast.LENGTH_SHORT).show();
                        });

                        break;
                }
                return false;
            }
        });
        materialCardView.setVisibility(View.GONE);
        findViewById(R.id.cancel_tt_table_row).setOnClickListener(c->{
            materialCardView.setVisibility(View.GONE);
        });

        recyclerView = findViewById(R.id.tt_recycler);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        ttList.add(new Tdata("CNS","8:00"));
        ttList.add(new Tdata("TOC","9:00"));
        ttList.add(new Tdata("AJV","10:00"));

        mAdapter = new TtAdapter(ttList);
        recyclerView.setAdapter(mAdapter);

        Button time = findViewById(R.id.select_tt_time);
        time.setOnClickListener(c->{
            MaterialTimePicker materialTimePicker = new MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_12H)
                    .setTitleText("Select Time")
                    .build();
            materialTimePicker.addOnPositiveButtonClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    time.setText(materialTimePicker.getHour()+" : "+materialTimePicker.getMinute());
                }
            });
            materialTimePicker
                    .show(getSupportFragmentManager(),"ok");
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.tt_bar_menu, menu);
        return true;
    }
}