package ua.in.sz.english.proucer.consumer;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
class Message {
    private final int no;

    Message(int no) {
        this.no = no;
    }
}
