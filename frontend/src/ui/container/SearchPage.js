import React, {Component} from 'react';
import {connect} from 'react-redux';
import {search} from 'reducers/search';

export class SearchPage extends Component {
    render() {
        const items = this.props.items;
        const list = items.map((item, index) => <li key={index}>{item}</li>);

        return (
            <div>
                <h2>Search result:</h2>
                <ul>
                    {list}
                </ul>
                <button onClick={this.props.search}>
                    Fetch
                </button>
            </div>
        );
    }
}

export default connect(
  state => ({items: state.search.items}),
  {search}
)(SearchPage);
