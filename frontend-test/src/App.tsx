
import Message from "./Message";
import ListGroup from "./components/ListGroup"
import Alert from "./components/Alert";
import Button from "./components/Button";
import {useEffect, useState} from "react";
function App() {
  const [todos, setTodos] = useState([])
  const [error, setError] = useState({})

  useEffect(() => {
     fetch('https://jsonplaceholder.typicode.com/todos')
         .then(respone => respone.json())
         .then(res => setTodos(res.slice(0,10)))
         .catch(err => setError(err))
  })

  let data: string = "Data"

  const mapTodosToList = todos.map(todo => <li>{todo}</li>)

  const getData = () =>
      fetch('https://jsonplaceholder.typicode.com/todos')
      .then(respone => respone.json())
      .then(res => console.log(res))

  return <div>
    <Button onClick={getData} color={"secondary"}>
      {data}
    </Button>
    <ul>
      {mapTodosToList}
    </ul>
  </div>

  /*
  return <div>
    <Alert>
      <span>Hello World</span>
    </Alert>
  </div>*/

  /*
  let items =  [
    'a',
    'b',
    'c'
  ];

  const handleSelectItem = (item: string) => {
    console.log(item);
  }

  return <div><ListGroup items={items} heading="Cities" onSelectItem={handleSelectItem}/></div>
  */
}

export default App;