import React, {Component} from 'react';
import {connect} from 'react-redux';
import {searchSentence} from './../../reducers/search';

export class SearchPage extends Component {

    handleInputChange = (e) => {
        if ('Enter' === e.key) {
            this.props.searchSentence(e.target.value);
        }
    };

    render() {
        const items = this.props.items;
        const list = items.map((item, index) => <div className="row mt-2" key={index}>{item}</div>);

        return (
            <div className="container">
                <div className="row">
                    <input className="form-control col-12" type="text"
                           onKeyDown={this.handleInputChange}/>
                </div>
                {list}
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
    searchSentence
};

export default connect(mapStateToProps, mapDispatchToProps)(SearchPage);
