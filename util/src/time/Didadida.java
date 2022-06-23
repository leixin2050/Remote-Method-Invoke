package time;

public class Didadida implements Runnable {
	private long delayTime;
	private Runnable task;
	private volatile boolean goon;
	private Object lock;
	
	public Didadida() {
		this.delayTime = IDidadida.DEFAULT_DELAY_TIME;
		this.goon = false;
		this.lock = new Object();
	}

	public Didadida(long delayTime) {
		this(null, delayTime);
	}

	public Didadida(Runnable task, long delayTime) {
		this();
		this.task = task;
		this.delayTime = delayTime;
	}

	public void setDelayTime(long delayTime) {
		this.delayTime = delayTime;
	}

	public void setTask(Runnable task) {
		this.task = task;
	}
	
	public void startup() throws Exception {
		if (this.task == null) {
			throw new Exception("定时器未制定任务");
		}
		if (this.goon == true) {
			return;
		}
		this.goon = true;
		synchronized (lock) {
			new Thread(new InnerTask()).start();
			lock.wait();
		}
		new Thread(this, "定时器").start();
	}
	
	public void stop() {
		if (this.goon == false) {
			return;
		}
		
		synchronized (this.lock) {
			this.goon = false;
			this.lock.notify();
		}
	}

	@Override
	public void run() {
		while (this.goon) {
			try {
				Thread.sleep(this.delayTime);
				synchronized (lock) {
					lock.notify();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	class InnerTask implements Runnable {
		public InnerTask() {
			new Thread(this).start();
		}
		
		@Override
		public void run() {
			synchronized (lock) {
				lock.notify();
				try {
					lock.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			while (goon) {
				synchronized (lock) {
					try {
						task.run();
						if (goon) {
							lock.wait();
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
		
	}
	
}
