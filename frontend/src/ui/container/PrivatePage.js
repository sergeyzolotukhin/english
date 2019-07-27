import React, {Component} from 'react';
import {connect} from 'react-redux';

export class PrivatePage extends Component {

  render() {
    return (
      <div>
          <h2>Private page</h2>
      </div>
    )
  }
}

export default connect(
    state => ({})
)(PrivatePage);
