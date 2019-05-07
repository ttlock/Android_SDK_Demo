package ttlock.demo.gateway.adapter;

import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import ttlock.demo.R;
import ttlock.demo.databinding.UserGatewayListItemBinding;
import ttlock.demo.gateway.GatewayDfuActivity;
import ttlock.demo.model.GatewayObj;

;

/**
 * Created on  2019/4/12 0012 14:19
 *
 * @author theodre
 */
public class UserGatewayListAdapter extends  RecyclerView.Adapter<UserGatewayListAdapter.DeviceViewHolder>{

    public ArrayList<GatewayObj> mDataList = new ArrayList<>();

    private Context mContext;
    public UserGatewayListAdapter(Context context){
        mContext = context;
    }

    public void updateData(ArrayList<GatewayObj> gatewayList) {
        if (gatewayList != null) {
            mDataList.clear();
            mDataList.addAll(gatewayList);
            notifyDataSetChanged();
        }
    }

    @Override
    public DeviceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(mContext).inflate(R.layout.user_gateway_list_item, parent, false);
        return new DeviceViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(DeviceViewHolder _holder, int position) {
        final GatewayObj item = mDataList.get(position);
        _holder.Bind(item);
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    class DeviceViewHolder extends RecyclerView.ViewHolder {

        UserGatewayListItemBinding itemBinding;

        public DeviceViewHolder(View itemView){
            super(itemView);
            itemBinding = DataBindingUtil.bind(itemView);
        }

        public void Bind(GatewayObj item){
            itemBinding.tvGatewayName.setText(item.getGatewayMac());
                itemBinding.getRoot().setOnClickListener(view -> {
                    //G2 gateway has dfu function
                    if (item.getGatewayVersion() == 2)
                        GatewayDfuActivity.launch((Activity) mContext, item);
                });
            }
        }
}
