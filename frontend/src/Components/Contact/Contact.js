import React from 'react';
import './Contact.scss';

const rowClass = "row";
const colClass = "col-12 content-chunk";
const twoColClass = "col-lg-6 col-xs-12 content-chunk";

const Contact = () => (
  <div className="contact">
    <div className={rowClass}>
      <div className={colClass}>
        <p className="contact__text">Let's continue the conversation on how we can make technology work for you.</p>
        <a href="https://kun.ai/#contact" target="__blank">
          <button className="button-blue contact__button">CONTACT US</button>
        </a>
      </div>
    </div>
  </div>
);

export default Contact;