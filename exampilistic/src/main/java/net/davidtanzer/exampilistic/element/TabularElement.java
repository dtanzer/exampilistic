package net.davidtanzer.exampilistic.element;

public class TabularElement extends PageElement {
	public static class InitParameter {
		private String parameterName;
		private String parameterValue;
		
		public String getParameterName() {
			return parameterName;
		}
		public void setParameterName(final String parameterName) {
			this.parameterName = parameterName;
		}
		public String getParameterValue() {
			return parameterValue;
		}
		public void setParameterValue(final String parameterValue) {
			this.parameterValue = parameterValue;
		}
	}
}
