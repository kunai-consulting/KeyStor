import React from 'react';
import './Footer.scss';

class Footer extends React.Component {
    render() {
        return (
            <footer className="footer">
              <div className="row">
                <div className="offset-2 col-8">
                  <div className="footer__text">
                    © 2019 KUNAI All Rights Reserved.
                  </div>
                </div>
              </div>
            </footer>
        )
    }
}

export default Footer;
