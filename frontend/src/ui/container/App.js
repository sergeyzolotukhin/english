import React, {Component} from 'react';
import {Link} from 'react-router';
import {connect} from 'react-redux';

const TopMenu = (props) => {
    const items = props.items.map((item, key) => (
        <li className="nav-item" key={key}>
            <Link onlyActiveOnIndex={true} to={item.link} className="nav-link active">{item.label}</Link>
        </li>
    ));

    return (
        <ul className="nav nav-tabs mt-2">
            {items}
        </ul>
    );
};

export class App extends Component {
    render() {
        const menuItems = [
            {label: 'Search', link: '/'},
            {label: 'Admin', link: '/private'}
        ];

        return (
            <div className="container">
                <TopMenu items={menuItems}/>
                <div className="mt-2">
                    {this.props.children}
                </div>
            </div>
        );
    }
}

export default connect()(App);
