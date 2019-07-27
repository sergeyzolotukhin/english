import React, {Component} from 'react';
import {connect} from 'react-redux';

export class AdminPage extends Component {

  render() {
    return (
      <div>
        <button>
          Indexing
        </button>
      </div>
    )
  }
}

export default connect()(AdminPage);
