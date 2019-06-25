import React from 'react';
import Header from '../Shared/Header/Header';
import Footer from '../Shared/Footer/Footer';
import SectionWithLeftBar from '../Shared/SectionWithLeftBar/SectionWithLeftBar';
import { Colors } from '../Shared/constants';
import KeystorDemo from '../KeystoreDemo/KeystorDemo';
import './App.scss';

class App extends React.Component {
  render() {
    return (
      <body>
        <Header />
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
