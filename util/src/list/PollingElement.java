package list;

/*此类存储了一个数据类型，包括了存储的数据以及数据是否被访问过
默认未访问*/
public class PollingElement <T> {
	private T element;
	//标明是否访问过，供hasNext与Next进行是否访问的比较，为了多线程安全问题
	private int visited;
	
	PollingElement() {
		this.visited = 0;
	}

	PollingElement(T element) {
		this();
		this.element = element;
	}

	T getElement() {
		return element;
	}

	/*不允许set操作，因为每一个element都需要保证存在一个他的专属visited状态*/

	//返回这个element元素（是否被访问与传入的状态参数的相等比较，如果相同则返回真
	boolean isVisited(int status) {
		return this.visited == status;
	}

	void revVisited() {
		this.visited = 1 - this.visited;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this.element) return true;
		if (obj == null) 	return false;
		System.out.println("obj.getClass" + obj.getClass());
		if (!obj.getClass().equals(this.element.getClass())) return false;
		
		return this.element.equals(obj);
	}
	@Override
	public String toString() {
		return "PollingElement [element=" + element + ", visited=" + visited + "]";
	}
	
	
	
}
