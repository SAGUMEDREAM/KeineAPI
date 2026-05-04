package cc.thonly.keine.api;

import java.util.List;
import java.util.function.Consumer;

public interface IEntryRegistry<C, E> {
    List<E> getEntries();

    void register(Consumer<C> accepter);
}
