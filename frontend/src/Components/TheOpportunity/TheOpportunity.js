import React from 'react';
import './TheOpportunity.scss';

const rowClass = "row";
const colClass = "col-12 content-chunk";

const TheOpportunity = () => (
  <div className="the-opportunity">
    <div className={rowClass}>
      <div className={colClass}>
        <h1 className="the-opportunity__title">KeyStor</h1>
      </div>
    </div>
    <div className={rowClass}>
      <div className={colClass}>
        <h2 className="the-opportunity__subtitle">THE OPPORTUNITY</h2>
      </div>
    </div>
    <div className={rowClass}>
      <div className={colClass}>
        <h1 className="the-opportunity__content-title">Companies want to modernize and optimize customer acquisition and onboarding.</h1>
      </div>
    </div>
    <div className={rowClass}>
      <div className={colClass}>
        <hr />
      </div>
    </div>
    <div className={rowClass}>
      <div className={colClass}>
        <p>Onboarding and validating customers means collecting and storing sensitive data. Even if using that data is not part of your core business, you have a security compliance obligation if you store PCI and PII information. As your company grows you store more user data in more centralized ways. Your compliance scope and risk increases as you become more efficient and modernize your business technologies.</p>
        <p><strong>Know Your Customer (KYC)</strong> onboarding and validation processes are often a combination of manual-analog and digital processes. There are many KYC and AML providers. You need to handle this data securely. To digitize this process, you need a <strong>secure solution</strong> that can integrate with any KYC or AML vendor (back end), and provides a customizable digital on-boarding experience (front end) for customers.</p>
      </div>
    </div>
  </div>
);

export default TheOpportunity;
