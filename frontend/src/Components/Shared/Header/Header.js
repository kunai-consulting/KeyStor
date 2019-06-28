import React from 'react';
import kunaiLogo from '../../../Assets/imgs/kunai-logo.svg';
import './Header.scss';

class Header extends React.Component {
  render() {
    return (
      <header className="header">
        <a href="https://kun.ai" target="__blank">
          <div className="header__logo">
            <img alt="logo" className="header__logo__img" src={kunaiLogo} />
          </div>
        </a>
        <div className="row">
          <div className="col-md-1 col-xs-0">
            <div className="header__menu"></div>
          </div>
        </div>
      </header>
    )
  }
}

export default Header;
