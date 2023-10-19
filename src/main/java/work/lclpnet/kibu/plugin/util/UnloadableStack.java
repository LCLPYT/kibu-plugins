package work.lclpnet.kibu.plugin.util;

import work.lclpnet.mplugins.ext.Unloadable;

import java.util.Stack;
import java.util.function.Supplier;

public class UnloadableStack<T extends Unloadable> implements Unloadable {

    private final Supplier<T> factory;
    private T current = null;
    private Stack<T> stack = null;

    public UnloadableStack(Supplier<T> factory) {
        this.factory = factory;
    }

    public void push() {
        synchronized (this) {
            if (stack == null) {
                stack = new Stack<>();
            }

            if (current != null) {
                stack.push(current);
            }

            current = null;
        }
    }

    public void pop() {
        synchronized (this) {
            if (current != null) {
                current.unload();
            }

            if (stack == null || stack.isEmpty()) {
                current = null;
                return;
            }

            current = stack.pop();
        }
    }

    @Override
    public void unload() {
        synchronized (this) {
            if (current != null) {
                current.unload();
            }

            if (stack != null) {
                while (!stack.isEmpty()) {
                    T element = stack.pop();
                    element.unload();
                }
            }

            current = null;
            stack = null;
        }
    }

    protected T current() {
        synchronized (this) {
            if (current == null) {
                current = factory.get();
            }

            return current;
        }
    }
}
