import React, { Component } from 'react';
import logo from './logo.svg';
import './App.css';

function formatName(user) {
  return user.firstName + ' ' + user.lastName;
}

const user = {
  firstName: 'Harper',
  lastName: 'Perez'
};

const element = (
  <h1>
    Hello, {formatName(user)}!  </h1>
);

// component as a f-n
function LevelsSelect(props) {
return (
  <div>
  <label>Include message level and above
    <select> {props.codes.map(l=><option value="{l}">{l}</option>)} </select>
  </label>
  <input type="submit" value="Submit" />
  </div>
);
}

// component as class w state & lifecycle
class MessagesList extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      error: null,
      isLoaded: false,
      items: []
    };
  }

  componentDidMount() {
    fetch('http://localhost:8081/consumer/messages')
      .then(res => res.json())
      .then(
        (result) => {
          this.setState({
            isLoaded: true,
            items: result
          });
        },
        (error) => {
          this.setState({
            isLoaded: true,
            error
          });
        }
      )
  }

  render() {
    const { error, isLoaded, items } = this.state;
    if (error) {
      return <div>Error: {error.message}</div>;
    } else if (!isLoaded) {
      return <div>Loading...</div>;
    } else {
      return (
        <table>
          {items.map(msg => (
            <tr>
              <td>{msg.timestamp}</td>
              <td>{msg.senderCode}: {msg.message}</td>
            </tr>
          ))}
        </table>
      );
    }
  }
}




function App() {
  return (
    <div className="App">
      <header className="App-header">
        <img src={logo} className="App-logo" alt="logo" />
	{element}
        <LevelsSelect codes={[  'LOW', 'MEDIUM', 'HIGH', 'SECRET' ].reverse()} />
        <MessagesList />
      </header>
    </div>
  );
}

export default App;
