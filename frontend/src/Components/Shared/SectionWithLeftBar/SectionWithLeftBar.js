import React from 'react';
import classNames from 'classnames';
import { Colors } from '../constants';
import './SectionWithLeftBar.scss';

class SectionWithLeftBar extends React.Component {
  render() {
    const sectionClass = classNames(
      "row section",
      {"section--red": (this.props.sectionBackgroundColor === Colors.red)},
      {"section--blue": (this.props.sectionBackgroundColor === Colors.blue)},
      {"section--peach": (this.props.sectionBackgroundColor === Colors.peach)},
      {"section--white": (this.props.sectionBackgroundColor === Colors.white)}
    );

    const leftColClass = classNames(
      "col-xs-0 col-md-1",
      {"section-left-col--red": (this.props.barColor === Colors.red)},
      {"section-left-col--blue": (this.props.barColor === Colors.blue)},
      {"section-left-col--peach": (this.props.barColor === Colors.peach)},
      {"section-left-col--white": (this.props.barColor === Colors.white)}
    );

    return (
        <section className={sectionClass}>
          <div className={leftColClass}></div>
          <div className="offset-md-1 col-md-8 col-xs-12">
            <div className="section-content">
              {this.props.children}
            </div>
          </div>
        </section>
    )
  }
}

export default SectionWithLeftBar;
