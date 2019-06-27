import React from 'react';
import Header from '../Shared/Header/Header';
import Footer from '../Shared/Footer/Footer';
import cubes1 from '../../Assets/imgs/cubes-1.png';
import cubes2 from '../../Assets/imgs/cubes-2.png';
import cubes4 from '../../Assets/imgs/cubes-4.png';
import cubes5 from '../../Assets/imgs/cubes-5.png';
import SectionWithLeftBar from '../Shared/SectionWithLeftBar/SectionWithLeftBar';
import { Colors } from '../Shared/constants';
import TheOpportunity from '../TheOpportunity/TheOpportunity';
import Benefits from '../Benefits/Benefits';
import Capabilities from '../Capabilities/Capabilities';
import SecureArchitecture from '../SecureArchitecture/SecureArchitecture';
import HowItWorks from '../HowItWorks/HowItWorks';
import KeystorDemo from '../KeystoreDemo/KeystorDemo';
import './App.scss';

class App extends React.Component {
  render() {
    return (
      <body>
        <Header />
        <img alt="cubes" className="cubes cubes-1" src={cubes4} />
        <img alt="cubes" className="cubes cubes-2" src={cubes5} />
        <img alt="cubes" className="cubes cubes-3" src={cubes2} />
        <img alt="cubes" className="cubes cubes-4" src={cubes1} />
        <img alt="cubes" className="cubes cubes-5" src={cubes1} />
        <img alt="cubes" className="cubes cubes-6" src={cubes5} />
        <img alt="cubes" className="cubes cubes-7" src={cubes1} />
        <img alt="cubes" className="cubes cubes-8" src={cubes1} />
        <img alt="cubes" className="cubes cubes-9" src={cubes1} />
        <img alt="cubes" className="cubes cubes-10" src={cubes5} />
        <SectionWithLeftBar
          barColor={Colors.white}
          sectionBackgroundColor={Colors.blue}
        >
          <TheOpportunity />
        </SectionWithLeftBar>
        <SectionWithLeftBar
          barColor={Colors.peach}
          sectionBackgroundColor={Colors.white}
        >
          <Benefits />
        </SectionWithLeftBar>
        <SectionWithLeftBar
          barColor={Colors.blue}
          sectionBackgroundColor={Colors.peach}
        >
          <Capabilities />
        </SectionWithLeftBar>
        <SectionWithLeftBar
          barColor={Colors.white}
          sectionBackgroundColor={Colors.blue}
        >
          <SecureArchitecture />
        </SectionWithLeftBar>
        <SectionWithLeftBar
          barColor={Colors.peach}
          sectionBackgroundColor={Colors.white}
        >
          <HowItWorks />
        </SectionWithLeftBar>
        <SectionWithLeftBar
          barColor={Colors.white}
          sectionBackgroundColor={Colors.blue}
        >
          <KeystorDemo />
        </SectionWithLeftBar>
        <Footer />
      </body>
    )
  }
}

export default App;
