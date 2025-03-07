import './App.css';
import CalculationResult from './CalculationResult';
import { useState, useEffect } from 'react';

function App() {
  const [results, setResults] = useState(null);

  useEffect(() => {
    fetch('http://localhost:8080/api/v1/calculations', {
      method: 'GET',
    })
      .then(response => response.json())
      .then(data => data.map(result => new CalculationResult(result.result, result.startedAt, result.finishedAt, result.scheduleType, result.scheduleString)))
      .then(data => setResults(data))
      .catch(error => console.error('Error:', error));
  }, []);

  const postSchedule = () => {
    const schedule = document.getElementById('schedule-input').value;
    fetch('http://localhost:8080/api/v1/schedule', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({ scheduleString: schedule })
    })
      .then(response => console.log(response.ok ? 'Schedule added' : 'Error adding schedule'))
      .catch(error => console.error('Error:', error));
  }


  return (
    <div className="App">
      <h1>Add schedule</h1>
      <input type="text" id="schedule-input" placeholder="* * * * * * or yyyy-MM-dd HH:mm:ss, yyyy-MM-dd HH:mm:ss"/>
      <button onClick={postSchedule}>Add schedule</button>

      <h1>Calculation results</h1>
      {results !== null && results.length > 0 &&
        <table>
          <thead>
            <tr>
              <th>Result</th>
              <th>Started at</th>
              <th>Finished at</th>
              <th>Schedule type</th>
              <th>Schedule string</th>
            </tr>
          </thead>
          <tbody>
            {results.map((result, index) => (
              <tr key={index}>
                <td>{result.result}</td>
                <td>{result.startedAt}</td>
                <td>{result.finishedAt}</td>
                <td>{result.scheduleType}</td>
                <td>{result.scheduleString}</td>
              </tr>
            ))}
          </tbody>
        </table>}


      {results != null && results.length === 0 && <div id="noData">No calculation results yet.</div>}

    </div>
  );
}

export default App;
