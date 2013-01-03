package net.davidtanzer.exampilistic.server.pages.wiki;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.attributes.ThrottlingSettings;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.util.time.Duration;

public class InputUpdateBehavior extends AjaxFormComponentUpdatingBehavior {
	public static interface InputUpdateListener {
		void inputUpdated(AjaxRequestTarget target);
	}

	private static final long serialVersionUID = 1L;
	private final InputUpdateListener updateListener;
	private final long throttleDuration;

	public InputUpdateBehavior(final String event, final InputUpdateListener updateListener, final long throttleDuration) {
		super(event);
		this.updateListener = updateListener;
		this.throttleDuration = throttleDuration;
	}
	
	@Override
	protected void updateAjaxAttributes(final AjaxRequestAttributes attributes) {
		super.updateAjaxAttributes(attributes);
		
		if(throttleDuration > 0) {
			attributes.setThrottlingSettings(new ThrottlingSettings("input-fields-throttle", Duration.milliseconds(throttleDuration), true));
		}
	}
	
	@Override
	protected void onUpdate(final AjaxRequestTarget target) {
		updateListener.inputUpdated(target);
	}
}