package br.com.doit.commons.moshi.adapter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Set;
import java.util.stream.Collectors;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import br.com.doit.commons.moshi.annotation.SerializeNull;

public final class SerializeNullAdapter implements JsonAdapter.Factory {
    @Override
    public JsonAdapter<?> create(Type type, Set<? extends Annotation> annotations, Moshi moshi) {
        for (Annotation annotation : annotations) {
            if (annotation instanceof SerializeNull) {
                annotations = annotations.stream().filter(a -> !a.annotationType().equals(SerializeNull.class)).collect(Collectors.toSet());
                JsonAdapter<?> adapter = moshi.nextAdapter(this, type, annotations);
                return adapter.serializeNulls();
            }
        }
        return null;
    }

}
