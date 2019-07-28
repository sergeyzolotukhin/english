import React, {Component} from 'react';
import {connect} from 'react-redux';
import {SEARCH, SEARCH_SUCCESS, SEARCH_FAIL} from 'reducers/search';

export class SearchPage extends Component {
    state = {
        query: ""
    };

    handleInputChange = (e) => {
        let value = e.target.value;
        this.setState({query: value});
    };

    handleSubmit = (e) => {
        e.preventDefault();
        const {query} = this.state;
        this.props.onSearch(query);
    };

    render() {
        const items = this.props.items;
        const list = items.map((item, index) => <li key={index}>{item}</li>);

        return (
            <div>
                <form className="pure-form" onSubmit={this.handleSubmit}>
                    <fieldset>
                        <input type="text" id="query" placeholder="Please type text"
                               onChange={this.handleInputChange}/>
                    </fieldset>
                </form>

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

const mapDispatchToProps = dispatch => ({
   onSearch: query => dispatch({
       types: [SEARCH, SEARCH_SUCCESS, SEARCH_FAIL],
       promise: client => client.get('/api/search?query=' + query)})
});

export default connect(mapStateToProps, mapDispatchToProps)(SearchPage);
