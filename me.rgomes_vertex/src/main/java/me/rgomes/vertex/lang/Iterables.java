package me.rgomes.vertex.lang;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * This class contains a static factory methods intended to create {@link Iterable} objects
 * backed by different data structures.
 * <p>
 * The documentation for the methods contained in this class includes briefs description of the implementations. Such descriptions
 * should be regarded as implementation notes, rather than parts of the specification. Implementors should feel free to substitute
 * other algorithms, so long as the specification itself is adhered to. (For example, the algorithm used by a certain method
 * can be changed, but must satisfy the requirements stated in the documentation.)
 *
 * @author Richard Gomes<rgomes1997@yahoo.co.uk>
 */
public class Iterables {

    /**
     * Returns an unmodifiable view of the specified {@link Enumeration}.
     * <p>
     * This method allows modules to provide users with "read-only" access to internal enumerations.
     * Query operations on the returned list "read through" to the specified Enumeration, and attempts to modify the
     * returned Enumeration, whether direct or via its iterator, result in an UnsupportedOperationException.
     *
     * @param <T> is the type of elements in the specified Enumeration
     * @param en is an Enumeration<T>
     * @return an Iterable<T>
     */
    public static <T> Iterable<T> unmodifiableIterable(final Enumeration<T> en) {
        return new IterableEnumeration<T>(en);

    }

    /**
     * Returns an unmodifiable view of the specified {@link Iterator}.
     * <p>
     * This method allows modules to provide users with "read-only" access to internal iterators.
     * Query operations on the returned list "read through" to the specified Iterator, and attempts to modify the
     * returned Iterator, result in an UnsupportedOperationException.
     *
     * @param <T> is the type of elements in the specified Iterator
     * @param en is an Iterator<T>
     * @return an Iterable<T>
     */
    public static <T> Iterable<T> unmodifiableIterable(final Iterator<T> it) {
        return new IterableIterator<T>(it);
    }

    /**
     * Returns an unmodifiable view of the specified {@link List}.
     * <p>
     * This method allows modules to provide users with "read-only" access to internal enumerations.
     * Query operations on the returned list "read through" to the specified List, and attempts to modify the
     * returned List, whether direct or via its iterator, result in an UnsupportedOperationException.
     *
     * @param <T> is the type of elements in the specified List
     * @param en is an List<T>
     * @return an Iterable<T>
     */
    public static <T> Iterable<T> unmodifiableIterable(final List<T> list) {
        return new IterableIterator<T>(list.iterator());
    }

    /**
     * Returns an unmodifiable view of the specified {@link Set}.
     * <p>
     * This method allows modules to provide users with "read-only" access to internal enumerations.
     * Query operations on the returned list "read through" to the specified Set, and attempts to modify the
     * returned Set, whether direct or via its iterator, result in an UnsupportedOperationException.
     *
     * @param <T> is the type of elements in the specified Set
     * @param en is an Set<T>
     * @return an Iterable<T>
     */
    public static <T> Iterable<T> unmodifiableIterable(final Set<T> set) {
        return new IterableIterator<T>(set.iterator());
    }

    /**
     * Returns an unmodifiable view of the specified array <code>&lt;T&gt; T[]</code>.
     * <p>
     * This method allows modules to provide users with "read-only" access to internal array elements.
     * Query operations on the returned list "read through" to the specified array, and attempts to modify the
     * returned array, whether direct or via its iterator, result in an UnsupportedOperationException.
     *
     * @param <T> is the type of elements in the specified array
     * @param array is an <T> T[]
     * @return an Iterable<T>
     */
    public static <T> Iterable<T> unmodifiableIterable(final T[] array) {
        return new IterableArray<T>(array);
    }


    //
    // private static inner classes
    //

    private static class IterableEnumeration<T> implements Iterable<T> {

        private final Enumeration<T> en;

        private IterableEnumeration(final Enumeration<T> en) {
            this.en = en;
        }

        @Override
        public Iterator<T> iterator() {
            return new Iterator<T>() {
                public boolean hasNext() {
                    return en.hasMoreElements();
                }

                public T next() throws NoSuchElementException {
                    return en.nextElement();
                }

                public void remove() {
                    throw new UnsupportedOperationException();
                }
            };
        }
    }


    private static class IterableIterator<T> implements Iterable<T> {

        private final Iterator<T> it;

        private IterableIterator(final Iterator<T> it) {
            this.it = it;
        }

        @Override
        public Iterator<T> iterator() {
            return new Iterator<T>() {
                public boolean hasNext() {
                    return it.hasNext();
                }

                public T next() throws NoSuchElementException {
                    return it.next();
                }

                public void remove() {
                    throw new UnsupportedOperationException();
                }
            };
        }
    }

    private static class IterableArray<T> implements Iterable<T> {

        private final T[] array;

        private IterableArray(final T[] array) {
            this.array = array;
        }

        @Override
        public Iterator<T> iterator() throws NoSuchElementException {
            return new Iterator<T>() {
                private int i = 0;

                public boolean hasNext() {
                    return (i < array.length);
                }

                public T next() {
                    if (i >= array.length) throw new NoSuchElementException();
                    return array[i++];
                }

                public void remove() {
                    throw new UnsupportedOperationException();
                }
            };
        }
    }


}
