package net.davidtanzer.exampilistic;

public class InconclusiveExampleException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public InconclusiveExampleException(final String message) {
		super(message);
	}

	public InconclusiveExampleException(final String message, final Throwable cause) {
		super(message, cause);
	}

}
