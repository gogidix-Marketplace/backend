package com.gogidix.aiservices.aifrauddetectionservice.shared.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Utility class for collection operations.
 * <p>
 * Provides methods for checking collections, extracting elements,
 * and common collection manipulations.
 */
@Slf4j
@UtilityClass
public class CollectionUtil {

    /**
     * Checks if a collection is null or empty.
     *
     * @param collection the collection to check
     * @return true if the collection is null or empty
     */
    public boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * Checks if a collection is not empty.
     *
     * @param collection the collection to check
     * @return true if the collection is not empty
     */
    public boolean isNotEmpty(Collection<?> collection) {
        return collection != null && !collection.isEmpty();
    }

    /**
     * Checks if a map is null or empty.
     *
     * @param map the map to check
     * @return true if the map is null or empty
     */
    public boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    /**
     * Checks if a map is not empty.
     *
     * @param map the map to check
     * @return true if the map is not empty
     */
    public boolean isNotEmpty(Map<?, ?> map) {
        return map != null && !map.isEmpty();
    }

    /**
     * Checks if an array is null or empty.
     *
     * @param array the array to check
     * @return true if the array is null or empty
     */
    public boolean isEmpty(Object[] array) {
        return array == null || array.length == 0;
    }

    /**
     * Gets the first element of a list.
     *
     * @param list the list
     * @param <T>  the type of elements
     * @return the first element, or null if the list is empty
     */
    public <T> T first(List<T> list) {
        if (isEmpty(list)) {
            return null;
        }
        return list.get(0);
    }

    /**
     * Gets the first element of a collection.
     *
     * @param collection the collection
     * @param <T>        the type of elements
     * @return the first element, or null if the collection is empty
     */
    public <T> T first(Collection<T> collection) {
        if (isEmpty(collection)) {
            return null;
        }
        return collection.iterator().next();
    }

    /**
     * Gets the last element of a list.
     *
     * @param list the list
     * @param <T>  the type of elements
     * @return the last element, or null if the list is empty
     */
    public <T> T last(List<T> list) {
        if (isEmpty(list)) {
            return null;
        }
        return list.get(list.size() - 1);
    }

    /**
     * Gets the element at the specified index, returning null if out of bounds.
     *
     * @param list  the list
     * @param index the index
     * @param <T>   the type of elements
     * @return the element at the index, or null if out of bounds
     */
    public <T> T getSafe(List<T> list, int index) {
        if (list == null || index < 0 || index >= list.size()) {
            return null;
        }
        return list.get(index);
    }

    /**
     * Partitions a list into sublists of a specified size.
     *
     * @param list       the list to partition
     * @param batchSize  the size of each partition
     * @param <T>        the type of elements
     * @return a list of partitioned lists
     */
    public <T> List<List<T>> partition(List<T> list, int batchSize) {
        if (isEmpty(list) || batchSize <= 0) {
            return new ArrayList<>();
        }

        List<List<T>> partitions = new ArrayList<>();
        for (int i = 0; i < list.size(); i += batchSize) {
            partitions.add(list.subList(i, Math.min(i + batchSize, list.size())));
        }

        return partitions;
    }

    /**
     * Creates a new empty list if the provided list is null.
     *
     * @param list the list
     * @param <T>  the type of elements
     * @return the original list, or a new empty list if null
     */
    public <T> List<T> orEmpty(List<T> list) {
        return list != null ? list : new ArrayList<>();
    }

    /**
     * Creates a new empty set if the provided set is null.
     *
     * @param set the set
     * @param <T> the type of elements
     * @return the original set, or a new empty set if null
     */
    public <T> Set<T> orEmpty(Set<T> set) {
        return set != null ? set : new HashSet<>();
    }

    /**
     * Creates a new empty map if the provided map is null.
     *
     * @param map the map
     * @param <K> the type of keys
     * @param <V> the type of values
     * @return the original map, or a new empty map if null
     */
    public <K, V> Map<K, V> orEmpty(Map<K, V> map) {
        return map != null ? map : new HashMap<>();
    }

