package net.davidtanzer.exampilistic.server.util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.form.IFormModelUpdateListener;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;

public abstract class ListEditor<T> extends RepeatingView implements IFormModelUpdateListener
{
	private static final long serialVersionUID = 1L;
	List<T> items;
	private final IModel<List<T>> model;

	public ListEditor(final String id, final IModel<List<T>> model)
	{
		super(id, model);
		this.model = model;
		items = new ArrayList<T>(model.getObject());
	}

	protected abstract void populateItem(ListItem<T> item);

	public void addItem(final T value)
	{
		items.add(value);
		if(hasBeenRendered()) {
			addListItem(items.size()-1);
		}
	}

	@Override
	protected Iterator<? extends Component> renderIterator() {
		return iterator(new Comparator<Component>() {
			@Override
			public int compare(final Component first, final Component second) {
				if(first == second) {
					return 0;
				}
				if(first == null) {
					return -1;
				}
				if(second == null) {
					return 1;
				}
				
				if(first instanceof ListItem<?> && second instanceof ListItem<?>) {
					return ((ListItem<?>)first).getIndex() - ((ListItem<?>)second).getIndex();
				}
				
				return 0;
			}
		});
	}
	
	private void addListItem(final int itemIndex) {
		ListItem<T> item = new ListItem<T>(newChildId(), itemIndex);
		add(item);
		populateItem(item);
	}

	@Override
	protected void onBeforeRender()
	{
		if (!hasBeenRendered())
		{
			for (int i = 0; i < items.size(); i++)
			{
				ListItem<T> li = new ListItem<T>(newChildId(), i);
				add(li);
				populateItem(li);
			}
		}
		super.onBeforeRender();
	}

	@Override
	public void updateModel()
	{
		model.setObject(items);
	}
	
	public List<T> getItems() {
		return items;
	}

	public void replace(final T toReplace, final List<T> toInsert) {
		int itemIndex=0;
		for(T itemModel : items) {
			if(itemModel == toReplace) {
				break;
			}
			itemIndex++;
		}
		if(itemIndex == items.size()) {
			throw new IllegalStateException("Item not found: \""+toReplace+"\" in "+items);
		}
		
		int indexDifference = toInsert.size()-1;
		for(int i=0; i<size(); i++) {
			ListItem<?> currentItem = (ListItem<?>) get(i);
			if(currentItem.getIndex() > itemIndex) {
				currentItem.setIndex(currentItem.getIndex()+indexDifference);
			}
		}
		
		remove(findItem(toReplace));
		items.remove(itemIndex);
		
		int insertIndex = itemIndex;
		for(T currentInsertItem : toInsert) {
			items.add(insertIndex, currentInsertItem);
			addListItem(insertIndex);
			insertIndex++;
		}
	}

	private ListItem<T> findItem(final T model) {
		Iterator<Component> it = iterator();
		while(it.hasNext()) {
			Component currentItem = it.next();
			if(currentItem instanceof ListItem) {
				int modelIndex = ((ListItem<T>) currentItem).getIndex();
				if(modelIndex < items.size() && items.get(modelIndex)==model) {
					return (ListItem<T>) currentItem;
				}
			}
		}
		throw new IllegalStateException("Could not find list item for model: "+model);
	}
}