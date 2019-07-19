package ua.in.sz.english;

import java.util.Iterator;

class PageRange implements Iterable<Integer> {
    private final int from;
    private final int to;

    PageRange(int from, int to) {
        this.from = from;
        this.to = to;
    }

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
