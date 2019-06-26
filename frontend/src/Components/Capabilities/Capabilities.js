import React from 'react';
import StyledBullet from '../Shared/StyledBullet/StyledBullet';
import './Capabilities.scss';

const rowClass = "row";
const colClass = "col-12 content-chunk";
const twoColClass = "col-lg-6 col-xs-12 content-chunk";

const Capabilities = () => (
  <div className="capabilities">
    <div className={rowClass}>
      <div className={colClass}>
        <h2 className="capabilities__subtitle">CAPABILITIES</h2>
      </div>
    </div>
    <div className={rowClass}>
      <div className={twoColClass}>
        <StyledBullet className="capabilities__main-bullet">Know Your Customer (KYC)</StyledBullet>
        <StyledBullet className="capabilities__main-bullet">Anti-Money Laundering (AML)</StyledBullet>
        <StyledBullet className="capabilities__main-bullet">Data Security, Cloud Security, Audit</StyledBullet>
      </div>
      <div className={twoColClass}>
        <StyledBullet className="capabilities__main-bullet">Own your UX, Customer Acquisition</StyledBullet>
        <StyledBullet className="capabilities__main-bullet">Partner Integration, PCI PII Compliance</StyledBullet>
      </div>
    </div>
    <div className={rowClass}>
      <div className={colClass}>
        <div className="capabilities__paper">
          <div className={rowClass}>
            <div className={colClass}>
              <h2 className="capabilities__paper__subtitle">USE CASE #1</h2>
              <p>Company X - Large financial services and marketing company that realized that its now competing with new, lean FinTech market entrants. Company collects sensitive user data and stores it, but acting on that data is not part of their core business.</p>
              <p>Company X has the challenge and opportunity to start acting more like a tech company in order to stay competitive. Making the whole company more tech savvy is a worthy undertaking, but won’t happen fast enough for any meaningful completion due to the speed of the competition.</p>
              <p>Solution: Hire technology consultants in the space (like Kunai) to build a competitive solution quickly, and demonstrate how this is done by the smaller fin-tech heavy start-ups. <strong>You have options for this digital transformation:</strong></p>
            </div>
          </div>
          <div className={`${rowClass} capabilities__paper__bullets`}>
            <div className={twoColClass}>
              <StyledBullet className="capabilities__paper__bullet">Do what the start-ups are doing in terms of technology approach; build what they’ve built in the way they build it. Use an Encryption Vendor and KYC vendor to layer encryption into your data that deals with compliance for PCI, PII KYC and AML- deferring audit risk and saving time on design by leveraging your vendor. Use that vendors packaged User Experience. Pick a winner and rely on that vendor.</StyledBullet>
            </div>
            <div className={twoColClass}>
              <StyledBullet className="capabilities__paper__bullet">You can buy the start-up competition.</StyledBullet>
              <StyledBullet className="capabilities__paper__bullet">Happy Medium - Build and own your user experience. Architect and own your security and compliance.  Stay agile and independent of individual vendors for external services like cloud hosting, KYC/AML, etc. Leverage existing company initiatives where applicable.</StyledBullet>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div className={rowClass}>
      <div className={colClass}>
        <div className="capabilities__paper">
          <div className={rowClass}>
            <div className={colClass}>
              <h2 className="capabilities__paper__subtitle">USE CASE #2</h2>
              <p>Small to Medium Sized Company - You collect sensitive data for your customers, but you do not act on it.</p>
              <p>Small to Medium sized Company needs to collect and validate sensitive user data for its customer acquisition and on-boarding process, but has no subsequent use for it beyond sending it to other companies. Your company will still be storing this data however, which means you now have a regulatory compliance burden and audit scope for PCI and PII data. As you grow your regulatory burden increases. You would like to limit audit scope and increase security. <strong>You want to:</strong></p>
            </div>
          </div>
          <div className={`${rowClass} capabilities__paper__bullets`}>
            <div className={twoColClass}>
              <StyledBullet className="capabilities__paper__bullet">Encrypt sensitive data at the endpoint utilizing a secure architecture.</StyledBullet>
              <StyledBullet className="capabilities__paper__bullet">Not store any sensitive (unencrypted) data on your servers thereby limiting your compliance audit scope.</StyledBullet>
            </div>
            <div className={twoColClass}>
              <StyledBullet className="capabilities__paper__bullet">Pass encrypted data to third parties who can validate it.</StyledBullet>
              <StyledBullet className="capabilities__paper__bullet">Choose a good KYC Vendor who’s verification results talk directly to your API, or are cryptographically protected.</StyledBullet>
            </div>
          </div>
          <div className={rowClass}>
            <div className={colClass}>
              <p><strong>Solutions:</strong></p>
            </div>
          </div>
          <div className={`${rowClass} capabilities__paper__bullets`}>
            <div className={twoColClass}>
              <StyledBullet className="capabilities__paper__bullet">Use an open source solution like <a href="https://github.com/kunai-consulting/KeyStor">KeyStor</a>.</StyledBullet>
            </div>
            <div className={twoColClass}>
              <StyledBullet className="capabilities__paper__bullet">Use a similar cloud based SaaS vendor like Very Good Security.</StyledBullet>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
);

export default Capabilities;