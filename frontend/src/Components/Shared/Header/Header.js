import React from 'react';
import kunaiLogo from '../../../Assets/imgs/kunai-logo.svg';
import cubes4 from '../../../Assets/imgs/cubes-4.png';
import menu from '../../../Assets/imgs/menu.svg';
import './Header.scss';

class Header extends React.Component {
  render() {
    return (
      <header className="header">
        <img className="header__logo" src={kunaiLogo} />
        <div className="row">
          <div className="col-md-1 col-xs-0">
            <img className="header__menu" src={menu} />
          </div>
        </div>
      </header>
    )
  }
}

export default Header;
