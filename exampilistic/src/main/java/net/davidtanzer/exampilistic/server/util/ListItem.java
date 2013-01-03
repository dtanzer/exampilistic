package net.davidtanzer.exampilistic.server.util;

import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.AbstractReadOnlyModel;

public class ListItem<T> extends Item<T>
{
	private static final long serialVersionUID = 1L;

	public ListItem(final String id, final int index)
	{
		super(id, index);
		setModel(new ListItemModel());
	}

	private class ListItemModel extends AbstractReadOnlyModel<T>
	{
		private static final long serialVersionUID = 1L;

		@Override
		public T getObject()
		{
			return ((ListEditor<T>)ListItem.this.getParent())
					.items.get(getIndex());
		}
	}
	
	@Override
	public String toString() {
		return "["+getIndex()+"] -> "+getModelObject();
	}
}