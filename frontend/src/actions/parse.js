export const PARSE_BOOK = 'parse/PARSE_BOOK';
export const PARSE_BOOK_SUCCESS = 'parse/PARSE_BOOK_SUCCESS';
export const PARSE_BOOK_FAIL = 'parse/PARSE_BOOK_FAIL';

export const PARSE_TEXT = 'parse/PARSE_TEXT';
export const PARSE_TEXT_SUCCESS = 'parse/PARSE_TEXT_SUCCESS';
export const PARSE_TEXT_FAIL = 'parse/PARSE_TEXT_FAIL';

export const INDEX_BUILD = 'parse/INDEX_BUILD';
export const INDEX_BUILD_SUCCESS = 'parse/INDEX_BUILD_SUCCESS';
export const INDEX_BUILD_FAIL = 'parse/INDEX_BUILD_FAIL';

export const INDEX_BOOK = 'parse/INDEX_BOOK';
export const INDEX_BOOK_SUCCESS = 'parse/INDEX_BOOK_SUCCESS';
export const INDEX_BOOK_FAIL = 'parse/INDEX_BOOK_FAIL';

export function parseBook() {
    return {
        types: [PARSE_BOOK, PARSE_BOOK_SUCCESS, PARSE_BOOK_FAIL],
        promise: client => client.get('/api/parse/book')
    };
}

export function parseText() {
    return {
        types: [PARSE_TEXT, PARSE_TEXT_SUCCESS, PARSE_TEXT_FAIL],
        promise: client => client.get('/api/parse/text')
    };
}

export function indexBuild() {
    return {
        types: [INDEX_BUILD, INDEX_BUILD_SUCCESS, INDEX_BUILD_FAIL],
        promise: client => client.get('/api/parse/text')
    };
}

export function indexBook() {
    return {
        types: [INDEX_BOOK, INDEX_BOOK_SUCCESS, INDEX_BOOK_FAIL],
        promise: client => client.get('/api/index/book')
    };
}