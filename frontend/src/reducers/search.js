export const SEARCH = 'search/SEARCH';
export const SEARCH_SUCCESS = 'search/SEARCH_SUCCESS';
export const SEARCH_FAIL = 'search/SEARCH_FAIL';

const initialState = {
    items: []
};

// Reducer

export default function searchReducer(state = initialState, action) {
    if (SEARCH_SUCCESS === action.type ) {
        return {
            ...state,
            items: action.result.data
        };
    } else {
        return state;
    }
}

// Actions

export function searchSentence(query) {
    return {
        types: [SEARCH, SEARCH_SUCCESS, SEARCH_FAIL],
        promise: client => client.get('/api/search?query=' + query)
    };
}
