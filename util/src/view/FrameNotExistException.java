package view;

public class FrameNotExistException extends RuntimeException {
	private static final long serialVersionUID = -2940483866585734615L;

	public FrameNotExistException() {
	}

	public FrameNotExistException(String message) {
		super(message);
	}

	public FrameNotExistException(Throwable cause) {
		super(cause);
	}

	public FrameNotExistException(String message, Throwable cause) {
		super(message, cause);
	}

	public FrameNotExistException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
