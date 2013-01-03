package net.davidtanzer.exampilistic.server.components.wiki;

import net.davidtanzer.exampilistic.element.TabularElement.InitParameter;
import net.davidtanzer.exampilistic.table.TableContent;
import net.davidtanzer.exampilistic.table.TableContent.ColumnName;
import net.davidtanzer.exampilistic.table.TableContent.TableCellValue;
import net.davidtanzer.exampilistic.table.TableContent.TableRow;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;

public class TabularExamplePanel extends Panel {
	private static final long serialVersionUID = 1L;
	
	public TabularExamplePanel(final String id, final TableContent model) {
		super(id);
		
		add(new Label("tableHeadline", model.getHeadline()));
		
		add(new ListView<InitParameter>("initParameters", model.getInitParameters()) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(final ListItem<InitParameter> item) {
				InitParameter parameter = item.getModelObject();
				
				item.add(new Label("parameterName", parameter.getParameterName()));
				item.add(new Label("parameterValue", parameter.getParameterValue()));
			}
		});
		
		add(new ListView<ColumnName>("columnNames", model.getColumnNames()) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(final ListItem<ColumnName> item) {
				item.add(new Label("columnName", item.getModelObject().getName()));
			}
		});
		
		add(new ListView<TableRow>("dataRows", model.getRows()) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(final ListItem<TableRow> item) {
				TableRow row = item.getModelObject();
				
				item.add(new ListView<TableCellValue>("cellValues", row.getCellValues()) {
					private static final long serialVersionUID = 1L;

					@Override
					protected void populateItem(final ListItem<TableCellValue> item) {
						item.add(new Label("cellValue", item.getModelObject().getValue()));
					}
				});
				item.add(new Label("rowDescription", row.getDescription()));
			}
		});
	}
}
