package jaypatel.co.in.bvmalumni.DevelopedBy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

import jaypatel.co.in.bvmalumni.R;

public class CyeCoders extends AppCompatActivity {

    private RecyclerView mRecyclerView, mRecyclerView2;
    private DeveloperAdapter adapter, adapter2;
    private List<Developer> developerList, maintainersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_cye_coders);
        //setTitle("Developed By");

        developerList = new ArrayList<>();
        adapter = new DeveloperAdapter(this,developerList);

        mRecyclerView = (RecyclerView) findViewById(R.id.mRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(adapter);

        prepareDevelopers();


    }

    private void prepareDevelopers() {

        int[] photos = new int[]{
                R.drawable.abhi,
                R.drawable.aseem,
                R.drawable.jay,
                R.drawable.ghanshyam
        };
        Developer d = new Developer(photos[0], "Abhishek Shah", "9978465658",
                "myselfabhishek1997@gmail.com", "abhi5658");
        developerList.add(d);

        d = new Developer(photos[1], "Aseem Thakkar", "9426068144",
                "aseem.thakker@gmail.com", "aseem-thakkar");
        developerList.add(d);

        d = new Developer(photos[2], "Jay Patel", "9374883988",
                "jp9573@gmail.com", "jp9573");
        developerList.add(d);

        d = new Developer(photos[3], "Ghanshyam Savaliya", "7487002347",
                "ghanshyam140297@gmail.com", "ghanshyam707");
        developerList.add(d);

        adapter.notifyDataSetChanged();
    }
}
