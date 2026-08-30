import { useState } from 'react';
import './counterComponent.css'

function CounterComponent() {
  const[counter,setCounter] = useState(0)
   function incrementCounter(event) {
    event.preventDefault();
     setCounter(counter+1);
   }
    function decrementCounter(event) {
        event.preventDefault();
     setCounter(counter-1);
   }
   function resetCounter(event) {
    event.preventDefault();
    setCounter(0);
   }
return (

     <>
       <h1 align='center'> Counter Application</h1>
       <button type="submit" value="increment" onClick={incrementCounter}>increment</button>
       <button type="submit" value="decrement" onClick={decrementCounter}>decrement</button>
       <button type="submit" value="reset" onClick={resetCounter}>reset</button>
       <h2 style = {{color:counter >0? 'Green' : counter < 0 ? 'red' : 'gray'}}>{counter}</h2>
    </>
)
   
}

export default CounterComponent;