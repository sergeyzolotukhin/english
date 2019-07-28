import React, {Component} from 'react';
import {connect} from 'react-redux';
import {parseBook, parseText, indexBuild, indexBook} from './../../actions/parse'

export class AdminPage extends Component {
    render() {
        return (
            <div>
                <button className="pure-button" onClick={this.props.parseBook}>
                    Parse PDF
                </button>
                <button className="pure-button" onClick={this.props.parseText}>
                    Parse Text
                </button>
                <button className="pure-button" onClick={this.props.indexBuild}>
                    Build Index
                </button>

                <button className="pure-button" onClick={this.props.indexBook}>
                    Build Index
                </button>
            </div>
        )
    }
}

const mapDispatchToProps = {
    parseBook,
    parseText,
    indexBuild,
    indexBook
};

export default connect(null, mapDispatchToProps)(AdminPage);
