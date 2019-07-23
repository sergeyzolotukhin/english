import React from 'react';
import {connect} from 'react-redux';

const mapStateToProps = (state) => ({
    ...state
});

const mapDispatchToProps = {

};

const privateRoute = (Wrapped) => connect(mapStateToProps, mapDispatchToProps)(
    class extends React.Component {
        render() {
            const {loading} = this.props;
            if (loading) {
                return (
                    <div className="center loader">
                        <div>Loading...</div>
                    </div>
                );
            }

            return <Wrapped {...this.props} />;
        }
    }
);

export default privateRoute;
