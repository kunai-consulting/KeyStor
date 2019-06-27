import React from 'react';
import bulletImg from '../../../Assets/imgs/bullet-point-red.svg';
import './StyledBullet.scss';

const StyledBullet = ({ children, className }) => (
  <div className={`styled-bullet ${className}`}>
    <img alt="bullet-img" className={`${className}__img`} src={bulletImg} />
    <div className={`${className}__text`}>{children}</div>
  </div>
);

export default StyledBullet;