    /**
     * Returns a new list with the specified element removed.
     *
     * @param list     the original list
     * @param element  the element to remove
     * @param <T>      the type of elements
     * @return a new list without the element
     */
    public <T> List<T> remove(List<T> list, T element) {
        if (isEmpty(list)) {
            return new ArrayList<>();
        }

        List<T> result = new ArrayList<>(list);
        result.remove(element);
        return result;
    }

    /**
     * Returns a new list with only unique elements.
     *
     * @param list the list
     * @param <T>  the type of elements
     * @return a list with unique elements
     */
    public <T> List<T> distinct(List<T> list) {
        if (isEmpty(list)) {
            return new ArrayList<>();
        }
        return list.stream().distinct().collect(Collectors.toList());
    }

    /**
     * Splits a collection into two lists based on a predicate.
     *
     * @param collection the collection to split
     * @param predicate  the predicate to test
     * @param <T>        the type of elements
     * @return a map with "true" and "false" keys containing the split lists
     */
    public <T> Map<Boolean, List<T>> split(Collection<T> collection, java.util.function.Predicate<T> predicate) {
        if (isEmpty(collection)) {
            Map<Boolean, List<T>> result = new HashMap<>();
            result.put(true, new ArrayList<>());
            result.put(false, new ArrayList<>());
            return result;
        }

        return collection.stream()
                .collect(Collectors.partitioningBy(predicate));
    }

    /**
     * Groups elements of a collection by a key extractor function.
     *
     * @param collection  the collection to group
     * @param keyExtractor the function to extract grouping keys
     * @param <T>         the type of elements
     * @param <K>         the type of keys
     * @return a map grouping elements by key
     */
    public <T, K> Map<K, List<T>> groupBy(Collection<T> collection, Function<T, K> keyExtractor) {
        if (isEmpty(collection)) {
            return new HashMap<>();
        }

        return collection.stream()
                .collect(Collectors.groupingBy(keyExtractor));
    }

    /**
     * Transforms a collection using a mapper function.
     *
     * @param collection the collection to transform
     * @param mapper     the mapper function
     * @param <T>        the input type
     * @param <R>        the output type
     * @return a list of transformed elements
     */
    public <T, R> List<R> map(Collection<T> collection, Function<T, R> mapper) {
        if (isEmpty(collection)) {
            return new ArrayList<>();
        }

        return collection.stream()
                .map(mapper)
                .collect(Collectors.toList());
    }

    /**
     * Filters a collection using a predicate.
     *
     * @param collection the collection to filter
     * @param predicate  the predicate
     * @param <T>        the type of elements
     * @return a list of filtered elements
     */
    public <T> List<T> filter(Collection<T> collection, java.util.function.Predicate<T> predicate) {
        if (isEmpty(collection)) {
            return new ArrayList<>();
        }

        return collection.stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }

    /**
     * Finds the first element matching a predicate.
     *
     * @param collection the collection to search
     * @param predicate  the predicate
     * @param <T>        the type of elements
     * @return the first matching element, or null if not found
     */
    public <T> T findFirst(Collection<T> collection, java.util.function.Predicate<T> predicate) {
        if (isEmpty(collection)) {
            return null;
        }

        return collection.stream()
                .filter(predicate)
                .findFirst()
                .orElse(null);
    }

    /**
     * Checks if any element in the collection matches the predicate.
     *
     * @param collection the collection to check
     * @param predicate  the predicate
     * @param <T>        the type of elements
     * @return true if any element matches
     */
    public <T> boolean anyMatch(Collection<T> collection, java.util.function.Predicate<T> predicate) {
        if (isEmpty(collection)) {
            return false;
        }

        return collection.stream().anyMatch(predicate);
    }

    /**
     * Checks if all elements in the collection match the predicate.
     *
     * @param collection the collection to check
     * @param predicate  the predicate
     * @param <T>        the type of elements
     * @return true if all elements match
     */
    public <T> boolean allMatch(Collection<T> collection, java.util.function.Predicate<T> predicate) {
        if (isEmpty(collection)) {
            return true;
        }

        return collection.stream().allMatch(predicate);
    }

