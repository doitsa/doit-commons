package br.com.doit.commons.foundation;

import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSKeyValueCoding;

/**
 * In some circumstances, when calling key-value code, one may receive a {@code NSKeyValueCoding.NullValue} as a result.
 * Dealing with this kind of condition is usually over complicated.
 * <p>
 * This class deals with {@code NSKeyValueCoding.NullValue} transparently. It allows casting of objects,
 * {@code NSArray}s and {@code NSDictionary}s in a straightforward way.
 *
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 */
public class ERXKeyValueCast {
    /**
     * Initial method to cast an object to a particular type, {@code NSArray} or {@code NSDictionary} of that type.
     * <p>
     * Usage:
     *
     * <pre>
     * cast(object).as(String.class);
     * cast(object).asNSArrayOf(String.class);
     * cast(object).asNSDictionaryOf(String.class);
     * </pre>
     *
     * @param object
     *            the object. It can be null or {@code NSKeyValueCoding.NullValue}.
     */
    public static ERXKeyValueCast cast(Object object) {
        return new ERXKeyValueCast(object);
    }

    private final Object object;

    private ERXKeyValueCast(Object object) {
        this.object = object;
    }

    /**
     * Casts an object to a particular type.
     *
     * @param type
     *            the type of the returned object.
     * @return Returns the object cast as the specified type or {@code null} if the object is equal to {@code null} or
     *         {@code NSKeyValueCoding.NullValue}.
     */
    @SuppressWarnings("unchecked")
    public <T> T as(Class<T> type) {
        if (isNullObject()) {
            return null;
        }

        return (T) object;
    }

    /**
     * Casts an object to a {@code NSArray} of a particular type.
     *
     * @param type
     *            the type of the items of the {@code NSArray}.
     * @return Returns the object cast as a {@code NSArray} of the specified type or an empty {@code NSArray} if the
     *         object is equal to {@code null} or {@code NSKeyValueCoding.NullValue}.
     */
    @SuppressWarnings("unchecked")
    public <T> NSArray<T> asNSArrayOf(Class<T> type) {
        if (isNullObject()) {
            return NSArray.emptyArray();
        }

        return (NSArray<T>) object;
    }

    /**
     * Casts an object to a {@code NSDictionary} of a particular key type and value type.
     *
     * @param keyType
     *            the type of the keys.
     * @param valueType
     *            the type of the values.
     * @return Returns the object cast as a {@code NSDictionary} of the specified key and value types or an empty
     *         {@code NSDictionary} if the object is equal to {@code null} or {@code NSKeyValueCoding.NullValue}.
     */
    @SuppressWarnings("unchecked")
    public <K, V> NSDictionary<K, V> asNSDictionayOf(Class<K> keyType, Class<V> valueType) {
        if (isNullObject()) {
            return NSDictionary.emptyDictionary();
        }

        return (NSDictionary<K, V>) object;
    }

    private boolean isNullObject() {
        return object == null || object == NSKeyValueCoding.NullValue;
    }
}
