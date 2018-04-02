package com.emav.javatest.services;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Created by Elinam on 02/04/2018.
 */
public class DataService {
    public <T> void insert(Supplier<T> function, Consumer<T> onSuccess, Consumer<Exception> onError) {
        try {
            T object = function.get();
            onSuccess.accept(object);
        } catch (Exception ex) {
            onError.accept(ex);
        }
    }

    public <T> void update(Supplier<T> function, Consumer<T> onSuccess, Consumer<Exception> onError) {
        try {
            T object = function.get();
            onSuccess.accept(object);
        } catch (Exception ex) {
            onError.accept(ex);
        }
    }

    public <T> void delete(Supplier<T> function, Consumer<T> onSuccess, Consumer<Exception> onError) {
        try {
            T object = function.get();
            onSuccess.accept(object);
        } catch (Exception ex) {
            onError.accept(ex);
        }
    }
}
