import axios from 'axios';

export default function promiseMiddleware({ dispatch, getState }) {
  return next => action => {

    // do execute action define as function

    if (typeof action === 'function') {
      return action(dispatch, getState);
    }

    const { promise, types, afterSuccess, ...rest } = action;
    if (!action.promise) {
      return next(action);
    }

    const [REQUEST, SUCCESS, FAILURE] = types;
    next({...rest, type: REQUEST});

    // do REST request

    const onFulfilled = result => {
      next({...rest, result, type: SUCCESS});
      if (afterSuccess) {
        afterSuccess(dispatch, getState, result);
      }
    };

    const onRejected = (error) => {
      next({...rest, error, type: FAILURE})
    };

    return promise(axios)
      .then(onFulfilled, onRejected)
      .catch(error => {
        console.error('PROMISE MIDDLEWARE ERROR:', error);
        onRejected(error)
      });
  };
}
