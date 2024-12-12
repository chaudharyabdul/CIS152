package ProjectTracker;

/**
 * @author Abdul Chaudhary
 * 
 * Generic dynamic array implementation with enhanced functionality
 * @param <T> the type of elements in the array
 */
public class DynamicArray<T> {
    private T[] array;
    private int size;
    private int capacity;
    private static final int INITIAL_CAPACITY = 10;
    private static final double GROWTH_FACTOR = 2.0;
    private static final double SHRINK_FACTOR = 0.25;

    /**
     * Constructs a new dynamic array with default initial capacity
     */
    @SuppressWarnings("unchecked")
    public DynamicArray() {
        this.capacity = INITIAL_CAPACITY;
        this.size = 0;
        this.array = (T[]) new Object[capacity];
    }

    /**
     * Constructs a new dynamic array with specified initial capacity
     * @param initialCapacity the initial capacity of the array
     * @throws IllegalArgumentException if initialCapacity is negative
     */
    @SuppressWarnings("unchecked")
    public DynamicArray(int initialCapacity) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("Initial capacity cannot be negative");
        }
        this.capacity = initialCapacity;
        this.size = 0;
        this.array = (T[]) new Object[capacity];
    }

    /**
     * Adds an element to the end of the array
     * @param element element to add
     * @throws IllegalArgumentException if element is null
     */
    public void add(T element) {
        if (element == null) {
            throw new IllegalArgumentException("Cannot add null element");
        }
        if (size == capacity) {
            resize(Math.max(capacity + 1, (int)(capacity * GROWTH_FACTOR)));
        }
        array[size++] = element;
    }

    /**
     * Adds an element at a specific index
     * @param index index at which to add the element
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
        if (size == capacity) {
            resize(Math.max(capacity + 1, (int)(capacity * GROWTH_FACTOR)));
        }
        System.arraycopy(array, index, array, index + 1, size - index);
        array[index] = element;
        size++;
    }

    /**
     * Removes and returns the element at the specified index
     * @param index index of element to remove
     * @return the removed element
     * @throws IndexOutOfBoundsException if index is invalid
     */
    public T remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        T removedElement = array[index];
        System.arraycopy(array, index + 1, array, index, size - index - 1);
        array[--size] = null;

        // Shrink array if necessary
        if (size > 0 && size < capacity * SHRINK_FACTOR) {
            resize(Math.max(INITIAL_CAPACITY, (int)(capacity / GROWTH_FACTOR)));
        }
        return removedElement;
    }

    /**
     * Removes the first occurrence of the specified element
     * @param element element to remove
     * @return true if element was found and removed
     */
    public boolean remove(T element) {
        for (int i = 0; i < size; i++) {
            if (array[i].equals(element)) {
                remove(i);
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the element at the specified index
     * @param index index of element to get
     * @return element at specified index
     * @throws IndexOutOfBoundsException if index is invalid
     */
    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        return array[index];
    }

    /**
     * Sets the element at the specified index
     * @param index index at which to set element
     * @param element element to set
     * @throws IndexOutOfBoundsException if index is invalid
     * @throws IllegalArgumentException if element is null
     */
    public void set(int index, T element) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        if (element == null) {
            throw new IllegalArgumentException("Cannot set null element");
        }
        array[index] = element;
    }

    /**
     * Clears all elements from the array
     */
    @SuppressWarnings("unchecked")
    public void clear() {
        this.capacity = INITIAL_CAPACITY;
        this.size = 0;
        this.array = (T[]) new Object[capacity];
    }

    /**
     * Checks if the array contains the specified element
     * @param element element to check for
     * @return true if element is found
     */
    public boolean contains(T element) {
        for (int i = 0; i < size; i++) {
            if (array[i].equals(element)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the index of the first occurrence of the specified element
     * @param element element to find
     * @return index of element, or -1 if not found
     */
    public int indexOf(T element) {
        for (int i = 0; i < size; i++) {
            if (array[i].equals(element)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Resizes the array to the specified capacity
     * @param newCapacity new capacity for the array
     */
    @SuppressWarnings("unchecked")
    private void resize(int newCapacity) {
        T[] newArray = (T[]) new Object[newCapacity];
        System.arraycopy(array, 0, newArray, 0, size);
        array = newArray;
        capacity = newCapacity;
    }

    /**
     * Returns the current size of the array
     * @return current size
     */
    public int size() { 
        return size; 
    }

    /**
     * Returns the current capacity of the array
     * @return current capacity
     */
    public int capacity() { 
        return capacity; 
    }

    /**
     * Checks if the array is empty
     * @return true if array is empty
     */
    public boolean isEmpty() { 
        return size == 0; 
    }

    /**
     * Trims the capacity to the current size
     */
    public void trimToSize() {
        if (size < capacity) {
            resize(size);
        }
    }
}