package ttlock.demo.iccard;

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

import ttlock.demo.iccard.model.ICCardObj;

/**
 * Created on  2019/4/28 0028 17:07
 *
 * @author theodre
 */
public class ICCardListAdapter extends  RecyclerView.Adapter<ICCardListAdapter.CardViewHolder> {

    public ArrayList<ICCardObj> mDataList = new ArrayList<>();
    private Activity mContext;
    onListItemClick mListener;
    public ICCardListAdapter (Activity context){
        mContext = context;
    }

    interface onListItemClick{
        void onItemClick(ICCardObj cardObj);
    }

    public void setOnListItemClick(onListItemClick listItemClick){
        this.mListener = listItemClick;
    }

    public void updateData(ArrayList<ICCardObj> list){
        mDataList.clear();
        mDataList.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(mContext).inflate(R.layout.attachment_list_item,parent,false);
        return new CardViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        ICCardObj cardObj = mDataList.get(position);
        holder.onBind(cardObj);
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    class CardViewHolder extends RecyclerView.ViewHolder {

        AttachmentListItemBinding itemBinding;

        public CardViewHolder(View itemView) {
            super(itemView);
            itemBinding = DataBindingUtil.bind(itemView);
        }

        public void onBind(ICCardObj cardObj){
            itemBinding.tvItem.setText(cardObj.getCardName());
            itemBinding.tvItem.setOnClickListener(v -> {
                if(mListener != null){
                    mListener.onItemClick(cardObj);
                }
            });
        }
    }
}
