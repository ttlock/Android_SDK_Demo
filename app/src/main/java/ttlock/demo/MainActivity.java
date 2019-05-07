package ttlock.demo;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import ttlock.demo.databinding.ActivityMainBinding;
import ttlock.demo.fingerprint.FingerprintActivity;
import ttlock.demo.firmwareupdate.FirmwareUpdateActivity;
import ttlock.demo.gateway.UserGatewayActivity;
import ttlock.demo.iccard.ICCardActivity;
import ttlock.demo.lock.LockApiActivity;
import ttlock.demo.passcode.PasscodeActivity;

;

public class MainActivity extends BaseActivity {
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        initListener();
    }

    private void initListener(){
        binding.btnLock.setOnClickListener(v -> startTargetActivity(LockApiActivity.class));
        binding.btnPasscode.setOnClickListener(v -> startTargetActivity(PasscodeActivity.class));
        binding.btnFirmware.setOnClickListener(v ->  startTargetActivity(FirmwareUpdateActivity.class));
        binding.btnFingerprint.setOnClickListener(v -> startTargetActivity(FingerprintActivity.class));
        binding.btnIc.setOnClickListener(v -> startTargetActivity(ICCardActivity.class));
        binding.btnGateway.setOnClickListener(v -> startTargetActivity(UserGatewayActivity.class));
    }
}
