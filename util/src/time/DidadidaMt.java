package time;

public class DidadidaMt implements Runnable {
	private long delayTime;
	private volatile boolean goon;
	private Runnable task;
	
	public DidadidaMt() {
		this.delayTime = IDidadida.DEFAULT_DELAY_TIME;
		this.goon = false;
		this.task = null;
	}
	
	public DidadidaMt(long delayTime, Runnable task) {
		this();
		this.delayTime = delayTime;
		this.task = task;
	}

	public DidadidaMt(long delayTime) {
		this(delayTime, null);
	}

	public void setDelayTime(long delayTime) {
		this.delayTime = delayTime;
	}

	public void setTask(Runnable task) {
		this.task = task;
	}
	
	public void start() throws Exception {
		if (this.task == null) {
			throw new Exception("定时器未制定任务");
		}
		if (this.goon == true) {
			return;
		}
		this.goon = true;
		new Thread(this, "定时器").start();
	}

	@Override
	public void run() {
		while (this.goon) {
			try {
				Thread.sleep(this.delayTime);
				new Thread(new Runnable() {
					@Override
					public void run() {
						task.run();
					}
				}).start();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
