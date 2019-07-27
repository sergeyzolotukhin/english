import React, {Component} from 'react';
import {connect} from 'react-redux';
import {search} from 'reducers/search';

export class SearchPage extends Component {
    render() {
        const items = this.props.items;
        const list = items.map((item, index) => <li key={index}>{item}</li>);

        return (
            <div>
                <form className="pure-form">
                    <fieldset>
                        <input type="text" id="query" placeholder="Please type text"/>
                        <button type="submit" className="pure-button"
                                onClick={this.props.search}>Search
                        </button>
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

const mapDispatchToProps = {
    search
};

export default connect(mapStateToProps, mapDispatchToProps)(SearchPage);
