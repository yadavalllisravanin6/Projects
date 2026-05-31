import { useState } from "react";

function LibraryItemComponent({ data }) {
  const [searchTerm, setSearchTerm] = useState("");

  // Find the book based on the user's input
  const foundBook = data.find((book) => book.title.toLowerCase() === searchTerm.toLowerCase());
   function handleSearchTermChange(event) {
    setSearchTerm(event.target.value);
   }
  return (
    <div>
      <h2>Find a Book</h2>
      
      <input
        type="text"
        placeholder="Enter book title"
        value={searchTerm}
        onChange={handleSearchTermChange}
      />

      {searchTerm && (
        <div style={{ marginTop: "20px" }}>
          {foundBook ? (
            <div>
              <p><strong>Title:</strong> {foundBook.title}</p>
              <p><strong>Type:</strong> {foundBook.type}</p>
              <p><strong>Description:</strong> {foundBook.description}</p>
            </div>
          ) : (
            <p>No book found with that title.</p>
          )}
        </div>
      )}
    </div>
  );
}

export default LibraryItemComponent;
