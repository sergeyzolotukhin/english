const FETCH = 'search/FETCH';
const FETCH_SUCCESS = 'search/FETCH_SUCCESS';
const FETCH_FAIL = 'search/FETCH_FAIL';

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
        default:
            return state;
    }
}

// Actions

export function search() {
    return {
        types: [FETCH, FETCH_SUCCESS, FETCH_FAIL],
        promise: client => client.get('/api/search')
    };
}
