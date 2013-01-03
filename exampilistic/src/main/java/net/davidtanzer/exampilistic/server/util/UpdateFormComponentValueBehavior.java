package net.davidtanzer.exampilistic.server.util;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;

public class UpdateFormComponentValueBehavior extends AjaxFormComponentUpdatingBehavior {
	private static final long serialVersionUID = 1L;

	public UpdateFormComponentValueBehavior() {
		super("onblur");
	}

	@Override
	protected void onUpdate(final AjaxRequestTarget target) {
	}
}