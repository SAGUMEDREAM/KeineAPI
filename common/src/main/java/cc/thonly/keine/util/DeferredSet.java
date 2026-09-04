package cc.thonly.keine.util;

import com.google.common.base.Suppliers;
import org.jspecify.annotations.NonNull;

import java.util.*;
import java.util.function.Supplier;

public class DeferredSet<E> implements Set<E> {

    private final Supplier<Set<E>> supplier;
    private final Set<E> delayObjects;

    public DeferredSet(Supplier<Set<E>> supplier) {
        this.supplier = Suppliers.memoize(supplier::get);
        this.delayObjects = new LinkedHashSet<>();
    }

    public DeferredSet(Set<E> set) {
        this.supplier = Suppliers.memoize(() -> set);
        this.delayObjects = new LinkedHashSet<>();
    }

    /**
     * 将所有延迟添加的元素真正加入底层 Set。
     */
    private void flush() {
        if (delayObjects.isEmpty()) {
            return;
        }

        this.set().addAll(delayObjects);
        delayObjects.clear();
    }

    /**
     * 获取真正的 Set。
     * <p>
     * 任何读取行为都会触发 flush。
     */
    public Set<E> set() {
        Set<E> set = this.supplier.get();

        if (!delayObjects.isEmpty()) {
            set.addAll(delayObjects);
            delayObjects.clear();
        }

        return set;
    }

    @Override
    public int size() {
        return this.set().size();
    }

    @Override
    public boolean isEmpty() {
        return this.set().isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return this.set().contains(o);
    }

    @NonNull
    @Override
    public Iterator<E> iterator() {
        return this.set().iterator();
    }

    @NonNull
    @Override
    public Object @NonNull [] toArray() {
        return this.set().toArray();
    }

    @NonNull
    @Override
    public <T> T @NonNull [] toArray(T[] a) {
        return this.set().toArray(a);
    }

    /**
     * 新增元素时只进入 delayObjects，
     * 不立即修改真正的 Set。
     */
    @Override
    public boolean add(E e) {
        return delayObjects.add(e);
    }

    @Override
    public boolean remove(Object o) {
        // remove 属于修改行为。
        // 如果元素还在延迟区，直接从延迟区删除即可。
        if (delayObjects.remove(o)) {
            return true;
        }

        return this.set().remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return this.set().containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return delayObjects.addAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        flush();
        return this.set().retainAll(c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = delayObjects.removeAll(c);
        return this.set().removeAll(c) || changed;
    }

    @Override
    public void clear() {
        delayObjects.clear();
        this.set().clear();
    }
}