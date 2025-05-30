package ttlock.demo.fingerprint;

import android.app.Activity;
import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


import ttlock.demo.R;

import ttlock.demo.databinding.AttachmentListItemBinding;
import ttlock.demo.fingerprint.model.FingerprintObj;


/**
 * Created on  2019/4/28 0028 16:03
 *
 * @author theodre
 */
public class FinggerprintAdapter extends  RecyclerView.Adapter<FinggerprintAdapter.FingerprintViewHolder> {

    public ArrayList<FingerprintObj> mDataList = new ArrayList<>();
    private Activity mContext;
    onListItemClick mListener;
    public FinggerprintAdapter (Activity context){
        mContext = context;
    }

    interface onListItemClick{
        void onItemClick(FingerprintObj fingerprintObj);
    }

    public void setOnListItemClick(onListItemClick listItemClick){
        this.mListener = listItemClick;
    }

    public void updateData(ArrayList<FingerprintObj> list){
        mDataList.clear();
        mDataList.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FingerprintViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(mContext).inflate(R.layout.attachment_list_item,parent,false);
        return new FingerprintViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull FingerprintViewHolder holder, int position) {
        FingerprintObj fingerprintObj = mDataList.get(position);
        holder.onBind(fingerprintObj);
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }


    class FingerprintViewHolder extends RecyclerView.ViewHolder {

        AttachmentListItemBinding itemBinding;

        public FingerprintViewHolder(View itemView) {
            super(itemView);
            itemBinding = DataBindingUtil.bind(itemView);
        }

        public void onBind(FingerprintObj fingerprintObj){
            itemBinding.tvItem.setText(fingerprintObj.getFingerprintName());
            itemBinding.tvItem.setOnClickListener(v -> {
                if(mListener != null){
                    mListener.onItemClick(fingerprintObj);
                }
            });
        }
    }
}
