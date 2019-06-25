import React from 'react';
import './Benefits.scss';

const rowClass = "row";
const colClass = "col-12 content-chunk";
const twoColClass = "col-lg-6 col-xs-12 content-chunk";

const Benefits = () => (
  <div className="benefits">
    <div className={rowClass}>
      <div className={colClass}>
        <h2 className="benefits__subtitle">BENEFITS</h2>
      </div>
    </div>
    <div className={rowClass}>
      <div className={twoColClass}>
        <h1 className="benefits__title">Companies who adopt this solution will improve operations workflow to become more streamlined as a result.</h1>
      </div>
      <div className={twoColClass}>
        <div className="benefits__info-tile">
          <h3 className="benefits__info-tile__title">Secure User Data</h3>
          <p className="benefits__info-tile__text">Use encryption and a Secure Architecture.</p>
        </div>
        <div className="benefits__info-tile">
          <h3 className="benefits__info-tile__title">Compliance & Risk</h3>
          <p className="benefits__info-tile__text">Reduce Audit scope and mitigate risk for PCI, PII, with partner integration.</p>
        </div>
        <div className="benefits__info-tile">
          <h3 className="benefits__info-tile__title">Own your UX</h3>
          <p className="benefits__info-tile__text">Your user experience should be optimized and customizable based on your domain experience. Don't rebuild your app if you have to switch KYC/AML vendors, or if you have them in different geo's.</p>
        </div>
      </div>
    </div>
  </div>
);

export default Benefits;