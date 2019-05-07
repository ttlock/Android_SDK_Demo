package ttlock.demo;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import ttlock.demo.databinding.ActivityIndexBinding;
import ttlock.demo.gateway.UserGatewayActivity;
import ttlock.demo.lock.UserLockActivity;

public class IndexActivity extends BaseActivity {

    private ActivityIndexBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_index);
        initListener();
    }

    private void initListener() {
        binding.btnLock.setOnClickListener(v -> {startTargetActivity(UserLockActivity.class);});
        binding.btnGateway.setOnClickListener(v -> {startTargetActivity(UserGatewayActivity.class);});
    }
}
