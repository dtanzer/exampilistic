package net.davidtanzer.exampilistic.server.util;

import java.util.List;
import java.util.Locale;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.convert.IConverter;

public class InputHintFocusBehavior extends AjaxEventBehavior {
	private static final long serialVersionUID = 1L;
	private final String pseudoUniqueJavascriptVariableName = this.getClass().getSimpleName()
            + String.valueOf(System.nanoTime());
	
	private final TextField<? extends Object> targetComponent;
	private final String hint;
	private boolean isHintShowing;

	public static class InputHintConverter<T> implements IConverter<T> {
		private static final long serialVersionUID = 1L;
		
		private final IConverter<T> delegatingConverter;
		private final TextField<?> target;

		public InputHintConverter(final TextField<?> target, final IConverter<T> delegatingConverter) {
			this.delegatingConverter = delegatingConverter;
			this.target = target;
		}
		
		@Override
		public T convertToObject(final String value, final Locale locale) {
			List<InputHintFocusBehavior> inputHintBehaviors = target.getBehaviors(InputHintFocusBehavior.class);
			if(inputHintBehaviors != null) {
				for(InputHintFocusBehavior bhv : inputHintBehaviors) {
					if(bhv.isHintShowing) {
						return null;
					}
				}
			}
			return delegatingConverter.convertToObject(value, locale);
		}

		@Override
		public String convertToString(final T value, final Locale locale) {
			return delegatingConverter.convertToString(value, locale);
		}
	}
	public InputHintFocusBehavior(final TextField<? extends Object> target, final String hint) {
		super("onfocus");
		this.targetComponent = target;
		this.hint = hint;
		
		isHintShowing = target.getModel() == null || target.getModel().getObject()==null || target.getModel().getObject().toString().length() == 0;
		target.setOutputMarkupId(true);
		target.add(new AttributeModifier("style", new IModel<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public void detach() {
			}

			@Override
			public String getObject() {
				if(isHintShowing) {
					return "color: #aaa;";
				}
				return "";
			}

			@Override
			public void setObject(final String object) {
			}
		}));
	}

	@Override
	public void renderHead(final Component component, final IHeaderResponse response)
	{
		super.renderHead(component, response);
		if(isHintShowing) {
			response.render(new OnDomReadyHeaderItem("var " + pseudoUniqueJavascriptVariableName
					+ "=document.getElementById('" + targetComponent.getMarkupId() + "');" + this.pseudoUniqueJavascriptVariableName
					+ ".value='" + this.hint + "';"));
		}
	}

	@Override
	protected void onEvent(final AjaxRequestTarget target) {
		if(isHintShowing) {
			isHintShowing = false;
			target.add(targetComponent);
		}
		
	}

}
