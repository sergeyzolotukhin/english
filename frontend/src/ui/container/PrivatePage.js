import React, {Component} from 'react';
import {connect} from 'react-redux';

export class PrivatePage extends Component {

  render() {
    return (
      <div>
        Admin page
      </div>
    )
  }
}

export default connect(

)(PrivatePage);
