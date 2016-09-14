package iseng.inu.de.cobaanimasi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.skyfishjy.library.RippleBackground;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AnjingRipple njing = (AnjingRipple)findViewById(R.id.content);
        njing.startRippleAnimation();

    }
}
