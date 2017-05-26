package br.com.doit.commons.foundation;

import static br.com.doit.commons.foundation.ERXKeyValueCast.cast;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSKeyValueCoding;

/**
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 */
public class TestERXKeyValeuCast {
    @Test
    public void returnCastedValueWhenValueIsNotNull() throws Exception {
        String result = cast("value").as(String.class);

        assertThat(result, is("value"));
    }

    @Test
    public void returnNullWhenValueIsNull() throws Exception {
        String result = cast(null).as(String.class);

        assertThat(result, nullValue());
    }

    @Test
    public void returnNullWhenValueIsNullKeyValue() throws Exception {
        String result = cast(NSKeyValueCoding.NullValue).as(String.class);

        assertThat(result, nullValue());
    }

    @Test
    public void returnCastedNSArrayWhenValueIsNotNull() throws Exception {
        NSArray<String> result = cast(new NSArray<>("value")).asNSArrayOf(String.class);

        assertThat(result, hasItem("value"));
    }

    @Test
    public void returnEmptyNSArrayWhenValueIsNull() throws Exception {
        NSArray<String> result = cast(null).asNSArrayOf(String.class);

        assertThat(result, notNullValue());
        assertThat(result.isEmpty(), is(true));
    }

    @Test
    public void returnEmptyNSArrayWhenValueIsNullKeyValue() throws Exception {
        NSArray<String> result = cast(NSKeyValueCoding.NullValue).asNSArrayOf(String.class);

        assertThat(result, notNullValue());
        assertThat(result.isEmpty(), is(true));
    }

    @Test
    public void returnCastedNSDictionaryWhenValueIsNotNull() throws Exception {
        NSDictionary<String, Object> result = cast(new NSDictionary<>("value", "key")).asNSDictionayOf(String.class, Object.class);

        assertThat(result.containsKey("key"), is(true));
        assertThat(result.get("key"), is("value"));
    }

    @Test
    public void returnCastedNSDictionaryWhenValueIsNull() throws Exception {
        NSDictionary<String, Object> result = cast(null).asNSDictionayOf(String.class, Object.class);

        assertThat(result, notNullValue());
        assertThat(result.isEmpty(), is(true));
    }

    @Test
    public void returnCastedNSDictionaryWhenValueIsNullKeyValue() throws Exception {
        NSDictionary<String, Object> result = cast(NSKeyValueCoding.NullValue).asNSDictionayOf(String.class, Object.class);

        assertThat(result, notNullValue());
        assertThat(result.isEmpty(), is(true));
    }
}
