import React from 'react';
import StyledBullet from '../Shared/StyledBullet/StyledBullet';
import './SecureArchitecture.scss';

const rowClass = "row";
const colClass = "col-12 content-chunk";
const twoColClass = "col-lg-6 col-xs-12 content-chunk";

const SecureArchitecture = () => (
  <div className="secure-arch">
    <div className={rowClass}>
      <div className={colClass}>
        <h2 className="secure-arch__title">KEYSTOR SECURE ARCHITECTURE</h2>
      </div>
    </div>
    <div className={rowClass}>
      <div className={twoColClass}>
        <p className="secure-arch__text">Its public facing API only knows how to encrypt data. Its private API is only accessible by the business logic that needs it and only knows how to decrypt data as it is being sent to a specific external end points and, if needed, encrypt data as it is passed back from a specific endpoint. Simple AES encryption with a single key. Amazon KMS Keys with envelope encryption. HP Voltage (stateless tokenization).</p>
      </div>
      <div className={twoColClass}>
        <div className="secure-arch__bullets">
          <StyledBullet className="secure-arch__bullet">Reduces your attack surface</StyledBullet>
          <StyledBullet className="secure-arch__bullet">Greatly increases the security of sensitive data through micro-segmentation</StyledBullet>
          <StyledBullet className="secure-arch__bullet">Simplifies any security and compliance audits</StyledBullet>
          <StyledBullet className="secure-arch__bullet">Frees your business logic from being responsible for protecting data</StyledBullet>
        </div>
      </div>
    </div>
  </div>
);

export default SecureArchitecture;