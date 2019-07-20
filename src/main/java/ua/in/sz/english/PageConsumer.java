package ua.in.sz.english;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.BlockingQueue;

@Slf4j
@RequiredArgsConstructor
public class PageConsumer implements Runnable {
    private final BlockingQueue<PageDto> queue;

    @Override
    public void run() {
        while (true) {
            try {
                PageDto page = queue.take();

                if (PageDto.LAST_PAGE == page.getPageNo()) {
                    log.info("End process book: {}", page.getBookTitle());
                    break;
                }

                if (log.isTraceEnabled()) {
                    log.trace("Receive page: {}", page.getPageNo());
                }
            } catch (InterruptedException e) {
                log.error("Interrupted", e);
                break;
            }
        }
    }
}
