package ProjectTracker;

/**
 * @author Abdul Chaudhary
 * 
 * Generic doubly linked list implementation with enhanced functionality
 * @param <T> the type of elements in the list
 */
public class LinkedList<T> {
    private Node<T> head;
    private Node<T> tail;
    private int size;

    /**
     * Node class for linked list elements
     * @param <T> the type of element stored in the node
     */
    private static class Node<T> {
        T data;
        Node<T> next;
        Node<T> prev;

        Node(T data) {
            this.data = data;
            this.next = null;
            this.prev = null;
        }
    }

    /**
     * Constructs an empty linked list
     */
    public LinkedList() {
        head = null;
        tail = null;
        size = 0;
    }

    /**
     * Adds an element to the end of the list
     * @param element element to add
     * @throws IllegalArgumentException if element is null
     */
    public void add(T element) {
        if (element == null) {
            throw new IllegalArgumentException("Cannot add null element");
        }

        Node<T> newNode = new Node<>(element);
        if (isEmpty()) {
            head = tail = newNode;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }
        size++;
    }

    /**
     * Adds an element at specified index
     * @param index index at which to add element
     * @param element element to add
     * @throws IndexOutOfBoundsException if index is invalid
     * @throws IllegalArgumentException if element is null
     */
    public void add(int index, T element) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        if (element == null) {
            throw new IllegalArgumentException("Cannot add null element");
        }

        if (index == size) {
            add(element);
            return;
        }

        Node<T> newNode = new Node<>(element);
        if (index == 0) {
            newNode.next = head;
            head.prev = newNode;
            head = newNode;
        } else {
            Node<T> current = getNode(index);
            newNode.next = current;
            newNode.prev = current.prev;
            current.prev.next = newNode;
            current.prev = newNode;
        }
        size++;
    }

    /**
     * Removes and returns element at specified index
     * @param index index of element to remove
     * @return removed element
     * @throws IndexOutOfBoundsException if index is invalid
     */
    public T remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        Node<T> current = getNode(index);
        T data = current.data;

        if (size == 1) {
            head = tail = null;
        } else if (index == 0) {
            head = head.next;
            head.prev = null;
        } else if (index == size - 1) {
            tail = tail.prev;
            tail.next = null;
        } else {
            current.prev.next = current.next;
            current.next.prev = current.prev;
        }
        size--;
        return data;
    }

    /**
     * Removes first occurrence of specified element
     * @param element element to remove
     * @return true if element was found and removed
     */
    public boolean remove(T element) {
        Node<T> current = head;
        while (current != null) {
            if (current.data.equals(element)) {
                if (current == head) {
                    head = head.next;
                    if (head != null) head.prev = null;
                    else tail = null;
                } else if (current == tail) {
                    tail = tail.prev;
                    tail.next = null;
                } else {
                    current.prev.next = current.next;
                    current.next.prev = current.prev;
                }
                size--;
                return true;
            }
            current = current.next;
        }
        return false;
    }

    /**
     * Gets element at specified index
     * @param index index of element to get
     * @return element at specified index
     * @throws IndexOutOfBoundsException if index is invalid
     */
    public T get(int index) {
        return getNode(index).data;
    }

    /**
     * Sets element at specified index
     * @param index index at which to set element
     * @param element element to set
     * @throws IndexOutOfBoundsException if index is invalid
     * @throws IllegalArgumentException if element is null
     */
    public void set(int index, T element) {
        if (element == null) {
            throw new IllegalArgumentException("Cannot set null element");
        }
        getNode(index).data = element;
    }

    /**
     * Gets node at specified index
     * @param index index of node to get
     * @return node at specified index
     * @throws IndexOutOfBoundsException if index is invalid
     */
    private Node<T> getNode(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        Node<T> current;
        if (index < size / 2) {
            current = head;
            for (int i = 0; i < index; i++) {
                current = current.next;
            }
        } else {
            current = tail;
            for (int i = size - 1; i > index; i--) {
                current = current.prev;
            }
        }
        return current;
    }

    /**
     * Checks if list contains specified element
     * @param element element to check for
     * @return true if element is found
     */
    public boolean contains(T element) {
        Node<T> current = head;
        while (current != null) {
            if (current.data.equals(element)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    /**
     * Returns index of first occurrence of specified element
     * @param element element to find
     * @return index of element, or -1 if not found
     */
    public int indexOf(T element) {
        Node<T> current = head;
        int index = 0;
        while (current != null) {
            if (current.data.equals(element)) {
                return index;
            }
            current = current.next;
            index++;
        }
        return -1;
    }

    /**
     * Clears all elements from the list
     */
    public void clear() {
        head = null;
        tail = null;
        size = 0;
    }

    /**
     * Returns current size of the list
     * @return current size
     */
    public int size() {
        return size;
    }

    /**
     * Checks if list is empty
     * @return true if list is empty
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Returns first element in the list
     * @return first element
     * @throws IllegalStateException if list is empty
     */
    public T getFirst() {
        if (isEmpty()) {
            throw new IllegalStateException("List is empty");
        }
        return head.data;
    }

    /**
     * Returns last element in the list
     * @return last element
     * @throws IllegalStateException if list is empty
     */
    public T getLast() {
        if (isEmpty()) {
            throw new IllegalStateException("List is empty");
        }
        return tail.data;
    }
}