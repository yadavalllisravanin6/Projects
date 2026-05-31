import { useState } from 'react';
import './AnimalsList.css';

function AnimalListComponent() {
    const [animal,setAnimal] = useState('');
      const [animalsList, setAnimalsList] = useState([]);
    function addAnimal(event) {
         event.preventDefault();
         if(!animal.trim()){
            alert("plase enter any animal");
            return;
         }
         setAnimalsList([...animalsList, animal]);
       setAnimal("");
    }
    return (
        <>
          <h1 className="animalsList">List Of Animals</h1>
           <form onSubmit={addAnimal} className="input-section">
          <input type="text" placeholder='Animal' value={animal} onChange={ (e)=> setAnimal(e.target.value) }/>
          <button type="submit" value="submit">Submit</button>
          </form>

          {animalsList.map((ani,index) =>(
          
            <h4 style = {{ color:index %2 == 0 ? 'red': 'pink'}}>{ani.toUpperCase()}</h4>
          ))}
        </>
    )
}

export default AnimalListComponent;