    /**
     * Calculates the intersection of two collections.
     *
     * @param coll1 the first collection
     * @param coll2 the second collection
     * @param <T>   the type of elements
     * @return a set containing the intersection
     */
    public <T> Set<T> intersection(Collection<T> coll1, Collection<T> coll2) {
        if (isEmpty(coll1) || isEmpty(coll2)) {
            return new HashSet<>();
        }

        Set<T> result = new HashSet<>(coll1);
        result.retainAll(coll2);
        return result;
    }

    /**
     * Calculates the union of two collections.
     *
     * @param coll1 the first collection
     * @param coll2 the second collection
     * @param <T>   the type of elements
     * @return a set containing the union
     */
    public <T> Set<T> union(Collection<T> coll1, Collection<T> coll2) {
        Set<T> result = new HashSet<>();
        if (coll1 != null) {
            result.addAll(coll1);
        }
        if (coll2 != null) {
            result.addAll(coll2);
        }
        return result;
    }

    /**
     * Calculates the difference of two collections (elements in coll1 but not in coll2).
     *
     * @param coll1 the first collection
     * @param coll2 the second collection
     * @param <T>   the type of elements
     * @return a set containing the difference
     */
    public <T> Set<T> difference(Collection<T> coll1, Collection<T> coll2) {
        if (isEmpty(coll1)) {
            return new HashSet<>();
        }
        if (isEmpty(coll2)) {
            return new HashSet<>(coll1);
        }

        Set<T> result = new HashSet<>(coll1);
        result.removeAll(coll2);
        return result;
    }

    /**
     * Converts an array to a list, returning empty list if null.
     *
     * @param array the array
     * @param <T>   the type of elements
     * @return a list containing the array elements
     */
    @SafeVarargs
    public <T> List<T> toList(T... array) {
        if (array == null || array.length == 0) {
            return new ArrayList<>();
        }
        return Arrays.asList(array);
    }

    /**
     * Creates an immutable list from the provided elements.
     *
     * @param elements the elements
     * @param <T>      the type of elements
     * @return an immutable list
     */
    @SafeVarargs
    public <T> List<T> immutableList(T... elements) {
        if (elements == null || elements.length == 0) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(Arrays.asList(elements));
    }

    /**
     * Creates an immutable set from the provided elements.
     *
     * @param elements the elements
     * @param <T>      the type of elements
     * @return an immutable set
     */
    @SafeVarargs
    public <T> Set<T> immutableSet(T... elements) {
        if (elements == null || elements.length == 0) {
            return Collections.emptySet();
        }
        return Collections.unmodifiableSet(new HashSet<>(Arrays.asList(elements)));
    }

    /**
     * Gets the size of a collection, returning 0 if null.
     *
     * @param collection the collection
     * @return the size, or 0 if null
     */
    public int size(Collection<?> collection) {
        return collection == null ? 0 : collection.size();
    }

    /**
     * Gets the size of a map, returning 0 if null.
     *
     * @param map the map
     * @return the size, or 0 if null
     */
    public int size(Map<?, ?> map) {
        return map == null ? 0 : map.size();
    }

    /**
     * Gets the size of an array, returning 0 if null.
     *
     * @param array the array
     * @return the size, or 0 if null
     */
    public int size(Object[] array) {
        return array == null ? 0 : array.length;
    }

    /**
     * Reverses a list.
     *
     * @param list the list to reverse
     * @param <T>  the type of elements
     * @return a new reversed list
     */
    public <T> List<T> reverse(List<T> list) {
        if (isEmpty(list)) {
            return new ArrayList<>();
        }

        List<T> reversed = new ArrayList<>(list);
        Collections.reverse(reversed);
        return reversed;
    }

    /**
     * Shuffles a list.
     *
     * @param list the list to shuffle
     * @param <T>  the type of elements
     * @return a new shuffled list
     */
    public <T> List<T> shuffle(List<T> list) {
        if (isEmpty(list)) {
            return new ArrayList<>();
        }

        List<T> shuffled = new ArrayList<>(list);
        Collections.shuffle(shuffled);
        return shuffled;
    }

    /**
     * Converts a collection to a comma-separated string.
     *
     * @param collection the collection
     * @return the comma-separated string
     */
    public String toCommaSeparatedString(Collection<?> collection) {
        if (isEmpty(collection)) {
            return "";
        }

        return collection.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
    }
}
