package com.sxzheng.tasklibrary;

/**
 * @author zheng.
 */
public interface MessageStack<E> {

    boolean isEmpty();

    void clear();

    E peek();

    E pop();

    E push(E e);

    E findElementBy();

}
