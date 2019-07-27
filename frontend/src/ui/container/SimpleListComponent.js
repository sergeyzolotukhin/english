import React, {Component} from 'react';
import {connect} from 'react-redux';
import {fetchSimple} from 'reducers/simple';

export class ListComponent extends Component {
    render() {
        const items = this.props.items;
        const list = items.map((item, index) => <li key={index}>{item}</li>);

        return (
            <div>
                <h2>Search result:</h2>
                <ul>
                    {list}
                </ul>
                <button onClick={this.props.fetchSimple}>
                    Fetch
                </button>
            </div>
        );
    }
}

export default connect(
  state => ({items: state.simple.items}),
  {fetchSimple}
)(ListComponent);
