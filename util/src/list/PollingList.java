package list;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


/**
 * 在PollingList中存在两个链表，其中一个称为“焦点”链表，另一个是非焦点链表。
 * @author ZHO
 *
 * @param <T>
 */
public class PollingList <T> {
	private List<List<PollingElement<T>>> list;
	//表明焦点链的下标
	private volatile int focus;
	private Object lock;
	//对于链表中元素的操作
	private IPollingAction<T> pollingAction;
	//初始状态为0，即用来判断元素是否被访问，如果状态一致则未访问过
	private int status = 0;


	public PollingList() {
		this.focus = 0;
		this.lock = new Object();
		this.pollingAction = new PollingActionAdapter<T>();

		this.list = new ArrayList<List<PollingElement<T>>>();
		this.list.add(new LinkedList<PollingElement<T>>());
		this.list.add(new LinkedList<PollingElement<T>>());
	}

	//这里是在判断是否还有元素之后取得元素的操作
	public T next() {
		synchronized (this.lock) {
			List<PollingElement<T>> list = this.list.get(0);
			for (PollingElement<T> element : list) {
				//这里再次判断是否被访问过
				if (element.isVisited(this.status)) {
					//这里实际上执行的是访问元素操作，访问完毕后转换访问状态
					element.revVisited();
					//返回取得的元素
					return element.getElement();
				}
			}

			list = this.list.get(1);
			for (PollingElement<T> element : list) {
				if (element.isVisited(this.status)) {
					element.revVisited();
					return element.getElement();
				}
			}

			return null;
		}
	}
	/*判断两个链中是否还存在元素*/
	public boolean hasNext() {
		synchronized (this.lock) {
			List<PollingElement<T>> list = this.list.get(0);
			for (PollingElement<T> element : list) {
				//这里的比较条件判断元素的状态是否与List的状态一致，一致则证明未访问过
				if (element.isVisited(this.status)) {
					return true;
				}
			}

			list = this.list.get(1);
			for (PollingElement<T> element : list) {
				if (element.isVisited(this.status)) {
					return true;
				}
			}
		}
		//全部发现无未访问的，即均访问完毕后，使得list的状态转化，
		//	转换后对于已经经过next方法访问到的元素，又可以重新访问一次
		this.status = 1 - this.status;
		return false;
	}

	//这里是轮询操作，遍历焦点链中的元素，访问操作焦点链首个元素，访问完毕后删除元素，添加到非焦点链的末端
	//且由于锁的存在，使得删除首位元素不会出现逻辑错误，当先轮询再删除时，实际上删除的是非焦点链中的最后一个
	//先删除再轮询则所轮询到的是第二位元素
	public void polling() {
		synchronized (this.lock) {

			List<PollingElement<T>> focusList = this.list.get(this.focus);
			List<PollingElement<T>> auxList = this.list.get(1 - this.focus);

			while (!focusList.isEmpty()) {
				PollingElement<T> element = focusList.remove(0);
				this.pollingAction.pollingAction(element.getElement());
				auxList.add(element);
			}
			//轮询完毕后转换焦点状态
			this.focus = 1 - this.focus;
		}
	}

	/**
	 * 要增加的元素一定追加到“非焦点”链表的末尾！这样做可以避免多线程操作时的不一致性。
	 * @param e 要加入的元素
	 */
	//Appends the specified element to the end of this list 添加一个元素到非焦点链的列表
	public void add(T e) {
		synchronized (this.lock) {
			this.list.get(1 - this.focus).add(new PollingElement<T>(e));
		}
	}

	/**
	 * 若e存在于非焦点链表中，则，直接删除即可；若e存在于焦点链表中，需要先判断e是否是
	 * 焦点链表首元素，若不是首元素，则，直接删除即可；若是首元素，它肯定会成为“轮询”
	 * 的下一个元素，在删除时，可能会造成很多逻辑困境。
	 * @param e 要删除的元素
	 */
	//这里删除的操作是给定一个元素，这个元素要new一个PollingElement元素，才可以继续进行contains比较
//	public void remove(T e) {
//		synchronized (this.lock) {
//			PollingElement<T> p  =new PollingElement<>(e);
//			//contains操作会在底层使用equals方法（indexOf方法也相同），在底层源码中体现（需要看List的源码）
//			if (this.list.get(this.focus).contains(p)) {
//				this.list.get(this.focus).remove(p);
//				return;
//			}
//			if (this.list.get(1 - this.focus).contains(p)) {
//				this.list.get((1 - this.focus)).remove(p);
//				return;
//			}
//		}
//	}

	public void remove(T e) {
		synchronized (this.lock) {
			//这里错误的原因是使用了e，即String类型的equals()方法
			if (this.list.get(this.focus).contains(e)) {
				this.list.get(this.focus).remove(e);
				return;
			}

			if (this.list.get(1 - this.focus).contains(e)) {
				this.list.get((1 - this.focus)).remove(e);
				return;
			}
		}
	}

	public void setPollingListAction(IPollingAction<T> pollingAction) {
		this.pollingAction = pollingAction;
	}

	public boolean isEmpty() {
		synchronized (this.lock) {
			return this.list.get(0).isEmpty() && this.list.get(1).isEmpty();
		}
	}

	public int size() {
		synchronized (this.lock) {
			return this.list.get(0).size() + this.list.get(1).size();
		}
	}

	//清空所有链表中的内容
	public void clear() {
		synchronized (this.lock) {
			this.list.get(0).clear();
			this.list.get(1).clear();
			this.focus = 0;
		}
	}

	public boolean contains(T object) {
		synchronized (this.lock) {
			return this.list.get(0).contains(object) || this.list.get(1).contains(object);
		}
	}

	//取得所有的元素
	public List<T> getElementList() {
		synchronized (this.lock) {
			List<T> elementList = new ArrayList<>();
			List<PollingElement<T>> focusList = this.list.get(this.focus);
			List<PollingElement<T>> auxList = this.list.get(1 - this.focus);

			for (PollingElement<T> ele : focusList) {
				elementList.add(ele.getElement());
			}
			for (PollingElement<T> ele : auxList) {
				elementList.add(ele.getElement());
			}

			return elementList;
		}
	}

}
