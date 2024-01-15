package ca.ship.model;


import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class MyNeatList<F> implements List<F>, Serializable {

    public NeatNode<F> head=null;

    @Override
    public int size() {
        if (head==null)//
            return 0;
        int sum = 0;
        for (NeatNode temp = head; temp!=null; temp = temp.next)
            sum++;//adds 1 to size for each node in the list
         return sum;
    }

    @Override
    public boolean isEmpty() {
        return (size()==0);
    }

    @Override
    public boolean contains(Object o) {
        for (NeatNode temp = head; temp!=null; temp = temp.next)
            if (temp.getContents()==o)
                return true;//returns true if temp is equal to the given object
        return false;//returns false otherwise
    }

    @Override
    public Iterator<F> iterator() {
        return new NeatIterator<F>(head);
    }

    @Override
    public Object[] toArray() {
        return new Object[0]; //note
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;  //note
    }

    @Override
    public boolean add(F f) {
        if (f!=null){
            NeatNode<F> nn = new NeatNode<>();
            nn.setContents(f);//sets nn to contents of f
            nn.next = head;//sets original head of list as nn's next node
            head = nn;//sets nn as head
            return true;
        }
        return false;//returns false if f null
    }

    @Override
    public boolean remove(Object o) {
        if (o==head.getContents()){
            head = head.next;// if the list has only one number, assigns the head to its next node(null)
        } else {
            for (NeatNode temp = head; temp!=null; temp = temp.next){
                if (temp.next.getContents()==o) {//checks if temps next node is the object to remove
                    temp.next = temp.next.next;//assigns temps next node to the objects next node, removing it from the list
                    return true;
                }
            }
        }
        return false;//false in not there
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends F> c) {
        return false;
    }

    @Override
    public boolean addAll(int index, Collection<? extends F> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {
        head=null;
    }

    @Override
    public F get(int index) {
        NeatNode temp = head;
        while (indexOf(temp)<index)
            temp=temp.next;//goes through list until the index of temp equals index inputted
        return (F) temp.getContents();//returns object at given index
    }

    @Override
    public F set(int index, F element) {
        NeatNode temp = head;
        while (indexOf(temp)<index) {
            temp = temp.next;//goes through list until the index of temp equals index inputted
        }
        temp.setContents(element);
        return (F) temp.getContents();
    }

    @Override
    public void add(int index, F element) {

    }

    @Override
    public F remove(int index) {
        NeatNode h;
        if (get(index)==head) {
            h = head;
            head = head.next;//null
            return (F) h.getContents();
        }
        NeatNode temp = head;
        while (indexOf(temp) < index - 1) {
            temp = temp.next;//makes temp the object before the object to remove
        }
        NeatNode removedNode=temp.next;
        temp.next=temp.next.next;//sets temps next node to the object to remove next node
        return (F) removedNode.getContents();//returns removed object
    }

    @Override
    public int indexOf(Object o) {
        NeatNode temp = head;
        int i = 0;

        while (temp != o) {
            temp = temp.next;
            i++;//adds 1 to the index until temp is equal to the object
        }

        return i;//returns index
    }

    @Override
    public int lastIndexOf(Object o) {
        return 0;
    }

    @Override
    public ListIterator<F> listIterator() {
        return null;
    }

    @Override
    public ListIterator<F> listIterator(int index) {
        return null;
    }

    @Override
    public List<F> subList(int fromIndex, int toIndex) {
        return null;
    }

    public class NeatIterator<K> implements Iterator<K> {//allows for each loops in list
        private NeatNode<K> pos; //Current position
        public NeatIterator(NeatNode<K> fnode) { pos=fnode; }
        @Override
        public boolean hasNext() {
            return pos!=null;
        }
        @Override
        public K next() {
            NeatNode<K> temp=pos;
            pos=pos.next;
            return temp.getContents();
        }
    }

    // INNER CLASS FOR NODES
    public class NeatNode<N> {
        public NeatNode<N> next=null;
        private N contents; //ADT reference!
        public N getContents() { return contents; }
        public void setContents(N c) { contents=c; }
    }

}

