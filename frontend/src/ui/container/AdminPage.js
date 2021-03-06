import React, {Component} from 'react';
import {connect} from 'react-redux';
import {indexBook} from './../../actions/parse'

export class AdminPage extends Component {
    render() {
        return (
            <div className="container mt-2 card">
                <div className="col-12 card-body">Rebuild index form all book locate in source directory
                    <input type="button" value="Build Index" className="btn btn-outline-secondary float-right"
                           onClick={this.props.indexBook}/>
                </div>
            </div>
        )
    }
}

const mapDispatchToProps = {
    indexBook
};

export default connect(null, mapDispatchToProps)(AdminPage);
