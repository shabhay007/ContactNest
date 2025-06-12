console.log("Contacts.js");


// const baseURL = "http://localhost:8082"; // your baseURL
const baseURL = "http://contactnest.ap-southeast-1.elasticbeanstalk.com"; // your baseURL

// Bootstrap modal setup
const contactModal = new bootstrap.Modal(document.getElementById("viewContactModal"));

function openContactModal() {
  contactModal.show();
}

function closeContactModal() {
  // Ensure no element is focused when modal is hiding
  if (document.activeElement) {
    document.activeElement.blur();
  }

  contactModal.hide();
}

async function loadContactdata(id) {
  // console.log("Loading contact with id:", id);

  try {
    const response = await fetch(`${baseURL}/api/contacts/${id}`);
    const data = await response.json();
    // console.log(data);

    document.querySelector("#contact_name").textContent = data.name;
    document.querySelector("#contact_email").textContent = data.email;
    document.querySelector("#contact_image").src = data.picture || 'fallback-img-url';
    document.querySelector("#contact_address").textContent = data.address;
    document.querySelector("#contact_phone").textContent = data.phoneNumber;
    document.querySelector("#contact_about").textContent = data.description;

    const favoriteElem = document.querySelector("#contact_favorite");
    if (data.favourite) {
      favoriteElem.innerHTML = "★ ★ ★ ★ ★";
    } else {
      favoriteElem.textContent = "Not Favorite Contact";
    }

    const websiteLink = document.querySelector("#contact_website");
    websiteLink.href = data.websiteLink;
    websiteLink.textContent = data.websiteLink;

    const linkedInLink = document.querySelector("#contact_linkedIn");
    linkedInLink.href = data.linkedinLink;
    linkedInLink.textContent = data.linkedinLink;

    openContactModal();

  } catch (error) {
    console.error("Error loading contact:", error);
  }
}

async function deleteContact(id) {
  Swal.fire({
    title: "Do you want to delete the contact?",
    icon: "warning",
    showCancelButton: true,
    confirmButtonText: "Delete",
  }).then((result) => {
    if (result.isConfirmed) {
      const url = `${baseURL}/user/contacts/delete/${id}`;
      window.location.replace(url);
    }
  });
}
