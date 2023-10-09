package com.khopan.api.common.list;

import android.animation.LayoutTransition;
import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatCheckBox;

import com.khopan.api.common.R;
import com.khopan.api.common.card.CardView;

import java.util.ArrayList;
import java.util.List;

import dev.oneuiproject.oneui.widget.RoundLinearLayout;

public class ListView extends LinearLayout {
	public final RoundLinearLayout allItemLayout;
	public final CardView allItemView;
	public final AppCompatCheckBox allItemCheckbox;
	public final View allItemSeparator;
	public final RoundLinearLayout itemLayout;
	public final View addItemSeparator;
	public final RoundLinearLayout addItemLayout;
	public final CardView addItemView;

	private final List<ListEntry> list;
	private final Context context;
	private final DisplayMetrics metrics;

	private boolean addItemViewVisible;
	private boolean selectionExpanded;
	private boolean actionViewExpanded;
	private OnAddItemListener onAddItemListener;
	private OnExpandListener onExpandListener;
	private OnCollapseListener onCollapseListener;
	private View actionView;

	public ListView(Context context) {
		this(context, null);
	}

	public ListView(Context context, AttributeSet attribute) {
		super(context, attribute);
		this.list = new ArrayList<>();
		this.context = context;
		Resources resources = this.context.getResources();
		this.metrics = resources.getDisplayMetrics();
		int backgroundColor = this.context.getColor(R.color.oui_background_color);
		int separatorSize = resources.getDimensionPixelSize(R.dimen.sesl_list_subheader_min_height);
		LayoutTransition transition = new LayoutTransition();
		transition.enableTransitionType(LayoutTransition.CHANGING);
		this.setLayoutTransition(transition);
		this.setOrientation(LinearLayout.VERTICAL);
		this.allItemLayout = new RoundLinearLayout(this.context);
		this.allItemLayout.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		this.allItemLayout.setOrientation(RoundLinearLayout.VERTICAL);
		this.allItemLayout.setBackgroundColor(backgroundColor);
		this.addView(this.allItemLayout);
		this.allItemView = new CardView(this.context);
		this.allItemView.containerView.removeAllViews();
		this.allItemCheckbox = new AppCompatCheckBox(this.context);
		TypedValue value = new TypedValue();
		this.context.getTheme().resolveAttribute(R.attr.textAppearanceListItem, value, true);
		this.allItemCheckbox.setTextAppearance(value.data);
		this.allItemCheckbox.setPadding(Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8.5f, this.metrics)), 0, 0, 0);
		this.allItemCheckbox.setOnClickListener(view -> this.allItemCheckbox());
		this.allItemView.containerView.addView(this.allItemCheckbox);
		this.allItemView.setOnClickListener(instance -> this.allItemCheckbox.performClick());
		this.allItemLayout.addView(this.allItemView);
		this.allItemSeparator = new View(this.context);
		this.allItemSeparator.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, separatorSize));
		this.addView(this.allItemSeparator);
		this.itemLayout = new RoundLinearLayout(this.context);
		this.itemLayout.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		this.itemLayout.setOrientation(RoundLinearLayout.VERTICAL);
		this.itemLayout.setBackgroundColor(backgroundColor);
		transition = new LayoutTransition();
		transition.enableTransitionType(LayoutTransition.CHANGING);
		this.itemLayout.setLayoutTransition(transition);
		this.addView(this.itemLayout);
		this.addItemSeparator = new View(this.context);
		this.addItemSeparator.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, separatorSize));
		this.addView(this.addItemSeparator);
		this.addItemLayout = new RoundLinearLayout(this.context);
		this.addItemLayout.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		this.addItemLayout.setOrientation(RoundLinearLayout.VERTICAL);
		this.addItemLayout.setBackgroundColor(backgroundColor);
		this.addView(this.addItemLayout);
		this.addItemView = new CardView(this.context);
		this.addItemView.setOnClickListener(view -> this.addItemView());
		this.addItemLayout.addView(this.addItemView);
		this.addItemViewVisible = true;
		this.setSelectionExpanded(false);
	}

	public boolean isAddItemViewVisible() {
		return this.addItemViewVisible;
	}

	public boolean isSelectionExpanded() {
		return this.selectionExpanded;
	}

	public boolean isActionViewExpanded() {
		return this.actionViewExpanded;
	}

	public OnAddItemListener getOnAddItemListener() {
		return this.onAddItemListener;
	}

	public OnExpandListener getOnExpandListener() {
		return this.onExpandListener;
	}

	public OnCollapseListener getOnCollapseListener() {
		return this.onCollapseListener;
	}

	public void setAddItemViewVisible(boolean addItemViewVisible) {
		this.addItemViewVisible = addItemViewVisible;
		this.addItemSeparator.setVisibility(this.addItemViewVisible && this.list.size() > 0 ? View.VISIBLE : View.GONE);
		this.addItemLayout.animate().alpha(this.addItemViewVisible ? 1.0f : 0.0f);
	}

	public void setSelectionExpanded(boolean selectionExpanded) {
		this.selectionExpanded = selectionExpanded;
		this.setAddItemViewVisible(!this.selectionExpanded);
		int visibility = this.selectionExpanded && this.list.size() > 1 ? View.VISIBLE : View.GONE;
		this.allItemSeparator.setVisibility(visibility);
		this.allItemLayout.setVisibility(visibility);

		if(!this.selectionExpanded) {
			this.allItemCheckbox.setChecked(false);
		}

		for(ListEntry item : this.list) {
			if(!this.selectionExpanded) {
				item.itemCheckbox.setChecked(false);
			}

			item.itemCheckbox.setVisibility(this.selectionExpanded ? View.VISIBLE : View.GONE);
		}

		this.setActionViewExpanded(this.selectionExpanded);

		if(this.selectionExpanded) {
			if(this.onExpandListener != null) {
				this.onExpandListener.onExpand(this);
			}
		} else {
			if(this.onCollapseListener != null) {
				this.onCollapseListener.onCollapse(this);
			}
		}
	}

	public void setActionViewExpanded(boolean actionViewExpanded) {
		this.actionViewExpanded = actionViewExpanded;

		if(this.actionView == null) {
			return;
		}

		this.actionView.animate().translationY(this.actionViewExpanded ? -this.actionView.getHeight() : 0.0f);
	}

	public void setOnAddItemListener(OnAddItemListener listener) {
		this.onAddItemListener = listener;
	}

	public void setOnExpandListener(OnExpandListener listener) {
		this.onExpandListener = listener;
	}

	public void setOnCollapseListener(OnCollapseListener listener) {
		this.onCollapseListener = listener;
	}

	public boolean onBackPressed() {
		if(!this.selectionExpanded) {
			return false;
		}

		this.setSelectionExpanded(false);
		return true;
	}

	public void attachActionView(View view) {
		this.actionView = view;
	}

	public void detachActionView() {
		this.actionView = null;
	}

	public CardView addItem() {
		return this.addItem(null, null, null);
	}

	public CardView addItem(String title) {
		return this.addItem(title, null, null);
	}

	public CardView addItem(String title, String summary) {
		return this.addItem(title, summary, null);
	}

	public CardView addItem(String title, String summary, OnClickListener listener) {
		CardView itemView = new CardView(this.context);
		itemView.setTitle(title == null ? "" : title);
		itemView.setSummary(summary == null ? "" : summary);
		AppCompatCheckBox itemCheckbox = new AppCompatCheckBox(this.context);
		itemView.setOnClickListener(view -> {
			if(this.selectionExpanded) {
				itemCheckbox.performClick();
			} else {
				if(listener != null) {
					listener.onClick(view);
				}
			}
		});

		itemView.setOnLongClickListener(view -> {
			this.setSelectionExpanded(true);
			itemCheckbox.setChecked(true);
			return true;
		});

		LayoutParams itemCheckboxParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		itemCheckboxParams.rightMargin = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15.0f, this.metrics));
		itemCheckbox.setLayoutParams(itemCheckboxParams);
		itemCheckbox.setOnClickListener(view -> {
			boolean allChecked = true;
			boolean noneChecked = true;

			for(ListEntry item : this.list) {
				if(item.itemCheckbox.isChecked()) {
					noneChecked = false;
				} else {
					allChecked = false;
				}
			}

			this.allItemCheckbox.setChecked(allChecked);
			this.setActionViewExpanded(!noneChecked);
		});

		itemCheckbox.setVisibility(this.selectionExpanded ? View.VISIBLE : View.GONE);
		itemView.containerView.addView(itemCheckbox, 0);
		itemView.containerView.setLayoutTransition(new LayoutTransition());
		this.itemLayout.addView(itemView);
		ListEntry item = new ListEntry();
		item.itemView = itemView;
		item.itemCheckbox = itemCheckbox;
		this.list.add(item);
		this.updateItem();
		return itemView;
	}

	public List<Integer> removeSelected() {
		if(!this.selectionExpanded) {
			return null;
		}

		List<Integer> indexList = new ArrayList<>();
		List<ListEntry> itemList = new ArrayList<>();

		for(int i = 0; i < this.list.size(); i++) {
			ListEntry item = this.list.get(i);

			if(item.itemCheckbox.isChecked()) {
				indexList.add(i);
				itemList.add(item);
				this.itemLayout.removeView(item.itemView);
			}
		}

		int size = itemList.size();
		this.list.removeAll(itemList);

		if(size > 0) {
			this.setSelectionExpanded(false);
		}

		return indexList;
	}

	private void allItemCheckbox() {
		boolean state = this.allItemCheckbox.isChecked();

		for(ListEntry item : this.list) {
			item.itemCheckbox.setChecked(state);
		}

		this.setActionViewExpanded(state);
	}

	private void addItemView() {
		if(!this.selectionExpanded && this.onAddItemListener != null) {
			this.onAddItemListener.onAddItem(this);
		}
	}

	private void updateItem() {
		for(int i = 0; i < this.list.size(); i++) {
			this.list.get(i).itemView.setDividerVisible(i != 0);
		}

		this.addItemSeparator.setVisibility(this.addItemViewVisible && this.list.size() > 0 ? View.VISIBLE : View.GONE);
	}

	public interface OnAddItemListener {
		void onAddItem(ListView listView);
	}

	public interface OnExpandListener {
		void onExpand(ListView listView);
	}

	public interface OnCollapseListener {
		void onCollapse(ListView listView);
	}

	private static class ListEntry {
		private CardView itemView;
		private AppCompatCheckBox itemCheckbox;
	}
}
