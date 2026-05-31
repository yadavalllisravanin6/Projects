import { useState } from "react"
import './AddItem.css'
function AddItemForm({onAddBook}) {
     
    const [title,setTitle] = useState("");
    const [type,setType] = useState("");
    const [description,setDescription] = useState("");

    function handleTitleChange(e) {
        setTitle(e.target.value);
    }
    function handleTypeChange(e) {
        setType(e.target.value);
    }
    function handleDescriptionChange(e) {
        setDescription(e.target.value);
    }

    const handleSubmit = () => {
        if(title && type && description) {
           const newBook = { title, type, description };
           
           if(onAddBook) {
            onAddBook(newBook);
           }else {
            console.log("Book added:", newBook);
            }
        setTitle("");
        setType("");
        setDescription("");
        } 
    }
    return (
        <>
        <h1 className='title'>Add NewBook Form</h1>
          <input type="text" placeholder="Title" value={title} onChange={handleTitleChange}/>
          <br/>
          <br/>
          <input type="text" placeholder="Type" value={type} onChange={handleTypeChange}/>
          <br/>
          <br/>
          <input type="text" placeholder="Description" value= {description} onChange={handleDescriptionChange}/>
          <br/>
          <br/>
          <button type="submit" className="button" onClick={handleSubmit}>AddBook</button>
       </>
    );
}

export default AddItemForm;