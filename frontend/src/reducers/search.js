const FETCH = 'search/FETCH';
const FETCH_SUCCESS = 'search/FETCH_SUCCESS';
const FETCH_FAIL = 'search/FETCH_FAIL';

export const SEARCH = 'search/SEARCH';
export const SEARCH_SUCCESS = 'search/SEARCH_SUCCESS';
export const SEARCH_FAIL = 'search/SEARCH_FAIL';

const initialState = {
    items: []
};

// Reducer

export default function searchReducer(state = initialState, action) {
    switch (action.type) {
        case FETCH_SUCCESS:
            return {
                ...state,
                items: action.result.data
            };
        case SEARCH_SUCCESS:
            return {
                ...state,
                items: action.result.data
            };
        default:
            return state;
    }
}

// Actions
export function doSearch(query) {
    return {
        types: [SEARCH, SEARCH_SUCCESS, SEARCH_FAIL],
        promise: client => client.get('/api/search?query=' + query)
    };
}

export function searchActionCreator() {
    return {
        types: [FETCH, FETCH_SUCCESS, FETCH_FAIL],
        promise: client => client.get('/api/search')
    };
}
