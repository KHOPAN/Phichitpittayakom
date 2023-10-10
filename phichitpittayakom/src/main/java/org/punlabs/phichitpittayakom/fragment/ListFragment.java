package org.punlabs.phichitpittayakom.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.khopan.api.common.card.CardView;
import com.khopan.api.common.fragment.ContextedFragment;
import com.sec.sesl.org.punlabs.phichitpittayakom.R;

import java.util.ArrayList;
import java.util.List;

import dev.oneuiproject.oneui.widget.RoundLinearLayout;
import dev.oneuiproject.oneui.widget.Separator;

public class ListFragment<T> extends ContextedFragment {
	private final List<T> list;
	private final int size;
	private final ListEntryGetter<T> titleGetter;
	private final ListEntryGetter<T> summaryGetter;
	private final ListEntrySupplier<T> onEntryClick;

	public ListFragment(List<T> list, ListEntryGetter<T> titleGetter, ListEntryGetter<T> summaryGetter, ListEntrySupplier<T> onEntryClick) {
		this.list = list;
		this.size = this.list.size();
		this.titleGetter = titleGetter;
		this.summaryGetter = summaryGetter;
		this.onEntryClick = onEntryClick;
		List<T> removeList = new ArrayList<>();

		for(T entry : this.list) {
			if(entry == null) {
				removeList.add(null);
			}
		}

		this.list.removeAll(removeList);
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
		LinearLayout linearLayout = (LinearLayout) view;
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		Separator separator = new Separator(this.context);
		separator.setText(this.size == 1 ? this.getString(R.string.foundOneSearchResult) : this.getString(R.string.foundSearchResults, this.size));
		linearLayout.addView(separator);
		RoundLinearLayout roundLinearLayout = new RoundLinearLayout(this.context);
		roundLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		roundLinearLayout.setOrientation(LinearLayout.VERTICAL);
		roundLinearLayout.setBackgroundColor(this.context.getColor(R.color.oui_background_color));
		linearLayout.addView(roundLinearLayout);
		RecyclerView recyclerView = new RecyclerView(this.context);
		recyclerView.setLayoutParams(new RoundLinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		recyclerView.setLayoutManager(new LinearLayoutManager(this.context));
		recyclerView.setAdapter(new StudentAdapter(this.context));
		recyclerView.setItemAnimator(null);
		recyclerView.setHasFixedSize(true);
		recyclerView.seslSetFillBottomEnabled(true);
		recyclerView.seslSetLastRoundedCorner(true);
		recyclerView.seslSetFastScrollerEnabled(true);
		recyclerView.seslSetGoToTopEnabled(true);
		recyclerView.seslSetSmoothScrollEnabled(true);
		roundLinearLayout.addView(recyclerView);
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup group, @Nullable Bundle bundle) {
		return new LinearLayout(this.context);
	}

	public class StudentAdapter extends RecyclerView.Adapter<StudentViewHolder> {
		private final Context context;

		public StudentAdapter(Context context) {
			this.context = context;
		}

		@NonNull
		@Override
		public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
			return new StudentViewHolder(new CardView(this.context));
		}

		@Override
		public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
			T entry = ListFragment.this.list.get(position);

			if(entry == null) {
				ListFragment.this.list.remove(position);
				this.notifyItemRemoved(position);
				return;
			}

			holder.view.setDividerVisible(position != 0);
			holder.view.setTitle(ListFragment.this.titleGetter.getString(this.context, entry));
			holder.view.setSummary(ListFragment.this.summaryGetter.getString(this.context, entry));
			holder.view.setOnClickListener(view -> ListFragment.this.onEntryClick.execute(this.context, entry));
		}

		@Override
		public int getItemCount() {
			return ListFragment.this.list.size();
		}
	}

	public static class StudentViewHolder extends RecyclerView.ViewHolder {
		public final CardView view;

		public StudentViewHolder(View view) {
			super(view);
			this.view = (CardView) view;
			this.view.setFocusable(true);
			this.view.setClickable(true);
		}
	}
}
