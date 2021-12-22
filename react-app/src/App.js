import React, { Component } from 'react';
import logo from './logo.svg';
import './App.css';
import { MapContainer, TileLayer, Marker, Popup } from 'react-leaflet'

/** gather level and time interval for messages query, then emit onSubmit() with state */
class FilterForm extends React.Component {
  constructor(props) {
   super(props)
   this.state = props.initialState
  }
  
  codes=[  'LOW', 'MEDIUM', 'HIGH', null ].reverse()
  
  setLevel = (e) => {
    const code = this.codes[e.target.selectedIndex]
  	this.setState({level: code})
  }
  
  handleSliding = (e) => {
  	const value = e.target.value
    this.setState({ageMinutes:value})
  } 

  render() {
	return (
	  <div id="FilterForm">
	   <label>Include message level
	     <select onChange={this.setLevel}> {this.codes.map(l=><option key={l}>{l}</option>)} </select>
	   </label>	   
	   <p/>
	   <label className="slidecontainer">Earlier than {String(this.state.ageMinutes).padStart(2, '0')} minutes
		  <input type="range" min="1" defaultValue={this.state.ageMinutes} max="99" className="slider" onChange={this.handleSliding} />
		</label>
		<p/>
	    <button id="submit" onClick={()=>{this.props.onSubmit(this.state)}}>
	      Query for messages
	    </button>
	  </div>
	);  
  }
}

/* listing preview of messages  */
function MessagesList(props) {
    const { error, isLoaded, items } = props.loadedState;
	console.log("loaded state", error, isLoaded, items)
    if (error) {
      return <div>Messages List Error: {error.message}. Is the web service running?</div>;
    } else if (!isLoaded) {
      return <div>Loading...</div>;
    } else if (items.length == 0) {
      return <div>An empty list...has fake-observation-gen produced an event yet?</div>;    
      //return <div>An empty list</div>;    
    } else {
      return (
        <div id="MessagesList">
         {items.map(i=><label key={i.id}>{new Date(i.timestamp).toLocaleTimeString()}:{i.message}<br/></label>)}
        </div>
      );
    }
}

function Map(props) {

	function calculateCenterCoordinates(list) { // avg of extemes
		const arrayAvgOfExtremes = (coordinateLocator) => {
				if (list.length==1) return coordinateLocator(list[0])
				const sorted = list.map(coordinateLocator).sort()
				return (sorted[sorted.length-1] - sorted[0]) / 2
			}
		
		const centerLat = arrayAvgOfExtremes(msg=>msg.coordLat)
		const centerLon = arrayAvgOfExtremes(msg=>msg.coordLon)
		
		return [centerLat, centerLon]
	}
	
	let center = [58.9996,24.8093]
	let messages = []
	const st = props.loadedState
	if (st.isLoaded && !st.error && st.items.length>0) {
		messages = st.items
		center = calculateCenterCoordinates(messages)
		console.log("center at ", center)
	}
	return (
		<div id="map">
			<MapContainer id="mapcontainer" center={center} zoom={7}>
			  <TileLayer url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png" />
	          {messages.map(message => <Marker key={message.id} position={[message.coordLat, message.coordLon]} />)}
			</MapContainer>		
		</div>		
	);
}


/* UI aggregate component of form,list,map view coupled to the REST query logic
 loaded state from the REST service API is passed down the composition hierarchy,
 thus the state has been lifted up for display in multiple components.
*/
class MessagesTool extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
	  filterCriteria: props.initialFilterCriteria,
	  loadedState: {
        error: null,
        isLoaded: false,
        items: []
      }
    };
  }

  componentDidMount() {
	this.fetch(this.state.filterCriteria)
  }

  handleFilterFormSubmit = (criteria) => {
	this.fetch(criteria)
  }

  fetch(criteria) {
	console.log("#fetch", criteria)
	let opts = {
		method: 'POST',
		headers: {'Content-Type': 'application/json'},		
		body: JSON.stringify(criteria)
	}
    fetch('http://localhost:8081/consumer/messages', opts)
      .then(res => res.json())
      .then(
        (result) => {
          console.log(result.length + " messages.")
          this.setState({loadedState: {
            isLoaded: true,
            items: result
          }});
        },
        (error) => {
          this.setState({loadedState: {
            isLoaded: true,
            error
          }});
        }
      )
  }
  
  render() {
	console.log("render")
    return (
    <div className="Messages">
      <header className="App-header">
        <img src={logo} className="App-logo" alt="logo" />
        <FilterForm 
				initialState={this.state.filterCriteria} 
				onSubmit={this.handleFilterFormSubmit} />
	  </header>
      <MessagesList loadedState={this.state.loadedState} />
	  <Map loadedState={this.state.loadedState} />				    
    </div>
  );
  }
}

function App() {
  const initial = { ageMinutes : 10, level : null }
  return (
    <div className="App">
        <MessagesTool initialFilterCriteria={initial} />
    </div>
  );
}

export default App;
