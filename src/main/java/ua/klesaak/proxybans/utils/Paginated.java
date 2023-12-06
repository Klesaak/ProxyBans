package ua.klesaak.proxybans.utils;

import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * A simple pagination utility
 *
 * @param <T> the element type
 */
public class Paginated<T> {
    private final List<T> content;

    public Paginated(Collection<T> content) {
        this.content = ImmutableList.copyOf(content);
    }

    public List<T> getContent() {
        return this.content;
    }

    public int getMaxPages(int entriesPerPage) {
        return (int) Math.ceil((double) this.content.size() / (double) entriesPerPage);
    }

    public List<Entry<T>> getPage(int pageNo, int pageSize) {
        if (pageNo < 1) {
            throw new IllegalArgumentException("pageNo cannot be less than 1: " + pageNo);
        }

        int first = (pageNo - 1) * pageSize;
        if (this.content.size() <= first) {
            throw new IllegalStateException("Content does not contain that many elements. (requested page: " + pageNo +
                    ", page size: " + pageSize + ", page first index: " + first + ", content size: " + this.content.size() + ")");
        }

        int last = first + pageSize - 1;
        List<Entry<T>> out = new ArrayList<>(pageSize);

        for (int i = first; i <= last && i < this.content.size(); i++) {
            out.add(new Entry<>(i + 1, this.content.get(i)));
        }

        return out;
    }

    public static final class Entry<T> {
        private final int position;
        private final T value;

        public Entry(int position, T value) {
            this.position = position;
            this.value = value;
        }

        public int position() {
            return this.position;
        }

        public T value() {
            return this.value;
        }

        @Override
        public String toString() {
            return this.position + ": " + this.value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Entry)) return false;
            Entry<?> entry = (Entry<?>) o;
            return this.position == entry.position && Objects.equals(this.value, entry.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.position, this.value);
        }
    }

}