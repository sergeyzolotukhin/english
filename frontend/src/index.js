import ReactDOM from 'react-dom';
import React from 'react';
import {Provider} from 'react-redux';
import {bindActionCreators} from 'redux';
import initStore from 'config/store';
import {hashHistory} from 'react-router'
import {syncHistoryWithStore} from 'react-router-redux'
import 'bootstrap/dist/css/bootstrap.css';

import {Router} from 'react-router';
import getRoutes from 'router/router';

const store = initStore();
const history = syncHistoryWithStore(hashHistory, store);

const actions = bindActionCreators({}, store.dispatch);

ReactDOM.render(
    <Provider store={store}>
        <div>
            <Router history={history} routes={getRoutes()}/>
        </div>
    </Provider>,
    document.getElementById('root')
);
