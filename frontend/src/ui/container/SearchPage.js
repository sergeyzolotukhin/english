import React, {Component} from 'react';
import {connect} from 'react-redux';
import {search} from 'reducers/search';

export class SearchPage extends Component {
    render() {
        const items = this.props.items;
        const list = items.map((item, index) => <li key={index}>{item}</li>);

        return (
            <div>
                <button onClick={this.props.search}>
                    Fetch
                </button>

                <ul>
                    {list}
                </ul>
            </div>
        );
    }
}

const mapStateToProps = state => {
    return {
        items: state.search.items
    }
};

export default connect(mapStateToProps,
  {search}
)(SearchPage);
