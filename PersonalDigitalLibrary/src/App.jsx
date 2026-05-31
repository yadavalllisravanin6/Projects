import { useState } from 'react'
import AddItemComponent from './AddItemComponent'
import LibraryListItemComponent from './LibraryListItemComponent'
import './App.css'
import LibraryItemComponent from './LibraryItemComponent';

function App() {

  const [library,setLibrary] = useState([]);
  const handleBookArray = (book) =>{
       setLibrary([...library,book]);
  };
  
  return (
    <>
      <AddItemComponent onAddBook={handleBookArray} />
      <LibraryListItemComponent data={library}/>
      <LibraryItemComponent data={library}/>
    </>
  )
}

export default App
