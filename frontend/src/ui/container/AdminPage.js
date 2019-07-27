import React, {Component} from 'react';
import {connect} from 'react-redux';
import {PARSE_BOOK, PARSE_BOOK_SUCCESS, PARSE_BOOK_FAIL} from 'reducers/parse';
import {PARSE_TEXT, PARSE_TEXT_SUCCESS, PARSE_TEXT_FAIL} from 'reducers/parse';
import {INDEX_BUILD, INDEX_BUILD_SUCCESS, INDEX_BUILD_FAIL} from 'reducers/parse';

export class AdminPage extends Component {
    render() {
        return (
            <div>
                <button className="pure-button" onClick={this.props.onParseBook}>
                    Parse PDF
                </button>
                <button className="pure-button" onClick={this.props.onParseText}>
                    Parse Text
                </button>
                <button className="pure-button" onClick={this.props.onBuildIndex}>
                    Build Index
                </button>
            </div>
        )
    }
}

const mapDispatchToProps = dispatch => ({
    onParseBook: () => dispatch({
        types: [PARSE_BOOK, PARSE_BOOK_SUCCESS, PARSE_BOOK_FAIL],
        promise: client => client.get('/api/parse/book')}),
    onParseText: () => dispatch({
        types: [PARSE_TEXT, PARSE_TEXT_SUCCESS, PARSE_TEXT_FAIL],
        promise: client => client.get('/api/parse/text')}),
    onBuildIndex: () => dispatch({
        types: [INDEX_BUILD, INDEX_BUILD_SUCCESS, INDEX_BUILD_FAIL],
        promise: client => client.get('/api/build/index')})
});

export default connect(null, mapDispatchToProps)(AdminPage);
