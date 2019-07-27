import { combineReducers } from 'redux';
import simple from './simple';
import { routerReducer as routing } from 'react-router-redux';

export default combineReducers({
  simple,
  routing
});
