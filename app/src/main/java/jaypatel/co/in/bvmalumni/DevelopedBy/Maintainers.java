package jaypatel.co.in.bvmalumni.DevelopedBy;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import jaypatel.co.in.bvmalumni.R;

public class Maintainers extends AppCompatActivity {

    private RecyclerView mRecyclerView, mRecyclerView2;
    private DeveloperAdapter adapter, adapter2;
    private List<Developer> developerList, maintainersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_maintainers);
        //setTitle("Developed By");

        developerList = new ArrayList<>();
        adapter = new DeveloperAdapter(this,developerList);

        maintainersList = new ArrayList<>();
        adapter2 = new DeveloperAdapter(this,maintainersList);

        mRecyclerView2 = (RecyclerView) findViewById(R.id.mRecyclerView2);
        mRecyclerView2.setHasFixedSize(true);
        mRecyclerView2.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView2.setAdapter(adapter2);


        prepareMaintainers();
    }

    private void prepareMaintainers() {

        int[] photos = new int[]{
                R.drawable.vatsal,
                R.drawable.chintan,
                R.drawable.priyank,
        };

        Developer d = new Developer(photos[0], "Prof. Vatsal Shah", "9601290965",
                "vatsal.shah@bvmengineering.ac.in", "");
        maintainersList.add(d);

        d = new Developer(photos[1], "Prof. Chintan Mahant", "9879726089",
                "chintan.mahant@bvmengineering.ac.in", "");
        maintainersList.add(d);

        d = new Developer(photos[2], "Prof. Priyank Bhojak", "7405641686",
                "priyank.bhojak@bvmengineering.ac.in", "");
        maintainersList.add(d);

        adapter2.notifyDataSetChanged();
    }

}
