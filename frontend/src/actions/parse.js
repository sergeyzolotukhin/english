export const INDEX_BOOK = 'parse/INDEX_BOOK';
export const INDEX_BOOK_SUCCESS = 'parse/INDEX_BOOK_SUCCESS';
export const INDEX_BOOK_FAIL = 'parse/INDEX_BOOK_FAIL';

export function indexBook() {
    return {
        types: [INDEX_BOOK, INDEX_BOOK_SUCCESS, INDEX_BOOK_FAIL],
        promise: client => client.get('/api/index/book')
    };
}