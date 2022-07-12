function clearMain() {
  document.getElementById("main").innerHTML = "";
}

//Creating HTML Elements for different pages
function createUserPortal() {
  var userPortal = document.createElement("div");

  userPortal.innerHTML =
    "<h2> How is it going today? </h2>" +
    "<form >" +
    '<div class="form-group">' +
    '<label for="title">Title</label>' +
    '<input type="text" class="form-control " id="title" name="title" placeholder="Title" required="">' +
    "</div>" +
    '<div class="form-group">' +
    '<label for="entry">Entry</label>' +
    '<textarea class="form-control " id="entry" name="entry" required="" rows="3"></textarea>' +
    "</div>" +
    '<button id="submitBtn" class="btn btn-dark mt-3">Submit</button>';

  document.getElementById("main").append(userPortal);
}

function createAddNewEntryButton() {
  var addNewEntryButton = document.createElement("div");

  addNewEntryButton.innerHTML =
    '<button onClick="getUserPortal()" class="btn btn-dark my-3" type="button"> Add a new Entry</button>';

  document.getElementById("main").appendChild(addNewEntryButton);
}

function createEntryElement(id, title, entry, createDate, modifyDate) {
  var entryDiv = document.createElement("div");

  entryDiv.innerHTML =
    '<div class="col">' +
    '<div class="card-deck" >' +
    '<div class="card my-2" ">' +
    '<div class="card-body " id="' +
    id +
    '" >' +
    '<h3 class="card-title" >' +
    title +
    "</h3>" +
    '<p class="card-text" >' +
    entry +
    "</p>" +
    '<div class="container d-flex justify-content-end">' +
    '<p class="card-text mx-3 "><small class="text-muted">' +
    createDate +
    "</small></p>" +
    '<p class="card-text "><small class="text-muted">' +
    modifyDate +
    "</small></p>" +
    "</div>" +
    '<button type="submit" class="btn btn-dark deleteBtn">Delete</button>' +
    '<button type="submit" class="btn btn-dark editBtn"> Edit</button>' +
    "</div>" +
    "</div>" +
    "</div>";

  document.getElementById("main").appendChild(entryDiv);
}

function createEditEntryElement(title, entry, id) {
  var editEntryDiv = document.createElement("div");

  editEntryDiv.innerHTML =
    "<h1> Your current entry is: </h1>" +
    '<form id="' +
    id +
    '">' +
    ' <div class="form-group">' +
    '<label for="title">Title</label>' +
    '<textarea class="form-control " id="title" name="title" rows="1" placeholder="">' +
    title +
    "</textarea>" +
    " </div>" +
    ' <div class="form-group">' +
    '<label for="entry">Entry</label>' +
    '<textarea class="form-control " id="entry" name="entry" rows="15" placeholder="">' +
    entry +
    "</textarea>" +
    "</div>" +
    '<button id="submitBtnEdit" type="submit" class="btn btn-dark ">Submit</button>';

  document.getElementById("main").appendChild(editEntryDiv);
}

//For transitioning between the pages
async function getUserPortal() {
  await $(main).fadeOut(600).promise();
  clearMain();
  createUserPortal();
  setTimeout(addSubmitBtnEventListener, 200);
  $(main).fadeIn(600);
}

async function getAllEntries() {
  await $(main).fadeOut(600).promise();
  clearMain();
  $(document).ready(function () {
    $.ajax({
      url: "/getEntries",
      type: "GET",
      success: function (data) {
        createAddNewEntryButton();
        for (var i = 0; i < data.length; i++) {
          var entry = data[i];
          createEntryElement(
            entry.entry_id,
            entry.title,
            entry.entry,
            entry.createDate,
            entry.modifyDate
          );
        }
      },
    });
    setTimeout(addDeleteAndEditBtnEventListener, 100);
  }, $(main).fadeIn(600));
}

async function getEntry(id) {
  await $(main).fadeOut(600).promise();
  clearMain();
  $(document).ready(function () {
    $.ajax({
      url: "/getEntry/" + id,
      type: "GET",
      success: function (data) {
        var title = data.title;
        var entry = data.entry;
        createEditEntryElement(title, entry, id);
      },
    });
    setTimeout(addSubmitBtnEditEventListener, 100);
  }, $(main).fadeIn(600));
}

async function removeEntry(id) {
  $(document).ready(function () {
    $.ajax({
      url: "/removeEntry/" + id,
      type: "GET",
      success: function (data) {
        getAllEntries();
      },
    });
  });
}

async function addEntry(title, entry) {
  $(document).ready(function () {
    $.ajax({
      url: "/addEntry/",
      type: "POST",
      data: JSON.stringify({ title: title, entry: entry }),
      contentType: "application/json",
      success: function (data) {
        getAllEntries();
      },
    });
  });
}

async function updateEntry(title, entry, id) {
  $(document).ready(function () {
    $.ajax({
      url: "/updateEntry/" + id,
      type: "POST",
      data: JSON.stringify({ title: title, entry: entry }),
      contentType: "application/json",
      success: function (data) {
        getAllEntries();
      },
    });
  });
}



//Event Listeners
function addDeleteBtnEventListener() {

  const deleteBtns = document.getElementsByClassName("deleteBtn");

  for (let i = 0; i < deleteBtns.length; i++) {

    deleteBtns[i].addEventListener("click", function (e) {
      e.preventDefault();
      if (confirm("Are you sure you want to delete this entry?")) {
        var id = e.target.parentElement.id;
        removeEntry(id);
      }
    });

  }
}

//Submit Btn for Single Entry Page
function addSubmitBtnEventListener() {

  const submitBtn = document.getElementById("submitBtn");

  submitBtn.addEventListener("click", function (e) {
    e.preventDefault();
    var title = document.getElementById("title").value;
    var entry = document.getElementById("entry").value;
    addEntry(title, entry);
  });

}

//Submit Btn for Edit Page -> I know I'm ashamed of the naming as well
function addSubmitBtnEditEventListener() {

  const submitBtnEdit = document.getElementById("submitBtnEdit");

  submitBtnEdit.addEventListener("click", function (e) {
    e.preventDefault();
    var title = document.getElementById("title").value;
    var entry = document.getElementById("entry").value;
    var id = e.target.parentElement.id;
    updateEntry(title, entry, id);
  });

}

//Edit Button on All Journals
function addEditBtnEventListener() {

  const editBtns = document.getElementsByClassName("editBtn");

  for (let i = 0; i < editBtns.length; i++) {

    editBtns[i].addEventListener("click", function (e) {
      e.preventDefault();
      var id = e.target.parentElement.id;
      getEntry(id);
    });

  }

}

function addDeleteAndEditBtnEventListener() {
  addDeleteBtnEventListener();
  addEditBtnEventListener();
}

//Run at the beginning of render
addSubmitBtnEventListener();
