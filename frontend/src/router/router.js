import React from 'react';
import {Route, IndexRoute} from 'react-router';

import App from 'container/App';
import SearchPage from 'container/SearchPage';
import AdminPage from 'container/AdminPage';

export default () => (
    <Route path="/" name="app" component={App}>
        <IndexRoute component={SearchPage}/>
        <Route path="private" component={AdminPage}/>
    </Route>
);
