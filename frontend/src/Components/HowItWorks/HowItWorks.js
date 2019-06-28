import React from 'react';
import "react-responsive-carousel/lib/styles/carousel.min.css";
import { Carousel } from 'react-responsive-carousel';
import keystorSlide1 from '../../Assets/imgs/key-stor-graphic-1.jpg';
import keystorSlide2 from '../../Assets/imgs/key-stor-graphic-2.jpg';
import keystorSlide3 from '../../Assets/imgs/key-stor-graphic-3.jpg';
import keystorSlide4 from '../../Assets/imgs/key-stor-graphic-4.jpg';
import './HowItWorks.scss';

const rowClass = "row";
const slideRowClass = "row slide__content";
const colClass = "col-12 content-chunk";

class HowItWorks extends React.Component {
  constructor() {
    super();
    this.state = {
      shouldAutoplay: false
    }
  }

  onMouseEnter = () => {
    console.log('onMouseEnter');
    this.setState({shouldAutoplay: true});
  }

  render() {
    const onMouseEnterFunc = (this.state.shouldAutoplay ? () => {} : this.onMouseEnter);
    return (
      <div className="how-it-works">
        <div className={rowClass}>
          <div className={colClass}>
            <h2 className="how-it-works__title">HOW IT WORKS</h2>
          </div>
        </div>
        <div className={rowClass} onMouseEnter={onMouseEnterFunc}>
          <div className={colClass}>
            <Carousel
              autoPlay={this.state.shouldAutoplay}
              interval={6000}
              transitionTime={0}
              showArrows={false}
              showStatus={false}
              showThumbs={false}
              infiniteLoop={true}>
              <div className={slideRowClass}>
                <div className="col-lg-5 col-md-12 slide__content__left">
                  <p>1.</p>
                  <p>Sensitive data gets collected. It gets encrypted on the device before it goes to the servers.</p>
                </div>
                <div className="col-lg-7 col-md-12 ">
                  <img alt="slide-1" className="slide__content__img" src={keystorSlide1} />
                </div>
              </div>
              <div className={slideRowClass}>
                <div className="col-lg-5 col-md-12 slide__content__left">
                  <p>2.</p>
                  <p>Now, the only secret data you have in your servers with your app is encrypted. The business logic can only read the non-secret part of the data. The encryption keys are not accessible by the main data center.</p>
                </div>
                <div className="col-lg-7 col-md-12">
                  <img alt="slide-2" className="slide__content__img" src={keystorSlide2} />
                </div>
              </div>
              <div className={slideRowClass}>
                <div className="col-lg-5 col-md-12 slide__content__left">
                  <p>3.</p>
                  <p>When the business logic needs to send this data (e.g. to do KYC validation), it talks to KeyStor which decrypts the data and then talks to the external endpoint.</p>
                </div>
                <div className="col-lg-7 col-md-12">
                  <img alt="slide-3" className="slide__content__img" src={keystorSlide3} />
                </div>
              </div>
              <div className={slideRowClass}>
                <div className="col-lg-5 col-md-12 slide__content__left">
                  <p>4.</p>
                  <p>The end point’s response is sent back to the main data center by KeyStor, after any sensitive data has been encrypted.</p>
                </div>
                <div className="col-lg-7 col-md-12">
                  <img alt="slide-4" className="slide__content__img" src={keystorSlide4} />
                </div>
              </div>
            </Carousel>
          </div>
        </div>
      </div>
    )
  }
}

export default HowItWorks;