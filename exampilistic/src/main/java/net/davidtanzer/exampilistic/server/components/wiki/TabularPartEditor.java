package net.davidtanzer.exampilistic.server.components.wiki;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.davidtanzer.exampilistic.element.TabularElement.InitParameter;
import net.davidtanzer.exampilistic.server.pages.wiki.InputUpdateBehavior;
import net.davidtanzer.exampilistic.server.pages.wiki.InputUpdateBehavior.InputUpdateListener;
import net.davidtanzer.exampilistic.server.util.InputHintFocusBehavior;
import net.davidtanzer.exampilistic.server.util.ListEditor;
import net.davidtanzer.exampilistic.server.util.ListItem;
import net.davidtanzer.exampilistic.server.util.UpdateFormComponentValueBehavior;
import net.davidtanzer.exampilistic.table.TableContent;
import net.davidtanzer.exampilistic.table.TableContent.ColumnName;
import net.davidtanzer.exampilistic.table.TableContent.TableCellValue;
import net.davidtanzer.exampilistic.table.TableContent.TableRow;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.convert.IConverter;

public class TabularPartEditor extends Panel implements InputUpdateListener {
	private static final long serialVersionUID = 1L;
	private final ListEditor<InitParameter> initParameters;
	private final WebMarkupContainer parametersContainer;
	private final ListEditor<ColumnName> columnNames;
	private final WebMarkupContainer tableData;
	private final TableRowEditor dataRows;

	public TabularPartEditor(final String id, final TableContent model) {
		super(id);
		setOutputMarkupId(true);
		
		TextField<String> tableHeadline = new TextField<String>("tableHeadline", new PropertyModel<String>(model, "headline")) {
			private static final long serialVersionUID = 1L;

			@Override
			public <C> IConverter<C> getConverter(final Class<C> type) {
				return new InputHintFocusBehavior.InputHintConverter<C>(this, super.getConverter(type));
			}
		};
		tableHeadline.add(new InputHintFocusBehavior(tableHeadline, "Headline of the table"));
		tableHeadline.add(new UpdateFormComponentValueBehavior());
		add(tableHeadline);
		
		initParameters = new ListEditor<InitParameter>("initParameters",
				new PropertyModel<List<InitParameter>>(model, "initParameters")) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(final ListItem<InitParameter> item) {
				InitParameter parameter = item.getModelObject();
				
				TextField<String> nameField = new TextField<String>("parameterName", new PropertyModel<String>(parameter, "parameterName")) {
					private static final long serialVersionUID = 1L;

					@Override
					public <C> IConverter<C> getConverter(final Class<C> type) {
						return new InputHintFocusBehavior.InputHintConverter<C>(this, super.getConverter(type));
					}
				};
				nameField.add(new InputHintFocusBehavior(nameField, "Parameter name"));
				nameField.add(new UpdateFormComponentValueBehavior());
				item.add(nameField);
				TextField<String> valueField = new TextField<String>("parameterValue", new PropertyModel<String>(parameter, "parameterValue")) {
					private static final long serialVersionUID = 1L;

					@Override
					public <C> IConverter<C> getConverter(final Class<C> type) {
						return new InputHintFocusBehavior.InputHintConverter<C>(this, super.getConverter(type));
					}
				};
				valueField.add(new InputUpdateBehavior("onfocus", TabularPartEditor.this, 0));
				valueField.add(new UpdateFormComponentValueBehavior());
				valueField.add(new InputHintFocusBehavior(valueField, "Value"));
				item.add(valueField);
			}
		};
		
		parametersContainer = new WebMarkupContainer("parametersContainer");
		parametersContainer.setOutputMarkupId(true);
		parametersContainer.add(initParameters);
		add(parametersContainer);
		
		tableData = new WebMarkupContainer("tableData");
		tableData.setOutputMarkupId(true);
		add(tableData);
		
