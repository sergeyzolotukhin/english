import React, {Component} from 'react';
import {connect} from 'react-redux';
import {indexBook} from './../../actions/parse'

export class AdminPage extends Component {
    render() {
        return (
            <div>
                <button className="pure-button" onClick={this.props.indexBook}>
                    Build Index
                </button>
            </div>
        )
    }
}

const mapDispatchToProps = {
    indexBook
};

export default connect(null, mapDispatchToProps)(AdminPage);
