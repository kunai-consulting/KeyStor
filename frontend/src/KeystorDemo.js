import React from 'react';
import './App.css';
import {Formik} from 'formik';
import * as Yup from 'yup';

class KeystorDemo extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            submission: {
                "AcceptTruliooTermsAndConditions": true,
                "CleansedAddress": false,
                "ConfigurationName": "Identity Verification",
                "ConsentForDataSources": [
                    "Visa Verification"
                ],
                "CountryCode": "AU",
                "DataFields": {
                    "PersonInfo": {
                        "DayOfBirth": 5,
                        "FirstGivenName": "J",
                        "FirstSurName": "Smith",
                        "MiddleName": "Henry",
                        "MinimumAge": 0,
                        "MonthOfBirth": 3,
                        "YearOfBirth": 1983
                    },
                    "Location": {
                        "BuildingNumber": "10",
                        "PostalCode": "3108",
                        "StateProvinceCode": "VIC",
                        "StreetName": "Lawford",
                        "StreetType": "St",
                        "Suburb": "Doncaster",
                        "UnitNumber": "3"
                    }
                }
            },
            response: '',
            isEncrypted: false
        }
    }

    handelSubmit(values, setSubmitting) {
        setTimeout(() => {
            fetch('http://localhost:8080/api/proxy', {
                method: 'POST',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json',
                    'connection-url': 'https://gateway.trulioo.com/trial/verifications/v1/verify',
                    'x-trulioo-api-key': 'febd78a9ce526e7a367b5601d1a7fe42',
                    'decryption-regex0': '(?<="FirstSurName":").*?(?=")',
                    'decryption-type0': 'generic',
                },
                body: JSON.stringify(this.state['submission'])
            }).then(response => response.json()).then(data => {
                this.setState({
                    submission: this.state.submission,
                    response: data,
                });
                setSubmitting(false);
            })
                .catch(error => {
                    console.log(error)
                    setSubmitting(false);
                })
        }, 0);
    }

    handelEncrypt(value) {
        fetch('http://localhost:8082/api/encrypt?type=generic', {
            method: 'POST',
            headers: {
                'Accept': '*/*'
            },
            body: this.state.submission['DataFields']['PersonInfo']['FirstSurName']
        }).then(response => response.text()).then(data => {
            this.handelChangeLastName(data, true);
        })
            .catch(error => {
                console.log(error)
            })
    }

    handelChangeLastName(value, isEncrypted) {
        const submission = this.state.submission;
        submission['DataFields']['PersonInfo']['FirstSurName'] = value;
        this.setState({
            submission: submission,
            response: this.state.response,
            isEncrypted: isEncrypted,
        });
    }


    render() {
        let responseStr = JSON.stringify(this.state['response'], null, 2);
        let matchStr = '';
        let responseText = '';
        if (responseStr.length > 2) {
            responseText = "Response From Trulioo:"
            if (responseStr.includes("nomatch")) {
                matchStr = "No Match";
            }
            else {
                matchStr = "Match";
            }
        }
        else {
            responseStr = '';
        }
        return (
            <div className="app">
                <h1>
                    {' '}
                    <a href="https://github.com/kunai-consulting/KeyStor" target="_blank" rel="noopener">
                        Keystor
                    </a>{' '}
                    Demo
                </h1>

                <Formik
                    initialValues={{lastName: 'Smith'}}
                    onSubmit={(values, {setSubmitting}) => this.handelSubmit(values, setSubmitting)}
                    validationSchema={Yup.object().shape({
                        lastName: Yup.string()
                            .required('Required'),
                    })}
                >
                    {props => {
                        const {
                            touched,
                            errors,
                            isSubmitting,
                            isEncrypting,
                            handleBlur,
                            handleSubmit,
                        } = props;
                        return (
                            <form onSubmit={handleSubmit}>
                                <label htmlFor="email" style={{display: 'block'}}>
                                    Last Name
                                </label>
                                <input
                                    id="lastName"
                                    type="text"
                                    value={this.state.submission['DataFields']['PersonInfo']['FirstSurName']}
                                    onInput={(values) => this.handelChangeLastName(values.target.value, false)}
                                    onBlur={handleBlur}
                                    className={
                                        errors.lastName && touched.lastName ? 'text-input error' : 'text-input'
                                    }
                                />
                                {errors.lastName && touched.lastName && (
                                    <div className="input-feedback">{errors.lastName}</div>
                                )}

                                <button
                                    type="button"
                                    className="outline"
                                    onClick={(values) => {
                                        this.handelChangeLastName("Smith", false);
                                        this.setState( {
                                            submission: this.state.submission,
                                            response: '',
                                            isEncrypted: false,
                                        })
                                    }}
                                    disabled={isSubmitting || isEncrypting || !this.state.isEncrypted}
                                >
                                    Reset
                                </button>
                                <button
                                    type="button"
                                    disabled={isEncrypting || isSubmitting || this.state.isEncrypted}
                                    onClick={(values) => this.handelEncrypt(values)}
                                >
                                    Encrypt
                                </button>

                                <div style={{margin: '1rem 0'}}>
                                    <h3 style={{fontFamily: 'monospace'}}/>
                                    <pre
                                        style={{
                                            background: '#f6f8fa',
                                            fontSize: '.65rem',
                                            padding: '.5rem',
                                        }}
                                    >
      <strong>
          {this.state.isEncrypted ? 'What gets sent to the main data center: ' : 'Collected on the client from the user: '}
      </strong>{' '}
                                        {JSON.stringify(this.state['submission'], null, 2)}
    </pre>
                                </div>
                                <button type="submit" disabled={isSubmitting || isEncrypting || !this.state.isEncrypted}>
                                    Send to Trulioo
                                </button>
                                <div style={{margin: '1rem 0'}}>
                                    <h3 style={{fontFamily: 'monospace'}}/>
                                    <pre
                                        style={{
                                            background: '#f6f8fa',
                                            fontSize: '.65rem',
                                            padding: '.5rem',
                                        }}
                                    >
      {responseText}{' '}<strong>{matchStr}{' '}</strong>
                                        {responseStr}
    </pre>
                                </div>
                            </form>
                        );
                    }}
                </Formik>

            </div>);
    }
}

export default KeystorDemo;