		columnNames = new ListEditor<ColumnName>("columnNames", new PropertyModel<List<ColumnName>>(model, "columnNames")) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(final ListItem<ColumnName> item) {
				
				TextField<String> columnName = new TextField<String>("columnName", new PropertyModel<String>(item.getModelObject(), "name")) {
					private static final long serialVersionUID = 1L;

					@Override
					public <C> IConverter<C> getConverter(final Class<C> type) {
						return new InputHintFocusBehavior.InputHintConverter<C>(this, super.getConverter(type));
					}
				};
				columnName.add(new InputHintFocusBehavior(columnName, "Column Name"));
				columnName.add(new InputUpdateBehavior("onfocus", TabularPartEditor.this, 0));
				columnName.add(new UpdateFormComponentValueBehavior());;
				item.add(columnName);
			}
		};
		tableData.add(columnNames);
		
		dataRows = new TableRowEditor("dataRows", new PropertyModel<List<TableRow>>(model, "rows"));
		tableData.add(dataRows);
		
		inputUpdated(null);
	}

	@Override
	public void inputUpdated(final AjaxRequestTarget target) {
		if(initParameters.getItems().isEmpty() || lastParameterFilled()) {
			initParameters.addItem(new InitParameter());
			if(target != null) {
				target.add(parametersContainer);
			}
		}
		if(columnNames.getItems().isEmpty() || lastColumnNameFilled()) {
			columnNames.addItem(new ColumnName());
			addColumnToAllRows();
			if(target != null) {
				target.add(tableData);
			}
		}
		if(dataRows.getItems().isEmpty() || lastRowContainsData()) {
			TableRow row = new TableRow();
			for(ColumnName name : columnNames.getItems()) {
				row.getCellValues().add(new TableCellValue());
			}
			dataRows.addItem(row);
			if(target != null) {
				target.add(tableData);
			}
		}
	}

	private boolean lastRowContainsData() {
		TableRow lastRow = dataRows.getItems().get(dataRows.getItems().size()-1);
		ListEditor<TableCellValue> editor = dataRows.getRowCellsEditors().get(lastRow);
		if(editor != null) {
			for(TableCellValue cell : editor.getItems()) {
				if(cell.getValue() != null && cell.getValue().length() > 0) {
					return true;
				}
			}
		}
		return false;
	}

	private void addColumnToAllRows() {
		for(TableRow row : dataRows.getItems()) {
			ListEditor<TableCellValue> editor = dataRows.getRowCellsEditors().get(row);
			if(editor != null) {
				editor.addItem(new TableCellValue());
			}
		}
	}

	private boolean lastColumnNameFilled() {
		String columnName = columnNames.getItems().get(columnNames.getItems().size()-1).getName();
		return columnName != null && columnName.length() > 0;
	}

	private boolean lastParameterFilled() {
		String parameterName = initParameters.getItems().get(initParameters.getItems().size()-1).getParameterName();
		return parameterName != null && parameterName.length() > 0;
	}

	private final class TableRowEditor extends ListEditor<TableRow> {
		private static final long serialVersionUID = 1L;
		private final Map<TableRow, ListEditor<TableCellValue>> rowCellsEditors = new HashMap<>();

		private TableRowEditor(final String id, final IModel<List<TableRow>> model) {
			super(id, model);
		}

		@Override
		protected void populateItem(final ListItem<TableRow> item) {
			TableRow row = item.getModelObject();
			
			ListEditor<TableCellValue> cellValuesEditor = new ListEditor<TableCellValue>("cellValues", new PropertyModel<List<TableCellValue>>(row, "cellValues")) {
				private static final long serialVersionUID = 1L;

				@Override
				protected void populateItem(final ListItem<TableCellValue> item) {
					TextField<String> cellValue = new TextField<String>("cellValue", new PropertyModel<String>(item.getModelObject(), "value")) {
						private static final long serialVersionUID = 1L;

						@Override
						public <C> IConverter<C> getConverter(final Class<C> type) {
							return new InputHintFocusBehavior.InputHintConverter<C>(this, super.getConverter(type));
						}
					};
					cellValue.add(new InputUpdateBehavior("onfocus", TabularPartEditor.this, 0));
					cellValue.add(new InputHintFocusBehavior(cellValue, "Value"));
					cellValue.add(new UpdateFormComponentValueBehavior());;
					item.add(cellValue);
				}
			};
			while(cellValuesEditor.getItems().size() < columnNames.getItems().size()) {
				cellValuesEditor.addItem(new TableCellValue());
			}
			
			rowCellsEditors.put(row, cellValuesEditor);
			item.add(cellValuesEditor);
			
			TextField<String> rowDescription = new TextField<String>("rowDescription", new PropertyModel<String>(row, "description")) {
				private static final long serialVersionUID = 1L;

				@Override
				public <C> IConverter<C> getConverter(final Class<C> type) {
					return new InputHintFocusBehavior.InputHintConverter<C>(this, super.getConverter(type));
				}
			};
			rowDescription.add(new InputUpdateBehavior("onfocus", TabularPartEditor.this, 0));
			rowDescription.add(new InputHintFocusBehavior(rowDescription, "Description"));
			rowDescription.add(new UpdateFormComponentValueBehavior());;
			item.add(rowDescription);
		}
		
		public Map<TableRow, ListEditor<TableCellValue>> getRowCellsEditors() {
			return rowCellsEditors;
		}
	}

}
