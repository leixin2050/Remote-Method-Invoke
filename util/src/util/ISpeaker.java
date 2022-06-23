package util;

public interface ISpeaker {
	void addListener(IListener listener);
	void removeListener(IListener listener);
	void speakOut(String message);
}
