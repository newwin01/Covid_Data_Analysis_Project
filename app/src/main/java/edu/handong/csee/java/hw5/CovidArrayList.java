package edu.handong.csee.java.hw5;
/*
 * CovidArrayList based on linked list
 */
public class CovidArrayList<E>  {
	
	private ListNode head;
	
	private class ListNode{
		private E data;
		private ListNode link;
		
		public ListNode(E newData, ListNode linkedNode) {
			data = newData;
			link = linkedNode;
		}
	}
	
	/*
	 * constructor, set head as null
	 */
	public CovidArrayList(){
		head=null;
	}
	
	/*
	 * shows the list of data in CovidArrayList
	 */
	public void showList() {
		ListNode position = head;
		while(position != null) {
			System.out.println(position.data);
			position = position.link;
		}
	}
	/*
	 * return the length of CovidArrayList
	 */
	public int length() {
		int count = 0;
		ListNode position = head;
		while(position != null) {
			count++;
			position = position.link;
		}
		return count;
	}
	
	/*
	 * put new data on the end of CovidArrayList
	 */
	
	public void add(E addData) {
		ListNode newNode = new ListNode(addData, null);
		if(head==null) {
			head=newNode;
		} else {
			ListNode current = null;
			for(current=head;current.link!=null;current=current.link) {
			}
			current.link=newNode;
		}
	}
	
	/*
	 * return true if there is data in the CovidArrayList
	 */
	public boolean contain(E target) {
		return find(target) != null;
	}
	
	
	private ListNode find(E target) {
		boolean found = false;
		ListNode position = head;
		while((position != null) && !found) {
			E dataAtPosition = position.data;
			if(dataAtPosition.equals(target))
				found = true;
			else
				position = position.link;
		}
		return position;
	}
	/*
	 * get the data of index
	 */
	public E get(int index) {
		ListNode newNode = head;
		for(int i=0;i<index;i++) {
			newNode = newNode.link;
		}
		return newNode.data;
	}	
		
	
	
}