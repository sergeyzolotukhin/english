package ua.in.sz.english;

import lombok.RequiredArgsConstructor;

import java.util.Iterator;

@RequiredArgsConstructor(staticName = "of")
class PageRange implements Iterable<Integer> {
    private final int from;
    private final int to;

    @Override
    public Iterator<Integer> iterator() {
        return new Iterator<Integer>() {
            int current = from;

            @Override
            public boolean hasNext() {
                return current <= to;
            }

            @Override
            public Integer next() {
                return current++;
            }
        };
    }
}
