package ttlock.demo.gateway.dialog;

import android.content.Context;
import android.content.DialogInterface;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.ttlock.bl.sdk.gateway.model.WiFi;
import com.ttlock.bl.sdk.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

import ttlock.demo.R;
import ttlock.demo.databinding.ItemScanWifiBinding;

/**
 * Created by TTLock on 2019/3/13.
 */

public class ChooseNetDialog extends BottomSheetDialog {
    private Context context;
    private RecyclerView recyclerView;
    private WiFiListAdapter adapter;
    private TextView cancel;
    private OnSelectListener onSelectListener;

    public interface OnSelectListener {
            public void onSelect(WiFi wiFi);
    }

    public ChooseNetDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    public ChooseNetDialog(@NonNull Context context, int theme) {
        super(context, theme);
    }

    protected ChooseNetDialog(@NonNull Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_net_dialog);
        init();
    }

    private void init() {
        Window window = this.getWindow();
        window.setLayout((ViewGroup.LayoutParams.MATCH_PARENT), ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.BOTTOM);
        recyclerView = findViewById(R.id.recycler_view);
        cancel = findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });

        View view = getDelegate().findViewById(com.google.android.material.R.id.design_bottom_sheet);
        final BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(view);
//        bottomSheetBehavior.setSkipCollapsed(false);
//        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                bottomSheetBehavior.setPeekHeight(0);
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                bottomSheetBehavior.setPeekHeight(BottomSheetBehavior.PEEK_HEIGHT_AUTO);
            }
        });

    }

    @Override
    public void cancel() {
        super.cancel();
        if (adapter != null)
            adapter.clearData();
        LogUtil.d("cancel");
    }

    /**
     * Dismiss this dialog, removing it from the screen. This method can be
     * invoked safely from any thread.  Note that you should not override this
     * method to do cleanup when the dialog is dismissed, instead implement
     * that in {@link #onStop}.
     */
    @Override
    public void dismiss() {
        super.dismiss();
        LogUtil.d("dismiss");
    }

    public void setOnSelectListener(OnSelectListener onSelectListener) {
        this.onSelectListener = onSelectListener;
    }

    public void updateWiFi(List<WiFi> wiFis) {
        if (!isShowing())//TODO:
            return;
        try {
            if (adapter == null) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
                adapter = new WiFiListAdapter();
                adapter.replaceData(wiFis);
                recyclerView.setAdapter(adapter);
            } else {
                adapter.replaceData(wiFis);
                adapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

 public class WiFiListAdapter extends RecyclerView.Adapter<WiFiListAdapter.WiFiViewHolder> {

        public ArrayList<WiFi> mDataList = new ArrayList<>();

        @NonNull
        @Override
        public WiFiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View mView = LayoutInflater.from(context).inflate(R.layout.item_scan_wifi, parent, false);
            return new WiFiViewHolder(mView);
        }

        @Override
        public void onBindViewHolder(@NonNull WiFiViewHolder holder, int position) {
            final WiFi item = mDataList.get(position);
            holder.Bind(item);
        }

        @Override
        public int getItemCount() {
            return mDataList.size();
        }

        public void clearData() {
            mDataList.clear();
        }

        public void replaceData(List<WiFi> wiFis) {
            clearData();
            mDataList.addAll(wiFis);
        }

        class WiFiViewHolder extends RecyclerView.ViewHolder {

            ItemScanWifiBinding itemBinding;

            public WiFiViewHolder(View itemView) {
                super(itemView);
                itemBinding = DataBindingUtil.bind(itemView);
            }

            public void Bind(WiFi item) {
                itemBinding.name.setText(item.getSsid());
                itemBinding.signal.setText(String.valueOf(item.getRssi()));
                itemBinding.getRoot().setOnClickListener(v -> {
                    if (onSelectListener != null) {
                        onSelectListener.onSelect(item);
                        cancel();
                    }
                });
                itemBinding.executePendingBindings();
            }
        }

    }
}
