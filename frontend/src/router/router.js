import React from 'react';
import { Route, IndexRoute } from 'react-router';

import App from 'container/App';

import SimpleListComponent from 'container/SimpleListComponent';
import PrivatePage from 'container/PrivatePage';
import privateRoute from 'router/privateRoute';

export default () => (
  <Route path="/" name="app" component={App}>
    <IndexRoute component={SimpleListComponent}/>
    <Route path="private" component={privateRoute(PrivatePage)}/>
  </Route>
);
